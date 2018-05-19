package com.jarvisdong.uikit.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JarvisDong on 2017/9/19.
 * OverView:
 */

public class GsonUtils<T> {
    private static GsonUtils mGsonUtils;
    private GsonUtils() {
    }

    public static GsonUtils getInstance(){
        if(mGsonUtils == null){
            mGsonUtils = new GsonUtils();
        }
        return mGsonUtils;
    }

    public String obj2string(Object target, Class clazz) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(target, clazz);
    }


    public Object string2Obj(String target, Class clazz) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(target, clazz);
    }

    public String list2string(ArrayList<T> list) {
        //需要注意的是这里的Type导入的是java.lang.reflect.Type的包
        //TypeToken导入的是 com.google.gson.reflect.TypeToken的包
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(list, type);
    }

    public List<T> string2list(String target, T clazz) {
        List<T> p2 = new ArrayList<T>();
        Type type1 = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();
        p2 = gson.fromJson(target, type1);
        return p2;
    }

    /**
     * 按章节点得到相应的内容
     * @param jsonString json字符串
     * @param note 节点
     * @return 节点对应的内容
     */
    public static String getNoteJsonString(String jsonString, String note){
        if(TextUtils.isEmpty(jsonString)){
            throw new RuntimeException("json字符串");
        }
        if(TextUtils.isEmpty(note)){
            throw new RuntimeException("note标签不能为空");
        }
        JsonElement element = new JsonParser().parse(jsonString);
        if(element.isJsonNull()){
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return element.getAsJsonObject().get(note).toString();
    }

    /**
     * 按照节点得到节点内容，然后传化为相对应的bean数组
     * @param jsonString 原json字符串
     * @param note 节点标签
     * @param beanClazz 要转化成的bean class
     * @return 返回bean的数组
     */
    public static <T> List<T> parserJsonToArrayBeans(String jsonString, String note, Class<T> beanClazz){
        String noteJsonString = getNoteJsonString(jsonString,note);
        return parserJsonToArrayBeans(noteJsonString,beanClazz);
    }
    /**
     * 按照节点得到节点内容，转化为一个数组
     * @param jsonString json字符串
     * @param beanClazz 集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public static <T> List<T> parserJsonToArrayBeans(String jsonString, Class<T> beanClazz){
        if(TextUtils.isEmpty(jsonString)){
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if(jsonElement.isJsonNull()){
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        if(!jsonElement.isJsonArray()){
            throw new RuntimeException("json字符不是一个数组对象集合");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> beans = new ArrayList<T>();
        for (JsonElement jsonElement2: jsonArray) {
            T bean = new Gson().fromJson(jsonElement2, beanClazz);
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 把相对应节点的内容封装为对象
     * @param jsonString json字符串
     * @param clazzBean  要封装成的目标对象
     * @return 目标对象
     */
    public static <T> T parserJsonToArrayBean(String jsonString, Class<T> clazzBean){
        if(TextUtils.isEmpty(jsonString)){
            throw new RuntimeException("json字符串为空");
        }
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if(jsonElement.isJsonNull()){
            throw new RuntimeException("json字符串为空");
        }
        if(!jsonElement.isJsonObject()){
            throw new RuntimeException("json不是一个对象");
        }
        return new Gson().fromJson(jsonElement, clazzBean);
    }
    /**
     * 按照节点得到节点内容，转化为一个数组
     * @param jsonString json字符串
     * @param note json标签
     * @param clazzBean 集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public static <T> T parserJsonToArrayBean(String jsonString, String note, Class<T> clazzBean){
        String noteJsonString = getNoteJsonString(jsonString, note);
        return parserJsonToArrayBean(noteJsonString, clazzBean);
    }
}
