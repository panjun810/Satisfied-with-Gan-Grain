package com.example.graduationdesign.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.graduationdesign.bean.NavigationBean;
import com.example.graduationdesign.bean.SearchInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonParse {
    //TODO 使用一个单例模式创建JsonParse对象，单例模式能确保整个应用中只有一个对象实例存在，避免在不同的地方创建多个相同的对象，节省内存空间
    // 常用的使用场景：实例对象需要被频繁访问，应用每次启动都只存在一个实例。    eg:数据库对象(SQLiteOpenHelper)
    private static JsonParse instance;
    private JsonParse(){}
    public static JsonParse getInstance(){
        if(instance == null){
            instance = new JsonParse();
        }
        return instance;
    }

    /**
     * 将获取的数据流转化为JSON数据
     */
    public String read(InputStream in){
        //BufferedReader是一个带有缓冲区的字符输入流，它可以从字符输入流中读取字符并缓存到缓冲区中，以提高读取效率。
        BufferedReader reader = null;
        //StringBuilder是一个可变字符串对象，它可以动态地添加、删除和修改字符串内容
        StringBuilder sb = null;
        String line = null;
        try{
            sb = new StringBuilder();    //实例化一个StringBuilder对象
            //用InputStreamReader把in这个字节流转换成字符流BufferedReader
            reader = new BufferedReader(new InputStreamReader(in));
            //判断从reader中读取的行内容是否为空
            while((line = reader.readLine())!= null){
                sb.append(line);
                sb.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }finally{
            try{
                if(in !=null) in.close();
                if(reader!= null) reader.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    //解析JSON文件返回信息集合
    public List<NavigationBean> getNavigationFromJson(@NonNull Context context){
        List<NavigationBean> Infos = new ArrayList<>();
        InputStream is = null;
        try{
            //从项目中的assets文件夹中获取json
            is = context.getResources().getAssets().open("navigation.json");
            String json = read(is);  //获取json数据

            Gson gson = new Gson(); //创建Gson对象
            //创建一个TypeToken的匿名子类对象，并调用该对象的getType()方法
            Type listType = new TypeToken<List<NavigationBean>>(){}.getType();
            //把获取到的信息集合存到infoList中
            List<NavigationBean> infoList = gson.fromJson(json,listType);
            return infoList;
        }catch (IOException e){
            e.printStackTrace();
        }
        return Infos;
    }


}
