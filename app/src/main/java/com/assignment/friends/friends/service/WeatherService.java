package com.assignment.friends.friends.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.friends.friends.util.CustomLruCache;
import com.assignment.friends.friends.util.HttpGetAsyncTask;
import com.assignment.friends.friends.util.ImageLoader;
import com.assignment.friends.friends.util.JsonHandler;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Li Yan on 2017/4/26.
 */

public class WeatherService extends HttpGetAsyncTask<String,String> {
    private TextView tv;
    private TextView range;
    private TextView location;
    private ImageView icon;
    private Context context;

    public WeatherService(Context c, String msg, String lat, String lon, TextView tv, TextView r, TextView l, ImageView icon){
        super(c,msg,"http://api.openweathermap.org/data/2.5/find?lat="+lat+"&lon="+lon+"&cnt=1&units=metric&appid=caa74f7a481f251f98cb6d9f48106581");
        this.tv = tv;
        this.range = r;
        this.location = l;
        this.icon = icon;
        this.context = c;
    }

    @Override
    protected void httpPostExecute(String weatherInfo) throws Exception{

        String[] infoArray = weatherInfo.split("\n");
        tv.setText(infoArray[0]);
        range.setText(infoArray[1]);
        location.setText(infoArray[2]);
        String fileName = infoArray[3]+".png";
        CustomLruCache cache = CustomLruCache.getInstance();
        Bitmap bmp = cache.getBitmapFromMemoryCache("weather/"+fileName);

        if (bmp != null)
            icon.setImageBitmap(bmp);
        else {
            ImageLoader imageLoader = new ImageLoader(context, "Cannot get weather icon", "http://openweathermap.org/img/w/" + fileName, icon, "weather/" + fileName);
            imageLoader.execute();
        }

    }

    @Override
    protected String ownTask(InputStream result) throws Exception{
        String json = "";
        Scanner inStream = new Scanner(result);
        while(inStream.hasNextLine()){
            json += inStream.nextLine();
        }
        String weatherInfo = JsonHandler.parseWeatherJson(json);//get the needed information
        return weatherInfo;
    }
}
