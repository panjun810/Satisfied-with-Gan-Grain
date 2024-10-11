package com.example.graduationdesign.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationBean implements Parcelable {
    private String city;      //所在城市
    private String county;    //所在区县
    private String name;      //粮仓名字
    private String belong;    //所属企业
    private String background;   //粮仓图片
    private String phone;    //咨询电话
    private String state;    //粮仓情况
    private String browse;   //浏览次数
    private String collect;   //是否收藏
    private String record;    //是否查看
    private Long historyTimeStamp;    //时间戳
    private Long collectTimeStamp;    //时间戳

    private String longitude;  //经度
    private String latitude;  //纬度

    public NavigationBean() {}

    protected NavigationBean(Parcel in) {
        city = in.readString();
        county = in.readString();
        name = in.readString();
        belong = in.readString();
        background = in.readString();
        phone = in.readString();
        state = in.readString();
        browse = in.readString();
        collect = in.readString();
        record = in.readString();
        if (in.readByte() == 0) {
            historyTimeStamp = null;
        } else {
            historyTimeStamp = in.readLong();
        }
        if (in.readByte() == 0) {
            collectTimeStamp = null;
        } else {
            collectTimeStamp = in.readLong();
        }
        longitude = in.readString();
        latitude = in.readString();
    }

    public static final Creator<NavigationBean> CREATOR = new Creator<NavigationBean>() {
        @Override
        public NavigationBean createFromParcel(Parcel in) {
            return new NavigationBean(in);
        }

        @Override
        public NavigationBean[] newArray(int size) {
            return new NavigationBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(county);
        parcel.writeString(name);
        parcel.writeString(belong);
        parcel.writeString(background);
        parcel.writeString(phone);
        parcel.writeString(state);
        parcel.writeString(browse);
        parcel.writeString(collect);
        parcel.writeString(record);
        if (historyTimeStamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(historyTimeStamp);
        }
        if (collectTimeStamp == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(collectTimeStamp);
        }
        parcel.writeString(longitude);
        parcel.writeString(latitude);
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public Long getHistoryTimeStamp() {
        return historyTimeStamp;
    }

    public void setHistoryTimeStamp(Long historyTimeStamp) {
        this.historyTimeStamp = historyTimeStamp;
    }

    public Long getCollectTimeStamp() {
        return collectTimeStamp;
    }

    public void setCollectTimeStamp(Long collectTimeStamp) {
        this.collectTimeStamp = collectTimeStamp;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
