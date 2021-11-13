package com.example.packetsniffer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.packetsniffer.data.Data;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Data[] localDataSet;
    Context context ;


    public CustomAdapter(PacketListView packetListView) {

        context = packetListView;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;
        private final TextView textSrc;
        private final TextView textDest;


        public ViewHolder(View view) {
            super(view);
            textSrc = itemView.findViewById(R.id.textViewSrc);
            textDest = itemView.findViewById(R.id.textViewDest);

            //textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextSrc() {
            return textSrc;
        }
        public TextView getTextDst() {
            return textDest;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(Data[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.test_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context c;
        final Data mydata = localDataSet[position];
        viewHolder.textSrc.setText(mydata.getSrc());
        viewHolder.textDest.setText(mydata.getDest());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data = localDataSet[viewHolder.getAdapterPosition()];
                //plz plz help me to fix this part
                // have no idea how to pass PackInfoActigiy
                /*
                Log.e("", viewHolder.getClass()+"");
                Intent intent;
                intent = new Intent(PackInfoActivity, PackInfoActivity.class);
                intent.putExtra("destination",data.getSrc());
                intent.putExtra("source",data.getDest());
                context.startActivity(intent);
                */
                Log.e("",localDataSet[viewHolder.getAdapterPosition()].toString()+"");
            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }


}

