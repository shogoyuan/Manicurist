package com.qiang.manicurists.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiang.manicurists.R;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.dialog.BuyBookingDialog;
import com.qiang.manicurists.dialog.BuyLocationDialog;
import com.qiang.manicurists.util.BtnUtil;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.morphingbutton.IndeterminateProgressButton;

public class BuyActivity extends AppCompatActivity {
    private BtnUtil btn_util;
    private TextView location_text,booking_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Goods goods = (Goods) getIntent().getSerializableExtra("goods");
        btn_util = new BtnUtil(getApplicationContext());
        initToolbar(goods.getGoodsName());
        initContent(goods);
        initBuy(goods);
    }

    private void initBuy(Goods goods) {
        final IndeterminateProgressButton goods_buy_btn = (IndeterminateProgressButton) this.findViewById(R.id.buy_confirmbuy_id);
        goods_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMorphButton1Clicked(goods_buy_btn,"购买");
            }
        });
        btn_util.morphToSquare(goods_buy_btn,0,"购买");
    }

    private int mMorphCounter1 = 1;
    private void onMorphButton1Clicked(final IndeterminateProgressButton btnMorph, String text_str) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            btn_util.morphToSquare(btnMorph, 500,text_str);
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            btn_util.simulateProgress1(btnMorph,text_str);
        }
    }

    private void initContent(final Goods goods) {
        //预约人的控件等dialog写好
        LinearLayout buy_booking_linear = (LinearLayout) this.findViewById(R.id.buy_booking_linear_id);
        booking_text = (TextView) this.findViewById(R.id.buy_booking_text_id);
        buy_booking_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出时间预约框
                new BuyBookingDialog(BuyActivity.this,goods.getGoodsBooking(), new BuyBookingDialog.onConfirmListener() {
                    @Override
                    public void clickconfirm(String text) {
                        booking_text.setText(text);
                    }
                }).show();
            }
        });
        LinearLayout buy_location_linear = (LinearLayout) this.findViewById(R.id.buy_location_linear_id);
        location_text = (TextView) this.findViewById(R.id.buy_location_text_id);
        buy_location_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出地址框
               new BuyLocationDialog(BuyActivity.this, new BuyLocationDialog.onConfirmListener() {
                   @Override
                   public void clickconfirm(String text) {
                       location_text.setText(text);
                   }
               }).show();
            }
        });

        ImageView goods_img = (ImageView) this.findViewById(R.id.buy_goods_img_id);
        Glide.with(this).load(goods.getGoodsurl())
                .placeholder(R.drawable.aliwx_default_photo)
                .error(R.mipmap.aliwx_image_download_fail_view)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(300,300).centerCrop().into(goods_img);
        TextView goods_name = (TextView) this.findViewById(R.id.buy_goods_name_text_id);
        goods_name.setText(goods.getGoodsName());
        TextView goods_prive = (TextView) this.findViewById(R.id.buy_goods_price_id);
        goods_prive.setText(goods.getGoodsDiscount());
        TextView goods_craftsmen = (TextView) this.findViewById(R.id.buy_craftsmen_name_text_id);
        goods_craftsmen.setText(goods.getGoodsCraftsmen().getCraftsmenName());
    }

    private void initToolbar(String goodsName) {
        Toolbar buy_toolebar = (Toolbar) this.findViewById(R.id.back_toolbar_id);
        ViewUtil.initAfterSetContentView(this,buy_toolebar);
        TextView title = (TextView) this.findViewById(R.id.toolbar_back_title_id);
        title.setText(goodsName);
        Button back = (Button) this.findViewById(R.id.toolbar_back_btn_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
