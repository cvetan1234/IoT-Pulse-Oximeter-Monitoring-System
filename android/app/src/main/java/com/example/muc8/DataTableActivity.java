package com.example.muc8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Activity to display MQTT data in a RecyclerView and provide a reset button to clear the database.
 */
public class DataTableActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView to display data
    private DataAdapter dataAdapter; // Adapter for RecyclerView
    private DatabaseHelper databaseHelper; // Database helper for SQLite operations
    private Button resetButton; // Button to reset database

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table); // Set layout for the activity

        resetButton = findViewById(R.id.reset_button); // Initialize reset button from layout

        recyclerView = findViewById(R.id.recycler_view); // Initialize RecyclerView from layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager for RecyclerView

        databaseHelper = new DatabaseHelper(this); // Initialize DatabaseHelper for SQLite operations
        List<MqttData> dataList = databaseHelper.getAllMqttData(); // Retrieve all MQTT data from database

        dataAdapter = new DataAdapter(dataList); // Create adapter with retrieved data
        recyclerView.setAdapter(dataAdapter); // Set adapter for RecyclerView

        // Set click listener for reset button
        resetButton.setOnClickListener(v -> {
            databaseHelper.deleteAllData(); // Delete all data from database
            // Show toast message on UI thread after database reset
            runOnUiThread(() -> Toast.makeText(DataTableActivity.this, "Database reset successful", Toast.LENGTH_SHORT).show());
            recreate(); // Refresh activity to reflect database changes
        });
    }
}
