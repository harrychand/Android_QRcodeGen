package com.intel.hari.qrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button gen,scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gen = (Button) findViewById(R.id.gen);

        gen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                Intent gIntent = new Intent(MainActivity.this, GeneratorActivity.class);
                startActivity(gIntent);}
       });
    }
}
