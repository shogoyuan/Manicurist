package com.qiang.manicurists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.bean.Rated;
import com.qiang.manicurists.bean.Rated_Url;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.view.ExpandableTextView;
import com.qiang.manicurists.view.RoundImageView;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/27 15:07
 * 修改备注：
 */
public class RatedRecyclerviewAdapter extends BaseRecyclerAdapter<Rated> {
    private Context mcontext;
    private onitemclickListener mlistener;
    public RatedRecyclerviewAdapter(ArrayList<Rated> img_list,onitemclickListener listener) {
        addGoodsDatas(img_list);
        mlistener = listener;
    }


    @Override
    public ViewHolder onCreate(ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_goods_rated_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBind(ViewHolder viewHolder, int RealPosition, Rated rated) {
        if(viewHolder instanceof Holder) {
            Glide.with(mcontext).load(Integer.parseInt(rated.getRatedIcon()))
                    .placeholder(R.drawable.aliwx_default_photo)
                    .error(R.mipmap.aliwx_image_download_fail_view)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((Holder) viewHolder).rated_img);
            ((Holder) viewHolder).rated_name.setText(rated.getRatedNum());
            ((Holder) viewHolder).rated_level.setImageResource(LaunchActivity.rated_id[Integer.parseInt(rated.getRatedLevel())]);
            ((Holder) viewHolder).rated_content.setText(rated.getRatedContent());
            final ArrayList<Rated_Url> rated_img_list = rated.getRatedPicUrl();
            if(rated_img_list.size() != 0){
                int gridview_width = LaunchActivity.windows_width/2;
                int num = 3;
                ((Holder) viewHolder).rated_gridview.setVisibility(View.VISIBLE);
                GoodsRatedGridviewAdapter adapter = new GoodsRatedGridviewAdapter(mcontext,rated_img_list,gridview_width/num);
                ((Holder) viewHolder).rated_gridview.setAdapter(adapter);
                BaseUtil.setgridviewHeightBasedOnChildren(((Holder) viewHolder).rated_gridview,num);

                ViewGroup.LayoutParams params = ((Holder) viewHolder).rated_gridview.getLayoutParams();
                params.width = gridview_width;
                ((Holder) viewHolder).rated_gridview.setLayoutParams(params);

                ((Holder) viewHolder).rated_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       mlistener.onItemClick(parent, view, position, id);
                    }
                });
            }else{
                ((Holder) viewHolder).rated_gridview.setVisibility(View.GONE);
            }
            ((Holder) viewHolder).rated_date.setText(rated.getRatedDate());
        }
    }


    class Holder extends BaseRecyclerAdapter.Holder {
        public RoundImageView rated_img;
        public TextView rated_name,rated_date;
        public ImageView rated_level;
        public ExpandableTextView rated_content;
        public GridView rated_gridview;

        public Holder(View itemView) {
            super(itemView);
            rated_img = (RoundImageView) itemView.findViewById(R.id.goods_rated_img_id);
            rated_name = (TextView) itemView.findViewById(R.id.goods_rated_name_id);
            rated_level = (ImageView) itemView.findViewById(R.id.goods_rated_level_id);
            rated_content = (ExpandableTextView) itemView.findViewById(R.id.goods_rated_content_id);
            rated_date = (TextView) itemView.findViewById(R.id.goods_rated_date_id);
            rated_gridview = (GridView) itemView.findViewById(R.id.goods_rated_gridview_id);

        }
    }

    public interface onitemclickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
