package com.example.packetsniffer.Views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.packetsniffer.R;
import com.example.packetsniffer.Utils.FileLoader;
import java.io.IOException;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.packet.Packet;
import io.pkts.packet.TCPPacket;
import io.pkts.protocol.Protocol;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    public static final int PICK_FILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToAnalyze(View view) {
        Intent intent = new Intent(this, PacketListView.class);
        startActivity(intent);
    }

    public void click(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.tcpdump.pcap");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resultData)
    {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if (resultData != null)
            {
                Uri uri = resultData.getData();

                new FileLoader(this,
                        new FileLoader.AsyncResponse(){
                            @Override
                            public void fileLoadFinish(String result){
                                readPcapFile(result);
                            }
                        }).execute(uri);
            }
        }
    }


    private void readPcapFile(String filename) {
        try {
            Pcap pcap = Pcap.openStream(filename);

            pcap.loop(new PacketHandler() {
                @Override
                public boolean nextPacket(Packet packet) throws IOException {
                    Log.d(TAG, "Arrival Time: " + packet.getArrivalTime());

                    if (packet.hasProtocol(Protocol.TCP)) {
                        TCPPacket tcpPacket = (TCPPacket) packet.getPacket(Protocol.TCP);
                        Log.d(TAG, "Seq. #: " + tcpPacket.getSequenceNumber());
                    }

                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}