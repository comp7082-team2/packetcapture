package com.example.packetsniffer.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.packetsniffer.Presenters.PacketInfoPresenter;
import com.example.packetsniffer.R;

import java.util.Arrays;

public class PacketInfoActivity extends AppCompatActivity implements PacketInfoPresenter.View {

    private static final int MISSING_INDEX = -1;
    private PacketInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_info);
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", MISSING_INDEX);
        presenter = new PacketInfoPresenter(index, this);
        presenter.displayPacket();
    }

    public void closeActivity(View view) {
        finish();
    }

    @Override
    public void displayArrivalTime(String time) {
        TextView tvTime = (TextView) findViewById(R.id.textViewInfoArrival);
        tvTime.setText(time);
    }

    @Override
    public void displaySrc(String src) {
        TextView tvSrc = (TextView) findViewById(R.id.textViewInfoSrc);
        tvSrc.setText(src);
    }

    @Override
    public void displaySrcPort(String port) {
        TextView tvSrcPort = (TextView) findViewById(R.id.textViewInfoSrcPort);
        tvSrcPort.setText(port);
    }

    @Override
    public void displayDst(String port) {
        TextView tvDst = (TextView) findViewById(R.id.textViewInfoDest);
        tvDst.setText(port);
    }

    @Override
    public void displayDstPort(String port) {
        TextView tvDstPort = (TextView) findViewById(R.id.textViewInfoDestPort);
        tvDstPort.setText(port);
    }

    @Override
    public void displayProtocol(String protocol) {
        TextView tvProtocol = (TextView) findViewById(R.id.textViewInfoProtocol);
        tvProtocol.setText(protocol);
    }

    @Override
    public void displayTcp(String ack, String seq, String[] flags) {
        View rowAck = findViewById(R.id.tcpRowAck);
        View rowSeq = findViewById(R.id.tcpRowSeq);
        View rowFlags = findViewById(R.id.tcpRowFlags);

        TextView tvAck = (TextView) findViewById(R.id.textViewInfoTcpAck);
        TextView tvSeq = (TextView) findViewById(R.id.textViewInfoTcpSeq);
        TextView tvFlags = (TextView) findViewById(R.id.textViewInfoTcpFlags);

        rowAck.setVisibility(View.VISIBLE);
        rowSeq.setVisibility(View.VISIBLE);
        rowFlags.setVisibility(View.VISIBLE);

        tvAck.setText(ack);
        tvSeq.setText(seq);
        tvFlags.setText(Arrays.toString(flags));
    }
}