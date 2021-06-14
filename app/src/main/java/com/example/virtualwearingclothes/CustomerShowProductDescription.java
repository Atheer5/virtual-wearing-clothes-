package com.example.virtualwearingclothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerShowProductDescription extends AppCompatActivity {

    private String estorownerId;
    private String productId;
    private String productprice;
    private String customerId;
    private ArrayList<ProductDesc> productdesc;
    private CustomerShowProductDescAdapter showProductDescAdapter;
    // private Context context ;

    //private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    RecyclerView recyclerView;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_show_product_description);
        estorownerId = getIntent().getStringExtra("estoreId");
        productId= getIntent().getStringExtra("productId");
        productprice= getIntent().getStringExtra("productprice");
        customerId=getIntent().getStringExtra("customerId");
        //Toast.makeText(getApplicationContext(),"productId"+productId +" "+" estorownerId "+ estorownerId , Toast.LENGTH_LONG).show();


        recyclerView=findViewById(R.id.recyclerviewdesc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager (this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        databaseReference= FirebaseDatabase.getInstance().getReference().child("virtualwearingclothes").child("Estoreowners");
        // products=new ArrayList<>();
        productdesc=new ArrayList<>();
        //clear arrray list
        clearAll();

        GetDataFromFirebase();


    }//end on create
    /////////////////////////////////////////////////////////////////////////////
    private void GetDataFromFirebase() {

        Query query=databaseReference.orderByChild("estoreOwnerId").equalTo(estorownerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {//addvalueevent...
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                clearAll();
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
                                        Query query3 = databaseReference3.orderByChild("descId");
                                        query3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot datasnapshot3) {

                                                productdesc = new ArrayList<>();
                                                ProductDesc desc = null;
                                                for (DataSnapshot snapshot3 : datasnapshot3.getChildren()) {
                                                    desc = snapshot3.getValue(ProductDesc.class);
                                                    if(Integer.parseInt(desc.getproductQuantity())!=0){
                                                    productdesc.add(desc);
                                                    showProductDescAdapter = new CustomerShowProductDescAdapter(getApplicationContext(), productdesc);
                                                    recyclerView.setAdapter(showProductDescAdapter);
                                                    showProductDescAdapter.notifyDataSetChanged();}//end if

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
    }//end GetDataFromFirebase
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void clearAll(){
        if( productdesc != null){
            productdesc.clear();
            if(showProductDescAdapter != null){
                showProductDescAdapter.notifyDataSetChanged();
            }
        }
        productdesc=new ArrayList<>();


    }//end clear

//_____________________________________________________________________________________________________________________

    public void tryproduct(View v) {
        Intent intent = new Intent(CustomerShowProductDescription.this,TryClothes.class);
        intent.putExtra("estoreId", estorownerId );
        intent.putExtra("productId",productId);
        CardView mycard=(CardView)v;
        RelativeLayout r=(RelativeLayout)mycard.getChildAt(0);//get relative
        TableRow mytable=(TableRow)r.getChildAt(0);//get table
        LinearLayout l =(LinearLayout)mytable.getChildAt(1);// quan deleted
        TextView t=(TextView)l.getChildAt(2);//get textview
        String s= t.getText().toString();
        intent.putExtra("DescId",s);
        TextView t1=(TextView)l.getChildAt(3);//get textview
        String imagepath= t1.getText().toString();
        intent.putExtra("imagepath",imagepath);

        intent.putExtra("productprice", productprice);

        TextView Size=(TextView)l.getChildAt(0);//get textview
        String size= Size.getText().toString();
        intent.putExtra("productsize",size);
        intent.putExtra("customerId",customerId);





        startActivity(intent);


    }

    //____________________________________________________________________________________________________________________________

}
