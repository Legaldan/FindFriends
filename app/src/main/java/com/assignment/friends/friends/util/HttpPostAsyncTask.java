package com.assignment.friends.friends.util;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Li Yan on 2017/5/10.
 */

public abstract class HttpPostAsyncTask<Params, Result> extends AsyncTask<Params,Void,Result> {

    protected Context context;
    private Exception e;
    private String error_msg;
    private String url_path;
    private String request_method;
    protected HttpURLConnection conn = null;

    public void setErrorMsg(String msg){
        error_msg = msg;
    }

    public HttpPostAsyncTask(Context c, String msg, String path){
        super();
        context = c;
        error_msg = msg;
        url_path = path;
        request_method = "POST";
    }

    @Override
    protected Result doInBackground(Params...params) {
        try {
            URL url = new URL(url_path);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod(request_method);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            return ownTask(conn.getOutputStream());
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
        else  if (this.e != null || result == null)
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

    protected abstract Result ownTask(OutputStream os) throws Exception;
}
