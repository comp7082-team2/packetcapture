package com.example.packetsniffer.Presenters;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.example.packetsniffer.Models.PcapRepository;
import com.example.packetsniffer.Utils.FileLoader;

public class MainPresenter {

    private PcapRepository pcapRepository;

    public MainPresenter() {
        pcapRepository = PcapRepository.getInstance();
    }

    public void parsePcapFileFromUri(Context context, Uri uri) {
        new FileLoader(context,
                new FileLoader.AsyncResponse(){
            @Override
            public void fileLoadFinish(String result){
                Toast.makeText(context, "Successfully loaded file " + result, Toast.LENGTH_SHORT).show();
                pcapRepository.readPcap(result);
            }
        }).execute(uri);
    }
}
