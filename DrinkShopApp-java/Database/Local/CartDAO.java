package com.osiog.myoldmancare.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.osiog.myoldmancare.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by OSIOG on 2018/6/28.
 */


//ANNOTATE @DAO HERE
@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id = :cartItemID")
    Flowable<List<Cart>> getCartItemByID(int cartItemID);

    @Query("SELECT COUNT(*) from Cart")
    int countCartItems();

    @Query("SELECT SUM(Price) from Cart")
    int sumPrice();


    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart cart);

}
