package com.qiang.manicurists.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 项目名称：Manicurists
 * 类描述：保存用户地址的数据库
 * 创建人：Qiang
 * 创建时间：2016/7/5 16:39
 * 修改备注：
 */
public class LocationDBHelper extends SQLiteOpenHelper {
    private final String TAG = "LocationDBHelper";
    private static final String DB_NAME = "location.db";
    private static final int DB_VERSION = 1;
    /**
     * 构造器
     * @param context
     */
    public LocationDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        String sql = "Create Table IF NOT EXISTS location_table (id integer primary key autoincrement, location varchar(100))";
        Log.i(TAG, "create Database------------->");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS location_table";
        Log.i(TAG, "update Database------------->");
        db.execSQL(sql);
    }
}
