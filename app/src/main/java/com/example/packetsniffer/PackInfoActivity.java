package com.example.packetsniffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


public class PackInfoActivity extends AppCompatActivity {
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_info);


    }


    public void closeActivity(View view) {
        ((PackInfoActivity)c).finish();
    }
}