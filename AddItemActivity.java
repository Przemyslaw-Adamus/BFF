package com.example.bff30;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private static final String[] STUFF = new String[] { "PLN", "EUR","HRK","HUF" };
    boolean[] checkedItems = new boolean[]{true, true, true, true, true, true, true, true, true, true, true};
    private static final int DIALOG_TIME_ID =1;
    private static final int DIALOG_DATE_ID =0;
    private static final int DIALOG_DEEBTORS_ID =2;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private static final String[] listNames = {
            "Adamus Przemysław",
            "Gdowska Paulina",
            "Gurgul Gabriela",
            "Hynek Karol",
            "Kozak Julia",
            "Kubala Weronika",
            "Kubis Justyna",
            "Kurek Kamil",
            "Kusior Miłosz",
            "Opioła Grzegorz",
            "Opioła Joanna"}; // to powinno być pobrane z bazy
    private Button buttonSetDate;
    private Button buttonSetTime;
    private Button buttonAddAdd;
    private Button buttonAddDebtors;
    private AutoCompleteTextView view;
    private DatePickerDialog.OnDateSetListener dpickerListnerDate;
    private TimePickerDialog.OnTimeSetListener dpickerListnerTime;
    private EditText editTextTime;
    private EditText editTextDate;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private TextView textViewDebtorsList;
    private AutoCompleteTextView autoCompleteTextViewCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Calendar calendar = Calendar.getInstance();
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        buttonSetDate = findViewById(R.id.buttonSetDate);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        buttonAddAdd = findViewById(R.id.buttonAddAdd);
        buttonAddDebtors = findViewById(R.id.buttonAddDebtors);
        editTextTime = findViewById(R.id.editTextAddPayTime);
        editTextDate = findViewById(R.id.editTextAddPayDate);
        editTextTime.setText(String.format("%d:%d", hour, minute));
        editTextDate.setText(String.format("%d-%d-%d", day, month+1, year));
        editTextTitle = findViewById(R.id.editTextAddTitle);
        autoCompleteTextViewCurrency = findViewById(R.id.autoCompleteTextViewAddCurrency);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextAddPrice);
        textViewDebtorsList = findViewById(R.id.textViewDebtorsList);
        view = findViewById(R.id.autoCompleteTextViewAddCurrency);

        dpickerListnerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int _year, int _month, int _dayOfMonth) {
                year = _year;
                month = _month+1;
                day = _dayOfMonth;
                editTextDate.setText(String.format("%d-%d-%d", day, month, year));
            }
        };

        dpickerListnerTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int _hourOfDay, int _minute) {
                hour = _hourOfDay;
                minute = _minute;
                editTextTime.setText(String.format("%d:%d", hour, minute));
            }
        };

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showDropDown();
            }
        });
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, STUFF);
        view.setAdapter(adapter);

        buttonSetDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(DIALOG_DATE_ID);
            }
        });

        buttonSetTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(DIALOG_TIME_ID);
            }
        });

        buttonAddDebtors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { showDialog(DIALOG_DEEBTORS_ID); }
        });

        buttonAddAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editTextTitle.getText().toString().contains(".")){
                    if(editTextPrice.getText().length() <= 8){
                        if(editTextTitle.getText().length()>0){
                            //SAVE TO DB
                            SaveCosts saveCosts = new SaveCosts();
                            saveCosts.execute(checkedItems);
                        }
                    }
                }else{
                    if(editTextPrice.getText().length() <= 6){
                        if(editTextTitle.getText().length()>0){
                            //SAVE TO DB
                            SaveCosts saveCosts = new SaveCosts();
                            saveCosts.execute(checkedItems);
                            return;
                        }
                    }
                }



                if(editTextPrice.getText().length() <= 4){
                    if(editTextTitle.getText().length()>0){
                        //SAVE TO DB
                        SaveCosts saveCosts = new SaveCosts();
                        saveCosts.execute(checkedItems);
                        return;
                    }
                    else{
                        Toast.makeText(AddItemActivity.this,"Title is empty!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(AddItemActivity.this,"Price is too long!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        Dialog dialog;
        if(id == DIALOG_DATE_ID) {
            dialog = new DatePickerDialog(this, dpickerListnerDate, year,month,day);
            return dialog;
        }
        else if(id == DIALOG_TIME_ID) {
            dialog = new TimePickerDialog(this,dpickerListnerTime, hour,minute,android.text.format.DateFormat.is24HourFormat(this));
            return dialog;
        }
        else if(id == DIALOG_DEEBTORS_ID){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
            builder.setTitle("Choose items");
            builder.setMultiChoiceItems(listNames, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    Toast.makeText(AddItemActivity.this, "Position: " + which + " Value: " + listNames[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_SHORT).show();
                }
            });

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textViewDebtorsList.setText("");
                    for (int i =0; i<checkedItems.length; i++){
                        if(checkedItems[i]==true){
                            textViewDebtorsList.setText(textViewDebtorsList.getText() + listNames[i] + "\n");
                        }
                    }
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton("All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textViewDebtorsList.setText("");
                    for (int i =0; i<checkedItems.length; i++){
                        checkedItems[i]=true;
                        textViewDebtorsList.setText(textViewDebtorsList.getText() + listNames[i] + "\n");
                    }
                }
            });

            dialog = builder.create();
            return dialog;
        }
        return null;
    }

    public class SaveCosts extends AsyncTask<boolean[],String,String> {
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private Connection conn;
        private ConnectionClass connectionClass = new ConnectionClass();
        private GlobalData gb;
        int day;
        int month;
        int year;
        int hour;
        int minute;


        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(String r) {
            Log.e("TAG",r);
            Toast.makeText(AddItemActivity.this,r,Toast.LENGTH_SHORT).show();
            if(z == "Update is succesfull"){
                Intent costsActivity = new Intent(AddItemActivity.this, CostsActivity.class);
                startActivity(costsActivity);
                finish();
            }
        }

        @Override
        protected String doInBackground(boolean[]... params) {
                ResultSet rs_tmp;
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
                        isSuccess=false;
                    } else {
                    stmt = conn.createStatement();
                    rs_tmp = stmt.executeQuery("SELECT MAX(Cost_Header_Id) FROM Cost_Headers");
                    rs_tmp.next();
                    int tmpIdCost = rs_tmp.getInt(1)+1;
                    GlobalData gd = new GlobalData();
                    StringBuilder query_tmp = new StringBuilder()
                            .append("INSERT INTO Cost_Headers VALUES(")
                            .append(tmpIdCost)
                            //.append(1)
                            .append(",")
                            .append(gd.getIDUserOnline())
                            .append(",\'")
                            .append(editTextTitle.getText().toString())
                            .append("\',\'")
                            .append(editTextDescription.getText().toString())
                            .append("\',")
                            .append(String.format("\'%d-%d-%d %d:%d\'", day, month+1, year,hour,minute))
                            .append(",\'")
                            .append(editTextDate.getText().toString() + " "+editTextTime.getText().toString())
                            .append("\',")
                            .append(editTextPrice.getText().toString())
                            .append(",\'")
                            .append(autoCompleteTextViewCurrency.getText().toString())
                            .append("\',NULL)");
                    Log.d("QUERY",query_tmp.toString());
                    stmt.executeUpdate(query_tmp.toString());
                    rs_tmp = stmt.executeQuery("SELECT MAX(Cost_Row_Id) FROM Cost_Rows");
                    rs_tmp.next();

                    double tmp_x=0.0;
                    for(int i=0; i< checkedItems.length; i++) {
                        if (checkedItems[i] == true) {
                            tmp_x+=1.0;
                        }
                    }
                        double pricePerUser =0.0;
                    switch(autoCompleteTextViewCurrency.getText().toString()){
                        case "PLN":
                            pricePerUser = Double.parseDouble(editTextPrice.getText().toString())/tmp_x;
                            break;
                        case "EUR":
                            pricePerUser = (Double.parseDouble(editTextPrice.getText().toString())/tmp_x)*gd.getEURtoPLN();
                            break;
                        case "HUF":
                            pricePerUser = (Double.parseDouble(editTextPrice.getText().toString())/tmp_x)*gd.getHUFtoPLN();
                            break;
                        case "HRK":
                            pricePerUser = (Double.parseDouble(editTextPrice.getText().toString())/tmp_x)*gd.getHRKtoPLN();
                            break;
                    }
                    BigDecimal bd = new BigDecimal(pricePerUser);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    Log.e("ROUND", Float.toString(bd.floatValue()));
                    for(int i=1; i< checkedItems.length+1; i++){
                        if(checkedItems[i-1] == true){
                            query = "INSERT INTO Cost_Rows " + "VALUES("+(rs_tmp.getInt(1)+i)+","+gd.getIdUsers()[i-1]+","+tmpIdCost+","+bd.floatValue()+")";
                            Log.e("QUERY",query);
                            //query = "INSERT INTO Cost_Rows " + "VALUES("+i+","+i+",1)";
                            stmt = conn.createStatement();
                            stmt.executeUpdate(query);
                        }
                    }
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
