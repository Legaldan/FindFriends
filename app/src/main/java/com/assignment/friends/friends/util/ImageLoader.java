package com.assignment.friends.friends.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * Created by Li Yan on 2017/5/2.
 */

public class ImageLoader extends HttpGetAsyncTask<String,Bitmap> {

    private ImageView image;
    private String key;

    public ImageLoader(Context c, String msg, String path,ImageView image,String key){
        super(c,msg,path);
        this.image = image;
        this.key = key;
    }

    @Override
    protected void httpPostExecute(Bitmap bmp) throws Exception{
        image.setImageBitmap(bmp);
        //write to memory cache
        CustomLruCache writer = CustomLruCache.getInstance();
        writer.addBitmapToMemoryCache(key,bmp);
    }

    @Override
    protected Bitmap ownTask(InputStream inputStream) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try {
            while ((inputStream.read(buffer)) != -1) {
                outStream.write(buffer);
            }
            inputStream.close();
        }catch (Exception e){
            throw e;
        }

        byte[] data = outStream.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
        return bmp;

    }

}


