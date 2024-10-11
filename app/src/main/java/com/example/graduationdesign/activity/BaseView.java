package com.example.graduationdesign.activity;

import android.view.KeyEvent;
import android.widget.Toast;

import com.example.graduationdesign.utils.ActivityCollector;

public class BaseView extends BaseActivity{

    protected long exitTime;//记录第一次点击时的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(this,"再按一次退出满意赣粮",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            }
        }else {
            ActivityCollector.finishAll();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
