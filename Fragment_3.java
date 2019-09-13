package com.example.bff30;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.TextView;

import com.github.mikephil.charting.data.PieEntry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Fragment_3 extends Fragment {

    private RecyclerView recyclerViewTransfer;
    private ItemTransferAdapter adapterTransfer;
    private RecyclerView.LayoutManager layoutManagerTransfer;
    private ArrayList<ItemTransfer> itemsTransfer = new ArrayList<ItemTransfer>();
    private TextView textViewMyBalance;
    private TextView textViewThey;
    private TextView textViewI;
    private SwipeRefreshLayout swipeTransfersRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3_layout,container,false);
        //itemsTransfer = new ArrayList<ItemList>();
        textViewMyBalance = view.findViewById(R.id.textViweMyBalance);
        textViewThey = view.findViewById(R.id.textViewThey);
        textViewI = view.findViewById(R.id.textViewI);
        recyclerViewTransfer = view.findViewById(R.id.recyclerViewMyTransfer);
        recyclerViewTransfer.setHasFixedSize(true);
        layoutManagerTransfer = new LinearLayoutManager(recyclerViewTransfer.getContext());
        adapterTransfer = new ItemTransferAdapter(itemsTransfer);
        recyclerViewTransfer.setLayoutManager(layoutManagerTransfer);
        recyclerViewTransfer.setAdapter(adapterTransfer);
        swipeTransfersRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutTransfer);
        swipeTransfersRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeTransfersRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        new LoadCosts3().execute();

//        adapterTransfer.setOnItemTransferClickListener(new ItemTransferAdapter.OnItemTransferClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Dialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
//                builder.setTitle("TITLE");
//                String ms="MS";
//                builder.setMessage(ms);
//                builder.create();
//                builder.show();
//            }
//        });

        swipeTransfersRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadCosts3().execute();
            }
        });
        return view;
    }
    public class LoadCosts3 extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private String query2;
        private String query3;
        private Statement stmt;
        private Statement stmt2;
        private ResultSet rsCosts;
        private ResultSet rsperUser;
        private ResultSet rsUser2;
        private ResultSet rsUser;
        private ResultSet rs;
        private GlobalData gd;
        private float i;
        private float they;
        private String p;
        private String s;

        @Override
        protected void onPreExecute() {
            itemsTransfer.clear();
        }

        @Override
        protected void onPostExecute(String r) {
            textViewThey.setText(Float.toString(they));
            textViewI.setText(Float.toString(i));
            textViewMyBalance.setText(Float.toString((i-they)));
            adapterTransfer.notifyDataSetChanged();
            swipeTransfersRefreshLayout.setRefreshing(false);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                con = connectionClass.CONN();
                if (con == null) {
                    isSuccess = false;
                    z = "Error in connection with SQL server";
                    Log.e("ERROR0:", z);
                } else {
                    stmt = con.createStatement();
                    stmt2 = con.createStatement();

                    //----------------------------------------------------------------------------------------
                    query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                            " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                            " WHERE ch.Cost_Settlements_Id IS NULL" +
                            " AND ch.Cost_Declarant_User_Id=" + gd.getIDUserOnline();
                    Log.d("QUERY",query);
                    rsperUser = stmt.executeQuery(query);
                    rsperUser.next();
                    i = rsperUser.getFloat(1);
                    //----------------------------------------------------------------------------------------
                    query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                            " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                            " WHERE ch.Cost_Settlements_Id IS NULL"+
                            " AND cr.Cost_Row_Debtor_Id = " + gd.getIDUserOnline();
                    Log.d("QUERY",query);
                    rsCosts = stmt.executeQuery(query);
                    rsCosts.next();
                    they = rsCosts.getFloat(1);
                    //----------------------------------------------------------------------------------------
                    query = "UPDATE Users SET Balance = " + Float.toString(i-they)  +  "WHERE Id = " + gd.getIDUserOnline();
                    Log.d("QUERY",query);
                    stmt.executeUpdate(query);
                    //---------------------------------------------------------------------------------------
                    query = "SELECT * FROM Transfers WHERE (Transfer_Paid != 1 OR Transfer_Sent != 1) AND (Transfer_Sender_Id = " + gd.getIDUserOnline() + " OR Transfer_Recipient_Id = " + gd.getIDUserOnline() + ")";
                    Log.d("QUERY",query);
                    rs = stmt.executeQuery(query);
                    while (rs.next()){

                        query2 = "SELECT Name, Surname FROM Users WHERE Id = " + rs.getInt(3);
                        Log.e("QUERY", query2);
                        query3 = "SELECT Name, Surname FROM Users WHERE Id = " + rs.getInt(4);
                        Log.e("QUERY", query3);

                        rsUser = stmt2.executeQuery(query2);
                        rsUser.next();
                        s = rsUser.getString(1) + " " + rsUser.getString(2);

                        rsUser2 = stmt2.executeQuery(query3);
                        rsUser2.next();
                        p = rsUser2.getString(1) + " " + rsUser2.getString(2);

                        itemsTransfer.add(new ItemTransfer(
                                   rs.getInt(1),
                                   rs.getString(2),
                                   rs.getInt(3),
                                   s,
                                   rs.getInt(4),
                                   p,
                                   rs.getFloat(5),
                                   rs.getInt(6),
                                   rs.getString(7),
                                   rs.getInt(8),
                                   rs.getInt(9)));

                                   Log.e("ID ",Integer.toString(rs.getInt(1)));           //INT
                                   Log.e("TITLE: ",rs.getString(2));        //STRING
                                   Log.e("ID ",Integer.toString(rs.getInt(3)));           //INT
                                   Log.e("NAME: ",s);                      //STRING
                                   Log.e("ID ",Integer.toString(rs.getInt(4)));           //INT
                                   Log.e("SURNAME: ",p);                      //STRING
                                   Log.e("VAL: ",Float.toString(rs.getFloat(5)));         //FLOAT
                                   Log.e("SEL: ",Integer.toString(rs.getInt(6)));           //INT
                                   Log.e("DATA: ",rs.getString(7));        //STRING
                                   Log.e("ID ",Integer.toString(rs.getInt(8)));           //INT(0,1)
                                   Log.e("ID ",Integer.toString(rs.getInt(9)));           //INT(0,1))
                    }

                    isSuccess = true;
                    z = "Loading sucessfull :)";
                }
            }
            catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }
}
