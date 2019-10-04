package com.comred.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.androidquery.AQuery;
import com.comred.Controls.ErrorAlert;
import com.comred.Glob.Global;
import com.comred.hangout.MainActivity;
import com.comred.hangout.R;
import com.comred.mylib.Connection.UploadPhoto;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * you can upload photo here in this fragment, to share it with your friends.
 */
public class UploadPhotosFragment extends android.support.v4.app.Fragment implements View.OnTouchListener {


    public UploadPhotosFragment() {
        // Required empty public constructor
    }

    private AQuery ajax;
    private String filePath;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utility.clearAllResources();
        View v=null;
        int height,width;

        width= Global.getwidth();
        height=Global.getheight();

        // Inflate the layout for this fragment
        try {
            v= inflater.inflate(R.layout.fragment_upload_photos, container, false);
            if(v!=null) {
                ajax= new AQuery(v);
                ajax.id(R.id.btn_select).getView().setOnTouchListener(this);
                ajax.id(R.id.btn_upload).getView().setOnTouchListener(this);

                ajax.id(R.id.img_logo).getView().getLayoutParams().width=width*55/100;
                ajax.id(R.id.img_logo).getView().getLayoutParams().height=height*18/100;

                progressDialog= new ProgressDialog(Global.getmContext());
                progressDialog.setTitle("Upload Photos");
                progressDialog.setMessage("Please wait...");


            }
        }catch (InflateException e){
            e.printStackTrace();
        }


        return  v;
    }

    private void clear(){
        ajax.id(R.id.btn_upload).enabled(false);
        ajax.id(R.id.iv_photo).clear();
    }


    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            ajax.id(R.id.btn_submit).enabled(true);
            Bundle b = msg.getData();
            Exception ex= new Exception("Error in Connection...");
            try {

                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");

                if(progressDialog!=null)
                    progressDialog.dismiss();

                if(!hasError && Response!=null && !Response.equals("")){


                    if(Response.equalsIgnoreCase("true")) {    // signup completed.
                        clear();
                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Success");
                    }
                    else {    //false logout. user deja existant

                        ErrorAlert ea= new ErrorAlert(Global.getmContext());
                        ea.showErrorDialog("Hangout","Unsuccessful Upload\nPlease try again..");

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
    public void setImage(final String file){
        if(file==null)
            return;
        filePath=file;
        ajax.id(R.id.iv_photo).image(file);
        ajax.id(R.id.btn_upload).enabled(true);
        final Animation animation = AnimationUtils.loadAnimation(Global.getmContext().getApplicationContext(), R.anim.wobble);
        Handler h= new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                ajax.id(R.id.btn_upload).animate(animation);
            }
        },700);


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                v.getBackground().setColorFilter(0xDAF6F4ff, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:

                switch (v.getId()){
                    case R.id.btn_select:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        ((Activity)Global.getmContext()).startActivityForResult(Intent.createChooser(intent, "Select Picture"), MainActivity.PICK_IMAGE);
                        break;
                    case R.id.btn_upload:
                        JSONObject json = new JSONObject();
                        try {
                            json.put("Method", "AddPhoto");
                            json.put("Token", Global.Token());
                            JSONObject params = new JSONObject();
                            json.put("Params", params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        StringXORer stringXORer=  new StringXORer();
                        String x=stringXORer.decode(StringXORer.ReverseS(Global.URL()), Global.getmContext().getPackageName());

                        UploadPhoto up =  new UploadPhoto(Global.getmContext(),filePath,uiHandler);
                        up.setURL(x+"UploadHandler.ashx");

                        up.setJson(json.toString());
                        if(progressDialog!=null)
                            progressDialog.show();

                        up.start();
                        ajax.id(R.id.btn_upload).getButton().clearAnimation();
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
}
