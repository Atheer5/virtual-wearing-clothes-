package com.example.virtualwearingclothes;

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








public class TryClothes extends AppCompatActivity {
    private  static final int ResultLoadImage=1;
    private Uri FilePathUri;
    private ImageView bodyPic;
    private String estorownerId;
    private String productId;
    private String descId;
    private String productprice;
    private String productsize;
    private String customerId;

    private  String productimagepath;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceq;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReferencenewq;


    private String humanencodedimage = "";
    private String clothesencodedimage = "";
    String url = "https://app-senior.herokuapp.com/";
    String human = "human image";
    String clothes = "clothes image ";
    String category = "Tshirt";




    protected void onCreate(Bundle savedInstanceState) {
        estorownerId = getIntent().getStringExtra("estoreId");
        productId = getIntent().getStringExtra("productId");
        descId = getIntent().getStringExtra("DescId");
        productimagepath= getIntent().getStringExtra("imagepath");
        customerId = getIntent().getStringExtra("customerId");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_clothes);

        ImageView ProfilePic=(ImageView)findViewById(R.id.clothesimg);
        Picasso.get().load(productimagepath).into(ProfilePic);

        findViewById(R.id.buybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"estore"+estorownerId+" "+" productId "+ productId +"desc"+descId+"price"+productprice+"size"+productsize, Toast.LENGTH_LONG).show();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");

                Query query = databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                changeQuantity();
                                String orderId = databaseReference.push().getKey();
                                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                Order order = new Order(orderId, customerId, productId, descId, date);
                                databaseReference.child(snapshot.getKey()).child("order").push().setValue(order);



                            }//end for
                        }//end if

                    }//end on data change

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }//end onclick
        });//end buy
//_______________________upload image for humane body _________________________________________________________________________
        //upload imag from gallary
        findViewById(R.id.uploadimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), ResultLoadImage);
            }//end uploadbtn onclik//then to onActivityResult method
        });
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
//______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________


    private void sendRequest(){
        Toast.makeText(TryClothes.this,"loading",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TryClothes.this,"uploaded",Toast.LENGTH_LONG).show();
                   }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TryClothes.this,"errors: "+error+"",Toast.LENGTH_LONG).show();
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


        RequestQueue requestQueue2 = Volley.newRequestQueue(TryClothes.this);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(jsonObjRequest);

    }

//______________________________________________________________________________________________________________________________________________________________________________________________________






//______________________________________________________this method will be call when image selected from image gallary and view it in xml ___________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bodyPic=(ImageView)findViewById(R.id.himg);
        if (requestCode == ResultLoadImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                bodyPic.setImageBitmap(bitmap);//show image n image view


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

    //_____________________________________________________________________________________________________________________________

    public void changeQuantity(){

Toast.makeText(getApplicationContext(),"Done.", Toast.LENGTH_LONG).show();
        databaseReferenceq = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        Query query=databaseReferenceq.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.exists()) {
                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");
                        Query query2 = databaseReference2.orderByChild("productId").equalTo( productId);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot2) {

                                if(datasnapshot2.exists()) {
                                    for (DataSnapshot snapshot2 : datasnapshot2.getChildren()) {
                                        // product prod = snapshot2.getValue(product.class);
                                        /*---------------------------------------------------------------------------------------------------------------------------------*/
                                        databaseReference3 = databaseReference2.child(snapshot2.getKey()).child("description");
                                        databaseReferencenewq=databaseReference3;
                                        Query query3 = databaseReference3.orderByChild("descId").equalTo(descId);
                                        query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot datasnapshot3) {
                                                ProductDesc desc = null;
                                                for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                    desc = snapshot3.getValue(ProductDesc.class);
                                                    if(Integer.parseInt(desc.getproductQuantity())>0){
                                                        int quan=Integer.parseInt(desc.getproductQuantity())-1;
                                                        desc.setproductQuantity(quan+"");
                                                        databaseReferencenewq.child(snapshot3.getKey()).child("productQuantity").setValue(desc.getproductQuantity());

                                                    }//end if

                                                }//end for desc


                                                //prod.setproductdesc(productdesc);
                                                // products.add(prod);




                                            }//end onDataChange for desc

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });//end addListenerForSingleValueEvent





                                        /*---------------------------------------------------------------------------------------------------------------------------------*/


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

    }//end change quan
//button to add nearest
//try clothes





}//end class