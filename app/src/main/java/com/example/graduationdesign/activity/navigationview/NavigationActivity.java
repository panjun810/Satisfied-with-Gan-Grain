package com.example.graduationdesign.activity.navigationview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.adapter.NavigationAdapter;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.DBUtils;
import com.example.graduationdesign.utils.JsonParse;

import java.util.List;


public class NavigationActivity extends BaseActivity {
    private ListView navigation_list;
    private NavigationAdapter adapter;

    //从XML解析获得的数据
    private List<NavigationBean> nbl;

    private DBUtils db;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getNavigationData();
        init();
    }

    private void getNavigationData() {
        //从XML解析获得的数据
        nbl = JsonParse.getInstance().getNavigationFromJson(this);
    }

    public static void actionStart(Context context,String city,String county){
        Intent intent=new Intent(context, NavigationActivity.class);
        intent.putExtra("city",city);
        intent.putExtra("county",county);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.navigation_title);
        titleLayout.setText("库点列表");

        navigation_list=(ListView)findViewById(R.id.navigation_list);
        adapter = new NavigationAdapter(this);
        //设置数据给适配器
        adapter.setData(nbl);

        //获取从库点导航查询页穿来的数据
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String county = intent.getStringExtra("county");

        adapter.setIF(city,county);
        navigation_list.setAdapter(adapter);
    }
}
