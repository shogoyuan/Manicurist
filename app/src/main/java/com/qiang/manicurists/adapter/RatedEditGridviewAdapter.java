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
 * Created by Administrator on 2016/8/1.
 */
public class RatedEditGridviewAdapter extends BaseAdapter{
    private Context mcontext;
    private ArrayList<Rated_Url> mrated_pic_list;
    private int win_width;
    private boolean isShowDel = false;
    private callbackListener mlistener;

    public RatedEditGridviewAdapter(Context context, ArrayList<Rated_Url> rated_pic_list, int gridview_width,callbackListener listener){
        mcontext = context;
        mrated_pic_list = rated_pic_list;
        win_width = gridview_width;
        mlistener = listener;
    }

    public void setisShowDel(boolean isShow){
        isShowDel = isShow;
        notifyDataSetChanged();
    }

    public boolean getisShowDel(){
        return isShowDel;
    }

    public ArrayList<Rated_Url> getPicList(){
        return  mrated_pic_list;
    }
    @Override
    public int getCount() {
        return mrated_pic_list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.layout_goods_rated_gridview_item,null);
            holder.img = (ImageView) convertView.findViewById(R.id.rated_pic_id);
            holder.Del_btn = (ImageView) convertView.findViewById(R.id.rated_pic_del_id);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        Glide.with(mcontext).load(mrated_pic_list.get(position).getUrl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(win_width,win_width).centerCrop().into(holder.img);
        if (isShowDel){
            holder.Del_btn.setVisibility(View.VISIBLE);
            holder.Del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mrated_pic_list.remove(position);
                    setisShowDel(false);
                    if (mrated_pic_list.size() ==0) mlistener.callback();
                }
            });
        }else{
            holder.Del_btn.setVisibility(View.GONE);
        }
        return convertView;
    }

    class Holder{
        ImageView img;
        ImageView Del_btn;
    }

    public interface callbackListener{
        void callback();
    }
}
