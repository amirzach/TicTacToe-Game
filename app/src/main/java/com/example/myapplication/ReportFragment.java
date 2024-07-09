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
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        int i;
        ArrayList<Float> rst = new ArrayList<>();
        float points;

        dbHelper = new DBHelper(getActivity());

        barChart = view.findViewById(R.id.bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);

        ArrayList<BarEntry> barEntries= new ArrayList<>();
        ArrayList<PieEntry> pieEntries= new ArrayList<>();

        //bar chart
        rst=dbHelper.getPoints();
        for(i=0; i<rst.size(); i++){
            points = (float) rst.get(i);
            BarEntry barEntry=new BarEntry(i+1,points);
            barEntries.add(barEntry);
        }

        //pie chart

        for(i=1;i<10;i++) {
            float value = (float) (i * 10);

            PieEntry pieEntry=new PieEntry(i,value);
            pieEntries.add(pieEntry);


        }
        BarDataSet barDataSet = new BarDataSet (barEntries,"Students Mark");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(true);

        barChart.setData(new BarData(barDataSet));
        //barChart.animateXY (5000, 5000) ;
        barChart.getDescription().setText("Students Chart");
        barChart.getDescription().setTextColor(Color.BLUE);

        //pie chart
        PieDataSet pieDataSet = new PieDataSet (pieEntries,"Students");
        pieDataSet.setColors (ColorTemplate .COLORFUL_COLORS) ;
        //pieChart.getDescription () .setEnabled (true);
        pieChart.setData(new PieData(pieDataSet)) ;
        pieChart.animateXY(5000,5000);
        pieChart.getDescription().setText("Students Chart");
        pieChart.getDescription ().setTextColor(Color .RED);



        return view;
    }




}