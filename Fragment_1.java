package com.example.bff30;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Fragment_1 extends Fragment {
//#####################################################################################################################################
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button buttonAddItem;
    private ArrayList<ItemList> itemsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadCosts loadCosts;
    private View view;
//#####################################################################################################################################
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//-------------------------------------------------------------------------------------------------------------------------------------
        view = inflater.inflate(R.layout.fragment_1_layout,container,false);
//-------------------------------------------------------------------------------------------------------------------------------------
        itemsList = new ArrayList<>();
        buttonAddItem = view.findViewById(R.id.buttonAddItem);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        itemAdapter = new ItemAdapter(itemsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        reloadRecyclerView();
//-------------------------------------------------------------------------------------------------------------------------------------
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadRecyclerView();
            }
        });

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("DEBTORS:");
                String ms="";
                Log.e("POSITION",Integer.toString(position));
                for(String s : itemsList.get(position).getDebtors()){
                    if(s!=null){
                        ms = ms + s +'\n';
                    }
                }
                builder.setMessage(ms);
                builder.create();
                builder.show();
            }
        });

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("DEBTORS:");
                String ms="";
                Log.e("POSITION",Integer.toString(position));
                for(String s : itemsList.get(position).getDebtors()){
                    if(s!=null){
                        ms = ms + s +'\n';
                    }
                }
                builder.setMessage(ms);
                builder.create();
                builder.show();
            }
        });

        itemAdapter.setOnItemLongClickListener(new ItemAdapter.OnItemLongClickListener() {

            @Override
            public void onItemLongClick(int position) {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("DELETE:");
                builder.setIcon(R.mipmap.ic_delete_cost);
                String ms="Do you want to delete this item?";
                Log.e("POSITION",Integer.toString(position));

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DeleteCost deleteCost = new DeleteCost();
                        deleteCost.execute(position);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setMessage(ms);
                builder.create();
                builder.show();
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }
//#####################################################################################################################################
    private void reloadRecyclerView() {
        itemsList.clear();
        //RELOAD FROM DB
        loadCosts = new LoadCosts();
        loadCosts.execute("");
    }
//#####################################################################################################################################
    public class LoadCosts extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private ResultSet rsCostsHeader;
        private String query2;
        private Statement stmt2;
        private ResultSet rsDebtors;

        private int costID;
        private double price;
        private String currency;
        private String createDate;
        private String declarateDate;
        private String title;
        private int payingID;
        private String paying;
        private String description = "Description";
        private List<String> debtorsList = new ArrayList<>();
        private Integer  settlementID;

        @Override
        protected void onPreExecute() {
            debtorsList.clear();
        }

        @Override
        protected void onPostExecute(String r) {
            swipeRefreshLayout.setRefreshing(false);
            itemAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(),z,Toast.LENGTH_LONG);
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
                    query = "SELECT ch.Cost_Header_Id," +
                            "ch.Cost_Declarant_User_Id," +
                            "ch.Cost_Title," +
                            "ch.Cost_Create_Date," +
                            "ch.Cost_Declarate_Date," +
                            "ch.Cost_Value," +
                            "ch.Cost_Currency," +
                            "u.Id,u.Name," +
                            "u.Surname" +
                            //"ch.Cost_Settlements_Id" +
                            " FROM Cost_Headers AS ch LEFT JOIN Users AS u ON ch.Cost_Declarant_User_Id = u.Id" +
                            " WHERE ch.Cost_Settlements_Id IS NULL";
                    rsCostsHeader = stmt.executeQuery(query);
                    while(rsCostsHeader.next()){

                        costID = rsCostsHeader.getInt("Cost_Header_Id");
                        price = rsCostsHeader.getDouble(6);
                        currency = rsCostsHeader.getString(7);
                        createDate = rsCostsHeader.getString(4);
                        declarateDate = rsCostsHeader.getString(5);
                        title = rsCostsHeader.getString(3);
                        payingID = rsCostsHeader.getInt(2);
                        paying = rsCostsHeader.getString(9) + " " + rsCostsHeader.getString(10);
                        //settlementID = rsCostsHeader.getInt(11);

                        query2 = "SELECT u.Name, u.Surname FROM Cost_Rows AS cr" +
                                " LEFT JOIN Users AS u ON cr.Cost_Row_Debtor_Id = u.Id"+
                                " WHERE Cost_Header_Id = " + costID;
                        rsDebtors = stmt2.executeQuery(query2);
                        debtorsList = new ArrayList<>();
                        while (rsDebtors.next()){
                            debtorsList.add(rsDebtors.getString(1) + " " + rsDebtors.getString(2));
                        }
                        itemsList.add(new ItemList(price,currency,title,declarateDate,createDate,paying,debtorsList,description));
                        z = "Load is sucessfull :)";
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
//#####################################################################################################################################
    private class DeleteCost extends AsyncTask<Integer, String, String> {
        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private String query3;
        private String query2;
        private String query4;
        private Statement stmt;
        private Statement stmt2;
        private Statement stmt3;
        private Statement stmt4;
        private ResultSet rs;
        private ResultSet rsId;
        private int id_max_not_steeled;
        private GlobalData gd;

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                    Log.e("ERROR0:", z);
                } else {

                    stmt = con.createStatement();
                    stmt3 = con.createStatement();
                    stmt2 = con.createStatement();
                    stmt4 = con.createStatement();

                    query = "select ROW_NUMBER() OVER(ORDER BY Cost_Header_Id) AS Row#,* from Cost_Headers WHERE Cost_Settlements_Id IS NULL order by 1";
                    Log.v("QUERY",query);
                    rs = stmt.executeQuery(query);
                    int i = integers[0];
                    while (i>=0) {
                        rs.next();
                        i--;
                    }
                        //-------
                    query4 = "SELECT Cost_Declarant_User_Id FROM Cost_Headers WHERE Cost_Header_Id = " + rs.getString(2);
                    Log.v("QUERY",query4);
                    rsId = stmt4.executeQuery(query4);
                    rsId.next();
                    if(rsId.getInt(1)!= gd.getIDUserOnline()){
                        isSuccess = false;
                        z = "The cost can be removed only by the reporting person!!!";
                        Log.e("INFO", z);
                        return z;
                    }
                    query2 = "DELETE FROM Cost_Rows WHERE Cost_Header_Id = " + rs.getString(2);
                    Log.v("QUERY",query);
                    stmt2.execute(query2);
                    query3 = "DELETE FROM Cost_Headers WHERE Cost_Header_Id = " + rs.getString(2);
                    Log.v("QUERY",query);
                    stmt3.execute(query3);
                    itemsList.remove(integers[0]);
                    z = "Deleted is sucessfull :) ";
                }
            }
            catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            //swipeRefreshLayout.setRefreshing(false);
            //temAdapter.notifyDataSetChanged();
            reloadRecyclerView();
            Toast.makeText(getContext(),z,Toast.LENGTH_LONG);
            Log.e("INFO", z);
        }
    }
//#####################################################################################################################################
}

