package com.comred.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.comred.Adapters.CalendarAdapter;
import com.comred.Adapters.WeeksAdapter;
import com.comred.Controls.CstmTextView;
import com.comred.Controls.Events_ShareCtrl;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.hangout.R;
import com.comred.mylib.Connection.HttpRequest;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.mylib.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/*
* Calendar fragment to show a calendar grid for a month.
* it has the ability to show a previous, next month , and hangout events of a date. */

public class CalendarFragment extends Fragment {


    private GregorianCalendar month;// calendar instances.

    private CalendarAdapter adapter;// adapter instance

    private GridView gv_days,gv_calendar;

    static final String[] numbers = new String[] {
            "MON", "TUE", "WED", "THU", "FRI",
            "SAT", "SUN"};
    private CstmTextView tv_month,tv_year;

    private Events_ShareCtrl events_shareCtrl;

    public CalendarFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Locale.setDefault(Locale.US);
        Utility.clearAllResources();
        View v=null;
        // Inflate the layout for this fragment
        try {
            v= inflater.inflate(R.layout.fragment_calendar, container, false);
            if(v==null)
                return null;

            tv_month=(CstmTextView)v.findViewById(R.id.tv_month);
            tv_year=(CstmTextView)v.findViewById(R.id.tv_year);

            gv_days = (GridView) v.findViewById(R.id.gv_days);
            gv_days.setAdapter(new WeeksAdapter(Global.getmContext(),numbers));
            month = (GregorianCalendar) GregorianCalendar.getInstance();
            events_shareCtrl= (Events_ShareCtrl)v.findViewById(R.id.esc_eventControl);
            adapter = new CalendarAdapter(Global.getmContext(), month);
            adapter.setEventData(events_shareCtrl);
            gv_calendar = (GridView) v.findViewById(R.id.gv_calendar);
            gv_calendar.setAdapter(adapter);



            ImageView previous = (ImageView) v.findViewById(R.id.iv_previous);

            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setPreviousMonth();
                    refreshCalendar();
                }
            });
            ImageView next = (ImageView) v.findViewById(R.id.iv_next);
            next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setNextMonth();
                    refreshCalendar();

                }
            });

            gv_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    adapter.setClickedChildPosition(position);
                    adapter.notifyDataSetChanged();
                    events_shareCtrl.hide();
                }

            });

            refreshHeader();

        }catch (InflateException e){
            e.printStackTrace();
        }

        return  v;
    }


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }
        getData(month.get(Calendar.MONTH)+1,month.get(Calendar.YEAR));

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        getData(month.get(Calendar.MONTH)+1,month.get(Calendar.YEAR));
    }

    private void refreshHeader(){
        String monthname=(String)android.text.format.DateFormat.format("MMMM", month);
        tv_month.setText(monthname);
        tv_year.setText(String.valueOf(month.get(Calendar.YEAR)));

    }

    public void refreshCalendar() {

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        gv_days.invalidate();
        refreshHeader();
    }

    private  void getData(int month, int year){
        try{
            JSONObject params = new JSONObject();
            JSONObject json=new JSONObject();
            try {
                params.put("DATA", month+"-"+year);
                json.put("Token", Global.Token());
                json.put("Method", "RefreshShcedule");
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

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            Exception ex= new Exception("Error in Connection...");
            try {
                boolean hasError = b.getBoolean("hasError");
                String Response = b.getString("Response");
                if(!hasError && Response!=null && !Response.equals("[]")){
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<JsonSchedules>>() {
                    }.getType();
                    // save index and top position

                    List<JsonSchedules> data = ( List<JsonSchedules> ) (Object)gson.fromJson(Response, listType);
                    JsonSchedules.addIfnotExists(JsonSchedules.data,data);
                    refreshCalendar();
                }

            } catch (Exception e) {
                // TODO: handle exception
                ex.printStackTrace();
            }
        };
    };


}
