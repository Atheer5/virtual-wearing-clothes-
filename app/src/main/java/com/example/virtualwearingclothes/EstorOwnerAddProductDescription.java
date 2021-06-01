package com.example.virtualwearingclothes;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EstorOwnerAddProductDescription  extends AppCompatActivity {

    private  static final int ResultLoadImage=1;
    private Uri FilePathUri;

    boolean flag=true;
    boolean uploadimage=false;
    boolean addproduct=false;

    static private String productId;

    ProgressDialog progressDialog ;


   ImageView productImage;
    EditText productSize;
    EditText productQuantity;

    private String estorownerId;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estor_owner_add_product_description);
        estorownerId=getIntent().getStringExtra("estoreId");
        Toast.makeText(getApplicationContext(),".....",Toast.LENGTH_LONG).show();

        progressDialog = new ProgressDialog(EstorOwnerAddProductDescription.this);


        storageReference = FirebaseStorage.getInstance().getReference("Images");
//_________________________________________________add from gallary__________________________________________________________________________
        //upload imag from gallary
        findViewById(R.id.eUploadBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), ResultLoadImage);
            }//end uploadbtn onclik//then to onActivityResult method
        });

//___________________________save button___________________________________________________________________________________________________

        findViewById(R.id.addextradescription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                productImage=findViewById(R.id.proimage);
                productSize=findViewById(R.id.addproductsize);
                productQuantity=findViewById(R.id.addproductquantity);


                String Productsize =productSize.getText().toString();
                String Productquantity =productQuantity.getText().toString();

//__________________________________validation _____________________________________________________________________________________


                flag = true;
                //not empty data

                if(!uploadimage){
                    flag=false;
                    Toast.makeText(getApplicationContext(),"you must upload image ",Toast.LENGTH_LONG).show();
                                  }



                String []array = new String[]{Productsize,Productquantity};
                EditText []editArray =new EditText[]{productSize,productQuantity};
                for (int i=0;i<array.length;i++){
                    if(array[i].isEmpty()) {
                        flag=false;
                        editArray[i].setError("You have to fill it!");
                        editArray[i].requestFocus();

                    }
                }

                if(!flag) return;

//_____________________________________________________old data_______________________________________________________________________________________

                databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");

                Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ProductName=getIntent().getStringExtra("proname");
                        String ProductPrice =getIntent().getStringExtra("proprice");
                        String ProductInfo =getIntent().getStringExtra("proinfo");
                        //String productId=null;
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                EStoreOwner eStoreOwner=snapshot.getValue(EStoreOwner.class);
                                if(!addproduct) {
                                    product prod;
                                    productId = databaseReference.push().getKey();
                                    prod=new product(productId,ProductName,ProductPrice,ProductInfo);
                                    databaseReference.child(snapshot.getKey()).child("products").push().setValue(prod);
                                }
                                    addproduct = true;


                                    //------------------------------------------------------------------------upload description----------------------------------------------------

                                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners").child(snapshot.getKey()).child("products");

                                    Query query = databaseReference2.orderByChild("productId").equalTo(productId);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            if (dataSnapshot2.exists()) {//at secomd time !!  id screenshot
                                                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {

                                                   String descriptionId = databaseReference2.push().getKey();


//___________________________________________________________________________________________________________________________________________

                                                    if (FilePathUri != null) {
                                                        progressDialog.setTitle("Image is Uploading...");
                                                        progressDialog.show();
                                                        StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                                                        storageReference2.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                progressDialog.dismiss();
                                                                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        String url = uri.toString();
                                                                        uploadinfo imageUploadInfo = new uploadinfo(null, url);

                                                                        String ImageUploadId = databaseReference2.push().getKey();
                                                                        ProductDesc pro;

                                                                        pro=new ProductDesc(Productsize,Productquantity,imageUploadInfo.getImageURL(),ImageUploadId);


                                                                        databaseReference2.child(snapshot2.getKey()).child("description").push().setValue(pro);
                                                                        Toast.makeText(getApplicationContext(),"Uploaded successfully , you  can add another description for this product ",Toast.LENGTH_LONG).show();

                                                                        productImage.setImageResource(R.drawable.ic_baseline_image_24);
                                                                        productSize.setText("");
                                                                        productQuantity.setText("");



                                                                    }
                                                                });

                                                            }
                                                        });
                                                    }
                                                    else {

                                                        Toast.makeText(EstorOwnerAddProductDescription.this, "Please Select Image", Toast.LENGTH_LONG).show();

                                                    }






                                                }//end for
                                            }//end if
                                        }//end on data change

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }

                                    });//end addlestiner for product to add desc



                            }//end for
                        }//end if
                    }//end on data change //get estore Owner id

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
            });//end addlestiner
            }//end onclick
        });//end sent on click listener
































        // then upload new data


    }//end on create
    //______________________________________________________this method will be call when image selected from image gallary and view it in xml ___________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        productImage=findViewById(R.id.proimage);
        if (requestCode == ResultLoadImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                productImage.setImageBitmap(bitmap);//show image n image view
                uploadimage=true;
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }//end onActivityResult
//____________________________________________________________________________________________________________________________________________

    //________________________________________________________________________this method will be call in if filePatheuri to get image extintion_______________________________________________________________________________________________

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
//__________________________________________________________________________________________________________________________________________________________________________

}//end class