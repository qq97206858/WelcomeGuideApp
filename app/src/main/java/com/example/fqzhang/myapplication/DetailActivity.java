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
import android.view.Window;
import android.view.WindowManager;

import com.example.fqzhang.myapplication.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener {
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
        fragment = DetailFragment.newInstance(args.getString("detail"),args.getInt("position")+"");
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
