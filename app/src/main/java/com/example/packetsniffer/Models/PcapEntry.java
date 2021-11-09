package com.example.packetsniffer.Models;

import io.pkts.packet.Packet;

public class PcapEntry {

    private String protocol;
    private long arrivalTime;
    private Packet packet;

    public PcapEntry() {
        protocol = "";
        arrivalTime = 0;
        packet = null;
    }

    public PcapEntry(String protocol, long arrivalTime, Packet packet) {
        this.protocol = protocol;
        this.arrivalTime = arrivalTime;
        this.packet = packet;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
