package com.example.virtualwearingclothes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class CustomerShowProductDescAdapter extends RecyclerView.Adapter<CustomerShowProductDescAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";




    private Context mContext;
    private ArrayList<ProductDesc> productdescList;


    public CustomerShowProductDescAdapter(Context mContext, ArrayList<ProductDesc> productdescList) {
        this.mContext = mContext;
        this.productdescList = productdescList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_show_product_description,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.prodSize.setText("size: "+productdescList.get(position).getproductSize());
        holder.descid.setText(productdescList.get(position).getdescId());
        holder.prodimagepath.setText(productdescList.get(position).getproductimagePath());
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
        TextView descid;
        TextView prodimagepath;
        public CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage=itemView.findViewById(R.id.productImagedesc);
            prodSize=itemView.findViewById(R.id.productsize);
            descid=itemView.findViewById(R.id.descid);
            prodimagepath=itemView.findViewById(R.id.productimage);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewdesc2);
        }
    }// end ViewHolder

}//end class

