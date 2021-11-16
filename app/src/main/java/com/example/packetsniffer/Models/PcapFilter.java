package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.IOException;

import io.pkts.filters.Filter;
import io.pkts.filters.FilterException;
import io.pkts.packet.Packet;
import io.pkts.packet.TCPPacket;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.UDPPacket;
import io.pkts.protocol.Protocol;

public class PcapFilter implements Filter {

    private static final String TAG = PcapFilter.class.getName();

    private String protocol;
    private String src;
    private String dst;
    private String srcPort;
    private String dstPort;

    public PcapFilter() {
        protocol = null;
        src = null;
        dst = null;
        srcPort = null;
        dstPort = null;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protcol) {
        this.protocol = protcol;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    @Override
    public boolean accept(Packet packet) throws FilterException {
        boolean isValidPacket = false;
        if (protocol != null) {
            if (protocol.equals("tcp")) {
                try {
                    isValidPacket = packet.hasProtocol(Protocol.TCP);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else if (protocol.equals("udp")) {
                try {
                    isValidPacket = packet.hasProtocol(Protocol.UDP);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        if (isTransportPacket(packet)) {
            TransportPacket transportPacket = getTransportPacket(packet);
            if (transportPacket != null) {
                isValidPacket = (src == null || src.equals(transportPacket.getSourceIP()))
                    || (dst == null || dst.equals(transportPacket.getDestinationIP()))
                    || (srcPort == null || srcPort.equals(String.valueOf(transportPacket.getSourcePort())))
                    || (dstPort == null || dstPort.equals(String.valueOf(transportPacket.getDestinationPort())));
            }
        }
        return isValidPacket;
    }

    // TODO: figure out a better way to know if this is a transport packet, this only identifies two protocols
    private boolean isTransportPacket(Packet packet) {
        try {
            return packet.hasProtocol(Protocol.TCP) || packet.hasProtocol(Protocol.UDP);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    private TransportPacket getTransportPacket(Packet packet) {
        try {
            if (packet.hasProtocol(Protocol.TCP)) {
                return (TCPPacket) packet.getPacket(Protocol.TCP);
            } else if (packet.hasProtocol(Protocol.UDP)) {
                return (UDPPacket) packet.getPacket(Protocol.UDP);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
