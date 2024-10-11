package com.example.graduationdesign.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.graduationdesign.MainActivity;
import com.example.graduationdesign.R;
import com.example.graduationdesign.utils.AnalysisUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

    }

    private void init() {
        //利用timer让此界面延迟3秒后跳转，timer有一个线程，这个线程不断执行task
        Timer timer = new Timer();
        //timertask实现runnable接口，TimeTask类表示在一个指定时间内执行的task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                /*//loginInfo表示文件名
                SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
                boolean isLogin = sp.getBoolean("isLogin", false);*/
                if (AnalysisUtils.readLoginStatus(SplashActivity.this)) {
                    // 用户已经登录，跳转到主页
                    MainActivity.actionStart(SplashActivity.this);
                    SplashActivity.this.finish();
                }else {
                    // 用户未登录，跳转到登录页
                    LoginActivity.actionStart(SplashActivity.this);
                    SplashActivity.this.finish();
                }
            }
        };
        timer.schedule(task,3000);//设置这个task在延迟三秒后自动执行
    }

}
