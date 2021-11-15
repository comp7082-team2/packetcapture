package com.example.packetsniffer.Presenters;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.pkts.PacketHandler;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;

public class PacketListPresenter {

    private PcapRepository pcapRepository;

    public PacketListPresenter() {
        pcapRepository = PcapRepository.getInstance();
    }

    public List<PcapEntry> loadPcap() {
        PacketListPacketHandler handler = new PacketListPacketHandler();
        pcapRepository.loopPcap(handler, null);
        return handler.getPackets();
    }


    private class PacketListPacketHandler implements PacketHandler {
        private List<PcapEntry> packets;
        PacketListPacketHandler() {
            packets = new ArrayList<>();
        }

        @Override
        public boolean nextPacket(Packet packet) throws IOException {
            if (packet.hasProtocol(Protocol.TCP)) {
                packets.add(new PcapEntry("TCP", packet.getArrivalTime(), packet));
            } else if (packet.hasProtocol(Protocol.UDP)) {
                packets.add(new PcapEntry("UDP", packet.getArrivalTime(), packet));
            } else if (packet.hasProtocol(Protocol.ICMP)) {
                packets.add(new PcapEntry("ICMP", packet.getArrivalTime(), packet));
            } else {
                packets.add(new PcapEntry("", packet.getArrivalTime(), packet));
            }
            return true;
        }

        public List<PcapEntry> getPackets() {
            return packets;
        }
    }
}
