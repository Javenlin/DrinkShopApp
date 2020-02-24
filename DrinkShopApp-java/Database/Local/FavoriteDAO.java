package com.osiog.myoldmancare.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.osiog.myoldmancare.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by OSIOG on 2018/7/6.
 */

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE id=:itemID)")
    int isFavorite(int itemID);

    @Insert
    void insertFav(Favorite...favorites);

    @Delete
    void delete(Favorite favorite);
}
