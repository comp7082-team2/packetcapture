package com.example.packetsniffer.Models;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

public class PcapGraph {
    private static final String TAG = PcapGraph.class.getName();
    private static final String TCP = "TCP";
    private PcapRepository pcapRepository;

    private long firstPacketTime;

    private List <TCPPacket> tcpPackets;

    public PcapGraph() {
        pcapRepository = PcapRepository.getInstance();
        // Filter only TCP packets
        tcpPackets = pcapRepository.getEntries(null).stream()
                .filter(pcapEntry -> pcapEntry.getProtocol().equals(TCP))
                .map(entry -> this.extractTcpPacket(entry))
                .collect(Collectors.toList());
        firstPacketTime = tcpPackets.size() > 0 ? tcpPackets.get(0).getArrivalTime() : 0;
    }

    public List<Entry> lineChartDataSet() {
        List<TCPPacket> acks = tcpPackets.stream()
                .filter(packet -> packet.isACK())
                .collect(Collectors.toList());
        return acks.stream()
                .map(packet -> this.findRTT(packet))
                .filter(entry -> entry != null)
                .collect(Collectors.toList());
    }

    private boolean isAck(PcapEntry entry) {
        try {
            TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);
            return packet.isACK();
        }catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
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

    private Entry findRTT(TCPPacket packet) {
        long ACKnum = packet.getAcknowledgementNumber();

        // Set time variable for original packet (ie, packet that is being ACK'd)
        long time = packet.getArrivalTime();

        Optional<TCPPacket> packetOrig = tcpPackets.stream()
                .filter(pcket -> pcket.getSequenceNumber() == ACKnum && pcket.getArrivalTime() < time)
                .findFirst();
        if (packetOrig.isPresent()) {
            long diffTime = time - packetOrig.get().getArrivalTime();
            long captureTime = time - firstPacketTime;
            return new Entry(captureTime / 1000, diffTime);
        }
        return null;
    }
}
