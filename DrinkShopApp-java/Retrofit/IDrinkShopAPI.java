package com.osiog.myoldmancare.Retrofit;


import com.osiog.myoldmancare.Model.Banner;
import com.osiog.myoldmancare.Model.Category;
import com.osiog.myoldmancare.Model.CheckUserResponse;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.Model.Order;
import com.osiog.myoldmancare.Model.User;

import java.util.List;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by OSIOG on 2018/6/23.
 */

public interface IDrinkShopAPI {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<Drink>> getDrink(@Field("menuID") String menuID);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone);

    @GET("getbanner.php")
    Observable<List<Banner>> getBanner();

    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);

    @GET("getalldrinks.php")
    Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<String> submitOrder(@Field("price") int orderprice,
                           @Field("orderDetail") String orderDetail ,
                           @Field("comment") String comment,
                           @Field("address") String address,
                           @Field("phone")  String phone);

    @FormUrlEncoded
    @POST("getorder.php")
    Observable<List<Order>> getOrder(@Field("userPhone") String userPhone,
                                     @Field("status") String status);



}

//    這邊要注意的是原本的 Call 變為 Observable ，因為是用 RxJava 觀察者模式訂閱，
//    所以必須要返回一個 Observable 對象，才能夠進行接續的 subscribeOn() 、 subScribe() 等 RxJava 操作。
//    其餘的都跟只有 Retrofit 時一樣。