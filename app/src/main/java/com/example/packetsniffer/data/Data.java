package com.example.packetsniffer.data;
import java.time.LocalTime;


public class Data {
    String src;
    String dest;
    long seq;
    long ack;
    int ws;
    LocalTime sqRecTime;
    LocalTime ackRecTime;

    public Data(String src, String dest)
    {
        this.src = src;
        this.dest = dest;

    }

    public void setSrc(String src) {
        this.src = src;
    }
    public String getSrc() {
        return src;
    }
    public void setDest(String dest) {
        this.dest = dest;
    }
    public String getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return "Data{" +
                "src='" + src + '\'' +
                ", dest='" + dest + '\'' +
                ", seq=" + seq +
                ", ack=" + ack +
                ", ws=" + ws +
                ", sqRecTime=" + sqRecTime +
                ", ackRecTime=" + ackRecTime +
                '}';
    }
}
