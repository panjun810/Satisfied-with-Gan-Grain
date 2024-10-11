package com.example.graduationdesign.activity.myslview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.adapter.GranaryCollectAdapter;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;
import com.example.graduationdesign.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class GranaryCollectActivity extends BaseActivity {
    private LinearLayout ll_collect_all;

    private TextView tv_none;
    private ListView granary_collect_list;
    private GranaryCollectAdapter adapter;
    private DBUtils db;
    private List<NavigationBean> nbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granary_collect);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = DBUtils.getInstance(this);
        nbl = new ArrayList<NavigationBean>();
        //从数据库中获取库点收藏信息
        nbl = db.getGranaryCollect(AnalysisUtils.readLoginUserName(this));

        init();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context, GranaryCollectActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.granary_collect_title);
        titleLayout.setText("库点收藏");

        ll_collect_all =(LinearLayout)findViewById(R.id.ll_collect_all);
        ll_collect_all.setVisibility(View.VISIBLE);

        ll_collect_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 更新数据库中的是否查看字段
                db.getInstance(GranaryCollectActivity.this).updateCollectFalse(AnalysisUtils.readLoginUserName(GranaryCollectActivity.this));

                //从数据库中重新获取collect为true的数据
                nbl = db.getGranaryCollect(AnalysisUtils.readLoginUserName(GranaryCollectActivity.this));
                adapter.setData(nbl);

                if(nbl.size()==0){
                    tv_none.setVisibility(View.VISIBLE);
                }
                granary_collect_list.setAdapter(adapter);
                Toast.makeText(GranaryCollectActivity.this,"库点收藏记录已清空",Toast.LENGTH_SHORT).show();
            }
        });

        
        tv_none = (TextView) findViewById(R.id.tv_none);
        granary_collect_list = (ListView) findViewById(R.id.granary_collect_list);

        //显示暂无收藏记录
        if (nbl.size() == 0) {
            tv_none.setVisibility(View.VISIBLE);
        }

        adapter = new GranaryCollectAdapter(this);
        adapter.setData(nbl);
        granary_collect_list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = DBUtils.getInstance(this);
        nbl = new ArrayList<NavigationBean>();
        //从数据库中获取库点收藏信息
        nbl = db.getGranaryCollect(AnalysisUtils.readLoginUserName(this));
        LogUtils.i("重新获取焦点","焦点");
        init();
    }
}
