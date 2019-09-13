package com.example.bff30;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class ItemSettlementAdapter extends RecyclerView.Adapter<ItemSettlementAdapter.ExampleViewHolder> {

    public OnItemSettlementClickListener onItemSettlementClickListener;

    public interface OnItemSettlementClickListener{
        void onItemClick(int position);
    }

    public void setOnItemSettlementClickListener(OnItemSettlementClickListener onItemSettlementClickListener) {
        this.onItemSettlementClickListener = onItemSettlementClickListener;
    }

    public ArrayList<ItemSettlementList> exampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPrice;
        public TextView tvDate;
        public TextView tvTitle;

        public ExampleViewHolder(@NonNull View itemView, OnItemSettlementClickListener onItemSettlementClickListener) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.textViewSummaryCost);
            tvDate = itemView.findViewById(R.id.textViewSettlementDate);
            tvTitle = itemView.findViewById(R.id.textViewTitleSettlement);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemSettlementClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemSettlementClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ItemSettlementAdapter(ArrayList<ItemSettlementList> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_settlement,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,onItemSettlementClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        ItemSettlementList currentItem = exampleList.get(i);
        exampleViewHolder.tvPrice.setText(currentItem.getTotalPrice() + " " + currentItem.getCurrency());
        exampleViewHolder.tvDate.setText(currentItem.getDate());
        exampleViewHolder.tvTitle.setText(currentItem.getTitle());


    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }
}
