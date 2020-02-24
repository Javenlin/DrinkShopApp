package com.osiog.myoldmancare.Utils;

import com.osiog.myoldmancare.Database.DataSource.CartRepository;
import com.osiog.myoldmancare.Database.DataSource.FavoriteRepository;
import com.osiog.myoldmancare.Database.Local.CartDataSource;
import com.osiog.myoldmancare.Database.Local.EDMTRoomDatabase;
import com.osiog.myoldmancare.Model.Category;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.Model.Order;
import com.osiog.myoldmancare.Model.User;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OSIOG on 2018/6/23.
 */

public class Common {

    //BASE_URL
//    public static final String BASE_URL = "http://10.0.2.2/DrinkShopPHP/";
    //39.8.162.41
    //192.168.43.112:8080

    //CURRENT_OBJ
    public static User currentUser = null;
    public static Category currentCategory = null;
    public static Order currentOrder = null;

    //TOPPING
    public static List<Drink> toppingList = new ArrayList<>();
    public static final String TOPPING_MENU_ID = "7";
    public static int toppingPrice = 0;
    public static List<String> toppingAdded = new ArrayList<>();

    //HOLD_FIELD
    public static int sizeOfCup = -1;   //-1 : no chose (error) , 0 : M , 1 : L
    public static int sugar = -1;
    public static int ice = -1;

    //DATABASE
    public static EDMTRoomDatabase edmtRoomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    //BASE_URL
    public static final String BASE_URL = "http://192.168.1.102:80/DrinkShopPHP_v2/";
    //39.8.162.41 (X)
    //192.168.43.112:80
    //192.168.1.100:80

    //RetrofitClient
    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }

    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Placed";
            case 1:
                return "Processing";
            case 2:
                return "Shipping";
            case 3:
                return "Shipped";
            case -1:
                return "Cancelled";
            default:
                return "Order Error";
        }
    }

}
