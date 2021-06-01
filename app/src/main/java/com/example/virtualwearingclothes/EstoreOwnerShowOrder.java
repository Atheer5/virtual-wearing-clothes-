package com.example.virtualwearingclothes;

import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EstoreOwnerShowOrder extends AppCompatActivity {
    private String estorownerId;
    private ArrayList<ShowOrder> orders;
    ShowOrder orderindex;
    private EstoreOwnerShowOrderAdapter showorderAdpater;


    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;
    private DatabaseReference databaseReference5;
    private DatabaseReference databaseReferencedelivered0;
    private DatabaseReference databaseReferencedelivered1;
    private DatabaseReference databaseReferencedelivered2;
    RecyclerView recyclerView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_show_order);
        estorownerId=getIntent().getStringExtra("estoreId");

        recyclerView=findViewById(R.id.recyclervieworder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        orders=new ArrayList<>();

        clearAll();

        GetDataFromFirebase();




    }//end on create
//__________________________________________________________________________________________________________________________________________

    private void GetDataFromFirebase() {

        Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                clearAll();
                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("order");
                        Query query2 = databaseReference2.orderByChild("orderid");
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {

                                    Order order = snapshot2.getValue(Order.class);
                                    if(order.getIsdileverd()==0){
                                    String customerid = order.getCustomerid();
                                    String productid = order.getProductid();
                                    String descid = order.getDescriptionid();

                                    /*---------------------------------------------------------------------------------------------------------------------------------*/
                                    databaseReference3 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                                    Query query3 = databaseReference3.orderByChild("productId").equalTo(productid);
                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot datasnapshot3) {
                                            for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                product prod = snapshot3.getValue(product.class);


                                                //-------------------------------------------------------------------------------------------------------------------------------------
                                                databaseReference4 = databaseReference3.child(snapshot3.getKey()).child("description");
                                                Query query4 = databaseReference4.orderByChild("descId").equalTo(descid);

                                                query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot datasnapshot4) {
                                                        for (DataSnapshot snapshot4 : datasnapshot4.getChildren()) {

                                                            ProductDesc desc = snapshot4.getValue(ProductDesc.class);

                                                            //----------------------------------------------------------------------------------------------------------------------------------

                                                            databaseReference5 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Customers");
                                                            Query query5 = databaseReference5.orderByChild("customerId").equalTo(customerid);

                                                            query5.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot datasnapshot5) {
                                                                    orderindex = new ShowOrder();
                                                                    for (DataSnapshot snapshot5 : datasnapshot5.getChildren()) {
                                                                        Customer customer = snapshot5.getValue(Customer.class);


                                                                        orderindex.setorderid(order.getOrderid());
                                                                        orderindex.setCustomerName(customer.getName());
                                                                        orderindex.setCustomerNumber(customer.getPhoneNumber());

                                                                        orderindex.setOrederprice(prod.getproductPrice());
                                                                        orderindex.setOrderImage(desc.getproductimagePath());
                                                                        orderindex.setOrdersize(desc.getproductSize());

                                                                        //Toast.makeText(getApplicationContext(),"inside for customer"+orderindex.getOrdersize(), Toast.LENGTH_LONG).show();

                                                                        orders.add(orderindex);
                                                                        showorderAdpater = new EstoreOwnerShowOrderAdapter(getApplicationContext(), orders);
                                                                        recyclerView.setAdapter(showorderAdpater);
                                                                        showorderAdpater.notifyDataSetChanged();

                                                                    }//end for customer

                                                                    ////


                                                                }//end on datachange customer

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                }


                                                            });
                                                            //-----------------------------------------------------------------------------------------------------------------------------------------

                                                        }//end for desc

                                                    }//end ondatachange desc

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }


                                                });

//----------------------------------------------------------------------------------------------------------------------------------
                                            }//end for product

                                        }//end onDataChange for desc


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });//end addListenerForSingleValueEvent
                                    /*---------------------------------------------------------------------------------------------------------------------------------*/
                                }//end if not delivered

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




    //____________________________________________________________________________________________________
    private void clearAll(){
        if(orders != null){
            orders.clear();
            if(showorderAdpater != null){
                showorderAdpater.notifyDataSetChanged();
            }
        }
        orders=new ArrayList<>();


    }//end clear

//_____________________________________________________________________________________________________________________


    public void delevirdproduct(View v) {
        databaseReferencedelivered0= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");

        Query query=databaseReferencedelivered0.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshotd) {

                CardView mycard=(CardView)v;
                RelativeLayout r=(RelativeLayout)mycard.getChildAt(0);//get relative
                TableLayout tl=(TableLayout)r.getChildAt(0);
                TableRow myrow=(TableRow)tl.getChildAt(0);//get table
                TextView t=(TextView)myrow.getChildAt(0);//get textview
                String orderid= t.getText().toString();

                if(datasnapshotd.exists()) {
                    for (DataSnapshot snapshot : datasnapshotd.getChildren()) {
                        databaseReferencedelivered1 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("order");
                        Query query2 = databaseReferencedelivered1.orderByChild("orderid").equalTo(orderid);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshotd2) {

                                for (DataSnapshot snapshotd2 : datasnapshotd2.getChildren()) {
                                    databaseReferencedelivered2=databaseReferencedelivered1;
                                    databaseReferencedelivered2.child(snapshotd2.getKey()).child("isdileverd").setValue(1);
                                    Toast.makeText(getApplicationContext(),"order delivered", Toast.LENGTH_LONG).show();

                                    finish();
                                    startActivity(getIntent());

                                }//end for

                            }//end on data change

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }//end on cancelled product
                        });//end product listener
                    }//end for
                     }//end if
            }//end on data change
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }//end on cancelled product


                    });


    }

 //_____________________________________________________________________________________________________________________________________




}//end class;
