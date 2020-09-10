package com.example.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomePageActivity extends AppCompatActivity {

    AlertDialog.Builder msgBox;
    CheckedTextView ct;
    DBHelper DatabaseHelper = new DBHelper(this);
    SQLiteDatabase mydb;
    String itemholder;
    AlertDialog.Builder msgbox;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_home_screen);
        msgBox = new AlertDialog.Builder(this);
        ct = findViewById(R.id.lan_items);


//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // ARRAY LIST FOR CHECK LIST
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        final ListView chkLi = findViewById(R.id.checkList);
        chkLi.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final String[] items = new String[]{"Fuel Licks", "Oil", "Body And Winds Security", "Tyres/Wheels And Wheel Flixings", "Brake Lines", "Coupling Security", "Electrical Connectors", "Air Built-Up/Leaks", "Spray Suppression Devices", "Vehicle Height", "Excessive Engine Smoke", "Registration Plates", "Seat Belts", "Sufficient Print Rolls", "Steerings", "Brakes", "In Door Lights", "Wipers", "Horn", "Head Lights", "Warrning Lamps", "Rear Mirror", "Side Glasses", "Reflactors", "Fuel Indicator", "Speed O Meter"};
        ArrayAdapter<?> listAdapter = new ArrayAdapter<>(HomePageActivity.this, R.layout.chk_text_view, R.id.lan_items, items);
        chkLi.setAdapter(listAdapter);

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //DISPLAY SELECTED ITEM FUNCTIONALITY
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Button submy = (Button) findViewById(R.id.next1);

        submy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selectedit = chkLi.getCheckedItemPositions(); //SparseBooleanArray used to map Integer to Boolean Value (key(id) to CheckBox state )
                int i = 0;
                if (chkLi.getCheckedItemCount() == 0) {
                    LinearLayout ll = findViewById(R.id.lll);
                    final Snackbar snb = Snackbar.make(ll, "No Option Selected Please Select At least One Option to continue Further", Snackbar.LENGTH_LONG);
                    snb.show();
                    snb.setActionTextColor(Color.RED);
                    snb.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snb.dismiss();
                            chkLi.requestFocus();
                        }
                    });
                }

                else {

                    while (i < selectedit.size()) {
                        if (selectedit.valueAt(i)) {
                            itemholder = itemholder + items[selectedit.keyAt(i)] + "\n";//getting Value
                        }
                        i++;
                    }

                    Intent intent1 = new Intent(HomePageActivity.this, ImageProcessActivity.class);
                    intent1.putExtra("checklist", itemholder);
                    startActivity(intent1);
                    finish();

                    insertData();
                }
            }
        });

//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //DEFINING TOOLBAR
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }
        public void insertData () {
            try {
                mydb = DatabaseHelper.getWritableDatabase();
                mydb.execSQL("Insert into  checklist_master (Checked_Item) values ('"+itemholder+"')");
            }

            catch (Exception assss){
                Toast.makeText(HomePageActivity.this,assss.getMessage(),Toast.LENGTH_LONG ).show();
            }
        }
    }