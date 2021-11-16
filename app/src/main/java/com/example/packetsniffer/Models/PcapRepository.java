package com.example.packetsniffer.Models;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.filters.Filter;
import io.pkts.protocol.Protocol;

public class PcapRepository {

    private static PcapRepository repository;

    private static String TAG = PcapRepository.class.getName();

    private Pcap pcap;

    private Filter filter;

    private HashMap<String, Protocol> protocolHashMap = new HashMap<String, Protocol>() {{
                                                            put("TCP", Protocol.TCP);
                                                            put("UDP", Protocol.UDP);}};


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


    public void loopPcap(PacketHandler handler, String protocolType) throws IOException {
        if (pcap == null) {
            return;
        }
//      if (filter == null) {
//          try {
//              pcap.loop(handler);
//          } catch (IOException e) {
//              Log.e(TAG, "Error looping over pcap", e);
//          }
        else {
          pcap.loop(packet -> {
              if (packet.hasProtocol(protocolHashMap.get(protocolType))) {
                  System.out.println(packet.getPacket(protocolHashMap.get(protocolType)).getPayload());
              }
              return true;
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



