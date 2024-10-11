package com.example.graduationdesign.activity.myslview.setting.userinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.bean.UserBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_phone, tv_address, tv_user_name, tv_sex,tv_status;
    private RelativeLayout rl_phone, rl_sex, rl_address,
            rl_title_bar,rl_status,rl_head;
    private static final int CHANGE_PHONE = 1;//修改电话的自定义常量
    private static final int CHANGE_ADDRESS = 2;//修改地址的自定义常量

    private String spUserName,spPhone;

    private ImageView iv_head_icon;
    private static final int CHANGE_ICON = 3;//修改头像的自定义常量

    public static void actionStartForResult(Activity activity, int requestCode){
        Intent intent=new Intent(activity, UserInfoActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //从SharedPreferences中获取登录时的用户名
        spUserName = AnalysisUtils.readLoginUserName(this);

        //从SharedPreferences中获取登录用户绑定的手机号
        spPhone = AnalysisUtils.readPhone(this);

        init();
        initData();
        setListener();
    }
    /**
     * 初始化控件
     */
    private void init() {
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.user_info_title);
        titleLayout.setText("个人资料");

        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_status = (RelativeLayout) findViewById(R.id.rl_status);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_status =  (TextView) findViewById(R.id.tv_status);
        tv_address = (TextView) findViewById(R.id.tv_address);

        iv_head_icon = (ImageView) findViewById(R.id.iv_head_icon);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
    }
    /**
     * 获取数据
     */
    private void initData() {
        UserBean bean = null;
        bean = DBUtils.getInstance(this).getUserInfo(spUserName);
        // 首先判断一下数据库是否有数据，若无数据则为用户设置默认数据
        if (bean == null) {
            bean = new UserBean();
            bean.iconPath=null;
            bean.userName=spUserName;
            bean.phone=spPhone;
            bean.sex="男";
            bean.status="普通市民";
            bean.address="东华理工大学广兰校区";
            //保存用户信息到数据库
            DBUtils.getInstance(this).saveUserInfo(bean);
        }
        setValue(bean);
    }
    /**
     * 为界面控件设置值
     */
    private void setValue(UserBean bean) {
        if(bean.iconPath == null) {
            iv_head_icon.setImageResource(R.drawable.calender);
        }else{
            Bitmap bitmap = BitmapFactory.decodeFile(bean.iconPath);
            iv_head_icon.setImageBitmap(bitmap);
        }
        tv_phone.setText(bean.phone);
        tv_user_name.setText(bean.userName);
        tv_sex.setText(bean.sex);
        tv_status.setText(bean.status);
        tv_address.setText(bean.address);
    }
    /**
     * 设置控件的点击监听事件
     */
    private void setListener() {
        rl_phone.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_status.setOnClickListener(this);
        rl_head.setOnClickListener(this);
    }
    /**
     * 控件的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_phone://电话的点击事件
                String phone = tv_phone.getText().toString();//获取电话控件上的数据
                Bundle bdPhone = new Bundle();
                bdPhone.putString("content", phone);//传递界面上的电话数据
                bdPhone.putString("title", "电话");
                bdPhone.putInt("flag", 1);//flag传递1时表示是修改电话
                enterActivityForResult(UserInfoChangeActivity.class,CHANGE_PHONE, bdPhone);//跳转到个人资料修改界面
                break;
            case R.id.rl_sex://性别的点击事件
                String sex = tv_sex.getText().toString();//获取性别控件上的数据
                sexDialog(sex);
                break;
            case R.id.rl_status://身份的点击事件
                String status = tv_status.getText().toString();//获取身份控件上的数据
                statusDialog(status);
                break;
            case R.id.rl_address://地址的点击事件
                String address = tv_address.getText().toString();//获取地址控件上的数据
                Bundle bdAddress = new Bundle();
                bdAddress.putString("content", address);//传递界面上的地址数据
                bdAddress.putString("title", "地址");
                bdAddress.putInt("flag", 2);//flag传递2时表示是修改地址
                enterActivityForResult(UserInfoChangeActivity.class, CHANGE_ADDRESS, bdAddress);//跳转到个人资料修改界面
                break;
            case R.id.rl_head://头像的点击事件
                Bundle bdIcon = new Bundle();
                bdIcon.putString("title", "头像");
                enterActivityForResult(HeadIconChangeActivity.class, CHANGE_ICON, bdIcon);//跳转到个人资料修改界面
                break;
            default:
                break;
        }
    }
    /**
     * 设置性别的弹出框
     */
    private void sexDialog(String sex){
        int sexFlag=0;
        if("男".equals(sex)){
            sexFlag=0;
        }else if("女".equals(sex)){
            sexFlag=1;
        }
        final String items[]={"男","女"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.CustomDialogTheme);  //先得到构造器
        builder.setTitle("性别"); //设置标题
        //参数1表示单选列表中的所有选项数据，参数2表示单选列表中的默认选择角标，参数3表示单选列表的监听事件
        builder.setSingleChoiceItems(items,sexFlag,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//第二个参数是点击时被选中的项目
                dialog.dismiss();     //关闭对话框
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();
                setSex(items[which]);
            }
        });
        builder.create().show();    //显示对话框
    }
    /**
     * 更新界面上的性别数据
     */
    private void setSex(String sex){
        tv_sex.setText(sex);
        // 更新数据库中的性别字段
        DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("sex",
                sex, spUserName);
    }

    /**
     * 设置身份的弹出框
     */
    private void statusDialog(String status){
        int statusFlag=0;
        if("普通市民".equals(status)){
            statusFlag=0;
        }else if("普通农户".equals(status)){
            statusFlag=1;
        }else if("种植大户（含经纪人）".equals(status)){
            statusFlag=2;
        }else if("加工企业".equals(status)){
            statusFlag=3;
        }else if("其他企业".equals(status)){
            statusFlag=4;
        }
        final String items[]={"普通市民","普通农户","种植大户（含经纪人）","加工企业","其他企业"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.CustomDialogTheme);  //先得到构造器
        builder.setTitle("身份"); //设置标题
        //参数1表示单选列表中的所有选项数据，参数2表示单选列表中的默认选择角标，参数3表示单选列表的监听事件
        builder.setSingleChoiceItems(items,statusFlag,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//第二个参数是点击时被选中的项目
                dialog.dismiss();     //关闭对话框
                Toast.makeText(UserInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();
                setStatus(items[which]);
            }
        });
        builder.create().show();    //显示对话框
    }
    /**
     * 更新界面上的身份数据
     */
    private void setStatus(String status){
        tv_status.setText(status);
        // 更新数据库中的身份字段
        DBUtils.getInstance(UserInfoActivity.this).updateUserInfo("status",
                status, spUserName);
    }
    /**
     * 回传数据
     */
    private String new_info;//最新数据
    @Override      //注解，表示这个方法是重写父类或接口中的方法。
    //接收回传的数据，并根据传递的参数requestCode,resultCode来识别数据的来源
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //根据请求码判断情况
        switch (requestCode) {
            case CHANGE_PHONE://个人资料修改界面回传过来的电话数据
                if (data != null) {
                    new_info = data.getStringExtra("phone");
                    if (TextUtils.isEmpty(new_info) || new_info == null) {
                        return;
                    }
                    tv_phone.setText(new_info);
                    // 更新数据库中的电话字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo(
                            "phone", new_info, spUserName);
                }
                break;
            case CHANGE_ADDRESS://个人资料修改界面回传过来的地址数据
                if (data != null) {
                    new_info = data.getStringExtra("address");
                    if (TextUtils.isEmpty(new_info) || new_info == null) {
                        return;
                    }
                    tv_address.setText(new_info);
                    // 更新数据库中的地址字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo(
                            "address", new_info, spUserName);
                }
                break;
            case CHANGE_ICON://头像修改界面回传过来的地址数据
                if (data != null) {
                    new_info = data.getStringExtra("IconPath");
                    if (TextUtils.isEmpty(new_info) || new_info == null) {
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(new_info);
                    iv_head_icon.setImageBitmap(bitmap);
                    // 更新数据库中的地址字段
                    DBUtils.getInstance(UserInfoActivity.this).updateUserInfo(
                            "IconPath", new_info, spUserName);

                    Intent intent = new Intent();
                    intent.putExtra("isLogin",AnalysisUtils.readLoginStatus(this));
                    //intent.putExtra("isLoginUser",readLoginStatus());
                    intent.putExtra("IconPath",new_info);
                    setResult(RESULT_OK,intent);

                }
                break;
        }
    }
    /**
     * TODO Class<?> 是一个泛型类型的语法，用于表示任何类型的类对象，也称为通配符类型参数。<?> 表示这个通配符可以匹配任何类型的类，包括原始类型、类类型、接口类型和数组类型等。
     * 获取回传数据时需使用的跳转方法，
     * 第一个参数to表示需要跳转到的界面，第二个参数requestCode表示一个请求码，第三个参数b表示跳转时传递的数据
     */
    public void enterActivityForResult(Class<?> to, int requestCode, Bundle b) {
        Intent i = new Intent(this, to);
        i.putExtras(b);
        startActivityForResult(i, requestCode);
    }


}
