package com.osiog.myoldmancare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;

import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.osiog.myoldmancare.Gson_Bean.Bean_FBAccount;
import com.osiog.myoldmancare.Model.CheckUserResponse;
import com.osiog.myoldmancare.Model.User;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

/**
 * Created by OSIOG on 2018/6/12.
 */

public class User_Login extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1001;
    //VAR_count
    private long mLastClickTime = 0;    //連點防呆延遲時間
    //http://localhost/android_connect/create_product.php 10.0.2.2 168.95.192.1:8080 192.168.43.112:8080
    private String INSERT_URL = "http://10.0.2.2/OldManPHP/php_FBAccount/FBAccount_UserInfo_Insert.php";
    private String SHOW_URL = "http://10.0.2.2/OldManPHP/php_FBAccount/FBAccount_UserInfo_Show.php";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * @basic_info
     */
    private String id;
    private String link;
    private String time;
    /**
     * @advanced_info
     */
    private String name;
    private String email;
    private String gender;
    private String birthday;
    private String picture;


    //VAR_initial
    private Context context;
    private CallbackManager callbackManager;
    private RequestQueue requestQueue;
    private IDrinkShopAPI mService;


    //INICOMPO
    private LoginButton btn_FBlogin;
    private ImageView btn_Phonelogin;
    private ImageView btn_FBlogin_skin;
    private ImageView Img_myFace;


    private void INICOMPONENT() {

        Img_myFace = (ImageView) findViewById(R.id.Img_myFace);
        btn_FBlogin = (LoginButton) findViewById(R.id.btn_FBlogin);
        btn_FBlogin.setOnClickListener(LOGIN);
        btn_FBlogin_skin = (ImageView) findViewById(R.id.btn_FBlogin_skin);
        btn_FBlogin_skin.setOnClickListener(LOGIN);
        btn_Phonelogin = (ImageView) findViewById(R.id.btn_Phonelogin);
        btn_Phonelogin.setOnClickListener(LOGIN);


    }


    //-- -- -- -- -- -- -- --
    //(LOGIN)_CLICK_SECTION
    //-- -- -- -- -- -- -- --
    private View.OnClickListener LOGIN = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_FBlogin:
                    //FB原生按鈕
                    break;
                case R.id.btn_FBlogin_skin:
                    //FB美化按鈕
                    btn_FBlogin.performClick();
                    break;
                case R.id.btn_Phonelogin:
                    //Phone登入按鈕
                    startLoginPage(LoginType.PHONE);
                    break;
            }
        }
    };

    //-- -- -- -- -- -- -- --
    // (onClick)_CLICK_SECTION
    //-- -- -- -- -- -- -- --
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
//                case R.id.btn_continue:
//
//                    break;
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_user_login);

        //=====================
        // REQUEST_PERMISSION
        //=====================
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSION);

        //=====================
        // INITIAL VolleyRequestQueue
        //=====================
        requestQueue = Volley.newRequestQueue(User_Login.this);

        //=====================
        // INITIAL IDrinkShopAPI Service
        //=====================
        mService = Common.getAPI();

        //=====================
        // INITIAL_Facebook
        //=====================
        FacebookSdk.sdkInitialize(getApplicationContext());

        //-- -- -- -- -- --
        //INITIAL_SECTION
        //-- -- -- -- -- --
        INICOMPONENT();
        GET_HASHCODE_API();
        FB_SIGNIN_API();

        //-- -- -- --
        //AUTO_LOGIN
        //-- -- -- --
        if (AccountKit.getCurrentAccessToken() != null) {

            AccountKit_CallbackAPI();

        }

    }


    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder
                        (loginType, AccountKitActivity.ResponseType.TOKEN);

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }


    //==================================================
    // 登入／登出完成後執行callbackManager管理的回呼程式
    //==================================================
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e("PHONE請求回傳", "onActivityResult=>" +
                " request_code: " + requestCode +
                " resultCode: " + resultCode);


        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (result.getError() != null) {
                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
            } else {
                if (result.getAccessToken() != null) {
                    Log.e("getAccessToken", result.getAccessToken().toString());

                    AccountKit_CallbackAPI();

                }
            }

        }
    }

    private void AccountKit_CallbackAPI() {

        final SpotsDialog waitingDialog = new SpotsDialog(User_Login.this);
        waitingDialog.show();
        waitingDialog.setMessage("請稍後...");

        //------------------------------
        //獲取User手機號碼 檢查是否已經存在
        //------------------------------
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                Log.e("AccountKit", account.getPhoneNumber().toString());

                mService.checkUserExists(account.getPhoneNumber().toString())
                        .enqueue(new Callback<CheckUserResponse>() {
                            @Override
                            public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                CheckUserResponse userResponse = response.body();

                                if (userResponse.isExists()) {
                                    //獲取用戶資料
                                    mService.getUserInformation(account.getPhoneNumber().toString())
                                            .enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {

                                                    waitingDialog.dismiss();

//                                                              //獲取當前用戶資料
                                                    Common.currentUser = response.body();
                                                    Log.d("checkUserExists", "使用者已存在 " + response.body().getName() + " / " + response.body().getPhone());
                                                    Log.d("Common.currentUser", "使用者已存在 " + Common.currentUser.getName() + " / " + Common.currentUser.getPhone());

                                                    //開啟新購物頁面
                                                    startActivity(new Intent(User_Login.this, HomeActivity.class));
                                                    //關閉登入頁面
                                                    finish();

//                                                                Log.e("getUserInformation", "使用者已存在");
//                                                                waitingDialog.dismiss();
//                                                                //開啟新購物頁面
//                                                                startActivity(new Intent(User_Login.this, HomeActivity.class));
//                                                                //關閉登入頁面
//                                                                finish();
                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {
                                                    Log.e("getUserInformation", "onFailure: " + t.getMessage());
                                                    Toast.makeText(User_Login.this, t.getMessage(), Toast.LENGTH_SHORT);

                                                }
                                            });
                                } else {
                                    Log.e("checkUserExists", "使用者尚未註冊");
                                    waitingDialog.dismiss();
                                    showRegisterDialog(account.getPhoneNumber().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                waitingDialog.dismiss();
                                Log.e("getPhoneNumber", "onFailure");
                            }
                        });
            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Log.e("ERROR", accountKitError.getErrorType().getMessage());
                Log.e("AccountKit", "onError");
            }
        });
    }

    //===============
    // 用戶註冊對話框
    //===============
    private void showRegisterDialog(final String phone) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(User_Login.this);
        alertDialog.setTitle("註冊");
        //
        final LayoutInflater inflater = this.getLayoutInflater();
        final View register_layout = inflater.inflate(R.layout.a_register_layout, null);
        //
        final MaterialEditText edt_name = (MaterialEditText) register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address = (MaterialEditText) register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_birthdate = (MaterialEditText) register_layout.findViewById(R.id.edt_birthdate);
        //
        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        //
        TextView btn_register = (TextView) register_layout.findViewById(R.id.btn_register);

        //---------------
        //設定並顯示對話框
        //---------------
        alertDialog.setView(register_layout);
        final AlertDialog dialog = alertDialog.create();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---------
                //關閉對話框
                //---------
                dialog.dismiss();

