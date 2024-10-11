package com.example.graduationdesign.activity.navigationview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;
import com.example.graduationdesign.utils.LogUtils;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.map.fusionlocation.model.TencentGeoLocation;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.location.api.GeoLocationObserver;
import com.tencent.map.location.core.FusionGeoLocationAdapter;

public class NavigationDetailActivity extends BaseActivity {
    private ImageView background;
    private TextView tv_name,tv_belong,tv_phone,tv_state,tv_browse;

    private LinearLayout ll_collect,ll_navigation;
    private String setbackground,setname,setbelong,setphone,setstate,setbrowse;

    private String setcollect,setlongitude,setlatitude;
    private ImageView image;

    private String spUserName;
    private FusionGeoLocationAdapter geoAdapter;
    private GeoLocationObserver locationObserver;
    private double latitude,longitude;

    private NavigationBean bean;
    private LinearLayout ll_navigation_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_detail);
        //从SharedPreferences中获取登录时的用户名
        spUserName = AnalysisUtils.readLoginUserName(this);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        SetText();

    }

    public static void actionStart(Context context,NavigationBean bean){
        Intent intent = new Intent(context,NavigationDetailActivity.class);
        intent.putExtra("navigationBean",bean);
        context.startActivity(intent);
    }

    public static void actionStartFromNavi(Context context, NavigationBean bean){
        //跳转到库点信息界面
        Intent intent = new Intent(context,NavigationDetailActivity.class);
        //把库点信息界面的信息传递到库点信息页面
        intent.putExtra("navigationBean",bean);
        intent.putExtra("Flag","Navi");
        context.startActivity(intent);
    }

    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.navigation_detail_title);
        //设置对应页的名称
        titleLayout.setText("库点信息");

        ll_navigation_detail = (LinearLayout)findViewById(R.id.ll_navigation_detail);

        background=(ImageView)findViewById(R.id.background);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_belong=(TextView) findViewById(R.id.tv_belong);
        tv_phone=(TextView) findViewById(R.id.tv_phone);
        tv_state=(TextView) findViewById(R.id.tv_state);
        tv_browse=(TextView) findViewById(R.id.tv_browse);

        ll_navigation=(LinearLayout) findViewById(R.id.ll_navigation);
        ll_collect=(LinearLayout) findViewById(R.id.ll_collect);

        image=(ImageView)findViewById(R.id.iv_collect);

        bean = getIntent().getParcelableExtra("navigationBean");

        setcollect =bean.getCollect();
        if(setcollect.equals("false")){
            image.setImageResource(R.drawable.collect_icon_unselect);
        }else if(setcollect.equals("true")){
            image.setImageResource(R.drawable.collect_icon_select);
        }

        setname = bean.getName();
        setbelong = bean.getBelong();

        String Flag = getIntent().getStringExtra("Flag");
        if(Flag != null && Flag.equals("Navi")){
            setlongitude = bean.getLongitude();
            setlatitude = bean.getLatitude();
        }

        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(setcollect.equals("false")){
                        image.setImageResource(R.drawable.collect_icon_select);
                        setcollect = "true";
                        Toast.makeText(NavigationDetailActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                        // 更新数据库中的是否收藏字段
                        DBUtils.getInstance(NavigationDetailActivity.this).updateKeyValue("collect",
                                setcollect,setname,setbelong,spUserName);

                        // 更新数据库中的收藏时间戳字段
                        DBUtils.getInstance(NavigationDetailActivity.this).updateTime("collectTimeStamp",
                                System.currentTimeMillis(),setname,setbelong,spUserName);

                    }else if(setcollect.equals("true")){
                        image.setImageResource(R.drawable.collect_icon_unselect);
                        setcollect = "false";
                        Toast.makeText(NavigationDetailActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                        // 更新数据库中的是否收藏字段
                        DBUtils.getInstance(NavigationDetailActivity.this).updateKeyValue("collect",
                                setcollect,setname,setbelong,spUserName);

                        // 更新数据库中的收藏时间戳字段
                        DBUtils.getInstance(NavigationDetailActivity.this).updateTime("collectTimeStamp",
                                System.currentTimeMillis(),setname,setbelong,spUserName);
                    }

            }
        });

        ll_navigation=(LinearLayout) findViewById(R.id.ll_navigation);
        ll_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTencentMapInstalled()){
                    if(isApplyPermission()){
                        Snackbar.make(ll_navigation_detail,"手机中已下载腾讯地图，跳转到腾讯地图导航",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String mapUrl ="qqmap://map/routeplan?type=drive&from=我的位置&fromcoord=" + latitude + ","+ longitude + "&to="+ setname +"&tocoord=" + setlongitude +"," + setlatitude + "&referer=PE2BZ-GNRKM-AJQ6Q-6E7DR-TLSMK-YTBVS";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
                                startActivity(intent);
                            }
                        }).show();
                    }
                }else{
                    Snackbar.make(ll_navigation_detail,"手机中未下载腾讯地图，跳转到浏览器下载",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String downloadUrl = "https://pr.map.qq.com/j/tmap/download?key=PE2BZ-GNRKM-AJQ6Q-6E7DR-TLSMK-YTBVS";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                            startActivity(intent);
                        }
                    }).show();

                }
            }
        });
    }

    //Android系统 判断手机中是否安装腾讯地图
    private boolean isTencentMapInstalled() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.tencent.map", PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Boolean isApplyPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            // 检查权限是否已被授予 TODO 在 Android 6.0 及以上的版本中，动态权限申请成为了必要的步骤！！！！！！
            if (ContextCompat.checkSelfPermission(NavigationDetailActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码（这里申请的是请求写入外部存储的权限）
                ActivityCompat.requestPermissions(NavigationDetailActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return false;
            } else {
                toNavigation();
                return true;
            }
        }else {
            toNavigation();
            return true;
        }
    }

    //获取当前经纬度
    private void toNavigation() {
        // 初始化FusionGeoLocationAdapter
        geoAdapter = FusionGeoLocationAdapter.getInstance(getApplicationContext());

        // 创建GeoLocationObserver
        locationObserver = new GeoLocationObserver() {
            @Override
            public void onGeoLocationChanged(TencentGeoLocation tencentGeoLocation) {
                // 定位数据回调
                TencentLocation currentLocation = tencentGeoLocation.getLocation();
                latitude = currentLocation.getLatitude();
                longitude = currentLocation.getLongitude();

                LogUtils.d("当前经纬度", latitude + ","+longitude);
            }
        };

        // 添加定位监听
        geoAdapter.addLocationObserver(locationObserver, 1000); // 定位回调频率，推荐1s
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                toNavigation();
            }else {
                Snackbar.make(ll_navigation_detail,"位置权限被拒绝,请重新授权",Snackbar.LENGTH_SHORT).setAction("前往", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //打开应用程序的详细设置页面
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
            }

        }
    }

    private void SetText() {
        tv_name.setText(setname);
        tv_belong.setText(setbelong);

        setphone = bean.getPhone();
        tv_phone.setText(setphone);

        setstate= bean.getState();
        tv_state.setText(setstate);

        setbrowse = bean.getBrowse();
        tv_browse.setText(setbrowse);

        setbackground = bean.getBackground();
        int resourceId = getResources().getIdentifier(setbackground, "drawable", getPackageName());
        background.setBackgroundResource(resourceId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (geoAdapter != null && locationObserver != null) {
            geoAdapter.removeLocationObserver(locationObserver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (geoAdapter != null && locationObserver != null) {
            geoAdapter.removeLocationObserver(locationObserver);
        }
    }
}
