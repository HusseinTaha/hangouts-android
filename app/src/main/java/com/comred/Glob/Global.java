package com.comred.Glob;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.comred.JsonDATA.JsonContact;
import com.comred.JsonDATA.JsonCountry;
import com.comred.hangout.R;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HTaha on 12/16/13.
 * Global is the Application class
 * containing static methods and variables for the hole app.
 */
public class Global extends Application {


    public static int id;
    private static Global singleton;
    private static SharedPreferences pref=null;

    private static Context mContext;

    private static Typeface tf;
    private static Boolean LoggedIn;
    private static String Token;

   /* //server home
    private static String URL="CpwDdcBAqEkEB4VJKpUVXtFFV9lUa1UXWBkUY9hSXpkQAllXZsxC";
    private static String URL_PHOTO="==AQD8gDE4VMKpUVXtFFV9lUa1UXWBkUY9hSXpkQAllXZsxC";*/

    //Testing
    private static String URL="=AkBYwRDIMiTbcEFk0VXXNlFX51UFtlXJZ1VZBgVcNkQAllXZsxC";
    private static String URL_PHOTO="IpAAHIEFw0VXXNlFX51UFtlXJZ1VZBgVcNkQAllXZsxC";
/*
//mycpmputer home
    private static String URL="=AkBYwRDIMgTbcEFE0FVbR1HcV1WCtlXJZ1VZBgVcNkQAllXZsxC";
    private static String URL_PHOTO="IpAAHIEFw0FVbR1HcV1WCtlXJZ1VZBgVcNkQAllXZsxC";
*/

   /* //real server
    private static String URL="=AkBYwRDIMiTbcEFk01XXFlFX51VFt1XVBUWbxhSWBkQAllXZsxC";
    private static String URL_PHOTO="IpAAHIEFw01XXFlFX51VFt1XVBUWbxhSWBkQAllXZsxC";*/


   /* private static String URL="=w0SBYQAbgDQUcQEpEQXRVEXel1Gf1VTDZkXJZ1VZBgVcNkQAllXZsxC";
    private static String URL_PHOTO="bFhDIIQE9EQXRVEXel1Gf1VTDZkXJZ1VZBgVcNkQAllXZsxC";*/


    public static SharedPreferences pref(){
        return pref;
    }

    public static String Token() {
        return Token;
    }

    public static void Token(String token) {
        Token = token;
    }
    public static void saveToken(){
        Editor edit= pref.edit();
        edit.putString("TOKEN",Token);
        edit.commit();
    }

    public static Boolean LoggedIn(){
        return LoggedIn;
    }



    public static void setLoggedIn(boolean loggedIn){
        LoggedIn=loggedIn;
        if(loggedIn){
            SharedPreferences.Editor editor = pref().edit();
            editor.putBoolean("LoggedIn",true);
            editor.commit();
        }

    }

    public static String URL() {
        return URL;
    }
    public static String URL_PHOTO() {
        return URL_PHOTO;
    }


    public static Context getmContext() {
        return mContext;
    }

    public static void SetContext(Context ctx){
        mContext=ctx;
    }
    public static Global getInstance() {
        return singleton;
    }


    public static boolean isNotificationEnabled(){
        return pref().getBoolean("isNotificationEnabled",true);
    }
    public static void setNotifEnabled(boolean val){
        SharedPreferences.Editor editor = pref().edit();
        editor.putBoolean("isNotificationEnabled",val);
        editor.commit();

    }
    public static void saveStringPref(String title,String data){
        SharedPreferences.Editor editor = pref().edit();
        editor.putString(title, data);
        editor.commit();
    }
    public static String getStringPref(String key){
        return pref().getString(key, "");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;
        pref=getSharedPreferences("Profile", Activity.MODE_PRIVATE);
        String fontPath = "fonts/simple_font.ttf";
        // Loading Font Face
         tf = Typeface.createFromAsset(getAssets(), fontPath);
       LoggedIn= pref.getBoolean("LoggedIn",false);
        String t= pref.getString("TOKEN","");
        if(!t.equals(""))
            this.Token=t;
    }

    public static void delete(){
        SharedPreferences.Editor editor = pref().edit();
        editor.remove("TOKEN");
        editor.remove("LoggedIn");
        editor.remove("isNotificationEnabled");
        editor.remove("Name");
        editor.remove("CountryCode");
        editor.remove("Mobile");
        editor.commit();
        LoggedIn=false;
        Token="";
    }


