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

import com.squareup.picasso.Picasso;

import java.io.IOException;


public class EstoreOwnerEditProfile extends AppCompatActivity {
    //to get from DB
    private  static final int ResultLoadImage=1;
    private String estorownerId;
    private String name;
    private String password;
    private String address;
    private String phoneNum;
    private String ImageUrl;
    private ImageView ProfilePic;
    //for firebase DB
    private Uri FilePathUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private DatabaseReference databaseReferenceaddress;
    private DatabaseReference databaseReferencename;
    private DatabaseReference databaseReferencepassword;
    private DatabaseReference databaseReferenceimage;

    ProgressDialog progressDialog ;
    boolean flag=true;

    //to set data in DB
    private EditText newName;
    private EditText newAddress;
    private EditText newPassword;
    private EditText newconfirmPassword;
    private EditText newphoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estore_owner_edit_profile);
        estorownerId=getIntent().getStringExtra("estoreId");
        progressDialog = new ProgressDialog(EstoreOwnerEditProfile.this);
        //________________________________________________________________________________________________________________________________________________________________
        //get data from firebase
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes")
                .child("Estoreowners");
        Query query=reference.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        EStoreOwner eStoreOwner=snapshot.getValue(EStoreOwner.class);

                        name=eStoreOwner.getName();
                        password=eStoreOwner.getPassword();
                        address=eStoreOwner.getAddress();
                        phoneNum=eStoreOwner.getPhoneNumber();
                        ImageUrl=eStoreOwner.getProfilePic();



                    }//end for
                }//end if

                //set data from DB to xml file
                ((EditText)findViewById(R.id.editename)).setText(name);
                ((EditText)findViewById(R.id.editcpassword)).setText(password);
                ((EditText)findViewById(R.id.editeaddress)).setText(address);
                ((EditText)findViewById(R.id.editconfirmpassword)).setText(password);
                ((EditText)findViewById(R.id.editphonenum)).setText(phoneNum);
                ProfilePic=(ImageView)findViewById(R.id.editimage);
                Picasso.get().load(ImageUrl).into(ProfilePic);

        }//end on data change

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//________________________________________________________________________________________________________________________________________________________________
         storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");

        databaseReferenceaddress=databaseReference;
        databaseReferencename=databaseReference;
        databaseReferencepassword=databaseReference;
        databaseReferenceimage=databaseReference;
        //_____________________________________________________________________________________________________________________________________________
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
//________________________________________________________________________________________________________________________________________________________________
//edit button

        findViewById(R.id.eeditbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName =findViewById(R.id.editename);
                newAddress=findViewById(R.id.editeaddress);
                newPassword=findViewById(R.id.editcpassword);
                newconfirmPassword=findViewById(R.id.editconfirmpassword);
                newphoneNum=findViewById(R.id.editphonenum);


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
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
                Query query=reference.orderByChild("phoneNumber").equalTo(newphonenum);
                query.addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                final EStoreOwner eStoreOwner;
                                eStoreOwner=snapshot.getValue(EStoreOwner.class);
                                eStoreOwner.setName(newname);
                                eStoreOwner.setPassword(newpassword);
                                eStoreOwner.setAddress(newaddress);
                                eStoreOwner.setPhoneNumber(newphonenum );
                                // eStoreOwner.setProfilePic(null);//cant do this use getDownloadUrl
//_______________________________________________use onSuccess to upload image to firebase storage and url to firebase storage and real time data base_______________________________________
                                if (FilePathUri != null) {
                                    progressDialog.setTitle("Image is Uploading...");
                                    progressDialog.show();

                                    final StorageReference storageReference2 = FirebaseStorage.getInstance().getReference( System.currentTimeMillis() + "."+GetFileExtension(FilePathUri));

                                    storageReference2.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //String TempImageName = txtdata.getText().toString().trim();//image name
                                            progressDialog.dismiss();
                                            Toast.makeText(EstoreOwnerEditProfile.this, "Upload successful!", Toast.LENGTH_LONG).show();

                                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String url = uri.toString();
                                                    uploadinfo imageUploadInfo = new uploadinfo(null, url);

                                                    eStoreOwner.setProfilePic(imageUploadInfo.getImageURL());
                                                   // databaseReference.push().setValue(eStoreOwner); //new child !!!!!
                                                    databaseReferenceaddress.child(snapshot.getKey()).child("address").setValue(eStoreOwner.getAddress());
                                                    databaseReferencename.child(snapshot.getKey()).child("name").setValue(eStoreOwner.getName());
                                                    databaseReferencepassword.child(snapshot.getKey()).child("password").setValue(eStoreOwner.getPassword());
                                                    databaseReferenceimage.child(snapshot.getKey()).child("profilePic").setValue(eStoreOwner.getProfilePic());
                                                  //  reference.child(snapshot.getKey()).removeValue();
                                                    Toast.makeText(getApplicationContext(),"Done ",Toast.LENGTH_LONG).show();



                                                }
                                            });}//end onSuccess
                                            });//addOnSuccessListener
                                    break;//if do edit with image break from for loop
                                }//end if filePathuri

//------------------------------if not do edit image will upload to data base from here -----------------------------------------------
                               // .push().setValue(eStoreOwner); //new child !!!!!
                               //
                                databaseReferenceaddress.child(snapshot.getKey()).child("address").setValue(eStoreOwner.getAddress());
                                databaseReferencename.child(snapshot.getKey()).child("name").setValue(eStoreOwner.getName());
                                databaseReferencepassword.child(snapshot.getKey()).child("password").setValue(eStoreOwner.getPassword());
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


}//end oncreate
//______________________________________________________this method will be call when image selected from image gallary and view it in xml ___________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProfilePic=(ImageView)findViewById(R.id.editimage);
        if (requestCode == ResultLoadImage && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                ProfilePic.setImageBitmap(bitmap);//show image n image view
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
}//end class














