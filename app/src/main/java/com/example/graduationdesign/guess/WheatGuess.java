package com.example.graduationdesign.guess;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationdesign.guess.GranaryGuessStrategy;
import com.example.graduationdesign.bean.GuessBean;
import com.example.graduationdesign.dialog.GuessDialog;
import com.example.graduationdesign.guess.TotalWaterAndImpurity;
import com.example.graduationdesign.utils.LogUtils;
import com.example.graduationdesign.utils.MyApplication;

//小麦测算策略类
public class WheatGuess implements GranaryGuessStrategy {
    //等级，单价文本
    String grade,unit_price;
    //单价，水分标准，不完善颗粒率标准
    double unitprice,waterStandard,incompleteParticleRate;
    //是否显示对话课
    Boolean showDialog = false;
    //重量，水分，杂质率
    private double dweight,dwater,dimpurity;
    //小麦的不完善颗粒率，容重
    private double dwheat_guess1,dwheat_guess2;
    private String wheat_guess1,wheat_guess2;
    private double total;

    public WheatGuess(EditText et_wheat_guess1,EditText et_wheat_guess2){
        wheat_guess1 = et_wheat_guess1.getText().toString().trim();
        wheat_guess2 = et_wheat_guess2.getText().toString().trim();
    }

    @Override
    public boolean ifIsEmpty() {
        if( TextUtils.isEmpty(wheat_guess1) || TextUtils.isEmpty(wheat_guess2)){
            return true;
        }
        return false;
    }

    @Override
    public void getDouble(String weight,String water,String impurity) {
        dweight = Double.parseDouble(weight);
        dwater = Double.parseDouble(water);
        dimpurity = Double.parseDouble(impurity);

        dwheat_guess1 = Double.parseDouble(wheat_guess1);
        dwheat_guess2 = Double.parseDouble(wheat_guess2);
    }

    //判断并且测算小麦等级
    @Override
    public void judgeLevel() {
        waterStandard = 12.5;
        showDialog = true;
        if(dwheat_guess2 >=790){
            grade = "等级: 一等";
            unit_price = "单价: 1.22元/斤";
            unitprice = 1.22;
            incompleteParticleRate = 6.0;
        }else if(dwheat_guess2 >=770){
            grade = "等级: 二等";
            unit_price = "单价: 1.20元/斤";
            unitprice = 1.20;
            incompleteParticleRate = 6.0;
        }else if(dwheat_guess2 >=750){
            grade = "等级: 三等";
            unit_price = "单价: 1.18元/斤";
            unitprice = 1.18;
            incompleteParticleRate = 8.0;
        }else if(dwheat_guess2 >=730){
            grade = "等级: 四等";
            unit_price = "单价: 1.16元/斤";
            unitprice = 1.16;
            incompleteParticleRate = 8.0;
        }else if(dwheat_guess2 >=710){
            grade = "等级: 五等";
            unit_price = "单价: 1.14元/斤";
            unitprice = 1.14;
            incompleteParticleRate = 10.0;
        }else if(dwheat_guess2 <710){
            showDialog = false;
            Toast.makeText(MyApplication.getContext(),"容重必须>=710,请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //小麦增重和扣重的计算方法
    @Override
    public void getTotal() {
        if(dwheat_guess2 < 710){
            showDialog = false;
        }else {
            GuessBean bean = TotalWaterAndImpurity.getWaterAndImpurity(dwater,dweight,dimpurity,waterStandard);

            if (dwheat_guess1 < incompleteParticleRate) {
                // 不完善粒率低于标准规定的，不增量
                bean.add = bean.add + 0;
            } else{
                if((dwheat_guess1 - incompleteParticleRate) % 1 == 0){
                    // 不完善粒率每高1%扣量0.5%
                    double decreasePercent = (dwheat_guess1 - incompleteParticleRate) * 0.5;
                    bean.sub = bean.sub + decreasePercent * dweight * 0.01;
                }else{
                    //高于不足1%的部分，不计增量
                    double result = dwheat_guess1 - incompleteParticleRate;
                    int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                    double increasePercent = integerPart * 0.5;
                    bean.sub = bean.sub + increasePercent * dweight * 0.01;
                }
            }
            LogUtils.i("不完善", String.valueOf(bean.add));
            LogUtils.i("不完善", String.valueOf(bean.sub));
            bean.total = bean.add - bean.sub;
            LogUtils.i("总共", String.valueOf(bean.total));
            total = bean.total;
        }

    }

    //待定mContext
    @Override
    public void setDialog(Context mContext,boolean Switch) {
        if (showDialog = false){
            return;
        }else {
            GuessDialog.setGuessDialog(mContext,dweight,total,grade,unit_price,unitprice,Switch);
            showDialog = true;
        }
    }
}