    public static void readContacts(String url,Handler uiHandler){
        String ccode=Global.GetCountryZipCode();
        ContentResolver cr = mContext.getContentResolver();
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        // use the cursor to access the contacts
        List<JsonContact> jsonContacts= new ArrayList<JsonContact>();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            // get display name
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNumber.startsWith("+00") || phoneNumber.startsWith("+ 00") || phoneNumber.startsWith("00"))
                phoneNumber= phoneNumber.replaceFirst("00","");
            if(phoneNumber.contains("-"))
                phoneNumber=phoneNumber.replaceAll("-","");
            phoneNumber.trim();
            if(phoneNumber.contains(" "))
                phoneNumber=phoneNumber.replaceAll(" ","");
            if(phoneNumber.startsWith("0"))
                phoneNumber= phoneNumber.replaceFirst("0", "");
            if(phoneNumber.length()<=8)
                phoneNumber=ccode+phoneNumber;
            if(phoneNumber.length()>8){
                if(!phoneNumber.contains("+"))
                    phoneNumber="+"+phoneNumber;
            }

            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {

                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "");

                int countryCode = numberProto.getCountryCode();
                JsonContact js= new JsonContact();
                js.Country= JsonCountry.getIDfromCountryCode(countryCode);
                js.ContactName=name;
                js.Mobile=numberProto.getNationalNumber()+"";
                jsonContacts.add(js);

            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }


        }


        Gson gson= new Gson();
        JSONObject json = new JSONObject();
        try {
            json.put("Method", "AddContacts");
            json.put("Token", Global.Token());
            JSONObject params = new JSONObject();
            params.put("DATA",gson.toJson(jsonContacts));
            json.put("Params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add your data
        Log.d("json", json.toString());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), mContext.getPackageName());


        HttpRequest d = new HttpRequest(uiHandler, x + url, nameValuePairs);
        //Log.d("AF", Global.URL()+"Init.ashx");
        d.start();

    }

    public static String GetCountryZipCode() {

        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = mContext.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        return CountryZipCode;
    }





    public static void init(Handler uiHandler){
        JSONObject json = new JSONObject();
        try {
            json.put("Method", "Init");
            json.put("Token", Global.Token());
            JSONObject params = new JSONObject();
            json.put("Params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), mContext.getPackageName());


        HttpRequest d = new HttpRequest(uiHandler, x + "InitHandler.ashx", nameValuePairs);
        d.start();
    }


    public static Typeface getTypeFace( ){
        return tf;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static int getheight(){
        return  Utility.getHeight((Activity) mContext);
    }
    public static int getwidth(){
        return  Utility.getWidth((Activity)mContext);
    }


    public static  String toStringFromDate(Calendar cal){

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        //"2009-05-08 14:40:52,531"
        return (""+year+"-"+(month + 1)+"-"+day+" "+hour+":"+minute+":"+second);
    }


    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);


        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }




    public static Animation inFromRightAnimation(Animation.AnimationListener listener) {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        inFromRight.setAnimationListener(listener);
        return inFromRight;
    }


    public static Animation outToLeftAnimation(Animation.AnimationListener listener) {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        outtoLeft.setAnimationListener(listener);
        return outtoLeft;
    }


    public static Animation inFromLeftAnimation(Animation.AnimationListener listener) {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        inFromLeft.setAnimationListener(listener);
        return inFromLeft;
    }

    public static Animation outToRightAnimation(Animation.AnimationListener listener) {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        outtoRight.setAnimationListener(listener);
        return outtoRight;
    }









    public static Animation inFromTopAnimation(Animation.AnimationListener listener) {

        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromTop.setDuration(500);
        inFromTop.setInterpolator(new AccelerateInterpolator());
        inFromTop.setAnimationListener(listener);
        return inFromTop;
    }


    public static Animation outToBottomAnimation(Animation.AnimationListener listener) {
        Animation outtoBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f);
        outtoBottom.setDuration(500);
        outtoBottom.setInterpolator(new AccelerateInterpolator());
        outtoBottom.setAnimationListener(listener);
        return outtoBottom;
    }


    public static Animation inFromBottomAnimation(Animation.AnimationListener listener) {
        Animation inFromBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromBottom.setDuration(500);
        inFromBottom.setInterpolator(new AccelerateInterpolator());
        inFromBottom.setAnimationListener(listener);
        return inFromBottom;
    }

    public static Animation outToTopAnimation(Animation.AnimationListener listener) {
        Animation outtoTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outtoTop.setDuration(500);
        outtoTop.setInterpolator(new AccelerateInterpolator());
        outtoTop.setAnimationListener(listener);
        return outtoTop;
    }
}
