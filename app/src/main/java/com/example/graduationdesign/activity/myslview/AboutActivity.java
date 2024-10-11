package com.example.graduationdesign.activity.myslview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.about_title);
        titleLayout.setText("关于赣粮");
    }
}
