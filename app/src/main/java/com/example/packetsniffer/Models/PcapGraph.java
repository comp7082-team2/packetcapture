package com.example.packetsniffer.Models;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.pkts.packet.Packet;
import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

public class PcapGraph {
    private static final String TAG = PcapGraph.class.getName();
    private static final String TCP = "TCP";
    public ArrayList<Entry> lineChartDataSet() {
        ArrayList<Entry> packets = new ArrayList<Entry>();
        PcapRepository pcapRepository;
        List<PcapEntry> entries;

        pcapRepository = PcapRepository.getInstance();
        entries = pcapRepository.getEntries(null);

        Packet firstPacket = entries.get(0).getPacket();
        long firstTime = firstPacket.getArrivalTime();
        for (int i=0; i < entries.size(); i++) {
            try {
                PcapEntry entry = entries.get(i);

                // if tcp packet look at it
                if (entry.getProtocol().equals(TCP)) {
                    TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);
                    long time = packet.getArrivalTime();

                    if(packet.isACK()){
                        long ACKnum = packet.getAcknowledgementNumber();

                        // Set time variable for original packet (ie, packet that is being ACK'd)
                        long timeOrig = 0;
                        for (int j=i-1; j>=0; j--){
                            // match ack number to sequence number of original packet
                            try {
                                PcapEntry entryOrig = entries.get(j);
                                if (entryOrig.getProtocol().equals(TCP)) {
                                    TCPPacket packetOrig = (TCPPacket) entryOrig.getPacket().getPacket(Protocol.TCP);

                                    long SEQnum = packetOrig.getSequenceNumber();
                                    if (SEQnum == ACKnum) {
                                        timeOrig = packetOrig.getArrivalTime();
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }

                        if (timeOrig != 0){
                            long diffTime = time - timeOrig;

                            long captureTime = time - firstTime;
                            Log.v("testing values", String.valueOf(new StringBuilder(String.valueOf(captureTime/1000)).append(" ").append(diffTime)));

                            packets.add(new Entry(captureTime/1000, diffTime));
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
