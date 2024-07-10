package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ReportFragment extends Fragment {

    DBHelper dbHelper;
    BarChart barChart;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        dbHelper = new DBHelper(getActivity());

        barChart = view.findViewById(R.id.bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);

        // Bar chart setup
        setupBarChart();

        // Pie chart setup
        setupPieChart();

        return view;
    }

    private void setupBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Float> rst = dbHelper.getPoints();

        for (int i = 0; i < rst.size(); i++) {
            float points = rst.get(i);
            BarEntry barEntry = new BarEntry(i + 1, points);
            barEntries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Player");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(true);

        barChart.setData(new BarData(barDataSet));
        barChart.getDescription().setText("");
        barChart.getDescription().setTextColor(Color.BLUE);
    }

    private void setupPieChart() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Fetch data from the database
        int totalWins = dbHelper.getTotalWins();
        int totalLosses = dbHelper.getTotalLosses();
        int totalDraws = dbHelper.getTotalDraws();

        // Create PieEntries for wins, losses, and draws
        pieEntries.add(new PieEntry(totalWins, "Wins"));
        pieEntries.add(new PieEntry(totalLosses, "Losses"));
        pieEntries.add(new PieEntry(totalDraws, "Draws"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(5000, 5000);
        pieChart.getDescription().setText("");
        pieChart.getDescription().setTextColor(Color.RED);
    }
}
