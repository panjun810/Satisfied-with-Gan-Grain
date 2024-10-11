package com.example.graduationdesign.customControl;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.graduationdesign.R;

// TODO 设置ViewPager控件中的数据
public class AdBannerFragment extends Fragment {
	private String ab;// 广告
	private ImageView iv;// 图片
	public static AdBannerFragment newInstance(Bundle args) {
		AdBannerFragment af = new AdBannerFragment();
		af.setArguments(args);
		return af;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {  //Fragment创建时被调用
		super.onCreate(savedInstanceState);
		//TODO getArguments() 是 Fragment 类的一个方法，用于获取在创建 Fragment 时传入的参数 Bundle。
		// 在创建 Fragment 时，可以通过 setArguments(Bundle bundle) 方法将参数传递给 Fragment。
		// 在 Fragment 中，可以通过 getArguments() 方法获取传递进来的参数 Bundle
		Bundle arg = getArguments();
		// 获取广告图片名称
		ab = arg.getString("ad");

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {   //在Activity创建完成后被调用，这里没有做任何操作
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume() {   //Fragment处于可见状态时被调用,用于设置广告图片的显示
		super.onResume();
        //根据ab的值来设置图片的资源ID
		if (ab != null) {
			if ("banner_1".equals(ab)) {
				iv.setImageResource(R.drawable.banner_1);
			} else if ("banner_2".equals(ab)) {
				iv.setImageResource(R.drawable.banner_2);
			} else if ("banner_3".equals(ab)) {
				iv.setImageResource(R.drawable.banner_3);
			}

		}
	}

	@Override
	public void onDestroy() {  //Fragment销毁时被调用
		super.onDestroy();
		//将ImageView的图片设置为null，以便释放内存
		if (iv != null) {
			iv.setImageDrawable(null);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {   //Fragment创建视图(加载布局)时被调用
		// 创建广告图片控件
		iv = new ImageView(getActivity());

		//TODO ViewGroup.LayoutParams是一个布局参数类，用于控制布局中ViewGroup中的子视图（View）的位置、尺寸和权重等属性
		//创建一个宽度(参数1)和高度(参数2)都填满了它的父控件的布局参数对象
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		// 设置图片（view）的布局参数
		iv.setLayoutParams(lp);

		/*设置ImageView中显示图片的缩放类型
		  FIT_XY表示图片会被拉伸或压缩来填充整个ImageView，这可能导致图片的宽高比例变形*/
		iv.setScaleType(ImageView.ScaleType.FIT_XY);// 把图片塞满整个控件
		return iv;
	}
}