package com.example.graduationdesign.activity.myslview;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;
import com.example.graduationdesign.dialog.SavePhotoDialog;

public class ContactActivity extends BaseActivity {
    private TextView tv_hotline;
    private ImageView ewm1,ewm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取界面控件
     */
    private void init(){
        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.contact_title);
        titleLayout.setText("联系方式");

        tv_hotline=(TextView) findViewById(R.id.tv_hotline);

        String hotlineInfo = "宜春市发改委                       0795—3274151\n\n"
                + "吉安市农业农村局                0796—8235195\n\n"
                + "赣州市农业农村局                0797—8196302\n\n"
                + "上饶市农业农村局                0793—8337220\n\n"
                + "抚州市发改委                       0794—8223939\n\n"
                + "南昌市农业农村局                0791—83986351\n\n"
                + "九江市农业农村局                0792-8584764\n\n"
                + "景德镇市农业农村局            0798—8335067\n\n"
                + "萍乡市农业农村局                0799—7191536\n\n"
                + "新余市粮食局                       0790—6442237\n\n"
                + "鹰潭市农业农村粮食局        0701—6222036";

        tv_hotline.setText(hotlineInfo);

        ewm1 = (ImageView) findViewById(R.id.ewm1);
        ewm2 = (ImageView) findViewById(R.id.ewm2);

        //长按二维码可以保存二维码
        SavePhotoDialog.setupLongClickToSaveImage(this, ewm1);
        SavePhotoDialog.setupLongClickToSaveImage(this, ewm2);
    }
}
