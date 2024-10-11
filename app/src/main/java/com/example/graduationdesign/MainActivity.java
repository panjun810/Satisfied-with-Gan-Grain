package com.example.graduationdesign;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.activity.BaseView;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.dialog.VisitorDialog;
import com.example.graduationdesign.view.HomeView;
import com.example.graduationdesign.view.NavigationView;
import com.example.graduationdesign.view.MyslView;
import com.example.graduationdesign.view.GuessView;


public class MainActivity extends BaseView implements View.OnClickListener {
    /**
     * 视图
     */
    private HomeView mHomeView;
    private NavigationView mNavigationView;
    private MyslView mMyslView;
    private GuessView mGuessView;
    /**
     * 中间内容栏
     */
    private FrameLayout mBodyLayout;
    /**
     * 底部按钮栏
     */
    public LinearLayout mBottomLayout;
    /**
     * 底部按钮
     */
    private View mHomeBtn;
    private View mCompanyBtn;
    private View mMessageBtn;
    private View mMyslBtn;


    private TextView tv_home;
    private TextView tv_company;
    private TextView tv_message;
    private TextView tv_mysl;


    private ImageView iv_home;
    private ImageView iv_company;
    private ImageView iv_message;
    private ImageView iv_mysl;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void actionStartIsLogin(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("isLogin",true);
        activity.setResult(RESULT_OK,intent);        //将登录成功的状态发送给MainActivity
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();               //获取界面上的UI控件
        initBottomBar();     //获取底部导航栏上的控件
        setListener();       //设置底部三个按钮的点击监听事件
        //TODO 设置界面view的初始化状态
        setInitStatus();
    }
    /**
     * 获取界面上的UI控件
     */
    private void init() {
        initBodyLayout();       //获取页面中间部分
    }
    /**
     * 获取底部导航栏上的控件
     */
    private void initBottomBar() {
        mBottomLayout = (LinearLayout) findViewById(R.id.main_bottom_bar);
        mHomeBtn = findViewById(R.id.bottom_bar_home_btn);
        mCompanyBtn = findViewById(R.id.bottom_bar_company_btn);
        mMessageBtn = findViewById(R.id.bottom_bar_message_btn);
        mMyslBtn=findViewById(R.id.bottom_bar_mysl_btn);


        tv_home = (TextView) findViewById(R.id.bottom_bar_text_home);
        tv_company = (TextView) findViewById(R.id.bottom_bar_text_company);
        tv_message = (TextView) findViewById(R.id.bottom_bar_text_message);
        tv_mysl = (TextView) findViewById(R.id.bottom_bar_text_mysl);


        iv_home = (ImageView) findViewById(R.id.bottom_bar_image_home);
        iv_company = (ImageView) findViewById(R.id.bottom_bar_image_company);
        iv_message = (ImageView) findViewById(R.id.bottom_bar_image_message);
        iv_mysl = (ImageView) findViewById(R.id.bottom_bar_image_mysl);


    }
    private void initBodyLayout() {        //获取页面中间部分
        mBodyLayout = (FrameLayout) findViewById(R.id.main_body);
    }
    /**
     * 控件的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //首页的点击事件
            case R.id.bottom_bar_home_btn:
                clearBottomImageState();
                selectDisplayView(0);
                break;
            //库点导航的点击事件
            case R.id.bottom_bar_company_btn:
                if(!AnalysisUtils.readLoginStatus(this)){
                    VisitorDialog.setupClickToVisitDialog(this);
                } else{
                    clearBottomImageState();
                    selectDisplayView(1);
                }
                break;
            //价格测算的点击事件
            case R.id.bottom_bar_message_btn:
                if(!AnalysisUtils.readLoginStatus(this)){
                    VisitorDialog.setupClickToVisitDialog(this);
                }else{
                    clearBottomImageState();
                    selectDisplayView(2);
                }
/*                if (mMyInfoView != null) {
                    // mMyInfoView.setLoginParams(readLoginStatus());
                }*/
                break;
            //我的赣粮点击事件
            case R.id.bottom_bar_mysl_btn:
                clearBottomImageState();
                selectDisplayView(3);
                /*if (mMyslView != null) {
                    mMyslView.setLoginParams(readLoginStatus());
                }*/
                break;

