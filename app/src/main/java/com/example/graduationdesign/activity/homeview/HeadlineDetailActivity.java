package com.example.graduationdesign.activity.homeview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduationdesign.R;

import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.bean.HomeBean;

public class HeadlineDetailActivity extends BaseActivity {
    //文章标题，发布时间，阅读时间
    private TextView title,time,read;
    //文章自然段
    private TextView p1,p2,p3,p4,p5,p6;
    //文字配图
    private ImageView background;

    //详细页标识符
    private String Flag;
    private HomeBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline_detail);
        Flag = getIntent().getStringExtra("Flag");
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        SetText();
    }

    public static void actionStart(Context context,HomeBean bean,String Flag){
        //跳转到功能模块详情界面
        Intent intent = new Intent(context,HeadlineDetailActivity.class);
        intent.putExtra("homeBean",bean);
        intent.putExtra("Flag",Flag);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.headline_detail_title);
        //设置对应页的名称
        titleLayout.setText(Flag);

        title=(TextView) findViewById(R.id.title);
        time=(TextView) findViewById(R.id.time);
        read=(TextView) findViewById(R.id.read);
        p1=(TextView) findViewById(R.id.p1);
        p2=(TextView) findViewById(R.id.p2);
        p3=(TextView) findViewById(R.id.p3);
        p4=(TextView) findViewById(R.id.p4);
        p5=(TextView) findViewById(R.id.p5);
        p6=(TextView) findViewById(R.id.p6);
        background=(ImageView) findViewById(R.id.background);
    }


    private void SetText() {
        bean = getIntent().getParcelableExtra("homeBean");
        //设置对应的文本信息
        title.setText(bean.getTitle());
        time.setText(bean.getTime());
        read.setText(bean.getRead());
        p1.setText(bean.getP1());
        p2.setText(bean.getP2());
        p3.setText(bean.getP3());
        p4.setText(bean.getP4());
        p5.setText(bean.getP5());
        p6.setText(bean.getP6());

        //根据给定的资源名称动态地获取相应的资源ID
        int resourceId = getResources().getIdentifier(bean.getBackground(), "drawable", getPackageName());
        if(resourceId!=0){
            background.setVisibility(View.VISIBLE);
            background.setBackgroundResource(resourceId);
        }

    }
}
