package com.osiog.myoldmancare.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;

import com.osiog.myoldmancare.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by OSIOG on 2018/7/6.
 */

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();

    int isFavorite(int itemID);

    void insertFav(Favorite...favorites);

    void delete(Favorite favorite);
}
