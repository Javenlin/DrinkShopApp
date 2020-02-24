package com.osiog.myoldmancare.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Retrofit.IItemClickListener;

/**
 * Created by OSIOG on 2018/6/26.
 */

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img_product;
    TextView txt_drink_name, txt_price;
    ImageView btn_add_to_card, btn_add_to_favorite;

    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DrinkViewHolder(View itemView) {
        super(itemView);

        img_product = (ImageView) itemView.findViewById(R.id.image_product);
        txt_drink_name = (TextView) itemView.findViewById(R.id.txt_drink_name);
        txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        btn_add_to_card = (ImageView) itemView.findViewById(R.id.btn_add_to_cart);
        btn_add_to_favorite = (ImageView) itemView.findViewById(R.id.btn_add_to_favorite);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
