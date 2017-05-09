package com.assignment.friends.friends.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Li Yan on 2017-05-05.
 */

public class CustomLruCache {
    private LruCache<String, Bitmap> stringBitmapLruCache;
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int cacheSize = maxMemory / 32;
    private static CustomLruCache customLruCache;

    /**
     * 私有化构造方法
     */
    private CustomLruCache() {
        stringBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * @return
     */
    public static CustomLruCache getInstance() {
        if (customLruCache == null) {
            customLruCache = new CustomLruCache();
        }
        return customLruCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) != bitmap)
            stringBitmapLruCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return stringBitmapLruCache.get(key);
    }
}

