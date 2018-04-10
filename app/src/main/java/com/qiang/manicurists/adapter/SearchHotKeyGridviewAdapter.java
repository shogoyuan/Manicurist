package com.qiang.manicurists.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiang.manicurists.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/22.
 */
public class SearchHotKeyGridviewAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<String> mkey_list;
    public SearchHotKeyGridviewAdapter(Context context,ArrayList<String> key_list){
        mcontext = context;
        mkey_list = key_list;
    }
    @Override
    public int getCount() {
        return mkey_list.size();
    }

    @Override
    public Object getItem(int position) {
        return mkey_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.layout_search_hotkey_gridview_item,null);
            holder.btn = (TextView) convertView.findViewById(R.id.search_gridview_hotkey_btn_id);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.btn.setText(mkey_list.get(position));
        return convertView;
    }

    class Holder{
        TextView btn;
    }
}
