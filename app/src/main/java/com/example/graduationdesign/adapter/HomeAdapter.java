package com.example.graduationdesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationdesign.R;
import com.example.graduationdesign.activity.homeview.HeadlineDetailActivity;
import com.example.graduationdesign.bean.HomeBean;

import java.util.List;

public class HomeAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeBean> hbl;
    private int mMaxItemsToShow;
    private String Flag;

    public HomeAdapter(Context context,List<HomeBean> hbl) {
        this.mContext = context;
        this.mMaxItemsToShow = hbl.size();
    }

    public HomeAdapter(Context context,int maxItemsToShow) {
        this.mContext = context;
        this.mMaxItemsToShow = maxItemsToShow;
    }
    /**
     * 设置数据更新界面
     */
    public void setData(List<HomeBean> hbl) {
        this.hbl = hbl;
        notifyDataSetChanged();
    }

    //设置要跳转的对应功能模块
    public void setFlag(String Flag){
        this.Flag = Flag;
    }
    /**
     * 获取Item的总数
     */
    @Override
    public int getCount() {
        return hbl == null ? 0 : hbl.size();
    }
    /**
     * 根据position得到对应Item的对象
     */
    @Override
    public HomeBean getItem(int position) {
        return hbl == null ? null : hbl.get(position);
    }
    /**
     * 根据position得到对应Item的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 得到相应position对应的Item视图，position是当前Item的位置，
     * convertView参数就是滚出屏幕的Item的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.home_list_item, null);
            vh.title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.background = (TextView) convertView.findViewById(R.id.tv_order);
            vh.read = (TextView) convertView.findViewById(R.id.tv_read);
			/*TODO 将 ViewHolder 对象 holder 存储在 convertView 中，以便在下次重复使用 convertView 时，可以通过调用 convertView.getTag() 方法获取
                   到保存在其中的 ViewHolder 对象，从而复用之前已经创建好的视图*/
            convertView.setTag(vh);
        } else {
            //复用convertView
            vh = (ViewHolder) convertView.getTag();
        }
        //设置界面上的文本和图片的数据信息
        //获取position对应的Item的数据对象

        final HomeBean bean = getItem(position);
        if (bean != null) {
            vh.title.setText(bean.getTitle());
            vh.time.setText(bean.getTime());
            //将String类型的资源名称转换为int类型的资源ID
            /*TODO getIdentifier()方法有三个参数：
                   1.资源名称（String类型）：指定要获取ID的资源的名称，例如“headline2”。
                   2.资源类型（String类型）：指定要获取ID的资源的类型，例如“drawable”、“string”等等。请注意，这里指定的资源类型应该是小写字母形式的，而不是R文件中使用的大写字母形式。
                   3.包名（String类型）：指定要获取ID的资源所在的包名，可以使用Context的getPackageName()方法获取。
                     getIdentifier()方法将返回一个int类型的资源ID。如果找到了指定名称和类型的资源，则返回该资源的ID；否则，返回0*/
            int resourceId = mContext.getResources().getIdentifier(bean.getBackground(), "drawable", mContext.getPackageName());
            if(resourceId!=0) {
                vh.background.setBackgroundResource(resourceId);
            }else{
                if(Flag.equals("农业气象")){
                    vh.background.setBackgroundResource(R.drawable.jxqx);
                }else if(Flag.equals("收购政策")){
                    vh.background.setBackgroundResource(R.drawable.sgzc);
                }
                else{
                    vh.background.setBackgroundResource(R.drawable.app_icon);
                }

            }
            vh.read.setText(bean.getRead());

        }
        //每个Item的点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean == null)
                    return;

                //跳转到对应功能模块详情界面
                HeadlineDetailActivity.actionStart(mContext,bean,Flag);
            }
        });

        if (position >= mMaxItemsToShow) {
            // 如果已经达到限制数目，则返回高度为分割线高度的空视图（不显示该项）
            View emptyView = new View(parent.getContext());
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    0);
            emptyView.setLayoutParams(params);
            return emptyView;
        }

        return convertView;
    }
    class ViewHolder {
        public TextView title, time,read;
        public TextView background;
    }
}
