package com.example.graduationdesign.activity.myslview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.activity.FindPswActivity;
import com.example.graduationdesign.activity.myslview.setting.ModifyPswActivity;
import com.example.graduationdesign.activity.myslview.setting.PhoneChangeActivity;
import com.example.graduationdesign.activity.myslview.setting.SecurityChangeActivity;
import com.example.graduationdesign.dialog.CommonDialog;
import com.example.graduationdesign.utils.AnalysisUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends BaseActivity {
    private RelativeLayout rl_modify_psw,rl_security_setting;
    private RelativeLayout rl_exit_login,rl_security_change,rl_phone_change;
    //声明一个静态变量，其不需要实例化类就可以被访问
    public static SettingActivity instance = null;   //如果在应用程序的其他地方需要访问SettingActivity的实例，可以使用这个静态变量。

    public static void actionStartForResult(Activity activity, int requestCode){
        Intent intent=new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        instance=this;   //将当前实例赋值给instance静态变量，以便在其他地方可以访问这个实例
        init();
    }
    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.setting_title);
        titleLayout.setText("设置");

        rl_modify_psw=(RelativeLayout) findViewById(R.id.rl_modify_psw);
        rl_security_setting=(RelativeLayout) findViewById(R.id.rl_security_setting);
        rl_exit_login=(RelativeLayout) findViewById(R.id.rl_exit_login);

        rl_security_change=(RelativeLayout) findViewById(R.id.rl_security_change);
        rl_phone_change=(RelativeLayout) findViewById(R.id. rl_phone_change);

        //修改密码的点击事件
        rl_modify_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改密码的界面
                ModifyPswActivity.actionStart(SettingActivity.this);
            }
        });
        //设置密保的点击事件
        rl_security_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExistSecurity()) {
                    Toast.makeText(SettingActivity.this, "该用户设置过密保", Toast.LENGTH_SHORT).show();
                }else {
                    //跳转到设置密保的界面
                    FindPswActivity.actionStart(SettingActivity.this,"security");
                }

            }
        });
        //修改密保的点击事件
        rl_security_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExistSecurity()) {
                    Toast.makeText(SettingActivity.this, "该用户未设置过密保", Toast.LENGTH_SHORT).show();
                }else {
                    //跳转到修改密保的界面
                    SecurityChangeActivity.actionStart(SettingActivity.this);
                }
            }
        });
        //换绑手机的点击事件
        rl_phone_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExistSecurity()) {
                    Toast.makeText(SettingActivity.this, "该用户未设置过密保", Toast.LENGTH_SHORT).show();
                }else {
                    //跳转到换绑手机的界面
                    PhoneChangeActivity.actionStart(SettingActivity.this);
                }
            }
        });

        //退出登录的点击事件
        rl_exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommonDialog dialog = new CommonDialog(SettingActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确定要退出登录吗？");
                dialog.setNegtive("取消");
                dialog.setPositive("确定");
                dialog.setOnClickBottomListener(new CommonDialog.
                        OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() { //确定按钮的点击事件
                        dialog.dismiss();
                        Toast.makeText(SettingActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                        clearLoginStatus();//清除登录状态和登录时的用户名
                        //退出登录成功后把退出成功的状态传递到MainActivity中
                        Intent data =new Intent();
                        data.putExtra("isLogin", false);
                        setResult(RESULT_OK, data);
                        SettingActivity.this.finish();
                    }
                    @Override
                    public void onNegtiveClick() { //取消按钮的点击事件
                        dialog.dismiss();
                    }  //取消按钮的点击事件
                });
                dialog.show();

            }
        });
    }
    /**
     * 清除SharedPreferences中的登录状态和登录时的用户名
     */
    private void clearLoginStatus(){
        SharedPreferences sp=getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//获取编辑器
        editor.putBoolean("isLogin", false);
        editor.putString("loginUserName", "");
        editor.commit();//提交修改
    }


    //判断登录用户是否设置过密保
    public boolean isExistSecurity() {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");
        try {
            //将获取到的字符串解析为一个JSONArray对象。
            JSONArray users = new JSONArray(usersString);
            //遍历JSONArray对象中的所有JSONObject对象，查找具有“username”属性的JSONObject对象，该属性的值等于给定用户名。
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("username").equals(AnalysisUtils.readLoginUserName(this))) {
                    if (!TextUtils.isEmpty(user.getString(AnalysisUtils.readLoginUserName(this) + "_security"))) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
