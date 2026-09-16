package com.example.muc8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to manage SQLite database operations for MQTT data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mqtt_data.db"; // Database name
    private static final int DATABASE_VERSION = 2; // Database version

    // Table name and column names
    public static final String TABLE_NAME = "mqtt_data";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_SPO2 = "spo2";
    public static final String COLUMN_DATE_TIME = "date_time";

    // Create table SQL query
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_BPM + " REAL," +
                    COLUMN_SPO2 + " REAL," +
                    COLUMN_DATE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    /**
     * Constructor for DatabaseHelper.
     *
     * @param context The context in which the database is created
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute SQL to create the database table
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // Upgrade tables if database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserts data into the database.
     *
     * @param username The username to insert
     * @param bpm      The BPM value to insert
     * @param spo2     The SpO2 value to insert
     * @return The row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertData(String username, float bpm, float spo2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_BPM, bpm);
        values.put(COLUMN_SPO2, spo2);
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Retrieves all MQTT data from the database.
     *
     * @return A list of MqttData objects containing all MQTT data stored in the database
     */
    public List<MqttData> getAllMqttData() {
        List<MqttData> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                MqttData mqttData = new MqttData();
                mqttData.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                mqttData.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
                mqttData.setBpm(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_BPM)));
                mqttData.setSpo2(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_SPO2)));
                mqttData.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME)));

                dataList.add(mqttData);
            }
            cursor.close();
        }

        return dataList;
    }

    /**
     * Deletes all data from the database.
     */
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
