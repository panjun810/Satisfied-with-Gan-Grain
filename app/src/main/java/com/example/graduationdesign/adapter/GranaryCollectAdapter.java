package com.example.graduationdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.navigationview.NavigationDetailActivity;
import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.utils.AnalysisUtils;
import com.example.graduationdesign.utils.DBUtils;

import java.util.List;

public class GranaryCollectAdapter extends BaseAdapter {

    private List<NavigationBean> nbl;
    private Context mContext;

    public GranaryCollectAdapter(Context mContext){
        this.mContext = mContext;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.granary_collect_list_item,null);
            vh.tv_background = (TextView) convertView.findViewById(R.id.tv_background);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_belong = (TextView) convertView.findViewById(R.id.tv_belong);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        final NavigationBean bean = getItem(position);
        if(bean!=null){
            vh.tv_name.setText(bean.getName());
            vh.tv_belong.setText(bean.getBelong());

            int resourceId = mContext.getResources().getIdentifier(bean.getBackground(), "drawable", mContext.getPackageName());
            if (resourceId != 0) {
                vh.tv_background.setBackgroundResource(resourceId);
            } else {
                vh.tv_background.setBackgroundResource(R.drawable.navigation1);
            }

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bean == null){ return ;}

                bean.setRecord("true");
                bean.setHistoryTimeStamp(System.currentTimeMillis());

                // 更新数据库中的是否收藏字段
                DBUtils.getInstance(mContext).updateKeyValue("record",
                        bean.getRecord(), bean.getName(), bean.getBelong(), AnalysisUtils.readLoginUserName(mContext));

                // 更新数据库中的时间戳字段
                DBUtils.getInstance(mContext).updateTime("historyTimeStamp",
                        bean.getHistoryTimeStamp(), bean.getName(), bean.getBelong(),AnalysisUtils.readLoginUserName(mContext));

                //跳转到库点信息界面
                NavigationDetailActivity.actionStart(mContext,bean);
            }
        });
        return convertView;


    }

    class ViewHolder{
        public TextView tv_name,tv_belong,tv_background;
    }
}
