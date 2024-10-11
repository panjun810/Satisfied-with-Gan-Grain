package com.example.graduationdesign.adapter;

import android.os.Bundle;
import android.os.Handler;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.graduationdesign.bean.AdBean;
import com.example.graduationdesign.customControl.AdBannerFragment;
import com.example.graduationdesign.view.HomeView;

import java.util.ArrayList;
import java.util.List;

//TODO 对ViewPager控件进行数据适配
// 将CourseBean 的数据集合转化为广告轮播图所需要的 Fragment 集合，同时也可以响应触摸事件并通知 Handler
public class AdBannerAdapter extends FragmentStatePagerAdapter implements
		OnTouchListener {
	private Handler mHandler;
	private List<AdBean> adl;
	public AdBannerAdapter(FragmentManager fm) {  //无参数构造函数
		super(fm);
		adl = new ArrayList<AdBean>();   //创建一个空的 AdBean 集合
	}
	public AdBannerAdapter(FragmentManager fm, Handler handler) {  //带 Handler 参数的构造函数
		super(fm);
		mHandler = handler;   //Handler 赋值给类的成员变量
		adl = new ArrayList<AdBean>();    //创建一个空的 AdBean 集合
	}
	/**
	 *  设置数据更新界面(设置广告数据并通知数据集已变化)
	 */
	public void setDatas(List<AdBean> adl) {
		this.adl = adl;
		//用于更新数据的方法，它会重新加载适配器中所有的数据项，让它们显示在最新的列表中
		notifyDataSetChanged();
	}
	@Override
	public Fragment getItem(int index) {   //返回指定位置的 Fragment
		Bundle args = new Bundle();
		if (adl.size() > 0)  //如果数据集不为空
			//TODO index % cadl.size()的作用是实现循环滚动，
			// 因为getItem()方法返回的是一个Fragment实例，而广告图片可能有多张，
			// 因此需要根据当前位置index取模cadl.size()的结果来确定应该显示哪一张广告图片

			/*
		    因为 AdBannerAdapter 的 getCount 方法返回了 Integer.MAX_VALUE，所以 getItem 方法中 index 的值可能非常大，
		    而 adl.size() 表示 adl 列表的元素个数，取余数操作可以让 index 的值在 0 到 adl.size()-1 的范围内循环变化，从而实现循环轮播*/
			args.putString("ad", adl.get(index % adl.size()).icon);   //将广告图片的名称放入 Bundle 对象中
		return AdBannerFragment.newInstance(args);  //再通过 AdBannerFragment 的静态工厂方法创建并返回一个新的 AdBannerFragment 对象
	}
	@Override
	public int getCount() {     //返回无限大的整数，用于实现广告轮播图的循环播放
		return Integer.MAX_VALUE;
	}
	/**
	 * 返回数据集的真实容量大小
	 */
	public int getSize() {
		return adl == null ? 0 : adl.size();
	}
	@Override
	public int getItemPosition(Object object) {
		// 防止刷新结果显示列表的时候出现缓存数据,重载这个函数 使之默认返回POSITION_NONE
		return POSITION_NONE;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {   //重载此方法，响应触摸事件并移除 Handler 中的延迟消息
		mHandler.removeMessages(HomeView.MSG_AD_SLID);
		return false;
	}
}