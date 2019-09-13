package com.example.bff30;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

public class Fragment_4 extends Fragment {

    private static final int DIALOG_SETTLEMENT_ROW =0;
    private RecyclerView recyclerViewSettlement;
    private ItemSettlementAdapter itemSettlementAdapter;
    private RecyclerView.LayoutManager layoutSettlementManager;
    private Button buttonAddItemSettlement;
    private ArrayList<ItemSettlementList> itemsSettlementList;
    private SwipeRefreshLayout swipeSettlementRefreshLayout;
    private GlobalData gd;
    private boolean [] checkedItems;


@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_4_layout,container,false);
        itemsSettlementList = new ArrayList<ItemSettlementList>();
        buttonAddItemSettlement = view.findViewById(R.id.buttonAddSettlementItem);
        recyclerViewSettlement = view.findViewById(R.id.recyclerViewSettlement);
        recyclerViewSettlement.setHasFixedSize(true);
        layoutSettlementManager = new LinearLayoutManager(recyclerViewSettlement.getContext());
        itemSettlementAdapter = new ItemSettlementAdapter(itemsSettlementList);
        recyclerViewSettlement.setLayoutManager(layoutSettlementManager);
        recyclerViewSettlement.setAdapter(itemSettlementAdapter);
        swipeSettlementRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutSettlement);
        swipeSettlementRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeSettlementRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        gd = new GlobalData();

        new Load4().execute();

        itemSettlementAdapter.setOnItemSettlementClickListener(new ItemSettlementAdapter.OnItemSettlementClickListener() {
            @Override
            public void onItemClick(int position) {
                Dialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                    builder.setTitle("COSTS");
                    String ms="";
                    for(String s : itemsSettlementList.get(position).getListCosts()){
                        if(s!=null){
                            ms = ms + s +'\n';
                        }
                    }
                    builder.setMessage(ms);
                    /*builder.setMultiChoiceItems(itemsSettlementList.get(position+1).getListCosts(), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        }
                    });

                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });*/
                    builder.create();
                    builder.show();
            }
        });

        buttonAddItemSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gd.getIDUserOnline() == 4){
                   SaveSettlement saveSettlement = new SaveSettlement();
                   saveSettlement.execute();
                }
                else{
                    Toast.makeText(view.getContext(),"nu nu ! You are not ADMIN :)", Toast.LENGTH_LONG);
                    Log.i("INFO", "nu nu ! You are not ADMIN :)");
                }
            }
        });

        swipeSettlementRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Load4().execute();
            }
        });

        return view;
    }

    public class Load4 extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private String query2;
        private Statement stmt;
        private Statement stmt2;
        private ResultSet rs;
        private Float sum;
        private int tmpIdSettlement;
        private String date;
        private String[] tmp_list;
        private ResultSet rs_list;


        @Override
        protected void onPreExecute() {
            itemsSettlementList.clear();
        }

        @Override
        protected void onPostExecute(String r) {
            swipeSettlementRefreshLayout.setRefreshing(false);
            itemSettlementAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                    Log.e("ERROR0:", z);
                } else {
                    stmt = con.createStatement();
                    stmt2 = con.createStatement();
                    query = "SELECT * FROM Settlements";
                    rs = stmt.executeQuery(query);
                    while (rs.next()){
                        sum = rs.getFloat(2);
                        tmpIdSettlement = rs.getInt(1);
                        date = rs.getString(3);
                        query2 = "SELECT * FROM Cost_Headers WHERE Cost_Settlements_Id = " + tmpIdSettlement;
                        Log.e("QUERY:", query2);
                        rs_list = stmt2.executeQuery(query2);
                        tmp_list = new String[100];
                        int i =0;
                        while (rs_list.next()){
                            tmp_list[i] =rs_list.getString(7) + " " +rs_list.getString(8) + "\t   - " + rs_list.getString(3);
                            i++;
                        }
                        itemsSettlementList.add(new ItemSettlementList(sum,"PLN", "Settlement" + tmpIdSettlement, date,tmp_list));
                    }
                }
            }
            catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }

    public class SaveSettlement extends AsyncTask<String,String,String> {
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private Connection conn;
        private ConnectionClass connectionClass = new ConnectionClass();
        private GlobalData gd;
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private ResultSet rsperUser;
        private ResultSet rsCosts;
        private float they;
        private float am;
        private Map<Integer, Float> balances = new HashMap<Integer, Float>();

        @Override
        protected void onPreExecute() {
            Calendar calendar = Calendar.getInstance();
            minute = calendar.get(Calendar.MINUTE);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }

        @Override
        protected void onPostExecute(String r) {
            Log.e("TAG",r);
            Toast.makeText(getView().getContext(),r,Toast.LENGTH_SHORT).show();
            new Load4().execute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {
            ResultSet rs_tmp;
            ResultSet rs_sum;
            float sum;
            try {
                conn = connectionClass.CONN();
                if (conn == null) {
                    z = "Error in connection with SQL server";
                    isSuccess=false;
                } else {
                    stmt = conn.createStatement();

                    query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                            " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                            " WHERE ch.Cost_Settlements_Id IS NULL";
                    Log.d("QUERY",query);
                    rs_sum = stmt.executeQuery(query);
                    rs_sum.next();
                    sum = rs_sum.getFloat(1);
                    if(sum == 0){
                        z = "No costs = no settlement";
                        isSuccess=false;
                        return z;
                    }

                    rs_tmp = stmt.executeQuery("SELECT MAX(Settlement_Id) FROM Settlements");
                    rs_tmp.next();
                    int tmpIdSettlement = rs_tmp.getInt(1)+1;

                    StringBuilder query_tmp = new StringBuilder()
                            .append("INSERT INTO Settlements(Sum,Date_created,Description) VALUES(")
                           // .append(tmpIdSettlement)
                            //.append(",")
                            .append(sum)
                            .append(",")
                            .append(String.format("\'%d-%d-%d %d:%d\'", day, month+1, year,hour,minute))
                            .append(",\'")
                            .append("Settlement" + tmpIdSettlement)
                            .append("\')");
                    Log.d("QUERY",query_tmp.toString());
                    stmt.executeUpdate(query_tmp.toString());
                    itemsSettlementList.add(new ItemSettlementList(sum,"PLN", "Settlement" + tmpIdSettlement, String.format("\'%d-%d-%d %d:%d\'", day, month+1, year,hour,minute),new String[100]));
                    query = "UPDATE Cost_Headers SET Cost_Settlements_Id = " + tmpIdSettlement +  "WHERE Cost_Settlements_Id IS NULL";
                    Log.d("QUERY",query);
                    stmt.executeUpdate(query);

                    for(int i=0; i<gd.getIdUsers().length;i++){
                        //----------------------------------------------------------------------------------------
                        query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                                " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                                " WHERE ch.Cost_Settlements_Id = " + tmpIdSettlement +
                                " AND ch.Cost_Declarant_User_Id=" + gd.getIdUsers()[i];
                        Log.d("QUERY",query);
                        rsperUser = stmt.executeQuery(query);
                        rsperUser.next();
                        am = rsperUser.getFloat(1);
                        //----------------------------------------------------------------------------------------
                        query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                                " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                                " WHERE ch.Cost_Settlements_Id = " + tmpIdSettlement +
                                " AND cr.Cost_Row_Debtor_Id = " + gd.getIdUsers()[i];
                        Log.d("QUERY",query);
                        rsCosts = stmt.executeQuery(query);
                        rsCosts.next();
                        they = rsCosts.getFloat(1);
                        //----------------------------------------------------------------------------------------
                        query = "UPDATE Users SET Balance = " + Float.toString(am-they)  +  "WHERE Id = " + gd.getIdUsers()[i];
                        Log.d("QUERY",query);
                        stmt.executeUpdate(query);
                        balances.put(gd.getIdUsers()[i],am-they);
                        //----------------------------------------------------------------------------------------
                    }

                    do{
                        Map.Entry<Integer, Float> maxEntry = null;
                        Map.Entry<Integer, Float> minEntry = null;
                        sum = 0f;

                        for (Map.Entry<Integer, Float> entry : balances.entrySet())
                        {
                            if (maxEntry == null || entry.getValue() > maxEntry.getValue())
                            {
                                maxEntry = entry;
                            }
                            if (minEntry == null || entry.getValue() < minEntry.getValue())
                            {
                                minEntry = entry;
                            }
                        }

                        if(maxEntry.getValue() > 0f && minEntry.getValue() < 0f){
                            if(Math.abs(minEntry.getValue()) > maxEntry.getValue()){
                                balances.remove(maxEntry.getKey(),maxEntry.getValue());
                                balances.replace(minEntry.getKey(),minEntry.getValue(),minEntry.getValue()+ maxEntry.getValue());

                            }
                            else {
                                balances.remove(minEntry.getKey(),minEntry.getValue());
                                balances.replace(maxEntry.getKey(),maxEntry.getValue(),maxEntry.getValue()+ minEntry.getValue());
                            }
                            if(minEntry.getValue() > 0.01){
                                StringBuilder query_tmp2 = new StringBuilder()
                                        .append("INSERT INTO Transfers(Transfer_Title,Transfer_Sender_Id,Transfer_Recipient_Id,Transfer_Value,Transfer_Settlement_Id,Transfer_Date_Created,Transfer_Paid,Transfer_Sent) VALUES(")
                                        .append("\'")
                                        .append(minEntry.getKey()+"_to_"+maxEntry.getKey())
                                        .append("\',")
                                        .append(minEntry.getKey())
                                        .append(",")
                                        .append(maxEntry.getKey())
                                        .append(",")
                                        .append(Math.abs(minEntry.getValue()))
                                        .append(",")
                                        .append(tmpIdSettlement)
                                        .append(",")
                                        .append(String.format("\'%d-%d-%d %d:%d\'", day, month+1, year,hour,minute))
                                        .append(",")
                                        .append("0")
                                        .append(",")
                                        .append("0)");

                                Log.d("QUERY",query_tmp2.toString());
                                stmt.executeUpdate(query_tmp2.toString());
                            }

                        }
                        else{
                            z = "Update is succesfull - surely :p";
                            isSuccess=true;
                            return z;
                        }

                    }while(balances.size() > 1);
                    z = "Update is succesfull";
                    isSuccess=true;
                }
            }catch (SQLException e) {
                e.printStackTrace();
                isSuccess=false;
                z = "Exceptions - wrong data";
            }
            return z;
        }
    }



}


