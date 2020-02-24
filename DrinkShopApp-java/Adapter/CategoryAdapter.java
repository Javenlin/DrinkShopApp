package com.osiog.myoldmancare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.osiog.myoldmancare.DrinkActivity;
import com.osiog.myoldmancare.Model.Category;
import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Retrofit.IItemClickListener;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

/**
 * Created by OSIOG on 2018/6/26.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.z_menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {

        //LOAD_IMAGE(MENU)
        Glide.with(context).load(categories.get(position).Link).into(holder.img_product);
        holder.txt_menu_name.setText(categories.get(position).Name);

        //EVENT LOAD_LIST(PRODUCT)
        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {

                Common.currentCategory = categories.get(position);

                //進入相關商品頁
                context.startActivity(new Intent(context, DrinkActivity.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
