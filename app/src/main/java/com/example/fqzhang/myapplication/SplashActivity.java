package com.example.fqzhang.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.InjectPrint;
import com.example.Seriable;

import butterknife.OnClick;
@Seriable
@InjectPrint("SplashActivity")
public class SplashActivity extends AppCompatActivity {
    @InjectPrint("oncreate")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        final boolean isEnter = getSharedPreferences("sp", Context.MODE_APPEND).getBoolean("firstEnter",false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEnter) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this,WelcomeGuideActivity.class));
                }
                finish();
            }
        },2000);

    }
}
