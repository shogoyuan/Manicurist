package com.qiang.manicurists.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.qiang.manicurists.R;
import com.qiang.manicurists.view.morphingbutton.IndeterminateProgressButton;
import com.qiang.manicurists.view.morphingbutton.MorphingButton;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BtnUtil {
    private Context mcontext;
    public BtnUtil(Context context){
        mcontext = context;
    }
    private int dimen(@DimenRes int resId) {
        return (int) mcontext.getResources().getDimension(resId);
    }
    private int color(@ColorRes int resId) {
        return mcontext.getResources().getColor(resId);
    }
    private int integer(@IntegerRes int resId) {
        return mcontext.getResources().getInteger(resId);
    }
    private String str(@StringRes int resId) {
        return mcontext.getResources().getString(resId);
    }

    public void morphToSquare(final IndeterminateProgressButton btnMorph, int duration ,String text_str) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_100))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_blue))
                .colorPressed(color(R.color.mb_blue_dark))
                .text(text_str);
        btnMorph.morph(square);
    }

    public void morphToSuccess(final IndeterminateProgressButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_green))
                .colorPressed(color(R.color.mb_green_dark))
                .icon(R.mipmap.ic_done);
        btnMorph.morph(circle);
    }

    public void morphToFailure(final IndeterminateProgressButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_height_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(color(R.color.mb_red))
                .colorPressed(color(R.color.mb_red_dark))
                .icon(R.mipmap.ic_lock);
        btnMorph.morph(circle);
    }

    public void simulateProgress2(@NonNull final IndeterminateProgressButton button, final String text_str) {
        int progressColor = color(R.color.mb_blue);
        int color = color(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_200);
        int height = dimen(R.dimen.mb_height_8);
        int duration = integer(R.integer.mb_animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                morphToSquare(button, integer(R.integer.mb_animation),text_str);
                button.unblockTouch();
            }
        }, 4000);

        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor);
    }

    public void simulateProgress1(@NonNull final IndeterminateProgressButton button, final String text_str) {
        int progressColor1 = color(R.color.holo_blue_bright);
        int progressColor2 = color(R.color.holo_green_light);
        int progressColor3 = color(R.color.holo_orange_light);
        int progressColor4 = color(R.color.holo_red_light);
        int color = color(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_200);
        int height = dimen(R.dimen.mb_height_8);
        int duration = integer(R.integer.mb_animation);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                morphToSuccess(button);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        morphToSquare(button, integer(R.integer.mb_animation),text_str);
                        button.unblockTouch();
                    }
                },2000);
            }
        }, 4000);

        button.blockTouch(); // prevent user from clicking while button is in progress
        button.morphToProgress(color, progressCornerRadius, width, height, duration, progressColor1, progressColor2,
                progressColor3, progressColor4);
    }
}
