package com.osiog.myoldmancare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.AccountKit;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.osiog.myoldmancare.Adapter.CategoryAdapter;
import com.osiog.myoldmancare.Database.DataSource.CartRepository;
import com.osiog.myoldmancare.Database.DataSource.FavoriteRepository;
import com.osiog.myoldmancare.Database.Local.CartDataSource;
import com.osiog.myoldmancare.Database.Local.EDMTRoomDatabase;
import com.osiog.myoldmancare.Database.Local.FavoriteDataSource;
import com.osiog.myoldmancare.Model.Banner;
import com.osiog.myoldmancare.Model.Category;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;
import com.osiog.myoldmancare.Utils.ProgressRequestBody;
import com.osiog.myoldmancare.Utils.UploadCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UploadCallBack {

    private static final int PICK_FILE_REQUEST = 1222;
    //INICOMPO
    TextView txt_name, txt_phone;
    SliderLayout sliderLayout;
    RecyclerView lst_menu;
    NotificationBadge badge;
    ImageView cart_icon;
    CircleImageView img_avatar;

    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;


    //VAR_initial
    CompositeDisposable compositeDisposable = new CompositeDisposable();   //RxJava
    IDrinkShopAPI mService;
    Uri selectedFileUri;


    private void INICOMPONENT() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        mService = Common.getAPI();
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        //
//        //電子郵件
//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        lst_menu = (RecyclerView) findViewById(R.id.lst_menu);
        lst_menu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lst_menu.setHasFixedSize(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity_home);
        INICOMPONENT();

        //SET_VIEW
        View headerView = navigationView.getHeaderView(0);
        txt_name = (TextView) headerView.findViewById(R.id.txt_name);
        txt_phone = (TextView) headerView.findViewById(R.id.txt_phone);
        img_avatar = (CircleImageView) headerView.findViewById(R.id.img_avatar);

        //EVENT
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        //SET_INFO
        txt_name.setText(Common.currentUser.getName());
        txt_phone.setText(Common.currentUser.getPhone());

        //SET_AVATAR
        if (!TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Glide.with(this)
                    .load(new StringBuilder(Common.BASE_URL)
                            .append("user_avatar/")
                            .append(Common.currentUser.getAvatarUrl()).toString())
                    .into(img_avatar);
        }

        //GET_BANNER
        getBannerImage();

        //GET_MENU
        getMenu();

        //SAVE_NEWEST_TOPPING_LIST
        getToppingList();

        //INIT_DATABASE
        initDB();
    }

    //============
    // 選擇圖片
    //============
    private void chooseImage() {
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "請選擇一張圖片"),
                PICK_FILE_REQUEST);
    }

    //============
    // 選擇結果
    //============
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    selectedFileUri = data.getData();
                    if (selectedFileUri != null && !selectedFileUri.getPath().isEmpty()) {
                        img_avatar.setImageURI(selectedFileUri);
                        uploadFile();
                    } else
                        Toast.makeText(this, "無法上傳文件至服務器!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //============
    // 上傳圖片
    //============
    private void uploadFile() {

        if (selectedFileUri != null) {
            File file = FileUtils.getFile(this, selectedFileUri);

            String fileName = new StringBuilder(Common.currentUser.getPhone())
                    .append(FileUtils.getExtension(file.toString()))
                    .toString();

            Log.e("uploadFile", "fileName: " + fileName);

            ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("upload_file", fileName, requestFile);

            final MultipartBody.Part userPhone = MultipartBody.Part.createFormData("phone", Common.currentUser.getPhone());


            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone, body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(HomeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }
    }

    //==================
    // 初始化購物車資料庫
    //==================
    private void initDB() {
        Common.edmtRoomDatabase = EDMTRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.edmtRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.edmtRoomDatabase.favoriteDAO()));
    }

    //=============
    // 取得配料列表
    //=============
    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.TOPPING_MENU_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        Common.toppingList = drinks;
                    }
                }));

    }

    //=============
    // 取得分類清單
    //=============
    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
                }));
    }

    private void displayMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        lst_menu.setAdapter(adapter);
    }

    //=============
    // 獲得輪播圖片
    //=============
    private void getBannerImage() {
        compositeDisposable.add(mService.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //=============
    // 撥放輪播圖片
    //=============
    private void displayImage(List<Banner> banners) {
        HashMap<String, String> bannerMap = new HashMap<>();
        for (Banner item : banners)
            bannerMap.put(item.getName(), item.getLink());

        for (String name : bannerMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);
        }
    }


//    boolean isBackButtonClicked = false;
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//
//            //==============
//            // 雙擊返回退出程式
//            //==============
//            if (isBackButtonClicked) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.isBackButtonClicked = true;
//            Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
//        }
//    }

    //==============
    //初始化購物車紅點
    //==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge) view.findViewById(R.id.badge);
        cart_icon = (ImageView) view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    //============
    //更新紅點計數
    //============
    private void updateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (Common.cartRepository.countCartItems() == 0) {
                    badge.setVisibility(View.INVISIBLE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu) { //action_settings
            return true;
        } else if (id == R.id.search_menu) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //===============
        // 使用者登出
        //===============
        if (id == R.id.nav_sign_out) {
            //建立確認按鈕
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("登出");
            builder.setMessage("您確認要登出本系統嗎");//Do you want to exit this application ?

            builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AccountKit.logOut();

                    //Clear all activity  关闭多个视图
                    Intent intent = new Intent(HomeActivity.this, User_Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            //-- -- -- --
            // 顯示對話框
            //-- -- -- --
            builder.show();

        } else if (id == R.id.nav_show_favorites) {
            startActivity(new Intent(HomeActivity.this, FavoriteListActivity.class));
        } else if (id == R.id.nav_show_orders) {
            if (Common.currentUser != null) {
                startActivity(new Intent(HomeActivity.this, ShowOrderActivity.class));

            } else {
                Toast.makeText(this, "請於登入後使用本功能!!", Toast.LENGTH_SHORT).show();
            }
        }


//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //================
    //  紅點顯示時計數
    //================
    //當Activity出現手機上後，呼叫onResume方法
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CartCount", String.valueOf(Common.cartRepository.countCartItems()));

        updateCartCount();  //購物紅點計數

//        isBackButtonClicked = false;    //初始雙擊返回退出
    }

    @Override
    public void onProgressUpdate(int partantage) {

    }

}



