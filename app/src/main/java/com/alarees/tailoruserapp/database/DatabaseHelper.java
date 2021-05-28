package com.alarees.tailoruserapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alarees.tailoruserapp.cart.CartUnstitchedModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mydatabase.db";
    public static final String TABLE_NAME = "unstitcheditems";
    public static final String ICOL_1 = "cid";
    public static final String ICOL_2 = "userId";
    public static final String ICOL_3 = "orderId";
    public static final String ICOL_4 = "clothName";
    public static final String ICOL_5 = "price";
    public static final String ICOL_6 = "cuffType";
    public static final String ICOL_7 = "placketType";
    public static final String ICOL_8 = "pocketType";
    public static final String ICOL_9 = "collarType";
    public static final String ICOL_10 = "quantity";
    public static final String ICOL_11 = "clothimg";
    public static final String ICOL_12 = "measurementId";


    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (cid INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT ,orderId TEXT , clothName TEXT , price TEXT, cuffType Text, placketType Text, pocketType Text, collarType Text,quantity Text,clothimg Text,measurementId Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUnstitchedData(CartUnstitchedModel rModel) {
        if (getID(rModel.getClothName(), String.valueOf(rModel.getClothimg())) == -1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ICOL_2, rModel.getUserId());
            contentValues.put(ICOL_3, rModel.getOrderId());
            contentValues.put(ICOL_4, rModel.getClothName());
            contentValues.put(ICOL_5, rModel.getPrice());
            contentValues.put(ICOL_6, rModel.getCuffType());
            contentValues.put(ICOL_7, rModel.getPlacketType());
            contentValues.put(ICOL_8, rModel.getPocketType());
            contentValues.put(ICOL_9, rModel.getCollarType());
            contentValues.put(ICOL_10, rModel.getQuantity());
            contentValues.put(ICOL_11, rModel.getClothimg());
            contentValues.put(ICOL_12,rModel.getMeasurementId());
            long result = db.insert(TABLE_NAME, null, contentValues);

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {

            return updateUnstitchedData(rModel.getCartId(), String.valueOf(rModel.getQuantity()), String.valueOf(rModel.getPrice()));
        }
    }

    public int getID(String clothName, String clothimg) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"clothName"}, "clothName =? AND clothimg =? ", new String[]{clothName, clothimg}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("clothName"));
        return -1;
    }

    public int getCard(String cid, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"quantity"}, "cid =? AND price =? ", new String[]{cid, price}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("quantity"));
        } else {
            return -1;

        }
    }

    public Cursor getUnstitchedData() {
        Cursor res;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("select * from " + TABLE_NAME, null);
            return res;
        } catch (Exception e) {
        }
        return null;
    }

    public boolean updateUnstitchedData(String cid, String qty, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_1, cid);
        contentValues.put(ICOL_10, qty);
        contentValues.put(ICOL_5, price);
        db.update(TABLE_NAME, contentValues, "cid = ?", new String[]{cid});
        return true;
    }

    public void DeleteCard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        //   txtCount.setText("0");
        //  lvlMycart.setVisibility(View.GONE);
    }

    public Integer deleteRData(String cid, String clothimg) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAME, "cid = ? AND clothimg =?", new String[]{cid, clothimg});
        Cursor res = getUnstitchedData();
        //  txtCount.setText("" + res.getCount());
        if (res.getCount() == 0) {
            //    lvlMycart.setVisibility(View.GONE);
        } else {
            //  HomeActivity activity = new HomeActivity();
            //    if (activity.isView()) {
            //    lvlMycart.setVisibility(View.VISIBLE);
        }
        //}
        return a;
    }

}