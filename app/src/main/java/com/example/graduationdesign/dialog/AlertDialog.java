package com.example.graduationdesign.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.utils.MyApplication;

public class AlertDialog extends android.app.AlertDialog {
    private TextView titleTv ;               //显示的标题
    private TextView tv_grade,tv_unit_price,tv_gross_weight, tv_weight,tv_net_weight,tv_total_price,tv_prompt;             //显示的消息
    private Button negtiveBn ,positiveBn;  //确认和取消按钮

    private String grade,unit_price,gross_weight, weight,net_weight,total_price,prompt;
    private Boolean Switch;
    private String title;
    private String positive,negtive;

    public AlertDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_dialog);
        initView();     //初始化界面控件
        initEvent();    //初始化界面控件的点击事件
    }

    //初始化界面控件
    private void initView() {
        negtiveBn = (Button) findViewById(R.id.negtive);
        positiveBn = (Button) findViewById(R.id.positive);
        titleTv = (TextView) findViewById(R.id.title);
        tv_grade = (TextView) findViewById(R.id.grade);
        tv_unit_price = (TextView) findViewById(R.id.unit_price);
        tv_gross_weight = (TextView) findViewById(R.id.gross_weight);
        tv_weight = (TextView) findViewById(R.id.weight);

        tv_net_weight = (TextView) findViewById(R.id.net_weight);
        tv_total_price = (TextView) findViewById(R.id.total_price);

        tv_prompt = (TextView) findViewById(R.id.prompt);
    }

    //初始化界面的确定和取消监听器
    private void initEvent() {
        //设置确定按钮的点击事件的监听器
        //如果注册了 onClickBottomListener 监听器，则调用 onPositiveClick() 方法，否则不执行任何操作
        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener!= null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置取消按钮的点击事件的监听器
        negtiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    //自定义接口
    public interface OnClickBottomListener{
        void onPositiveClick();//实现确定按钮点击事件的方法
        void onNegtiveClick(); //实现取消按钮点击事件的方法
    }

    //设置确定取消按钮的回调
    public AlertDialog.OnClickBottomListener onClickBottomListener;
    public AlertDialog setOnClickBottomListener(AlertDialog.OnClickBottomListener
                                                         onClickBottomListener){
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public AlertDialog setGrade(String grade) {
        this.grade = grade;
        return this ;
    }
    public AlertDialog setUnitPrice(String unit_price) {
        this.unit_price = unit_price;
        return this ;
    }
    public AlertDialog setGrossWeight(String gross_weight) {
        this.gross_weight = gross_weight;
        return this ;
    }
    public AlertDialog setWeight(String weight) {
        this.weight = weight;
        return this ;
    }

    public AlertDialog setNetWeight(String net_weight) {
        this.net_weight = net_weight;
        return this ;
    }
    public AlertDialog setTotalPrice(String total_price) {
        this.total_price = total_price;
        return this ;
    }

    public AlertDialog setPrompt(String prompt) {
        this.prompt = prompt;
        return this ;
    }

    public AlertDialog setSwitch(Boolean Switch) {
        this.Switch = Switch;
        return this ;
    }

/*    public void Visibility(Boolean Switch){
        if(Switch == true){
            tv_total_price.setVisibility(View.GONE);
        }else {
            tv_total_price.setVisibility(View.VISIBLE);
        }
    }*/

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    //初始化界面控件的显示数据
    private void refreshView() {
        //设置消息控件的文本为自定义的文本信息
        if (!TextUtils.isEmpty(grade)) {
            tv_grade.setText(grade);
        }

        if (!TextUtils.isEmpty(unit_price)) {
            tv_unit_price.setText(unit_price);
        }

        if (!TextUtils.isEmpty(gross_weight)) {
            tv_gross_weight.setText(gross_weight);
        }

        if (!TextUtils.isEmpty(weight)) {
            tv_weight.setText(weight);
        }

        if (!TextUtils.isEmpty(net_weight)) {
            tv_net_weight.setText(net_weight);
        }

        if (!TextUtils.isEmpty(total_price)) {
            tv_total_price.setText(total_price);
        }

        if(Switch == true){
            tv_total_price.setVisibility(View.GONE);
        }else {
            tv_total_price.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(prompt)) {
            tv_prompt.setText(prompt);
        }

    }
}
