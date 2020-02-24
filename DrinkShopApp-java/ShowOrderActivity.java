package com.osiog.myoldmancare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.osiog.myoldmancare.Adapter.OrderAdapter;
import com.osiog.myoldmancare.Model.Order;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowOrderActivity extends AppCompatActivity {

    IDrinkShopAPI mService;
    RecyclerView recycler_orders;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_activity_show_order);

        mService = Common.getAPI();

        recycler_orders = (RecyclerView) findViewById(R.id.recycler_orders);
        recycler_orders.setLayoutManager(new LinearLayoutManager(this));
        recycler_orders.setHasFixedSize(true);

        Log.e("getPhone", Common.currentUser.getPhone());

        loadOrder();
    }

    private void loadOrder() {
        if (Common.currentUser != null) {
            compositeDisposable.add(mService.getOrder(Common.currentUser.getPhone(), "0")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Order>>() {
                        @Override
                        public void accept(List<Order> orders) throws Exception {
                            displayOrder(orders);
                        }
                    }));
        } else {
            Toast.makeText(this, "請再次登入 !", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void displayOrder(List<Order> orders) {
        OrderAdapter adapter = new OrderAdapter(this, orders);
        recycler_orders.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrder();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
