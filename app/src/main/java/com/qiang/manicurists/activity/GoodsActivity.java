package com.qiang.manicurists.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.GoodsRatedGridviewAdapter;
import com.qiang.manicurists.bean.Craftsmen;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.bean.Rated;
import com.qiang.manicurists.bean.Rated_Url;
import com.qiang.manicurists.dialog.GoodsBookingDialog;
import com.qiang.manicurists.util.BaseUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.DampScrollViewView;
import com.qiang.manicurists.view.ExpandableTextView;
import com.qiang.manicurists.view.MyBookingView;
import com.qiang.manicurists.view.RoundImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class GoodsActivity extends AppCompatActivity {
    //toolbar的相关控件
    private Toolbar goods_toolbar;
    private Button back;
    //scrollview的相关控件
    private DampScrollViewView goods_scrollview;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        Goods goods = (Goods) getIntent().getSerializableExtra("goods");
        initContent(goods);
        initToolbar(goods.getGoodsName());
        initBuy(goods);
    }

    private void initBuy(final Goods goods) {
        Button goods_buy_btn = (Button) this.findViewById(R.id.goods_buy_btn_id);
        goods_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.GoIntent(GoodsActivity.this,BuyActivity.class,"goods",goods);
            }
        });
    }

    private void initContent(Goods goods) {
        ImageView topimg = (ImageView) this.findViewById(R.id.goods_topimg_id);
        Glide.with(this).load(goods.getGoodsurl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(topimg);
        goods_scrollview = (DampScrollViewView) this.findViewById(R.id.goods_scrollview);
        goods_scrollview.setImageView(topimg);
        //基础的商品介绍
        TextView goods_name = (TextView) this.findViewById(R.id.goods_name_id);
        goods_name.setText(goods.getGoodsName());
        TextView goods_discount = (TextView) this.findViewById(R.id.goods_discount_id);
        goods_discount.setText(goods.getGoodsDiscount());
        TextView goods_price = (TextView) this.findViewById(R.id.goods_price_id);
        goods_price.setText(goods.getGoodsPrice());
        goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView goods_like = (TextView) this.findViewById(R.id.goods_like_id);
        goods_like.setText(goods.getGoodsLike()+"个人喜欢");
        TextView goods_content = (TextView) this.findViewById(R.id.goods_content_id);
        goods_content.setText(goods.getGoodsContent());
        LinearLayout goods_time_linear = (LinearLayout) this.findViewById(R.id.goods_time_linear_id);
//        ArrayList<String> time_list = goods.getGoodsTime();
//        for (int i = 0 ; i < time_list.size() ; i++){
            View time = LayoutInflater.from(this).inflate(R.layout.layout_goods_text_item,null);
            TextView time_text = (TextView) time.findViewById(R.id.goods_item_text_id);
            time_text.setText(goods.getGoodsTime());
            goods_time_linear.addView(time);
//        }
        LinearLayout goods_recommend_linear = (LinearLayout) this.findViewById(R.id.goods_recommend_linear_id);
        ArrayList<String> recommend_list = goods.getGoodsRecommend();
        for (int i = 0 ; i < recommend_list.size() ; i++){
            View recommend = LayoutInflater.from(this).inflate(R.layout.layout_goods_text_item,null);
            TextView recommend_text = (TextView) time.findViewById(R.id.goods_item_text_id);
            recommend_text.setText(recommend_list.get(i));
            goods_recommend_linear.addView(recommend);
        }
        //预约时间
        initBooking(goods);
        //服务商圈
        TextView goods_services_name = (TextView) this.findViewById(R.id.goods_service_name);
//        ArrayList<String> services_list = goods.getGoodsServiceConfines();
        String services = goods.getGoodsServiceConfines();
        goods_services_name.setText("服务商圈("+services.split(",").length+")");
        ExpandableTextView goods_services_content = (ExpandableTextView) this.findViewById(R.id.goods_service_content);
//        for (int i = 0;i<services_list.size();i++){
            goods_services_content.append(services+"  ");
//        }
        //评价
        initRated(goods);
        //美甲师
        initCraftsmen(goods);
    }

    private void initCraftsmen(Goods goods) {
        Craftsmen craftsmen = goods.getGoodsCraftsmen();
        RoundImageView goods_craftsmen_img = (RoundImageView) this.findViewById(R.id.goods_craftsmen_img_id);
        Glide.with(this).load(Integer.parseInt(craftsmen.getCraftsmenIcon())).placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view).into(goods_craftsmen_img);
        TextView goods_craftsmen_name = (TextView) this.findViewById(R.id.goods_craftsmen_name_id);
        goods_craftsmen_name.setText(craftsmen.getCraftsmenName());
        TextView goods_craftsmen_position = (TextView) this.findViewById(R.id.goods_craftsmen_position_id);
        goods_craftsmen_position.setText(craftsmen.getCraftsmenPosition());
        TextView goods_craftsmen_level = (TextView) this.findViewById(R.id.goods_craftsmen_level_id);
        goods_craftsmen_level.setText(craftsmen.getCraftsmenLevel());
    }

    private void initRated(final Goods goods) {
        LinearLayout goods_rated_linear = (LinearLayout) this.findViewById(R.id.goods_rated_linear_id);
        final ArrayList<Rated> rated_list = goods.getGoodsRated();
        int show_count = rated_list.size() > 3?3:rated_list.size();
        for (int i = 0; i<show_count;i++){
            Rated rated = rated_list.get(i);
            View rated_view = LayoutInflater.from(this).inflate(R.layout.layout_goods_rated_item,null);
            RoundImageView rated_img = (RoundImageView) rated_view.findViewById(R.id.goods_rated_img_id);
            Glide.with(this).load(Integer.parseInt(rated.getRatedIcon())).placeholder(R.drawable.aliwx_default_photo)
                    .error(R.mipmap.aliwx_image_download_fail_view).into(rated_img);
            TextView rated_name = (TextView) rated_view.findViewById(R.id.goods_rated_name_id);
            rated_name.setText(rated.getRatedNum());
            ImageView rated_level = (ImageView) rated_view.findViewById(R.id.goods_rated_level_id);
            rated_level.setImageResource(LaunchActivity.rated_id[Integer.parseInt(rated.getRatedLevel())]);
            ExpandableTextView rated_content = (ExpandableTextView) rated_view.findViewById(R.id.goods_rated_content_id);
            rated_content.setText(rated.getRatedContent());
            GridView rated_gridview = (GridView) rated_view.findViewById(R.id.goods_rated_gridview_id);
            final ArrayList<Rated_Url> rated_img_list = rated.getRatedPicUrl();
            if(rated_img_list.size() != 0){
                int gridview_width = LaunchActivity.windows_width/2;
                int num =3;
                rated_gridview.setVisibility(View.VISIBLE);
                GoodsRatedGridviewAdapter adapter = new GoodsRatedGridviewAdapter(GoodsActivity.this,rated_img_list,gridview_width/num);
                rated_gridview.setAdapter(adapter);
                BaseUtil.setgridviewHeightBasedOnChildren(rated_gridview,num);
                ViewGroup.LayoutParams params = rated_gridview.getLayoutParams();
                params.width = gridview_width;
                rated_gridview.setLayoutParams(params);
                rated_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //记录下点击的图片的位置和大小
                        int[] screenLocation = new int[2];
                        ImageView imageview = (ImageView) view.findViewById(R.id.rated_pic_id);
                        imageview.getLocationOnScreen(screenLocation);
                        Intent intent = new Intent(GoodsActivity.this,PhotoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position",position);
                        bundle.putSerializable("rated_url_list",rated_img_list);
                        bundle.putInt("left", screenLocation[0]);
                        bundle.putInt("top", screenLocation[1]);
                        bundle.putInt("width", imageview.getWidth());
                        bundle.putInt("height", imageview.getHeight());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_activity_push_in,R.anim.anim_activity_push_out);
                    }
                });
            }else{
                rated_gridview.setVisibility(View.GONE);
            }
            TextView rated_date = (TextView) rated_view.findViewById(R.id.goods_rated_date_id);
            rated_date.setText(rated.getRatedDate());
            goods_rated_linear.addView(rated_view);
        }
        LinearLayout more_linear = (LinearLayout) this.findViewById(R.id.goods_rated_more_id);
        more_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsActivity.this, RatedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title","全部评论");
                bundle.putSerializable("rated_list",(Serializable) rated_list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        LinearLayout edit_linear = (LinearLayout) this.findViewById(R.id.goods_rated_edit_id);
        edit_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.GoIntent(GoodsActivity.this,RatedEditActivity.class,"goods",goods);
            }
        });
    }

    private void initBooking(final Goods goods) {
        int width = LaunchActivity.windows_width;

        MyBookingView goods_booking_tag1 = (MyBookingView) this.findViewById(R.id.goods_booking_tag1_id);
        goods_booking_tag1.setIsSingle(true);
        goods_booking_tag1.setIsOk(true);
        goods_booking_tag1.setWidth(width);

        MyBookingView goods_booking_tag2 = (MyBookingView) this.findViewById(R.id.goods_booking_tag2_id);
        goods_booking_tag2.setIsSingle(true);
        goods_booking_tag2.setIsOk(false);
        goods_booking_tag2.setWidth(width);

        MyBookingView goods_booking_time = (MyBookingView) this.findViewById(R.id.goods_booking_time_id);
        goods_booking_time.setType(MyBookingView.TYPE_TIME);
        goods_booking_time.setWidth(width);

        MyBookingView goods_booking = (MyBookingView) this.findViewById(R.id.goods_booking_id);
        goods_booking.setType(MyBookingView.TYPE_BOOKING);
        goods_booking.setWidth(width);
        goods_booking.setIsGoodsView(true);

        goods_booking.setList(goods.getGoodsBooking());

        TextView goods_booking_more = (TextView) this.findViewById(R.id.goods_booking_more_id);
        goods_booking_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsBookingDialog dialog = new GoodsBookingDialog(GoodsActivity.this,goods);
                dialog.show();
            }
        });
    }

    private void initToolbar(String title_text) {
        final int alpha_height = LaunchActivity.windows_width;
        goods_toolbar = (Toolbar) this.findViewById(R.id.goods_toolbar_id);
        setSupportActionBar(goods_toolbar);
        ViewUtil.initAfterSetContentView(this,goods_toolbar);
        TextView title = (TextView) this.findViewById(R.id.goods_toolbar_back_title_id);
        title.setText(title_text);
        back = (Button) this.findViewById(R.id.goods_toolbar_back_btn_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        goods_toolbar.getBackground().setAlpha(0);//让背景先变成透明
        goods_scrollview.setScrollviewlistener(new DampScrollViewView.onScrollViewListener() {
            @Override
            public void onScrollChanged(DampScrollViewView scrollView, int x, int y, int oldx, int oldy) {
                if (y < alpha_height) {
                    float alpha = (float) y / alpha_height;
                    if (alpha >= 0) {
                        if(alpha == 0) back.setBackgroundResource(R.mipmap.icon_shop_back);
                        else back.setBackgroundResource(R.mipmap.icon_shop_back2);
                        goods_toolbar.getBackground().setAlpha((int) (alpha * 255));
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK
                && event.getRepeatCount() == 0){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
