
package com.example.graduationdesign.PopupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.graduationdesign.R;

/**
 * PopupWindow 工具类
 */
public class PhotoPopupWindow extends PopupWindow {
    private View mView; // PopupWindow 菜单布局
    private Context mContext; // 上下文参数
    private View.OnClickListener mSelectListener; // 相册选取的点击监听器
    private View.OnClickListener mCaptureListener; // 拍照的点击监听器

    public PhotoPopupWindow(Activity context, View.OnClickListener selectListener, View.OnClickListener captureListener) {
        super(context);
        this.mContext = context;
        this.mSelectListener = selectListener;
        this.mCaptureListener = captureListener;
        Init();
    }

    /**
     * 设置布局以及点击事件
     */
    private void Init() {
        //调用 Context 的 getSystemService() 方法获取一个 LayoutInflater 对象。LayoutInflater 是 Android 中用于加载布局资源文件并创建对应的 View 对象的工具类
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //从布局资源文件 R.layout.pop_item 中创建一个 View 对象，并将其赋值给类的成员变量 mView。第二个参数为 null，表示将 mView 添加到父容器时不使用父容器的布局参数
        mView = inflater.inflate(R.layout.pop_item, null);
        Button btn_camera = mView.findViewById(R.id.icon_btn_camera);
        Button btn_select = mView.findViewById(R.id.icon_btn_select);
        Button btn_cancel = mView.findViewById(R.id.icon_btn_cancel);

        btn_select.setOnClickListener(mSelectListener);
        btn_camera.setOnClickListener(mCaptureListener);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 导入布局
        this.setContentView(mView);
        // 设置动画效果
        this.setAnimationStyle(R.style.popwindow_anim_style);
        //设置 PopupWindow 的宽度和高度,TODO MATCH_PARENT，即充满父容器; WRAP_CONTENT，即根据内容自动调整高度
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置 PopupWindow 可以获得焦点。当设置为 true 时，用户可以通过点击或触摸 PopupWindow 以外的区域来关闭 PopupWindow
        this.setFocusable(true);
        //创建了一个 ColorDrawable 对象 dw，其构造函数参数为 0x00000000。这个参数表示一个完全透明的颜色，即背景颜色为透明
        ColorDrawable dw = new ColorDrawable(0x0000000);
        //设置 PopupWindow 的背景。
        this.setBackgroundDrawable(dw);
        // 单击弹出窗以外处 关闭弹出窗
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取mView 中 id 为 R.id.ll_pop 的 View 的顶部位置(PopupWindow的顶部位置)
                int height = mView.findViewById(R.id.ll_pop).getTop();
                //获取触摸事件的 Y 坐标
                int y = (int) event.getY();
                //TODO MotionEvent.ACTION_UP为屏幕被弹起（手指或者触控笔离开屏幕时触发的事件）
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