//                alertDialog.create().dismiss();

                final SpotsDialog waitingDialog = new SpotsDialog(User_Login.this);
                waitingDialog.show();
                waitingDialog.setMessage("請稍後...");

                if (TextUtils.isEmpty(edt_name.getFloatingLabelText().toString())) {
                    Toast.makeText(User_Login.this, "請輸入你的名字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edt_birthdate.getFloatingLabelText().toString())) {
                    Toast.makeText(User_Login.this, "請輸入你的生日", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edt_address.getFloatingLabelText().toString())) {
                    Toast.makeText(User_Login.this, "請輸入你的地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                mService.registerNewUser(phone,
                        edt_name.getText().toString(),
                        edt_birthdate.getText().toString(),
                        edt_address.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                waitingDialog.dismiss();
                                User user = response.body();
                                if (TextUtils.isEmpty(user.getError_msg())) {
                                    Toast.makeText(User_Login.this, "註冊成功!!!", Toast.LENGTH_SHORT).show();
                                    //獲取當前用戶資料
                                    Common.currentUser = response.body();
                                    //開啟新購物頁面
                                    startActivity(new Intent(User_Login.this, HomeActivity.class));
                                    //關閉登入頁面
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitingDialog.dismiss();
                            }
                        });

            }
        });

//        //---------------
//        //簡易顯示對話框
//        //---------------
//        alertDialog.setView(register_layout);
//        alertDialog.show();

        dialog.show();
    }


    //==================
    // 臉書登入程序API
    //==================
    //第16課 Facebook登入登出
    private void FB_SIGNIN_API() {

        context = this;

        //-------------------------
        // 登出後, 偏好內容即清除
        //-------------------------
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                SharedPreferences.Editor editor = getSharedPreferences().edit();

                editor.putString("id", null);
                editor.putString("link", null);
                editor.putString("name", null);
                editor.putString("email", null);
                editor.putString("time", null);
                editor.putString("gender", null);
                editor.putString("birthday", null);
                editor.putString("picture", null);

                editor.apply();

                Img_myFace.setImageDrawable(null); //頭像清空

            }
        };

        //------------------------------------------------------
        // 建立一個callbackManager, 用來管理登入/登出後的回呼程式
        //------------------------------------------------------
        callbackManager = CallbackManager.Factory.create();

        //----------------
        // 登入按鈕
        //----------------
        btn_FBlogin.setReadPermissions(Arrays
                .asList("email")); //  讀取權限 - 用戶動態

        //------------------------------------------
        // 註冊回呼程式內容, 並向Facebook發出登入請求
        //------------------------------------------
        btn_FBlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    //---------------
                                    //預覽 JSONObject
                                    //---------------
