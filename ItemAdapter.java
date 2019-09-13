package com.example.bff30;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ExampleViewHolder> {

    public ArrayList<ItemList> exampleList;
    public ItemList mRecentlyItem;
    public int mRecentlyItemPosition;
    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ItemAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPrice;
        public TextView tvPayDate;
        public TextView tvCreationDate;
        public TextView tvPaying;
        public TextView tvTitle;

        public ExampleViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.textViewPrice);
            tvPayDate = itemView.findViewById(R.id.textViewPayDate);
            tvCreationDate = itemView.findViewById(R.id.textViewCreationDate);
            tvPaying = itemView.findViewById(R.id.textViewTransferPaying);
            tvTitle = itemView.findViewById(R.id.textViewTitleSettlement);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    if(onItemLongClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemLongClickListener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }

    public ItemAdapter(ArrayList<ItemList> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,onItemClickListener,onItemLongClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        ItemList currentItem = exampleList.get(i);
        exampleViewHolder.tvPrice.setText(currentItem.getTotalPrice() + " " + currentItem.getCurrency());
        exampleViewHolder.tvCreationDate.setText(currentItem.getCreationDate());
        exampleViewHolder.tvPayDate.setText(currentItem.getPayDate());
        exampleViewHolder.tvPaying.setText(currentItem.getPaying());
        exampleViewHolder.tvTitle.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    public void deleteItem(int position) {

    }

    public void editItem(int position) {

    }

}