            default:
                break;
        }
    }
    /**
     * 设置底部四个按钮的点击监听事件
     */
    private void setListener() {
        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setOnClickListener(this);  //getChildAt(int index)方法会返回指定视图容器的子视图
        }
    }
    /**
     * 清除底部按钮的选中状态
     */
    private void clearBottomImageState() {
        //文字改成未选中时的灰色
        tv_home.setTextColor(Color.parseColor("#666666"));
        tv_company.setTextColor(Color.parseColor("#666666"));
        tv_message.setTextColor(Color.parseColor("#666666"));
        tv_mysl.setTextColor(Color.parseColor("#666666"));

        //图标改成未选中时的灰色
        iv_home.setImageResource(R.drawable.unselect_home_icon);
        iv_company.setImageResource(R.drawable.unselect_company_icon);
        iv_message.setImageResource(R.drawable.unselect_message_icon);
        iv_mysl.setImageResource(R.drawable.unselect_mysl_icon);

        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setSelected(false);      //取消mBottomLayout的子视图的选中状态
        }
    }
    /**
     * 设置底部按钮选中状态
     */
    public void setSelectedStatus(int index) {
        switch (index) {
            case 0:
                mHomeBtn.setSelected(true);
                iv_home.setImageResource(R.drawable.select_home_icon);
                tv_home.setTextColor(Color.parseColor("#33ff00"));
                // rl_title_bar.setVisibility(View.VISIBLE);
                //tv_main_title.setText("博学谷课程");
                break;
            case 1:
                mCompanyBtn.setSelected(true);
                iv_company.setImageResource(R.drawable.select_company_icon);
                tv_company.setTextColor(Color.parseColor("#33ff00"));
                //rl_title_bar.setVisibility(View.VISIBLE);
                //tv_main_title.setText("博学谷习题");
                break;
            case 2:
                mMessageBtn.setSelected(true);
                iv_message.setImageResource(R.drawable.select_message_icon);
                tv_message.setTextColor(Color.parseColor("#33ff00"));
                //rl_title_bar.setVisibility(View.GONE);
                break;
            case 3:
                mMyslBtn.setSelected(true);
                iv_mysl.setImageResource(R.drawable.select_mysl_icon);
                tv_mysl.setTextColor(Color.parseColor("#33ff00"));
                break;
        }
    }
    /**
     * 移除不需要的视图
     */
    private void removeAllView() {
        for (int i = 0; i < mBodyLayout.getChildCount(); i++) {
            mBodyLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }
    /**
     * 设置界面view的初始化状态
     */
    private void setInitStatus() {
        clearBottomImageState();    //清除底部按钮的选中状态
        setSelectedStatus(0);      //设置底部按钮选中状态
        createView(0);  //选择视图
    }
    /**
     * 显示对应的页面
     */
    private void selectDisplayView(int index) {
        removeAllView();            //移除不需要的视图
        createView(index);          //选择视图
        setSelectedStatus(index);   //设置底部按钮选中状态
    }
    /**
     * 选择视图
     */
    private void createView(int viewIndex) {
        switch (viewIndex) {
            case 0:
                //首页界面
                if (mHomeView == null) {
                    mHomeView = new HomeView(this);
                    mBodyLayout.addView(mHomeView.getView());
                } else {
                    mHomeView.getView();
                }
                mHomeView.showView();
                break;
            case 1:
                //库点导航界面
                if (mNavigationView == null) {
                    mNavigationView = new NavigationView(this);
                    mBodyLayout.addView(mNavigationView.getView());
                } else {
                    mNavigationView.getView();
                }
                //设置点击库点导航页后的显示页面默认状态为所有城市和所有区县
                mNavigationView.setFlag(0,0);
                mNavigationView.showView();
                break;
            case 2:
                //价格测算界面
                if (mGuessView == null) {
                    mGuessView = new GuessView(this);
                    mBodyLayout.addView(mGuessView.getView());
                } else {
                    mGuessView.getView();
                }
                //设置点击价格测算页后的显示页面的所有编辑框都没有文本,并且默认显示小麦
                mGuessView.SetGuessView();
                mGuessView.showView();
                break;
            case 3:
                //我的赣粮界面
                if (mMyslView == null) {
                    mMyslView = new MyslView(this);
                    mBodyLayout.addView(mMyslView.getView());
                } else {
                    mMyslView.getView();
                }
                mMyslView.showView();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //从设置界面或登录界面传递过来的登录状态
            boolean isLogin=data.getBooleanExtra("isLogin",false);
            if(isLogin){//登录成功时显示课程界面
                clearBottomImageState();
                selectDisplayView(0);
            }
            if (mMyslView != null) { //登录成功或退出登录时根据isLogin设置我的界面
                mMyslView.setLoginParams(isLogin);
            }
            if(requestCode == 2){
                String iconPath=data.getStringExtra("IconPath");
                mMyslView.updataIcon(iconPath);
                clearBottomImageState();
                selectDisplayView(3);
                /*boolean isLoginUser=data.getBooleanExtra("isLoginUser",false);
                mMyslView.setLoginParams(isLoginUser);*/
            }
        }

    }


/*    protected long exitTime;//记录第一次点击时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出满意赣粮",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                if (readLoginStatus()) {
                    //如果退出此应用时是登录状态，则需要清除登录状态，同时需清除登录时的用户名
                    clearLoginStatus();
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    *//**
     * 获取SharedPreferences中的登录状态
     *//*
    private boolean readLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo",
                Context.MODE_PRIVATE);      //获取已经保存的登录信息
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }


   *//**
     * 清除SharedPreferences中的登录状态
     *//*
    private void clearLoginStatus() {
        SharedPreferences sp = getSharedPreferences("loginInfo",
                Context.MODE_PRIVATE);   //获取已经保存的登录信息
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.putBoolean("isLogin", false);//清除登录状态
        editor.putString("loginUserName", "");//清除登录时的用户名
        editor.commit();//提交修改
    }*/


}