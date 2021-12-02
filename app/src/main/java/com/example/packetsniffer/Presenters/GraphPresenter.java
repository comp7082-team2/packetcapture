package com.example.packetsniffer.Presenters;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapGraph;
import com.example.packetsniffer.Models.PcapRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphPresenter  {
    private static final int MAX_GRAPH_SIZE = 7000;

    public void setUpGraph(View chartId){
        LineChart rttChart;
        PcapRepository repository = PcapRepository.getInstance();
        List<PcapEntry> entries =  repository.getEntries();
        if (entries.size() > MAX_GRAPH_SIZE) {
            Toast.makeText(chartId.getContext(), "Captures with more than " + MAX_GRAPH_SIZE + " entries will be truncated to improve graph quality and performance",
                    Toast.LENGTH_LONG).show();
            entries = entries.subList(0, MAX_GRAPH_SIZE);
        }
        PcapGraph graphData = new PcapGraph(entries);
        rttChart = (LineChart) chartId;
        LineDataSet lineDataSet = new LineDataSet(graphData.lineChartDataSet(),"data set");
        Log.d(GraphPresenter.class.getName(), "Found Packets");
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
