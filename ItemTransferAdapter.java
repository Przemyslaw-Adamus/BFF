package com.example.bff30;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemTransferAdapter extends RecyclerView.Adapter<ItemTransferAdapter.ExampleViewHolder> {

    public ArrayList<ItemTransfer> exampleList;
    public GlobalData gd = new GlobalData();

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvId;
        public TextView tvPrice;
        public TextView tvTitle;
        public TextView tvPaingName;
        public TextView tvRecipientName;
        public ToggleButton toggleButtonIsSend;
        public ToggleButton toggleButtonIsCome;
        public ImageView imageView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.textViewTransferId);
            tvTitle = itemView.findViewById(R.id.textViewTransferTitle);
            tvPrice = itemView.findViewById(R.id.textViewTrnasferValue);
            tvPaingName = itemView.findViewById(R.id.textViewTransferPaying);
            tvRecipientName = itemView.findViewById(R.id.textViewTransferRecipient);
            imageView =  itemView.findViewById(R.id.imageViewType);
            toggleButtonIsCome =  itemView.findViewById(R.id.toggleButtonCome);
            toggleButtonIsSend =  itemView.findViewById(R.id.toggleButtonPaid);

            toggleButtonIsSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            toggleButtonIsSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public ItemTransferAdapter(ArrayList<ItemTransfer> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ItemTransferAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transfer,viewGroup,false);
        ItemTransferAdapter.ExampleViewHolder evh = new ItemTransferAdapter.ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTransferAdapter.ExampleViewHolder exampleViewHolder, int i) {
        ItemTransfer currentItem = exampleList.get(i);
        exampleViewHolder.tvId.setText("ID: "+currentItem.getId());
        exampleViewHolder.tvTitle.setText(currentItem.getTitle());
        exampleViewHolder.tvPrice.setText(Float.toString(currentItem.getValue()) + " PLN");
        exampleViewHolder.tvPaingName.setText("-: " + currentItem.getSenderName());
        exampleViewHolder.tvRecipientName.setText("+: " + currentItem.getRecipientName());
        if(gd.getIDUserOnline() == currentItem.getSenderId()){
            exampleViewHolder.imageView.setImageResource(R.mipmap.ic_down_transfer);
            //exampleViewHolder.imageView.setBackgroundColor(Color.GREEN);
        }
        else{
            exampleViewHolder.imageView.setImageResource(R.mipmap.ic_up_transfer);
            //exampleViewHolder.imageView.setBackgroundColor(Color.RED);
        }
        if(currentItem.isSend() == 0){
            exampleViewHolder.toggleButtonIsSend.setChecked(false);
        }
        else{
            exampleViewHolder.toggleButtonIsSend.setChecked(true);

        }
        if(currentItem.isPaid() == 0){
            exampleViewHolder.toggleButtonIsCome.setChecked(false);
        }
        else{
            exampleViewHolder.toggleButtonIsCome.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }





   /* public OnItemTransferClickListener onItemTransferClickListener;
    public interface OnItemTransferClickListener{
        void onItemClick(int position);
    }

    public void setOnItemTransferClickListener(OnItemTransferClickListener onItemTransferClickListener) {
        this.onItemTransferClickListener = onItemTransferClickListener;
    }

    public ArrayList<ItemTransfer> exampleList;
    public GlobalData gd = new GlobalData();


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvPrice;
        public TextView tvTitle;
        public TextView tvPaingName;
        public TextView tvRecipientName;
        public ToggleButton toggleButtonIsSend;
        public ToggleButton toggleButtonIsCome;
        public ImageView imageView;


        public ExampleViewHolder(@NonNull View itemView, OnItemTransferClickListener onItemTransferClickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textViewTransferTitle);
            tvPrice = itemView.findViewById(R.id.textViewTrnasferValue);
            tvPaingName = itemView.findViewById(R.id.textViewTransferPaying);
            tvRecipientName = itemView.findViewById(R.id.textViewTransferRecipient);
            imageView =  itemView.findViewById(R.id.imageViewType);
            toggleButtonIsCome =  itemView.findViewById(R.id.toggleButtonCome);
            toggleButtonIsSend =  itemView.findViewById(R.id.toggleButtonPaid);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemTransferClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemTransferClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ItemTransferAdapter(ArrayList<ItemTransfer> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transfer,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,onItemTransferClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        ItemTransfer currentItem = exampleList.get(i);
        exampleViewHolder.tvTitle.setText(currentItem.getTitle());
//        exampleViewHolder.tvPrice.setText(Float.toString(currentItem.getValue()) + "PLN");
//        exampleViewHolder.tvPaingName.setText(currentItem.getSenderName());
//        exampleViewHolder.tvRecipientName.setText(currentItem.getRecipientName());
        exampleViewHolder.tvTitle.setText("TITLE");
        exampleViewHolder.tvPrice.setText("50.00" + "PLN");
        exampleViewHolder.tvPaingName.setText("a");
        exampleViewHolder.tvRecipientName.setText("a");
//        if(gd.getIDUserOnline() == currentItem.getSenderId()){
//            //exampleViewHolder.imageView.setImageDrawable();
//            //exampleViewHolder.imageView.setBackgroundColor();
//        }
//        else{
//            //exampleViewHolder.imageView.setImageDrawable();
//            //exampleViewHolder.imageView.setBackgroundColor();
//        }
//        if(currentItem.isSend() == 0){
//            exampleViewHolder.toggleButtonIsSend.setChecked(false);
//        }
//        else{
//            exampleViewHolder.toggleButtonIsSend.setChecked(true);
//
//        }
//        if(currentItem.isPaid() == 0){
//            exampleViewHolder.toggleButtonIsCome.setChecked(false);
//        }
//        else{
//            exampleViewHolder.toggleButtonIsCome.setChecked(true);
//
//        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }*/

    public class SaveCosts extends AsyncTask<boolean[],String,String> {
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Connection conn;
        private ConnectionClass connectionClass = new ConnectionClass();
        private GlobalData gb;
        private Statement stmt;
        int day;
        int month;
        int year;
        int hour;
        int minute;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            Log.e("TAG", r);
            //Toast.makeText(CostsActivity.this,r,Toast.LENGTH_SHORT).show();
//            if(z == "Update is succesfull"){
//
//            }
        }

        @Override
        protected String doInBackground(boolean[]... params) {
            Calendar calendar = Calendar.getInstance();
            minute = calendar.get(Calendar.MINUTE);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            try {
                conn = connectionClass.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                    isSuccess = false;
                } else {
                    stmt = conn.createStatement();

                }
                z = "Update is succesfull";
                isSuccess = true;

            } catch (SQLException e) {
                e.printStackTrace();
                isSuccess = false;
                z = "Exceptions - wrong data";
            }
            return z;
        }
    }
}
