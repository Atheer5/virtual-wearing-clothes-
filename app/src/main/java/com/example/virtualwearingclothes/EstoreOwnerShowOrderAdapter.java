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

public class EstoreOwnerShowOrderAdapter extends RecyclerView.Adapter<EstoreOwnerShowOrderAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";




    private Context mContext;
    private ArrayList<ShowOrder> orderList;


    public EstoreOwnerShowOrderAdapter(Context mContext, ArrayList<ShowOrder> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }



    @NonNull
    @Override
    public EstoreOwnerShowOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estore_owner_show_order,parent,false);
        return new EstoreOwnerShowOrderAdapter.ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull EstoreOwnerShowOrderAdapter.ViewHolder holder, int position) {

        holder.orderSize.setText(orderList.get(position).getOrdersize());
        holder.orderPrice.setText(orderList.get(position).getOrederprice());
        holder.customerName.setText(orderList.get(position).getCustomerName());
        holder.CustomerPhonenum.setText(orderList.get(position).getCustomerNumber());
        holder.orderid.setText(orderList.get(position).getOrderid());
        Picasso.get().load(orderList.get(position).getOrderImage()).into(holder.orderImage);

        holder.mCardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView orderImage;
        TextView orderSize;
        TextView orderPrice;
        TextView customerName;
        TextView CustomerPhonenum;
        TextView orderid;
        public CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage=itemView.findViewById(R.id.Imageorder);
            orderSize=itemView.findViewById(R.id.ocsize);
            orderPrice=itemView.findViewById(R.id.ocprice);
            customerName=itemView.findViewById(R.id.ocname);
            CustomerPhonenum=itemView.findViewById(R.id.ocphonenum);
            orderid=itemView.findViewById(R.id.orderid);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewdesc2);
        }
    }// end ViewHolder
}
