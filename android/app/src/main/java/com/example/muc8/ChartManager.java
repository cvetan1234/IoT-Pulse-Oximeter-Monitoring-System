package com.example.muc8;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

/**
 * Manages the configuration and updating of a LineChart using MPAndroidChart library.
 */
public class ChartManager {

    public static final int SAVE_INTERVAL = 10; // Save interval constant

    private LineChart lineChart; // The LineChart view to manage
    private List<Entry> entries; // List of data points to display on the chart
    private LineData lineData; // LineData object that holds data and styling for the chart

    /**
     * Constructs a new ChartManager to manage the provided LineChart with given data entries.
     *
     * @param lineChart The LineChart view to manage
     * @param entries   The list of data entries to display on the chart
     */
    public ChartManager(LineChart lineChart, List<Entry> entries) {
        this.lineChart = lineChart;
        this.entries = entries;
    }

    /**
     * Sets up the LineChart with provided data entries and basic styling.
     * This method should be called initially or whenever chart setup needs to be refreshed.
     */
    public void setupLineChart() {
        LineDataSet dataSet = new LineDataSet(entries, "Heart Rate Data");
        dataSet.setDrawValues(false); // Disable drawing values on data points
        dataSet.setLineWidth(2f); // Set line width to 2 pixels
        dataSet.setDrawCircles(false); // Disable drawing circles at data points
        lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Set minimum value of Y-axis
        leftAxis.setAxisMaximum(160f); // Set maximum value of Y-axis
        lineChart.getAxisRight().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(28f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Description description = new Description();
        description.setText("Time (seconds) vs Heart Rate");
        lineChart.setDescription(description);

        // Refresh the chart
        lineChart.invalidate();
    }

    /**
     * Clears all data and resets the chart to its initial state.
     * This method should be called when resetting or clearing the chart.
     */
    public void resetChart() {
        entries.clear(); // Clear the list of entries
        lineData.clearValues(); // Clear values from LineData
        lineChart.clear(); // Clear the chart
        setupLineChart(); // Setup chart again
        lineChart.invalidate(); // Refresh the chart
    }

    /**
     * Notifies the chart that the underlying data has changed and updates the chart accordingly.
     * This method should be called after updating data entries dynamically.
     */
    public void updateChart() {
        lineData.notifyDataChanged(); // Notify LineData that data has changed
        lineChart.notifyDataSetChanged(); // Notify chart that data has changed
        lineChart.invalidate(); // Refresh the chart
    }
}
