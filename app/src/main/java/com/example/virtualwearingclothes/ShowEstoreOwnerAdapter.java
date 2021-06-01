package com.example.virtualwearingclothes;

import androidx.recyclerview.widget.RecyclerView;
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

public class ShowEstoreOwnerAdapter extends RecyclerView.Adapter<ShowEstoreOwnerAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";




    private Context mContext;
    private ArrayList<EStoreOwner> estoreList;

    public ShowEstoreOwnerAdapter(Context mContext, ArrayList<EStoreOwner> estoreList) {
        this.mContext = mContext;
        this.estoreList = estoreList;
    }



    @NonNull
    @Override
    public ShowEstoreOwnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_main_page ,parent,false);
        return new ShowEstoreOwnerAdapter.ViewHolder(view );
    }


    @Override
    public void onBindViewHolder(@NonNull ShowEstoreOwnerAdapter.ViewHolder holder, int position) {

        holder.estoreText.setText(estoreList.get(position).getName());
        holder.estoreid.setText(estoreList.get(position).getEStoreOwnerId());


        //Glide.with(mContext).load(productList.get(position).getproductdesc().get(0).getproductimagePath()).into(holder.prodImage);
        Picasso.get().load(estoreList.get(position).getProfilePic()).into(holder.estoreImage);

        holder.mCardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return estoreList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView estoreImage;
        TextView estoreText;
        TextView estoreid;
        public CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            estoreImage=itemView.findViewById(R.id.estoreImage);
            estoreText=itemView.findViewById(R.id.estoreText);
            estoreid=itemView.findViewById(R.id.estoreid);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewestore);
        }
    }// end ViewHolder
}
