package com.comred.mylib;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by Administrator on 27/09/13.
 */
public  class TapGestureListener extends GestureDetector.SimpleOnGestureListener{
    private Activity activity;



    public TapGestureListener(Activity mcontext){
        super();
        this.activity=mcontext;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {


        try {

            Utility.hideSoftKeyboard(activity);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return false;
        // Your Code here
    }
}