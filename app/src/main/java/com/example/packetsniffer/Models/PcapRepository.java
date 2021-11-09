package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.filters.Filter;

public class PcapRepository {

    private static PcapRepository repository;

    private static String TAG = PcapRepository.class.getName();

    private Pcap pcap;


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

    public static PcapRepository getInstance() {
        if (repository == null) {
            repository = new PcapRepository();
        }
        return repository;
    }
}
