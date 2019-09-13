package com.example.bff30;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

private ItemAdapter adapter;

public SwipeToDeleteCallback(ItemAdapter adapter) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
             this.adapter = adapter;
        }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        int position = viewHolder.getAdapterPosition();
        Log.i("MOVE","MOVE" +position);
        //adapter.editItem(position);
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        //adapter.deleteItem(position);
        Log.i("SWIPED","SWIPED"+position);

    }
};

