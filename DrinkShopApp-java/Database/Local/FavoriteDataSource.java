package com.osiog.myoldmancare.Database.Local;

import com.osiog.myoldmancare.Database.DataSource.IFavoriteDataSource;
import com.osiog.myoldmancare.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by OSIOG on 2018/7/6.
 */

public class FavoriteDataSource implements IFavoriteDataSource {

    private FavoriteDAO favoriteDAO;
    private static FavoriteDataSource instance;

    public FavoriteDataSource(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public static FavoriteDataSource getInstance(FavoriteDAO favoriteDAO) {
        if(instance == null)
            instance = new FavoriteDataSource(favoriteDAO);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDAO.getFavItems();
    }

    @Override
    public int isFavorite(int itemID) {
        return favoriteDAO.isFavorite(itemID);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDAO.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDAO.delete(favorite);
    }
}
