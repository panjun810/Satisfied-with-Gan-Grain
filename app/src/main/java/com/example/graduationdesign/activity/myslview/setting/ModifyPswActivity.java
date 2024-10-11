package com.example.graduationdesign.activity.myslview.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.activity.LoginActivity;
import com.example.graduationdesign.activity.myslview.SettingActivity;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.MD5Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModifyPswActivity extends BaseActivity {
    private EditText et_original_psw,et_new_psw,et_new_psw_again;
    private Button btn_save;
    private String originalPsw,newPsw,newPswAgain;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        userName= AnalysisUtils.readLoginUserName(this);
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,ModifyPswActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件并处理相关控件的点击事件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.modify_psw_title);
        titleLayout.setText("修改密码");

        et_original_psw=(EditText) findViewById(R.id.et_original_psw);
        et_new_psw=(EditText) findViewById(R.id.et_new_psw);
        et_new_psw_again=(EditText) findViewById(R.id.et_new_psw_again);
        btn_save=(Button) findViewById(R.id.btn_save);

        //保存按钮的点击事件
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                if (TextUtils.isEmpty(originalPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "请输入原始密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!MD5Utils.md5(originalPsw).equals(AnalysisUtils.readPsw(ModifyPswActivity.this,userName))) {
                    Toast.makeText(ModifyPswActivity.this, "输入的密码与原始密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if(MD5Utils.md5(newPsw).equals(AnalysisUtils.readPsw(ModifyPswActivity.this,userName))){
                    Toast.makeText(ModifyPswActivity.this, "输入的新密码与原始密码不能一致", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPsw)) {
                    Toast.makeText(ModifyPswActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPsw.length() < 8){
                    Toast.makeText(ModifyPswActivity.this, "新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPsw.length() > 16) {
                    Toast.makeText(ModifyPswActivity.this, "新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPsw.contains(" ")){
                    Toast.makeText(ModifyPswActivity.this, "新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newPswAgain)) {
                    Toast.makeText(ModifyPswActivity.this, "再次输入的新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPswAgain.length() < 8){
                    Toast.makeText(ModifyPswActivity.this, "再次输入的新密码长度至少为8位", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPswAgain.length() > 16) {
                    Toast.makeText(ModifyPswActivity.this, "再次输入的新密码长度不能超过16位", Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPswAgain.contains(" ")){
                    Toast.makeText(ModifyPswActivity.this, "再次输入的新密码中不能包含空格", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newPsw.equals(newPswAgain)) {
                    Toast.makeText(ModifyPswActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(ModifyPswActivity.this, "新密码设置成功", Toast.LENGTH_SHORT).show();
                    //修改登录成功时保存在SharedPreferences中的密码
                    modifyPsw(newPsw,userName);

                    LoginActivity.actionStart(ModifyPswActivity.this);
                    SettingActivity.instance.finish();
                    ModifyPswActivity.this.finish();
                }
            }
        });
    }
    /**
     * 获取控件上的字符串
     */
    private void getEditString(){
        originalPsw=et_original_psw.getText().toString().trim();
        newPsw=et_new_psw.getText().toString().trim();
        newPswAgain=et_new_psw_again.getText().toString().trim();
    }
    /**
     * 修改登录成功时保存在SharedPreferences中的密码
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
}

