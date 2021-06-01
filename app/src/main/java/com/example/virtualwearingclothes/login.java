package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText PhoneNumber;
    private EditText Password;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //to fill country code
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R
                .layout.simple_spinner_dropdown_item,
                CountryData.countryNames));



        //welcome logo:
        LayoutInflater myinflater = getLayoutInflater();
        View myView = myinflater.inflate(R.layout.activity_welcome_logo, null);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.FILL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(myView);
        toast.show();
        //end welcome logo



        PhoneNumber = (EditText)findViewById(R.id.lphonenum);
        Password = (EditText)findViewById(R.id.lpassword);


        findViewById(R.id.registerasEStoreowner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(login.this,EStoreOwnerSignUp.class);

                startActivity(intent3);

            }
        });

        findViewById(R.id.registerasfactory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(login.this,FactorySignUp.class);
                startActivity(intent4);

            }
        });

        findViewById(R.id.registerascustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(login.this,CustomerSignUp.class);
                startActivity(intent2);

            }
        });


        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum=PhoneNumber.getText().toString();
                String password=Password.getText().toString();
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                if(phonenum.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Please Enter your Phone number",Toast.LENGTH_LONG).show();
                    PhoneNumber.setError("Please Enter your Phone number");
                    PhoneNumber.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Please Enter your Password",Toast.LENGTH_LONG).show();
                    Password.setError("Please Enter your Password");
                    Password.requestFocus();
                    return;

                }
                if(phonenum.isEmpty() && password.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Please Enter your  Phone number and Password",Toast.LENGTH_LONG).show();
                    PhoneNumber.setError("Please Enter your Phone number");
                    PhoneNumber.requestFocus();
                    Password.setError("Please Enter your Password");
                    Password.requestFocus();
                    return;

                }



                if(phonenum.length() ==10 &&phonenum.startsWith("0") )
                    phonenum= phonenum.substring(1);


                if (phonenum.length() != 9) {
                    PhoneNumber.setError("number must be 9 digit");
                    PhoneNumber.requestFocus();
                    return;
                }

                else {
                    String phoneNumberr = "+" + code+ phonenum;
                    isexist1( phoneNumberr);
                }
            }
        });
    }//end oncreate

    private void isexist1(final String phoneNumberr) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Customers");
        Query query=reference.orderByChild("phoneNumber").equalTo(phoneNumberr);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Customer customer=snapshot.getValue(Customer.class);
                        if(Password.getText().toString().equals(customer.getPassword())){
                            Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
                            /////////////
//Go back
                            //////////////////////////
                            Intent intent = new Intent(login.this,Customermainpage.class);
                            String customerId=customer.getCustomerId();
                            intent.putExtra("customerId",customerId.toString());//new
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "your password is not correct !", Toast.LENGTH_LONG).show();
                            Password.setError("Password not correct");
                            Password.requestFocus();
                        }
                    }
                    // progressBar.setVisibility(View.GONE);
                }

                else
                    isexist2(phoneNumberr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }//isexist1

    private void isexist2(String phoneNumberr){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Estoreowners");
        Query query=reference.orderByChild("phoneNumber").equalTo(phoneNumberr);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        EStoreOwner eStoreOwner=snapshot.getValue(EStoreOwner.class);
                        //Toast.makeText(getApplicationContext(),customer.getPassword(),Toast.LENGTH_LONG).show();

                        if(Password.getText().toString().equals(eStoreOwner.getPassword())){
                            Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(login.this,EStoreOwnerDashboard.class);
                            intent.putExtra("estoreId",eStoreOwner.getEStoreOwnerId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "your password is not correct !", Toast.LENGTH_LONG).show();
                            Password.setError("Password not correct");
                            Password.requestFocus();
                            return;
                        }

                    }
                }

                else
                    isexist3(phoneNumberr);
                    //Toast.makeText(getApplicationContext(), "you don't have an a account ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//endexist

    private void isexist3(String phoneNumberr){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Factories");
        Query query=reference.orderByChild("phoneNumber").equalTo(phoneNumberr);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        Factories factory =snapshot.getValue(Factories.class);

                        if(Password.getText().toString().equals(factory.getPassword())){
                            Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(login.this,FactorySignUp.class);
                            intent.putExtra("factoryId",factory.getFactoryId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "your password is not correct !", Toast.LENGTH_LONG).show();
                            Password.setError("Password not correct");
                            Password.requestFocus();
                            return;
                        }

                    }
                }

                else
                   Toast.makeText(getApplicationContext(), "you don't have an a account ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//endexist



}//class
