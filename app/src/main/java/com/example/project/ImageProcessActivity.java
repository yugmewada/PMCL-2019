package com.example.project;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfWriter;


public class ImageProcessActivity extends AppCompatActivity {
    ImageView img1, img2, img3, img4;
    Button sbmt;
    Intent in;
    int req = 1;
    String items, SELECTEDITEMS;
    File pdfFile;
    EditText des;
    Document document;
    PdfWriter pdfWriter;
    private int STORAGE_PERMISSION_CODE=1;


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_img_view);
        Intent i = getIntent();
        SELECTEDITEMS = i.getStringExtra("checklist");
        Intent in1 = getIntent();
        items = in1.getStringExtra("checklist");


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //CHECKING SELF PERMISSION
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if(ContextCompat.checkSelfPermission(ImageProcessActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"You Have Already Granted Permission",Toast.LENGTH_LONG).show();
        } else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //INITIALIZING IMAGE VIEW
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        {
            img1 = findViewById(R.id.Image1);
        }
        img2 = findViewById(R.id.Image2);
        img3 = findViewById(R.id.Image3);
        img4 = findViewById(R.id.Image4);

        des = findViewById(R.id.Description);

        sbmt = findViewById(R.id.next);

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //ONCLICK LISTENER FOR EVERY IMAGE VIEW
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = 1;
                Toast.makeText(ImageProcessActivity.this, "Image1 Clicked", Toast.LENGTH_LONG).show();
                selectImage();

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = 2;
                Toast.makeText(ImageProcessActivity.this, "Image2 Clicked", Toast.LENGTH_LONG).show();
                selectImage();

            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = 3;
                Toast.makeText(ImageProcessActivity.this, "Image3 Clicked", Toast.LENGTH_LONG).show();
                selectImage();

            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = 4;
                Toast.makeText(ImageProcessActivity.this, "Image4 Clicked", Toast.LENGTH_LONG).show();
                selectImage();

            }
        });

        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                submitData();
                Intent gm = new Intent(Intent.ACTION_SEND);
                gm.setType("text/plain");
                gm.putExtra(Intent.EXTRA_SUBJECT, "Vehicle Maintanance Request");
                gm.putExtra(Intent.EXTRA_TEXT, "PMCL 2019 © All Rights Resevered");
                gm.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/emulated/0/PMCL 2019/Maintanance Information.pdf")));
                startActivity(Intent.createChooser(gm, "Choose Email Application"));
            }
        });
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //MAKING FUNCTION
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void selectImage() {

        final CharSequence[] options = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageProcessActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(in, req);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @TargetApi(20)
    protected void onActivityResult(int reqcode, int rescode, Intent data) {

        {
            if (reqcode == 1) {
                Bitmap bit = (Bitmap) data.getExtras().get("data");
                img1.setImageBitmap(bit);

            }
            if (reqcode == 2) {
                Bitmap bit = (Bitmap) data.getExtras().get("data");
                img2.setImageBitmap(bit);
            }
            if (reqcode == 3) {
                Bitmap bit = (Bitmap) data.getExtras().get("data");
                img3.setImageBitmap(bit);
            }
            if (reqcode == 4) {
                Bitmap bit = (Bitmap) data.getExtras().get("data");
                img4.setImageBitmap(bit);
            }
            if (reqcode == 20) {
                Uri selectimage = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectimage, filepath, null, null, null);
                c.moveToFirst();
                int columindex = c.getColumnIndex(filepath[0]);
                String picturepath = c.getString(columindex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturepath));
                Log.w("Path Of Image", picturepath);
                img1.setImageBitmap(thumbnail);
            }
        }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    }//END OF onActivityResult METHOD
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //FUNCTION FOR GENERATING PDF
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void submitData() {

        String description = des.getText().toString();
        try {
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //MAKING DIRECTORY
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            File folder = new File(Environment.getExternalStorageDirectory().toString(), "PMCL 2019");
            if (!folder.exists())
                folder.mkdirs();
            Toast.makeText(this,"PDF Generated Named Maintanance Information",Toast.LENGTH_LONG).show();
            pdfFile = new File(folder, "Maintanance Information.pdf");

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //ADDING AND DEFINING AND INITIALIZAATION OF DOCUMENT
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


            document = new Document();
            pdfWriter=PdfWriter.getInstance(document,new FileOutputStream(pdfFile));

            document.open();
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //ADDING PARAGRAPH TO DOCUMENT
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            Paragraph p1 = new Paragraph("Description: " + description);
            Font paraFont = new Font(Font.FontFamily.HELVETICA);
            p1.setAlignment(Paragraph.ALIGN_LEFT);
            p1.setFont(paraFont);
            document.add(p1);

            Paragraph p2 = new Paragraph("Selected Problems Are: " +SELECTEDITEMS+"\n");
            Font paraFont2 = new Font(Font.FontFamily.HELVETICA);
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(paraFont2);
            document.add(p2);

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            //ADDING IMAGE TO DOCUMENT
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        } catch (Exception de) {
            Toast.makeText(ImageProcessActivity.this, de.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //SHOWING PERMISSION RESULT
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"The Permission Granted",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_LONG).show();
        }
    }
}

