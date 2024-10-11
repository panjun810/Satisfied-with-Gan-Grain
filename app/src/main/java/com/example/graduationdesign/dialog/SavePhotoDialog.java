package com.example.graduationdesign.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduationdesign.dialog.CommonDialog;
import com.example.graduationdesign.utils.MyApplication;

public class SavePhotoDialog {
    public static void setupLongClickToSaveImage(final Context context, final ImageView imageView) {
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final CommonDialog dialog = new CommonDialog(MyApplication.getContext());
                dialog.setTitle("提示");
                dialog.setMessage("您确定要保存图片吗？");
                dialog.setNegtive("取消");
                dialog.setPositive("确定");
                dialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {      //确定按钮的点击事件

                        // 将 ImageView 中的图片复制到 Bitmap 对象中
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        // 保存图片到本地相册
                        /*TODO 使用 MediaStore.Images.Media.insertImage() 方法将 Bitmap 对象保存到本地相册。
                           该方法会将指定的 Bitmap 对象保存到相册中，并返回保存后的图片的 URI。在该方法中，
                           第一个参数 getContentResolver() 表示获取应用的内容解析器，用于操作相册；
                           第二个参数是要保存的 Bitmap 对象；
                           第三个参数是图片的标题，可以自己定义；第四个参数是图片的描述，也可以自己定义。*/
                        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "null", "null");
                        Toast.makeText(MyApplication.getContext(), "图片已保存到相册", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    @Override
                    public void onNegtiveClick() {      //取消按钮的点击事件
                        dialog.dismiss();   //关闭对话框
                    }
                });
                dialog.show();    //显示对话框
                return true;
            }
        });
    }
}
