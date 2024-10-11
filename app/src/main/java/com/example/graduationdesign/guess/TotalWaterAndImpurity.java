package com.example.graduationdesign.guess;

import android.util.Log;

import com.example.graduationdesign.bean.GuessBean;
import com.example.graduationdesign.utils.LogUtils;

public class TotalWaterAndImpurity {
    public static GuessBean getWaterAndImpurity(double dwater, double dweight, double dimpurity, double waterStandards){

        LogUtils.i("粮食水分", String.valueOf(dweight));
        LogUtils.i("粮食重量", String.valueOf(dwater));
        LogUtils.i("粮食杂质", String.valueOf(dimpurity));
        LogUtils.i("粮食水标准", String.valueOf(waterStandards));
        GuessBean bean = new GuessBean();
        //低于或者高于不足0.5%，不计增量
        if (dwater < waterStandards) {
            // 水分每低0.5%增量0.75%。但低于标准规定指标2.5%及以上时，不再增量。
            if (dwater >= (waterStandards - 2.5)) {
                if((waterStandards - dwater) % 0.5 == 0){
                    double decreasePercent = (waterStandards - dwater) / 0.5 * 0.75;
                    bean.add = bean.add + decreasePercent * dweight * 0.01;
                }else{
                    //低于不足0.5%的部分，不计增量
                    double result = (waterStandards - dwater) / 0.5;
                    int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                    double decreasePercent = integerPart * 0.75;
                    bean.add = bean.add + decreasePercent * dweight * 0.01;
                }
            }else {
                double decreasePercent = 2.5 / 0.5 * 0.75;
                bean.add = bean.add + decreasePercent * dweight * 0.01;
            }
        } else if (dwater > waterStandards) {
            if((dwater - waterStandards) % 0.5 == 0){
                // 水分每高0.5%扣量1%
                double increasePercent = (dwater - waterStandards) / 0.5 * 1.0;
                bean.sub = bean.sub + increasePercent * dweight * 0.01;
            }else{
                //低于不足0.5%的部分，不计增量
                double result = (dwater - waterStandards) / 0.5;
                int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                double increasePercent = integerPart * 1.0;
                bean.sub = bean.sub + increasePercent * dweight * 0.01;
            }
        }

        Log.i("粮食bean.add1", String.valueOf(bean.add));
        Log.i("粮食bean.sub1", String.valueOf(bean.sub));

        //低于或者高于不足0.5%，不计增量
        if (dimpurity < 1.0) {
            // 杂质率每低0.5%增量0.75%
            if((1.0 - dimpurity) % 0.5 == 0){
                double decreasePercent = (1.0 - dimpurity) / 0.5 * 0.75;
                bean.add = bean.add + decreasePercent * dweight * 0.01;
            }else{
                //低于不足0.5%的部分，不计增量
                double result = (1.0 - dimpurity) / 0.5;
                int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                double decreasePercent = integerPart * 0.75;
                bean.add = bean.add + decreasePercent * dweight * 0.01;
            }
        } else if (dimpurity > 1.0) {
            // 杂质率每高0.5%扣量1.5%
            if((dimpurity - 1.0) % 0.5 == 0){
                double increasePercent = (dimpurity - 1.0) / 0.5 * 1.5;
                bean.sub = bean.sub + increasePercent * dweight * 0.01;
            }else{
                //高于不足0.5%的部分，不计增量
                double result = (dimpurity - 1.0) / 0.5;
                int integerPart = (int) result;  // 使用类型转换将浮点数值转换为整数部分，取整操作
                double increasePercent = integerPart * 1.5;
                bean.sub = bean.sub + increasePercent * dweight * 0.01;
            }
        }

        Log.i("粮食bean.add2", String.valueOf(bean.add));
        Log.i("粮食bean.sub2", String.valueOf(bean.sub));

        return bean;
    }
}
