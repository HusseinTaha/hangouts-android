package com.comred.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.comred.Controls.ErrorAlert;
import com.comred.Glob.Global;
import com.comred.hangout.R;
import com.comred.hangout.Signup_login;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
* this settings fragment contain controls to manage your account.
* */
public class SettingsFragment extends android.support.v4.app.Fragment implements View.OnTouchListener{

    private AQuery ajax;
    private int width,height;

    private boolean notifEnabled=true;
    private int task=-1;
    private String token;

    ProgressDialog progressDialog;

    public SettingsFragment() {
        // Required empty public constructor
        width=Global.getwidth();
        height=Global.getheight();
        progressDialog=  new ProgressDialog(Global.getmContext());
        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Hangout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_settings, container, false);
        notifEnabled=Global.isNotificationEnabled();
        try{
            if (v != null) {
                ajax = new AQuery(v);

               updateNotif();

                ajax.id(R.id.btn_changePassword).getView().setOnTouchListener(this);
                ajax.id(R.id.btn_SyncContacts).getView().setOnTouchListener(this);
                ajax.id(R.id.btn_changeName).getView().setOnTouchListener(this);
                //ajax.id(R.id.ll_notif).getView().setOnTouchListener(this);
                ajax.id(R.id.btn_signout).getView().setOnTouchListener(this);
                ajax.id(R.id.ll_delete).getView().setOnTouchListener(this);

                ajax.id(R.id.btn_changePassword).getView().getLayoutParams().height=height*8/100;
                ajax.id(R.id.btn_SyncContacts).getView().getLayoutParams().height=height*8/100;
                ajax.id(R.id.btn_changeName).getView().getLayoutParams().height=height*8/100;
                ajax.id(R.id.btn_signout).getView().getLayoutParams().height=height*8/100;
                ajax.id(R.id.ll_delete).getView().getLayoutParams().height=height*8/100;

                ajax.id(R.id.img_logo).getView().getLayoutParams().height=height*18/100;
                ajax.id(R.id.img_logo).getView().getLayoutParams().width=width*55/100;


                ajax.id(R.id.btn_changePassword).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
                ajax.id(R.id.btn_SyncContacts).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
                ajax.id(R.id.btn_changeName).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
                ajax.id(R.id.btn_signout).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
                ajax.id(R.id.tv_deleteaccount).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
            }
            }catch(InflateException e){
                e.printStackTrace();
            }



