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
        //return expression.equals("tcp") || expression.equals("udp");
        return true;
    }

    public List<PcapEntry> loadFilteredPcap(String expression) {
        PcapFilter filter = new PcapFilter();
        String[] expressionArr = expression.split(" && ");
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
        return pcapRepository.getEntries(filter);
    }

}
