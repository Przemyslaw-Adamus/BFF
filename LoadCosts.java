package com.example.bff30;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoadCosts extends AsyncTask<String, String, String> {

    private ConnectionClass connectionClass;
    private String z = "";
    private Boolean isSuccess = false;
    private String query;
    private Statement stmt;
    private ResultSet rsCostsHeader;
    private ResultSet rsCostsRow;
    private ResultSet rsUsers;
    private ArrayList<ItemList> itemsList;

    private int costID;
    private double price;
    private String currency;
    private String createDate;
    private String declarateDate;
    private String title;
    private int payingID;
    private String paying;
    private String description;
    private List<String> debtorsList;



    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String r) {

    }

    @Override
    protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    stmt = con.createStatement();

                    query = "select * from Costs_Header";
                    rsCostsHeader = stmt.executeQuery(query);

                    while(rsCostsHeader.next()){

                        costID = rsCostsHeader.getInt("Cost_Id");

                        query = "select * from Costs_Row where Cost_price_Id = "+costID;
                        rsCostsRow = stmt.executeQuery(query);
                        while (rsCostsRow.next()){
                            debtorsList.add(getDBpaying(rsCostsRow.getInt("Cost_user_Id")));
                        }


                        price = rsCostsHeader.getDouble("Cost_price");
                        currency = rsCostsHeader.getString("Cost_currency");
                        createDate = rsCostsHeader.getString("Cost_create_date");
                        declarateDate = rsCostsHeader.getString("Cost_create_date");
                        title = rsCostsHeader.getString("Cost_ctitle");
                        description = rsCostsHeader.getString("Cost_description");
                        payingID = rsCostsHeader.getInt("Cost_payer_Id");
                        paying = getDBpaying(payingID);

                        itemsList.add(new ItemList(price,currency,title,declarateDate,createDate,paying,debtorsList,description));
                    }
                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = "Exceptions";
            }
        return z;
    }

    private String getDBpaying(int payingID) throws SQLException {
        query = "select * from User where User_Id = " + payingID;
        rsUsers = stmt.executeQuery(query);
        return rsUsers.getString("User_name") + " " + rsUsers.getString("User_surname");
    }

    public List<ItemList> getItemsList() {
        return itemsList ;
    }
}