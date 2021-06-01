package com.example.virtualwearingclothes;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TryClothes extends AppCompatActivity {
    private String estorownerId;
    private String productId;
    private String descId;
    private String productprice;
    private String productsize;
    private String customerId;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceq;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReferencenewq;

    protected void onCreate(Bundle savedInstanceState) {
        estorownerId = getIntent().getStringExtra("estoreId");
        productId = getIntent().getStringExtra("productId");
        descId = getIntent().getStringExtra("DescId");
       // productprice = getIntent().getStringExtra("productprice");
       // productsize = getIntent().getStringExtra("productsize");
        customerId = getIntent().getStringExtra("customerId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_clothes);
        findViewById(R.id.buybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"estore"+estorownerId+" "+" productId "+ productId +"desc"+descId+"price"+productprice+"size"+productsize, Toast.LENGTH_LONG).show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");

                Query query = databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                changeQuantity();
                                String orderId = databaseReference.push().getKey();
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                Order order = new Order(orderId, customerId, productId, descId, date);
                                databaseReference.child(snapshot.getKey()).child("order").push().setValue(order);



                            }//end for
                        }//end if

                    }//end on data change

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }//end onclick
        });//end buy

    }//end on create
   //_____________________________________________________________________________________________________________________________

    public void changeQuantity(){

Toast.makeText(getApplicationContext(),"Done.", Toast.LENGTH_LONG).show();
        databaseReferenceq = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        Query query=databaseReferenceq.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                        Query query2 = databaseReference2.orderByChild("productId").equalTo( productId);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                if(datasnapshot2.exists()) {
                                    for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {
                                        // product prod = snapshot2.getValue(product.class);
                                        /*---------------------------------------------------------------------------------------------------------------------------------*/
                                        databaseReference3 = databaseReference2.child(snapshot2.getKey()).child("description");
                                        databaseReferencenewq=databaseReference3;
                                        Query query3 = databaseReference3.orderByChild("descId").equalTo(descId);
                                        query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot datasnapshot3) {
                                                ProductDesc desc = null;
                                                for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                    desc = snapshot3.getValue(ProductDesc.class);
                                                    if(Integer.parseInt(desc.getproductQuantity())>0){
                                                        int quan=Integer.parseInt(desc.getproductQuantity())-1;
                                                        desc.setproductQuantity(quan+"");
                                                        databaseReferencenewq.child(snapshot3.getKey()).child("productQuantity").setValue(desc.getproductQuantity());

                                                    }//end if

                                                }//end for desc


                                                //prod.setproductdesc(productdesc);
                                                // products.add(prod);




                                            }//end onDataChange for desc

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });//end addListenerForSingleValueEvent





                                        /*---------------------------------------------------------------------------------------------------------------------------------*/


                                    }//end for product snapshot

                                }//end  if2



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

    }//end change quan
//button to add nearest
//try clothes
}//end class