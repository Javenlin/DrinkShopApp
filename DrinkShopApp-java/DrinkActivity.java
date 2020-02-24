package com.osiog.myoldmancare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.osiog.myoldmancare.Adapter.DrinkAdapter;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    IDrinkShopAPI mService;
    RecyclerView lst_drink;
    TextView txt_banner_name;

    //RxJava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_drink);

        mService = Common.getAPI();

        lst_drink = (RecyclerView) findViewById(R.id.recycler_drinks);
        lst_drink.setLayoutManager(new GridLayoutManager(this,2));
        lst_drink.setHasFixedSize(true);

        txt_banner_name = (TextView) findViewById(R.id.txt_menu_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        LoadListDrink(Common.currentCategory.ID);
    }

    private void LoadListDrink(String menuID) {
        compositeDisposable.add(mService.getDrink(menuID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Drink>>() {
            @Override
            public void accept(List<Drink> drinks) throws Exception {
                displayDrinkList(drinks);
            }
        }));
    }

    private void displayDrinkList(List<Drink> drinks) {

        DrinkAdapter adapter = new DrinkAdapter(this,drinks);
        lst_drink.setAdapter(adapter);

    }

}



//    //==============
//    // 雙擊返回退出程式
//    //==============
//    boolean isBackButtonClicked = false;
//
//    @Override
//    public void onBackPressed() {
//
//        if (isBackButtonClicked) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.isBackButtonClicked = true;
//        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isBackButtonClicked = false;
//    }

//    onPostCreate是指onPostCreate方法是指onCreate方法彻底执行完毕的回调，onPostResume类似，
//    那我们什么时候在可以使用这个呢。大家肯定遇到这种情况，在onCreate中获取某个View的高度和宽度，
//    发现获取到的值是0，因为这个View可能还没初始化好，这时候比如我们在onPostResume中取获取这个View的高和宽，
//    因为onPostResume是指onResume彻底执行完毕的回调，所以这时候去获取就可以了。