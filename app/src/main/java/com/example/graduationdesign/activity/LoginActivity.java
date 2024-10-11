package com.example.graduationdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.MainActivity;
import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.SmsLayout;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.LogUtils;
import com.example.graduationdesign.utils.MD5Utils;


public class LoginActivity extends BaseActivity {
    private TextView tv_register,tv_find_psw;
    private TextView switch_password,switch_sms;
    private Button btn_login,btn_guestlogin;

    private EditText et_user_name,et_psw;
    private LinearLayout login_password;

    private String userName,psw,spPsw,phone;
    private SmsLayout smsLayout;

    public static void actionStart(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context,String userName,String phone){
        Intent intent=new Intent(context,LoginActivity.class);
        intent.putExtra("username",userName);
        intent.putExtra("phone_number",phone);
        context.startActivity(intent);
    }

    public static void actionStartForResult(Activity activity,int requestCode){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getData();
        init();
        initMobSDK();
        LogUtils.d("登录页","创建LoginActivity");
}

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.login_title);
        titleLayout.setText("登录").setColorTransparent().setBackGone();

        tv_register=(TextView) findViewById(R.id.tv_register);
        tv_find_psw= (TextView) findViewById(R.id.tv_find_psw);
        btn_login=(Button) findViewById(R.id.btn_login);
        btn_guestlogin=(Button) findViewById(R.id.btn_guestlogin);
        et_user_name=(EditText) findViewById(R.id.et_user_name);

        login_password=(LinearLayout)findViewById(R.id.login_password);

        switch_password=(TextView) findViewById(R.id.switch_password);
        switch_sms=(TextView) findViewById(R.id.switch_sms);
        //默认显示的是密码登录
        switch_password.setTextColor(Color.parseColor("#228B22"));
        switch_password.getPaint().setFakeBoldText(true);
        switch_sms.setTextColor(Color.parseColor("#000000"));

        et_psw=(EditText) findViewById(R.id.et_psw);

        //切换到密码登录的点击事件
        switch_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //密码登录控件显示
                login_password.setVisibility(View.VISIBLE);
                //手机登录控件隐藏
                smsLayout.setVisibility(View.GONE);

                //设置切换登录方式字体显示颜色
                switch_password.setTextColor(Color.parseColor("#228B22"));
                switch_password.getPaint().setFakeBoldText(true);
                switch_sms.setTextColor(Color.parseColor("#000000"));
                switch_sms.getPaint().setFakeBoldText(false);

                //密码登录按钮隐藏
                btn_login.setVisibility(View.VISIBLE);
            }
        });

        //切换到验证码登录的点击事件
        switch_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //密码登录控件隐藏
                login_password.setVisibility(View.GONE);
                //手机登录控件显示
                smsLayout.setVisibility(View.VISIBLE);

                //设置切换登录方式字体显示颜色
                switch_sms.setTextColor(Color.parseColor("#228B22"));
                switch_sms.getPaint().setFakeBoldText(true);
                switch_password.setTextColor(Color.parseColor("#000000"));
                switch_password.getPaint().setFakeBoldText(false);

                //密码登录按钮隐藏
                btn_login.setVisibility(View.GONE);
            }
        });

        //立即注册控件的点击事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsLayout.onDestroy();
                RegisterActivity.actionStartForResult(LoginActivity.this,1);
            }
        });

        //找回密码控件的点击事件
        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TODO MOBSDK中使用了事件处理器（EventHandler）来处理短信验证码相关的事件，而事件处理器是单例的，
                   它在登录页面中被注册，如果此时在修改密码页面中也使用了它，在修改密码页触发点击事件时，事件处理器会收到事件并将其发送到所有注册的handler中，
                   因此登录页面和修改密码页面中的handler都会收到事件并进行处理，为了避免这种情况，跳转页面后必须要销毁EventHandler！
                 */
                smsLayout.onDestroy();
                FindPswActivity.actionStart(LoginActivity.this);
            }
        });

        //登录按钮的点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                String md5Psw= MD5Utils.md5(psw);    //把登录页面输入的密码用MD5加密
                spPsw=AnalysisUtils.readPsw(LoginActivity.this,userName);      //SharedPreferences中保存的用户名
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }else if(!AnalysisUtils.isExistUserName(LoginActivity.this,userName)){
                    Toast.makeText(LoginActivity.this, "此用户名未被注册", Toast.LENGTH_SHORT).show();;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else if(md5Psw.equals(spPsw)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态
                    saveLoginStatus(true, userName);
                    //跳转到登录页
                    MainActivity.actionStartIsLogin(LoginActivity.this);
                    LoginActivity.this.finish();
                    return;
                }else if((spPsw!=null&&!TextUtils.isEmpty(spPsw)&&!md5Psw.equals(spPsw))){
                    Toast.makeText(LoginActivity.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //游客登录按钮的点击事件
        btn_guestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.actionStart(LoginActivity.this);
                LoginActivity.this.finish();
            }
        });
    }

    private void initMobSDK() {
        smsLayout = (SmsLayout) findViewById(R.id.ll_login);
        smsLayout.setSMSCallback(new SmsLayout.SmsCallback() {
            @Override
            public boolean smsOnClick() {
                phone = smsLayout.getPhoneString();
                if(!AnalysisUtils.isExistPhone(LoginActivity.this,phone)){
                    Toast.makeText(LoginActivity.this, "此手机号未绑定过用户", Toast.LENGTH_SHORT).show();
                    LogUtils.i("phone",phone);
                    return true;
                }
                return false;
            }

            @Override
            public boolean submitOnClickOne() {
                return false;
            }

            @Override
            public boolean submitOnClickTwo() {
                phone = smsLayout.getPhoneString();
                if(!AnalysisUtils.isExistPhone(LoginActivity.this,phone)){
                    Toast.makeText(LoginActivity.this, "此手机号未绑定过用户", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void validateSuccessful() {
                //验证登录成功的操作
                phone = smsLayout.getPhoneString();
                userName=AnalysisUtils.readName(LoginActivity.this,phone);
                //保存登录状态
                saveLoginStatus(true, userName);

                //把登录成功的状态传递到MainActivity中
                MainActivity.actionStartIsLogin(LoginActivity.this);
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsLayout.onDestroy();
        LogUtils.i("登录页","销毁LoginActivity");
    }


    @Override
    protected void onResume() {
        super.onResume();
        smsLayout.onResume();
    }

    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin", status);//存入boolean类型的登录状态
        editor.putString("loginUserName", userName);//存入登录状态时的用户名
        editor.commit();//提交修改
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);   //将子Activity的结果传递给它的父Activity。
        if (data != null) {   //从注册页传递过来的数据data
            //从注册界面传递过来的用户名
            String userName = data.getStringExtra("username");
            if (!TextUtils.isEmpty(userName)) {
                et_user_name.setText(userName);
                //设置光标的位置
                et_user_name.setSelection(userName.length());
            }

            //从注册界面传递过来的手机号
            String phone = data.getStringExtra("phone_number");
            if (!TextUtils.isEmpty(phone)) {
                //获取手机号输入框控件
                EditText et_phone = smsLayout.getEt_phone();
                et_phone.setText(phone);
                //设置光标的位置
                et_phone.setSelection(phone.length());
            }
        }
    }

    private void getData(){
        String userName = getIntent().getStringExtra("username");
        String phone = getIntent().getStringExtra("phone_number");
        if(userName!=null && phone!=null){
            et_user_name.setText(userName);
            //设置光标的位置
            et_user_name.setSelection(userName.length());

            //获取手机号输入框控件
            EditText et_phone = smsLayout.getEt_phone();
            et_phone.setText(phone);
            //设置光标的位置
            et_phone.setSelection(phone.length());
        }
    }


}

