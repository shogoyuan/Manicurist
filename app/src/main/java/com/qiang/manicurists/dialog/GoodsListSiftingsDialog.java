package com.qiang.manicurists.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.adapter.GoodListSiftingsListviewAdapter;
import com.qiang.manicurists.util.BaseUtil;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/8 15:58
 * 修改备注：
 */
public class GoodsListSiftingsDialog extends Dialog {
    private Context mcontext;
    private View localView;

    private int width = LaunchActivity.windows_width;;

    private RadioGroup radiogroup;
    private RadioButton radiobutton_price,radiobutton_time;
    private Drawable right_draw;
    private LinearLayout time_linear,booking_linear;
    private String[] msiftings_str;

    private ArrayList date_list,time_list;
    private int date_choose;
//    private HashMap<String,ArrayList<String>> date_time_list;

    private onConfirmListener mlistener;
    private WheelView date,time;
    private EditText leftedit,rightedit;
    private TextView datetext,timetext;

    public GoodsListSiftingsDialog(Context context,String[] siftings_str,onConfirmListener listener) {
        super(context);
        mcontext = context;
        msiftings_str = siftings_str;
        mlistener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        localView = inflater.inflate(R.layout.dialog_goodslist_siftings, null);
        localView.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.anim_dialog_siftings_style));
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
        radiogroup = (RadioGroup) localView.findViewById(R.id.dialog_goodslist_siftings_radiogroup_id);
        for (int i =0;i<msiftings_str.length;i++){
            View radio = LayoutInflater.from(mcontext).inflate(R.layout.layout_category_radiogroup_item,null);
            ((RadioButton) radio).setText(msiftings_str[i]);
            radiogroup.addView(radio, width/4, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        radiobutton_price = (RadioButton) radiogroup.getChildAt(0);
        radiobutton_time = (RadioButton) radiogroup.getChildAt(1);
        time_linear = (LinearLayout) localView.findViewById(R.id.dialog_goodslist_siftings_time_id);
        booking_linear = (LinearLayout) localView.findViewById(R.id.dialog_goodslist_siftings_booking_id);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<msiftings_str.length;i++){
                    RadioButton radiobutton = (RadioButton) radiogroup.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        String choose_text = radiobutton.getText().toString();
                        if (choose_text.equals(msiftings_str[0])){
                            time_linear.setVisibility(View.VISIBLE);
                            booking_linear.setVisibility(View.INVISIBLE);
                        }else if(choose_text.equals(msiftings_str[1])){
                            time_linear.setVisibility(View.INVISIBLE);
                            booking_linear.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
            }
        });
        //设置隐藏的时候不能用gone，因为可能导致绘画不完成不现实datepicker
        ((RadioButton) radiogroup.getChildAt(0)).setChecked(true);

        right_draw = mcontext.getResources().getDrawable(R.mipmap.image_loading_01);
        right_draw.setBounds(0, 0, right_draw.getMinimumWidth(), right_draw.getMinimumHeight()); //设置边界
        initPrice();
        initBooking();
        initConfirm();


        LinearLayout showlinear = (LinearLayout) localView.findViewById(R.id.dialog_show_linear);
        ViewGroup.LayoutParams params = showlinear.getLayoutParams();
        params.width = (width/5) *4;
        showlinear.setLayoutParams(params);
    }

    private void initConfirm() {
        Button confirm_btn = (Button) localView.findViewById(R.id.dialog_goodslist_siftings_confirm_btn_id);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftedit_text = leftedit.getText().toString();
                String rightedit_text = rightedit.getText().toString();
                String date_text= datetext.getText().toString();
                String time_text = timetext.getText().toString();
                mlistener.clickconfirm(leftedit_text.equals("")?0:Integer.parseInt(leftedit_text),
                        rightedit_text.equals("")?999:Integer.parseInt(rightedit_text),
                        date_text.equals("不限")?"":date_text,
                        time_text.equals("时间")?"":time_text);
                dismiss();
            }
        });
    }

    private void initPrice() {
        ListView time_list = (ListView) localView.findViewById(R.id.dialog_goodslist_siftings_time_listview_id);
        ArrayList<String> str_list = new ArrayList<String>();
        str_list.add("不限");
        for (int i = 0;i<5;i++) str_list.add(i*100+"----"+(i+1)*100);
        time_list.setAdapter(new GoodListSiftingsListviewAdapter(mcontext,str_list));
        leftedit = (EditText) localView.findViewById(R.id.dialog_goodslist_siftings_time_leftedit_id);
        rightedit = (EditText) localView.findViewById(R.id.dialog_goodslist_siftings_time_rightedit_id);
        time_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    radiobutton_price.setCompoundDrawables(null,null,right_draw,null);
                    leftedit.setText((position-1) * 100 + "");
                    rightedit.setText(position  * 100 + "");
                } else {
                    radiobutton_price.setCompoundDrawables(null,null,null,null);
                    leftedit.setText("");
                    rightedit.setText("");
                }
            }
        });
    }
    private void initBooking() {
        createDate();
        datetext = (TextView) localView.findViewById(R.id.dialog_goodslist_siftings_booking_datetext_id);
        timetext = (TextView) localView.findViewById(R.id.dialog_goodslist_siftings_booking_timetext_id);
        date = (WheelView) localView.findViewById(R.id.dialog_goodslist_siftings_booking_date_id);
        date.setWheelData(date_list);
        date.setWheelAdapter(new ArrayWheelAdapter(mcontext));
        date.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#0288ce");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        date.setStyle(style);
        date.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                time.resetDataFromTop(time_list);
                if(i == 0 ){
                    radiobutton_time.setCompoundDrawables(null,null,null,null);
                    datetext.setText("不限");
                    timetext.setText("时间");
                }else{
                    radiobutton_time.setCompoundDrawables(null,null,right_draw,null);
                    date_choose = i;
                    datetext.setText(date_list.get(i)+"");
                }
            }
        });

        time = (WheelView) localView.findViewById(R.id.dialog_goodslist_siftings_booking_time_id);
        time.setWheelData(time_list);
        time.setWheelAdapter(new ArrayWheelAdapter(mcontext));
        time.setSkin(WheelView.Skin.Holo);
        time.setStyle(style);
//        date.join(time);
//        date.joinDatas(date_time_list);
        time.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                if(date.getCurrentPosition() == 0 && time.getCurrentPosition() == 0){
                    radiobutton_time.setCompoundDrawables(null,null,null,null);
                    datetext.setText("不限");
                    timetext.setText("时间");
                }else {
                    radiobutton_time.setCompoundDrawables(null, null, right_draw, null);
                    datetext.setText(date_list.get(date_choose) + "");
                    timetext.setText(time_list.get(i) + "");
                }
            }
        });
    }

    private void createDate() {
        date_list = new ArrayList();
        time_list = new ArrayList();
        time_list = BaseUtil.getSpecifiedAllTime();
        for (int i = 0 ;i<30;i++){
            String date = BaseUtil.getSpecifiedDayAfter(i);
            date_list.add(date);
        }
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

    private onConfirmListener confirmlistener;
    public interface onConfirmListener{
        void clickconfirm(int small_price,int big_price,String date,String time);
    }
}
