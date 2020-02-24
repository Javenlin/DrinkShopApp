package com.osiog.myoldmancare.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by OSIOG on 2018/7/6.
 */

@Entity(tableName = "Favorite")
public class Favorite {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "price")
    public String price;

    @ColumnInfo(name = "menuID")
    public String menuID;
}
