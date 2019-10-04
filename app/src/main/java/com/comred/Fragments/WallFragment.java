package com.comred.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.comred.Adapters.WallAdapters;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonWall;
import com.comred.PullDownTools.PullToRefreshBase;
import com.comred.PullDownTools.PullToRefreshBase.State;
import com.comred.PullDownTools.PullToRefreshListView;
import com.comred.PullDownTools.SoundPullEventListener;
import com.comred.hangout.R;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * its a wall containing all photo shared by you or by your friends.
 * you can also refresh the data with Fragment.
 */
public class WallFragment extends android.support.v4.app.Fragment implements PullToRefreshBase.OnRefreshListener {


    public WallFragment() {
        // Required empty public constructor
    }

    AQuery ajax;
    private PullToRefreshListView listView;
    private WallAdapters wa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_wall, container, false);
        try{
            if (v != null) {
                int height,width;
                ajax = new AQuery(v);
                width= Global.getwidth();
                height=Global.getheight();
                ajax.id(R.id.img_logo).getView().getLayoutParams().width=width*56/100;
                ajax.id(R.id.img_logo).getView().getLayoutParams().height=height*18/100;


                listView = (PullToRefreshListView) ajax.id(R.id.lv_wallphotos).getView();
                listView.setOnRefreshListener(this);
                wa=  new WallAdapters(Global.getmContext(), JsonWall.data);

                listView.setAdapter(wa);

                /**
                 * Add Sound Event Listener
                 */
                SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(Global.getmContext());
                soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
                soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
                soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
                listView.setOnPullEventListener(soundListener);

            }
        }catch(InflateException e){
            e.printStackTrace();
        }



        return  v;
    }

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            Exception ex= new Exception("Error in Connection...");
            try {
                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");
                if(!hasError && Response!=null && !Response.equals("[]")){
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<JsonWall>>() {
                    }.getType();
                    // save index and top position

                    List<JsonWall> data = ( List<JsonWall> ) (Object)gson.fromJson(Response, listType);
                    JsonWall.data.addAll(data);
                    JsonWall.maxID();
                    wa.notifyDataSetChanged();
                    wa.add(data);
                    wa.notifyDataSetChanged();

                }

            } catch (Exception e) {
                // TODO: handle exception
                ex.printStackTrace();
            }finally {
                listView.onRefreshComplete();
            }
        };
    };


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        try{
            JSONObject params = new JSONObject();
            JSONObject json=new JSONObject();
            try {
                params.put("DATA",JsonWall.max);
                json.put("Token", Global.Token());
                json.put("Method", "RefreshWall");
                json.put("Params", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

            StringXORer stringXORer = new StringXORer();
            String x = stringXORer.decode(StringXORer.ReverseS(Global.URL()), Global.getmContext().getPackageName());


            HttpRequest d = new HttpRequest(uiHandler, x+"RefreshHandler.ashx", nameValuePairs);
            d.start();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
