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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.adapter.BuyBookingGridviewAdapter;
import com.qiang.manicurists.util.BaseUtil;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/6 17:01
 * 修改备注：
 */
public class BuyBookingDialog extends Dialog{
    private Context mcontext;
    private View localView;
    private ArrayList<ArrayList<Integer>> goodsBooking;

    private RadioGroup radiogroup;
    private GridView gridview;
    private BuyBookingGridviewAdapter adapter;

    private int choose_group_position;

    public BuyBookingDialog(Context context,ArrayList<ArrayList<Integer>> goodsBooking,onConfirmListener listener) {
        super(context);
        mcontext = context;
        confirmlistener = listener;
        this.goodsBooking = goodsBooking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        localView = inflater.inflate(R.layout.dialog_booking_choose, null);
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
        radiogroup = (RadioGroup) localView.findViewById(R.id.dialog_booking_radiogroup_id);
        for (int i = 0 ; i < goodsBooking.size();i++){
            View radio = LayoutInflater.from(mcontext).inflate(R.layout.layout_neworhot_radiogroup_item,null);
            ((RadioButton) radio).setText(BaseUtil.getSpecifiedDayAfter(i));
            radiogroup.addView(radio, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        gridview = (GridView) localView.findViewById(R.id.dialog_booking_gridview_id);
        adapter = new BuyBookingGridviewAdapter(mcontext,goodsBooking.get(0),BaseUtil.getSpecifiedDayAfter(0));
        gridview.setAdapter(adapter);

        ((RadioButton)radiogroup.getChildAt(0)).setChecked(true);

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

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<goodsBooking.size();i++){
                    RadioButton radiobutton = (RadioButton) radiogroup.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        choose_group_position = i;
                        String choose_location = radiobutton.getText().toString();
                        adapter.setData(goodsBooking.get(i),choose_location);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (goodsBooking.get(choose_group_position).get(position) == 1){
                    BaseUtil.ShowToast(mcontext,"我被人约了");
                }else{
                    confirmlistener.clickconfirm((String) parent.getAdapter().getItem(position));
                    cancel();
                }
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private onConfirmListener confirmlistener;
    public interface onConfirmListener{
        void clickconfirm(String text);
    }
}
