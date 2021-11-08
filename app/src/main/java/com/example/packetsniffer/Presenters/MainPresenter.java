package com.example.packetsniffer.Presenters;


import android.content.Context;
import android.net.Uri;

import com.example.packetsniffer.Models.PcapParser;
import com.example.packetsniffer.Utils.FileLoader;

public class MainPresenter {

    private PcapParser parser;

    public MainPresenter() {
        parser = new PcapParser();
    }

    public void parsePcapFileFromUri(Context context, Uri uri) {
        new FileLoader(context,
                new FileLoader.AsyncResponse(){
            @Override
            public void fileLoadFinish(String result){
                PcapParser parser = new PcapParser();
                parser.readPcapFile(result);
            }
        }).execute(uri);
    }
}
