package com.example.muc8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter for RecyclerView to display MQTT data in a list format.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private List<MqttData> dataList; // List of MQTT data objects to display

    /**
     * Constructor to initialize the DataAdapter with a list of MqttData objects.
     *
     * @param dataList The list of MQTT data objects to display
     */
    public DataAdapter(List<MqttData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new DataViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        // Bind data to views inside DataViewHolder for given position
        MqttData data = dataList.get(position);
        holder.usernameTextView.setText(data.getUsername());
        holder.timeTextView.setText(formatDateTime(data.getDateTime()));
        holder.bpmTextView.setText(String.valueOf(data.getBpm()));
        holder.spo2TextView.setText(String.valueOf(data.getSpo2())); // Bind the spo2 value
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return dataList.size();
    }

    /**
     * ViewHolder class that holds references to the views inside each item of the RecyclerView.
     */
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;
        TextView timeTextView;
        TextView bpmTextView;
        TextView spo2TextView; // Declare the TextView for spo2

        /**
         * Constructor to initialize the DataViewHolder with the item view.
         *
         * @param itemView The view representing each item in the RecyclerView
         */
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews from item layout
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            bpmTextView = itemView.findViewById(R.id.bpm_text_view);
            spo2TextView = itemView.findViewById(R.id.spo2_text_view); // Initialize the spo2 TextView
        }
    }

    /**
     * Helper method to format date and time string.
     *
     * @param dateTimeString The date and time string in "yyyy-MM-dd HH:mm:ss" format
     * @return Formatted time string in "HH:mm:ss" format
     */
    public static String formatDateTime(String dateTimeString) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = "";

        try {
            Date dateTime = inputDateFormat.parse(dateTimeString);
            formattedTime = outputDateFormat.format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedTime;
    }
}
