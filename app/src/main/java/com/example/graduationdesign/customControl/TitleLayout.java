package com.example.graduationdesign.customControl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.graduationdesign.R;

//自定义控件标题
public class TitleLayout extends LinearLayout {
    //标题,返回按钮
    private TextView tv_main_title,tv_back;
    private RelativeLayout rl_title_bar;

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_title_bar,this);
        tv_main_title=(TextView) findViewById(R.id.tv_main_title);
        tv_back=(TextView) findViewById(R.id.tv_back);
        rl_title_bar=(RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#33ff00"));

        tv_main_title.setText("标题");
        //后退键的点击事件
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    //设置标题栏文本
    public TitleLayout setText(String title){
        if(!TextUtils.isEmpty(title)){
            //设置标题栏文本
            tv_main_title.setText(title);
            tv_main_title.setVisibility(View.VISIBLE);
        }else {
            tv_main_title.setVisibility(View.GONE);
        }
        return this;
    }

    //将标题栏设置为透明
    public TitleLayout setColorTransparent(){
        rl_title_bar.setBackgroundColor(Color.parseColor("#00000000"));
        return this;
    }

    //隐藏返回键
    public TitleLayout setBackGone(){
        tv_back.setVisibility(GONE);
        return this;
    }

}
