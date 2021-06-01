package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EstoreOwnerEditProduct extends AppCompatActivity {
    private String estorownerId;
    private String productId;
    private String desctId;


    //---------------------to get data from database----------------------------------
    private String Name;
    private String Information;
    private String Price;
    private String Size;
    private String Quntity;
    private String ImageUrl;
    private ImageView productimage;
    //.......................................................

    private EditText newPrice;
    private EditText newQuan;
    private EditText newSize;


    //......................................................

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReferenceprice;
    private DatabaseReference databaseReferencesize;
    private DatabaseReference databaseReferencequan;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_edit_product);/////////////////
        estorownerId = getIntent().getStringExtra("estoreId");
        productId = getIntent().getStringExtra("productId");
        desctId = getIntent().getStringExtra("DescId");

      //  Toast.makeText(getApplicationContext(), "productId" + productId + " " + " estorownerId " + estorownerId+" "+"desc id :"+desctId , Toast.LENGTH_LONG).show();
       // Toast.makeText(EstoreOwnerEditProduct.this, ",,,,,,,,,,,,,", Toast.LENGTH_LONG).show();
        //__________________________________________________________________________________________________________________________________________________________________
        //firebase
        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        GetDataFromFirebase();
        //_________________________________________________________________________________________________________________________________________________________________

        //get new data and update

        findViewById(R.id.eeditproductbtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                newPrice=findViewById(R.id.editprice);
                newQuan=findViewById(R.id.editequan);
                newSize=findViewById(R.id.editsize);


                String newprice =newPrice.getText().toString();
                String newquan =newQuan.getText().toString();
                String newsize =newSize.getText().toString();

                ///////////////////////////////////////////////////////////////////////////////////////////////////////////


                Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if(datasnapshot.exists()) {

                            for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                                databaseReferenceprice=databaseReference2;
                                Query query2 = databaseReference2.orderByChild("productId").equalTo( productId);
                                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                        if(datasnapshot2.exists()) {
                                           // Toast.makeText(EstoreOwnerEditProduct.this, datasnapshot2.getChildrenCount() + "mmmm", Toast.LENGTH_LONG).show();

                                            for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {
                                               // product prod = snapshot2.getValue(product.class);

                                               databaseReferenceprice.child(snapshot2.getKey()).child("productPrice").setValue(newprice);
                                                /*---------------------------------------------------------------------------------------------------------------------------------*/
                                                databaseReference3 = databaseReference2.child(snapshot2.getKey()).child("description");
                                                databaseReferencesize = databaseReference3;
                                                databaseReferencequan=databaseReference3;
                                                Query query3 = databaseReference3.orderByChild("descId").equalTo(desctId);
                                                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot datasnapshot3) {

                                                        if(datasnapshot3.exists()) {
                                                            ProductDesc desc = null;
                                                            for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                               // desc = snapshot3.getValue(ProductDesc.class);
                                                                //set data in xml..............................................................................................

                                                                databaseReferencesize.child(snapshot3.getKey()).child("productSize").setValue(newsize);

                                                              databaseReferencequan.child(snapshot3.getKey()).child("productQuantity").setValue(newquan);
                                                                //.............................................................................................................

                                                                Toast.makeText(EstoreOwnerEditProduct.this, "Update successful!", Toast.LENGTH_LONG).show();

                                                              /*  Intent intent = new Intent(EstoreOwnerEditProduct.this,EStoreOwnerDashboard.class);//////////...............................................

                                                                intent.putExtra("estoreId", estorownerId );
                                                                intent.putExtra("productId",productId);
                                                                intent.putExtra("DescId",desctId);
                                                                startActivity(intent);*/
                                                                finish();
                                                                startActivity(getIntent());

                                                            }//end for des


                                                        }//end if desc
                                                    }//end onDataChange for desc

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }

                                                });//end addListenerForSingleValueEvent
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
            ////////

            }
        });//end edit button

    }// end on create

    //__________________________________________________________________________________________________________________________________________________________________

    private void GetDataFromFirebase() {

        Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                        Query query2 = databaseReference2.orderByChild("productId").equalTo( productId);
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                if(datasnapshot2.exists()) {
                                    for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {
                                        product prod = snapshot2.getValue(product.class);
                                        /*---------------------------------------------------------------------------------------------------------------------------------*/
                                        databaseReference3 = databaseReference2.child(snapshot2.getKey()).child("description");
                                        Query query3 = databaseReference3.orderByChild("descId").equalTo(desctId);
                                        query3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot datasnapshot3) {

                                                if(datasnapshot3.exists()) {
                                                ProductDesc desc = null;
                                                for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                    desc = snapshot3.getValue(ProductDesc.class);
                                                    //set data in xml..............................................................................................
                                                    Name = prod.getproductName();
                                                    Information = prod.getproductInfo();
                                                    Price = prod.getproductPrice();
                                                    Quntity = desc.getproductQuantity();
                                                    Size = desc.getproductSize();
                                                    ImageUrl = desc.getproductimagePath();
                                                    //.............................................................................................................

                                                }//end for des


                                            }//end if desc

                                                ((EditText)findViewById(R.id.editproductname)).setText(Name);
                                                ((EditText)findViewById(R.id.editinfo)).setText(Information);
                                                ((EditText)findViewById(R.id.editprice)).setText(Price+" $ ");
                                                ((EditText)findViewById(R.id.editequan)).setText(Quntity);
                                                ((EditText)findViewById(R.id.editsize)).setText(Size);
                                                productimage=(ImageView)findViewById(R.id.editproductimage);
                                                Picasso.get().load(ImageUrl).into(productimage);

                                            }//end onDataChange for desc

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });//end addListenerForSingleValueEvent
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
    }//end GetDataFromFirebase

}//end class
