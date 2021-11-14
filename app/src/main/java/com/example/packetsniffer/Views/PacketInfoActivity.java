package com.example.packetsniffer.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.packetsniffer.Presenters.PacketInfoPresenter;
import com.example.packetsniffer.R;

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
    public void displaySrc(String src) {
        TextView tvSrc = (TextView) findViewById(R.id.textViewInfoSrc);
        tvSrc.setText(src);
    }

    @Override
    public void displayDst(String dst) {
        TextView tvDst = (TextView) findViewById(R.id.textViewInfoDest1);
        tvDst.setText(dst);
    }
}