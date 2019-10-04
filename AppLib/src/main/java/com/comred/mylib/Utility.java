package com.comred.mylib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Utility {


    public static int UI_DENSITY;
    public static int UI_SIZE;
    public static int UI_YAHOO_SCROLL;
    public static int UI_YAHOO_ALLOW;
    public static int UI_RESOLUTION;


    public static int getScale( Context ctx,int PIC_WIDTH ){
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }

    public static void clearAllResources() {

        // Set related variables null

        System.gc();
        Runtime.getRuntime().gc();
    }

    public static String getMac(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;
    }

    public static String sha1(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

    public static String get_date(){
        Calendar c = new GregorianCalendar();
        int mYear=c.get(Calendar.YEAR);
        int mMonth=c.get(Calendar.MONTH);
        int mDay=c.get(Calendar.DAY_OF_MONTH);
        Log.d("date",String.valueOf(mYear)+"-"+String.valueOf(mMonth+1)+"-"+String.valueOf(mDay));

        String s=String.valueOf((mYear+mMonth+mDay+1)*1500);
        return s;
    }

    public static int getHeight(Activity a){



        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return height;
    }

    public static int getWidth(Activity a){



        Display display = a.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }
	
public static String getString(String s , Calendar c){
		
		return (String) android.text.format.DateFormat.format(s, c.getTime());
	}
	public static void hideTiTleBar(Activity A){
		A.requestWindowFeature(Window.FEATURE_NO_TITLE);
        A.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	public static void hidekeyboardv2(Activity A){
		A.getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	public static void hideKeyboard(Context activity, View view)
	{
		
	    InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
	 public static void showtoast(Context activity, String msg){
		 
	    	Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	    }
	 
	 public static double round(double value, int places) {
		    if (places < 0) 
		    		return 0.0;

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
	 
	 

	    public static  long getID(){
	        Calendar cal = Calendar.getInstance();
	       /* System.out.println("cal:"+new Date(cal.getTimeInMillis()).toString());
	        int s= cal.get(Calendar.YEAR)+cal.get(Calendar.MONTH)+cal.get(Calendar.DAY_OF_MONTH)+cal.get(Calendar.HOUR_OF_DAY)
	                +cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND);
	        System.out.println("cal:"+cal.getTimeInMillis());
	        System.out.println("cal:" + s);*/
	        return cal.getTimeInMillis();
	    }


    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    public static void getDisplayDpi(Context ctx) {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        double screenInches = Math.sqrt(x + y);
        int screenInch = (int) Math.round(screenInches);
        int dapi = dm.densityDpi;

        Log.d("Resolution X", String.valueOf(width));
        Log.d("Resolution Y", String.valueOf(height));
        Log.d("screeninch", String.valueOf(screenInch));
        Log.d("dapi", String.valueOf(dapi));

        try {
            switch (dm.densityDpi) {

                case DisplayMetrics.DENSITY_LOW:

                    UI_DENSITY = 120;

                    if (screenInch <= 7) {
                        UI_SIZE = 4;
                        UI_YAHOO_SCROLL = 290;
                        UI_YAHOO_ALLOW = 125;

                    } else {
                        UI_SIZE = 10;
                    }

                    break;
                case DisplayMetrics.DENSITY_MEDIUM:

                    UI_DENSITY = 160;

                    if (screenInch <= 7) {

                        // For devices having width 320
                        if (width == 320) {
                            UI_YAHOO_SCROLL = 390;
                            UI_YAHOO_ALLOW = 105;
                            UI_SIZE = 3;
                        } else if (width == 480) {
                            UI_YAHOO_SCROLL = 600;
                            UI_YAHOO_ALLOW = 200;
                            UI_SIZE = 4;
                        } else {
                            UI_YAHOO_SCROLL = 1;
                            UI_YAHOO_ALLOW = 1;
                            UI_SIZE = 7;
                        }
                    } else {
                        UI_SIZE = 10;
                        UI_YAHOO_SCROLL = 1;
                        UI_YAHOO_ALLOW = 1;
                    }

                    break;

                case DisplayMetrics.DENSITY_HIGH:

                    UI_DENSITY = 240;
                    UI_YAHOO_SCROLL = 715;
                    UI_YAHOO_ALLOW = 375;

                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    UI_DENSITY = 320;
                    if (width >= 720 && width < 1280) {
                        UI_SIZE = 7;
                        UI_YAHOO_SCROLL = 900;
                        UI_YAHOO_ALLOW = 475;
                    } else if (width >= 1280) {
                        UI_SIZE = 10;
                        UI_YAHOO_SCROLL = 1;
                        UI_YAHOO_ALLOW = 1;
                    } else {
                        UI_YAHOO_SCROLL = 1;
                        UI_YAHOO_ALLOW = 1;
                    }

                    break;

                case 213:
                    UI_DENSITY = 213;
                    UI_YAHOO_SCROLL = 300;
                    UI_YAHOO_ALLOW = 155;

                default:
                    break;
            }
        } catch (Exception e) {
            // Caught exception here
        }
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


    

    public static  String toStringFromDate(Calendar cal){

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);      // 0 to 11
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        return ("Now is "+year+"/"+(month + 1)+"/"+day+" "+hour+":"+minute+":"+second+"\n");
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
}
