package com.osiog.myoldmancare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.osiog.myoldmancare.Database.ModelDB.Favorite;
import com.osiog.myoldmancare.R;

import java.util.List;

/**
 * Created by OSIOG on 2018/7/11.
 */

//TODO cycView Adapter設計詳解
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    //RecyclerView extends 格式 : RecyclerView.Adapter<Adapter名稱.Holder名稱>

    Context context;
    List<Favorite> favoriteList;

    //建構式
    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //設置景圖
        View itemView = LayoutInflater.from(context).inflate(R.layout.z_fav_item_layout,parent,false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        //設置資料
        Glide.with(context).load(favoriteList.get(position).link).into(holder.img_product);
        holder.txt_price.setText(new StringBuilder("$").append(favoriteList.get(position).price).toString());
        holder.txt_product_name.setText(favoriteList.get(position).name);

    }

    @Override
    public int getItemCount() {
        //啟動點
        return favoriteList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        //設置操作元件
        ImageView img_product;
        TextView txt_product_name, txt_price;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            img_product = (ImageView) itemView.findViewById(R.id.img_product);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }
}
