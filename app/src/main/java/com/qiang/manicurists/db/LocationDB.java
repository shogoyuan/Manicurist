package com.qiang.manicurists.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/5 17:07
 * 修改备注：
 */
public class LocationDB {
    private LocationDBHelper locDBHelper;

    public LocationDB(Context context) {
        locDBHelper = new LocationDBHelper(context);
    }

    /**
     * 获取用户保存的地址信息
     *
     * @return
     */
    public ArrayList<String> getData() {
        SQLiteDatabase db = locDBHelper.getReadableDatabase();
        Cursor cursor = db
                .rawQuery(
                        "select location from location_table",
                        new String[]{});
        ArrayList<String> data = new ArrayList<String>();
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * 保存用户添加的地址信息
     *
     * @param path
     */
    public void save(String path) {// int threadid,
        // int position
        SQLiteDatabase db = locDBHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            db.execSQL(
                    "insert into location_table(location) values(?)",
                    new Object[]{path});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    /**
     * 删去用户不要的地址
     *
     * @param path
     */
    public void delete(String path) {
        SQLiteDatabase db = locDBHelper.getWritableDatabase();
        db.execSQL("delete from location_table where location=?",
                new Object[]{path});
        db.close();
    }
}
