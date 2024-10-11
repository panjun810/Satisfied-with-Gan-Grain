package com.example.graduationdesign.activity.homeview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.adapter.HomeAdapter;
import com.example.graduationdesign.bean.HomeBean;
import com.example.graduationdesign.utils.AnalysisUtils;

import java.io.InputStream;
import java.util.List;

public class HeadlineActivity extends BaseActivity {
    private List<HomeBean> hbl;
    private HomeAdapter adapter;
    private ListView headline_list;

    //判断用户点击了功能模块
    String Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline);
        Flag = getIntent().getStringExtra("Flag");
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getHomeData();
        init();
    }

    public static void actionStartFlag(Context context,String Flag){
        Intent intent=new Intent(context, HeadlineActivity.class);
        intent.putExtra("Flag",Flag);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.headline_title);
        //设置对应页的名称
        titleLayout.setText(Flag);

        headline_list=(ListView)findViewById(R.id.headline_list);
        adapter = new HomeAdapter(this,hbl);
        //设置数据给适配器
        adapter.setData(hbl);
        adapter.setFlag(Flag);
        headline_list.setAdapter(adapter);

    }

    private void getHomeData(){
        try {
            InputStream is = null;
            //通过Flag解析对应的xml文件
            if(Flag.equals("粮食头条")){
                 is = this.getResources().getAssets().open("headline.xml");
            }else if(Flag.equals("质量标准")){
                 is = this.getResources().getAssets().open("zjbz.xml");
            }else if(Flag.equals("收购政策")){
                is = this.getResources().getAssets().open("sgzc.xml");
            }else if(Flag.equals("农业气象")){
                is = this.getResources().getAssets().open("nyqx.xml");
            }
            hbl= AnalysisUtils.getHomeInfos(is);

        }catch(Exception e){
            e.printStackTrace();

        }
    }
}
