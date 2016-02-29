package com.oscode.gatherthis;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

public class Database {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "gatherer.db";
    public static final String TABLE_NAME = "gatherertb";
    public static final String NAME = "name";
    public static final String MANA_COST = "manaCost";
    public static final String CONVERTED_MANA_COST = "cmc";
    public static final String COLORS = "colors";
    public static final String TYPE = "type";
    public static final String SUPERTYPES = "supertypes";
    public static final String TYPES = "types";
    public static final String SUBTYPES = "subtypes";
    public static final String RARITY = "rarity";
    public static final String TEXT = "text";
    public static final String FLAVOR = "flavor";
    public static final String ARTIST = "artist";
    public static final String NUMBER = "number";
    public static final String POWER = "power";
    public static final String TOUGHNESS = "toughness";
    public static final String LAYOUT = "normal";
    public static final String MULTIVERSE_ID = "multiverseid";
    public static final String IMAGE_NAME = "imagename";
    public static final String SET = "setblock";
    public static final String RULINGS = "rulings";
    public static final String LEGALITIES = "legalities";
    public static final String PRINTINGS = "printings";
    
    JSONArray mastercardlist;
    DB openHelper;
    private SQLiteDatabase database;
    String set = "";

    ProgressDialog diag;
    public final static String TAG_CARDS = "cards";

//    private boolean checkDatabase() {
//        //this.deleteDatabase("gatherer.db");
//        SQLiteDatabase checkDB = null;
//        try {
//            checkDB = SQLiteDatabase.openDatabase(getApplicationContext()
//                    .getDatabasePath("gatherer.db").toString(), null,
//                    SQLiteDatabase.OPEN_READONLY);
//            checkDB.close();
//        }
//        catch (SQLiteException e) {
//            // database doesn't exist yet.
//        }
//        return checkDB != null ? true : false;
//    }

//    @Override 
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.datamgmt);
//        if (checkDatabase()) {
//            Intent i = new Intent(getApplicationContext(), FragmentManager.class);
//            startActivity(i);
//            finish();
//        }
//        else {
//            openHelper = new DB(getApplicationContext(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
//            database = openHelper.getWritableDatabase();
//            new GetCards().execute();
//        }
//
//    }

    public Database(SQLiteDatabase db) {
    	database = db;
    }

    class Card {
        String name = "n";
        String manacost = "n";
        int cmc = 0;
        String colors = "n";
        String type = "n";
        String supertypes = "n";
        String types = "n";
        String subtypes = "n";
        String rarity = "n";
        String text = "n";
        String flavor = "n";
        String artist = "n";
        String number = "n";
        String power = "n";
        String toughness = "n";
        String layout = "n";
        int multiverseid = 0;
        String imageName = "n";
        String rulings = "n";
        String legality = "n";
        String printings = "n";

        void save() {
            saveCard(name, manacost, cmc, colors, type, supertypes, types, subtypes,
                    rarity, text, flavor, artist, number, power, toughness,
                    layout, multiverseid, imageName, set, rulings, legality, printings);
        }
    }
    public void saveCard(String name, String manacost, int cmc, String colors,
            String type, String supertypes, String types, String subtypes,
            String rarity, String text, String flavor, String artist,
            String number, String power, String toughness, String layout,
            int multiverseid, String imageName, String set, String rulings, String legality, String printings) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(MANA_COST, manacost);
        cv.put(CONVERTED_MANA_COST, cmc);
        cv.put(COLORS, colors);
        cv.put(TYPE, type);
        cv.put(SUPERTYPES, supertypes);
        cv.put(TYPES, types);
        cv.put(SUBTYPES, subtypes);
        cv.put(RARITY, rarity);
        cv.put(TEXT, text);
        cv.put(FLAVOR, flavor);
        cv.put(ARTIST, artist);
        cv.put(NUMBER, number);
        cv.put(POWER, power);
        cv.put(TOUGHNESS, toughness);
        cv.put(LAYOUT, layout);
        cv.put(MULTIVERSE_ID, multiverseid);
        cv.put(IMAGE_NAME, imageName);
        cv.put(SET, set);
        cv.put(RULINGS, rulings);
        cv.put(LEGALITIES, legality);
        cv.put(PRINTINGS, printings);
        database.insert(TABLE_NAME, null, cv);

    }
}
