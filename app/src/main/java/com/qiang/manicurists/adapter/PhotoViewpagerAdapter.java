package com.qiang.manicurists.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.bean.Rated_Url;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/26 22:56
 * 修改备注：
 */
public class PhotoViewpagerAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<Rated_Url> url_list;
    private onPicClickListener listener;
    private int img_width;

    public PhotoViewpagerAdapter(Context context,ArrayList<Rated_Url> url_list,onPicClickListener listener){
        this.context = context;
        this.url_list = url_list;
        this.listener = listener;
        img_width = LaunchActivity.windows_height;
    }
    @Override
    public int getCount() {
        return url_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果用的不是view列表，可以用这方法。如果用的是view列表，则要remove   list.get(position)
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_photo_viewpager_item,null);
        PhotoView img = (PhotoView) view.findViewById(R.id.photo_viewpager_img_id);
        Glide.with(context).load(url_list.get(position).getUrl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(img_width,img_width).fitCenter().into(img);
//        img.setBackgroundDrawable(new BitmapDrawable(img_list.get(position)));
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(img);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                listener.onClick(view);
            }
        });
        container.addView(view);//加进父容器后才会显示
        return view;
    }

    public interface onPicClickListener{
        void onClick(View v);
    }
}
