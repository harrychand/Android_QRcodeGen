package com.intel.hari.qrcode;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GeneratorActivity extends AppCompatActivity {

    EditText name_text,carplate_text,phonenum_text;
    Button generate_button;
    ImageView image;

    String name_string,carplate_string,phonenum_string,text2QR;

    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        name_text = (EditText) findViewById(R.id.name);
        carplate_text = (EditText) findViewById(R.id.car_plate);
        phonenum_text = (EditText) findViewById(R.id.phone_number);

        generate_button = (Button) findViewById(R.id.qr);
        image = (ImageView) findViewById(R.id.image);

        pay = (Button) findViewById(R.id.pay);

        generate_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                name_string = name_text.getText().toString().trim();
                carplate_string = carplate_text.getText().toString().trim();
                phonenum_string = phonenum_text.getText().toString().trim();
                text2QR = name_string + "," + carplate_string + "," + phonenum_string;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                final String time = format.format(calendar.getTime());

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }

                pay.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view){
                        Intent pIntent = new Intent(GeneratorActivity.this, pay2Activity.class);
                        pIntent.putExtra("Name",name_string);
                        pIntent.putExtra("CarNum",carplate_string);
                        pIntent.putExtra("PhoneNum",phonenum_string);
                        pIntent.putExtra("TimeIn",time);

                        startActivity(pIntent);}
                });
            }
        });

//
    }
}
