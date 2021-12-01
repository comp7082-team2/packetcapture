package com.example.packetsniffer.Models;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

public class PcapGraph {
    private static final String TAG = PcapGraph.class.getName();
    private static final String TCP = "TCP";

    private long firstPacketTime;

    private List <TCPPacket> tcpPackets;

    public PcapGraph(List<PcapEntry> entries) {
        // Filter only TCP packets
        tcpPackets = entries.stream()
                .filter(pcapEntry -> pcapEntry.getProtocol().equals(TCP))
                .map(entry -> this.extractTcpPacket(entry))
                .collect(Collectors.toList());
        firstPacketTime = tcpPackets.size() > 0 ? tcpPackets.get(0).getArrivalTime() : 0;
    }

    public List<Entry> lineChartDataSet() {
        List<TCPPacket> acks = tcpPackets.stream()
                .filter(packet -> packet.isACK())
                .collect(Collectors.toList());
        return findRTT();
    }

    private TCPPacket extractTcpPacket(PcapEntry entry) {
        try {
            TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);
            return packet;
        }catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private List<Entry> findRTT() {
        List<Entry> graphEntries = new ArrayList<>();
        for (int i = 0; i < tcpPackets.size(); i++) {
            TCPPacket ackPacket = tcpPackets.get(i);
            if(ackPacket.isACK()) {
                Entry entry = createEntryFromAck(ackPacket, i);
                if (entry != null) {
                    graphEntries.add(entry);
                }
            }
        }
        return graphEntries;
    }

    private Entry createEntryFromAck(TCPPacket ackPacket, int index) {
        long time = ackPacket.getArrivalTime();
        for (int i = index; i >= 0; i--) {
            TCPPacket packet = tcpPackets.get(i);
            if(packet.getSequenceNumber() == ackPacket.getAcknowledgementNumber()) {
                long diffTime = time - packet.getArrivalTime();
                long captureTime = time - firstPacketTime;
                return new Entry(captureTime / 1000, diffTime);
            }
        }
        return null;
    }
}
