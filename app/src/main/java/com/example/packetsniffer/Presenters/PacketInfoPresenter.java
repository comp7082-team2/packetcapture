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

    private PcapRepository pcapRepository;
    private PcapEntry entry;
    private PacketInfoPresenter.View view;

    public PacketInfoPresenter(int index, PacketInfoPresenter.View view) {
        pcapRepository = PcapRepository.getInstance();
        entry = pcapRepository.getEntries().get(index);
        this.view = view;
    }

    public void displayPacket() {
        try {
            if (entry.getProtocol() == "TCP") {
                // Display TCP specific info
                TCPPacket packet = (TCPPacket) entry.getPacket().getPacket(Protocol.TCP);
                view.displaySrc(packet.getSourceIP());
                view.displayDst(packet.getDestinationIP());
            } else if (entry.getProtocol() == "UDP") {
                // Display UDP specific info
                UDPPacket packet = (UDPPacket) entry.getPacket().getPacket(Protocol.UDP);
                view.displaySrc(packet.getSourceIP());
                view.displayDst(packet.getDestinationIP());
            } else {
                // TODO implement more protocools
                // Unsupported packet type handling
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public interface View {
        void displaySrc(String src);
        void displayDst(String src);
    }
}
