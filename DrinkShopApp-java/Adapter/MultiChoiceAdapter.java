package com.osiog.myoldmancare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.R;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

/**
 * Created by OSIOG on 2018/6/27.
 */

//TODO 訂購單多選區_列表適配器

public class MultiChoiceAdapter extends  RecyclerView.Adapter<MultiChoiceAdapter.MultiChoiceViewHolder>{

    Context context;
    List<Drink> optionList;

    public MultiChoiceAdapter(Context context, List<Drink> optionList) {
        this.context = context;
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public MultiChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.c_multi_check_layout,null);
        return new MultiChoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiChoiceViewHolder holder, final int position) {
        holder.checkBox.setText(optionList.get(position).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//狀態:勾選 add
                    Common.toppingAdded.add(buttonView.getText().toString());
                    Common.toppingPrice+=Integer.parseInt(optionList.get(position).Price);
                }
                else {//狀態:未勾選 remove
                    Common.toppingAdded.remove(buttonView.getText().toString());
                    Common.toppingPrice-=Integer.parseInt(optionList.get(position).Price);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class MultiChoiceViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MultiChoiceViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.ckb_topping);
        }
    }
}
