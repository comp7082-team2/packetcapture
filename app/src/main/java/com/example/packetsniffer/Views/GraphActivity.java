package com.example.packetsniffer.Views;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapRepository;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import android.util.Log;

import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

import com.example.packetsniffer.R;

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = GraphActivity.class.getName();

    private PcapRepository pcapRepository;
    private List<PcapEntry> entries;

    private static final String TCP = "TCP";

    LineChart rttChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtt_graph);

        rttChart = findViewById(R.id.rttChart);
        LineDataSet lineDataSet = new LineDataSet(lineChartDataSet(),"data set");
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

    private ArrayList<Entry> lineChartDataSet() {
        ArrayList<Entry> packets = new ArrayList<Entry>();
        
        pcapRepository = PcapRepository.getInstance();
        entries = pcapRepository.getEntries();
        long firstTime = 0;

        try {
            TCPPacket firstPacket = (TCPPacket) entries.get(0).getPacket().getPacket(Protocol.TCP);
            firstTime = firstPacket.getArrivalTime();
            Log.v("testing values FT", String.valueOf(firstTime));
        } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
        }

        
        for (int i=0; i < entries.size(); i++) {
            try {
                PcapEntry entry = entries.get(i);

                // if tcp packet look at it
                if (entry.getProtocol() == TCP) {
                    TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);

                    String sourceIP = packet.getSourceIP();
                    long time = packet.getArrivalTime();

                    if(packet.isACK() && (packet.isPSH() == false )){
                        long ACKnum = packet.getAcknowledgementNumber();
                        Log.v("testing ACK", String.valueOf(ACKnum));

                        // Set time variable for original packet (ie, packet that is being ACK'd)
                        long timeOrig = 0;
                        for (int j=i-1; j>=0; j--){
                            // match ack number to sequence number of original packet
                            try {
                                PcapEntry entryOrig = entries.get(j);
                                TCPPacket packetOrig = (TCPPacket) entryOrig.getPacket().getPacket(Protocol.TCP);

                                long SEQnum = packetOrig.getSequenceNumber();
                                if (SEQnum == ACKnum){
                                    timeOrig = packetOrig.getArrivalTime();
                                    break;
                                }
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }

                        if (timeOrig != 0){
                            long diffTime = time - timeOrig;

                            long captureTime = time - firstTime;
                            Log.v("testing values", String.valueOf(new StringBuilder(String.valueOf(captureTime/1000)).append(" ").append(diffTime)));

                            packets.add(new Entry(captureTime/1000,diffTime));
                        }
                    }
                }

                // if not, discard
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }

        }

        return packets;
    }
}
