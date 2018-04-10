package com.qiang.manicurists.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.bean.Rated_Url;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/26.
 */
public class GoodsRatedGridviewAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<Rated_Url> mrated_pic_list;
    private int win_width;

    public GoodsRatedGridviewAdapter(Context context, ArrayList<Rated_Url> rated_pic_list, int gridview_width){
        mcontext = context;
        mrated_pic_list = rated_pic_list;
        win_width = gridview_width;
    }

    public ArrayList<Rated_Url> getPicList(){
        return  mrated_pic_list;
    }
    @Override
    public int getCount() {
        if (mrated_pic_list.size() > 6) return 6;
        else return mrated_pic_list.size();
    }

    @Override
    public Object getItem(int position) {
        return mrated_pic_list.get(position);
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
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.layout_goods_rated_gridview_item,null);
            holder.img = (ImageView) convertView.findViewById(R.id.rated_pic_id);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        Glide.with(mcontext).load(mrated_pic_list.get(position).getUrl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(win_width,win_width).centerCrop().into(holder.img);
        return convertView;
    }

    class Holder{
        ImageView img;
    }
}
