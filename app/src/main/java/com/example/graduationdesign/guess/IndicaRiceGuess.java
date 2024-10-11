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

public class IndicaRiceGuess implements GranaryGuessStrategy {
    //等级，单价文本
    String grade,unit_price;
    //单价，水分标准，不完善颗粒率标准
    double unitprice,waterStandard,headRiceRateStandard;
    //是否显示对话课
    Boolean showDialog = false;
    //重量，水分，杂质率
    private double dweight,dwater,dimpurity;
    //籼稻的整精米率，出糙率，谷外糙米率，黄粒米
    private double dindica_rice_guess1,dindica_rice_guess2,dindica_rice_guess3,dindica_rice_guess4;
    private String indica_rice_guess1,indica_rice_guess2,indica_rice_guess3,indica_rice_guess4;
    private double total;

    public IndicaRiceGuess(EditText et_indica_rice_guess1,EditText et_indica_rice_guess2,EditText et_indica_rice_guess3,EditText et_indica_rice_guess4){
        indica_rice_guess1 = et_indica_rice_guess1.getText().toString().trim();
        indica_rice_guess2 = et_indica_rice_guess2.getText().toString().trim();
        indica_rice_guess3 = et_indica_rice_guess3.getText().toString().trim();
        indica_rice_guess4 = et_indica_rice_guess4.getText().toString().trim();
    }

    @Override
    public boolean ifIsEmpty() {
        if(TextUtils.isEmpty(indica_rice_guess1) || TextUtils.isEmpty(indica_rice_guess2)
                || TextUtils.isEmpty(indica_rice_guess3) || TextUtils.isEmpty(indica_rice_guess4)){
            return true;
        }
        return false;
    }

    @Override
    public void getDouble(String weight, String water, String impurity) {
        dweight = Double.parseDouble(weight);
        dwater = Double.parseDouble(water);
        dimpurity = Double.parseDouble(impurity);

        dindica_rice_guess1 = Double.parseDouble(indica_rice_guess1);
        dindica_rice_guess2 = Double.parseDouble(indica_rice_guess2);
        dindica_rice_guess3 = Double.parseDouble(indica_rice_guess3);
        dindica_rice_guess4 = Double.parseDouble(indica_rice_guess4);
    }

    //判断并且测算籼稻等级
    @Override
    public void judgeLevel() {
        waterStandard = 13.5;
        showDialog = true;
        if(dindica_rice_guess2 >=79.0){
            grade = "等级: 一等";
            unit_price = "单价: 1.3元/斤";
            unitprice = 1.3;

            headRiceRateStandard = 50.0;

        }else if(dindica_rice_guess2 >=77.0){
            grade = "等级: 二等";
            unit_price = "单价: 1.28元/斤";
            unitprice = 1.28;

            headRiceRateStandard = 47.0;

        }else if(dindica_rice_guess2 >=75.0){
            grade = "等级: 三等";
            unit_price = "单价: 1.26元/斤";
            unitprice = 1.26;

            headRiceRateStandard = 44.0;

        }else if(dindica_rice_guess2 >=73.0){
            grade = "等级: 四等";
            unit_price = "单价: 1.24元/斤";
            unitprice = 1.24;

            headRiceRateStandard = 41.0;

        }else if(dindica_rice_guess2 >=71.0){
            grade = "等级: 五等";
            unit_price = "单价: 1.22元/斤";
            unitprice = 1.22;

            headRiceRateStandard = 38.0;

        }else if(dindica_rice_guess2 <71.0){
            showDialog = false;
            Toast.makeText(MyApplication.getContext(),"出糙率必须>=71.0,请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //籼稻增重和扣重的计算方法
    @Override
    public void getTotal() {
        if(dindica_rice_guess2 < 71.0){
            showDialog = false;
        }else {
            GuessBean bean = TotalWaterAndImpurity.getWaterAndImpurity(dwater,dweight,dimpurity,waterStandard);

            LogUtils.i("籼稻水", String.valueOf(bean.add));
            LogUtils.i("籼稻水", String.valueOf(bean.sub));

            if(dindica_rice_guess1 > headRiceRateStandard){
                //整精米率增加对增量无影响
                bean.add = bean.add + 0;
            }else{
                //整精米率每-1%扣量0.75%
                if((headRiceRateStandard - dindica_rice_guess1) % 1 == 0){
                    double decreasePercent = (headRiceRateStandard - dindica_rice_guess1) * 0.75;
                    bean.sub = bean.sub + decreasePercent * dweight * 0.01;
                }else{
                    //低不足1%的，不扣量
                    double result = headRiceRateStandard - dindica_rice_guess1;
                    int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                    double increasePercent = integerPart * 0.75;
                    bean.sub = bean.sub + increasePercent * dweight * 0.01;
                }
            }
            LogUtils.i("整精米率", String.valueOf(bean.add));
            LogUtils.i("整精米率", String.valueOf(bean.sub));

            if(dindica_rice_guess3 <= 2.0){
                //谷外糙米减少对增量无影响
                bean.add = bean.add + 0;
            }else{
                //谷外糙米每+2%扣量1%，只+1%时扣量0%
                double remainder = (dindica_rice_guess3 - 2.0) % 2;
                double divisor = (dindica_rice_guess3 - 2.0) / 2;
                if(remainder == 0){
                    bean.sub = bean.sub + divisor * dweight * 0.01;
                }else{
                    int integerPart = (int) divisor;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                    bean.sub = bean.sub + integerPart * dweight * 0.01;
                }
            }
            LogUtils.i("谷外糙米", String.valueOf(bean.add));
            LogUtils.i("谷外糙米", String.valueOf(bean.sub));

            if(dindica_rice_guess4 <= 1.0){
                //黄粒米减少对增量无影响
                bean.add = bean.add + 0;
            }else{
                //黄粒米每+1%扣量1%
                if((dindica_rice_guess4 - 1.0) %1 ==0){
                    bean.sub = bean.sub + (dindica_rice_guess4 - 1.0) * dweight * 0.01;
                }else{
                    double result = dindica_rice_guess4 - 1.0;
                    int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                    bean.sub = bean.sub + integerPart * dweight * 0.01;
                }
            }
            LogUtils.i("黄粒米", String.valueOf(bean.add));
            LogUtils.i("黄粒米", String.valueOf(bean.sub));

            bean.total = bean.add - bean.sub;
            total = bean.total;
        }

    }

    @Override
    public void setDialog(Context mContext, boolean Switch) {
        if (showDialog = false){
            return;
        }else {
            GuessDialog.setGuessDialog(mContext,dweight,total,grade,unit_price,unitprice,Switch);
            showDialog = true;
        }
    }
}
