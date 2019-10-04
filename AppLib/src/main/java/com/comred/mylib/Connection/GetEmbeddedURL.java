package com.comred.mylib.Connection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 07/09/13.
 */
public class GetEmbeddedURL extends Thread {

    Context mContext;
    String URLString="";

    String Resp_URl=null;
    Handler UiHandelr;

    public GetEmbeddedURL(Context ctx,Handler handler){
        this.mContext=ctx;
        this.UiHandelr=handler;
    }

    public void SetUrl(String url){
        this.URLString=url;
    }

    @Override
    public void run() {
        super.run();

      //  Resp_URl=getUrlFromYoutube();
        Resp_URl=getCodeSource(URLString);
        Message msg=new Message();
        Bundle b=new Bundle();
        b.putString("Response", Resp_URl);
        Log.d("resp",Resp_URl);
        msg.setData(b);
        UiHandelr.sendMessage(msg);
    }

    public String getEmbedded(){
        return Resp_URl;
    }


    private String getUrlFromYoutube(){

        String page=getCodeSource(URLString);
        Pattern p = Pattern.compile("href=\"(.*?)\"");
        Matcher m = p.matcher(page);
        String jsonpage="";
        while(m.find()) {
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            String s=m.group(1);
            if(s.contains("oembed?"))
            {
                jsonpage = m.group(1);
                break;
            }
        }
        page=getCodeSource(jsonpage);
        try {
            JSONObject js=new JSONObject(page);
            return js.getString("html");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String getCodeSource(String urls){
        StringBuilder builder = new StringBuilder(100000);


            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urls);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    builder.append(s);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        return builder.toString();
    }
    private String getContents(String url) {
        String contents ="";

        try {
            URLConnection conn = new URL(url).openConnection();

            InputStream in = conn.getInputStream();
            contents = convertStreamToString(in);
        } catch (MalformedURLException e) {
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contents;
    }



    private  String convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
