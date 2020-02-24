package com.osiog.myoldmancare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.osiog.myoldmancare.Adapter.DrinkAdapter;
import com.osiog.myoldmancare.Model.Drink;
import com.osiog.myoldmancare.Retrofit.IDrinkShopAPI;
import com.osiog.myoldmancare.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    List<String> suggestList = new ArrayList<>();
    List<Drink> localDataSource = new ArrayList<>();
    MaterialSearchBar searchBar;

    IDrinkShopAPI mService;
    RecyclerView recycler_search;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DrinkAdapter searchAdapter, adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_search);

        //INIT_SERVICE
        mService = Common.getAPI();

        recycler_search = (RecyclerView) findViewById(R.id.recycler_search);
        recycler_search.setLayoutManager(new GridLayoutManager(this, 2));

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("請輸入您的搜尋");

        loadAllDrinks();

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) { //若有大寫則轉成小寫
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recycler_search.setAdapter(adapter);    //若搜尋不到 恢復飲料的完整列表
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    //==================
    // 開始進行搜尋與比對
    //==================
    private void startSearch(CharSequence text) {
        List<Drink> result = new ArrayList<>();
        for (Drink drink : localDataSource) {
            if (drink.Name.contains(text))
                result.add(drink);
            searchAdapter = new DrinkAdapter(this, result);
            recycler_search.setAdapter(searchAdapter);
        }
    }

    //=====================
    // 當此Activity進入背景
    //=====================
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    //================
    // 讀取所有商品資料
    //================
    private void loadAllDrinks() {
        compositeDisposable.add(mService.getAllDrinks().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayListDrink(drinks);
                        buildSuggestList(drinks);
                    }
                })
        );
    }

    //================
    // 列出所有商品
    //================
    private void displayListDrink(List<Drink> drinks) {
        localDataSource = drinks;
        adapter = new DrinkAdapter(this, drinks);
        recycler_search.setAdapter(adapter);
    }

    //================
    // 建立建議列表
    //================
    private void buildSuggestList(List<Drink> drinks) {
        for (Drink drink : drinks)
            suggestList.add(drink.Name);
        searchBar.setLastSuggestions(suggestList);
    }
}
