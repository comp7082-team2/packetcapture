package com.example.packetsniffer.Presenters;

import android.util.Log;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapRepository;

import java.io.IOException;

import io.pkts.packet.TCPPacket;
import io.pkts.packet.UDPPacket;
import io.pkts.protocol.Protocol;

public class PacketInfoPresenter {

    private static final String TAG = PacketInfoPresenter.class.getName();

    private static final String TCP = "TCP";
    private static final String UDP = "UDP";

    private final PcapEntry entry;
    private final PacketInfoPresenter.View view;


    public PacketInfoPresenter(int index, String filterExpression, PacketInfoPresenter.View view) {
        PcapRepository pcapRepository = PcapRepository.getInstance();
        entry = pcapRepository.getEntries(filterExpression).get(index);
        this.view = view;
    }

    public void displayPacket() {
        try {
            view.displayArrivalTime(Long.valueOf(entry.getArrivalTime()).toString());
                if (entry.getProtocol().equals(TCP)) {
                    // Display TCP specific info
                    TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);
                    view.displayProtocol(TCP);
                    view.displayTcp(Long.valueOf(packet.getAcknowledgementNumber()).toString(),
                            Long.valueOf(packet.getSequenceNumber()).toString(),
                            displayFlags(packet));
                    view.displaySrc(packet.getSourceIP());
                    view.displayDst(packet.getDestinationIP());
                    view.displaySrcPort(Integer.valueOf(packet.getSourcePort()).toString());
                    view.displayDstPort(Integer.valueOf(packet.getDestinationPort()).toString());
                } else if (entry.getProtocol().equals(UDP)) {
                    // Display UDP specific info, nothing really there
                    UDPPacket packet = (UDPPacket) entry.getPacket().getPacket(Protocol.UDP);
                    view.displayProtocol(UDP);
                    view.displaySrc(packet.getSourceIP());
                    view.displayDst(packet.getDestinationIP());
                    view.displaySrcPort(Integer.valueOf(packet.getSourcePort()).toString());
                    view.displayDstPort(Integer.valueOf(packet.getDestinationPort()).toString());
            }  // TODO implement more protocools
                // Unsupported packet type handling

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private String[] displayFlags(TCPPacket packet) {
        String[] flags = new String[8];
        flags[0] = (packet.isACK()) ? "ACK" : "";
        flags[1] = (packet.isCWR()) ? "CWR" : "";
        flags[2] = (packet.isECE()) ? "ECE" : "";
        flags[3] = (packet.isFIN()) ? "FIN" : "";
        flags[4] = (packet.isPSH()) ? "PSH" : "";
        flags[5] = (packet.isRST()) ? "RST" : "";
        flags[6] = (packet.isSYN()) ? "SYN" : "";
        flags[7] = (packet.isURG()) ? "URG" : "";
        return flags;
    }

    public interface View {
        void displayArrivalTime(String time);
        void displaySrc(String src);
        void displaySrcPort(String port);
        void displayDst(String dst);
        void displayDstPort(String port);
        void displayProtocol(String protocol);
        void displayTcp(String ack, String seq, String[] flags);
    }
}
