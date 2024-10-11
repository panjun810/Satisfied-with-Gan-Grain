package com.example.graduationdesign.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.navigationview.NavigationDetailActivity;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;
import com.example.graduationdesign.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class NavigationAdapter extends BaseAdapter {
    private Context mContext;
    private List<NavigationBean> nbl;

    private String city,county;

    private DBUtils db;
    private String userName;

    private List<NavigationBean> gnbl;   //从数据库读取的值

    private NavigationBean nb;   //从数据库读取的值

    public NavigationAdapter(Context context){
        this.mContext = context;
        //创建数据库工具类对象
        db = DBUtils.getInstance(mContext);
        //从SharedPreferences中获取登录时的用户名
        userName = AnalysisUtils.readLoginUserName(mContext);
    }

    public void setData(List<NavigationBean> nbl){
        this.nbl = nbl;
        notifyDataSetChanged();
    }

    public void setIF(String city,String county){
        this.city = city;
        this.county = county;
    }


    @Override
    public int getCount() {
        return nbl == null ? 0 : nbl.size();
    }

    @Override
    public NavigationBean getItem(int position) {
        return nbl == null ? null : nbl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.navigation_list_item,null);
            vh.background = (TextView)convertView.findViewById(R.id.tv_background);
            vh.name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.belong = (TextView) convertView.findViewById(R.id.tv_belong);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        //设置库点列表上的文本和图片的数据信息
        final NavigationBean bean = getItem(position);
        if(bean != null) {
            if(city.equals(bean.getCity()) && county.equals(bean.getCounty())) {
                setView(vh,bean);
            }else if(city.equals("所有城市") && county.equals("所有区县")){
                setView(vh,bean);
            }else if(city.equals(bean.getCity()) && county.equals("所有区县")){
                setView(vh,bean);
            }else{
                // 如果已经达到限制数目，则返回高度为分割线高度的空视图（不显示该项）
                View emptyView = new View(parent.getContext());
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                emptyView.setLayoutParams(params);
                return emptyView;
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bean == null){ return ;}

                //检查数据库中是否已存在与当前bean对象具有相同名称和所属关系的记录
                Boolean exists  = db.isGranaryHistoryExists(bean,userName);

                //第一次点击时触发,将xml解析的数据和默认值设置到数据库里
                if(!exists){
                    //初始化信息
                    bean.setCollect("false");
                    bean.setRecord("true");
                    bean.setHistoryTimeStamp(System.currentTimeMillis());
                    bean.setCollectTimeStamp(System.currentTimeMillis());
                    //保存信息到数据库中
                    db.save(bean,userName);
                    Log.i("启动","第一次点击");
                }else{
                    gnbl = new ArrayList<NavigationBean>();
                    //从数据库中获取信息
                    gnbl = db.getGranary(AnalysisUtils.readLoginUserName(mContext));

                    if(gnbl.size()!=0){
                        //遍历从数据库中解析出的信息
                        for(NavigationBean gnb : gnbl){
                            //第一次点击事时表内没对应数据，不会触发if
                            if(gnb.getName().equals(bean.getName())&&gnb.getBelong().equals(bean.getBelong())){
                                nb = gnb;
                                if(gnb.getCollect() == null){
                                    //TODO 可能要删除
                                    //默认为false,即库点没有被收藏
                                    bean.setCollect("false");
                                    bean.setRecord("true");
                                    bean.setHistoryTimeStamp(System.currentTimeMillis());
                                    LogUtils.i("启动","gnb.collect == null");

                                }else{
                                    //从数据库中读取对应的信息出来
                                    bean.setCollect(gnb.getCollect());
                                    bean.setRecord(gnb.getRecord());
                                    bean.setHistoryTimeStamp(System.currentTimeMillis());
                                    bean.setCollectTimeStamp(gnb.getCollectTimeStamp());
                                    LogUtils.i("启动","gnb.collect 不为空");
                                }
                            }
                        }
                    }

                    bean.setCollect(nb.getCollect());
                    //以后点击设置为已查看
                    bean.setRecord("true");
                    bean.setHistoryTimeStamp(System.currentTimeMillis());

                    // 更新数据库中的是否收藏字段
                    DBUtils.getInstance(mContext).updateKeyValue("record",
                            bean.getRecord(), bean.getName(), bean.getBelong(),userName);

                    // 更新数据库中的时间戳字段
                    DBUtils.getInstance(mContext).updateTime("historyTimeStamp",
                            bean.getHistoryTimeStamp(), bean.getName(), bean.getBelong(),userName);

                }

                /*//跳转到库点信息界面
                Intent intent = new Intent(mContext,
                        NavigationDetailActivity.class);

                //把库点信息界面的信息传递到库点信息页面
                intent.putExtra("background", bean.getBackground());
                intent.putExtra("name", bean.getName());
                intent.putExtra("belong", bean.getBelong());
                intent.putExtra("phone", bean.getPhone());
                intent.putExtra("state", bean.getState());
                intent.putExtra("browse", bean.getBrowse());
                intent.putExtra("collect", bean.getCollect());

                intent.putExtra("Flag","Navi");
                intent.putExtra("longitude", bean.getLongitude());
                intent.putExtra("latitude", bean.getLatitude());

                mContext.startActivity(intent);*/

                //跳转到库点信息界面  有问题
                NavigationDetailActivity.actionStartFromNavi(mContext,bean);

            }
        });
        return convertView;
    }

    public void setView(ViewHolder vh,NavigationBean bean){
        vh.name.setText(bean.getName());
        vh.belong.setText(bean.getBelong());

        int resourceId = mContext.getResources().getIdentifier(bean.getBackground(), "drawable", mContext.getPackageName());
        if (resourceId != 0) {
            vh.background.setBackgroundResource(resourceId);
        } else {
            vh.background.setBackgroundResource(R.drawable.navigation1);
        }

    }

    class ViewHolder{
        public TextView background,name,belong;
    }
}
