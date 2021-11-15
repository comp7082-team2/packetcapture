package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.IOException;

import io.pkts.filters.Filter;
import io.pkts.filters.FilterException;
import io.pkts.packet.Packet;
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
        return isValidPacket;
    }
}
