package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EstoreOwnerAddProduct extends AppCompatActivity {



    boolean flag=true;
     private String estorownerId;



    private EditText productName;
    private EditText productPrice;
    private EditText productInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_add_product);
        estorownerId=getIntent().getStringExtra("estoreId");







        findViewById(R.id.adddescription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from xml
                productName=findViewById(R.id.addproductname);
                productPrice=findViewById(R.id.addproductprice);
                productInfo=findViewById(R.id.addproductinfo);



                String ProductName=productName.getText().toString();
                String ProductPrice =productPrice.getText().toString();
                String ProductInfo =productInfo.getText().toString();




                flag = true;
                //not empty data
                String []array = new String[]{ProductName,ProductPrice,ProductInfo};
                EditText []editArray =new EditText[]{productName,productPrice,productInfo};
                for (int i=0;i<array.length;i++){
                    if(array[i].isEmpty()) {
                        flag=false;
                        editArray[i].setError("You have to fill it!");
                        editArray[i].requestFocus();

                    }
                }
                if(!flag) return;

                Intent intent4 = new Intent(EstoreOwnerAddProduct.this,EstorOwnerAddProductDescription.class);
                intent4.putExtra("estoreId",estorownerId);//put product id as extra
                intent4.putExtra("proname",ProductName);//put product id as extra
                intent4.putExtra("proprice",ProductPrice);
                intent4.putExtra("proinfo",ProductInfo);
                startActivity(intent4);


            }
        });
















    }//end on create
}//end class
