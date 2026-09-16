package com.example.muc8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class represents the main activity of the application.
 * It manages UI components and interactions related to heart rate and oxygen saturation monitoring.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MAX_ENTRIES = 30;

    // UI components
    private TextView heartRateTextView;
    private TextView satTextView;
    private Button startButton, stopButton, viewDataButton, optionsButton;
    private LineChart lineChart;

    // Data management
    private List<Entry> entries;
    private DatabaseHelper databaseHelper;
    private Handler resetHandler = new Handler();
    private MqttManager mqttManager;
    private ChartManager chartManager;
    private SoundManager soundManager;

    // User-specific data
    private String username;
    private int entryCount = 0;
    private float latestSpo2Value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        heartRateTextView = findViewById(R.id.heart_rate);
        satTextView = findViewById(R.id.spo2);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        viewDataButton = findViewById(R.id.view_data_button);
        optionsButton = findViewById(R.id.options_button);
        lineChart = findViewById(R.id.lineChart);

        // Initialize data structures
        entries = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        mqttManager = new MqttManager(this, this::handleMqttMessage);
        chartManager = new ChartManager(lineChart, entries);
        soundManager = new SoundManager(this);

        // Set up button click listeners
        startButton.setOnClickListener(v -> connect());
        stopButton.setOnClickListener(v -> disconnect());
        viewDataButton.setOnClickListener(v -> showSavedData());
        optionsButton.setOnClickListener(v -> openOptionsActivity());

        // Set up the chart display
        chartManager.setupLineChart();
    }

    /**
     * Connect to the MQTT broker and start receiving data.
     */
    private void connect() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");

        if (savedUsername.isEmpty()) {
            // Redirect to OptionsActivity if username is not saved
            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
        } else {
            // Use saved username and connect to MQTT broker
            username = savedUsername;
            mqttManager.connectToMqttBroker();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show());
    }

    /**
     * Disconnect from MQTT broker and stop data reception.
     */
    private void disconnect() {
        try {
            mqttManager.disconnectFromMqttBroker();
            resetHandler.removeCallbacksAndMessages(null);
            chartManager.resetChart();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            heartRateTextView.setText("--");
            satTextView.setText("--");
            soundManager.stopSound();
        } catch (MqttException e) {
            Log.e(TAG, "Exception while disconnecting from MQTT broker: " + e.getMessage(), e);
        }
        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show());
    }

    /**
     * Handle incoming MQTT messages.
     *
     * @param topic   The topic on which the message was received
     * @param payload The message payload
     */
    private void handleMqttMessage(String topic, String payload) {
        if (MqttManager.TOPIC_BPM.equals(topic)) {
            try {
                float bpmValue = Float.parseFloat(payload.trim());
                int currentTime = entries.size();
                int heartRate = Integer.parseInt(payload.trim());

                runOnUiThread(() -> {
                    // Update UI with BPM data
                    entries.add(new Entry(currentTime, bpmValue));
                    chartManager.updateChart();
                    heartRateTextView.setText(String.format("%d bpm", heartRate));
                    soundManager.playSound(bpmValue);

                    if (bpmValue == 0) {
                        // Stop sound if BPM value is 0
                        soundManager.stopSound();
                    } else {
                        // Play sound for BPM value
                        soundManager.playSound(bpmValue);
                    }

                    if (++entryCount % MAX_ENTRIES == 0) {
                        // Reset chart after reaching MAX_ENTRIES
                        chartManager.resetChart();
                        entries.clear();
                        entryCount = 0;
                    }

                    if (currentTime % ChartManager.SAVE_INTERVAL == 0) {
                        // Insert BPM and latest SPO2 values into database at regular intervals
                        databaseHelper.insertData(username, bpmValue, latestSpo2Value);
                    }
                });
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (MqttManager.TOPIC_SAT.equals(topic)) {
            try {
                latestSpo2Value = Float.parseFloat(payload.trim());
                runOnUiThread(() -> satTextView.setText(String.format("%s%%", payload)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open the activity to view saved data.
     */
    private void showSavedData() {
        Intent intent = new Intent(this, DataTableActivity.class);
        startActivity(intent);
    }

    /**
     * Open the activity to manage options.
     */
    private void openOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
