package com.osiog.myoldmancare.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.osiog.myoldmancare.Database.ModelDB.Cart;
import com.osiog.myoldmancare.Database.ModelDB.Favorite;

/**
 * Created by OSIOG on 2018/6/28.
 */
@Database(entities = {Cart.class, Favorite.class},version = 1)
public abstract class EDMTRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static EDMTRoomDatabase instance;

    public static EDMTRoomDatabase getInstance(Context context){

        if(instance == null)
            instance = Room.databaseBuilder(context,EDMTRoomDatabase.class,"EDMT_DrinkShopDB")
                    .allowMainThreadQueries()
                    .build();

        return instance;
    }

}
