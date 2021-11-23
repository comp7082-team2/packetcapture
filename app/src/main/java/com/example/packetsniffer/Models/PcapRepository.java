package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.filters.Filter;
import io.pkts.filters.FilterFactory;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

public class PcapRepository {

    private static PcapRepository repository;

    private static String TAG = PcapRepository.class.getName();

    private Pcap pcap;

    private String filename;

    private List<PcapEntry> entries;

    private FilterFactory filterFactory = FilterFactory.getInstance();

    private PcapRepository() {

    }

    public void readPcap(String filename) {
        try {
            pcap = Pcap.openStream(filename);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + filename, e);
        } catch (IOException e) {
            Log.e(TAG, "Error opening File: " + filename, e);
        }
    }

    public void loopPcap(PacketListPacketHandler handler, Filter filter) throws IOException {
        if (pcap == null) {
            return;
        }
        handler.setFilter(filter);
        if(filename != null) {
            readPcap(filename);
        }
        pcap.loop(handler);
    }

    private List<PcapEntry> getEntriesFromFilter(Filter filter) {
        PacketListPacketHandler handler = new PacketListPacketHandler();
        try {
            this.loopPcap(handler, filter);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        entries = handler.getPackets();
        return entries;

    }

    public List<PcapEntry> getEntries(String filterExpression) {
        if (filterExpression != null) {
            PcapFilter filter = new PcapFilter();
            String[] expressionArr = filterExpression.split(" && ");
            for (String exp : expressionArr) {
                exp = exp.trim();
                if (exp.equals("tcp") || exp.equals("udp")) {
                    filter.setProtocol(exp);
                }
                if (exp.startsWith("src ")) {
                    filter.setSrc(exp.replace("src ", "").trim());
                }
                if (exp.startsWith("dst ")) {
                    filter.setDst(exp.replace("dst ", "").trim());
                }
                if (exp.startsWith("srcPort ")) {
                    filter.setSrcPort(exp.replace("srcPort ", "").trim());
                }
                if (exp.trim().startsWith("dstPort ")) {
                    filter.setDstPort(exp.replace("dstPort ", "").trim());
                }
            }
            return getEntriesFromFilter(filter);
        }
        return getEntriesFromFilter(null);
    }

    public static PcapRepository getInstance() {
        if (repository == null) {
            repository = new PcapRepository();
        }
        return repository;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    private class PacketListPacketHandler implements PacketHandler {
        private List<PcapEntry> packets;
        private Filter filter;
        PacketListPacketHandler() {
            packets = new ArrayList<>();
            filter = null;
        }

        @Override
        public boolean nextPacket(Packet packet) throws IOException {
            if (filter == null || filter.accept(packet)) {
                if (packet.hasProtocol(Protocol.TCP)) {
                    packets.add(new PcapEntry("TCP", packet.getArrivalTime(), packet));
                } else if (packet.hasProtocol(Protocol.UDP)) {
                    packets.add(new PcapEntry("UDP", packet.getArrivalTime(), packet));
                } else if (packet.hasProtocol(Protocol.ICMP)) {
                    packets.add(new PcapEntry("ICMP", packet.getArrivalTime(), packet));
                } else {
                    packets.add(new PcapEntry("", packet.getArrivalTime(), packet));
                }
            }
            return true;
        }

        public List<PcapEntry> getPackets() {
            return packets;
        }

        public void setFilter(Filter filter) {
            this.filter = filter;
        }
    }
}
