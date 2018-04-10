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
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/10 16:35
 * 修改备注：
 */
public class GoodListSiftingsListviewAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<String> mstr_list;

    public GoodListSiftingsListviewAdapter(Context context, ArrayList<String> str_list){
        mcontext = context;
        mstr_list = str_list;
    }
    @Override
    public int getCount() {
        return mstr_list.size();
    }

    @Override
    public Object getItem(int position) {
        return mstr_list.get(position);
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
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_goodlist_siftings_listview_item,null);
            holder.text = (TextView) convertView.findViewById(R.id.dialog_siftings_listview_text_id);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.text.setText(mstr_list.get(position));
        return convertView;
    }

    class Holder{
        TextView text;
    }
}
