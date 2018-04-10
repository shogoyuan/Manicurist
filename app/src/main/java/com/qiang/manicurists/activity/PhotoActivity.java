package com.qiang.manicurists.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qiang.manicurists.R;
import com.qiang.manicurists.adapter.PhotoViewpagerAdapter;
import com.qiang.manicurists.bean.Rated_Url;
import com.qiang.manicurists.util.ViewUtil;
import com.qiang.manicurists.view.MyViewPager;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {

    private CheckBox last_Btn;
    private MyViewPager photo_viewpager;
    private ArrayList<Rated_Url> url_list;
    private int position;
    private int left,top,width,height;

    private float mWidthScale,mHeightScale,mLeftDelta,mTopDelta;
    private int ANIM_DURATION = 500;
    private ColorDrawable colorDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ViewUtil.initAfterSetContentView(this,null);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        url_list = (ArrayList<Rated_Url>) bundle.getSerializable("rated_url_list");
        left = bundle.getInt("left");
        top = bundle.getInt("top");
        width = bundle.getInt("width");
        height = bundle.getInt("height");

        RelativeLayout root_rel = (RelativeLayout) this.findViewById(R.id.photo_root_rel_id);
        colorDrawable = new ColorDrawable(Color.BLACK);
        root_rel.setBackgroundDrawable(colorDrawable);

        initViewpager();

        if (savedInstanceState == null) {
            ViewTreeObserver observer = photo_viewpager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    photo_viewpager.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    photo_viewpager.getLocationOnScreen(screenLocation);
                    mLeftDelta = left - screenLocation[0];
                    mTopDelta = top - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) width / photo_viewpager.getWidth();
                    mHeightScale = (float) height / photo_viewpager.getHeight();

                    enterAnimation();
                    return true;
                }
            });
        }
    }



    private void initViewpager() {
        photo_viewpager = (MyViewPager) this.findViewById(R.id.photo_viewpager_id);
        PhotoViewpagerAdapter adapter = new PhotoViewpagerAdapter(this, url_list, new PhotoViewpagerAdapter.onPicClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        photo_viewpager.setAdapter(adapter);

        final LinearLayout photo_viewpager_linear = (LinearLayout) this.findViewById(R.id.photo_viewpager_cyclelinear_id);
        for (int i = 0; i < url_list.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_home_viewpager_cycle_item,null);
            if (i == 0) {
                last_Btn = (CheckBox) view;
                last_Btn.setChecked(true);
            }
            photo_viewpager_linear.addView(view,10,10);
            //要先addview后才能获取到paeams
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(10,0,0,0);
            view.setLayoutParams(params);
        }
        photo_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (last_Btn != null) last_Btn.setChecked(false);
                CheckBox current_btn = (CheckBox) photo_viewpager_linear.getChildAt(position);
                current_btn.setChecked(true);
                last_Btn = current_btn;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        photo_viewpager.setCurrentItem(position);
    }

    public void enterAnimation() {
        photo_viewpager.setPivotX(0);
        photo_viewpager.setPivotY(0);
        photo_viewpager.setScaleX(mWidthScale);
        photo_viewpager.setScaleY(mHeightScale);
        photo_viewpager.setTranslationX(mLeftDelta);
        photo_viewpager.setTranslationY(mTopDelta);

        // interpolator where the rate of change starts out quickly and then decelerates.
        TimeInterpolator sDecelerator = new DecelerateInterpolator();

        // Animate scale and translation to go from thumbnail to full size
        photo_viewpager.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
                translationX(0).translationY(0).setInterpolator(sDecelerator);

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    public void exitAnimation() {
        TimeInterpolator sInterpolator = new AccelerateInterpolator();
        photo_viewpager.animate().setDuration(ANIM_DURATION).scaleX(mWidthScale).scaleY(mHeightScale).
                translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(sInterpolator).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(R.anim.anim_activity_push_in,R.anim.anim_activity_push_out);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
        bgAnim.setDuration(ANIM_DURATION);
        bgAnim.start();
    }

    @Override
    public void onBackPressed() {
        exitAnimation();
    }
}
