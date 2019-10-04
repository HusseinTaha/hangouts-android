package com.comred.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.comred.Controls.CstmTextView;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.hangout.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by husseintaha on 11/27/14.
 * this class is used to add hangout events,
 * this events are displayed as checkboxes
 * with ability to share many events at the same time.
 */
public class EventsAdapter extends BaseAdapter {



    private Context mcontext;
    private List<JsonSchedules> events;

    public EventsAdapter(Context ctx,List<JsonSchedules> hg){
        this.mcontext= ctx;
        this.events=  new ArrayList<JsonSchedules>();
        this.events.addAll(hg);
        this.removecheckedItems();
    }

    public void addData(List<JsonSchedules> hg){
        if(events==null)
            events= new ArrayList<JsonSchedules>();
        this.events.addAll(hg);
        this.removecheckedItems();
    }

    public void clear(){
        this.events.clear();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<JsonSchedules> getcheckedItems(){
        List<JsonSchedules> temp =  new ArrayList<JsonSchedules>();
        for(JsonSchedules js : events){
            if(js.getSelected())
                temp.add(js);
        }
        return temp;
    }
    public void removecheckedItems(){
        for(JsonSchedules js : events){
            if(js.getSelected())
                js.setSelected(false);
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v=convertView;
        CstmTextView tv_event;
        CheckBox cb_event;

        if (v == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.event_item, null);

        }

        tv_event=(CstmTextView)v.findViewById(R.id.tv_event);
        cb_event=(CheckBox)v.findViewById(R.id.cb_event);

        final JsonSchedules event= events.get(position);

        SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        java.util.Date dateObj = null;
        try {
            dateObj = curFormater.parse(event.Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateObj);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        tv_event.setText(event.Name+" : "+event.Hangout +" at "+ String.format("%02d",hour) + ":" +String.format("%02d",minute)+":"+String.format("%02d",second));
        cb_event.setChecked(event.getSelected());


        cb_event.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                events.get(position).setSelected(isChecked);
            }
        });

        return v;
    }
}
