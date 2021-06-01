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

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private static final String Tag="RecyclerView";




    private Context mContext;
    private ArrayList<ShowOrder> orderList;


    public CustomerCartAdapter(Context mContext, ArrayList<ShowOrder> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }
    @NonNull
    @Override
    public CustomerCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_cart,parent,false);
        return new CustomerCartAdapter.ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerCartAdapter.ViewHolder holder, int position) {

        holder.orderSize.setText(orderList.get(position).getOrdersize());
        holder.name.setText(orderList.get(position).getCustomerName());
        holder.orderPrice.setText(orderList.get(position).getOrederprice());
        holder.status.setText(orderList.get(position).getStatus());
       holder.orderdate.setText(orderList.get(position).getOrderdate());
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
        TextView status;
       TextView orderdate;
        TextView name;
        public CardView mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage=itemView.findViewById(R.id.cImageorder);
            orderSize=itemView.findViewById(R.id.cocsize);
            orderPrice=itemView.findViewById(R.id.cocprice);
            status=itemView.findViewById(R.id.codelivered);
            orderdate=itemView.findViewById(R.id.codate);
            name=itemView.findViewById(R.id.cstorename);
            mCardView = (CardView) itemView.findViewById(R.id.cardViewcustomerorder);
        }
    }// end ViewHolder
}
