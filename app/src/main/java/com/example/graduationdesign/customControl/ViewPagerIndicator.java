package com.example.graduationdesign.customControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.graduationdesign.R;

public class ViewPagerIndicator extends LinearLayout {
    private int mCount;//小圆点的个数
    private int mIndex;//当前小圆点的位置
    private Context context;//保存上下文对象
    public ViewPagerIndicator(Context context) {
        this(context, null);    //调用另一个构造函数，传递上下文参数和一个空的属性集合参数
    }
    //构造函数，接收一个上下文（Context）参数和一个属性集合（AttributeSet）参数。
    // AttributeSet 是一个接口，用于表示 XML 中声明的 View 的属性集合
    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setGravity(Gravity.CENTER);//设置此布局居中,设置此布局的子视图（ViewPager 指示器）在父布局中居中显示
    }
    /**
     *设置滑动到当前小圆点时其他圆点的位置
     */
    public void setCurrentPosition(int currentIndex) {
        mIndex = currentIndex;//当前小圆点
        removeAllViews();//移除界面上存在的view
        int pex = 5;   //表示小圆点的padding值
        for (int i = 0; i < mCount; i++) {
            //创建一个ImageView控件来放置小圆点
            ImageView imageView = new ImageView(context);
                if (mIndex == i) {//滑动到的当前界面
                    //设置小圆点的图片为蓝色图片
                    imageView.setImageResource(R.drawable.indicator_on);
                }else {
                    //设置小圆点的图片为灰色图片
                    imageView.setImageResource(R.drawable.indicator_off);
                }
                imageView.setPadding(pex, 0, pex, 0);//设置小圆点图片的上下左右的padding
                addView(imageView);//把此小圆点添加到自定义的ViewPagerIndicator控件上
        }
    }
    /**
     * 设置小圆点的数目
     */
    public void setCount(int count) {
        this.mCount = count;
    }
}