package com.osiog.myoldmancare;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.osiog.myoldmancare.Adapter.CartAdapter;
import com.osiog.myoldmancare.Database.ModelDB.Cart;
import com.osiog.myoldmancare.Model.User;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;
import com.osiog.myoldmancare.Utils.RecyclerItemTouchHelper;
import com.osiog.myoldmancare.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_cart;
    TextView btn_place_order;

    RelativeLayout rootLayout;
    CartAdapter cartAdapter;
    List<Cart> cartList = new ArrayList<>();

    CompositeDisposable compositeDisposable;

    IDrinkShopAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_cart);

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        //INIT_SECTION
        recycler_cart = (RecyclerView) findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        btn_place_order = (TextView) findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        //讀取購物資料
        loadCartItems();

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);
    }

    private void placeOrder() {
        //建立新對話框
        //Submit Order
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("訂單送出");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.d_submit_order_layout, null);

        final EditText edt_comment = (EditText) submit_order_layout.findViewById(R.id.edt_comment);
        final EditText edt_other_address = (EditText) submit_order_layout.findViewById(R.id.edt_other_address);

        final RadioButton rdi_user_address = (RadioButton) submit_order_layout.findViewById(R.id.rdi_user_address);
        final RadioButton rdi_other_address = (RadioButton) submit_order_layout.findViewById(R.id.rdi_other_address);

        //EVENT
        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_other_address.setEnabled(false);    //採用預設地址
                }
            }
        });

        rdi_other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_other_address.setEnabled(true);     //採用其它地址
                }
            }
        });

        builder.setView(submit_order_layout);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String orderComment = edt_comment.getText().toString();
                final String orderAdress;
                if (rdi_user_address.isChecked())
                    orderAdress = Common.currentUser.getAddress();
                else if (rdi_other_address.isChecked())
                    orderAdress = edt_other_address.getText().toString();
                else
                    orderAdress = "";   //加入空值欄位，以免被判未設置初始值

                //Submit Order
                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<List<Cart>>() {
                                    @Override
                                    public void accept(List<Cart> carts) throws Exception {
                                        if (!TextUtils.isEmpty(orderAdress))
                                            sendOrderToServer(Common.cartRepository.sumPrice(),
                                                    carts,
                                                    orderComment, orderAdress);
                                        else
                                            Toast.makeText(CartActivity.this, "Order Address can't null", Toast.LENGTH_SHORT).show();
                                    }
                                })
                );
            }
        });

        //===========
        // 顯示對話框
        //===========
        builder.show();


    }

    private void sendOrderToServer(int sumPrice, List<Cart> carts, String orderComment, String orderAddress) {
        if (carts.size() > 0) {
            String orderDetail = new Gson().toJson(carts);

            mService.submitOrder(sumPrice, orderDetail, orderComment, orderAddress, Common.currentUser.getPhone())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order submited !!!", Toast.LENGTH_SHORT).show();

                            //CLEAR_CART
                            Common.cartRepository.emptyCart();

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
                        }
                    });

        }
    }

    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItem(carts);
                            }
                        })
        );

    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
//        isBackButtonClicked = false;

        //讀取購物資料
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //Delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            //Delete item from Room database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append("removed from Cart List"), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
