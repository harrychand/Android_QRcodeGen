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


public class GeneratorActivity extends AppCompatActivity {

    EditText text;
    Button button;
    ImageView image;
    String text2QR;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        text = (EditText) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        image = (ImageView) findViewById(R.id.image);
        pay = (Button) findViewById(R.id.pay);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                text2QR = text.getText().toString().trim();
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
            }
        });
        pay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                Intent gIntent = new Intent(GeneratorActivity.this, pay2Activity.class);
                startActivity(gIntent);}
        });
    }
}
