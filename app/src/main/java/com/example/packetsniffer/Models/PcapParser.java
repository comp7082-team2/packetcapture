package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.IOException;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.packet.Packet;
import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

public class PcapParser {

    public static final String TAG = PcapParser.class.getName();

    public void readPcapFile(String filename) {
        try {
            Pcap pcap = Pcap.openStream(filename);
            pcap.loop(new PacketHandler() {
                @Override
                public boolean nextPacket(Packet packet) throws IOException {
                    Log.d(TAG, "Arrival Time: " + packet.getArrivalTime());

                    if (packet.hasProtocol(Protocol.TCP)) {
                        TCPPacket tcpPacket = (TCPPacket) packet.getPacket(Protocol.TCP);
                        Log.d(TAG, "Seq. #: " + tcpPacket.getSequenceNumber());
                    }

                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}
