package com.example.graduationdesign.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.bean.UserBean;
import com.example.graduationdesign.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了一个 DBUtils 类，其中包括一个 instance、helper 和 db 变量。
 * instance 用于保存 DBUtils 类的唯一实例，helper 用于创建或更新数据库，db 用于操作数据库
 */
public class DBUtils {
    private static DBUtils instance = null;
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;
    public DBUtils(Context context) {
        helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();     //获取可读写SQLiteDatabase对象
    }
    /*获取 DBUtils 的唯一实例。如果 instance 为 null，则创建一个 DBUtils 对象，并将其赋值给 instance 变量。最后返回 instance 变量*/
    public static DBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }
    /**
     * 保存个人资料信息
     */
    public void saveUserInfo(UserBean bean) {
        ContentValues cv = new ContentValues();   //创建ContentValues对象
        //将 UserBean 对象 bean 的各个属性值存入 cv 对象
        cv.put("iconPath", bean.iconPath);
        cv.put("userName", bean.userName);
        cv.put("phone", bean.phone);
        cv.put("sex", bean.sex);
        cv.put("status", bean.status);
        cv.put("address", bean.address);
        /*TODO insert的三个参数分别是
           1.数据表的名称
           2.如果发现将要插入的行为空行时，会将这个列名的值设为null
           3.ContentValues(类似于Map类，通过键值对的形式存入数据，key表示插入数据的列名，value表示要插入的数据)对象
           */
        db.insert(SQLiteHelper.U_USERINFO, null, cv);
    }
    /**
     * 获取个人资料信息
     */
    public UserBean getUserInfo(String userName) {
        //TODO SELECT * FROM中的*表示个人信息表的全部列名
        String sql = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";

        /*rawQuery()是一个用于执行带参数 SQL 查询的方法，其中 sql 是 SQL 查询语句字符串，
            new String[]{userName} 是一个字符串数组，它包含一个或多个参数，这些参数将被替换到 SQL 查询语句中的 "?" 占位符。
      TODO 在这个例子中，查询语句是 "SELECT * FROM U_USERINFO WHERE userName=?"，其中 "?" 占位符将被替换为 userName*/
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        UserBean bean = null;
        while (cursor.moveToNext()) {   //移动游标指向下一行
            bean = new UserBean();
            //cursor.getColumnIndex("columnName") 方法会返回某一列的索引
            //cursor.getString(columnIndex) 方法根据列索引获取对应列的值，
            bean.iconPath=cursor.getString(cursor.getColumnIndex("iconPath"));
            bean.userName=cursor.getString(cursor.getColumnIndex("userName"));
            bean.phone=cursor.getString(cursor.getColumnIndex("phone"));
            bean.sex=cursor.getString(cursor.getColumnIndex("sex"));
            bean.status=cursor.getString(cursor.getColumnIndex("status"));
            bean.address=cursor.getString(cursor.getColumnIndex("address"));
        }
        cursor.close();  //关闭游标
        return bean;
    }


     //修改个人资料
    public void updateUserInfo(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        //将U_USERINFO 表中 userName 列的值更新为为参数值 userName 的数据。
        db.update(SQLiteHelper.U_USERINFO, cv, "userName=?",
                new String[]{userName});
    }

    /**
     * 保存库点浏览历史记录
     */
    public void save(NavigationBean bean, String userName) {
        ContentValues cv = new ContentValues();
        cv.put("userName", userName);
        cv.put("name", bean.getName());
        cv.put("belong", bean.getBelong());
        cv.put("backGround", bean.getBackground());
        cv.put("phone", bean.getPhone());
        cv.put("state", bean.getState());
        cv.put("browse", bean.getBrowse());
        cv.put("collect",bean.getCollect());
        cv.put("record", bean.getRecord());
        cv.put("historyTimeStamp", bean.getHistoryTimeStamp());
        cv.put("collectTimeStamp", bean.getCollectTimeStamp());
        db.insert(SQLiteHelper.U_GRANARY_LIST, null, cv);
    }

    /**
     * 获取库点浏览历史记录信息
     */
    public List<NavigationBean> getGranary(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_GRANARY_LIST +" WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        List<NavigationBean> nbl = new ArrayList<NavigationBean>();
        NavigationBean bean = null;
        //移动游标到下一行
        while (cursor.moveToNext()) {
            bean = new NavigationBean();
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setBelong(cursor.getString(cursor.getColumnIndex("belong")));
            bean.setBackground(cursor.getString(cursor.getColumnIndex("backGround")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            bean.setState(cursor.getString(cursor.getColumnIndex("state")));
            bean.setBrowse(cursor.getString(cursor.getColumnIndex("browse")));
            bean.setCollect(cursor.getString(cursor.getColumnIndex("collect")));
            bean.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            bean.setHistoryTimeStamp(cursor.getLong(cursor.getColumnIndex("historyTimeStamp")));
            bean.setCollectTimeStamp(cursor.getLong(cursor.getColumnIndex("collectTimeStamp")));
            nbl.add(bean);
            bean = null;
        }
        cursor.close();
        return nbl;
    }

    public List<NavigationBean> getGranaryCollect(String userName) {
        //根据时间戳排序
        String sql = "SELECT * FROM " + SQLiteHelper.U_GRANARY_LIST + " WHERE userName=? AND collect='true'  ORDER BY collectTimeStamp DESC ";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        List<NavigationBean> nbl = new ArrayList<NavigationBean>();
        NavigationBean bean = null;
        while (cursor.moveToNext()) {
            bean = new NavigationBean();
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setBelong(cursor.getString(cursor.getColumnIndex("belong")));
            bean.setBackground(cursor.getString(cursor.getColumnIndex("backGround")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            bean.setState(cursor.getString(cursor.getColumnIndex("state")));
            bean.setBrowse(cursor.getString(cursor.getColumnIndex("browse")));
            bean.setCollect(cursor.getString(cursor.getColumnIndex("collect")));
            bean.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            bean.setHistoryTimeStamp(cursor.getLong(cursor.getColumnIndex("historyTimeStamp")));
            bean.setCollectTimeStamp(cursor.getLong(cursor.getColumnIndex("collectTimeStamp")));
            nbl.add(bean);
            bean = null;
        }
        cursor.close();
        return nbl;
    }

    public List<NavigationBean> getGranaryHistory(String userName) {
        //根据时间戳排序
        String sql = "SELECT * FROM " + SQLiteHelper.U_GRANARY_LIST + " WHERE userName=? AND record='true' ORDER BY historyTimeStamp DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        List<NavigationBean> nbl = new ArrayList<NavigationBean>();
        NavigationBean bean = null;
        while (cursor.moveToNext()) {
            bean = new NavigationBean();
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setBelong(cursor.getString(cursor.getColumnIndex("belong")));
            bean.setBackground(cursor.getString(cursor.getColumnIndex("backGround")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            bean.setState(cursor.getString(cursor.getColumnIndex("state")));
            bean.setBrowse(cursor.getString(cursor.getColumnIndex("browse")));
            bean.setCollect(cursor.getString(cursor.getColumnIndex("collect")));
            bean.setRecord(cursor.getString(cursor.getColumnIndex("record")));
            bean.setHistoryTimeStamp(cursor.getLong(cursor.getColumnIndex("historyTimeStamp")));
            bean.setCollectTimeStamp(cursor.getLong(cursor.getColumnIndex("collectTimeStamp")));
            nbl.add(bean);
            bean = null;
        }
        cursor.close();
        return nbl;
    }


    //修改信息
    public void updateKeyValue(String key, String value, String name, String belong, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        //将U_USERINFO 表中 key 列的值更新为为参数值 Value 的数据。
        db.update(SQLiteHelper.U_GRANARY_LIST, cv, " name=? AND belong=? AND userName=?",
                new String[]{ name + "", belong + "" ,userName});

    }

    //将数据库中record列为true的行 的record值修改为false
    public void updateRecordFalse(String userName){
        ContentValues cv = new ContentValues();
        cv.put("record","false");
        db.update(SQLiteHelper.U_GRANARY_LIST, cv, " userName=? AND record=? ",
                new String[]{ userName + "","true"});
    }

    //将数据库中collect列为true的行 的collect值修改为false
    public void updateCollectFalse(String userName){
        ContentValues cv = new ContentValues();
        cv.put("collect","false");
        db.update(SQLiteHelper.U_GRANARY_LIST, cv, " userName=? AND collect=? ",
                new String[]{ userName + "","true"});
    }


    //更新数据库里历史时间戳或者收藏时间戳
    public void updateTime(String key, Long value, String name,String belong, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_GRANARY_LIST, cv, " name=? AND belong=? AND userName=?",
                new String[]{ name + "", belong + "" ,userName});
    }

    //检查数据库中是否已存在与当前bean对象具有相同名称和所属关系的记录
    public boolean isGranaryHistoryExists(NavigationBean bean, String userName) {
        /*
        TODO 如果需要获取符合条件的所有记录，则需要用*遍历查询结果集中的每一行记录。
         如果只需要计算符合条件的记录数量，则不需要遍历结果集，只需要使用COUNT(*)聚合函数来计算结果集中的行数。
         */
        String sql = "SELECT COUNT(*) FROM " + SQLiteHelper.U_GRANARY_LIST + " WHERE name = ? AND belong = ? AND userName = ?";
        String[] selectionArgs = { bean.getName(), bean.getBelong(), userName };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        boolean exists = false;
        if (cursor != null) {
            //移动到第一个结果行并获取第一个列的值
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            exists = (count > 0);
            cursor.close();
        }
        return exists;
    }
}
