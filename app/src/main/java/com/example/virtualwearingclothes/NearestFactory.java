package com.example.virtualwearingclothes;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NearestFactory extends AppCompatActivity {
    String name;
    String address;
    String phonenum;
    protected void onCreate(Bundle savedInstanceState) {
       name = getIntent().getStringExtra("fname");
        address = getIntent().getStringExtra("faddress");
        phonenum = getIntent().getStringExtra("fphonenum");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_factory);
        ((TextView)findViewById(R.id.factnam)).setText(name);
        ((TextView)findViewById(R.id.factaddres)).setText(address);
        ((TextView)findViewById(R.id.factPhnum)).setText(phonenum);
    }//end on create
}//end class
