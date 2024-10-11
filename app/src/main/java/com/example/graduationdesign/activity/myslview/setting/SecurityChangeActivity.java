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

public class SecurityChangeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_user_security;
    private EditText et_security_change;
    private Button btn_change_security;
    private String phone;
    private SmsLayout smsLayout;

    public static void actionStart(Context context){
        Intent intent=new Intent(context,SecurityChangeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_change);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        initMobSDK();
    }

    private void initMobSDK() {
        smsLayout = (SmsLayout) findViewById(R.id.ll_security_change);
        smsLayout.setButtonText("手机验证");
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
                smsLayout.validateGone();

                //修改密保框显示
                tv_user_security.setVisibility(View.VISIBLE);
                tv_user_security.setText("您的原密保是:"+AnalysisUtils.readSecurity(SecurityChangeActivity.this));
                et_security_change.setVisibility(View.VISIBLE);
                btn_change_security.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 获取界面控件
     */
    private void init(){
        //标题栏
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.security_change_title);
        titleLayout.setText("修改密保");

        tv_user_security=(TextView) findViewById(R.id.tv_user_security);
        et_security_change=(EditText)findViewById(R.id.et_security_change);
        btn_change_security=(Button) findViewById(R.id.btn_change_security);
        btn_change_security.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String security = "";
        int id = view.getId();       //TODO 获取当前点击的view的id
        switch (id) {
            case R.id.btn_change_security:   //保存修改后的密保的点击事件
                security=et_security_change.getText().toString().trim();
                if(AnalysisUtils.isthisSecurity(this,security)){
                    Toast.makeText(SecurityChangeActivity.this,"新密保不能和原密保相同",Toast.LENGTH_LONG).show();
                }else if(security.contains(" ")){
                    Toast.makeText(SecurityChangeActivity.this, "新密保不能包含空格", Toast.LENGTH_SHORT).show();
                }else {
                    AnalysisUtils.saveSecurity(this,security);
                    Toast.makeText(SecurityChangeActivity.this,"密保修改成功",Toast.LENGTH_LONG).show();
                    SecurityChangeActivity.this.finish();
                }
                break;
        }

    }



    //修改密保的判断手机号是否被用户绑定过
    private boolean isthisExistPhone(String phone) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String usersString = sp.getString("user_list", "[]");
        try {
            JSONArray users = new JSONArray(usersString);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("phone_number").equals(phone)) {
                    if (user.getString("username").equals(AnalysisUtils.readLoginUserName(this))) {
                        //此时输入的手机号为登录用户绑定的手机号
                        return true;
                    }else {
                        Toast.makeText(SecurityChangeActivity.this, "此手机号不是登录用户绑定的手机号", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "此手机号为未被任何用户绑定", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        smsLayout.onDestroy();
    }

}
