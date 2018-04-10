package com.qiang.manicurists.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.util.BaseUtil;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/21 22:56
 * 修改备注：
 */
public class HomeViewpagerAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<Integer> img_list;
    private int mposition;
    private int win_width;

    public HomeViewpagerAdapter(Context context,ArrayList<Integer> img_list){
        this.context = context;
        this.img_list = img_list;
        win_width = LaunchActivity.windows_width;
    }
    @Override
    public int getCount() {
//        return img_list.size();
        //设置成最大，使用户看不到边界
        return Integer.MAX_VALUE;
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
        //对ViewPager页号求模取出View列表中要显示的项
        mposition = position % img_list.size();
        if (position<0){
            mposition = img_list.size()+mposition;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_viewpager_item,null);
        ImageView img = (ImageView) view.findViewById(R.id.home_viewpager_img_id);
//        img.setImageBitmap(img_list.get(mposition));
//        img.setBackgroundDrawable(context.getResources().getDrawable(img_list.get(mposition)));
        Glide.with(context).load(img_list.get(mposition))
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.ShowToast(context,"点击顶部切换卡"+mposition+"号");
            }
        });
        container.addView(view);//加进父容器后才会显示
        return view;
    }

}
