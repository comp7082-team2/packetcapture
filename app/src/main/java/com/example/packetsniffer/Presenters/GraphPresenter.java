package com.example.packetsniffer.Presenters;

import android.graphics.Color;
import android.view.View;

import com.example.packetsniffer.Models.PcapGraph;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class GraphPresenter  {

    public void setUpGraph(View chartId){
        LineChart rttChart;
        PcapGraph graphData = new PcapGraph();
        rttChart = (LineChart) chartId;
        LineDataSet lineDataSet = new LineDataSet(graphData.lineChartDataSet(),"data set");
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);
        rttChart.setData(lineData);
        rttChart.invalidate();

        rttChart.setNoDataText("Data not Available");

        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.GREEN);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setLineWidth(5);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleHoleRadius(8);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setValueTextColor(Color.BLACK);

    }
}
