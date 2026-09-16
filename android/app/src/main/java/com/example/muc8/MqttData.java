package com.example.muc8;

/**
 * Represents MQTT data containing BPM (heart rate), SPO2 (oxygen saturation), and related information.
 */
public class MqttData {

    private int id;             // Unique ID for the data entry
    private String username;    // Username associated with the data
    private float bpm;          // BPM (heart rate) value
    private float spo2;         // SPO2 (oxygen saturation) value
    private String dateTime;    // Date and time when the data was recorded

    // Getters and setters

    /**
     * Gets the ID of the MQTT data entry.
     *
     * @return The ID of the data entry
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the MQTT data entry.
     *
     * @param id The ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username associated with the MQTT data.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the MQTT data.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the BPM (heart rate) value.
     *
     * @return The BPM value
     */
    public float getBpm() {
        return bpm;
    }

    /**
     * Sets the BPM (heart rate) value.
     *
     * @param bpm The BPM value to set
     */
    public void setBpm(float bpm) {
        this.bpm = bpm;
    }

    /**
     * Gets the SPO2 (oxygen saturation) value.
     *
     * @return The SPO2 value
     */
    public float getSpo2() {
        return spo2;
    }

    /**
     * Sets the SPO2 (oxygen saturation) value.
     *
     * @param spo2 The SPO2 value to set
     */
    public void setSpo2(float spo2) {
        this.spo2 = spo2;
    }

    /**
     * Gets the date and time when the MQTT data was recorded.
     *
     * @return The date and time string
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time when the MQTT data was recorded.
     *
     * @param dateTime The date and time string to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
