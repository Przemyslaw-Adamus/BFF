package com.example.bff30;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Fragment_2 extends Fragment {//implements OnChartValueSelectedListener {

    private PieChart pieChart;
    private ArrayList<PieEntry> values = new ArrayList<>();
    private BarChart barChart;
    private ArrayList NoOfEmp = new ArrayList();
    private GlobalData gd = new GlobalData();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//----------------------------------------------------------------------------------------------------------
        final View view =  inflater.inflate(R.layout.fragment_2_layout,container,false);
        pieChart = (PieChart)view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
//----------------------------------------------------------------------------------------------------------
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,10);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.LTGRAY);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);

//----------------------------------------------------------------------------------------------------------
        new LoadCosts2().execute();
        return  view;
    }

    public class LoadCosts2 extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private ResultSet rsCosts;
        private ResultSet rsperUser;

        @Override
        protected void onPreExecute() {
            values.clear();
        }

        @Override
        protected void onPostExecute(String r) {
            PieDataSet pieDataSet = new PieDataSet(values,"Costs");
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(3f);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextSize(14);
            pieData.setValueTextSize(Color.BLACK);
            pieChart.animateX(5000);
            pieChart.setData(pieData);
//----------------------------------------------------------------------------------------------
            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Common expenses");
            barChart.animateY(5000);
            BarData data = new BarData(bardataset);
            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart.setData(data);
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
                    float tmp_PLN =0f;
                    for(int i=0; i<gd.getListNames().length; i++){
                        query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                                " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                                " WHERE ch.Cost_Settlements_Id IS NULL" +
                                " AND ch.Cost_Declarant_User_Id=" + gd.getIdUsers()[i];
                        Log.d("QUERY",query);
                        rsperUser = stmt.executeQuery(query);
                        rsperUser.next();
                        tmp_PLN = rsperUser.getFloat(1);
                        if(tmp_PLN!=0f){
                            values.add(new PieEntry(tmp_PLN,gd.getListNames()[i]));
                        }
                    }
//--------------------------------------------------------------------------------------------------------------
                    Calendar calendar = Calendar.getInstance();
                    //day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int year = calendar.get(Calendar.YEAR);
                    for(int i=0; i<=31; i++) {
                        query = "SELECT SUM(Cost_Row_Price_Per_User) FROM Cost_Rows AS cr" +
                                " LEFT JOIN Cost_Headers AS ch ON cr.Cost_Header_Id = ch.Cost_Header_Id" +
                                " WHERE ch.Cost_Declarate_Date like \'%" + i + "-" + month + "-" + year + "%\'";
                        Log.d("QUERY", query);
                        rsCosts = stmt.executeQuery(query);
                        rsCosts.next();
                        NoOfEmp.add(new BarEntry((float)i+1,rsCosts.getFloat(1)));
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
}
