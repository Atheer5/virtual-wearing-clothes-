package com.example.virtualwearingclothes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomerShowProduct extends AppCompatActivity {
    private String estorownerId;

    private  String customerId;

    private  String productId;
    private static String imageurl;

    private ArrayList<product> products;
    private ArrayList<ProductDesc>productdesc;
    private ShowProductAdapter showProductAdapter;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_show_product);
        estorownerId = getIntent().getStringExtra("estoreId");
        customerId = getIntent().getStringExtra("customerId");


        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        //firebase
        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        products=new ArrayList<>();
        //clear arrray list
        clearAll();

        GetDataFromFirebase();



    }//end on create

    private void GetDataFromFirebase() {

        Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                clearAll();
                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                        Query query2 = databaseReference2.orderByChild("productId");
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                for (DataSnapshot snapshot2 : datasnapshot2.getChildren()){
                                    product prod=snapshot2.getValue(product.class);

                                    /*---------------------------------------------------------------------------------------------------------------------------------*/
                                    databaseReference3 =databaseReference2.child(snapshot2.getKey()).child("description");
                                    Query query3 = databaseReference3.orderByChild("descId");
                                    query3.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot datasnapshot3) {

                                            productdesc=new ArrayList<>();
                                            ProductDesc desc =null;
                                            for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                desc = snapshot3.getValue(ProductDesc.class);
                                                productdesc.add(desc);

                                            }//end for desc



                                            prod.setproductdesc(productdesc);
                                            products.add(prod);

                                            showProductAdapter = new ShowProductAdapter(getApplicationContext(), products);
                                            recyclerView.setAdapter(showProductAdapter);
                                            showProductAdapter.notifyDataSetChanged();


                                        }//end onDataChange for desc


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });//end addListenerForSingleValueEvent





                                    /*---------------------------------------------------------------------------------------------------------------------------------*/


                                }//end for product snapshot



                            }//end on change product

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }//end on cancelled product
                        });//end product listener
                        //____________________________________________________________________________________________________________


                    }//end for e store



                }//end if

            }//end onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });//end addListenerForSingleValueEvent
    }//end GetDataFromFirebase


    private void clearAll(){
        if(products != null){
            products.clear();
            if(showProductAdapter != null){
                showProductAdapter.notifyDataSetChanged();
            }
        }
        products=new ArrayList<>();


    }//end clear

//_____________________________________________________________________________________________________________________

    public void gotodesc(View v) {
        Intent intent = new Intent(CustomerShowProduct.this,CustomerShowProductDescription.class);
        intent.putExtra("estoreId", estorownerId );
        CardView mycard=(CardView)v;
        RelativeLayout r=(RelativeLayout)mycard.getChildAt(0);//get relative
        TableRow mytable=(TableRow)r.getChildAt(0);//get table
        LinearLayout l =(LinearLayout)mytable.getChildAt(1);//get layout
        TextView t=(TextView)l.getChildAt(3);//get textview
        String s= t.getText().toString();
        intent.putExtra("productId",s);

        TextView Price =(TextView)l.getChildAt(1);
        String price= Price.getText().toString();
        intent.putExtra("productprice",price);
        intent.putExtra("customerId",customerId);


        startActivity(intent);


    }

}//end class
