package com.qiang.manicurists.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiang.manicurists.util.BaseUtil;

import java.util.ArrayList;

/**
 * 项目名称：Manicurists
 * 类描述：
 * 创建人：Qiang
 * 创建时间：2016/6/29 14:11
 * 修改备注：
 */
public class MyBookingView extends TextView {
    //话哪种线的标记
    public final static int TYPE_TIME = 0;
    public final static int TYPE_BOOKING = 1;

    //时间线的高度
    private float TIME_HEIGHT = 50;
    //文字距离顶部的高度
    private float MARGIN_TEXT = TIME_HEIGHT / 2;
    //文字与时间线之间的高度
    private float MARGIN_TEXTANDLINE = TIME_HEIGHT / 6;
    //文字的大小
    private float TEXT_SIZE = 30;
    //预约条之间的间隔
    private final float MARGIN_BOOKING = 10;
    //有多少个时区
    private final float count = 16;//16个小时

    private float mType = 0;
    private float mWidth = 20;
    //是否话单个
    private boolean isSingle = false;
    private boolean isOK = true;

    //是不是商品界面
    private boolean isGoodsView = true;

    private ArrayList<ArrayList<Integer>> mList = new ArrayList<ArrayList<Integer>>();

    public MyBookingView(Context context) {
        super(context);
    }

    public MyBookingView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public void setIsSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public void setIsOk(boolean isOk) {
        this.isOK = isOk;
    }

    public void setIsGoodsView(boolean isGoodsView) {
        this.isGoodsView = isGoodsView;
    }

    public void setList(ArrayList<ArrayList<Integer>> list) {
        mList = list;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //0是画时间标度，1是画预约时间
        float item_marginleft = mWidth / 5;//先空出左右的间距
        float item_width = item_marginleft * 3 / count;//屏幕宽度的5分3再处于时间的个数
        //根据屏幕宽度重新设定基础参数
        TEXT_SIZE = (int) (item_width / 3) * 2;
        TIME_HEIGHT = (int) item_width * 2;
        MARGIN_TEXT = TIME_HEIGHT / 2;
        MARGIN_TEXTANDLINE = TIME_HEIGHT / 6;
        //
        if (isSingle) {
            Paint p = new Paint();
            if (isOK) {
                p.setColor(Color.GREEN);
                canvas.drawRect(0, 0, item_width, item_width, p);
            } else {
                p.setColor(Color.GRAY);
                canvas.drawRect(0, 0, item_width, item_width, p);
                p.setColor(Color.WHITE);
                canvas.drawLine(0,0,item_width,item_width, p);
                canvas.drawLine(0,item_width ,item_width,0, p);
            }
            setLayoutParams(new LinearLayout.LayoutParams((int) item_width, (int) item_width));
        } else {
            if (mType == TYPE_TIME) {
                float MARGIN_TIME = MARGIN_TEXT + MARGIN_TEXTANDLINE;//绘画竖线的最高点与顶部的距离
                float LINE_HEIGHT = TIME_HEIGHT / 3 + MARGIN_TIME;//竖线的最低点到顶部的距离
                Paint p = new Paint();
                p.setColor(Color.BLACK);
                p.setTextSize(item_width);
                p.setTextAlign(Paint.Align.LEFT);
                p.setAntiAlias(true);//抗锯齿
                canvas.drawText("可约时间", 0, MARGIN_TIME, p);
                p.setTextSize(TEXT_SIZE);
                for (int i = 0; i <= count; i++) {
                    //绘制竖线
                    if (i % 2 == 0 || i == count) {
                        if (i + 8 >= 10)
                            canvas.drawText(i + 8 + "", (item_marginleft + item_width * i) - p.getTextSize() / 2, MARGIN_TEXT, p);
                        else
                            canvas.drawText(i + 8 + "", (item_marginleft + item_width * i) - p.getTextSize() / 4, MARGIN_TEXT, p);
                        //偶数长线
                        canvas.drawLine(item_marginleft + item_width * i, MARGIN_TIME
                                , item_marginleft + item_width * i, LINE_HEIGHT, p);
                    } else {
                        //奇数短线
                        canvas.drawLine(item_marginleft + item_width * i, MARGIN_TIME + (LINE_HEIGHT-MARGIN_TIME)/2
                                , item_marginleft + item_width * i, LINE_HEIGHT, p);
                    }
                }
                //绘制直线
                canvas.drawLine(item_marginleft / 2, LINE_HEIGHT
                        , mWidth - item_marginleft / 2, LINE_HEIGHT, p);
                setLayoutParams(new LinearLayout.LayoutParams((int) mWidth, (int) (LINE_HEIGHT + MARGIN_TEXTANDLINE)));
            } else if (mType == TYPE_BOOKING) {
                int draw_count = isGoodsView ? 3 : mList.size();
                if (mList.size() == 0) return;
                else {
                    Paint p = new Paint();
                    p.setStyle(Paint.Style.FILL);
                    for (int i = 0; i < draw_count; i++) {
                        p.setColor(Color.BLACK);
                        p.setTextSize(item_width);
                        p.setTextAlign(Paint.Align.LEFT);
                        p.setAntiAlias(true);//抗锯齿
                        Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
                        float text_baseline = ((item_width * i + item_width + MARGIN_BOOKING * i) + (item_width * i + MARGIN_BOOKING * i
                        ) - fontMetrics.bottom - fontMetrics.top )/ 2;//文字的垂直对中矩形
                        switch (i) {
                            case 0:
                                canvas.drawText("今天", 0, text_baseline, p);
                                break;
                            case 1:
                                canvas.drawText("明天", 0, text_baseline, p);
                                break;
                            case 2:
                                canvas.drawText("后天", 0, text_baseline, p);
                                break;
                            default:
                                canvas.drawText(BaseUtil.getSpecifiedDayAfter(i + 1), 0, text_baseline, p);
                                break;
                        }
                        ArrayList<Integer> mList_one = mList.get(i);
                        for (int j = 0; j < mList_one.size(); j++) {
                            if (mList_one.get(j) == 0) {//0可约，1被约
                                p.setColor(Color.GREEN);
                            } else {
                                p.setColor(Color.GRAY);
                            }//x,y,x，y，，前两个是起原点，后两个是斜对角的点
                            canvas.drawRect(item_marginleft + item_width * j,
                                    item_width * i + MARGIN_BOOKING * i,
                                    item_marginleft + item_width * j + item_width,
                                    item_width * i + item_width + MARGIN_BOOKING * i, p);
                            if (mList_one.get(j) != 0) {
                                p.setColor(Color.WHITE);
                                canvas.drawLine(item_marginleft + item_width * j,
                                        item_width * i + MARGIN_BOOKING * i,
                                        item_marginleft + item_width * j + item_width,
                                        item_width * i + item_width + MARGIN_BOOKING * i, p);
                                canvas.drawLine(item_marginleft + item_width * j,
                                        item_width * i + MARGIN_BOOKING * i+ item_width,
                                        item_marginleft + item_width * j+ item_width,
                                        item_width * i + MARGIN_BOOKING * i, p);
                            }
                            }
                    }
                }
                setLayoutParams(new LinearLayout.LayoutParams((int) mWidth, (int) (item_width + MARGIN_BOOKING) * draw_count));
            }
        }
        canvas.save();
        canvas.restore();
    }

}
