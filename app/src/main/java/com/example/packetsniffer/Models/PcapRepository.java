package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.filters.Filter;
import io.pkts.filters.FilterFactory;
import io.pkts.filters.FilterParseException;
import io.pkts.framer.FramerManager;
import io.pkts.packet.Packet;


public class PcapRepository {

    private static PcapRepository repository;

    private static String TAG = PcapRepository.class.getName();

    private Pcap pcap;

    private Filter filter;

    private FilterFactory filterFactory = FilterFactory.getInstance();

    private FramerManager framerManager;


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

    public void loopPcap(PacketHandler handler, String filterExpression) throws IOException {
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
           setFilter(filterExpression);
           pcap.loop(packet -> {
               if (filter != null && filter.accept(packet)) {
                   System.out.println(packet.getPayload());
               }
               return false;
           });
      }
    }

    public static PcapRepository getInstance() {
        if (repository == null) {
            repository = new PcapRepository();
        }
        return repository;
    }
}
