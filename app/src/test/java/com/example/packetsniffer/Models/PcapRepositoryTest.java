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
    }

    @Test
    public void getEntries() {
        assertNotNull(pcapRepository.getEntries("tcp"));
    }

    @Test
    public void getInstance() {
        assertNotNull(PcapRepository.getInstance());
    }

    @Test
    public void setFilename() {
        pcapRepository.setFilename("Test File");
        assertEquals("Test File", pcapRepository.getFilename());
    }
}
