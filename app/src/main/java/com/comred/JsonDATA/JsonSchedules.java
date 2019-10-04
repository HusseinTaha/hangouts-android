package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonSchedules {



    public static List<JsonSchedules> data= new ArrayList<JsonSchedules>();


    @SerializedName("ID")
    public int ID;

    @SerializedName("ClientID")
    public int ClientID;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Date")
    public String Date;

    @SerializedName("Hangout")
    public String Hangout;



    private boolean isSelected=false;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public boolean getSelected(){
        return isSelected;
    }


    private static boolean contains (List<JsonSchedules> dt, JsonSchedules js){
        for (JsonSchedules j:dt){
            if(j.ID==js.ID)
                return true;
        }
        return false;
    }


    public static void addIfnotExists(List<JsonSchedules> scheduleses , List<JsonSchedules> newlist){
        for (JsonSchedules js : newlist){
            if(!contains(scheduleses,js))
                scheduleses.add(js);
        }
    }

    public static List<JsonSchedules> getListByDate(List<JsonSchedules> scheduleses ,int day, int month, int year){
        List<JsonSchedules> temp = new ArrayList<JsonSchedules>();
        for(JsonSchedules js: scheduleses){
            String dateStr = js.Date;

            SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            java.util.Date dateObj = null;
            try {
                dateObj = curFormater.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObj);
            //Log.d("DAte:",calendar.toString());
            if(calendar.get(Calendar.MONTH)==(month-1) && calendar.get(Calendar.DAY_OF_MONTH)==day && calendar.get(Calendar.YEAR)==year){
                temp.add(js);
            }
        }
        return temp;
    }

    
}
