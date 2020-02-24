package com.osiog.myoldmancare.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.osiog.myoldmancare.Database.ModelDB.Cart;
import com.osiog.myoldmancare.Database.ModelDB.Favorite;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Retrofit.IItemClickListener;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

/**
 * Created by OSIOG on 2018/6/26.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    Context context;
    List<Drink> drinkList;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.z_drink_item_layout, null);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {

        //SET_EVENT
        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price));
        holder.txt_drink_name.setText((drinkList.get(position).Name));
        holder.btn_add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToCartDialog(position);
            }
        });

        //SET_PIC
        Glide.with(context).load(drinkList.get(position).Link).into(holder.img_product);

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Favorite System
        if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID)) == 1)

            //初始_圖案設置
            holder.btn_add_to_favorite.setImageResource(R.drawable.add_to_favorite);
            else
            holder.btn_add_to_favorite.setImageResource(R.drawable.add_to_favorite_v2);//add_to_favorite_v2

            //點擊_圖案設置
            holder.btn_add_to_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID)) != 1) {
                        addOrRemoveFavorite(drinkList.get(position),true);
                        holder.btn_add_to_favorite.setImageResource(R.drawable.add_to_favorite);
                    }
                    else {
                        addOrRemoveFavorite(drinkList.get(position),false);
                        holder.btn_add_to_favorite.setImageResource(R.drawable.add_to_favorite_v2);
                    }
                }
            });





    }

    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id = drink.ID;
        favorite.link = drink.Link;
        favorite.name = drink.Name;
        favorite.price = drink.Price;
        favorite.menuID = drink.MenuID;

        if(isAdd)
            Common.favoriteRepository.insertFav(favorite);
        else
            Common.favoriteRepository.delete(favorite);
    }


    private void showAddToCartDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.c_add_to_cart_layout, null);

        //VIEW
        ImageView img_product_dialog = (ImageView) itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = (ElegantNumberButton) itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView) itemView.findViewById(R.id.txt_cart_product_name);
        EditText edt_comment = (EditText) itemView.findViewById(R.id.edt_comment);

        //SIZE
        RadioButton rdi_sizeM = (RadioButton) itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = (RadioButton) itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sizeOfCup = 0;   //SIZE_M
                }
            }
        });

        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sizeOfCup = 1;   //SIZE_L
                }
            }
        });

        //SUGAR
        RadioButton rdi_suger_100 = (RadioButton) itemView.findViewById(R.id.rdi_suger_100);
        RadioButton rdi_suger_70 = (RadioButton) itemView.findViewById(R.id.rdi_suger_70);
        RadioButton rdi_suger_50 = (RadioButton) itemView.findViewById(R.id.rdi_suger_50);
        RadioButton rdi_suger_30 = (RadioButton) itemView.findViewById(R.id.rdi_suger_30);
        RadioButton rdi_suger_free = (RadioButton) itemView.findViewById(R.id.rdi_suger_free);

        rdi_suger_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.sugar = 0;   //FREE
            }
        });

        rdi_suger_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.sugar = 30;
            }
        });

        rdi_suger_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.sugar = 50;
            }
        });

        rdi_suger_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.sugar = 70;
            }
        });

        rdi_suger_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.sugar = 100;
            }
        });


        //ICE
        RadioButton rdi_ice_100 = (RadioButton) itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = (RadioButton) itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = (RadioButton) itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = (RadioButton) itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = (RadioButton) itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.ice = 0;   //FREE
            }
        });

        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.ice = 30;
            }
        });

        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.ice = 50;
            }
        });

        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.ice = 70;
            }
        });

        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Common.ice = 100;
            }
        });

        //SET_ADAPTER
        RecyclerView recycler_topping = (RecyclerView) itemView.findViewById(R.id.recycler_topping);
        recycler_topping.setLayoutManager(new LinearLayoutManager(context));
        recycler_topping.setHasFixedSize(true);

        MultiChoiceAdapter adapter = new MultiChoiceAdapter(context, Common.toppingList);
        recycler_topping.setAdapter(adapter);

        for (int i = 0; i < Common.toppingList.size() - 1; i++) {
            Log.e("toppingList", Common.toppingList.get(i).Name);
        }
        //SET_DATA
        Glide.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        txt_product_dialog.setText(drinkList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("加入購物車", new DialogInterface.OnClickListener() {//ADD TO CART
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Common.sizeOfCup == -1) {
                    Toast.makeText(context, "請選擇杯子的大小", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Common.sugar == -1) {
                    Toast.makeText(context, "請選擇甜度", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Common.ice == -1) {
                    Toast.makeText(context, "請選擇冰塊量", Toast.LENGTH_SHORT).show();
                    return;
                }

                showConfirmDialog(position, txt_count.getNumber());//Common.sizeOfCup,Common.sugar,Common.ice
                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void showConfirmDialog(final int position, final String number) {//int sizeOfCup, int sugar, int ice
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.d_confirm_add_to_cart_layout, null);

        //VIEW
        ImageView img_product_dialog = (ImageView) itemView.findViewById(R.id.img_product);
        final TextView txt_product_dialog = (TextView) itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = (TextView) itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar = (TextView) itemView.findViewById(R.id.txt_sugar);
        TextView txt_ice = (TextView) itemView.findViewById(R.id.txt_ice);
        final TextView txt_topping_extra = (TextView) itemView.findViewById(R.id.txt_topping_extra);

        //SET_DATA
        Glide.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        //
        txt_product_dialog.setText(new StringBuilder(drinkList.get(position).Name).append(" x")

                .append(Common.sizeOfCup == 0 ? " 小杯" : " 大杯")//" Size M" : " Size L"
                .append(number).toString());
        //
        txt_ice.setText(new StringBuilder("冰度: ").append(Common.ice).append("%").toString());//Ice: //
        //
        txt_sugar.setText(new StringBuilder("甜度: ").append(Common.sugar).append("%").toString());//Sugar: //
        //
        int price = (Integer.parseInt(drinkList.get(position).Price) * Integer.parseInt(number)) + Common.toppingPrice;

        if (Common.sizeOfCup == 1)   // size L
            price += (10.0*Integer.parseInt(number));



        StringBuilder topping_final_comment = new StringBuilder("");

        for (String line : Common.toppingAdded)
            topping_final_comment.append(line).append("\n");

        txt_topping_extra.setText(topping_final_comment);

        final int finalPrice = Math.round(price);

        txt_product_price.setText(new StringBuilder("$").append(finalPrice));
        //
        //清單確認按鈕

        builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                try {
                    //Add to SQLite
                    //Create new cart item
                    Cart cartItem = new Cart();
                    cartItem.name = drinkList.get(position).Name;
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.price = finalPrice;
                    cartItem.size = Common.sizeOfCup;
                    cartItem.toppingExtras = txt_topping_extra.getText().toString();
                    cartItem.link = drinkList.get(position).Link;

                    //Add to DB
                    Common.cartRepository.insertToCart(cartItem);
//save item to cart success
                    Log.d("EDMT_DEBUG", new Gson().toJson(cartItem));
                    Toast.makeText(context, "您的訂購 已加入購物車 !", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setView(itemView);
        builder.show();


    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
