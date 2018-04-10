package com.qiang.manicurists.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;
import com.qiang.manicurists.db.LocationDB;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/6 14:20
 * 修改备注：
 */
public class BuyLocationDialog extends Dialog {
    private Context mcontext;
    private View localView;

    private RadioGroup radiogroup;
    private ArrayList<String> location;
    private String choose_location;
    private LocationDB db;

    public BuyLocationDialog(Context context,onConfirmListener listener) {
        super(context);
        mcontext = context;
        confirmlistener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        localView = inflater.inflate(R.layout.dialog_location_choose, null);
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
        db = new LocationDB(mcontext);

        radiogroup = (RadioGroup) localView.findViewById(R.id.dialog_location_radiogroup_id);
        initradiogroup();


        LinearLayout showlinear = (LinearLayout) localView.findViewById(R.id.dialog_show_linear);
        ViewGroup.LayoutParams params = showlinear.getLayoutParams();
        params.height = LaunchActivity.windows_height/2;
        showlinear.setLayoutParams(params);

    }

    private void initradiogroup() {
        location = db.getData();
        if(radiogroup.getChildCount() > 0) radiogroup.removeAllViews();
        for (int i =0;i<location.size();i++){
            View radio = LayoutInflater.from(mcontext).inflate(R.layout.layout_buy_location_radiogroup_item,null);
            ((RadioButton) radio).setText(location.get(i));
            radiogroup.addView(radio, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        if(location.size()>0){
            ((RadioButton)radiogroup.getChildAt(0)).setChecked(true);
            choose_location = ((RadioButton)radiogroup.getChildAt(0)).getText().toString();
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

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<location.size();i++){
                    RadioButton radiobutton = (RadioButton) radiogroup.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        choose_location = radiobutton.getText().toString();
                        break;
                    }
                }
            }
        });

        Button delete_btn = (Button) localView.findViewById(R.id.dialog_location_delete_btn_id);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mcontext).setTitle("问一下您").setMessage("是不是要删去 "+choose_location+" 这个地址？")
                .setNegativeButton("等一下", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("删吧删吧", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(choose_location);
                        initradiogroup();
                    }
                }).show();
            }
        });

        Button confirm = (Button) localView.findViewById(R.id.dialog_location_confirm_btn_id);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                confirmlistener.clickconfirm(choose_location);
            }
        });

        Button insert = (Button) localView.findViewById(R.id.dialog_location_insert_btn_id);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(mcontext);
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setTitle("输入地址").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                       db.save(inputServer.getText().toString());
                        initradiogroup();
                    }
                });
                builder.show();
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