package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, "PMCL", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String sql = "Create Table vehicle_master (v_id Integer Primary Key Autoincrement, Owner_Name varchar(255),Vehicle_Number_Plate varchar(255) , Model_Name varchar(255), Make varchar(255), Manu_Year varchar(255), Owner_Email varchar(255), Last_Report varchar(255),Last_Register_On DATEIME DEFAULT CURRENT_DATE, flag INTEGER DEFAULT 0)";
        db.execSQL(sql);

        String sql1 = "Create Table  driver_master (driver_id Integer Primary Key Autoincrement, Driver_Name varchar(255) , Contact_No varchar(255) , Email_Address varchar(255) , Birthdate varchar(255) ,Blood_Group varchar(10),Last_Register_on DATEIME DEFAULT CURRENT_DATE)";
        db.execSQL(sql1);


        String sql2 = "Create Table checklist_master(c_id Integer Primary Key Autoincrement,Checked_Item Varchar (255), last_checklist_submitted  DATEIME DEFAULT CURRENT_DATE,v_id Integer, foreign Key (v_id) References driver_master(v_id))";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists driver_master");
        db.execSQL("drop table if exists vehicle_master");
        db.execSQL("drop table if exists checklist_master");
        Log.d("Data Base Upgraded", "Done");

    }
}