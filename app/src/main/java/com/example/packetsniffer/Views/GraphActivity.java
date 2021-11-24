package com.example.packetsniffer.Views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.packetsniffer.Presenters.GraphPresenter;
import com.example.packetsniffer.R;

public class GraphActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtt_graph);
        GraphPresenter gp = new GraphPresenter();
        View rrtChart = findViewById(R.id.rttChart);
        gp.setUpGraph(rrtChart);
    }
}
