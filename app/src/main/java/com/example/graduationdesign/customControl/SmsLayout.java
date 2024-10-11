package com.example.graduationdesign.customControl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.FindPswActivity;
import com.example.graduationdesign.activity.LoginActivity;
import com.example.graduationdesign.activity.RegisterActivity;
import com.example.graduationdesign.activity.myslview.setting.PhoneChangeActivity;
import com.example.graduationdesign.activity.myslview.setting.SecurityChangeActivity;
import com.example.graduationdesign.utils.LogUtils;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SmsLayout extends LinearLayout implements View.OnClickListener {
    private EditText et_phone,et_sms;
    private Button btn_sms;
    private Button btn_login_sms;
    private LinearLayout ll_sms,linearlayout_validate_sms;

    private Handler handler;
    private EventHandler eventHandler;
    private Timer timer;
    private int count = 60;

    private SmsCallback callback;
    private Context context;
    private String Msg;

    public SmsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setMsg();
        this.context = getContext();
        if((Activity)context instanceof LoginActivity || (Activity)context instanceof RegisterActivity){
            LayoutInflater.from(context).inflate(R.layout.sms_item_style_one,this);
            et_phone=(EditText) findViewById(R.id.et_phone);
            et_sms = (EditText) findViewById(R.id.et_sms);

        }else if((Activity)context instanceof SecurityChangeActivity || (Activity)context instanceof PhoneChangeActivity
                || (Activity)context instanceof FindPswActivity){
            LayoutInflater.from(context).inflate(R.layout.sms_item_style_two,this);
            et_phone = (EditText) findViewById(R.id.et_validate_phone);
            et_sms = (EditText) findViewById(R.id.et_validate_sms);
            ll_sms = (LinearLayout) findViewById(R.id.ll_sms);
            linearlayout_validate_sms = (LinearLayout) findViewById(R.id.linearlayout_validate_sms);
        }
        btn_sms = (Button) findViewById(R.id.btn_sms);
        btn_login_sms =(Button) findViewById(R.id.btn_login_sms);

        /*TODO 向MobSDK服务提交用户隐私政策授权结果
         第一个参数是一个boolean值，表示用户是否同意隐私政策。如果用户同意了隐私政策，则传递true，否则传递false。
         第二个参数是一个字符串类型的参数，表示用户拒绝隐私政策时的拒绝理由。如果用户同意了隐私政策，那么该参数可以为null。*/
        MobSDK.submitPolicyGrantResult(true,null);
        initHandler(getContext());
        init();

        btn_sms.setOnClickListener(this);
        btn_login_sms.setOnClickListener(this);
    }

    public void setSMSCallback(SmsCallback callback) {
        this.callback = callback;
    }

    public String getPhoneString(){
        String phone = et_phone.getText().toString().trim();
        return phone;
    }

    //设置按钮文本
    public SmsLayout setButtonText(String text){
        btn_login_sms.setText(text);
        return this;
    }

    //获取手机号输入框控件
    public EditText getEt_phone(){
        return et_phone;
    }

    public EditText getEt_sms(){return et_sms;}

    //验证操作结束后隐藏部分控件
    public void validateGone(){
        //手机验证按钮隐藏
        btn_login_sms.setVisibility(View.GONE);

        //使手机验证相关的控件无法获取点击（防止用户再次操作）
        et_phone.setEnabled(false);
        //隐藏输入验证码的编辑栏和获取验证码按钮
        ll_sms.setVisibility(View.GONE);
    }

    //验证操作结束后显示部分控件
    public void validateVisible(){
        //手机验证按钮显示
        btn_login_sms.setVisibility(View.VISIBLE);

        //使手机验证相关的控件能获取点击
        et_phone.setEnabled(true);
        //显示输入验证码的编辑栏和获取验证码按钮
        ll_sms.setVisibility(View.VISIBLE);
    }

    //切换验证方式时重置验证码按钮
    public void reSms(){
        btn_sms.setText("获取验证码");
        count = 60;
        if(timer != null){
            timer.cancel();
        }
        btn_sms.setEnabled(true);
    }

    //设置手机验证成功后的提示信息
    private void setMsg(){
        if((Activity)context instanceof LoginActivity ){
            Msg = "登录成功！";
        }else if((Activity)context instanceof RegisterActivity){
            Msg = "注册成功！";
        }else if((Activity)context instanceof SecurityChangeActivity || (Activity)context instanceof FindPswActivity){
            Msg = "验证成功！";
        }else if((Activity)context instanceof PhoneChangeActivity){
            Msg = "换绑成功！";
        }
    }


    @Override
    public void onClick(View view) {
        String phone = et_phone.getText().toString().trim();
        String code = et_sms.getText().toString().trim();
        switch (view.getId()){
            case R.id.btn_sms:
                //TODO 此处为获取验证码按钮的点击事件
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(context, "请输入手机号码",Toast.LENGTH_LONG).show();
                }else if(phone.length() != 11){
                    Toast.makeText(context, "手机号位数必须为11位", Toast.LENGTH_SHORT).show();
                }else if(phone.contains(" ")){
                    Toast.makeText(context, "手机号中不能包含空格", Toast.LENGTH_SHORT).show();
                }else if(callback.smsOnClick()){
                    //判断内容在接口回调处，可拓展判断条件
                }else {
                    CountdownStart();
                    getVerificationCode("86", phone);
                }
                break;
            case R.id.btn_login_sms:
                if (callback.submitOnClickOne()){
                    //判断内容在接口回调处，可拓展判断条件
                }else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(context, "请输入手机号码",Toast.LENGTH_LONG).show();
                }else if(phone.length() != 11){
                    Toast.makeText(context, "手机号位数必须为11位", Toast.LENGTH_SHORT).show();
                }else if(phone.contains(" ")){
                    Toast.makeText(context, "手机号中不能包含空格", Toast.LENGTH_SHORT).show();
                }else if(callback.submitOnClickTwo()){
                    //判断内容在接口回调处，可拓展判断条件
                }else if (TextUtils.isEmpty(code)){
                    Toast.makeText(context, "请输入验证码",Toast.LENGTH_LONG).show();
                }else{//登录逻辑
                    /**
                     * cn.smssdk.SMSSDK.class
                     * 提交验证码
                     * @param country   国家区号
                     * @param phone     手机号
                     * @param code      验证码
                     */
                    /*TODO 根据 SMSSDK 的文档，submitVerificationCode 函数用于提交验证码并验证，如果验证码正确，会触发 eventHandler 对象的回调事件（有三种）
                       onRegister(String country, String phone): 该函数会在用户通过短信验证码注册成功后触发，参数中包含用户注册时所使用的国家和手机号码。
                       beforeEvent(int event, Object data): 该函数会在 SMSSDK 执行一次操作前触发，参数中包含操作的类型（event）和操作相关的数据（data）。
                       afterEvent(int event, int result, Object data): 该函数会在 SMSSDK 执行完一次操作后触发，参数中包含操作的类型（event）、操作结果（result）和操作相关的数据（data）
                       此处触发的是afterEvent
                     */
                    SMSSDK.submitVerificationCode("86",phone,code);//提交验证码

                }
                break;
        }

    }

    public interface SmsCallback{
        boolean smsOnClick();
        boolean submitOnClickOne();
        boolean submitOnClickTwo();
        void validateSuccessful();
    }

    private void initHandler(Context context) {
        //TODO 在主线程中创建Handle对象时，系统就默认存在Looper对象（无需手动创建），用于处理不同信息
        //Looper.getMainLooper()方法获取了UI线程的Looper对象，确保Handler会在UI线程中处理消息
        handler = new Handler(Looper.getMainLooper()){
            public void handleMessage(Message msg) {
                //获取消息的标志值
                int tag = msg.what;
                switch (tag){
                    //为1时计时器结束。此时根据传入的arg1字段判断是否需要恢复计时器，并将计数器值重置为60。同时，取消计时器并使获取验证码按钮可用。
                    case 1:
                        int arg = msg.arg1;
                        if(arg==1){
                            //计时结束停止计时把值恢复 TODO （即更新UI线程中的btn_sms控件，下面同理）
                            btn_sms.setText("重新获取");
                            //计时结束停止计时把值恢复
                            count = 60;
                            timer.cancel();
                            btn_sms.setEnabled(true);
                        }else{
                            //TODO 此处count+""这种将变量的值和一个空字符串连接起来，形成一个新的字符串的用法称为字符串拼接。这项可以让setText里的字符串成为变量
                            btn_sms.setText(count+""+"s");
                        }
                        break;
                    //为2时，表示获取短信验证码成功。此时显示一个Toast提示。
                    case 2:
                        Toast.makeText(context,"获取短信验证码成功",Toast.LENGTH_LONG).show();
                        break;
                    //为3时，表示获取短信验证码失败。此时将失败的提示信息通过Toast显示出来
                    case 3:
                        LogUtils.i("Code","获取短信验证码失败");
                        Toast.makeText(context,msg.getData().getString("code"),Toast.LENGTH_LONG).show();
                        break;
                    //为4时，表示需要显示一个提示信息。此时将传入的提示信息通过Toast显示出来 TODO 此处为输入的验证码不正确
                    case 4:
                        Toast.makeText(context,msg.getData().getString("code"),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,"输入的验证码不正确！",Toast.LENGTH_LONG).show();
                        break;
                    //为5时，表示需要显示一个提示信息。此时将传入的提示信息通过Toast显示出来 TODO 此处为登录成功
                    case 5:
                        Toast.makeText(context,msg.getData().getString("code"),Toast.LENGTH_LONG).show();
                        //Toast.makeText(MainActivity.this,"登录成功！",Toast.LENGTH_LONG).show();

                        callback.validateSuccessful();
                        break;

                    default:
                        break;
                }
            };
        };
    }

    private void init() {
        MobSDK.submitPolicyGrantResult(true);

        //EventHandler是一个接口，此处是当收到服务器回应时的回调函数
        //todo EventHandler对象是SMSSDK特有的。它是SMSSDK提供的一个事件处理类，用于处理与短信验证相关的事件
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) { // TODO 此处为子线程！不可直接处理UI线程！处理后续操作需传到主线程中操作！
                LogUtils.i("返回:",event+" | "+result+" | "+data.toString());

                /*TODO result：SMSSDK.RESULT_COMPLETE表示操作成功，SMSSDK.RESULT_ERROR表示操作失败
                       event==SMSSDK.EVENT_GET_VERTIFICATION_CODE获取验证码操作的回调（向服务器请求验证码后收到服务器回应时调用）
                       event==SMSSDK.EVENT_SUBMIT_VERTIFICATION_CODE提交验证码操作的回调（收到验证处理结果时回调）*/

                //成功回调
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Bundle bundle = new Bundle();
                    Message message = new Message();
                    //提交短信、语音验证码成功
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //验证码验证通过
                        /*bundle.putString("code","登录成功！");*/
                        bundle.putString("code",Msg);
                        message.what = 5;
                        message.setData(bundle);
                        //将消息发送到主线程，以在UI上显示“登录成功”信息
                        handler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //服务器答应发短信(成功收到验证码)
                        message.what = 2;
                        handler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                        //获取语音验证码成功
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    Bundle bundle = new Bundle();
                    Message message = new Message();
                    if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){     //验证码校验失败
                        bundle.putString("code","输入的验证码不正确！");
                        message.what = 4;
                    }else{         //服务器拒绝发短信(获取验证码失败)
                        bundle.putString("code",data.toString());
                        message.what = 3;
                    }
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    //其他失败回调
                    ((Throwable) data).printStackTrace();
                }
            }
        };

        SMSSDK.registerEventHandler(eventHandler);  //注册短信回调
    }

    /**
     * cn.smssdk.SMSSDK.class
     * 请求文本验证码
     * @param country   国家区号
     * @param phone     手机号
     */
    public void getVerificationCode(String country, String phone) {
        SMSSDK.getVerificationCode(country, phone);
    }

    //倒计时函数
    private void CountdownStart() {
        //禁用获取验证码的按钮
        btn_sms.setEnabled(false);
        timer = new Timer();
        /*TODO timer.schedule(new TimerTask() { ... }, 1000, 1000)：使用计时器对象 timer 执行定时任务，
           即在 1000 毫秒（即 1 秒）之后开始执行，并且每隔 1000 毫秒（即 1 秒）执行一次。*/
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                message.arg1 = count;
                if (count != 0) {
                    count--;
                } else {
                    cancel();
                    return;
                }
                handler.sendMessage(message);
            }
        }, 1000, 1000);
    }

    public void onDestroy() {
        // 使用完EventHandler需注销，否则可能出现内存泄漏
        SMSSDK.unregisterEventHandler(eventHandler);
        if (timer != null) {
            timer.cancel();
        }
    }

    public void onResume(){
        SMSSDK.registerEventHandler(eventHandler);
    }
}
