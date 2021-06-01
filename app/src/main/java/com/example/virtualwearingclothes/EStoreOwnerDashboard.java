package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class EStoreOwnerDashboard extends AppCompatActivity {
    private String estorownerId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_dashboard);
        estorownerId=getIntent().getStringExtra("estoreId");



        findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EStoreOwnerDashboard.this,EstoreOwnerEditProfile.class);
                intent4.putExtra("estoreId",estorownerId);
                startActivity(intent4);

            }
        });

        findViewById(R.id.addproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EStoreOwnerDashboard.this,EstoreOwnerAddProduct.class);
                intent4.putExtra("estoreId",estorownerId);
                startActivity(intent4);

            }
        });

        findViewById(R.id.showproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EStoreOwnerDashboard.this,EStoreOwnerShowProduct.class);
                intent4.putExtra("estoreId",estorownerId);
                startActivity(intent4);

            }
        });

        findViewById(R.id.showorder).setOnClickListener(new View.OnClickListener() {//not ////////////////////////////////////////////////////
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EStoreOwnerDashboard.this,EstoreOwnerShowOrder.class);
                intent4.putExtra("estoreId",estorownerId);
                startActivity(intent4);

            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(EStoreOwnerDashboard.this,login.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);

            }
        });


    }//end create
}//end class
