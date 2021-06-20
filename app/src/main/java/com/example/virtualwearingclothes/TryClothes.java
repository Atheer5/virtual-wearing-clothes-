package com.example.virtualwearingclothes;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//__________________________________________________________

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

//_______________________________________________________________________________




public class TryClothes extends AppCompatActivity implements LocationListener {
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

    LocationManager locationManager;
    static double lat=0.0;
    static double lon=0.0;

    private DatabaseReference databaseReferencenearest;

    protected void onCreate(Bundle savedInstanceState) {
        estorownerId = getIntent().getStringExtra("estoreId");
        productId = getIntent().getStringExtra("productId");
        descId = getIntent().getStringExtra("DescId");
        productimagepath= getIntent().getStringExtra("imagepath");
        customerId = getIntent().getStringExtra("customerId");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_clothes);

        //__________________________________________________________________________________

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(TryClothes.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TryClothes.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

                getLocation();
        Toast.makeText(getApplication(),"Location: "+lat+"  "+lon,Toast.LENGTH_SHORT).show();



        //________________________________________________________________________________________

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
//__________________________________________________________________________find nearest tailor shop ______________________________________

        //findnerst
        databaseReferencenearest= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Factories");
        ArrayList<LocationData> allfactory = new ArrayList<LocationData>();
        findViewById(R.id.findnerst).setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             Toast.makeText(TryClothes.this,"inside button",Toast.LENGTH_SHORT).show();

                                                             //get all factory from DB

                                                             Query query=databaseReferencenearest.orderByChild("factoryId");
                                                             query.addValueEventListener(new ValueEventListener() {//addvalueevent...
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot datasnapshot) {


                                                                     if(datasnapshot.exists()) {
                                                                         for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                                                                             Factories factory =snapshot.getValue(Factories.class);
                                                                             allfactory.add(new LocationData(Math.sqrt((lat - factory.getX()) * (lat - factory.getX()) + (lon - factory.getY()) * (lon - factory.getY())),factory.getName(),factory.getPhoneNumber(),factory.getAddress()));



                                                                         }//end for e store////................................................
                                                                         LocationData nearestfact=allfactory.get(0);
                                                                         for(int i=0;i<allfactory.size();i++){
                                                                             if(nearestfact.getDistance() >allfactory.get(i).getDistance())
                                                                                 nearestfact=allfactory.get(i);
                                                                             Toast.makeText(TryClothes.this,"nearest fact "+nearestfact.getName(),Toast.LENGTH_SHORT).show();

                                                                         }
                                                                         //_________________to nearest factory page ____________________________________________________________________________________________

                                                                         Intent intent = new Intent(TryClothes.this,NearestFactory.class);
                                                                         intent.putExtra("fname",nearestfact.getName());
                                                                         intent.putExtra("faddress",nearestfact.getAddress());
                                                                         intent.putExtra("fphonenum",nearestfact.getPhnum());
                                                                         startActivity(intent);



                                                                         //____________________________________________________________________________________________________________________________________________________


                                                                     }//end if

                                                                 }//end onDataChange ///.....................................................................

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                                 }

                                                             });//end addListenerForSingleValueEvent







                                                         }//end onclick
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
//____________________________________________________________________________________________________________________________________
    @Override
    public void onLocationChanged(@NonNull Location location) {

        lat=(Double)location.getLatitude();
        lon=(Double)location.getLongitude();
        Toast.makeText(this, ""+lat+","+lon, Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(TryClothes.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);


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
//_____________________________________________________________________________________________________________________


    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,TryClothes.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
//_____________________________________________________________________________________________________________________________________





}//end class