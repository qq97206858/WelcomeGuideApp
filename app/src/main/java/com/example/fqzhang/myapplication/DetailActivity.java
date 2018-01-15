package com.example.fqzhang.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.Seriable;
import com.example.fqzhang.myapplication.fragment.DetailFragment;
import com.example.fqzhang.myapplication.fragment.DetailFragment2;
import com.example.fqzhang.myapplication.view.TextImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.id.list;
import static android.R.id.text1;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener,DetailFragment2.OnFragmentInteractionListener {
    static class T {
        public List<String> text = new ArrayList<>()/* Arrays.asList(new String[]{"dd","dd"})*/;
    }
    public static void main(String[] args) {
        T t = new T();
        t.text.add("dd");
        t.text.add("dd");
        method(t.text);
        System.out.println(t.text);
    }
    public static void method(List<String> text){
        text = Arrays.asList(new String[]{"xx","xx"});
        /*text.add("kk");
        text.add("kk");*/
    }
    @Seriable
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = getIntent().getExtras();
        int sourceType = args.getInt("sourceType");
        if (sourceType == 0) {
            fragment = DetailFragment.newInstance(args.getString("detail"),args.getInt("position")+"");
        } else if (sourceType == 1) {
            fragment = DetailFragment2.newInstance("","");
        }
        addFragment(fragmentManager,fragment ,android.R.id.content,"detailFragment");
    }


    @SuppressWarnings("ResourceType")
    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        try {
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
           // transaction.setCustomAnimations(R.anim.common_anim_fragment_in, R.anim.common_anim_fragment_out, R.anim.common_anim_fragment_close_in, R.anim.common_anim_fragment_close_out);
            Fragment fragment = supportFragmentManager.findFragmentById(content);
            if (fragment != null) {
                if (fragment instanceof FragmentManager.OnBackStackChangedListener) {
                    supportFragmentManager.addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) fragment);
                }
                transaction.hide(fragment);
            }
            transaction.add(content, baseDialogFragment, tag);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment != null ){
                if ("detailFragment".equals(fragment.getTag())){

                    ((DetailFragment)fragment).onBackKeyControl();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
