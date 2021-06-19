package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//needed lib for firebase
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FactorySignUp extends AppCompatActivity {
    private EditText FullName;
    private EditText PhoneNum;
    private EditText Address;
    private EditText Password;
    private EditText ConfirmPassword;
    private Spinner spinner;

    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_signup);
        //fill country data (from conuntrydata class in java file )
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R
                .layout.simple_spinner_dropdown_item,
                CountryData.countryNames));


        //get data from view
        FullName = findViewById(R.id.fname);
        PhoneNum = findViewById(R.id.fphonenum);
        Address = findViewById(R.id.faddress);
        Password = findViewById(R.id.fpassword);
        ConfirmPassword = findViewById(R.id.fconfirmpassword);

        //verfiy data
        findViewById(R.id.fregisterbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                flag = true;
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String fullName = FullName.getText().toString();
                String number = PhoneNum.getText().toString().trim();
                String address = Address.getText().toString().trim();
                String password = Password.getText().toString();
                String confirmpassword = ConfirmPassword.getText().toString();


                //not empty data
                String[] array = new String[]{fullName, number, address, password, confirmpassword};
                EditText[] editArray = new EditText[]{FullName, PhoneNum, Address, Password, ConfirmPassword};
                for (int i = 0; i < array.length; i++) {

                    if (array[i].isEmpty()) {
                        flag = false;
                        editArray[i].setError("You have to fill it!");
                        editArray[i].requestFocus();

                    }
                }
                if (!flag) return;

                //password length must be 6 digit at least

                if (password.length() < 6) {
                    Password.setError("Password must be more than 6 characters");
                    Password.requestFocus();
                    return;
                }
                //password ==confirm password
                if (!password.equals(confirmpassword)) {
                    Password.setError("Password must be match with Comfirm Passowrd");
                    Password.requestFocus();
                    return;
                }

                //phone number validation 9 digit without 0 at start
                if (number.length() == 10 && number.startsWith("0"))
                    number = number.substring(1);

                else if (number.length() != 9) {
                    PhoneNum.setError("number must be 9 digit");
                    PhoneNum.requestFocus();
                    return;
                }

                String PhoneNumber = "+" + code + number;
                isValidPhoneNumberClassCustomer(PhoneNumber);


            }//end onclick for registerbtn


        });//end verfiy method
//___________________________________________________________________________________
        //________________________________________________________________________


    }//end oncreate


    public void isValidPhoneNumberClassCustomer(String PhoneNumber) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Customers");
        Query query = reference.orderByChild("phoneNumber").equalTo(PhoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "It's a used phone number use another one or you can Login", Toast.LENGTH_LONG).show();

                } else
                    isValidPhoneNumberClassEstore();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//isValidPhoneNumberClassCustomer

    public void isValidPhoneNumberClassEstore() {
        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
        PhoneNum = findViewById(R.id.fphonenum);
        String number = PhoneNum.getText().toString().trim();
        if (number.startsWith("0"))
            number = number.substring(1);
        String PhoneNumber = "+" + code + number;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Estoreowners");
        Query query = reference.orderByChild("phoneNumber").equalTo(PhoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "It's a used phone number use another one or you can Login", Toast.LENGTH_LONG).show();


                } else
                    isValidPhoneNumberClassFactory();
                //sendData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//isvalidPhonenumberclassEstor

    public void isValidPhoneNumberClassFactory() {
        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
        PhoneNum = findViewById(R.id.fphonenum);
        String number = PhoneNum.getText().toString().trim();
        if (number.startsWith("0"))
            number = number.substring(1);
        String PhoneNumber = "+" + code + number;


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Factories");
        Query query = reference.orderByChild("phoneNumber").equalTo(PhoneNumber);// or in general i can put the edittext insted of "sham"
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "It's a used phone number use another one or you can Login", Toast.LENGTH_LONG).show();

                } else
                    sendData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }//end isValidPhoneNumberClassFactory


    void sendData() {

        PhoneNum = findViewById(R.id.fphonenum);
        FullName = findViewById(R.id.fname);
        Password = findViewById(R.id.fpassword);
        Address = findViewById(R.id.faddress);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                CountryData.countryNames));


        String number = PhoneNum.getText().toString().trim();
        String fullName = FullName.getText().toString();
        String password = Password.getText().toString();
        String address = Address.getText().toString();


        String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
        if (number.startsWith("0"))
            number = number.substring(1);
        String PhoneNumber = "+" + code + number;


        //____________________________________________________________________________________________________


        Intent intent = new Intent(FactorySignUp.this, sendCodeVrificationforfactory.class);
        intent.putExtra("phonenumber", PhoneNumber);
        intent.putExtra("fullname", fullName);
        intent.putExtra("password", password);
        intent.putExtra("address", address);
        startActivity(intent);


    }//end set data
//_________________________________________________________________________________________________________

}
