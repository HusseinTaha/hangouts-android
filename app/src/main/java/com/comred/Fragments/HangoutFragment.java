package com.comred.Fragments;


import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.comred.Controls.ErrorAlert;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonHangout;
import com.comred.hangout.R;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;
import com.google.gson.Gson;
import com.pagilagan.lib.datetimepicker.datetimepicker.DateTime;
import com.pagilagan.lib.datetimepicker.datetimepicker.DateTimePicker;
import com.pagilagan.lib.datetimepicker.datetimepicker.SimpleDateTimePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
* Hangout Fragment is used to add an event with a date.
* this event will be shown on all his friends calendars.
* */
public class HangoutFragment extends Fragment implements DateTimePicker.OnDateTimeSetListener{


    private AQuery ajax;
    private ProgressDialog progressDialog;
    private int height,width;

    public HangoutFragment() {
        width=Global.getwidth();
        height=Global.getheight();
        progressDialog=  new ProgressDialog(Global.getmContext());
        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Hangout");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utility.clearAllResources();
        View v=null;


        // Inflate the layout for this fragment
        try {
            v= inflater.inflate(R.layout.fragment_hangout, container, false);
            if(v==null)
                return null;

            ajax=  new AQuery(v);

            int scale=2;

            ajax.id(R.id.tv_date).getView().getLayoutParams().width=width*16/100;
            ajax.id(R.id.tv_date).getView().getLayoutParams().height=height*4/100;

            ajax.id(R.id.tv_event).getView().getLayoutParams().width=width*16/100;
            ajax.id(R.id.tv_event).getView().getLayoutParams().height=height*4/100;

            //ajax.id(R.id.tv_date).height(2+scale*height/100);
            //ajax.id(R.id.et_date).height(scale*height/100);
            //ajax.id(R.id.tv_event).height(2+scale*height/100);
            //ajax.id(R.id.et_event).height(10*height/100);

            ajax.id(R.id.et_date).getView().getLayoutParams().height=height*4/100;
            ajax.id(R.id.et_event).getView().getLayoutParams().height=height*21/100;

            ajax.id(R.id.tv_date).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            ajax.id(R.id.tv_event).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            ajax.id(R.id.et_date).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            ajax.id(R.id.et_event).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            ajax.id(R.id.btn_Add).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            //scale=10;
            //ajax.id(R.id.tv_date).width(scale*width/100);
            //ajax.id(R.id.tv_event).width(scale*width/100);

            ajax.id(R.id.rl_parent).getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        Utility.hideKeyboard(Global.getmContext(), ajax.id(R.id.rl_parent).getView());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });

            final SimpleDateTimePicker dateTimePickerEvent = SimpleDateTimePicker.make(
                    "Set Date & Time Title",
                    new Date(),
                    this,
                    ((FragmentActivity) Global.getmContext()).getSupportFragmentManager()
            );

            ajax.id(R.id.et_date).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateTimePickerEvent.show();
                }
            });

            ajax.id(R.id.btn_Add).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            String date = ajax.id(R.id.et_date).getText().toString();
                            String s_event = ajax.id(R.id.et_event).getText().toString();

                            if (date == null || date.equals("")) {
                                ajax.id(R.id.et_date).getEditText().setError("Date is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            if (s_event == null || s_event.equals("")) {
                                ajax.id(R.id.et_event).getEditText().setError("Date is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            progressDialog.show();
                            hangout(s_event, date);

                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });
        }catch (InflateException e){
            e.printStackTrace();
        }
        return v;

    }


    public void hangout(String hangout,String date){
        Gson gson =  new Gson();
        JsonHangout json=new JsonHangout();

        json.Date=date;
        json.Hangout=hangout;
        json.Token=Global.Token();


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", gson.toJson(json)));

        //decrypt url
        StringXORer stringXORer=  new StringXORer();
        String x=stringXORer.decode(StringXORer.ReverseS(Global.URL()), Global.getmContext().getPackageName());


        HttpRequest d = new HttpRequest(uiHandler, x+"HangoutHandler.ashx", nameValuePairs);
        d.start();
    }


    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            ajax.id(R.id.btn_submit).enabled(true);
            Bundle b = msg.getData();
            Exception ex= new Exception("Error in Connection...");
            try {

                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                progressDialog.dismiss();
                if(!hasError && Response!=null && !Response.equals("")){

                    if(Response.equalsIgnoreCase("true")) {
                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Event Successfully updated");
                        ajax.id(R.id.et_date).clear();
                        ajax.id(R.id.et_event).clear();
                    }
                    else {
                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Unsuccessful update\nPlease try again..");
                    }

                }else{
                    ErrorAlert ea= new ErrorAlert(Global.getmContext());
                    ea.showErrorDialog("Hangout",ex.getMessage());
                }

            } catch (Exception e) {

                ErrorAlert ea= new ErrorAlert(Global.getmContext());
                ea.showErrorDialog("Hangout",ex.getMessage());

            }
        };
    };

    @Override
    public void DateTimeSet(Date date) {
        DateTime mDateTime = new DateTime(date);
        // Show in the LOGCAT the selected Date and Time
        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
        ajax.id(R.id.et_date).text(mDateTime.getDateString());
    }
}
