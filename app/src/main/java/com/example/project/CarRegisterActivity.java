package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CarRegisterActivity extends AppCompatActivity {
    Button button;
    EditText edt_owner_name, edt_number_pate, edt_model_name, edt_make, edt_manu_year, edt_owner_email, edt_lastrepoter_name;
    TextInputLayout textInputOwnerName, textInputEmail;
    String ownername, numberplate, modelname, make, manufacyr, email, lastreportersname;
    AlertDialog.Builder msgbox;
    DBHelper DatabaseHelper = new DBHelper(this);
    SQLiteDatabase mydb;

    //!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //MAKING REGULAR EXPRESSION FOR  NAME
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    public static final Pattern NAME = Pattern.compile("^[a-zA-Z\\s]*$");

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //ON CREATE METHOD
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!

    public void onCreate(Bundle bb) {
        super.onCreate(bb);
        setContentView(R.layout.activity_car_register);

        button = (Button) findViewById(R.id.AddVeh);

        edt_owner_name = findViewById(R.id.OwnerName);
        edt_number_pate = findViewById(R.id.CarNoPlate);
        edt_model_name = findViewById(R.id.CarModelName);
        edt_make = findViewById(R.id.CarMake);
        edt_manu_year = findViewById(R.id.ManufacturerYear);
        edt_owner_email = findViewById(R.id.OwnerEmail);
        edt_lastrepoter_name = findViewById(R.id.LastReporSubmitedBy);

        textInputOwnerName = findViewById(R.id.InputOwnerName);
        textInputEmail = findViewById(R.id.InputOwnerEmail);


//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //DEFINING TOOLBAR
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ownername = edt_owner_name.getText().toString().trim();
                email = edt_owner_email.getText().toString().trim();


                try {

                    if (edt_owner_email.getText().toString().isEmpty() && edt_owner_name.getText().toString().isEmpty()) {
                        msgbox = new AlertDialog.Builder(CarRegisterActivity.this);
                        msgbox.setTitle("Vehicle Registration");
                        msgbox.setMessage("All Fiellds Are Mandatory Please Provide Correct Data");
                        msgbox.setCancelable(false);
                        msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CarRegisterActivity.this, CarRegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        msgbox.show();


                    } else if (!NAME.matcher(ownername).matches()) {//Validating First Name
                        textInputOwnerName.setError("Please Enter Valid Employee Name");
                        Intent intent = new Intent(CarRegisterActivity.this, CarRegisterActivity.class);
                        startActivity(intent);


                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//Validating Email Address
                        textInputEmail.setError("Please Enter Valid Email Address");


                    } else {
                        textInputOwnerName.setError(null);
                        textInputEmail.setError(null);
                        insertData();
                        edt_owner_name.setText("");
                        edt_owner_email.setText("");
                        edt_number_pate.setText("");
                        edt_model_name.setText("");
                        edt_make.setText("");
                        edt_manu_year.setText("");
                        edt_lastrepoter_name.setText("");
                        edt_owner_name.requestFocus();
                    }
                } catch (Exception ex) {
                    Toast.makeText(CarRegisterActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // FUNCTION FOR INSERT DATA
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void insertData() {

        try {
            mydb = DatabaseHelper.getWritableDatabase();

            numberplate = edt_number_pate.getText().toString().trim();
            modelname = edt_model_name.getText().toString().trim();
            make = edt_make.getText().toString().trim();
            manufacyr = edt_manu_year.getText().toString().trim();
            ownername = edt_owner_name.getText().toString().trim();
            email = edt_owner_email.getText().toString().trim();
            lastreportersname = edt_lastrepoter_name.getText().toString().trim();


            String insert = "insert into vehicle_master(Owner_Name,Vehicle_Number_Plate,Model_Name, Make, Manu_Year,Owner_Email,Last_Report,flag) values('" + ownername + "','" + numberplate + "' , '" + modelname + "' , '" + make + "','" + manufacyr + "','" + email + "', '" + lastreportersname + "','1')";
            mydb.execSQL(insert);

            msgbox = new AlertDialog.Builder(CarRegisterActivity.this);
            msgbox.setTitle("Car Registration Successful..!");
            msgbox.setCancelable(false);
            msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent car = new Intent(CarRegisterActivity.this, HomePageActivity.class);
                    startActivity(car);
                }
            });
            msgbox.show();


        } catch (Exception e) {
            msgbox = new AlertDialog.Builder(CarRegisterActivity.this);
            msgbox.setTitle("Error..!");
            msgbox.setMessage("Could Not Register Such Data");
            msgbox.setCancelable(false);
            msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent car = new Intent(CarRegisterActivity.this, CarRegisterActivity.class);
                    startActivity(car);

                }
            });
        }
    }
}

