package com.comred.hangout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.comred.Gifs.GifAnimationDrawable;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonContact;
import com.comred.JsonDATA.JsonCountry;
import com.comred.JsonDATA.JsonInit;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.JsonDATA.JsonWall;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* The first screen in the app, contains a loading animation
* this app get data of the user if he is connected.
* or doesn't do anything.*/
public class SplashScreen extends Activity {


    private ImageView img_raindrop;
    private GifAnimationDrawable rain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Global.SetContext(this);
       /* width= Global.getwidth();
        height=Global.getheight();
        ajax=  new AQuery(this);
        ajax.id(R.id.img_logo).width(width*50/100);
        ajax.id(R.id.img_logo).margin(0,10*height/100,0,0);
        ajax.id(R.id.img_raindrop).width(width*50/100);
        ajax.id(R.id.img_raindrop).height(height*4/100);*/


        //load GIF image to imageview.
        img_raindrop = (ImageView) findViewById(R.id.img_raindrop);
        try {
            rain = new GifAnimationDrawable(getResources().openRawResource(R.raw.raindrops));
            rain.setOneShot(false);
            img_raindrop.setImageDrawable(rain);
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* StringXORer stringXORer=  new StringXORer();
        String x=stringXORer.encode("http://192.168.137.225:11749/Apis/Mobile/",this.getPackageName());
        Log.d("--url: ", StringXORer.ReverseS(x));

        String x2=stringXORer.encode("http://192.168.137.225:11749/Upload/",this.getPackageName());
        Log.d("--url_photo: ",StringXORer.ReverseS(x2));*/

      /* StringXORer stringXORer=  new StringXORer();
        String x=stringXORer.encode("http://192.168.0.103/Apis/Mobile/",this.getPackageName());
        Log.d("--url: ",StringXORer.ReverseS(x));

        String x2=stringXORer.encode("http://192.168.0.103:8282/Upload/",this.getPackageName());
        Log.d("--url: ",StringXORer.ReverseS(x2));*/


        //refresh, check for account information, get data if available.
        refresh();
    }


    public void refresh() {
        //new user, or user signed out
        if (!Global.LoggedIn()) {
            if (Global.pref().getBoolean("isCountriesAvailable", false)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, Signup_login.class);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
                return;
            }
            JSONObject json = new JSONObject();
            try {
                json.put("Method", "Countries");
                json.put("Token", Utility.sha1("0"));
                JSONObject params = new JSONObject();
                json.put("Params", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

            StringXORer stringXORer = new StringXORer();
            String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), this.getPackageName());
            HttpRequest d = new HttpRequest(uiHandler, x + "InitHandler.ashx", nameValuePairs);
            d.start();
        } else {

            Global.init(uiHandler);

        }
    }


    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            //Exception ex= new Exception("Error in Connection...");
            try {

                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                if (!hasError && Response != null && !Response.equals("") && !Response.equals("False")) {

                    Gson gson = new Gson();
                    JsonInit parser = gson.fromJson(Response, JsonInit.class);
                    if (parser.isContacts)
                        JsonContact.data = parser.contacts;
                    if (parser.isCountries) {
                        JsonCountry.data = parser.countries;
                        SharedPreferences.Editor editor = Global.pref().edit();
                        editor.putString("Countries", gson.toJson(parser.countries));
                        editor.putBoolean("isCountriesAvailable", true);
                        editor.commit();
                    }
                    if (parser.isSchedules)
                        JsonSchedules.data = parser.schedules;
                    if (parser.isWalls) {
                        JsonWall.data = parser.walls;
                        JsonWall.maxID();
                    }

                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreen.this, Signup_login.class);
                            startActivity(i);
                            finish();
                        }
                    }, 2000);

                } else if (!hasError) {
                    this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Global.setLoggedIn(false);
                            Intent i = new Intent(SplashScreen.this, Signup_login.class);
                            startActivity(i);
                            finish();
                        }
                    }, 2000);

                } else {
                    throw new Exception("Error connecting ");
                }

            } catch (Exception e) {
                Utility.showtoast(getApplicationContext(), "Error in Connection ...");
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);

            }
        }
    };


}
