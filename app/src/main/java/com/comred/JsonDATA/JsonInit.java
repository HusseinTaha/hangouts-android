package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonInit {



    public static JsonInit data=new JsonInit();


    // to check the returned response what is contained :P

    @SerializedName("isCountries")
    public boolean isCountries;

    @SerializedName("isContacts")
    public boolean isContacts;

    @SerializedName("isWalls")
    public boolean isWalls;

    @SerializedName("isSchedules")
    public boolean isSchedules;


    //values
    @SerializedName("Countries")
    public List<JsonCountry> countries;

    @SerializedName("Contacts")
    public List<JsonContact> contacts;

    @SerializedName("Walls")
    public List<JsonWall> walls;

    @SerializedName("Schedules")
    public List<JsonSchedules> schedules;
}
