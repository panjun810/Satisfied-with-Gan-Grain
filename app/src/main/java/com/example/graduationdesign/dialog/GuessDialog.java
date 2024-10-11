package com.example.graduationdesign.dialog;

import android.content.Context;
import android.widget.Toast;

import com.example.graduationdesign.dialog.AlertDialog;
import com.example.graduationdesign.utils.LogUtils;

import java.math.BigDecimal;

public class GuessDialog {
    //设置测算对话框内显示的信息
    public static void setGuessDialog(Context mContext, double dweight, double total,
                                      String grade,String unit_price,double unitprice,boolean Switch){
        double guessweight,netweight;
        String prompt,gweight,net_weight,total_price;

        String gross_weight = "毛重: "+ dweight + "斤";

        if(total <= 0){
            gweight = "扣量: "+ total  + "斤";
            guessweight = total;
            //净重=毛重-扣量
            netweight = dweight + guessweight;
            net_weight = "净重: " + netweight + "斤";
        }else {
            gweight = "增量: " + total + "斤";
            guessweight = total;
            //净重=毛重-扣量
            netweight = dweight + guessweight;
            net_weight = "净重: " + netweight + "斤";
        }

        BigDecimal bdUnitPrice = BigDecimal.valueOf(unitprice);
        BigDecimal bdNetWeight = BigDecimal.valueOf(netweight);
        //TODO BigDecimal的使用方式和普通的浮点数运算略有不同，需要使用特定的方法进行运算，如multiply()方法用于乘法运算。
        BigDecimal bdTotalPrice = bdUnitPrice.multiply(bdNetWeight);
        total_price = "总价: " + bdTotalPrice  + "元";

        LogUtils.i("结果单价", String.valueOf(unitprice));
        LogUtils.i("结果净重", String.valueOf(netweight));
        LogUtils.i("结果总价", String.valueOf(bdTotalPrice));

        // 判断bdTotalPrice是否小于0
        //TODO 在使用BigDecimal进行比较时，可以使用compareTo()方法来比较两个BigDecimal对象的大小关系。该方法返回一个整数值，表示比较结果。
        // 如果compareTo()方法返回值小于0，则bdTotalPrice小于0。
        // eg：bdTotalPrice为131.95000000000002，compareTo()方法返回值为131.95000000000002
        if(bdTotalPrice.compareTo(BigDecimal.ZERO) <=0){
            Toast.makeText(mContext,"农民朋友你好，你的粮食未达到国家规定的最低收购标准！",Toast.LENGTH_SHORT).show();
            return;
        }else{
            final AlertDialog dialog = new AlertDialog(mContext);
            dialog.setGrade(grade);
            dialog.setUnitPrice(unit_price);

            dialog.setGrossWeight(gross_weight);
            dialog.setWeight(gweight);
            dialog.setNetWeight(net_weight);

            //托市粮测算若没达到国家三级标志需要隐藏总价，并且提示
            if(Switch == true){
                dialog.setTotalPrice(total_price);
                if(grade.equals("等级: 一等") || grade.equals("等级: 二等") || grade.equals("等级: 三等")){
                    dialog.setSwitch(false);
                    prompt = "本价格测算仅作参考，以实际收购价格为准";
                    dialog.setPrompt(prompt);
                }else if(grade.equals("等级: 四等") || grade.equals("等级: 五等")){
                    dialog.setSwitch(true);
                    prompt = "没有达到国家三等质量标准";
                    dialog.setPrompt(prompt);
                }
            }else{
                dialog.setSwitch(false);
                dialog.setTotalPrice(total_price);
                prompt = "本价格测算仅作参考，以实际收购价格为准";
                dialog.setPrompt(prompt);
            }

            dialog.setOnClickBottomListener(new AlertDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
