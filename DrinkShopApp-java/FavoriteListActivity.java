package com.osiog.myoldmancare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.osiog.myoldmancare.Adapter.FavoriteAdapter;
import com.osiog.myoldmancare.Database.ModelDB.Favorite;
import com.osiog.myoldmancare.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity {

    RecyclerView recycler_fav;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_favorite_list);

        compositeDisposable = new CompositeDisposable();

        //INIT_SECTION
        recycler_fav = (RecyclerView) findViewById(R.id.recycler_fav);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.setHasFixedSize(true);

        loadFavoritesItem();
    }

    private void loadFavoritesItem() {
         Log.e("getFavItems", String.valueOf(Common.favoriteRepository.getFavItems()));
        compositeDisposable.add(Common.favoriteRepository.getFavItems()
        .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displayFavoritesItem(favorites);
                    }
                }));
    }

    private void displayFavoritesItem(List<Favorite> favorites) {
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this,favorites);
        recycler_fav.setAdapter(favoriteAdapter);
    }
}
