package com.comred.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comred.Controls.Events_ShareCtrl;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonSchedules;
import com.comred.hangout.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/*
* Calendar adapter
* create the days for a month and fill the adapter
* */
public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private Calendar month;
    public GregorianCalendar pmonth; // calendar instance for previous month
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    private int firstDay;
    private int maxWeeknumber;
    private int maxP;
    private int calMaxP;
    private int lastWeekDay;
    private int leftDays;
    private int mnthlength;
    private String itemvalue, curentDateString;
    private DateFormat df;
    private Events_ShareCtrl shareCtrl;
    public static List<String> dayString;
    private int clickedChildPosition=-1;
    private View previousView;
    private Handler handler;
    private int width, height;


    public void setHandler(Handler handler){
        this.handler=handler;
    }
    public void setEventData(Events_ShareCtrl shareCtrl){
            this.shareCtrl=shareCtrl;
    }

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        width= Global.getwidth();
        height=Global.getheight();
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {

        return dayString.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public void setClickedChildPosition(int newClickedChildPosition){
        this.clickedChildPosition=newClickedChildPosition;
    }


    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        LinearLayout ll;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }
        dayView = (TextView) v.findViewById(R.id.date);
        ll= (LinearLayout)v.findViewById(R.id.item_linear);

        ll.getLayoutParams().height=7*height/100;
        ll.getLayoutParams().width=11*width/100;
        dayView.setPadding(3,3,3,3);

        ll.setBackgroundResource(Color.TRANSPARENT);
        // separates daystring into parts.
        String[] separatedTime = dayString.get(position).split("-");
        // taking last part of date. ie; 2 from 2012-12-02
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        // checking whether the day is in current month or not.
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            // setting offdays to white color.
            dayView.setText(" ");
            dayView.setClickable(false);
            dayView.setFocusable(false);
            ll.setBackgroundResource(R.drawable.day_hidden);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setText(" ");
            dayView.setClickable(false);
            ll.setBackgroundResource(R.drawable.day_hidden);
            dayView.setFocusable(false);
        } else {
            dayView.setText(gridvalue);
            dayView.setTextColor(mContext.getResources().getColor(R.color.DarkBlue_bottom_top));
            String[] temp=dayString.get(position).split("-");
            List<JsonSchedules> jsc=JsonSchedules.getListByDate(JsonSchedules.data
                    ,Integer.parseInt(temp[2]),Integer.parseInt(temp[1]),Integer.parseInt(temp[0]));
            if(jsc!=null && !jsc.isEmpty()){
                ll.setBackgroundResource(R.drawable.day_with_event);
            }else{
                ll.setBackgroundResource(R.drawable.day_shown);
            }
        }



        if(position==clickedChildPosition)
        {

            ll.setBackgroundResource(R.drawable.day_selected);
            dayView.setTextColor(mContext.getResources().getColor(R.color.white));
            String[] temp=dayString.get(position).split("-");
            List<JsonSchedules> jsc=JsonSchedules.getListByDate(JsonSchedules.data
                    ,Integer.parseInt(temp[2]),Integer.parseInt(temp[1]),Integer.parseInt(temp[0]));
            if(jsc!=null && !jsc.isEmpty()){
                shareCtrl.fillEvents(jsc);
            }else{
                shareCtrl.hide();
            }

        }
        /*else {
          //  ll.setBackgroundResource(R.drawable.day_shown);
            //  rlEvent.setVisibility(View.GONE);
        }*/
        if(clickedChildPosition==-1)
        {
            if (dayString.get(position).equals(curentDateString) )
            {
                ll.setBackgroundResource(R.drawable.day_selected);
                dayView.setTextColor(mContext.getResources().getColor(R.color.white));
               // String[] temp=dayString.get(position).split("-");
               /* JsonCalendar jsc=JsonCalendar.FindByDate(temp[0],temp[1],temp[2]);
                if(jsc!=null){
                    rlEvent.setVisibility(View.VISIBLE);
                    try {
                        String decodedTitle = URLDecoder.decode(jsc.Title, "UTF-8");
                        // eventitle.setText(decodedTitle);
                        String decodeddesc = URLDecoder.decode(jsc.Description, "UTF-8");
                        //eventdesc.setText(""+decodeddesc);
                        if (handler != null) {
                            b.putString("Event_Title", decodedTitle);
                            b.putString("Event_Desc", decodeddesc);
                            b.putInt("IsLatin",jsc.IsLatin);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }else{
                    rlEvent.setVisibility(View.GONE);
                }*/
            }

        }
        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }


        // show icon if date is not empty and it exists in the items array

        return v;
    }

    public View setSelected(View view) {
        if (previousView != null) {
            previousView.setBackgroundResource(R.drawable.day_hidden);
        }
        previousView = view;
        view.setBackgroundResource(R.drawable.day_hidden);
        return view;
    }

    public void refreshDays() {
        // clear items
        dayString.clear();
        Locale.setDefault(Locale.US);
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);

        Calendar test= (GregorianCalendar) month.clone();

        int monthIndex=test.get(Calendar.MONTH);
        test.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = test.getTime();

        DateFormat sdf = new SimpleDateFormat("EEEEEEEE");
        System.out.println("First Day of Month: " + sdf.format(firstDayOfMonth));

        firstDay=getWeek(sdf.format(firstDayOfMonth));
        maxWeeknumber=getWeeksOfMonth(test,getWeek(sdf.format(firstDayOfMonth)));
        // allocating maximum row number for the gridview.

        mnthlength = maxWeeknumber * 7;
        if(firstDay==6 && mnthlength>29 && monthIndex!=1){
            maxWeeknumber+=1;
            mnthlength+=7;
        }

        // finding number of weeks in current month.

        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay );// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP+1 );

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);
            //Log.d("item",itemvalue);
        }
    }

    public int getWeeksOfMonth(Calendar c, int weekStart) {

        c.setFirstDayOfWeek(weekStart);
        int numOfWeeksInMonth = c.getActualMaximum(Calendar.WEEK_OF_MONTH);

        return numOfWeeksInMonth;
    }
    private int getWeek(String day){

        if(day.contains( "Mon" ))
            return 0;
        if(day.contains("Tue" ))
            return 1;
        if(day.contains( "Wed" ))
            return 2;
        if(day.contains( "Thu" ))
            return 3;
        if(day.contains("Fri" ))
            return 4;
        if(day.contains("Sat" ))
            return 5;
        if(day.contains("Sun") )
            return 6;

        return -1;
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        return maxP;
    }

}