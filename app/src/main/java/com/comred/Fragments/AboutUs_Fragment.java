package com.comred.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.comred.Glob.Global;
import com.comred.hangout.R;
import com.comred.mylib.Utility;

/**
 * Created by HTaha on 2/6/14.
 * About us fragment to show an overview of the App.
 */
public class AboutUs_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utility.clearAllResources();
        View v=null;
        int height,width;
        AQuery ajax;

        // Inflate the layout for this fragment
        try {
            v= inflater.inflate(R.layout.aboutus_fragment, container, false);
            if(v==null)
                return null;
            ajax=new AQuery(v);
            width= Global.getwidth();
            height=Global.getheight();

            ajax.id(R.id.iv_aboutus_top).getView().getLayoutParams().width=width*54/100;
            ajax.id(R.id.iv_aboutus_top).getView().getLayoutParams().height=height*16/100;

            ajax.id(R.id.iv_aboutus_bottom).getView().getLayoutParams().width=width*54/100;
            ajax.id(R.id.iv_aboutus_bottom).getView().getLayoutParams().height=height*16/100;

            TextView tv= (TextView) v.findViewById(R.id.about_text);
            tv.setText(getString(R.string.s_about_text));
            tv.setTypeface(Global.getTypeFace());


//Determine screen size
            if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                tv.setTextSize(22);
            }
            else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                tv.setTextSize(20);
            }
            else if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                tv.setTextSize(17);
            }
            else
            if ((getResources().getConfiguration().screenLayout &      Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                tv.setTextSize(24);
            }
            else{
                tv.setTextSize(19);
            }


        }catch (InflateException e){
            e.printStackTrace();
        }


        return  v;
    }
}
