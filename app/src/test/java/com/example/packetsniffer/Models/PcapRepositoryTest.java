package com.example.packetsniffer.Models;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;

public class PcapRepositoryTest {
    PcapRepository pcapRepository = PcapRepository.getInstance();

    @Test
    public void readPcap() {
        String basePath = new File("").getAbsolutePath();
        pcapRepository.readPcap(basePath + "\\src\\test\\java\\com\\example\\packetsniffer\\Models\\test.pcap");

        assertNotNull(pcapRepository.getPcap());
    }

    @Test
    public void getPcap() {
        assertNotNull(pcapRepository.getPcap());
    }

    @Test
    public void getEntries() {
        assertNotNull(pcapRepository.getEntries());
    }

    @Test
    public void getInstance() {
        assertEquals(pcapRepository, PcapRepository.getInstance());

    }
}