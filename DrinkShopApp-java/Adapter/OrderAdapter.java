package com.osiog.myoldmancare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.osiog.myoldmancare.Model.Order;
import com.osiog.myoldmancare.OrderDetailActivity;
import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Retrofit.IItemClickListener;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

/**
 * Created by OSIOG on 2018/7/2.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.e_order_layout,parent,false);
        Log.e("OrderAdapter","onCreateViewHolder_CHECKED !!");
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        holder.txt_order_id.setText(new StringBuilder("#").append(orderList.get(position).getOrderID()));
        holder.txt_order_price.setText(new StringBuilder("$").append(orderList.get(position).getOrderPrice()));
        holder.txt_order_address.setText(orderList.get(position).getOrderAddress());
        holder.txt_order_comment.setText(orderList.get(position).getOrderComment());
        holder.txt_order_status.setText(new StringBuilder("Order Status :").append(Common.convertCodeToStatus(orderList.get(position).getOrderStatus())));

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentOrder = orderList.get(position);
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            }
        });

        Log.e("OrderAdapter","onBindViewHolder_CHECKED !!");
    }

    @Override
    public int getItemCount() {
        Log.e("OrderAdapter","getItemCount_CHECKED !!");
        return orderList.size();
    }
}
