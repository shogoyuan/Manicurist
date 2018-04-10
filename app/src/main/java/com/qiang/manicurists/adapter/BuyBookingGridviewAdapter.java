package com.qiang.manicurists.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.util.BaseUtil;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/7 9:30
 * 修改备注：
 */
public class BuyBookingGridviewAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<Integer> booking_list;
    private ArrayList<String> time_list;

    public BuyBookingGridviewAdapter(Context context, ArrayList<Integer> booking_list,String day){
        this.mcontext = context;
        this.booking_list = booking_list;
        time_list = BaseUtil.getSpecifiedDayAllTime(day);
    }

    public void setData(ArrayList<Integer> booking_list,String day){
        this.booking_list = booking_list;
        time_list = BaseUtil.getSpecifiedDayAllTime(day);
    }

    @Override
    public int getCount() {
        return booking_list.size();
    }

    @Override
    public Object getItem(int position) {
        return time_list.get(position);
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
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_booking_choose_item,null);
            holder.booking_time = (TextView) convertView.findViewById(R.id.dialog_booking_gridview_item_id);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.booking_time.setText(time_list.get(position));
        if (booking_list.get(position) == 1){//被约
            holder.booking_time.setTextColor(Color.GRAY);
        }else{
            holder.booking_time.setTextColor(Color.GREEN);
        }

        return convertView;
    }

    class Holder{
        TextView booking_time;
    }


}
