package com.example.packetsniffer.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.packetsniffer.Presenters.MainPresenter;
import com.example.packetsniffer.R;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    public static final int PICK_FILE_REQUEST_CODE = 1;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter();
    }

    public void goToAnalyze(View view) {
        Intent intent = new Intent(this, PacketListView.class);
        startActivity(intent);
    }

    public void goToGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
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
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                presenter.parsePcapFileFromUri(this, resultData.getData());
            }
        }
    }

}