//                                    Log.e("PRETTYLOGGER", "JSONObject : " + object.toString());

                                    //-- -- -- -- --
                                    // 獲取頭像並顯示
                                    //-- -- -- -- --
                                    picture = "";
                                    JSONObject DATA = response.getJSONObject();
                                    if (DATA.has("picture")) {
                                        picture = DATA.getJSONObject("picture").getJSONObject("data").getString("url");
                                        Glide.with(User_Login.this).load(picture).into(Img_myFace);
                                    }

                                    //---------------------------------------------------
                                    // 將Facebook回傳的id及name, 另系統時間加入偏好內容中
                                    //---------------------------------------------------

                                    //TODO 登入基本歸檔資料
                                    /**@basic_info*/
                                    id = object.optString("id");
                                    link = object.optString("link");
                                    time = new Date(System.currentTimeMillis()).toString();
                                    /**@advanced_info*/
                                    name = object.optString("name");
                                    email = object.optString("email");
                                    gender = object.optString("gender");
                                    birthday = object.optString("birthday");
//                                  String picture = DATA.getJSONObject("picture").getJSONObject("data").getString("url");


                                    //=====================
                                    // 將資料儲存進資料庫
                                    //=====================//
                                    FBINFO_INSERT_DATA(); //id, link, time, name, email, gender, birthday, picture


                                    SharedPreferences.Editor editor = getSharedPreferences().edit();

                                    editor.putString("id", id);
                                    editor.putString("link", link);
                                    editor.putString("time", time);
                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.putString("gender", gender);
                                    editor.putString("birthday", birthday);
                                    editor.putString("picture", picture);

                                    editor.apply();

                                } catch (Exception e) {
                                    Toast.makeText(context, "Exception! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );

                //---------------------------------
                // 要求Facebook回覆的使用者資料項目
                //---------------------------------
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, link, name, email, gender, birthday, picture.width(700).height(700)");
                request.setParameters(parameters);


                //----------------------
                // 向Facebook發出請求
                //----------------------
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "登入取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(context, "登入錯誤", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //===================
    // 登入資料儲存資料庫
    //===================

    private void FBINFO_INSERT_DATA() {

        //TODO ERASE LOG
        // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        Log.e("FBINFO_INSERT_DATA", "ACTIVATED !!!!");
        Logger.e("FBINFO_INSERT_DATA:" + "\n" + "+id = %s / link = %s / time = %s " + "\n" +
                        "name = %s / email = %s" + "\n" +
                        "gender = %s / birthday = %s / picture = %s",
                id, link, time, name, email, gender, birthday, picture);
        // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {

            @Override
            public void run() {

                //----------------
                // 登入資訊儲存DATA
                //----------------

                final StringRequest INSERT = new StringRequest(com.android.volley.Request.Method.POST, INSERT_URL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String mresponse = response.substring(10);
                        // response
                        Log.d("INSERT_URL_success", mresponse);

                        //----------------
                        // 判斷註冊狀況
                        //----------------

                        if (mresponse.equals("number_of_rows = 0")) {
                            Log.d("您是第一次登入 歡迎光臨 !!!", "FIRST TIME : " + mresponse);
                            Toast.makeText(getApplicationContext(), "您是第一次登入 歡迎光臨 !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("您以註冊過 歡迎光臨 !!!", "OLD MEMBER : " + mresponse);
                            Toast.makeText(getApplicationContext(), "您以註冊過 歡迎光臨 !!!", Toast.LENGTH_SHORT).show();
                        }


                        //----------------
                        // 進入主畫面
                        //----------------
                        Intent intent = new Intent(User_Login.this, MainActivity.class);
                        startActivity(intent);


                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("INSERT_URL_Error", error.toString());
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", id);
                        map.put("link", link);
                        map.put("time", time);
                        map.put("name", name);
                        map.put("email", email);
                        map.put("gender", gender);
                        map.put("birthday", birthday);
                        map.put("picture", picture);

                        return map;
                    }
                };
                requestQueue.add(INSERT);
                Log.e("INSERT GO!!", "INSERT GO:" + INSERT.toString());

                //重設連線延遲
                INSERT.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                //----------------
                // 登入資訊展示DATA
                //----------------

                final StringRequest SHOW = new StringRequest(com.android.volley.Request.Method.POST, SHOW_URL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String mresponse = response.substring(10);
                        // response
                        Log.d("SHOW_URL_success", mresponse);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        Bean_FBAccount fbaccount = gson.fromJson(mresponse, Bean_FBAccount.class);

                        ArrayList<Bean_FBAccount.FBAccount> FBArray = fbaccount.getFbaccount();
                        for (int i = 0; i < FBArray.size(); i++) {

                            String show_id = FBArray.get(i).getId();
                            String show_link = FBArray.get(i).getLink();
                            String show_time = FBArray.get(i).getTime();
                            String show_name = FBArray.get(i).getName();
                            String show_email = FBArray.get(i).getEmail();
                            String show_gender = FBArray.get(i).getGender();
                            String show_birthday = FBArray.get(i).getBirthday();
                            String show_picture = FBArray.get(i).getPicture();
                            //
                            Log.i("show_id", show_id);
                            Log.i("show_link", show_link);
                            Log.i("show_time", show_time);
                            Log.i("show_name", show_name);
                            Log.i("show_email", show_email);
                            Log.i("show_gender", show_gender);
                            Log.i("show_birthday", show_birthday);
                            Log.i("show_picture", show_picture);
                            //
                            Log.i("", "-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ");
                            Log.i("", "\n");
                        }
                        Log.e("id & name", "id: " + FBArray.get(FBArray.size() - 1).getId() + " / " + "name: " + FBArray.get(FBArray.size() - 1).getName());
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("SHOW_URL_Error", error.toString());
                    }
                });
                requestQueue.add(SHOW);
                Log.e("SHOW GO!!", "SHOW GO:" + SHOW.toString());

                //重設連線延遲
                SHOW.setRetryPolicy(new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
        });
    }

    //=====================
    // 取得偏好設定空間
    //=====================
    private SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    //=====================
    // TODO 取得密鑰雜湊碼
    //=====================
    private void GET_HASHCODE_API() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.osiog.myoldmancare", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyResult = new String(Base64.encode(md.digest(), 0));//String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("FB hash key", KeyResult);
