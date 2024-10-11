package com.example.graduationdesign.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.SmsLayout;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.LogUtils;
import com.example.graduationdesign.utils.MD5Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {
    //账号、密码、再次输入的密码的控件
    private EditText et_user_name,et_psw,et_psw_again;

    private SmsLayout smsLayout;
    private String userName,psw,pswAgain,phone;

    public static void actionStartForResult(Activity activity,int requestCode){
        Intent intent=new Intent(activity,RegisterActivity.class);
        intent.putExtra("Flag","login");
        activity.startActivityForResult(intent, requestCode);    //开启RegisterActivity，并在销毁时返回数据和请求码1
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置页面布局
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        initMobSDK();
    }

    private void init() {
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.register_title);
        titleLayout.setText("注册").setColorTransparent();

        //从activity_register.xml页面布局中获得对应的UI控件
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_psw_again = (EditText) findViewById(R.id.et_psw_again);
    }

    private void initMobSDK(){
        smsLayout = (SmsLayout) findViewById(R.id.ll_register);
        smsLayout.setButtonText("注 册");
        smsLayout.setSMSCallback(new SmsLayout.SmsCallback() {
            @Override
            public boolean smsOnClick() {
                //获取输入的手机号
                phone= smsLayout.getPhoneString();
                if(AnalysisUtils.isExistPhone(RegisterActivity.this,phone)){
                    Toast.makeText(RegisterActivity.this, "此手机号已经被其他用户使用", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public boolean submitOnClickOne() {
                //获取输入的用户名、密码、再次输入的密码
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                pswAgain=et_psw_again.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    LogUtils.i("userName",userName);
                    return true;
                }else if(AnalysisUtils.isExistUserName(RegisterActivity.this,userName)){
                    Toast.makeText(RegisterActivity.this, "此用户名已经存在", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(psw.length() < 8){
                    Toast.makeText(RegisterActivity.this, "密码长度至少为8位", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(psw.length() > 16) {
                    Toast.makeText(RegisterActivity.this, "密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(psw.contains(" ")){
                    Toast.makeText(RegisterActivity.this, "密码中不能包含空格", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "再次输入的密码不能为空", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(pswAgain.length() < 8){
                    Toast.makeText(RegisterActivity.this, "再次输入的密码长度至少为8位", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(pswAgain.length() > 16) {
                    Toast.makeText(RegisterActivity.this, "再次输入的密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(pswAgain.contains(" ")){
                    Toast.makeText(RegisterActivity.this, "再次输入的密码中不能包含空格", Toast.LENGTH_SHORT).show();
                    return true;
                }else if(!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public boolean submitOnClickTwo() {
                phone = smsLayout.getPhoneString();
                if(AnalysisUtils.isExistPhone(RegisterActivity.this,phone)){
                    Toast.makeText(RegisterActivity.this, "此手机号已经被其他用户使用", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void validateSuccessful() {
                userName=et_user_name.getText().toString().trim();
                psw=et_psw.getText().toString().trim();
                pswAgain=et_psw_again.getText().toString().trim();
                phone = smsLayout.getPhoneString();

                //把账号、密码和账号标识保存到sp里面
                saveRegisterInfo(userName, psw,phone);

                //注册成功后把账号传递到LoginActivity.java中
                Intent data =new Intent(RegisterActivity.this,LoginActivity.class);
                data.putExtra("username", userName);
                data.putExtra("phone_number", phone);
                setResult(RESULT_OK, data);  //携带数据进行回传，RESULT_OK为返回码

                String Flag = getIntent().getStringExtra("Flag");
                //如果是从登录页跳转过来的话
                if(Flag.equals("login")){
                    RegisterActivity.this.finish();
                }else if(Flag.equals("other")){    //如果是从其他页面跳转过来

                    LoginActivity.actionStart(RegisterActivity.this,userName,phone);
                    RegisterActivity.this.finish();
                }

            }
        });
    }

    /**
     * 保存账号和密码到SharedPreferences中
     */
    private void saveRegisterInfo(String userName,String psw,String phone){
        String md5Psw= MD5Utils.md5(psw);//把密码用MD5加密
        //loginInfo表示文件名
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器

        // 将用户信息存储到SharedPreferences中
        try {
            JSONObject userObject = new JSONObject();
            userObject.put("username", userName);
            userObject.put("password", md5Psw);
            userObject.put("phone_number", phone);
          /*从 SharedPreferences 中获取了一个名为 "user_list" 的键对应的值（默认值为空的 JSON 数组字符串 "[]"），
             并将其解析为一个 JSONArray 对象 userArray
          */
            JSONArray userArray = new JSONArray(sp.getString("user_list", "[]"));
            userArray.put(userObject);

            /*将userArray 对象转换为字符串，并将其存储在 SharedPreferences 中，键为 "user_list"。*/
            editor.putString("user_list", userArray.toString());
            editor.apply();

        } catch (JSONException e) {    //捕获可能发生的 JSONException 异常
            e.printStackTrace();  //打印异常信息
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        smsLayout.onDestroy();
    }
}
