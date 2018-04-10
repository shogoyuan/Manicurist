package com.qiang.manicurists.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.qiang.manicurists.bean.Rated_Url;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29.
 */
public class SharePreferenceUtil {
    private final String KEY1 = "RATED_TEXT";
    private final String KEY2 = "RATED_IMG_URL";
    private final String KEY3 = "RATED_LEVEL";
    private SharedPreferences sp;
    public SharePreferenceUtil(Context context){
        sp = context.getSharedPreferences("Manicurists_RatedEdit", Context.MODE_PRIVATE);
    }

    public void saveRated(String string, ArrayList<Rated_Url> list,float level){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY1, string);
        StringBuffer str = new StringBuffer();
        str.append(",");
        for (Rated_Url url : list){
            str.append(url.getUrl()+",");
        }
        editor.putString(KEY2,str.toString());
        editor.putFloat(KEY3,level);
        editor.commit();
    }
    public void removeRated(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY1,"");
        editor.putString(KEY2,",");
        editor.putFloat(KEY3,3);
        editor.commit();
    }

    public String  getRatedText(){
        return sp.getString(KEY1,"");
    }

    public ArrayList<Rated_Url>  getRatedImgList(){
        String[] url = (sp.getString(KEY2,"")).split(",");
        ArrayList<Rated_Url> rated_list = new ArrayList<>();
        for (int i = 1;i<url.length;i++) {
            Rated_Url rated = new Rated_Url();
            rated.setUrl(url[i]);
            rated_list.add(rated);
        }
        return rated_list;
    }

    public float getRatedLevel(){
        return sp.getFloat(KEY3,3);
    }
}
