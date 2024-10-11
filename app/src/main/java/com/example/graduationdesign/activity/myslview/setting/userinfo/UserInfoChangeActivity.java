package com.example.graduationdesign.activity.myslview.setting.userinfo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.TitleLayout;
import com.example.graduationdesign.activity.BaseActivity;

public class UserInfoChangeActivity extends BaseActivity {
    private TextView tv_save;
    private String title, content;
    private int flag;               //flag为1时表示修改昵称，为2时表示修改地址
    private EditText et_content;
    private ImageView iv_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    private void init() {
        // 从个人资料界面传递过来的标题和内容
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        flag = getIntent().getIntExtra("flag", 0);

        TitleLayout titleLayout = (TitleLayout) findViewById(R.id.userinfo_change_title);
        titleLayout.setText(title);

        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.VISIBLE);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        if (!TextUtils.isEmpty(content)) {
            et_content.setText(content);
            et_content.setSelection(content.length());   //将光标移动到末尾
        }
        contentListener();

        //"删除"按钮的单击事件
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
            }
        });
        //"保存"按钮的单击事件
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                String etContent = et_content.getText().toString().trim();
                switch (flag) {
                    //保存修改后的电话
                    case 1:
                        if (!TextUtils.isEmpty(etContent)) {
                            data.putExtra("phone", etContent);
                            //携带数据进行回传，参数1为resultCode返回码，用于标识返回的数据来自哪个Activity
                            //参数2为intent表示用于携带数据返回上一个界面
                            setResult(RESULT_OK, data);
                            Toast.makeText(UserInfoChangeActivity.this, "保存成功",
                                    Toast.LENGTH_SHORT).show();
                            UserInfoChangeActivity.this.finish();
                        } else {
                            Toast.makeText(UserInfoChangeActivity.this, "电话不能为空",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    //保存修改后的地址
                    case 2:
                        if (!TextUtils.isEmpty(etContent)) {
                            data.putExtra("address", etContent);
                            setResult(RESULT_OK, data);
                            Toast.makeText(UserInfoChangeActivity.this, "保存成功",
                                    Toast.LENGTH_SHORT).show();
                            UserInfoChangeActivity.this.finish();
                        } else {
                            Toast.makeText(UserInfoChangeActivity.this, "地址不能为空",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }
    /**
     * 监听个人资料修改界面输入的文字
     */
    private void contentListener() {
        et_content.addTextChangedListener(new TextWatcher() {      //设置了一个文本变化的监听器对象,
            //当文本内容发生变化时会回调这个方法。
            // 参数 s 是变化后的文本内容，参数 start 是文本变化的起始位置，参数 before 是变化前的文本长度，参数 count 是新增的文本长度
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_content.getText();     //获取文本对象
                int len = editable.length();                //输入的文本的长度
                //删除图标的可见性
                if (len > 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
                switch (flag) {
                    case 1:// 电话
                        // 电话限制最多11个字，超过11个需要截取掉多余的字
                        if (len > 11) {
                            int selEndIndex = Selection.getSelectionEnd(editable);  //获取用户在文本输入框中当前选中文本的结束位置
                            String str = editable.toString();//将 Editable 对象转换成 String 对象。
                            // 截取新字符串
                            String newStr = str.substring(0, 11); //对字符串进行截取操作，第一个参数为截取的起始位置，第二个为截取长度
                            et_content.setText(newStr);
                            editable = et_content.getText();
                            // 新字符串的长度
                            int newLen = editable.length();
                            // 旧光标位置超过新字符串长度
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            // 设置新光标所在的位置
                            //第一个参数是 Editable 对象，即 EditText 中的文本内容，第二个参数是 int 类型的 index，表示要将光标放置到第几个字符后面
                            Selection.setSelection(editable, selEndIndex);
                        }
                        break;
                    case 2:// 地址
                        // 地址最多是32个字，超过32个字需要截取掉多余的字
                        if (len > 32) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            // 截取新字符串
                            String newStr = str.substring(0, 32);
                            et_content.setText(newStr);
                            editable = et_content.getText();
                            // 新字符串的长度
                            int newLen = editable.length();
                            // 旧光标位置超过字符串长度
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            // 设置新光标所在的位置
                            Selection.setSelection(editable, selEndIndex);
                        }
                        break;
                    default:
                        break;
                }
            }
            //文本变化前的回调方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            //文本变化后的回调方法
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
}