package com.comred.mylib.Connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpRequest {
	private String url = null;
	private Handler handler = null;
	private List<NameValuePair> nameValuePairs = null;
	int TIME_OUT_IN_SECONDS= 60;

	public HttpRequest(Handler handler, String Url,List<NameValuePair> nameValuePairs) {
		this.url = Url;
		this.handler = handler;
		this.nameValuePairs = nameValuePairs;
	}

	public void start() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				GetData();
			}
		});
		thread.start();
	}

	/*
	 * Using Of Handler in The Client Side :
	 * ------------------------------------- Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { // get the bundle and
	 * extract data by key Bundle b = msg.getData(); String key =
	 * b.getString("My Key"); txt.setText(txt.getText() + "Item " + key
	 * +System.getProperty("line.separator")); } };
	 */

	private void GetData() {
		String response = null;
		Message msg = new Message();
		Bundle b = new Bundle();

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			if (nameValuePairs == null)
				nameValuePairs = new ArrayList<NameValuePair>(1);
			// nameValuePairs.add(new BasicNameValuePair("ID", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			long requestStratTime = new Date().getTime();               //---------------
			HttpResponse httpResponse = httpClient.execute(httpPost);
			long requestEndTime = new Date().getTime();               //------------------
			 long timeOfRequest = (requestEndTime - requestStratTime) / 1000;   //----------------
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			if (httpResponse == null && timeOfRequest > TIME_OUT_IN_SECONDS) {    //----------------

	            throw new TimeoutException();
	        }
			System.out.println(response);

			if (handler != null) {
				b.putString("Response", response);
			//	Log.d("resp",response);
				b.putBoolean("hasError", false);
				msg.setData(b);
				handler.sendMessage(msg);
			}

			return;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			b.putString("ResponseError", e.getMessage());
			b.putBoolean("hasError", true);
			msg.setData(b);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			b.putString("ResponseError", e.getMessage());
			b.putBoolean("hasError", true);
			msg.setData(b);
		} catch (IOException e) {
			e.printStackTrace();
			b.putString("ResponseError", e.getMessage());
			b.putBoolean("hasError", true);
			msg.setData(b);
		} catch (Exception e) {
			e.printStackTrace();
			b.putString("ResponseError", e.getMessage());
			b.putBoolean("hasError", true);
			msg.setData(b);
		}
		if (handler != null)
			handler.sendMessage(msg);

		return;
	}


	public List<NameValuePair> add_params(String apikey,String method,String params){
		
    	try {
    		
			String json="{\"ApiKey\":\"" + apikey + "\",\"Method\":\"" + method + "\",\"Params\":" + params + "}";
			  	
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));
	        this.nameValuePairs=nameValuePairs;
	        return nameValuePairs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
	}
	


	public List<NameValuePair> add_params(String apikey,String method,JSONObject params){
		
    	try {
    		JSONObject json=new JSONObject();
			json.put("Apikey", apikey);
			json.put("Method", method);
	    	json.put("Params", params);
	    	//Log.d("json",json.toString());
	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));
	        this.nameValuePairs=nameValuePairs;
	        return nameValuePairs;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
	}
	
 

}
