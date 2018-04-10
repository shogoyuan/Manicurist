package com.qiang.manicurists.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.qiang.manicurists.util.BaseUtil;

/**
 * scrollview滑动时对顶部图片进行拉伸
 *
 * @author HeLin
 *
 * 2015-7-10
 */

public class DampScrollViewView extends ScrollView {
    public final String TAG = "DampScrollViewView";
    private onScrollViewListener scrollviewlistener = null;

    private static final int LEN = 0xc8;
    private static final int DURATION = 500;
    private static final int MAX_DY = 200;
    private Scroller mScroller;
    TouchTool tool;
    int left, top;
    float startX, startY, currentX, currentY;
    int imageViewH;
    int rootW, rootH;
    ImageView imageView;
    boolean scrollerType;

    public DampScrollViewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public DampScrollViewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public DampScrollViewView(Context context) {
        super(context);

    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (!mScroller.isFinished()) {
//            BaseUtil.ShowLog(TAG,!mScroller.isFinished()+"");
            return super.onTouchEvent(event);
        }
        currentX = event.getX();
        currentY = event.getY();
        imageView.getTop();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                left = imageView.getLeft();
                top = imageView.getBottom();
                rootW = getWidth();
                rootH = getHeight();
                imageViewH = imageView.getHeight();
                startX = currentX;
                startY = currentY;
                tool = new TouchTool(imageView.getLeft(), imageView.getBottom(),
                        imageView.getLeft(), imageView.getBottom() + LEN);
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollY=getScrollY();
                if (imageView.isShown() && imageView.getTop() >= 0&&scrollY==0) {
                    if (tool != null) {
                        int t = tool.getScrollY(currentY - startY);
                        if (t >= top && t <= imageView.getBottom() + LEN) {
                            android.view.ViewGroup.LayoutParams params = imageView
                                    .getLayoutParams();
                            params.height = t;
                            imageView.setLayoutParams(params);
                        }
                    }
                    scrollerType = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                scrollerType = true;
                mScroller.startScroll(imageView.getLeft(), imageView.getBottom(),
                        0 - imageView.getLeft(),
                        imageViewH - imageView.getBottom(), DURATION);
                invalidate();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            imageView.layout(0, 0, x + imageView.getWidth(), y);
            invalidate();
            if (!mScroller.isFinished() && scrollerType && y > MAX_DY) {
                android.view.ViewGroup.LayoutParams params = imageView
                        .getLayoutParams();
                params.height = y;
                imageView.setLayoutParams(params);
            }
        }
    }

    public class TouchTool {

        private int startX, startY;

        public TouchTool(int startX, int startY, int endX, int endY) {
            super();
            this.startX = startX;
            this.startY = startY;
        }

        public int getScrollX(float dx) {
            int xx = (int) (startX + dx / 2.5F);
            return xx;
        }

        public int getScrollY(float dy) {
            int yy = (int) (startY + dy / 2.5F);
            return yy;
        }
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
        void onScrollChanged(DampScrollViewView scrollView, int x, int y, int oldx, int oldy);
    }
}
