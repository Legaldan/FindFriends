package com.assignment.friends.friends.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Li Yan on 2017/5/3.
 */

public abstract class HttpGetAsyncTask<Params,Result> extends AsyncTask<Params,Void,Result> {
    private Context context;
    private Exception e;
    private String error_msg;
    private String url_path;
    private String request_method;

    public HttpGetAsyncTask(Context c, String msg, String path){
        context = c;
        error_msg = msg;
        url_path = path;
        request_method = "GET";
    }

    @Override
    protected Result doInBackground(Params... params) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(url_path);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(request_method);
            return ownTask(conn.getInputStream());
        } catch (Exception ie){
            this.e = ie;
        } finally {
            if (conn!=null)
                conn.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result){
        if (e instanceof UnknownHostException)
            Toast.makeText(context, "Please check your network",Toast.LENGTH_SHORT).show();
        else  if (this.e != null)
            Toast.makeText(context, error_msg,Toast.LENGTH_SHORT).show();
        else
            try {
                httpPostExecute(result);
            }catch (Exception ie){
                ie.printStackTrace();
                Toast.makeText(context, "Init view error", Toast.LENGTH_SHORT).show();
            }
    }

    protected abstract void httpPostExecute(Result result) throws Exception;

    protected abstract Result ownTask(InputStream result) throws Exception;
}
