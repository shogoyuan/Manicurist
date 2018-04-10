package com.qiang.manicurists.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qiang.manicurists.util.BaseUtil;

/**
 * 支持上拉加载更多的
 *
 * @author 肖肖
 * @version $$Id: LoadMoreRecyclerView.java, v 0.1 11/17/15 10:07 alicx Exp $$
 */
public class LoadMoreRecyclerView extends RecyclerView {

    private boolean mIsFooterEnable = false;//是否允许加载更多
    private boolean mCanScroller = true;//是否允许滑动

    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore;
    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private LoadMoreListener mListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private int mLastDy = 0;
    private int mTotalDy = 0;
    private View mTargetView = null;
    private ObjectAnimator mAnimator = null;
    private boolean isAlreadyHide = false, isAlreadyShow = false;

    public void setTopheadView(View v) {
        if (v == null) {
            throw new IllegalArgumentException("target shouldn't be null");
        }
        mTargetView = v;
    }


    /**
     * 初始化-添加滚动监听
     * <p/>
     * 回调加载更多方法，前提是
     * <pre>
     *    1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     *    2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     * </pre>
     */
    private void init() {
        super.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mTargetView !=null ) {
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            final float transY = mTargetView.getTranslationY();
                            int distance = -mTargetView.getBottom();

                            if (transY == 0 || transY == distance) {
                                return;
                            }
                            if (mLastDy > 0) {//向上滑动
                                mAnimator = animateShow(mTargetView);
                            }
//                            else {//向下滑动
//                                mAnimator = animateHide(mTargetView);
//                            }
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            if (mAnimator != null && mAnimator.isRunning()) {
                                mAnimator.cancel();
                            }
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            break;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //加载更多的判断
                if (null != mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == getAdapter().getItemCount()) {
                        setLoadingMore(true);
                        mListener.onLoadMore();
                    }
                }
                //加载更多结束
                if (mTargetView !=null ) {
                    mTotalDy -= dy;
                    mLastDy = dy;
                    final float transY = mTargetView.getTranslationY();
                    float newTransY;
                    int distance = -mTargetView.getBottom();

//                    if (!recyclerView.canScrollVertically(-1)) {
////                        BaseUtil.ShowLog("321","到顶部");
//                        mAnimator = animateHide(mTargetView);
//                    }

                    if (mTotalDy >= distance && dy > 0) {
                        return;
                    }

                    if (isAlreadyHide && dy > 0) {
                        return;
                    }

                    if (isAlreadyShow && dy < 0) {
                        return;
                    }

                    newTransY = transY - dy;
                    if (newTransY < distance) {
                        newTransY = distance;
                    } else if (newTransY == distance) {
                        return;
                    } else if (newTransY > 0) {
                        newTransY = 0;
                    } else if (newTransY == 0) {
                        return;
                    }

                    mTargetView.setTranslationY(newTransY);
                    isAlreadyHide = newTransY == distance;
                    isAlreadyShow = newTransY == 0;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!mCanScroller) {
            return true;
        }
        else {
            return super.onTouchEvent(e);
        }
    }

    public void setCanScroller(boolean canScroller){
        mCanScroller = canScroller;
    }

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }


    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    private ObjectAnimator animateHide(View targetView) {
        int distance = -targetView.getBottom();
        return animationFromTo(targetView, targetView.getTranslationY(), distance);
    }

    private ObjectAnimator animateShow(View targetView) {
        return animationFromTo(targetView, targetView.getTranslationY(), 0);
    }

    private ObjectAnimator animationFromTo(View view, float start, float end) {
        String propertyName = "translationY";
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, propertyName, start, end);
        animator.start();
        return animator;
    }
}