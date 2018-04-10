package com.qiang.manicurists.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qiang.manicurists.R;
import com.qiang.manicurists.activity.LaunchActivity;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/7/8 14:26
 * 修改备注：
 */
public class GoodsListPopupWindows {
    private Context mcontext;
    private String[] morder_str;
    private View mtarget;
    private String mradio_text;

    private RadioGroup radiogroup;
    private Drawable right_draw;
    private PopupWindow popup;

    private popupCheckListener mlistener;
    //窗口，数组，显示目标，外围radio的文字，监听
    public GoodsListPopupWindows(Context context, String[] order_str, View target, String radio_text, popupCheckListener listener){
        mcontext = context;
        morder_str = order_str;
        mtarget =target;
        mradio_text = radio_text;
        mlistener = listener;
        initView();
    }

    private void initView() {
        right_draw = mcontext.getResources().getDrawable(R.mipmap.image_loading_01);
        right_draw.setBounds(0, 0, right_draw.getMinimumWidth(), right_draw.getMinimumHeight()); //设置边界
        View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_goodslist_popupwindows,null);
        radiogroup = (RadioGroup) view.findViewById(R.id.goodslist_headerview_popupwindows_radiogroup_id);
        for (int i = 0;i<morder_str.length;i++) {
            View radio = LayoutInflater.from(mcontext).inflate(R.layout.dialog_goodslist_popupwindows_item, null);
            ((RadioButton) radio).setText(morder_str[i]);
            radiogroup.addView(radio,LaunchActivity.windows_width,ViewGroup.LayoutParams.WRAP_CONTENT);
            if(mradio_text.equals(morder_str[i])) {
                ((RadioButton) radio).setChecked(true);
                ((RadioButton) radio).setCompoundDrawables(null, null, right_draw, null);//画在右边
            }
        }
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0 ;i<morder_str.length;i++){
                    RadioButton radiobutton = (RadioButton) radiogroup.getChildAt(i);
                    if(radiobutton.getId() == checkedId){
                        mlistener.popupCheck(radiobutton.getText().toString());
                        break;
                    }
                }
                popup.dismiss();
            }
        });
        popup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popup.setTouchable(true);
        popup.setOutsideTouchable(true);
        popup.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popup.setBackgroundDrawable(mcontext.getResources().getDrawable(
                R.drawable.snapshot_panel));
        popup.showAsDropDown(mtarget);
    }

    public interface popupCheckListener{
        void popupCheck(String text);
    }
}
