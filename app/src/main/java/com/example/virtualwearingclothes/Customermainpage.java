package com.example.virtualwearingclothes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Customermainpage extends AppCompatActivity {

    private String customerId;

    private  String estoreId;

    private ArrayList<EStoreOwner> estores;

    private ShowEstoreOwnerAdapter showestoreAdapter;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_main_page);
        customerId = getIntent().getStringExtra("customerId");
//________________________________________________________________________________________________________________________

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:

                        return true;


                    case R.id.nav_profile:
                        Intent intent = new Intent(Customermainpage.this,CustomerEditProfile.class);
                        intent.putExtra("customerId",customerId);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(Customermainpage.this,login.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.nav_orders:
                        Intent intent2 = new Intent(Customermainpage.this,CustomerCart.class);
                        intent2.putExtra("customerId",customerId.toString());//new
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.nav_real://real
                        Intent intent3 = new Intent(Customermainpage.this,CustomerRealShopping.class);
                        intent3.putExtra("customerId",customerId.toString());//new
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        return true;


                }



                return false;
            }
        });

//________________________________________________________________________________________________________________________


        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        //firebase
        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        estores=new ArrayList<>();
        //clear arrray list
        clearAll();

        GetDataFromFirebase();




    }//end on create

    //______________________________________________________________________________________________________________________

    private void clearAll(){
        if(estores != null){
            estores.clear();
            if(showestoreAdapter != null){
                showestoreAdapter.notifyDataSetChanged();
            }
        }
        estores=new ArrayList<>();


    }//end clear

    //______________________________________________________________________________________________________________________
    private void GetDataFromFirebase() {

        Query query=databaseReference.orderByChild("estoreOwnerId");
        query.addValueEventListener(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                clearAll();
                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        EStoreOwner estore=snapshot.getValue(EStoreOwner.class);
                        estores.add(estore);

                    }//end for e store////................................................

                    showestoreAdapter = new ShowEstoreOwnerAdapter(getApplicationContext(), estores);
                    recyclerView.setAdapter(showestoreAdapter);
                    showestoreAdapter.notifyDataSetChanged();
                }//end if

            }//end onDataChange ///.....................................................................

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });//end addListenerForSingleValueEvent
    }//end GetDataFromFirebase




    public void gotoestore(View v) {


        Intent intent = new Intent(Customermainpage.this,CustomerShowProduct.class);
        CardView mycard=(CardView)v;
        RelativeLayout r=(RelativeLayout)mycard.getChildAt(0);//get relative
        LinearLayout l =(LinearLayout)r.getChildAt(0);//get layout
        LinearLayout l2 =(LinearLayout)l.getChildAt(1);//get layout
        TextView t=(TextView)l2.getChildAt(1);//get textview id
        String s= t.getText().toString();
        intent.putExtra("estoreId",s);
        intent.putExtra("customerId",customerId);
        startActivity(intent);


    }


}//end class
