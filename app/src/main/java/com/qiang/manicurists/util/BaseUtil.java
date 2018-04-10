package com.qiang.manicurists.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.qiang.manicurists.view.viewpagerutil.FixedSpeedScroller;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/20 11:27
 * 修改备注：
 */
public class BaseUtil {

    public static void GoIntent(Activity activity, Class<?> cla) {
        Intent intent = new Intent(activity.getApplicationContext(), cla);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void GoIntent(Activity activity, Class<?> cla,String key, Object obj) {
        Intent intent = new Intent(activity.getApplicationContext(), cla);
        if(obj != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(key, (Serializable) obj);
            intent.putExtras(bundle);
        }
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void ShowToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void ShowLog(String tag, String text) {
        Log.d(tag, text);
    }

    //重新计算gridview的高度
    public static void setgridviewHeightBasedOnChildren(GridView gridview,int col) {
        // 获取gridview的adapter  
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列  
        int mcol = col;// gridview.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，
        //计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加  
        for (int i = 0; i < listAdapter.getCount(); i += mcol) {
            // 获取gridview的每一个item  
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0);
            // 获取item的高度和  
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取gridview的布局参数  
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin  
//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数  
        gridview.setLayoutParams(params);
    }

    /**
     * 获得指定日期的后的第几天
     * @param i
     * @return
     */
    public static String getSpecifiedDayAfter(int i){
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,i);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("M月d日");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获得指定日期的全天时间段
     * @return
     */
    public static ArrayList<String> getSpecifiedAllTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date start_time = null;
        Date end_time = null;
        try {
            start_time = df.parse("08:00:00");
            end_time = df.parse("24:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startLong = start_time.getTime();
        long endtLong = end_time.getTime();
        // System.out.println(startLong + "---" + endtLong);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
        ArrayList<String> time_list = new ArrayList<String>();
        long i;
        for (i = startLong; i < endtLong; i = i + 30 * 60 * 1000) {
            Date date = new Date(i);
            time_list.add(df2.format(date));
        }
        return time_list;
    }

    /**
     * 获得指定日期的全天时间段
     * @return
     */
    public static ArrayList<String> getSpecifiedDayAllTime(String date_str){
        SimpleDateFormat df = new SimpleDateFormat("M月d日 HH:mm:ss");
        Date start_time = null;
        Date end_time = null;
        try {
            start_time = df.parse(date_str+" 08:00:00");
            end_time = df.parse(date_str+" 24:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startLong = start_time.getTime();
        long endtLong = end_time.getTime();
        // System.out.println(startLong + "---" + endtLong);
        SimpleDateFormat df2 = new SimpleDateFormat("M月d日 HH:mm");
        ArrayList<String> time_list = new ArrayList<String>();
        long i;
        for (i = startLong; i < endtLong; i = i + 30 * 60 * 1000) {
            Date date = new Date(i);
            time_list.add(df2.format(date));
            System.out.println(df2.format(date));
        }
        return time_list;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取SD卡的根路径
     * @return
     */
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Log.w("Utility", "couldn't get connectivity manager");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            Log.d("Utility", "network is available");
                            return true;
                        }
                    }
                }
            }
        }
        Log.d("Utility", "network is not available");
        return false;
    }

    /**
     * 设置ViewPager的滑动速度
     *
     * */
    public static void setViewPagerScrollSpeed(ViewPager mViewPager,int mDuration){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller( mViewPager.getContext( ) );
            scroller.setmDuration(mDuration);
            mScroller.set( mViewPager, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }
}
