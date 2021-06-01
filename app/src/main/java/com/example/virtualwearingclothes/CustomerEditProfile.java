package com.example.virtualwearingclothes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CustomerEditProfile extends AppCompatActivity {


    private String customerId;
    private String name;
    private String password;
    private String address;
    private String phoneNum;
    private boolean flag;
    //for firebase DB
    private DatabaseReference databaseReference;

    private DatabaseReference databaseReferenceaddress;
    private DatabaseReference databaseReferencename;
    private DatabaseReference databaseReferencepassword;
    private DatabaseReference databaseReferenceimage;



    //to set data in DB
    private EditText newName;
    private EditText newAddress;
    private EditText newPassword;
    private EditText newconfirmPassword;
    private EditText newphoneNum;
//_____________________________________________________________________________________________________________________________
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_edit_profile);
        customerId = getIntent().getStringExtra("customerId");

//________________________________________________________________________________________________________________________________________________________________
        //get data from firebase
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Customers");
        Query query=reference.orderByChild("customerId").equalTo(customerId);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        Customer customer=snapshot.getValue(Customer.class);

                        name=customer.getName();
                        password=customer.getPassword();
                        address=customer.getAddress();
                        phoneNum=customer.getPhoneNumber();



                    }//end for
                }//end if

                //set data from DB to xml file
                ((EditText)findViewById(R.id.editcname)).setText(name);
                ((EditText)findViewById(R.id.editpassword)).setText(password);
                ((EditText)findViewById(R.id.editcaddress)).setText(address);
                ((EditText)findViewById(R.id.editcconfirmpassword)).setText(password);
                ((EditText)findViewById(R.id.ceditphonenum)).setText(phoneNum);

            }//end on data change

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//________________________________________________________________________________________________________________________________________________________________
        databaseReference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Customers");
        databaseReferenceaddress=databaseReference;
        databaseReferencename=databaseReference;
        databaseReferencepassword=databaseReference;
        databaseReferenceimage=databaseReference;


        //________________________________________________________________________________________________________________________________________________________________
//edit button

        findViewById(R.id.ceditbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName =findViewById(R.id.editcname);
                newAddress=findViewById(R.id.editcaddress);
                newPassword=findViewById(R.id.editpassword);
                newconfirmPassword=findViewById(R.id.editcconfirmpassword);
                newphoneNum=findViewById(R.id.ceditphonenum);


                String newname =newName .getText().toString();
                String newpassword =newPassword.getText().toString();
                String newaddress =newAddress.getText().toString();
                String newconfirmpassword  =newconfirmPassword.getText().toString();
                String newphonenum  =newphoneNum.getText().toString();
                flag = true;
                //not empty data
                String []array = new String[]{newname,newaddress,newpassword,newconfirmpassword };
                EditText []editArray =new EditText[]{newName,newAddress,newPassword,newconfirmPassword};
                for (int i=0;i<array.length;i++){

                    if(array[i].isEmpty()) {
                        flag=false;
                        editArray[i].setError("You have to fill it!");
                        editArray[i].requestFocus();

                    }
                }
                if(!flag) return;

                //password length must be 6 digit at least

                if(newpassword.length()<6){
                    newPassword.setError("Password must be more than 6 characters");
                    newPassword.requestFocus();
                    return;
                }

                //password ==confirm password
                if(!newpassword .equals( newconfirmpassword)){
                    newPassword.setError("Password must be match with Comfirm Passowrd");
                    newPassword.requestFocus();
                    return;
                }

                //_____________________________________________________________________end get string data from xml_________________________________________________
//set string data in firebase by onDAtaChang and set image by onDataChang and onSucccess
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Customers");
                Query query=reference.orderByChild("phoneNumber").equalTo(newphonenum);
                query.addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                final Customer customer;
                                customer=snapshot.getValue(Customer.class);
                                customer.setName(newname);
                                customer.setPassword(newpassword);
                                customer.setAddress(newaddress);
                                customer.setPhoneNumber(newphonenum );

                                databaseReferenceaddress.child(snapshot.getKey()).child("address").setValue(customer.getAddress());
                                databaseReferencename.child(snapshot.getKey()).child("name").setValue(customer.getName());
                                databaseReferencepassword.child(snapshot.getKey()).child("password").setValue(customer.getPassword());
//________________________________________________________________________________________________________________________________________________________________

                                Toast.makeText(getApplicationContext(),"Done ",Toast.LENGTH_LONG).show();
                            }//end for
                        }//end if
                    }//end on Data chang
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });//end data base listenr
            }//end editbtn onclick
        });//end lestiner



















//________________________________________________________________________________________________________________________

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:

                        Intent intent = new Intent(CustomerEditProfile.this,Customermainpage.class);
                        intent.putExtra("customerId",customerId);
                        startActivity(intent);
                        overridePendingTransition(0,0);




                    case R.id.nav_profile:
                        return true;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(CustomerEditProfile.this,login.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;



                    case R.id.nav_orders:
                        Intent intent2 = new Intent(CustomerEditProfile.this,CustomerCart.class);
                        intent2.putExtra("customerId",customerId.toString());//new
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
/*

                    case R.id.nav_profile://real
                        Intent intent3 = new Intent(ActivityHome.this,ProfileActivity.class);
                        intent3.putExtra("customerId",customerId.toString());//new
                        startActivity(intent3);
                        overridePendingTransition(0,0);
                        return true;
*/

                }



                return false;
            }
        });

//________________________________________________________________________________________________________________________



    }//end on create
}
