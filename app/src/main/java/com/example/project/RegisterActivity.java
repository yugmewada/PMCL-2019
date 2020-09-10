package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_fname, edt_contactno, edt_email, edt_bday, edt_bloodgrp;
    Button reg = null;
    AlertDialog.Builder msgbox;
    DBHelper DatabaseHelper = new DBHelper(this);
    SQLiteDatabase mydb;
    public TextInputLayout textInputFirstName, textInputContactNo, textInputEmail;

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //MAKING REGULAR EXPRESSION FOR  NAME
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    public static final Pattern NAME = Pattern.compile("^[a-zA-Z\\s]*$");

    //!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //MAKING REGULAR EXPRESSION FOR CONTACT
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    public static final Pattern PHONE = Pattern.compile("^[+(000)][0-9]{6,14}$");

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    //ON CREATE METHOD
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //DEFINING TOOLBAR
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


//!------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
        //INTTIALIZING ALL WIDGETS
//!-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!

        textInputFirstName = findViewById(R.id.InputFirstName);
        textInputEmail = findViewById(R.id.InputEmail);
        textInputContactNo = findViewById(R.id.InputPh);




        edt_bday = findViewById(R.id.Birthdate);
        edt_bloodgrp = findViewById(R.id.BloodGroup);
        edt_fname = findViewById(R.id.Firstname);
        edt_contactno = findViewById(R.id.ContactNo);
        edt_email = findViewById(R.id.EmailId);

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
        //DEFINING REGISTER BUTTON AND PUTTING VARIOUS CONDITIONS
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
        reg = findViewById(R.id.Submit);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String fname = edt_fname.getText().toString();
                String con = edt_contactno.getText().toString();
                String email = edt_email.getText().toString();


                try {

                    if (edt_email.getText().toString().isEmpty() && edt_contactno.getText().toString().isEmpty() && edt_fname.getText().toString().isEmpty() && edt_bday.getText().toString().isEmpty() && edt_bloodgrp.getText().toString().isEmpty()) {
                        msgbox = new AlertDialog.Builder(RegisterActivity.this);
                        msgbox.setTitle("Driver Registration");
                        msgbox.setMessage("All Fiellds Are Mandatory Please Provide Correct Data");
                        msgbox.setCancelable(false);
                        msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        msgbox.show();


                    } else if (!NAME.matcher(fname).matches()) {//Validating First Name
                        textInputFirstName.setError("Please Enter Valid Employee Name");
                        Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                        startActivity(intent);



                    } else if (!PHONE.matcher(con).matches()) {//Validating Phone Number
                        textInputContactNo.setError("Please Enter Valid  Contact Number");
                        Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                        startActivity(intent);


                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//Validating Email Address
                        textInputEmail.setError("Please Enter Valid Email Address");


                    } else {
                        textInputFirstName.setError(null);
                        textInputContactNo.setError(null);
                        textInputEmail.setError(null);
                        insertData();
                        edt_fname.setText("");
                        edt_contactno.setText("");
                        edt_email.setText("");
                        edt_bday.setText("");
                        edt_bloodgrp.setText("");
                        edt_fname.requestFocus();
                    }
                } catch (Exception ex) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayout);
                    Snackbar snackbar = Snackbar.make(linearLayout, ex.getMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!
    }//END OF ON CREATE METHOD
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------!

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // FUNCTION FOR INSERT DATA
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void insertData() {

        try {
            mydb = DatabaseHelper.getWritableDatabase();
            String fname = edt_fname.getText().toString();
            String contact_no = edt_contactno.getText().toString();
            String email_add = edt_email.getText().toString();
            String birth_date = edt_bday.getText().toString();
            String blood_group = edt_bloodgrp.getText().toString();

            String sqlinsert = "INSERT INTO driver_master(Driver_Name,Contact_No,Email_Address,Birthdate,Blood_Group)" + "VALUES('" + fname + "','" + contact_no + "','" + email_add + "','" + birth_date + "','" + blood_group + "')";
            mydb.execSQL(sqlinsert);

            msgbox = new AlertDialog.Builder(RegisterActivity.this);
            msgbox.setTitle("Driver Registration Successful..!");
            msgbox.setCancelable(false);
            msgbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent car = new Intent(RegisterActivity.this, CarRegisterActivity.class);
                    startActivity(car);
                }
            });
            msgbox.show();


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}