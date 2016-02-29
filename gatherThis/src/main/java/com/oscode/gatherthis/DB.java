package com.oscode.gatherthis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    public DB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String TEXT_TYPE = " TEXT, ";
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Database.TABLE_NAME + " (" + Database.NAME
                        + TEXT_TYPE + Database.MANA_COST + TEXT_TYPE
                        + Database.CONVERTED_MANA_COST + TEXT_TYPE
                        + Database.COLORS + TEXT_TYPE + Database.TYPE
                        + TEXT_TYPE + Database.SUPERTYPES + TEXT_TYPE
                        + Database.TYPES + TEXT_TYPE + Database.SUBTYPES
                        + TEXT_TYPE + Database.RARITY + TEXT_TYPE
                        + Database.TEXT + TEXT_TYPE + Database.FLAVOR
                        + TEXT_TYPE + Database.ARTIST + TEXT_TYPE
                        + Database.NUMBER + TEXT_TYPE + Database.POWER
                        + TEXT_TYPE + Database.TOUGHNESS + TEXT_TYPE
                        + Database.LAYOUT + TEXT_TYPE
                        + Database.MULTIVERSE_ID + TEXT_TYPE
                        + Database.IMAGE_NAME + TEXT_TYPE + Database.SET + TEXT_TYPE + Database.RULINGS + TEXT_TYPE + Database.LEGALITIES
                        + TEXT_TYPE + Database.PRINTINGS + " TEXT " + ");";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_NAME);
        onCreate(db);
    }

}