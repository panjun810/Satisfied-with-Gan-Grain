package com.example.graduationdesign.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.graduationdesign.activity.RegisterActivity;
import com.example.graduationdesign.dialog.CommonDialog;

public class VisitorDialog {
    public static void setupClickToVisitDialog(final Activity context) {
                final CommonDialog dialog = new CommonDialog(context);
                dialog.setTitle("提示");
                dialog.setMessage("您目前的身份是游客，无法继续本操作，注册后可以正常使用");
                dialog.setNegtive("去注册");
                dialog.setPositive("以后再说");
                dialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {      //以后再说按钮的点击事件
                        dialog.dismiss();   //关闭对话框
                    }
                    @Override
                    public void onNegtiveClick() {      //去注册按钮的点击事件
                        Intent intent = new Intent(context, RegisterActivity.class);  //跳转到注册界面
                        intent.putExtra("Flag","other");
                        context.startActivity(intent);
                        context.finish();
                        dialog.dismiss();
                    }
                });
                dialog.show();    //显示对话框
    }
}