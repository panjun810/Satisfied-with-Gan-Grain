package com.example.graduationdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.navigationview.NavigationDetailActivity;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GranaryHistoryAdapter extends BaseAdapter {

    private List<NavigationBean> nbl;
    private Context mContext;
    private DBUtils db;

    public GranaryHistoryAdapter(Context mContext){
        this.mContext = mContext;
        db = DBUtils.getInstance(mContext);
    }

    public void setData(List<NavigationBean> nbl){
        /*Collections.reverse(nbl); // 倒序排列数据*/
        this.nbl = nbl;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return nbl == null ? 0 :nbl.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.granary_history_list_item,null);
            vh.tv_background = (TextView) convertView.findViewById(R.id.tv_background);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_belong = (TextView) convertView.findViewById(R.id.tv_belong);
            vh.tv_timestamp_data = (TextView) convertView.findViewById(R.id.tv_timestamp_date);
            vh.tv_timestamp_time = (TextView) convertView.findViewById(R.id.tv_timestamp_time);
            vh.ll_delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        final NavigationBean bean = getItem(position);
        if(bean!=null){
            vh.tv_name.setText(bean.getName());
            vh.tv_belong.setText(bean.getBelong());

            long timeStamp = bean.getHistoryTimeStamp(); // 获取当前时间戳

            // 将时间戳格式化为年月日格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            String dateStr = dateFormat.format(new Date(timeStamp));
            vh.tv_timestamp_data.setText(dateStr);

            // 将时间戳格式化为时分秒格式
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String timeStr = timeFormat.format(new Date(timeStamp));
            vh.tv_timestamp_time.setText(timeStr);


            int resourceId = mContext.getResources().getIdentifier(bean.getBackground(), "drawable", mContext.getPackageName());
            if (resourceId != 0) {
                vh.tv_background.setBackgroundResource(resourceId);
            } else {
                vh.tv_background.setBackgroundResource(R.drawable.navigation1);
            }

            vh.ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.setRecord("false");
                    // 更新数据库中的是否查看字段
                    DBUtils.getInstance(mContext).updateKeyValue("record",
                            bean.getRecord(), bean.getName(), bean.getBelong(), AnalysisUtils.readLoginUserName(mContext));
                    Toast.makeText(mContext,"该行记录已清除",Toast.LENGTH_SHORT).show();


                    nbl = new ArrayList<NavigationBean>();
                    //从数据库中获取库点浏览记录信息
                    nbl = db.getGranaryHistory(AnalysisUtils.readLoginUserName(mContext));
                    setData(nbl);
                }
            });

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bean == null){ return ;}

                bean.setHistoryTimeStamp(System.currentTimeMillis());
                // 更新数据库中的历史时间戳字段
                DBUtils.getInstance(mContext).updateTime("historyTimeStamp",
                        bean.getHistoryTimeStamp(), bean.getName(), bean.getBelong(), AnalysisUtils.readLoginUserName(mContext));

                //跳转到库点信息界面
                NavigationDetailActivity.actionStart(mContext,bean);

            }
        });
        return convertView;


    }

    class ViewHolder{
        public TextView tv_name,tv_belong,tv_background;
        public TextView tv_timestamp_data,tv_timestamp_time;
        public LinearLayout ll_delete;
    }
}
