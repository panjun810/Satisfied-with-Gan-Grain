package com.example.graduationdesign.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;

import com.example.graduationdesign.R;
import com.example.graduationdesign.bean.HomeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AnalysisUtils {
    /**
     * 从SharedPreferences中读取登录用户名
     */
    public static String readLoginUserName(Context context){
        SharedPreferences sp=context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“loginUserName”的字符串，如果不存在，则默认为空
        String userName=sp.getString("loginUserName", "");
        return userName;
    }

    /**
     * 获取SharedPreferences中的登录状态
     */
    public static boolean readLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo",
                Context.MODE_PRIVATE);      //获取已经保存的登录信息
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    /**
     * 判断手机号是否已被其它用户绑定
     *
     * @param context 上下文对象
     * @param phone 手机号
     * @return true表示已被绑定，false表示未被绑定
     */
    public static boolean isExistPhone(Context context, String phone) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String usersString = sp.getString("user_list", "[]");
        try {
            JSONArray users = new JSONArray(usersString);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("phone_number").equals(phone)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断用户名是否已被其他用户注册
     *
     * @param context 上下文对象
     * @param userName 用户名
     * @return true表示已被绑定，false表示未被绑定
     */
    public static boolean isExistUserName(Context context, String userName) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp=context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");
        try {
            //将获取到的字符串解析为一个JSONArray对象。
            JSONArray users = new JSONArray(usersString);
            //遍历JSONArray对象中的所有JSONObject对象，查找具有“username”属性的JSONObject对象，该属性的值等于给定用户名。
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("username").equals(userName)) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断该密保是不是当前登录用户设置的密保
    public static boolean isthisSecurity(Context context, String security) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String usersString = sp.getString("user_list", "[]");
        try {
            JSONArray users = new JSONArray(usersString);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString(AnalysisUtils.readLoginUserName(context) + "_security").equals(security)) {
                    if (user.getString("username").equals(AnalysisUtils.readLoginUserName(context))) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     *从SharedPreferences中根据用户名读取密码
     */
    public static String readPsw(Context context, String userName) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String savedPassword = null;
        //遍历users的所有JSONObject对象，查找具有“username”属性的JSONObject对象，获取对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("username").equals(userName)) {
                        savedPassword = user.getString("password");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedPassword;
    }


    /**
     *从SharedPreferences中根据手机号读取密码
     */
    public static String readName(Context context, String phone) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String savedName = null;
        //遍历users的所有JSONObject对象，查找具有“username”属性的JSONObject对象，获取对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("phone_number").equals(phone)) {
                        savedName = user.getString("username");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedName;
    }


    /**
     *从SharedPreferences中根据登录的用户读取密保
     */
    public static String readSecurity(Context context) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String savedSecurity = null;
        //遍历users的所有JSONObject对象，查找具有“username”属性的JSONObject对象，获取对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("username").equals(AnalysisUtils.readLoginUserName(context))) {
                        savedSecurity = user.getString(AnalysisUtils.readLoginUserName(context) + "_security");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedSecurity;
    }


    /**
     *从SharedPreferences中根据登录的用户读取用户绑定的手机号
     */
    public static String readPhone(Context context) {
        //获取sp对象，参数loginInfo表示文件名，MODE_PRIVATE表示文件操作模式,即只有本应用可以访问该 SharedPreferences 对象。
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        //从sp里获取获取一个名为“user_list”的字符串，如果不存在，则默认为空数组
        String usersString = sp.getString("user_list", "[]");

        JSONArray users = null;
        if (usersString != null) {
            try {
                //将获取到的字符串解析为一个JSONArray对象。
                users = new JSONArray(usersString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String savedPhone = null;
        //遍历users的所有JSONObject对象，查找具有“username”属性的JSONObject对象，获取对应对象的密码。
        if (users != null) {
            for (int i = 0; i < users.length(); i++) {
                try {
                    JSONObject user = users.getJSONObject(i);
                    if (user.getString("username").equals(AnalysisUtils.readLoginUserName(context))) {
                        savedPhone = user.getString("phone_number");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return savedPhone;
    }

    /**
     * 保存密保到SharedPreferences中
     */
    public static void saveSecurity(Context context, String validateName) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);//loginInfo表示文件名
        String usersString = sp.getString("user_list", "[]");

        try {
            JSONArray users = new JSONArray(usersString);

            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    try {
                        JSONObject user = users.getJSONObject(i);
                        if (user.getString("username").equals(AnalysisUtils.readLoginUserName(context))) {
                            // 新增一个密保字段
                            user.put(AnalysisUtils.readLoginUserName(context) + "_security", validateName);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 保存更新后的数据
            SharedPreferences.Editor editor = sp.edit();//获取编辑器
            editor.putString("user_list", users.toString());//存入账号对应的密保
            editor.apply();//提交修改
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存手机号到SharedPreferences中
     */
    public static void savePhone(Context context, String Phone) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);//loginInfo表示文件名
        String usersString = sp.getString("user_list", "[]");

        try {
            JSONArray users = new JSONArray(usersString);

            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    try {
                        JSONObject user = users.getJSONObject(i);
                        if (user.getString("username").equals(AnalysisUtils.readLoginUserName(context))) {
                            // 新增一个密保字段
                            user.put("phone_number", Phone);
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 保存更新后的数据
            SharedPreferences.Editor editor = sp.edit();//获取编辑器
            editor.putString("user_list", users.toString());//存入账号对应的密保
            editor.apply();//提交修改
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析对应功能模块xml文件的信息
     */
    public static List<HomeBean> getHomeInfos (InputStream is)throws Exception{
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is,"utf-8");
        List<HomeBean> homeInfos=null;
        HomeBean homeInfo=null;
        int type= parser.getEventType();
        while (type!=XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG:
                    if("infos".equals(parser.getName())){
                        homeInfos=new ArrayList<HomeBean>();
                    }else if("new".equals(parser.getName())){
                        homeInfo=new HomeBean();
                        String ids=parser.getAttributeValue(0);
                        homeInfo.setId(Integer.parseInt(ids));
                    }else if("title".equals(parser.getName())){
                        String title = parser.nextText();
                        homeInfo.setTitle(title);
                    }else if("time".equals(parser.getName())){
                        String time = parser.nextText();
                        homeInfo.setTime(time);
                    }else if("read".equals(parser.getName())){
                        String read = parser.nextText();
                        homeInfo.setRead(read);
                    }else if("p1".equals(parser.getName())){
                        String p1 = parser.nextText();
                        p1 = p1.replace("\\n", "\n"); // 替换换行符为实体引用
                        p1 = p1.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP1(p1);
                    }else if("p2".equals(parser.getName())){
                        String p2 = parser.nextText();
                        //TODO 在 Java 中，反斜杠 \ 是一个转义字符。当您使用单个反斜杠 \ 时，Java 会将其解释为一个转义序列。
                        // \n 是一个转义序列，代表换行符。但是在这个上下文中，我们需要替换的是字符串中的字面字符 \n，而不是使用转义序列表示的换行符。
                        // 为了匹配原始字符串中的字面字符 \n，我们需要在代码中使用双反斜杠 \\n。第一个反斜杠 \ 是用来转义第二个反斜杠 \，这样第二个反斜杠才能被解释为字面字符 \。
                        p2 = p2.replace("\\n", "\n"); // 替换换行符为实体引用
                        p2 = p2.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP2(p2);

                    }else if("p3".equals(parser.getName())){
                        String p3 = parser.nextText();
                        p3 = p3.replace("\\n", "\n"); // 替换换行符为实体引用
                        p3 = p3.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP3(p3);

                    }else if("p4".equals(parser.getName())){
                        String p4 = parser.nextText();
                        p4 = p4.replace("\\n", "\n"); // 替换换行符为实体引用
                        p4 = p4.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP4(p4);

                    }else if("p5".equals(parser.getName())){
                        String p5 = parser.nextText();
                        p5 = p5.replace("\\n", "\n"); // 替换换行符为实体引用
                        p5 = p5.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP5(p5);

                    }else if("p6".equals(parser.getName())) {
                        String p6 = parser.nextText();
                        p6 = p6.replace("\\t", "\t\t\t\t"); // 替换换行符为实体引用
                        homeInfo.setP6(p6);

                    }else if("background".equals(parser.getName())){
                        String background = parser.nextText();
                        homeInfo.setBackground(background);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if("new".equals(parser.getName())){
                        homeInfos.add(homeInfo);
                        homeInfo=null;
                    }
                    break;
            }
            type=parser.next();
        }
        return homeInfos;
    }
}
