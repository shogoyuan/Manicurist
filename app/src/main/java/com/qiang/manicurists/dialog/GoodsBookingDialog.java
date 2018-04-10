package com.qiang.manicurists.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.bean.Goods;
import com.qiang.manicurists.view.MyBookingView;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/6 9:45
 * 修改备注：
 */
public class GoodsBookingDialog extends Dialog {
    private Context mcontext;
    private View localView;
    private Goods goods;
    public GoodsBookingDialog(Context context,Goods goods) {
        super(context);
        mcontext = context;
        this.goods = goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        localView = inflater.inflate(R.layout.dialog_booking, null);
        localView.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.anim_dialog_booking_style));
        setContentView(localView);
        // 这句话起全屏的作用
        Window windows = getWindow();
        windows.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        windows.setGravity(Gravity.BOTTOM);//全屏显示就不需要放置最底
//        setCancelable(true);
//        setCanceledOnTouchOutside(true);//全屏显示也不需要点击外围消失
        initView();
        initListener();
    }


    private void initView() {
        int width = LaunchActivity.windows_width;

        MyBookingView goods_booking_tag1 = (MyBookingView) localView.findViewById(R.id.dialog_booking_tag1_id);
        goods_booking_tag1.setIsSingle(true);
        goods_booking_tag1.setIsOk(true);
        goods_booking_tag1.setWidth(width);

        MyBookingView goods_booking_tag2 = (MyBookingView) localView.findViewById(R.id.dialog_booking_tag2_id);
        goods_booking_tag2.setIsSingle(true);
        goods_booking_tag2.setIsOk(false);
        goods_booking_tag2.setWidth(width);

        MyBookingView goods_booking_time = (MyBookingView) localView.findViewById(R.id.dialog_booking_time_id);
        goods_booking_time.setType(MyBookingView.TYPE_TIME);
        goods_booking_time.setWidth(width);

        MyBookingView goods_booking = (MyBookingView) localView.findViewById(R.id.dialog_booking_id);
        goods_booking.setType(MyBookingView.TYPE_BOOKING);
        goods_booking.setWidth(width);
        goods_booking.setIsGoodsView(false);

        goods_booking.setList(goods.getGoodsBooking());

        LinearLayout showlinear = (LinearLayout) localView.findViewById(R.id.dialog_show_linear);
        ViewGroup.LayoutParams params = showlinear.getLayoutParams();
        params.height = LaunchActivity.windows_height/2;
        showlinear.setLayoutParams(params);

    }

    private void initListener() {
        RelativeLayout cancellinear = (RelativeLayout) localView.findViewById(R.id.cancenlinear);
        cancellinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
