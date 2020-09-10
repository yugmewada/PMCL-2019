package com.example.project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity {

    Button button1, button2, button3;
    DBHelper mydb = new DBHelper(this);

    SQLiteDatabase mysql;
    int flag1;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdtae= new Intent(WelcomeActivity.this, CarUpdateActivity.class);
                startActivity(intentUpdtae);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomeActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        watchData();
    }


    public void watchData() {
        mysql = mydb.getReadableDatabase();

        try {
            Cursor c = mysql.rawQuery("select flag from vehicle_master", null);
            if (c != null) {
                c.moveToFirst();
            }
            do {
                int in = c.getColumnIndex("flag");
                flag1 = c.getInt(in);

            } while (c.moveToNext());
        }

        catch (Exception e) {
        }
        if (flag1 == 0) {
            button1.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent register = new Intent(WelcomeActivity.this, RegisterActivity.class);
                    startActivity(register);

                }
            });
        } else  {
          button1.setVisibility(View.GONE);
        }
    }
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ALERT DIALOGE FOR BACK PRESSED
//!----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void onBackPressed() {
        android.app.AlertDialog.Builder ad = new android.app.AlertDialog.Builder(WelcomeActivity.this);
        ad.setTitle("Do You  Want to Exit From Application? ");
        ad.setCancelable(true);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                onStop();
            }
        });
        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog al = ad.create();
        al.show();
    }
}
