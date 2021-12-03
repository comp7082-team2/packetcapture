package com.example.packetsniffer.Views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.packetsniffer.Models.PcapEntry;
import com.example.packetsniffer.Models.PcapFilter;
import com.example.packetsniffer.R;

import java.util.List;

import io.pkts.packet.TransportPacket;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final List<PcapEntry> localDataSet;

    private String filterString; //HACK

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textView2;
        private final TextView textView3;


        public ViewHolder(View view) {
            super(view);
            // TODO: Set onClick listener to open detail view of packet

            textView = (TextView) view.findViewById(R.id.textView);
            textView2 = (TextView) view.findViewById(R.id.src_adr);
            textView3 = (TextView) view.findViewById(R.id.dst_adr);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getSrcAdr() {return textView2;}

        public TextView getDstAdr() {return textView3;}

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(List<PcapEntry> dataSet) {
        localDataSet = dataSet;
        filterString = null; //HACK
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PcapEntry entry = localDataSet.get(position);
        String src_adr = null, dst_adr = null;
        if (PcapFilter.isTransportPacket(entry.getPacket())) {
            TransportPacket packet = PcapFilter.getTransportPacket(entry.getPacket());
            src_adr = packet.getSourceIP();
            dst_adr = packet.getDestinationIP();
            viewHolder.getSrcAdr().setText(String.format("Source: %s", src_adr));
            viewHolder.getDstAdr().setText(String.format("Destination: %s", dst_adr));
        }
        String lineText = entry.getArrivalTime() + " " + entry.getProtocol();
        viewHolder.getTextView().setText(lineText);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, PacketInfoActivity.class);
                int index = viewHolder.getAdapterPosition();
                intent.putExtra("index", index);
                intent.putExtra("filterExpression", filterString);
                context.startActivity(intent);
            }
        });
    }

    public void updateData(List<PcapEntry> dataset, String filterString) {
        localDataSet.clear();
        localDataSet.addAll(dataset);
        this.filterString = filterString;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

