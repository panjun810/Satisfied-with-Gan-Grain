package com.example.graduationdesign.activity;

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


import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.SmsLayout;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.MD5Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FindPswActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_validate_name, et_user_name, et_new_psw, et_new_psw_again;
    private Button btn_validate,btn_validate_sms2;
    //从哪个界面跳转过来的（from为security时是从设置密保界面跳转过来的，否则就是从登录界面跳转过来的）
    private String from;
    private TextView  tv_user_name, tv_new_psw, tv_user_name_sms;
    private int clickCount = 0;       //设置计数器变量来记录点击次数
    private LinearLayout ver_switch,ver_psw,ver_psw_sms;
    private TextView  tv_validate_name,tv_security_ver, tv_phone_ver;
    private TextView  tv_validate_phone,tv_new_psw_sms;
    private EditText et_new_psw_sms,et_new_psw_again_sms;
    private String phone;
    private SmsLayout smsLayout;

    public static void actionStart(Context context){
        Intent intent=new Intent(context,FindPswActivity.class);
        context.startActivity(intent);
    }

    public static void actionStart(Context context,String security){
        Intent intent = new Intent(context, FindPswActivity.class);
        intent.putExtra("from", security);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取从登录界面和设置界面传递过来的数据
        from = getIntent().getStringExtra("from");
        init();
        initMobSDK();
    }

    private void initMobSDK() {
        smsLayout = (SmsLayout) findViewById(R.id.ll_find_psw);
        smsLayout.setButtonText("手机验证");
        smsLayout.setSMSCallback(new SmsLayout.SmsCallback() {
            @Override
            public boolean smsOnClick() {
                phone = smsLayout.getPhoneString();
                if(!AnalysisUtils.isExistPhone(FindPswActivity.this,phone)){
                    Toast.makeText(FindPswActivity.this, "此手机号未绑定过用户", Toast.LENGTH_SHORT).show();
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
                if(!AnalysisUtils.isExistPhone(FindPswActivity.this,phone)){
                    Toast.makeText(FindPswActivity.this, "此手机号未绑定过用户", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void validateSuccessful() {
                //显示绑定了输入手机号的用户名
                tv_user_name_sms.setVisibility(View.VISIBLE);
                phone = smsLayout.getPhoneString();

                tv_user_name_sms.setText("您的用户名是:"+AnalysisUtils.readName(FindPswActivity.this,phone));
                //设置新密码相关操作框显现
                tv_new_psw_sms.setVisibility(View.VISIBLE);
                ver_psw_sms.setVisibility(View.VISIBLE);

                //隐藏验证码,获取验证码，手机验证控件
                smsLayout.validateGone();

                //第二次手机验证按钮显示
                btn_validate_sms2.setVisibility(View.VISIBLE);

            }
        });
    }

    /**
     * 获取界面控件及处理相应控件的点击事件
     */
    private void init() {
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.find_psw_title);

        //验证方式切换的控件
        ver_switch=(LinearLayout)findViewById(R.id.ver_switch);
        tv_security_ver=(TextView) findViewById(R.id.tv_security_ver);
        tv_phone_ver=(TextView) findViewById(R.id.tv_phone_ver);

        //密保验证的默认颜色设置
        tv_security_ver.setTextColor(Color.parseColor("#228B22"));
        tv_phone_ver.setTextColor(Color.parseColor("#000000"));

        //密保验证的控件
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        tv_validate_name=(TextView) findViewById(R.id.tv_validate_name);
        et_validate_name = (EditText) findViewById(R.id.et_validate_name);

        //设置新密码的控件
        tv_new_psw = (TextView) findViewById(R.id.tv_new_psw);
        ver_psw = (LinearLayout)findViewById(R.id.ver_psw);
        et_new_psw = (EditText) findViewById(R.id.et_new_psw);
        et_new_psw_again = (EditText) findViewById(R.id.et_new_psw_again);

        //密保验证按钮控件
        btn_validate = (Button) findViewById(R.id.btn_validate);

        //手机验证的控件
        tv_validate_phone=(TextView) findViewById(R.id.tv_validate_phone);

        tv_user_name_sms=(TextView) findViewById(R.id.tv_user_name_sms);
        tv_new_psw_sms=(TextView) findViewById(R.id.tv_new_psw_sms);
        ver_psw_sms = (LinearLayout)findViewById(R.id.ver_psw_sms);
        et_new_psw_sms = (EditText) findViewById(R.id.et_new_psw_sms);
        et_new_psw_again_sms = (EditText) findViewById(R.id.et_new_psw_again_sms);

        //手机验证按钮控件
        btn_validate_sms2 = (Button) findViewById(R.id.btn_validate_sms2);
        btn_validate_sms2.setOnClickListener(this);

        //切换到密保验证的点击事件
        tv_security_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_security_ver.setTextColor(Color.parseColor("#228B22"));
                tv_phone_ver.setTextColor(Color.parseColor("#000000"));
                //用户名，密保输入此时可见
                tv_validate_name.setVisibility(View.VISIBLE);
                et_validate_name.setVisibility(View.VISIBLE);
                tv_user_name.setVisibility(View.VISIBLE);
                et_user_name.setVisibility(View.VISIBLE);
                btn_validate.setVisibility(View.VISIBLE);

                //手机号和验证码输入此时不可见
                tv_validate_phone.setVisibility(View.GONE);
                smsLayout.setVisibility(View.GONE);

                tv_user_name_sms.setVisibility(View.GONE);
                tv_new_psw_sms.setVisibility(View.GONE);
                ver_psw_sms.setVisibility(View.GONE);
                btn_validate_sms2.setVisibility(View.GONE);

                //初始化编辑框的值
                smsLayout.getEt_phone().setText("");
                smsLayout.getEt_sms().setText("");
                et_new_psw_sms.setText("");
                et_new_psw_again_sms.setText("");

                //编辑框重新启用
                et_user_name.setEnabled(true);
                et_validate_name.setEnabled(true);

                smsLayout.reSms();
            }
        });

        //切换到手机验证的点击事件
        tv_phone_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_security_ver.setTextColor(Color.parseColor("#000000"));
                tv_phone_ver.setTextColor(Color.parseColor("#228B22"));
                //用户名，密保输入此时不可见
                tv_validate_name.setVisibility(View.GONE);
                et_validate_name.setVisibility(View.GONE);
                tv_user_name.setVisibility(View.GONE);
                et_user_name.setVisibility(View.GONE);
                btn_validate.setVisibility(View.GONE);
                //设置密码此时不可见
                tv_new_psw.setVisibility(View.GONE);
                ver_psw.setVisibility(View.GONE);

                //初始化编辑框的值
                et_validate_name.setText("");
                et_user_name.setText("");
                et_new_psw.setText("");
                et_new_psw_again.setText("");
                clickCount = 0;     //初始化点击计时器

                //手机号和验证码输入此时可见
                tv_validate_phone.setVisibility(View.VISIBLE);
                smsLayout.setVisibility(View.VISIBLE);
                smsLayout.validateVisible();
            }
        });

        if ("security".equals(from)) {
            titleLayout.setText("设置密保").setColorTransparent();
        } else {
            titleLayout.setText("找回密码").setColorTransparent();
            tv_user_name.setVisibility(View.VISIBLE);
            et_user_name.setVisibility(View.VISIBLE);
            ver_switch.setVisibility(View.VISIBLE);
        }

        //密保验证的点击事件
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validateName = et_validate_name.getText().toString().trim();
                if ("security".equals(from)) {
                    //设置密保
                    if (TextUtils.isEmpty(validateName)) {
                        Toast.makeText(FindPswActivity.this, "请输入要验证的姓名", Toast.LENGTH_SHORT).show();
                    }else if(validateName.contains(" ")){
                        Toast.makeText(FindPswActivity.this, "密保不能包含空格", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FindPswActivity.this, "密保设置成功", Toast.LENGTH_SHORT).show();
                        //保存密保到SharedPreferences
                        AnalysisUtils.saveSecurity(FindPswActivity.this,validateName);
                        FindPswActivity.this.finish();
                    }
                } else {
                    //找回密码
                    String userName = et_user_name.getText().toString().trim();
                    String sp_security = readSecurity(userName);
                    if(clickCount<2) {  //记录的点击次数最多只能为2
                        clickCount++;
                    }
                    if (TextUtils.isEmpty(userName)) {
                        Toast.makeText(FindPswActivity.this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                    } else if (!AnalysisUtils.isExistUserName(FindPswActivity.this,userName)) {
                        Toast.makeText(FindPswActivity.this, "您输入的用户名不存在", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(validateName)) {
                        Toast.makeText(FindPswActivity.this, "请输入要验证的姓名", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(sp_security)) {
                        Toast.makeText(FindPswActivity.this, "该用户未设置密保", Toast.LENGTH_SHORT).show();
                    } else if (!validateName.equals(sp_security)) {
                        Toast.makeText(FindPswActivity.this, "输入的密保不正确", Toast.LENGTH_SHORT).show();
                    } else if(clickCount==1){
                           //点击次数为1时显示新密码编辑框
                        tv_new_psw.setVisibility(View.VISIBLE);
                        ver_psw.setVisibility(View.VISIBLE);

                        //验证成功后后就禁用用户名和姓名的编辑框
                        et_user_name.setEnabled(false);
                        et_validate_name.setEnabled(false);

                    }
                    //新密码编辑框显示后，再次点击（即点击次数为1时）才判断新密码是否为空
                    else if (clickCount==2) {
                        //输入的密保正确，重新给用户设置一个密码
                        String newPsw = et_new_psw.getText().toString().trim();
                        String newPswAgain = et_new_psw_again.getText().toString().trim();

                        if (TextUtils.isEmpty(newPsw)) {
                            Toast.makeText(FindPswActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                            et_new_psw.requestFocus(); // 聚焦到新密码输入框
                        }else if(newPsw.length() < 8){
                            Toast.makeText(FindPswActivity.this, "新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                        }else if(newPsw.length() > 16) {
                            Toast.makeText(FindPswActivity.this, "新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                        }else if(newPsw.contains(" ")){
                            Toast.makeText(FindPswActivity.this, "新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(newPswAgain)) {
                            Toast.makeText(FindPswActivity.this, "再次输入的新密码不能为空", Toast.LENGTH_SHORT).show();
                            et_new_psw_again.requestFocus(); // 聚焦到再次输入新密码输入框
                        }else if(newPswAgain.length() < 8){
                            Toast.makeText(FindPswActivity.this, "再次输入的新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                        }else if(newPswAgain.length() > 16) {
                            Toast.makeText(FindPswActivity.this, "再次输入的新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                        }else if(newPswAgain.contains(" ")){
                            Toast.makeText(FindPswActivity.this, "再次输入的新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                        } else if (!newPsw.equals(newPswAgain)) {
                            Toast.makeText(FindPswActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindPswActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
                            clickCount = 0;
                            //修改登录成功时保存在SharedPreferences中的密码
                            modifyPsw(newPsw, userName);

                            LoginActivity.actionStart(FindPswActivity.this);
                            FindPswActivity.this.finish();
                        }
                    }
                }
            }
        });
    }

    /**
     * 密保验证通过时修改保存在SharedPreferences中的密码
     */
    private void modifyPsw(String newPsw,String userName){
        String md5Psw= MD5Utils.md5(newPsw);//把密码用MD5加密
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);

        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //遍历users的所有JSONObject对象，查找具有“username”属性的JSONObject对象，修改对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("username").equals(userName)) {
                        // 找到要修改密码的用户，将其密码修改为新密码
                        user.put("password", md5Psw);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        // 将修改后的JSONArray数据保存回SharedPreferences中
        editor.putString("user_list", users.toString());
        editor.apply();  //提交修改
    }

    /**
     * 手机验证通过时修改保存在SharedPreferences中的密码
     */
    private void smsModifyPsw(String newPsw, String phone){
        String md5Psw= MD5Utils.md5(newPsw);//把密码用MD5加密
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);

        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //遍历users的所有JSONObject对象，查找具有“phone_number”属性的JSONObject对象，修改对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("phone_number").equals(phone)) {
                        // 找到要修改密码的用户，将其密码修改为新密码
                        user.put("password", md5Psw);
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        // 将修改后的JSONArray数据保存回SharedPreferences中
        editor.putString("user_list", users.toString());
        editor.apply();  //提交修改
    }

    /**
     * 从SharedPreferences中读取密保
     */
    private String readSecurity(String userName) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        String savedSecurity = null;
        try {
            //将获取到的字符串解析为一个JSONArray对象。
            JSONArray users = new JSONArray(usersString);
            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    try {
                        JSONObject user = users.getJSONObject(i);
                        if (user.getString("username").equals(userName)) {
                            savedSecurity = user.getString(userName + "_security");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedSecurity;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();       //TODO 获取当前点击的view的id
        switch (id){
            case R.id.btn_validate_sms2:
                //输入的密保正确，重新给用户设置一个密码
                String smsPhone = smsLayout.getPhoneString();
                String newPsw = et_new_psw_sms.getText().toString().trim();
                String newPswAgain = et_new_psw_again_sms.getText().toString().trim();
                if (TextUtils.isEmpty(newPsw)) {
                    Toast.makeText(FindPswActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    et_new_psw_sms.requestFocus(); // 聚焦到新密码输入框
                }else if(newPsw.length() < 8){
                    Toast.makeText(FindPswActivity.this, "新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                }else if(newPsw.length() > 16) {
                    Toast.makeText(FindPswActivity.this, "新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                }else if(newPsw.contains(" ")){
                    Toast.makeText(FindPswActivity.this, "新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(newPswAgain)) {
                    Toast.makeText(FindPswActivity.this, "再次输入的新密码不能为空", Toast.LENGTH_SHORT).show();
                    et_new_psw_again_sms.requestFocus(); // 聚焦到再次输入新密码输入框
                }else if(newPswAgain.length() < 8){
                    Toast.makeText(FindPswActivity.this, "再次输入的新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                }else if(newPswAgain.length() > 16) {
                    Toast.makeText(FindPswActivity.this, "再次输入的新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                }else if(newPswAgain.contains(" ")){
                    Toast.makeText(FindPswActivity.this, "再次输入的新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                } else if (!newPsw.equals(newPswAgain)) {
                    Toast.makeText(FindPswActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindPswActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
                    //修改登录成功时保存在SharedPreferences中的密码
                    smsModifyPsw(newPsw, smsPhone);

                    LoginActivity.actionStart(FindPswActivity.this);
                    FindPswActivity.this.finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        smsLayout.onDestroy();
    }
}