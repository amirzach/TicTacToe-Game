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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(getActivity());

        barChart = view.findViewById(R.id.bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        // Bar chart
        ArrayList<Float> pointsList = dbHelper.getPoints();
        for (int i = 0; i < pointsList.size(); i++) {
            float points = pointsList.get(i);
            BarEntry barEntry = new BarEntry(i + 1, points);
            barEntries.add(barEntry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Player");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(true);

        barChart.setData(new BarData(barDataSet));
        barChart.getDescription().setText("");
        barChart.getDescription().setTextColor(Color.BLUE);

        // Pie chart for host's win, lose, and draw percentages
        int hostWins = dbHelper.getHostWins();
        int hostLosses = dbHelper.getHostLosses();
        int hostDraws = dbHelper.getHostDraws();
        int totalGames = hostWins + hostLosses + hostDraws;

        if (totalGames > 0) {
            float winPercentage = (hostWins / (float) totalGames) * 100;
            float losePercentage = (hostLosses / (float) totalGames) * 100;
            float drawPercentage = (hostDraws / (float) totalGames) * 100;

            pieEntries.add(new PieEntry(winPercentage, "Wins"));
            pieEntries.add(new PieEntry(losePercentage, "Losses"));
            pieEntries.add(new PieEntry(drawPercentage, "Draws"));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.invalidate(); // Refresh the chart

            pieChart.getDescription().setText("");
            pieChart.getDescription().setTextColor(Color.RED);
        }

        return view;
    }
}