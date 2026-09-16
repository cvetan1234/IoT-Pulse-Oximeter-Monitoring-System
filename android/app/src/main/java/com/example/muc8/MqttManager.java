package com.example.muc8;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Manages MQTT connection and message handling.
 */
public class MqttManager {

    public static final String TOPIC_BPM = "bpm";
    public static final String TOPIC_SAT = "sat";
    private static final String MQTT_BROKER = "tcp://10.0.2.2:1883"; // MQTT broker address
    private static final String CLIENT_ID = "AndroidClient"; // MQTT client ID
    private static final String TAG = "MqttManager"; // Logging tag

    private MqttClient mqttClient; // MQTT client instance
    private Context context; // Application context
    private MessageHandler messageHandler; // Handler for incoming MQTT messages

    /**
     * Constructor to initialize MQTT manager with context and message handler.
     *
     * @param context         The application context
     * @param messageHandler  Handler for incoming MQTT messages
     */
    public MqttManager(Context context, MessageHandler messageHandler) {
        this.context = context;
        this.messageHandler = messageHandler;
    }

    /**
     * Connects to the MQTT broker.
     */
    public void connectToMqttBroker() {
        try {
            mqttClient = new MqttClient(MQTT_BROKER, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true); // Clean session for MQTT connection
            mqttClient.connect(options);

            // Set MQTT callback to handle connection loss, message arrival, and delivery completion
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "Connection lost", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    messageHandler.handleMessage(topic, payload); // Pass incoming message to handler
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Delivery complete");
                }
            });

            // Subscribe to MQTT topics for BPM and SPO2
            mqttClient.subscribe(TOPIC_BPM);
            mqttClient.subscribe(TOPIC_SAT);

        } catch (MqttException e) {
            Log.e(TAG, "Exception while connecting to MQTT broker: " + e.getMessage(), e);
        }
    }

    /**
     * Disconnects from the MQTT broker.
     *
     * @throws MqttException If disconnection fails
     */
    public void disconnectFromMqttBroker() throws MqttException {
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
        }
    }

    /**
     * Interface for handling MQTT messages.
     */
    public interface MessageHandler {
        /**
         * Handles incoming MQTT message.
         *
         * @param topic   The topic on which the message was received
         * @param payload The message payload
         */
        void handleMessage(String topic, String payload);
    }
}
