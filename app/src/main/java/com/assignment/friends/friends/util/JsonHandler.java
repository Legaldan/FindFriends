package com.assignment.friends.friends.util;


import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.assignment.friends.friends.model.Profile;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Li Yan on 2017/4/26.
 */

public class JsonHandler {

    public static String parseWeatherJson(String source){
        String result = "";
        try {
            JSONArray ja = JSON.parseObject(source).getJSONArray("list");
            JSONObject info = ja.getJSONObject(0).getJSONObject("main");
            result += (int)(Float.parseFloat(info.getString("temp"))+0.5);
            result += "℃\n" + (int)(Float.parseFloat(info.getString("temp_min"))+0.5)+"℃ ~ " + (int)(Float.parseFloat(info.getString("temp_max"))+0.5) + "℃\n";
            result += ja.getJSONObject(0).getString("name")+"\n";
            JSONObject weather = ja.getJSONObject(0).getJSONArray("weather").getJSONObject(0);
            result += weather.getString("icon");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public static String parseMovieJson(String source){
        String result = "";
        try {
            JSONObject item = JSON.parseObject(source).getJSONArray("items").getJSONObject(0);
            result = item.getString("link");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public static String readInputStream(InputStream is){
        String json = "";
        Scanner scanner = new Scanner(is);
        while(scanner.hasNextLine()){
            json += scanner.nextLine();
        }
        return json;
    }

    public static Object parseSingleObject(String json){
        return JSON.parseArray(json).get(0);
    }

    @NonNull
    public static Object[] parseObjectArray(String json){
        return JSON.parseArray(json).toArray();
    }
}
