package com.example.packetsniffer.Presenters;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapFilter;
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
        return expression.equals("tcp") || expression.equals("udp");
    }

    public List<PcapEntry> loadFilteredPcap(String expression) {
        PcapFilter filter = new PcapFilter();
        String[] expressionArr = expression.split(" && ");
        for (String exp : expressionArr) {
            if (exp.equals("tcp") || exp.equals("udp")) {
                filter.setProtocol(exp);
            }
        }
        return pcapRepository.getEntries(filter);
    }

}
