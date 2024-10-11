package com.example.graduationdesign.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;     //数据库的版本号
    public static String DB_NAME = "Graduationdesign.db";     //数据库的名称
    public static final String U_USERINFO = "userinfo";// 用户信息表（数据库中的一张表）
   //
    public static final String U_GRANARY_LIST = "granarylist";//视频播放列表

     /*TODO 调用了父类 SQLiteOpenHelper 的构造函数，用于创建数据库,
        方法里的四个参数分别是上下文对象，数据库名称，游标工厂（通常为null）,数据库版本 */
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    //数据库第一次被创建的时调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建个人信息表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_USERINFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "iconPath VARCHAR, "// 头像
                + "userName VARCHAR, "// 用户名
                + "phone VARCHAR, "// 电话
                + "sex VARCHAR, "// 性别
                + "status VARCHAR, "// 身份
                + "address VARCHAR"// 地址
                + ")");

        // 创建库点浏览历史记录表
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_GRANARY_LIST + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +" userName VARCHAR,"//用户名
                + "name VARCHAR,"//库点名
                + "belong VARCHAR,"//所属企业
                + "backGround VARCHAR,"//图片
                + "phone VARCHAR,"// 咨询电话
                + "state VARCHAR,"//粮仓状态
                + "browse VARCHAR,"//浏览次数
                + "collect VARCHAR,"  // 是否收藏
                + "record VARCHAR,"  // 是否查看
                + "historyTimeStamp Long,"  // 历史时间戳
                + "collectTimeStamp Long"  // 收藏时间戳
                + ")");
    }
    /**
     * 当数据库版本号增加时才会调用此方法，它执行了一个 SQL 命令，用于删除名为 U_USERINFO 的表，并调用 onCreate() 方法重新创建该表
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + U_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + U_GRANARY_LIST);
        onCreate(db);
    }
}
