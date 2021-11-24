package com.example.packetsniffer.Presenters;

import android.graphics.Color;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapGraph;
import com.example.packetsniffer.Models.PcapRepository;
import com.example.packetsniffer.Presenters.GraphPresenter;
import com.example.packetsniffer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class GraphPresenter  {
    private PcapRepository pcapRepository;
    private List<PcapEntry> entries;

    private static final String TCP = "TCP";

    /** line 29 should be able to load the*/
    public void setUpGraph(View chartId){
        LineChart rttChart;
        PcapGraph grappData = new PcapGraph();
        rttChart = (LineChart) chartId;
        LineDataSet lineDataSet = new LineDataSet(grappData.lineChartDataSet(),"data set");
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
