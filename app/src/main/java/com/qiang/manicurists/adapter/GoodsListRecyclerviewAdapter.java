package com.qiang.manicurists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.view.RoundImageView;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/27 15:07
 * 修改备注：
 */
public class GoodsListRecyclerviewAdapter extends BaseRecyclerAdapter<Goods> {
    private Context mcontext;
    private int win_width;
    public GoodsListRecyclerviewAdapter(ArrayList<Goods> img_list,int win_width) {
        addGoodsDatas(img_list);
        this.win_width = win_width-10;
    }
    @Override
    public ViewHolder onCreate(ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_goodslist_recyclerview_item, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBind(ViewHolder viewHolder, int RealPosition, Goods goods) {
        if(viewHolder instanceof Holder) {
            Glide.with(mcontext).load(goods.getGoodsurl())
                    .placeholder(R.drawable.aliwx_default_photo)
                    .error(R.mipmap.aliwx_image_download_fail_view)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(win_width,win_width).centerCrop().into(((Holder) viewHolder).img);
            ((Holder) viewHolder).name.setText(goods.getGoodsName());
            ((Holder) viewHolder).price.setText(goods.getGoodsDiscount());
            ((Holder) viewHolder).like.setText(goods.getGoodsLike()+"人喜欢");
            ((Holder) viewHolder).craftsmen_name.setText(goods.getGoodsCraftsmen().getCraftsmenName());
            Glide.with(mcontext).load(Integer.parseInt(goods.getGoodsCraftsmen().getCraftsmenIcon()))
                    .placeholder(R.drawable.aliwx_default_photo)
                    .error(R.mipmap.aliwx_image_download_fail_view)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(win_width/8,win_width/8).centerCrop().into(((Holder) viewHolder).craftsmen_img);
//            ((Holder) viewHolder).craftsmen_img.setImageBitmap(BitmapFactory.decodeResource(mcontext.getResources(),Integer.parseInt(goods.getGoodsCraftsmen().getCraftsmenIcon())));
            ((Holder) viewHolder).craftsmen_rated.setImageResource(LaunchActivity.rated_id[Integer.parseInt(goods.getGoodsCraftsmen().getCraftsmenLevel())]);
        }
    }

    class Holder extends BaseRecyclerAdapter.Holder {
        public ImageView img,craftsmen_rated;
        public RoundImageView craftsmen_img;
        public TextView name,price,like,craftsmen_name;

        public Holder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.goodslist_recycleview_img_id);
            craftsmen_img = (RoundImageView) itemView.findViewById(R.id.goodslist_recycleview_craftsmen_img_id);
            craftsmen_rated = (ImageView) itemView.findViewById(R.id.goodslist_recycleview_craftsmen_rated_id);
            name = (TextView) itemView.findViewById(R.id.goodslist_recycleview_name_id);
            price = (TextView) itemView.findViewById(R.id.goodslist_recycleview_price_id);
            like = (TextView) itemView.findViewById(R.id.goodslist_recycleview_like_id);
            craftsmen_name = (TextView) itemView.findViewById(R.id.goodslist_recycleview_craftsmen_name_id);

        }
    }
}
