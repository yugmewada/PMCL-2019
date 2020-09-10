package com.example.project;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CarUpdateActivity extends AppCompatActivity {
    Button button;
    EditText edt_number_pate, edt_model_name, edt_make, edt_manu_year, edt_driNa;
    String numberplate, modelname, make, manufacyr, drivername;
    AlertDialog.Builder msgbox;
    DBHelper DatabaseHelper = new DBHelper(this);
    SQLiteDatabase mydb;


//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //ON CREATE METHOD
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!

    public void onCreate(Bundle bb) {
        super.onCreate(bb);
        setContentView(R.layout.activity_car_update);

        button = (Button) findViewById(R.id.UpVeh);

        edt_number_pate = findViewById(R.id.UpCarNoPlate);
        edt_model_name = findViewById(R.id.UpCarModelName);
        edt_make = findViewById(R.id.UpCarMake);
        edt_driNa = findViewById(R.id.UpOwnerName);
        edt_manu_year = findViewById(R.id.UpManufacturerYear);


//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //DEFINING TOOLBAR
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edt_number_pate.getText().toString().isEmpty() && edt_make.getText().toString().isEmpty() && edt_manu_year.getText().toString().isEmpty() && edt_model_name.getText().toString().isEmpty()) {
                    msgbox = new AlertDialog.Builder(CarUpdateActivity.this);
                    msgbox.setTitle("Vehicle Updation");
                    msgbox.setMessage("All Fields Are Mandatory Please Provide Correct Data");
                    msgbox.setCancelable(false);
                    msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CarUpdateActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    msgbox.show();
                } else {
                    updateData();
                    edt_model_name.setText("");
                    edt_manu_year.setText("");
                    edt_driNa.setText("");
                    edt_number_pate.setText("");
                    edt_make.setText("");
                    edt_number_pate.requestFocus();

                }
            }
        });
    }

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // FUNCTION FOR UPDATE DATA
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void updateData() {
        try {
            numberplate = edt_number_pate.getText().toString();
            modelname = edt_model_name.getText().toString();
            make = edt_make.getText().toString();
            manufacyr = edt_manu_year.getText().toString();
            drivername = edt_driNa.getText().toString();
            mydb = DatabaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Vehicle_Number_Plate", numberplate);
            contentValues.put("Model_Name", modelname);
            contentValues.put("Make", make);
            contentValues.put(" Manu_Year", manufacyr);

            String where = "Owner_Name = '" + drivername + "' ";
            mydb.update("vehicle_master", contentValues, where, null);


            msgbox = new AlertDialog.Builder(CarUpdateActivity.this);
            msgbox.setTitle("Vehicle Details Are Updated Successfully..!");
            msgbox.setCancelable(false);
            msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent car = new Intent(CarUpdateActivity.this, CarUpdateActivity.class);
                    startActivity(car);
                }
            });
            msgbox.show();

        } catch (Exception er) {
            msgbox = new AlertDialog.Builder(CarUpdateActivity.this);
            msgbox.setTitle("Error..!");
            msgbox.setMessage("Could Not Register Such Data");
            msgbox.setCancelable(false);
            msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent car = new Intent(CarUpdateActivity.this, CarUpdateActivity.class);
                    startActivity(car);
                }
            });
            msgbox.show();
        }
    }
}



