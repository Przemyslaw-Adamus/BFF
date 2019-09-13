package com.example.bff30;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {

    private Button buttonBack;
    private Button buttonNewPassword;
    private Button buttonConfirm;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextNumber;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button buttonSavePassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        buttonBack = findViewById(R.id.buttonBack);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonNewPassword = findViewById(R.id.buttonNewPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextNumber = findViewById(R.id.editTextNumber);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent costsActivity = new Intent(MyAccountActivity.this, CostsActivity.class);
                startActivity(costsActivity);
                finish();
            }
        });

        buttonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MyAccountActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_new_password,null);
                editTextNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
                editTextConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);
                //buttonSavePassword = dialogView.findViewById(R.id.buttonSavePassword);
                builder.setView(dialogView);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editTextConfirmPassword.getText().toString() != editTextNewPassword.getText().toString()){
                            Toast.makeText(MyAccountActivity.this,"Password is not the same!",Toast.LENGTH_LONG);
                        }
                        else{
                            new SaveNewPassword().execute();
                            dialog.dismiss();
                        }
                    }
                });
                builder.create();
                builder.show();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SavePersonalData().execute();
            }
        });

        new LoadPersonalData().execute();
    }

    public class LoadPersonalData extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private ResultSet rs;
        private GlobalData gd = new GlobalData();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MyAccountActivity.this,z, Toast.LENGTH_LONG);

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
                    query = "SELECT * FROM Users WHERE Id = " + gd.getIDUserOnline();
                    rs = stmt.executeQuery(query);
                    rs.next();
                    editTextName.setText(rs.getString(4));
                    editTextSurname.setText(rs.getString(5));
                    editTextNumber.setText(rs.getString(8));
                    z = "Load is sucessfull :)";
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }

    public class SaveNewPassword extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private ResultSet rs;
        private GlobalData gd = new GlobalData();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MyAccountActivity.this,z, Toast.LENGTH_LONG);
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
                    query = "UPDATE Users SET Password = " + editTextNewPassword.getText().toString() + " WHERE Id = " + gd.getIDUserOnline();
                    Log.i("QERY",query);
                    stmt.executeQuery(query);
                    z = "Load is sucessfull :)";
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }

    public class SavePersonalData extends AsyncTask<String, String, String> {

        private ConnectionClass connectionClass = new ConnectionClass();
        private Connection con;
        private String z = "";
        private Boolean isSuccess = false;
        private String query;
        private Statement stmt;
        private ResultSet rs;
        private GlobalData gd = new GlobalData();

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MyAccountActivity.this,z, Toast.LENGTH_LONG);
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
                    query = "UPDATE Users SET Account_number = " + editTextNumber.getText().toString() + " WHERE Id = " + gd.getIDUserOnline();
                    Log.d("QERY",query);
                    stmt.executeQuery(query);
                    z = "Load is sucessfull :)";
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }
}
