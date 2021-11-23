package com.example.packetsniffer.Models;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;

public class PcapFilterTest {
    PcapFilter pcapFilter = new PcapFilter();
    PcapRepository pcapRepository = PcapRepository.getInstance();

    @Test
    public void getProtocol() {
        pcapFilter.setProtocol("UDP");
        assertEquals("UDP", pcapFilter.getProtocol());
    }

    @Test
    public void setProtocol() {
        pcapFilter.setProtocol("TCP");
        assertEquals("TCP", pcapFilter.getProtocol());
    }

    @Test
    public void getSrc() {
        pcapFilter.setSrc("192.168.0.18");
        assertEquals("192.168.0.18", pcapFilter.getSrc());
    }

    @Test
    public void setSrc() {
        pcapFilter.setSrc("192.168.0.14");
        assertEquals("192.168.0.14", pcapFilter.getSrc());
    }

    @Test
    public void getDst() {
        pcapFilter.setDst("192.168.0.15");
        assertEquals("192.168.0.15", pcapFilter.getDst());
    }

    @Test
    public void setDst() {
        pcapFilter.setDst("192.168.0.17");
        assertEquals("192.168.0.17", pcapFilter.getDst());
    }

    @Test
    public void getSrcPort() {
        pcapFilter.setSrcPort("44");
        assertEquals("44", pcapFilter.getSrcPort());
    }

    @Test
    public void setSrcPort() {
        pcapFilter.setSrcPort("52");
        assertEquals("52", pcapFilter.getSrcPort());
    }

    @Test
    public void getDstPort() {
        pcapFilter.setDstPort("87");
        assertEquals("87", pcapFilter.getDstPort());
    }

    @Test
    public void setDstPort() {
        pcapFilter.setDstPort("93");
        assertEquals("93", pcapFilter.getDstPort());
    }

    @Test
    public void accept() {
        String basePath = new File("").getAbsolutePath();
        pcapRepository.readPcap(basePath + "\\src\\test\\java\\com\\example\\packetsniffer\\" +
                "Models\\test.pcap");
        assertTrue(pcapFilter.accept(pcapRepository.getEntries("tcp").get(1).getPacket()));
    }
}