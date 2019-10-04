package com.comred.Controls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.comred.Adapters.EventsAdapter;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.anim.DisplayNextView;
import com.comred.anim.Flip3dAnimation;
import com.comred.hangout.R;
import com.comred.mylib.Utility;
import com.comred.mylib.socialauth.DialogListener;
import com.comred.mylib.socialauth.SocialAuthAdapter;
import com.comred.mylib.socialauth.SocialAuthError;
import com.comred.mylib.socialauth.SocialAuthListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by husseintaha on 11/27/14.
 * the event view that show up  when select a date contain an event.
 * u can share any event u want in this control.
 */
public class Events_ShareCtrl extends RelativeLayout implements View.OnTouchListener,View.OnClickListener {

    private AQuery ajax;
    private View root;
    private Context mcontext;
    private List<JsonSchedules> events;
    private ListView lv_events;
    private EventsAdapter eventsAdapter;
    private boolean isOpen;
    private SocialAuthAdapter share_adapter;
    private String dlg_message , message;


    public Events_ShareCtrl(Context context) {
        super(context);
        init(context);
    }

    public Events_ShareCtrl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Events_ShareCtrl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context ctx){
        this.mcontext=ctx;
        LayoutInflater layoutInflater= (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root= layoutInflater.inflate(R.layout.event_schedule_layout,null);
        if(root!=null) {
            events=  new ArrayList<JsonSchedules>();
            ajax = new AQuery(root);
            ajax.id(R.id.ll_shareWord).getView().setOnTouchListener(this);
            lv_events= ajax.id(R.id.lv_events).getListView();

            ajax.id(R.id.iv_facebook).clicked(this);
            ajax.id(R.id.iv_twitter).clicked(this);
            ajax.id(R.id.iv_linkedin).clicked(this);

            this.addView(root);

            share_adapter = new SocialAuthAdapter(new ResponseListener());

            // Add providers

            share_adapter.addProvider(SocialAuthAdapter.Provider.FACEBOOK, R.drawable.fbicon);
            share_adapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
            share_adapter.addCallBack(SocialAuthAdapter.Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
            share_adapter.addProvider(SocialAuthAdapter.Provider.LINKEDIN, R.drawable.lnkedinicon);


        }
        isOpen=false;
    }

    // fill events from calendar fragment.
    public void fillEvents(List<JsonSchedules> hangoutList){
        if(events==null)
            events=  new ArrayList<JsonSchedules>();

        this.events.addAll(hangoutList);
        if(eventsAdapter==null)
            eventsAdapter=  new EventsAdapter(mcontext,events);
        else{
            eventsAdapter.clear();
            eventsAdapter.addData(events);
        }
        //eventsAdapter.removecheckedItems();
        lv_events.setAdapter(eventsAdapter);
        this.show();
    }


    //to show the event layout with checkboxes
    public void show(){

        this.startAnimation(Global.inFromBottomAnimation(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
                isOpen=true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }));

    }

    //to hide this layout events, if no data available.
    public void hide(){
        if(events!=null)
            events.clear();
        if(isOpen){
            applyRotation(0, 90, true, (LinearLayout) ajax.id(R.id.ll_shareOptions).getView(),
                    (LinearLayout) ajax.id(R.id.ll_shareWord).getView());
            this.startAnimation(Global.outToBottomAnimation(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setVisibility(View.INVISIBLE);
                    isOpen = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            }));
        }
    }



    @Override
    public void onClick(View v) {

        List<JsonSchedules> data= eventsAdapter.getcheckedItems();
        if(data==null || data.isEmpty() )
        {
            ErrorAlert ea= new ErrorAlert(Global.getmContext());
            ea.showErrorDialog("Error","Please check the events do you want to share.");
            return;
        }
        ProgressDialog pd=new ProgressDialog(Global.getmContext());
        pd.setCancelable(false);
        pd.setMessage("Loading");
        pd.show();

        ajax.id(R.id.iv_facebook).enabled(false);
        ajax.id(R.id.iv_twitter).enabled(false);
        ajax.id(R.id.iv_linkedin).enabled(false);
        int identifier=Integer.parseInt(String.valueOf(v.getTag()));

        switch (identifier){
            case 0:
                dlg_message="Successfully shared\nFacebook";
                break;
            case 1:
                dlg_message="Successfully shared\nTwitter";
                break;
            case 2:
                dlg_message="Successfully shared\nLinkedIn";
                break;
            default:
                break;
        }
        share_adapter.Share(Global.getmContext(),identifier);

        pd.dismiss();
    }


    // Response Listener  for twitter and linkedin
    private final class ResponseListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {

            Log.d("ShareButton", "Authentication Successful");

            // Get name of provider after authentication
            final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
            Log.d("ShareButton", "Provider Name = " + providerName);
            Utility.showtoast(Global.getmContext(), providerName + " connected");

            // Please avoid sending duplicate message. Social Media Providers
            // block duplicate messages.

            List<JsonSchedules> data= eventsAdapter.getcheckedItems();
            if(data==null || data.isEmpty() )
            {
                ErrorAlert ea= new ErrorAlert(Global.getmContext());
                ea.showErrorDialog("Error","Please check the events do you want to share.");
                return;
            }
            for(JsonSchedules js : data) {
                SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                java.util.Date dateObj = null;
                try {
                    dateObj = curFormater.parse(js.Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateObj);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                String msg=js.Name+" : "+js.Hangout +" at "+ String.format("%02d",hour) + ":" +String.format("%02d",minute)
                        +":"+String.format("%02d",second) +" using HangoutApp";

                share_adapter.updateStatus(msg, new MessageListener(), false);
            }
            eventsAdapter.removecheckedItems();
            eventsAdapter.notifyDataSetChanged();
            //lv_events.invalidate();
        }

        @Override
        public void onError(SocialAuthError error) {
            Log.d("ShareButton", "Authentication Error: " + error.getMessage());

            ((Activity)Global.getmContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ajax.id(R.id.iv_facebook).enabled(true);
                    ajax.id(R.id.iv_twitter).enabled(true);
                    ajax.id(R.id.iv_linkedin).enabled(true);
                }
            });

        }

        @Override
        public void onCancel() {
            Log.d("ShareButton", "Authentication Cancelled");

            ((Activity)Global.getmContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ajax.id(R.id.iv_facebook).enabled(true);
                    ajax.id(R.id.iv_twitter).enabled(true);
                    ajax.id(R.id.iv_linkedin).enabled(true);
                }
            });

        }

        @Override
        public void onBack() {
            Log.d("Share-Button", "Dialog Closed by pressing Back Key");

            ((Activity)Global.getmContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ajax.id(R.id.iv_facebook).enabled(true);
                    ajax.id(R.id.iv_twitter).enabled(true);
                    ajax.id(R.id.iv_linkedin).enabled(true);
                }
            });

        }

    }

    // To get status of message after authentication
    private final class MessageListener implements SocialAuthListener<Integer> {
        @Override
        public void onExecute(String provider, Integer t) {
            Integer status = t;
            ajax.id(R.id.iv_facebook).enabled(true);
            ajax.id(R.id.iv_twitter).enabled(true);
            ajax.id(R.id.iv_linkedin).enabled(true);

            if (status.intValue() == 200 || status.intValue() == 201 || status.intValue() == 204){
                Utility.showtoast(Global.getmContext(), "Message posted on " + provider);
                ErrorAlert ea=  new ErrorAlert(Global.getmContext());
                ea.showErrorDialog("Social Media",dlg_message);
            }
            else
                Utility.showtoast(Global.getmContext(), "Message not posted on " + provider);
        }

        @Override
        public void onError(SocialAuthError e) {
            Log.d("Share-Button", e.getMessage());
            ajax.id(R.id.iv_facebook).enabled(true);
            ajax.id(R.id.iv_twitter).enabled(true);
            ajax.id(R.id.iv_linkedin).enabled(true);
        }
    }


    private void applyRotation(float start, float end,final Boolean isfirst ,final  LinearLayout ll1, final LinearLayout ll2) {

// Find the center of image

        final float centerX = ll1.getWidth() / 2.0f;

        final float centerY = ll1.getHeight() / 2.0f;



// Create a new 3D rotation with the supplied parameter

// The animation listener is used to trigger the next animation

        final Flip3dAnimation rotation =

                new Flip3dAnimation(start, end, centerX, centerY);

        rotation.setDuration(400);

        rotation.setFillAfter(false);

        rotation.setInterpolator(new AccelerateInterpolator());

        rotation.setAnimationListener(new DisplayNextView(isfirst, ll1, ll2));



        if (isfirst)
        {
            ll1.startAnimation(rotation);
        } else {
            ll2.startAnimation(rotation);
        }



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        applyRotation(0,90,true,(LinearLayout)ajax.id(R.id.ll_shareWord).getView(),(LinearLayout)ajax.id(R.id.ll_shareOptions).getView());
        return true;
    }
}
