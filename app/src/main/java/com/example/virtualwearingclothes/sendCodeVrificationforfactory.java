package com.example.virtualwearingclothes;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

//______________________________________________________________
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.util.List;
import java.util.Locale;


//___________________________________________________________

public class sendCodeVrificationforfactory extends AppCompatActivity implements LocationListener {
    DatabaseReference databaseReference;
    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText editText;

    LocationManager locationManager;
    static int lat=0;static int lon=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code_vrification);


        //___________________________________________________________//for location
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //   textView_location = findViewById(R.id.text_location);
        //  button_location = findViewById(R.id.button_location);
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(sendCodeVrificationforfactory.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(sendCodeVrificationforfactory.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }


        getLocation();

        Toast.makeText(sendCodeVrificationforfactory.this,"location="+lat+lon,Toast.LENGTH_LONG).show();




//________________________________________________

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");

        editText = findViewById(R.id.editTextCode);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerficationCode(phonenumber);


        findViewById(R.id.vregisterbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString();
                //.trim();
                if(code.isEmpty()){
                    editText.setError(" Enter code ...");
                    editText.requestFocus();

                    return;
                }
                if(code.length()<6){
                    editText.setError("The code must be 6 digit");
                    editText.requestFocus();

                    return;
                }

                verifiyCode(code);

            }
        });



    }//end oncreate

    private void sendVerficationCode(String number){


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }//end sendVerficationCode


    public  PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.
            OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s ;


            Toast.makeText(getApplicationContext(),"on code sent",Toast.LENGTH_LONG).show();


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(getApplicationContext(),"completed",Toast.LENGTH_LONG).show();
            if(code !=null){
                editText.setText(code);
                verifiyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(sendCodeVrificationforfactory.this,"onVerificationFailed",Toast.LENGTH_LONG).show();
            return;
        }
    };//end PhoneAuthProvider

    private void verifiyCode(String codeByUser){
        //here we will pass the code that entered or recive otomaticlly
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,codeByUser);
        signInWithPhoneAuthCredential(credential);

    }//end verficode





    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(sendCodeVrificationforfactory.this,new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    try {
                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("virtualwearingclothes").child("Factories");

                        final String phonenumber = getIntent().getStringExtra("phonenumber");
                        String fullname = getIntent().getStringExtra("fullname");
                        String address = getIntent().getStringExtra("address");
                        String password = getIntent().getStringExtra("password");


                        Toast.makeText(sendCodeVrificationforfactory.this,"location"+lat+lon,Toast.LENGTH_LONG).show();

                        final String factoryId = databaseReference.push().getKey();

                        Factories factory = new Factories(factoryId, fullname, phonenumber, password,address,lat,lon);
                        databaseReference.push().setValue(factory);


                        Intent intent = new Intent(sendCodeVrificationforfactory.this,login.class);
                        intent.putExtra("factoryId",factoryId);
                        startActivity(intent);

                    }catch (Exception exp){

                        Toast.makeText(sendCodeVrificationforfactory.this,exp.getMessage()
                                ,Toast.LENGTH_LONG).show();

                    }


                }


                else {
                    Toast.makeText(sendCodeVrificationforfactory.this,"incorrect code "
                            ,Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    //______________________________________________________________________________________________________
    //get factory location .
    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,sendCodeVrificationforfactory.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //__________________________________________________________________________________________________
    @Override
    public void onLocationChanged(@NonNull Location location) {

        lat=(int)location.getLatitude();
        lon=(int)location.getLongitude();
        while(lat==0)Toast.makeText(sendCodeVrificationforfactory.this,"wait",Toast.LENGTH_LONG).show();

        try {
            Geocoder geocoder = new Geocoder(sendCodeVrificationforfactory.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            //  textView_location.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

//_________________________________________________________________________________________________
}//end class




