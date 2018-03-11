package com.intel.hari.qrcode;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class pay2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay2);

        Button pay_button = (Button) findViewById(R.id.pay);

        final ImageView qr_image;
        qr_image = (ImageView) findViewById(R.id.last_qr);

        Bundle bundle = getIntent().getExtras();

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        final String time2 = format2.format(calendar2.getTime());

        final String data_1 = bundle.getString("Name");
        final String data_2 = bundle.getString("CarNum");
        String data_3 = bundle.getString("PhoneNum");
        String data_4 = bundle.getString("TimeIn");

        String[] timeA = data_4.split(":");
        String[] timeB = time2.split(":");

        int a = (Integer.valueOf(timeA[0]));
        int b = (Integer.valueOf(timeB[0]));
        //        int b = 24;

        int c = (b - a);
        int bill = 0;
        int base_pay = 1;
        if (c<=1 ){
            bill = base_pay;
        }
        else if (c>1){
            bill = base_pay + c;
        }
        String paybill = Integer.toString(bill);

        TextView textView1 = (TextView) findViewById(R.id.out1);
        textView1.setText(data_1);
        TextView textView2 = (TextView) findViewById(R.id.out2);
        textView2.setText(data_2);
        TextView textView3 = (TextView) findViewById(R.id.out3);
        textView3.setText(data_3);
        TextView textView4 = (TextView) findViewById(R.id.out4);
        textView4.setText(data_4);
        TextView textView5 = (TextView) findViewById(R.id.out5);
        textView5.setText(time2);
        TextView textView6 = (TextView) findViewById(R.id.out6);
        textView6.setText("RM"+paybill);

        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String text2QR = "PaymentDone";

                String text2QR, name_string, carplate_string;

                name_string = data_1;
                carplate_string = data_2;

                text2QR = "PaymentDone," + name_string+","+carplate_string;


                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    qr_image.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