        return  v;
    }

    private void updateNotif(){
        if(notifEnabled)
            ajax.id(R.id.iv_notif_on_off).image(R.drawable.button_on);
        else
            ajax.id(R.id.iv_notif_on_off).image(R.drawable.button_off);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:


                switch (v.getId()){
                    case R.id.btn_changePassword:
                        changePassword();
                        break;
                    case R.id.btn_SyncContacts:
                        progressDialog.show();
                        syncContacts();
                        break;
                    case R.id.btn_changeName:
                        changeName();
                        break;
                    case R.id.ll_notif:
                        Global.setNotifEnabled(!Global.isNotificationEnabled());
                        updateNotif();
                        break;
                    case R.id.btn_signout:
                        signout();
                        break;
                    case R.id.ll_delete:
                        deleteAccount();
                        break;
                 }
            case MotionEvent.ACTION_CANCEL: {
                v.getBackground().clearColorFilter();
                v.invalidate();
                break;
            }
        }
        return true;
    }



    private void signout(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.signout_dialog,null);
        if(view!=null){
            final AQuery dialogAjax=  new AQuery(view);

            final AlertDialog dialog = new AlertDialog.Builder(Global.getmContext()).create();
            dialogAjax.id(R.id.btn_no).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            dialog.dismiss();
                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });

            dialogAjax.id(R.id.btn_yes).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            Global.delete();
                            Context ctc= Global.getmContext();
                            Intent i=  new Intent(ctc, Signup_login.class);
                            startActivity(i);
                            ((Activity)ctc).finish();

                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });


            dialog.setView(view);
            dialog.show();

        }
    }

    private void deleteAccount(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_account_dialog,null);
        if(view!=null){
            final AQuery dialogAjax=  new AQuery(view);
            /*int scale=2;
            dialogAjax.id(R.id.tv_password).height(2+scale*height/100);
            dialogAjax.id(R.id.et_password).height(scale*height/100);*/

            dialogAjax.id(R.id.tv_password).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.et_password).getView().getLayoutParams().height=height*7/100;

            dialogAjax.id(R.id.tv_password).getView().getLayoutParams().width=width*30/100;

            dialogAjax.id(R.id.tv_password).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);
            dialogAjax.id(R.id.et_password).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);


            /*scale=17;
            dialogAjax.id(R.id.tv_password).width(scale*width/100);*/

            final AlertDialog dialog = new AlertDialog.Builder(Global.getmContext()).create();
            dialogAjax.id(R.id.btn_no).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                          dialog.dismiss();
                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });

            dialogAjax.id(R.id.btn_yes).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            String old_pass=dialogAjax.id(R.id.et_password).getText().toString();
                            if( old_pass==null || old_pass.equals("")){
                                dialogAjax.id(R.id.et_password).getEditText().setError("Password is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }

                            String oldToken=Utility.sha1(Global.getStringPref("CountryCode")+":"+Global.getStringPref("Mobile")+":"+old_pass);
                            if(!oldToken.equals(Global.Token())){
                                dialogAjax.id(R.id.et_password).getEditText().setError("Password is Wrong !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            dialogAjax.id(R.id.btn_yes).enabled(false);

                            task=4;
                            JSONObject params=  new JSONObject();
                            request("DeleteAccount",params);
                            dialog.dismiss();


                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });


            dialog.setView(view);
            dialog.show();

        }
    }


    private void changePassword(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog,null);
        if(view!=null){
            final AQuery dialogAjax=  new AQuery(view);
            /*int scale=2;
            dialogAjax.id(R.id.tv_old_pass).height(2+scale*height/100);
            dialogAjax.id(R.id.tv_new_pass).height(2+scale*height/100);
            dialogAjax.id(R.id.et_old_pass).height(scale*height/100);
            dialogAjax.id(R.id.et_new_pass).height(scale*height/100);*/

            dialogAjax.id(R.id.tv_old_pass).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.tv_new_pass).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.et_old_pass).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.et_new_pass).getView().getLayoutParams().height=height*7/100;


            dialogAjax.id(R.id.tv_new_pass).getView().getLayoutParams().width=width*30/100;
            dialogAjax.id(R.id.tv_old_pass).getView().getLayoutParams().width=width*30/100;

            dialogAjax.id(R.id.tv_old_pass).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);
            dialogAjax.id(R.id.tv_new_pass).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);
            dialogAjax.id(R.id.et_old_pass).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            dialogAjax.id(R.id.et_new_pass).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);

            dialogAjax.id(R.id.btn_change).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            /*scale=17;
            dialogAjax.id(R.id.tv_new_pass).width(scale*width/100);
            dialogAjax.id(R.id.tv_old_pass).width(scale*width/100);*/

            final AlertDialog dialog = new AlertDialog.Builder(Global.getmContext()).create();

            dialogAjax.id(R.id.btn_change).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            String old_pass=dialogAjax.id(R.id.et_old_pass).getText().toString();
                            String new_pass=dialogAjax.id(R.id.et_new_pass).getText().toString();
                            if( old_pass==null || old_pass.equals("")){
                                dialogAjax.id(R.id.et_old_pass).getEditText().setError("Old Password is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            if( new_pass==null || new_pass.equals("")){
                                dialogAjax.id(R.id.et_new_pass).getEditText().setError("Old Password is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            String oldToken=Utility.sha1(Global.getStringPref("CountryCode")+":"+Global.getStringPref("Mobile")+":"+old_pass);
                            if(!oldToken.equals(Global.Token())){
                                dialogAjax.id(R.id.et_old_pass).getEditText().setError("Old Password is Wrong !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }
                            dialogAjax.id(R.id.btn_change).enabled(false);
                            JSONObject Data= new JSONObject();
                            JSONObject params= new JSONObject();
                            try {
                                token=Utility.sha1(Global.getStringPref("CountryCode")+":"+Global.getStringPref("Mobile")+":"+new_pass);
                                params.put("OLD_TOKEN",Global.Token());
                                params.put("NEW_TOKEN",token);
                                Data.put("DATA",params);
                                task=0;

                                request("ChangePassword",Data);
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });


            dialog.setView(view);
            dialog.show();

        }
    }



    private void changeName(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_name_dialog,null);
        if(view!=null){
            final AQuery dialogAjax=  new AQuery(view);
            /*int scale=2;
            dialogAjax.id(R.id.tv_old_name).height(2+scale*height/100);
            dialogAjax.id(R.id.tv_new_name).height(2+scale*height/100);
            dialogAjax.id(R.id.et_old_name).height(scale*height/100);
            dialogAjax.id(R.id.et_new_name).height(scale*height/100);*/

            dialogAjax.id(R.id.tv_old_name).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.tv_new_name).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.et_old_name).getView().getLayoutParams().height=height*7/100;
            dialogAjax.id(R.id.et_new_name).getView().getLayoutParams().height=height*7/100;

            dialogAjax.id(R.id.tv_new_name).getView().getLayoutParams().width=width*30/100;
            dialogAjax.id(R.id.tv_old_name).getView().getLayoutParams().width=width*30/100;

            dialogAjax.id(R.id.tv_old_name).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);
            dialogAjax.id(R.id.tv_new_name).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*3/100);
            dialogAjax.id(R.id.et_old_name).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);
            dialogAjax.id(R.id.et_new_name).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);

            dialogAjax.id(R.id.btn_change).getButton().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*4/100);

            /*scale=17;
            dialogAjax.id(R.id.tv_new_name).width(scale*width/100);
            dialogAjax.id(R.id.tv_old_name).width(scale*width/100);*/

            dialogAjax.id(R.id.et_old_name).text(Global.getStringPref("Name"));
            final AlertDialog dialog = new AlertDialog.Builder(Global.getmContext()).create();

            dialogAjax.id(R.id.btn_change).getButton().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {

                            v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.OVERLAY);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            String new_name=dialogAjax.id(R.id.et_new_name).getText().toString();

                            if( new_name==null || new_name.equals("")){
                                dialogAjax.id(R.id.et_new_name).getEditText().setError("Name is required !");
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                                return false;
                            }

                            dialogAjax.id(R.id.btn_change).enabled(false);
                            JSONObject params= new JSONObject();
                            try {
                                params.put("DATA",new_name);
                                task=2;

                                request("ChangeName",params);
                                Global.saveStringPref("Name",new_name);
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        case MotionEvent.ACTION_CANCEL: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });


            dialog.setView(view);
            dialog.show();

        }
    }

    private void syncContacts(){

        Global.readContacts("SettingsHandler.ashx",uiHandler);
    }


    private void request(String method,JSONObject params){
        progressDialog.show();
        JSONObject json = new JSONObject();
        try {
            json.put("Method", method);
            json.put("Token", Global.Token());
            json.put("Params", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), Global.getmContext().getPackageName());


        HttpRequest d = new HttpRequest(uiHandler, x + "SettingsHandler.ashx", nameValuePairs);
        d.start();
    }





    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            ajax.id(R.id.btn_submit).enabled(true);
            Bundle b = msg.getData();
            Exception ex= new Exception("Error in Connection...");
            try {

                progressDialog.dismiss();
                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                if(!hasError && Response!=null && !Response.equals("")){


                    if(Response.equalsIgnoreCase("true")) {
                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Success");

                        switch (task){
                            case 0://password change
                                Global.Token(token);
                                Global.saveToken();
                                break;
                            case 1://sync contacts
                                break;
                            case 2://change name
                                break;
                            case 3://notification option
                                break;
                            case 4:// delete account
                                Global.delete();
                                ((Activity)Global.getmContext()).finish();

                                break;
                        }
                        task=-1;
                    }
                    else {

                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Error\nPlease try again..");

                    }


                }else{
                    ErrorAlert ea= new ErrorAlert(Global.getmContext());
                    ea.showErrorDialog("Hangout",ex.getMessage());
                }

            } catch (Exception e) {

                // TODO: handle exception
                Utility.showtoast(Global.getmContext(), "Error in Connection ...");

            }
        };
    };


}
