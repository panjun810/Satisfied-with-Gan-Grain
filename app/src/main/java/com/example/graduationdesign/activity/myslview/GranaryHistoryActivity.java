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
import com.example.graduationdesign.adapter.GranaryHistoryAdapter;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class GranaryHistoryActivity extends BaseActivity {
    private LinearLayout ll_delete_all;

    private TextView tv_none;
    private ListView granary_history_list;
    private GranaryHistoryAdapter adapter;
    private DBUtils db;
    private List<NavigationBean> nbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granary_history);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = DBUtils.getInstance(this);
        nbl = new ArrayList<NavigationBean>();

        //从数据库中获取库点浏览记录信息
        nbl = db.getGranaryHistory(AnalysisUtils.readLoginUserName(this));

        init();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context, GranaryHistoryActivity.class);
        context.startActivity(intent);
    }

    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.granary_history_title);
        titleLayout.setText("库点浏览");

        ll_delete_all =(LinearLayout)findViewById(R.id.ll_delete_all);
        ll_delete_all.setVisibility(View.VISIBLE);

        ll_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 更新数据库中的是否查看字段
                db.getInstance(GranaryHistoryActivity.this).updateRecordFalse(AnalysisUtils.readLoginUserName(GranaryHistoryActivity.this));

                //从数据库中获取record为true的数据
                nbl = db.getGranaryHistory(AnalysisUtils.readLoginUserName(GranaryHistoryActivity.this));
                adapter.setData(nbl);

                if(nbl.size()==0){
                    tv_none.setVisibility(View.VISIBLE);
                }
                granary_history_list.setAdapter(adapter);
                Toast.makeText(GranaryHistoryActivity.this,"库点浏览记录已清空",Toast.LENGTH_SHORT).show();
            }
        });

        tv_none=(TextView) findViewById(R.id.tv_none);
        granary_history_list=(ListView) findViewById(R.id.granary_history_list);

        if(nbl.size()==0){
            tv_none.setVisibility(View.VISIBLE);
        }

        adapter = new GranaryHistoryAdapter(this);
        adapter.setData(nbl);
        granary_history_list.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = DBUtils.getInstance(this);
        nbl = new ArrayList<NavigationBean>();
        //从数据库中获取record为true的数据
        nbl = db.getGranaryHistory(AnalysisUtils.readLoginUserName(this));
        init();
    }

}
