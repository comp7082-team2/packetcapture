package com.example.packetsniffer.Presenters;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapRepository;

import java.util.List;

public class PacketListPresenter {

    private PcapRepository pcapRepository;
    public PacketListPresenter() {
        pcapRepository = PcapRepository.getInstance();
    }

    public List<PcapEntry> loadPcap() {
        return pcapRepository.getEntries(null);
    }

    public boolean isValidFilter(String expression) {
        //return expression.equals("tcp") || expression.equals("udp");
        return true;
    }

    public List<PcapEntry> loadFilteredPcap(String expression) {
        return pcapRepository.getEntries(expression);
    }

}
