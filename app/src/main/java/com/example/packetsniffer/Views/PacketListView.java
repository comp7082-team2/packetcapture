package com.example.packetsniffer.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packetsniffer.Presenters.PacketListPresenter;
import com.example.packetsniffer.R;

public class PacketListView extends AppCompatActivity {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private PacketListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_list_view);
        presenter = new PacketListPresenter();
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mAdapter = new CustomAdapter(presenter.loadPcap());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public void goToGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void applyFilter(View v) {
        filterPcap();
    }

    public void filterPcap() {
        EditText etFilter = (EditText) findViewById(R.id.etFilter);
        String filterExpression = etFilter.getText().toString();
        if (filterExpression == null || filterExpression.isEmpty() || filterExpression.equals("Enter Filter...")) {
            Toast.makeText(this, "Please enter a filter", Toast.LENGTH_SHORT).show();
            return;
        }
        if (presenter.isValidFilter(filterExpression)) {
            mAdapter.updateData(presenter.loadFilteredPcap(filterExpression));
        }

    }
}