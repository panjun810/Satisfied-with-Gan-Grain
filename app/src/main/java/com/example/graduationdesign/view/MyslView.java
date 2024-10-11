package com.example.graduationdesign.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.myslview.AboutActivity;
import com.example.graduationdesign.activity.myslview.ContactActivity;
import com.example.graduationdesign.activity.myslview.GranaryCollectActivity;
import com.example.graduationdesign.activity.myslview.GranaryHistoryActivity;
import com.example.graduationdesign.activity.LoginActivity;
import com.example.graduationdesign.activity.myslview.SettingActivity;
import com.example.graduationdesign.activity.myslview.setting.userinfo.UserInfoActivity;
import com.example.graduationdesign.bean.UserBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;
import com.example.graduationdesign.dialog.VisitorDialog;

public class MyslView {
    public ImageView iv_head_icon;
    private LinearLayout ll_head;
    private RelativeLayout rl_granary_history,rl_granary_collect,rl_setting;
    private RelativeLayout rl_about ,rl_contact;
    private TextView tv_user_name;
    private Activity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    public MyslView(Activity context) {
        mContext = context;
        //为之后将Layout转化为view时用，LayoutInflater是一个用于将XML布局文件转换为对应的View对象的工具类
        //这段代码的含义是使用当前类的上下文对象（即构造函数中传入的context对象）来获取一个LayoutInflater实例，并将这个实例赋值给成员变量mInflater，以便在类中的其他方法中使用
        mInflater = LayoutInflater.from(mContext);
    }
    private  void createView() {
        initView();
    }
    /**
     * 获取界面控件
     */
    private void initView() {
        //设置布局文件
        mCurrentView = mInflater.inflate(R.layout.main_view_mysl, null);   //将名为main_view_mysl的布局文件转换为一个View对象
        ll_head= (LinearLayout) mCurrentView.findViewById(R.id.ll_head);
        iv_head_icon=(ImageView) mCurrentView.findViewById(R.id.iv_head_icon);
        rl_granary_history =(RelativeLayout) mCurrentView.findViewById(R.id.rl_granary_history);
        rl_granary_collect =(RelativeLayout) mCurrentView.findViewById(R.id.rl_granary_collect);
        rl_setting = (RelativeLayout) mCurrentView.findViewById(R.id.rl_setting);
        rl_about = (RelativeLayout)mCurrentView.findViewById(R.id.rl_about);
        rl_contact = (RelativeLayout)mCurrentView.findViewById(R.id.rl_contact);
        tv_user_name=(TextView) mCurrentView.findViewById(R.id.tv_user_name);
        mCurrentView.setVisibility(View.VISIBLE);

        setLoginParams(readLoginStatus());//设置登录时界面控件的状态


        ll_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否已经登录
                if(readLoginStatus()){
                    //已登录跳转到个人资料界面
                    UserInfoActivity.actionStartForResult(mContext,2);
                }else{
                    //未登录跳转到登录界面
                    LoginActivity.actionStartForResult(mContext,1);
                    mContext.finish();
                }
            }
        });

        //库点浏览页的点击事件
        rl_granary_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readLoginStatus()){
                    //跳转到库点浏览记录界面
                    GranaryHistoryActivity.actionStart(mContext);
                }else{
                    VisitorDialog.setupClickToVisitDialog(mContext);
                }
            }
        });
        //库点收藏页的点击事件
        rl_granary_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(readLoginStatus()){
                    //跳转到库点浏览记录界面
                    GranaryCollectActivity.actionStart(mContext);
                }else{
                    VisitorDialog.setupClickToVisitDialog(mContext);
                }

            }
        });


        //系统设置的点击事件
        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readLoginStatus()){
                    //跳转到设置界面
                    SettingActivity.actionStartForResult(mContext,1);
                }else{
                    VisitorDialog.setupClickToVisitDialog(mContext);
                }
            }
        });
        //联系方式的点击事件
        rl_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactActivity.actionStart(mContext);
            }
        });
        //关于赣粮的点击事件
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.actionStart(mContext);
            }
        });

    }
    /**
     * 登录成功后设置我的赣粮界面的用户名显示方式
     */
    public void setLoginParams(boolean isLogin){
        if(isLogin){
            tv_user_name.setText(AnalysisUtils.readLoginUserName(mContext));

            UserBean bean = DBUtils.getInstance(mContext).getUserInfo(AnalysisUtils.readLoginUserName(mContext));
            if (bean != null && bean.iconPath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(bean.iconPath);
                iv_head_icon.setImageBitmap(bitmap);
            } else {
                iv_head_icon.setImageResource(R.drawable.calender);
            }

        }else{
            tv_user_name.setText("点击登录");
            iv_head_icon.setImageResource(R.drawable.calender);
        }
    }
    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView() {
        if (mCurrentView == null) {
            createView();
        }
        return mCurrentView;
    }
    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView(){
        if(mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }
    /**
     * 从SharedPreferences中读取登录状态
     */
    private boolean readLoginStatus(){
        SharedPreferences sp=mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        /* TODO false 是默认值参数，因此，如果 "isLogin" 键不存在或者值为 false，那么 isLogin 的值将为 false，表示用户未登录。
           反之，如果 "isLogin" 键存在且值为 true，那么 isLogin 的值将为 true，表示用户已登录。*/
        boolean isLogin=sp.getBoolean("isLogin", false);
        return isLogin;
    }

    public void updataIcon(String iconPath){
        Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
        iv_head_icon.setImageBitmap(bitmap);

    }
}
