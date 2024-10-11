package com.example.graduationdesign.activity.myslview.setting;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.SmsLayout;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.utils.AnalysisUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PhoneChangeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_user_security,tv_user_phone;
    private EditText et_user_security;
    private Button btn_security;
    private String phone;
    private SmsLayout smsLayout;

    public static void actionStart(Context context){
        Intent intent=new Intent(context,PhoneChangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_change);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        initMobSDK();
    }

    private void initMobSDK() {
        smsLayout = (SmsLayout) findViewById(R.id.ll_phone_change);
        smsLayout.setButtonText("保存");
        smsLayout.setSMSCallback(new SmsLayout.SmsCallback() {
            @Override
            public boolean smsOnClick() {
                phone = smsLayout.getPhoneString();
                //如果输入电话号码不存在
                if(!isthisExistPhone(phone)){
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
                //如果输入电话号码不存在
                if(!isthisExistPhone(phone)){
                    return true;
                }
                return false;
            }

            @Override
            public void validateSuccessful() {
                phone = smsLayout.getPhoneString();
                //保存换绑后的手机号
                AnalysisUtils.savePhone(PhoneChangeActivity.this,phone);
                PhoneChangeActivity.this.finish();
            }
        });

    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.phone_change_title);
        titleLayout.setText("换绑手机");

        tv_user_security=(TextView) findViewById(R.id.tv_user_security);
        tv_user_phone=(TextView) findViewById(R.id.tv_user_phone);
        et_user_security=(EditText) findViewById(R.id.et_user_security);

        btn_security=(Button) findViewById(R.id.btn_security);
        btn_security.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String security = "";
        int id = view.getId();       //TODO 获取当前点击的view的id
        switch (id) {
            case R.id.btn_security:  //密保验证的点击事件
                security=et_user_security.getText().toString().trim();
                if(security.contains(" ")){
                    Toast.makeText(PhoneChangeActivity.this, "密保不能包含空格", Toast.LENGTH_SHORT).show();
                }else if(!AnalysisUtils.isthisSecurity(this,security)){
                    Toast.makeText(PhoneChangeActivity.this,"输入的密保有误，验证失败",Toast.LENGTH_LONG).show();
                }else{

                    //禁用EditView，从而阻止用户再次进行输入
                    et_user_security.setEnabled(false);
                    Toast.makeText(PhoneChangeActivity.this,"密保验证通过！",Toast.LENGTH_LONG).show();
                    //密保验证框隐藏
                    btn_security.setVisibility(View.GONE);

                    //修改手机框显示
                    tv_user_phone.setVisibility(View.VISIBLE);
                    tv_user_phone.setText("您的当前绑定的手机号是:"+AnalysisUtils.readPhone(PhoneChangeActivity.this));

                    smsLayout.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    //换绑手机的判断手机号是否被用户绑定过
    private boolean isthisExistPhone(String phone) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String usersString = sp.getString("user_list", "[]");
        try {
            JSONArray users = new JSONArray(usersString);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("phone_number").equals(phone)) {
                    if (user.getString("username").equals(AnalysisUtils.readLoginUserName(this))) {
                        Toast.makeText(this, "此手机号为当前用户绑定的手机号", Toast.LENGTH_SHORT).show();
                        return false;
                    }else {
                        Toast.makeText(PhoneChangeActivity.this, "此手机号被其它用户绑定", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        smsLayout.onDestroy();
    }
}
