package com.qiang.manicurists.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.view.GifView;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/25 0:20
 * 修改备注：
 */
public class HomeGridviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> img_list;
    private ArrayList<String> name_list;

    public HomeGridviewAdapter(Context context, ArrayList<Integer> gridview_img_list, ArrayList<String> gridview_name_list) {
        this.context = context;
        this.img_list = gridview_img_list;
        this.name_list = gridview_name_list;
    }

    @Override
    public int getCount() {
        return img_list.size();
    }

    @Override
    public Object getItem(int position) {
        return img_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_home_gridview_item, null);
            holder.img = (GifView) convertView.findViewById(R.id.home_gridview_img_id);
            holder.name = (TextView) convertView.findViewById(R.id.home_gridview_name_id);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.img.setMovieResource(img_list.get(position));
//        holder.img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),img_list.get(position)));
//        Glide.with(context).load(img_list.get(position))//用glide加载的话颜色会边谈和尺寸会变窄，但消耗内存的差别并不大，色差的原因估计是使用的图片格式不同
//                .placeholder(R.drawable.aliwx_default_photo)
//                .error(R.mipmap.aliwx_image_download_fail_view)
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);
        holder.name.setText(name_list.get(position));
        return convertView;
    }

    class Holder {
        GifView img;
        TextView name;
    }
}
