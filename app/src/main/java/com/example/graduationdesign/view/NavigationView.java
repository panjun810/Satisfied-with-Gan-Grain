package com.example.graduationdesign.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.navigationview.NavigationActivity;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.bean.SearchInfo;
import com.example.graduationdesign.utils.JsonParse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class NavigationView implements View.OnClickListener {
    private LayoutInflater mInflater;
    private Activity mContext;
    private View mCurrentView;

    private TextView tv_main_title,tv_back;
    private RelativeLayout rl_title_bar;

    private RelativeLayout rl_city,rl_county;
    private TextView tv_city,tv_county;
    private Button btn_search;

    private List<SearchInfo> infoList;

    private List<NavigationBean> nbl;

    String[] cities;

    int cityFlag=0,countyFlag=0;


    public NavigationView(Activity context)
    {
        mContext = context;
        //为之后将Layout转化为view时用
        mInflater = LayoutInflater.from(mContext);
    }
    private  void createView() {
        mCurrentView = mInflater.inflate(R.layout.main_view_navigation, null);
        mCurrentView.setVisibility(View.VISIBLE);

        getSearchInfoData();

        nbl = JsonParse.getInstance().getNavigationFromJson(mContext);
        initView();
    }

    private void getSearchInfoData() {
        try{
            InputStream is = mContext.getResources().getAssets().open("searchdata.json");
            String json = JsonParse.getInstance().read(is);  //获取json数据
            infoList = getSearchInfoFromJson(json);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //解析JSON文件返回信息集合
    public List<SearchInfo> getSearchInfoFromJson(String json){
        Gson gson = new Gson(); //使用gson库解析Json数据
        //创建一个TypeToken的匿名子类对象，并且调用对象的getType()方法
        Type listType = new TypeToken<List<SearchInfo>>(){}.getType();
        //把获取到的信息集合存在shopList中
        List<SearchInfo> infoList = gson.fromJson(json,listType);
        return infoList;
    }

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

    public void setFlag( int cityFlag,int countyFlag){
        this.cityFlag = cityFlag;
        this.countyFlag = countyFlag;
        tv_city.setText("所有城市");
        tv_county.setText("所有区县");
    }

    /**
     * 获取界面控件
     */
    private void initView(){
        tv_main_title=(TextView) mCurrentView.findViewById(R.id.tv_main_title);
        tv_main_title.setText("库点导航");
        tv_back=(TextView) mCurrentView.findViewById(R.id.tv_back);
        rl_title_bar=(RelativeLayout) mCurrentView.findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#33ff00"));

        rl_city=(RelativeLayout)mCurrentView.findViewById(R.id.rl_city);
        rl_county=(RelativeLayout)mCurrentView.findViewById(R.id.rl_county);


        btn_search=(Button)mCurrentView.findViewById(R.id.btn_search);

        rl_city.setOnClickListener(this);
        rl_county.setOnClickListener(this);

        btn_search.setOnClickListener(this);

        tv_city=(TextView) mCurrentView.findViewById(R.id.tv_city);
        tv_county=(TextView) mCurrentView.findViewById(R.id.tv_county);


        tv_back.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_city:
                cityDialog();
                break;
            case R.id.rl_county:
                countyDialog();
                break;
            case R.id.btn_search:
                String city = tv_city.getText().toString();//获取city控件上的数据
                String county = tv_county.getText().toString();//获取city控件上的数据

                //判断库点是否存在
                boolean hasdata = false;
                for (NavigationBean nb : nbl) {
                    if (nb.getCity().equals(city)&&nb.getCounty().equals(county)) {
                        hasdata = true;
                        break;
                    }else if(city.equals("所有城市")&&county.equals("所有区县")){
                        hasdata = true;
                        break;
                    }else if(nb.getCity().equals(city)&&county.equals("所有区县")){
                        hasdata = true;
                        break;
                    }
                }
                if (!hasdata) {
                    Toast.makeText(mContext,"该地不存在库点",Toast.LENGTH_SHORT).show();
                }else{
                    NavigationActivity.actionStart(mContext,city,county);
                }
                break;
        }

    }



    /**
     * 设置城市的弹出框
     */
    private void cityDialog(){
        getcities();
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext, R.style.CustomDialogTheme);  //先得到构造器
        builder.setTitle("城市"); //设置标题
        //参数1表示单选列表中的所有选项数据，参数2表示单选列表中的默认选择角标，参数3表示单选列表的监听事件
        builder.setSingleChoiceItems(cities,cityFlag,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//第二个参数是点击时被选中的项目
                cityFlag = which;
                countyFlag = 0;
                tv_county.setText("所有区县");

                dialog.dismiss();     //关闭对话框
                Toast.makeText(mContext,cities[which],Toast.LENGTH_SHORT).show();
                tv_city.setText(cities[which]);
            }
        });
        builder.create().show();    //显示对话框
    }

    private void getcities() {
        cities = new String[infoList.size()];
        // 将城市放入城市数组中
        for (int i = 0; i < infoList.size(); i++) {
            cities[i] = infoList.get(i).getCity();
        }
    }


    /**
     * 设置区县的弹出框
     */
    private void countyDialog() {
        String city = tv_city.getText().toString();//获取city控件上的数据
        //获取对应城市对应的区县
        String[] counties = getcounties(city);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomDialogTheme);  //先得到构造器
        builder.setTitle("区县"); //设置标题
        //参数1表示单选列表中的所有选项数据，参数2表示单选列表中的默认选择角标，参数3表示单选列表的监听事件
        builder.setSingleChoiceItems(counties, countyFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//第二个参数是点击时被选中的项目
                countyFlag = which;
                dialog.dismiss();     //关闭对话框
                Toast.makeText(mContext, counties[which], Toast.LENGTH_SHORT).show();

                tv_county.setText(counties[which]);
            }
        });
        builder.create().show();    //显示对话框
    }

    private String[] getcounties(String city) {
        //遍历当前城市列表
        for (int i = 0; i < infoList.size(); i++) {
            SearchInfo info = infoList.get(i);
            if (info.getCity().equals(city)) {
                // 声明一个数组，用于保存当前城市的区县名称
                String[] counties = new String[info.getCounty().size()];
                // 遍历当前城市的区县列表，将名称放入 counties 数组中
                for (int j = 0; j < info.getCounty().size(); j++) {
                    counties[j] = info.getCounty().get(j);
                }
                return counties;
            }
        }
        // 如果没有找到对应城市，则返回一个空数组
        return new String[0];
    }
}