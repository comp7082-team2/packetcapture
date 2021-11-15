package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.filters.Filter;
import io.pkts.packet.Packet;
import io.pkts.protocol.Protocol;
import io.pkts.filters.FilterFactory;
import io.pkts.filters.FilterParseException;

public class PcapRepository {

    private static PcapRepository repository;

    private static String TAG = PcapRepository.class.getName();

    private Pcap pcap;

    private List<PcapEntry> entries;
    private Filter filter;

    private FilterFactory filterFactory = FilterFactory.getInstance();


    private PcapRepository() {

    }

    public void readPcap(String filename) {
        try {
            pcap = Pcap.openStream(filename);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + filename, e);
        } catch (IOException e) {
            Log.e(TAG, "Error opening File: " + filename, e);
        }
    }

    public Pcap getPcap() {
        return pcap;
    }

    public void setFilter(String expression) throws FilterParseException {
        if (expression != null && !expression.isEmpty()) {
            this.filter = this.filterFactory.createFilter(expression);
        }
    }

    public void loopPcap(PacketHandler handler, Filter filter) {
        if (pcap == null) {
            return;
        }
      if (filter == null) {
          try {
              pcap.loop(handler);
          } catch (IOException e) {
              Log.e(TAG, "Error looping over pcap", e);
          }
      } else {
          // TODO: implement filtering, not sure where that responsibility should lie
          /*
           * pcap.setFilter(filter)
           * pcap.loop(handler)
           */
      }
    }

    public List<PcapEntry> getEntries() {
        if (entries == null) {
            PacketListPacketHandler handler = new PacketListPacketHandler();
            this.loopPcap(handler, null);
            entries = handler.getPackets();
        }
        return entries;
    }

    public static PcapRepository getInstance() {
        if (repository == null) {
            repository = new PcapRepository();
        }
        return repository;
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
