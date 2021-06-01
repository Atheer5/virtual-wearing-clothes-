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

public class ShowProductAdapter extends RecyclerView.Adapter<ShowProductAdapter.ViewHolder> {

    private static final String Tag="RecyclerView";




    private Context mContext;
    private ArrayList<product> productList;


    public ShowProductAdapter(Context mContext, ArrayList<product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estore_owner_show_product ,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.prodText.setText("name: "+productList.get(position).getproductName());
        holder.prodprice.setText("Price: " +productList.get(position).getproductPrice()+"$");
        holder.productid.setText(productList.get(position).getproductId());

        //Glide.with(mContext).load(productList.get(position).getproductdesc().get(0).getproductimagePath()).into(holder.prodImage);
        Picasso.get().load(productList.get(position).getproductdesc().get(0).getproductimagePath()).into(holder.prodImage);

        holder.mCardView.setTag(position);
                    }

    @Override
    public int getItemCount() {
        return productList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView prodImage;
        TextView prodText;
        TextView prodprice;
        TextView productid;
        public CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prodImage=itemView.findViewById(R.id.productImage);
            prodText=itemView.findViewById(R.id.productText);
            prodprice=itemView.findViewById(R.id.productprice);
            productid=itemView.findViewById(R.id.productid);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewdesc);
        }
    }// end ViewHolder
}//end ShowProductAdapter
