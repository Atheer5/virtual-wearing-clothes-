package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerCart extends AppCompatActivity {
    private String customerId;
    private ArrayList<ShowOrder> orders;
    ShowOrder orderindex;

    private CustomerCartAdapter showorderAdpater;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;
    private DatabaseReference databaseReference5;
    private DatabaseReference databaseReferencedelivered0;
    private DatabaseReference databaseReferencedelivered1;
    private DatabaseReference databaseReferencedelivered2;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cart);
        customerId = getIntent().getStringExtra("customerId");
       recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        orders=new ArrayList<>();

        clearAll();

        GetDataFromFirebase();




//________________________________________________________________________________________________________________________

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_orders);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:

                        Intent intent3 = new Intent(CustomerCart.this,Customermainpage.class);
                        intent3.putExtra("customerId",customerId);
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        return true;



                    case R.id.nav_profile:
                        Intent intent2 = new Intent(CustomerCart.this,CustomerEditProfile.class);
                        intent2.putExtra("customerId",customerId.toString());//new
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(CustomerCart.this,login.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;



                    case R.id.nav_orders:
                        return true;

                    case R.id.nav_real://real
                        Intent intent5 = new Intent(CustomerCart.this,CustomerRealShopping.class);
                        intent5.putExtra("customerId",customerId.toString());//new
                        startActivity(intent5);
                        overridePendingTransition(0,0);
                        return true;

                }



                return false;
            }
        });

//________________________________________________________________________________________________________________________

    }//end on create
    //__________________________________________________________________________________________________________________________________________

    private void GetDataFromFirebase() {
        Query query=databaseReference.orderByChild("estoreOwnerId");
        query.addValueEventListener(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                clearAll();
               if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                       EStoreOwner estoreowner = snapshot.getValue(EStoreOwner.class);
                       // Toast.makeText(getApplicationContext(),"inside for estore"+estoreowner.getName(), Toast.LENGTH_LONG).show();

                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("order");
                        Query query2 = databaseReference2.orderByChild("orderid");
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {

                                    Order order = snapshot2.getValue(Order.class);
                                    if(order.getCustomerid().equals(customerId )){
                                      //  String customerid = order.getCustomerid();
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
                                                    databaseReference4 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products").child(snapshot3.getKey()).child("description");
                                                    Query query4 = databaseReference4.orderByChild("descId").equalTo(descid);

                                                    query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot datasnapshot4) {
                                                            for (DataSnapshot snapshot4 : datasnapshot4.getChildren()) {
                                                                ProductDesc desc = snapshot4.getValue(ProductDesc.class);
                                                                            orderindex = new ShowOrder();
                                                                            orderindex.setOrderdate(order.getOrderdate());

                                                                            if(order.getIsdileverd()==0) {
                                                                                orderindex.setStatus(" Not Delivered");
                                                                            }else
                                                                                orderindex.setStatus(" Delivered");
                                                                            orderindex.setCustomerName(estoreowner.getName());
                                                                            orderindex.setOrederprice(prod.getproductPrice());
                                                                            orderindex.setOrderImage(desc.getproductimagePath());
                                                                            orderindex.setOrdersize(desc.getproductSize());
                                                                            //Toast.makeText(getApplicationContext(),"inside for customer"+orderindex.getOrdersize(), Toast.LENGTH_LONG).show();
                                                                            orders.add(orderindex);


                                                                //-----------------------------------------------------------------------------------------------------------------------------------------

                                                            }//end for desc
                                                            showorderAdpater = new CustomerCartAdapter(getApplicationContext(), orders);
                                                            recyclerView.setAdapter(showorderAdpater);
                                                            showorderAdpater.notifyDataSetChanged();

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
                                    }//end if not customer

                                }//end for product snapshot



                            }//end on change product

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }//end on cancelled product
                        });//end product listener
//____________________________________________________________________________________________________________


                    }//end for e store



               }//end if//---------------------------------------

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


}
