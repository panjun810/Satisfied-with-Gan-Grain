package com.example.graduationdesign.view;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.graduationdesign.R;
import com.example.graduationdesign.customControl.ViewPagerIndicator;
import com.example.graduationdesign.activity.homeview.HeadlineActivity;
import com.example.graduationdesign.adapter.AdBannerAdapter;
import com.example.graduationdesign.adapter.HomeAdapter;
import com.example.graduationdesign.bean.HomeBean;
import com.example.graduationdesign.bean.AdBean;
import com.example.graduationdesign.utils.AnalysisUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeView {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private View mCurrentView;
    private ViewPager adPager;// 广告
    private View adBannerLay;// 广告条容器
    private AdBannerAdapter ada;// 适配器
    public static final int MSG_AD_SLID = 002;// 广告自动滑动
    private ViewPagerIndicator vpi;// 小圆点
    private MHandler mHandler;// 事件捕获

    private List<AdBean> adl;
    private LinearLayout more;
    private ListView lv_list;
    private HomeAdapter adapter;
    private List<HomeBean> hbl;

    private RelativeLayout rl_zjbz,rl_sgzc,rl_nyqx;

    public HomeView(FragmentActivity context) {
        mContext = context;
        //获取一个布局解析器对象，用于在将布局文件转换为视图时使用
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }

    private  void createView() {
        mHandler = new MHandler();
        initAdData();
        //getCourseData();
        getHomeData();
        initView();
        //启动一个名为AdAutoSlidThread的新线程，并执行该线程的run()方法
        new AdAutoSlidThread().start();
    }

    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {  //用于处理从消息队列中取出的消息,如果不重写该方法，则会执行默认的实现，即直接调用 handleMessage() 方法
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);  //广告切换到下一页
                    }
                    break;
            }
        }
    }

    /**
     * 初始化广告中的数据
     */
    private void initAdData() {
        //TODO ArrayList<AdBean> 表示一个泛型为 AdBean 类型的动态数组对象，它可以存储多个 AdBean 对象，并可以根据需要动态地添加或删除元素
        adl = new ArrayList<AdBean>();
        for (int i = 0; i < 3; i++) {
            AdBean bean = new AdBean();
            //bean.id=(i + 1);
            switch (i) {
                case 0:
                    bean.icon="banner_1";
                    break;
                case 1:
                    bean.icon="banner_2";
                    break;
                case 2:
                    bean.icon="banner_3";
                    break;
                default:
                    break;
            }
            adl.add(bean);  //向列表中添加元素
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //将布局文件转换为一个视图对象，参数1是布局文件的 ID，参数2是视图的父布局，null，表示不将 main_view_home.xml 加载到任何父布局中，而是直接生成一个视图对象
        mCurrentView = mInflater.inflate(R.layout.main_view_home, null);

        //设置粮食头条
        lv_list = (ListView) mCurrentView.findViewById(R.id.lv_list);
        adapter = new HomeAdapter(mContext,2);
        adapter.setData(hbl);
        adapter.setFlag("粮食头条");
        lv_list.setAdapter(adapter);

        //设置广告
        adPager = (ViewPager) mCurrentView.findViewById(R.id.vp_advertBanner);
        adPager.setLongClickable(false);   //禁用长按事件
        //创建适配器
        ada = new AdBannerAdapter(mContext.getSupportFragmentManager(),
                mHandler);
        adPager.setAdapter(ada);      // 给ViewPager设置适配器
        //将触摸事件设置给 ViewPager 对象 adPager，并将 AdBannerAdapter 对象 ada 作为触摸监听器传递进去
        adPager.setOnTouchListener(ada);
        // 获取广告条上的小圆点
        vpi = (ViewPagerIndicator) mCurrentView
                .findViewById(R.id.vpi_advert_indicator);
        vpi.setCount(ada.getSize());// 设置小圆点的个数
        adBannerLay = mCurrentView.findViewById(R.id.rl_adBanner);
        //给ViewPager添加页面切换监听器
        adPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            /*该方法在页面滑动的过程中不断被调用，position是当前滑动页面的位置，
                positionOffset是当前页面偏移的百分比，
                positionOffsetPixels是当前页面偏移的像素值，
              可以通过这些参数做一些与滑动相关的处理*/
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            //该方法在页面滑动结束后被调用，position是当前选中的页面的位置。可以在这个方法中对当前选中的页面进行相关的处理，例如更新选中页面的UI等
            public void onPageSelected(int position) {
                //判断当前AdBannerAdapter对象ada中是否有数据
                if (ada.getSize() > 0) {
                    //由于index数据在滑动时是累加的，因此用index % ada.getSize()来标记滑动到的当前位置。
                    // 例如，如果数据列表大小为3，而当前页面位置为5，则通过取模运算得到的结果为2，即当前页面应该是数据列表中的第2个位置
                    vpi.setCurrentPosition(position % ada.getSize());
                }
            }
            @Override
            /*该方法在页面滑动状态发生变化时被调用，
               其中state是滑动状态，包括三种状态：
                  SCROLL_STATE_IDLE（滑动结束）、SCROLL_STATE_DRAGGING（正在拖动中）和SCROLL_STATE_SETTLING（自动滑动中）。
               可以在这个方法中对滑动状态进行相关的处理，例如在拖动页面时暂停自动滑动*/
            public void onPageScrollStateChanged(int state) {

            }
        });
        resetSize();
        if (adl != null) {
            if (adl.size() > 0) {
                //指示器(vpi)的计数值[小圆点的个数]设置为数据列表的大小
                vpi.setCount(adl.size());
                //设置滑动到当前页的当前小圆点以及其他圆点的位置
                vpi.setCurrentPosition(0);
            }
            ada.setDatas(adl);   //将数据列表(adl)设置到广告条适配器中
        }

        //更多的点击事件
        more=(LinearLayout)mCurrentView.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HeadlineActivity.actionStartFlag(mContext,"粮食头条");
            }
        });



        //质价标准的点击事件
        rl_zjbz = (RelativeLayout)mCurrentView.findViewById(R.id.rl_zjbz);
        rl_zjbz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HeadlineActivity.actionStartFlag(mContext,"质量标准");
            }
        });

        //收购政策的点击事件
        rl_sgzc = (RelativeLayout)mCurrentView.findViewById(R.id.rl_sgzc);
        rl_sgzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HeadlineActivity.actionStartFlag(mContext,"收购政策");
            }
        });

        //农业气象的点击事件
        rl_nyqx = (RelativeLayout)mCurrentView.findViewById(R.id.rl_nyqx);
        rl_nyqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HeadlineActivity.actionStartFlag(mContext,"农业气象");
            }
        });
    }

    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = getScreenWidth(mContext);   //获取屏幕的宽度
        int adLheight = sw / 2;// 广告条高度
        //获取广告条的布局参数对象
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        // 设置广告条的布局参数
        adBannerLay.setLayoutParams(adlp);
    }

    /**
     * 读取屏幕宽
     */
    public static int getScreenWidth(Activity context) {
        //TODO DisplayMetrics类是一个包含有关显示器通用信息的结构，可以获取显示器的宽度、高度、密度、缩放等信息。在应用程序中，通常使用DisplayMetrics来获取设备的屏幕分辨率和像素密度等信息
        //保存屏幕尺寸信息
        DisplayMetrics metrics = new DisplayMetrics();
        //TODO Display类表示一个屏幕设备，它提供了获取屏幕尺寸、旋转、像素格式等信息的方法。通过Display类，可以获取当前设备的默认屏幕，以及支持多屏幕的情况下的其他屏幕信息
        //获取当前Activity所在的屏幕设备，TODO getWindowManager()返回当前Activity所在的窗口管理器对象，getDefaultDisplay()方法获取当前窗口的Display对象，以便获取屏幕的尺寸信息
        Display display = context.getWindowManager().getDefaultDisplay();
        //TODO getMetrics()方法获取当前屏幕的显示指标
        //将当前设备的屏幕尺寸信息保存到应用程序中
        display.getMetrics(metrics);
        //从DisplayMetrics对象中获取屏幕的宽度，并将其作为函数的返回值
        return metrics.widthPixels;
    }

    /**
     * 广告自动滑动
     */
    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            //由于条件表达式为true，因此循环会一直执行，直到程序被手动停止或者发生异常。这种写法也被称为“死循环”
            while (true) {
                try {
                    //暂停线程执行5秒钟，使得轮播图停留在当前页面5秒钟
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null)
                    //发送一个名为MSG_AD_SLID的消息给主线程的mHandler对象
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
            }
        }
    }
    /**
     * 获取xml中的粮食头条的数据
     */
    private void getHomeData(){
        try {
            InputStream is = mContext.getResources().getAssets().open("headline.xml");
            hbl= AnalysisUtils.getHomeInfos(is);

        }catch(Exception e){
            e.printStackTrace();

        }
    }

    /**
     * 获取当前在导航栏上方显示对应的View
     */
    public View getView() {
        if (mCurrentView == null) {
            createView();
        }
        return mCurrentView;
    }
    /**
     * 显示当前导航栏上方所对应的view界面
     */
    public void showView(){
        if(mCurrentView == null){
            createView();
        }
        mCurrentView.setVisibility(View.VISIBLE);
    }

}
