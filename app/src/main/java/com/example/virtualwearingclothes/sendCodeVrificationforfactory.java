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



public class sendCodeVrificationforfactory extends AppCompatActivity {
    DatabaseReference databaseReference;
    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText editText;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code_vrification);

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

                        final String factoryId = databaseReference.push().getKey();

                        Factories factory = new Factories(factoryId, fullname, phonenumber, password,address);
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





}//end class
