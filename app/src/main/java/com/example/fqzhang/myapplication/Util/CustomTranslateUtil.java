package com.example.fqzhang.myapplication.Util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by fqzhang on 2017/8/21.
 */

public class CustomTranslateUtil {
    public static final int TOP_TO_DOWN = 1;
    public static final int DOWN_TO_TOP = 2;
    public static final int LEFT_TO_RIGHT = 3;
    public static final int RIGHT_TO_LEFT = 4;

    private static CustomTranslateUtil sInstance = new CustomTranslateUtil();

    public static CustomTranslateUtil getInstance() {
        if (sInstance == null) {
            sInstance = new CustomTranslateUtil();
        }

        return sInstance;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();

        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;

        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void showView(View view, int direction, int duration) {
        view.setVisibility(View.VISIBLE);

        if (view.getMeasuredHeight() == 0) {
            measureView(view);
        }

        TranslateAnimation ani = getTranslateAnimation(view, direction, true);

        ani.setDuration(duration);
        view.startAnimation(ani);
    }

    private TranslateAnimation getTranslateAnimation(View view, int direction, boolean isShow) {
        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, 0);

        if (isShow) {
            if (direction == TOP_TO_DOWN) {
                ani = new TranslateAnimation(0, 0, -view.getMeasuredHeight(), 0);
            } else if (direction == DOWN_TO_TOP) {
                ani = new TranslateAnimation(0, 0, view.getMeasuredHeight(), 0);
            } else if (direction == RIGHT_TO_LEFT) {
                ani = new TranslateAnimation(view.getMeasuredWidth(), 0, 0, 0);
            } else if (direction == LEFT_TO_RIGHT) {
                ani = new TranslateAnimation(-view.getMeasuredWidth(), 0, 0, 0);
            }
        } else {
            if (direction == TOP_TO_DOWN) {
                ani = new TranslateAnimation(0, 0, 0, view.getMeasuredHeight());
            } else if (direction == DOWN_TO_TOP) {
                ani = new TranslateAnimation(0, 0, 0, -view.getMeasuredHeight());
            } else if (direction == RIGHT_TO_LEFT) {
                ani = new TranslateAnimation(0, -view.getMeasuredWidth(), 0, 0);
            } else if (direction == LEFT_TO_RIGHT) {
                ani = new TranslateAnimation(0, view.getMeasuredWidth(), 0, 0);
            }
        }

        ani.setInterpolator(new DecelerateInterpolator());

        return ani;
    }

    private TranslateAnimation getTranslateAnimation(View view, int direction, boolean isShow, float distance) {
        TranslateAnimation ani = new TranslateAnimation(0, 0, 0, 0);

        if (isShow) {
            if (direction == TOP_TO_DOWN) {
                ani = new TranslateAnimation(0, 0, -distance, 0);
            } else if (direction == DOWN_TO_TOP) {
                ani = new TranslateAnimation(0, 0, distance, 0);
            } else if (direction == RIGHT_TO_LEFT) {
                ani = new TranslateAnimation(distance, 0, 0, 0);
            } else if (direction == LEFT_TO_RIGHT) {
                ani = new TranslateAnimation(-distance, 0, 0, 0);
            }
        } else {
            if (direction == TOP_TO_DOWN) {
                ani = new TranslateAnimation(0, 0, 0, distance);
            } else if (direction == DOWN_TO_TOP) {
                ani = new TranslateAnimation(0, 0, 0, -distance);
            } else if (direction == RIGHT_TO_LEFT) {
                ani = new TranslateAnimation(0, -distance, 0, 0);
            } else if (direction == LEFT_TO_RIGHT) {
                ani = new TranslateAnimation(0, distance, 0, 0);
            }
        }

        ani.setInterpolator(new DecelerateInterpolator());

        return ani;
    }

    public void showViewWithAniListener(View view, int direction, int duration, Animation.AnimationListener listener) {
        view.setVisibility(View.VISIBLE);

        if (view.getMeasuredHeight() == 0) {
            measureView(view);
        }

        TranslateAnimation ani = getTranslateAnimation(view, direction, true);

        ani.setDuration(duration);
        ani.setAnimationListener(listener);
        view.startAnimation(ani);
    }

    public void hideViewWithAniListener(final View view, int direction, int duration, final Animation.AnimationListener listener) {
        TranslateAnimation ani = getTranslateAnimation(view, direction, false);

        ani.setDuration(duration);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                listener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                listener.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                listener.onAnimationEnd(animation);
            }
        });

        view.startAnimation(ani);
    }

    public void hideView(final View view, int direction, int duration) {
        TranslateAnimation ani = getTranslateAnimation(view, direction, false);

        ani.setDuration(duration);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });

        view.startAnimation(ani);
    }

    public void hideViewWithAlpha(final View view, int direction, int duration, int distance, boolean fillAfter) {
        hideViewWithAlpha(view, direction, duration, distance, fillAfter, null);
    }

    public void hideViewWithAlpha(final View view, int direction, int duration, int distance, boolean fillAfter, Animation.AnimationListener listener) {
        TranslateAnimation ani = getTranslateAnimation(view, direction, false, distance);
        ani.setDuration(duration);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(duration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(ani);

        if (fillAfter) {
            animationSet.setFillAfter(true);
        }

        if (listener != null) {
            animationSet.setAnimationListener(listener);
        }

        if (view != null) {
            view.startAnimation(animationSet);
        }
    }

    public void showViewWithAlpha(final View view, int direction, int duration, int distance, boolean fillAfter) {
        showViewWithAlpha(view, direction, duration, distance, fillAfter, null);
    }

    public void showViewWithAlpha(final View view, int direction, int duration, int distance, boolean fillAfter, Animation.AnimationListener listener) {
        TranslateAnimation ani = getTranslateAnimation(view, direction, true, distance);
        ani.setDuration(duration);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(duration);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(ani);

        if (fillAfter) {
            animationSet.setFillAfter(true);
        }

        if (listener != null) {
            animationSet.setAnimationListener(listener);
        }

        if (view != null) {
            view.startAnimation(animationSet);
        }
    }
}
