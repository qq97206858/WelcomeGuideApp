package com.example.fqzhang.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private Button btnEnter;
    private ImageView[] dots;
    private int currentIndex = 0 ;
    private LinearLayout ll_dots;
    private List<View> views;
    private Button startBtn;
    private static final int[] pics = {
            R.layout.guid_view1,
            R.layout.guid_view2,
            R.layout.guid_view3,
            R.layout.guid_view4
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("sp", Context.MODE_APPEND);
        sp.edit().putBoolean("firstEnter",true).commit();

    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        btnEnter = (Button) findViewById(R.id.btn_enter);
        ll_dots = (LinearLayout) findViewById(R.id.ll);
        initPageView();
        initDots();
        setListener();
        mViewPager.setAdapter(new GuideViewPageAdapter(views));
    }

    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPageView() {
        views = new ArrayList<>();
        for (int i = 0;i<pics.length;i++){
            View inflate = View.inflate(this, pics[i], null);
            if (i == pics.length-1) {
                startBtn = (Button) inflate.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }
            views.add(inflate);
        }
    }

    private void initDots() {
        dots = new ImageView[pics.length];
        for (int i =0 ;i<pics.length;i++) {
            dots[i] = (ImageView) ll_dots.getChildAt(i);
            dots[i].setEnabled(false);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }
        dots[currentIndex].setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterSplashActivity();
        } else {
            int position  = (int) v.getTag();
            setCurDot(position);
            setCurView(position);
        }
    }

    private void enterSplashActivity() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void setCurView(int position) {
        if (position<0 || position>dots.length || currentIndex == position) {
            return ;
        }
        mViewPager.setCurrentItem(position);
    }

    private void setCurDot(int position) {
        if (position<0 || position>dots.length || currentIndex == position) {
            return ;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    class GuideViewPageAdapter extends PagerAdapter {
        List<View> views;
        public GuideViewPageAdapter (List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position),0);
            return views.get(position);
        }
    }
}
