package com.osiog.myoldmancare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.osiog.myoldmancare.Database.ModelDB.Cart;
import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

/**
 * Created by OSIOG on 2018/7/3.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    Context context;
    List<Cart> cartList;

    public OrderDetailAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.e_order_detail_layout, parent, false);
        return new OrderDetailAdapter.OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailAdapter.OrderDetailViewHolder holder, final int position) {
        Glide.with(context)
                .load(cartList.get(position).link)
                .into(holder.img_product);

        holder.txt_price.setText(new StringBuilder("$").append(cartList.get(position).price));
        holder.txt_product_name.setText(cartList.get(position).name);
//        holder.txt_product_name.setText(new StringBuilder(cartList.get(position).name)
//        .append(" x")
//        .append(cartList.get(position).amount)
//        .append(cartList.get(position).size == 0 ? " Size M":"Size L"));

        holder.txt_sugar_ice.setText(cartList.get(position).name);
        holder.txt_sugar_ice.setText(new StringBuilder
                ("甜度:").append(cartList.get(position).sugar).append("%").append(" / ")//Sugar
                .append("冰度:").append(cartList.get(position).ice).append("%")//Ice:
                .toString());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView img_product;
        TextView txt_product_name, txt_sugar_ice, txt_price;


        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public OrderDetailViewHolder(View itemView) {
            super(itemView);

            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            txt_sugar_ice = (TextView) itemView.findViewById(R.id.txt_sugar_ice);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);

            view_background = (RelativeLayout) itemView.findViewById(R.id.view_background);
            view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position) {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}

