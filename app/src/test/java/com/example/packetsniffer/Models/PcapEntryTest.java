package com.example.packetsniffer.Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class PcapEntryTest {
    PcapEntry pcapEntry = new PcapEntry("TCP", 1000000, null);

    @Test
    public void getProtocol() {
        assertEquals("TCP", pcapEntry.getProtocol());
    }

    @Test
    public void setProtocol() {
        pcapEntry.setProtocol("UDP");
        assertEquals("UDP", pcapEntry.getProtocol());
    }

    @Test
    public void getArrivalTime() {
        assertEquals(1000000, pcapEntry.getArrivalTime());
    }

    @Test
    public void setArrivalTime() {
        pcapEntry.setArrivalTime(10);
        assertEquals(10, pcapEntry.getArrivalTime());
    }

    @Test
    public void getPacket() {
        assertNull(pcapEntry.getPacket());
    }
}