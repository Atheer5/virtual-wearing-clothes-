package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;




import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;





public class CustomerRealShopping extends AppCompatActivity {
    private String customerId;

    private  static final int ResultLoadImage=1;
    private Uri FilePathUri;
    private ImageView Pic;
    private String estorownerId;
    private String productId;
    private String descId;
    private String productprice;
    private String productsize;

    private  String productimagepath;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceq;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReferencenewq;
    private static int clothesVShumen=0;

    private String humanencodedimage = "";
    private String clothesencodedimage = "";
    String url = "https://app-senior.herokuapp.com/";
    String human = "human image";
    String clothes = "clothes image ";
    String category = "Tshirt";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_real_shopping);

        customerId = getIntent().getStringExtra("customerId");
        //_________________________________________________________________________________________________________

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_real);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:

                        Intent intent3 = new Intent(CustomerRealShopping.this,Customermainpage.class);
                        intent3.putExtra("customerId",customerId.toString());//new
                        startActivity(intent3);
                        overridePendingTransition(0,0);

                        return true;


                    case R.id.nav_profile:
                        Intent intent = new Intent(CustomerRealShopping.this,CustomerEditProfile.class);
                        intent.putExtra("customerId",customerId);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(CustomerRealShopping.this,login.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.nav_orders:
                        Intent intent2 = new Intent(CustomerRealShopping.this,CustomerCart.class);
                        intent2.putExtra("customerId",customerId.toString());//new
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.nav_real://real
                        return true;


                }



                return false;
            }
        });
        //________________________________________________________________________________________________________

//_______________________upload image for humane body _________________________________________________________________________
        //upload imag from gallary
        findViewById(R.id.uploadhumanimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), ResultLoadImage);
                clothesVShumen=1;
            }//end uploadbtn onclik//then to onActivityResult method
        });
//_____________________________________________________________________________________________________________


        //_______________________upload image for clothes_________________________________________________________________________
        //upload imag from gallary
        findViewById(R.id.uploadclothesimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), ResultLoadImage);
                clothesVShumen=2;
            }//end uploadbtn onclik//then to onActivityResult method
        });
//_____________________________________________________________________________________________________________
//___________________________________________________________________________________________________________________________
        findViewById(R.id.wearme).setOnClickListener(new View.OnClickListener() {

            /*when click on try button
             *get humane image from image view and .... convaret it to base 64
             * get clothes image and ....convarte it to base 64
             * send request to herkuo *** from responce we will decode wearing humane to image and view it .
             */
            @Override
            public void onClick(View v) {

                ImageView humaneImage = (ImageView)findViewById(R.id.himg);
                BitmapDrawable humandrawable = (BitmapDrawable) humaneImage.getDrawable();
                Bitmap humanbitmap = humandrawable.getBitmap();
                ByteArrayOutputStream humanbos = new ByteArrayOutputStream();
                humanbitmap.compress(Bitmap.CompressFormat.PNG,100,humanbos);
                byte[] humanByte = humanbos.toByteArray();
                humanencodedimage = Base64.encodeToString(humanByte, Base64.DEFAULT);

                //-------------------------------------------------------------------------------------------------------------

                ImageView clothesImage = (ImageView)findViewById(R.id.clothesimg);
                BitmapDrawable clothesdrawable = (BitmapDrawable) clothesImage.getDrawable();
                Bitmap clothesbitmap = clothesdrawable.getBitmap();
                ByteArrayOutputStream clothesbos = new ByteArrayOutputStream();
                clothesbitmap .compress(Bitmap.CompressFormat.PNG,100,clothesbos);
                byte[] clothesByte = clothesbos.toByteArray();
                clothesencodedimage = Base64.encodeToString(clothesByte, Base64.DEFAULT);

                //-----------------------------------------------------------------------------------------------------------------
/*
                Toast.makeText(getApplication(),"encoding and decoding ",Toast.LENGTH_SHORT).show();
                byte[] decodedString = Base64.decode(humanencodedimage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ImageView loadingImage = (ImageView)findViewById(R.id.loading);
                loadingImage.setImageBitmap(decodedByte);
*/
//-------------------------------------------------------------------------------------------------------------------------------------------------------

                sendRequest();

            }
        });

//_______________________________________________________________________________________________________________________________


    }//end on create


    //______________________________________________________this method will be call when image selected from image gallary and view it in xml ___________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(clothesVShumen==1)//humane image
        Pic=(ImageView)findViewById(R.id.himg);
        if(clothesVShumen==2)//clothes
            Pic=(ImageView)findViewById(R.id.clothesimg);
        if (requestCode == ResultLoadImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                Pic.setImageBitmap(bitmap);//show image n image view


            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

//________________________________________________________________________this method will be call in if filePatheuri to get image extintion_______________________________________________________________________________________________

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
//__________________________________________________________________________________________________________________________________________________________________________
//______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________


    private void sendRequest(){
        Toast.makeText(CustomerRealShopping.this,"loading",Toast.LENGTH_SHORT).show();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplication(),"response"+response.substring(0,10),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplication(),"response"+response.substring(0,8),Toast.LENGTH_LONG).show();

                        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ImageView loadingImage = (ImageView)findViewById(R.id.loading);
                        loadingImage.setImageBitmap(decodedByte);
                        Toast.makeText(CustomerRealShopping.this,"uploaded",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerRealShopping.this,"errors: "+error+"",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                human = humanencodedimage;
                clothes = clothesencodedimage;
                category ="0";
                params.put("human", human);
                params.put("clothes", clothes);
                params.put("category", category);


                return params;
            }

        };


        RequestQueue requestQueue2 = Volley.newRequestQueue(CustomerRealShopping.this);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(jsonObjRequest);

    }

//______________________________________________________________________________________________________________________________________________________________________________________________________



}//end class
