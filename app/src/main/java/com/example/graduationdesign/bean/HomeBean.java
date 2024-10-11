package com.example.graduationdesign.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeBean implements Parcelable {
	private int id;               //新闻id
	private String title;         //新闻标题
	private String time;          //发布时间
	private String background;    //新闻插图
	private String read;          //阅读次数
	private String p1;            //第一段
	private String p2;            //第二段
	private String p3;            //第三段
	private String p4;            //第四段
	private String p5;            //第五段
	private String p6;            //第六段

	protected HomeBean(Parcel in) {
		id = in.readInt();
		title = in.readString();
		time = in.readString();
		background = in.readString();
		read = in.readString();
		p1 = in.readString();
		p2 = in.readString();
		p3 = in.readString();
		p4 = in.readString();
		p5 = in.readString();
		p6 = in.readString();
	}

	public HomeBean(){}

	public static final Creator<HomeBean> CREATOR = new Creator<HomeBean>() {
		@Override
		public HomeBean createFromParcel(Parcel in) {
			return new HomeBean(in);
		}

		@Override
		public HomeBean[] newArray(int size) {
			return new HomeBean[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		this.p6 = p6;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeString(title);
		parcel.writeString(time);
		parcel.writeString(background);
		parcel.writeString(read);
		parcel.writeString(p1);
		parcel.writeString(p2);
		parcel.writeString(p3);
		parcel.writeString(p4);
		parcel.writeString(p5);
		parcel.writeString(p6);
	}
}