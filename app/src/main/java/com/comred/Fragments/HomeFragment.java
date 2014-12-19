package com.comred.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.comred.Glob.Global;
import com.comred.Listeners.HomeClickListener;
import com.comred.hangout.R;


/*
* Home Fragment contains the main items for the app.
* links for all the items.
* */
public class HomeFragment extends Fragment implements View.OnClickListener {


    public HomeFragment() {
        // Required empty public constructor
    }

    private HomeClickListener homeClickListener;

    public void setHomeClickListener(HomeClickListener listener) {
        this.homeClickListener = listener;
    }


    private AQuery ajax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        try {
            if (v != null) {
                ajax = new AQuery(v);
                int width = Global.getwidth();
                int height = Global.getheight();

                ajax.id(R.id.iv_schedule_home).clicked(this);
                ajax.id(R.id.iv_contacts_home).clicked(this);
                ajax.id(R.id.iv_upload_home).clicked(this);
                ajax.id(R.id.iv_photowall_home).clicked(this);


                ajax.id(R.id.iv_schedule_home).getView().getLayoutParams().width = width * 41 / 100;
                ajax.id(R.id.iv_schedule_home).getView().getLayoutParams().height = height * 23 / 100;

                ajax.id(R.id.iv_contacts_home).getView().getLayoutParams().width = width * 41 / 100;
                ajax.id(R.id.iv_contacts_home).getView().getLayoutParams().height = height * 23 / 100;

                ajax.id(R.id.iv_upload_home).getView().getLayoutParams().width = width * 41 / 100;
                ajax.id(R.id.iv_upload_home).getView().getLayoutParams().height = height * 23 / 100;

                ajax.id(R.id.iv_photowall_home).getView().getLayoutParams().width = width * 41 / 100;
                ajax.id(R.id.iv_photowall_home).getView().getLayoutParams().height = height * 23 / 100;
            }
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        if (homeClickListener != null) {
            String title = "";
            switch (v.getId()) {
                case R.id.iv_schedule_home:
                    title = "My Schedule";
                    break;
                case R.id.iv_contacts_home:
                    title = "Friends Schedule";
                    break;
                case R.id.iv_upload_home:
                    title = "Upload Photos";
                    break;
                case R.id.iv_photowall_home:
                    title = "Photo Wall";
                    break;
            }
            homeClickListener.setFragmentFromHome(title, v);
        }
    }
}


