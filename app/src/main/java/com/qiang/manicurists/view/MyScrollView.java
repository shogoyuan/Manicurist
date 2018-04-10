package com.qiang.manicurists.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/20 15:53
 * 修改备注：
 */
public class MyScrollView extends ScrollView {
    private onScrollViewListener scrollviewlistener = null;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        //x为当前滑动条的横坐标，y表示当前滑动条的纵坐标，oldx为前一次滑动的横坐标，oldy表示前一次滑动的纵坐标
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollviewlistener != null) {
            scrollviewlistener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    //接口
    public void setScrollviewlistener(onScrollViewListener listener) {
        this.scrollviewlistener = listener;
    }

    public interface onScrollViewListener {
        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
