package com.example.virtualwearingclothes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowProductdescAdapter extends RecyclerView.Adapter<ShowProductdescAdapter.ViewHolder>{

private static final String Tag="RecyclerView";




private Context mContext;
private ArrayList<ProductDesc> productdescList;


public ShowProductdescAdapter(Context mContext, ArrayList<ProductDesc> productdescList) {
        this.mContext = mContext;
        this.productdescList = productdescList;
        }



@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estore_owner_show_product_desc ,parent,false);
        return new ViewHolder(view );
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.prodSize.setText("size: "+productdescList.get(position).getproductSize());
        holder.prodquan.setText("quantity: " +productdescList.get(position).getproductQuantity());
       holder.descid.setText(productdescList.get(position).getdescId());

        //Glide.with(mContext).load(productList.get(position).getproductdesc().get(0).getproductimagePath()).into(holder.prodImage);
        Picasso.get().load(productdescList.get(position).getproductimagePath()).into(holder.prodImage);

        holder.mCardView.setTag(position);
        }

@Override
public int getItemCount() {
        return productdescList.size();
        }





public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView prodImage;
    TextView prodSize;
    TextView prodquan;
    TextView descid;
    public CardView mCardView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        prodImage=itemView.findViewById(R.id.productImagedesc);
        prodSize=itemView.findViewById(R.id.productsize);
        prodquan=itemView.findViewById(R.id.productquan);
       descid=itemView.findViewById(R.id.descid);
        mCardView = (CardView) itemView.findViewById(R.id.cardViewdesc2);
    }
}// end ViewHolder

}//end class