//                Toast.makeText(this, "My FB Key is \n" + KeyResult, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

    //==============
    // 雙擊返回退出程式
    //==============
    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {

        if (isBackButtonClicked) {
            this.finish();

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

            super.onBackPressed();
            return;
        }

        this.isBackButtonClicked = true;
        Toast.makeText(this, "請再次點擊 迴車鍵離開 !", Toast.LENGTH_SHORT).show();
//        this.finish();

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //销毁目标Activity和它之上的所有Activity，重新创建目标Activity
        startActivity(homeIntent);

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    protected void onResume() {
        isBackButtonClicked = false;
        super.onResume();
    }
}


/*
e490318@yahoo.com.tw
aimage19javen0618aimage19
 */

//    final String id, final String link, final String time, final String name,
//    final String email, final String gender, final String birthday, final String picture

//        "email, user_age_range, user_birthday, user_friends, user_gender, user_hometown, user_link, user_location," +  //讀取權限 - 用戶屬性
//                "user_likes, user_photos, user_posts, user_tagged_places, user_videos"  //  讀取權限 - 用戶動態

//                                    Toast.makeText(User_Login.this, id + "," + name + "," + time, Toast.LENGTH_SHORT).show();
//                                    Logger.e("LoginCallback:" + "\n" + "+id = %s / link = %s / time = %s " + "\n" +
//                                                    "name = %s / email = %s" + "\n" +
//                                                    "gender = %s / birthday = %s / picture = %s",
//                                            id, link, time, name, email, gender, birthday, picture);

