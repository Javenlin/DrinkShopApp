package com.osiog.myoldmancare.Database.DataSource;

import com.osiog.myoldmancare.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by OSIOG on 2018/6/28.
 */

public interface ICartDataSource {

    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemByID(int cartItemID);
    int countCartItems();
    int sumPrice();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
