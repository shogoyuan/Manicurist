package com.qiang.manicurists.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.bean.Goods;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/27 11:28
 * 修改备注：
 */
public class CategoryGoodsGridviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Goods> goods_list;
    private int win_width;

    public CategoryGoodsGridviewAdapter(Context context, ArrayList<Goods> goods_list,int win_width) {
        this.context = context;
        this.goods_list = goods_list;
        this.win_width = win_width - 10;//(-70是因为要减去左边radiogroup的宽度，应该是动态获取的)
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return goods_list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_category_goods_gridview_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.category_goods_gridview_img_id);
            holder.name = (TextView) convertView.findViewById(R.id.category_goods_gridview_name_id);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Goods goods = goods_list.get(position);
        //需要把手机的宽度传进来再计算大小才行
        Glide.with(context).load(goods.getGoodsurl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(win_width,win_width).centerCrop().into(holder.img);
        holder.name.setText(goods.getGoodsName());
        return convertView;
    }

    class Holder {
        ImageView img;
        TextView name;
    }
}
