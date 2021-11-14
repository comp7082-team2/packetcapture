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
        return pcapRepository.getEntries();
    }

}
