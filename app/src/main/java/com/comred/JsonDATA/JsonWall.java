package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonWall {



    public static List<JsonWall> data=new ArrayList<JsonWall>();

    public static int max=0;

    public static void maxID(){

        for (JsonWall js:data){
            if(js.ID>max)
                max=js.ID;
        }
    }

    @SerializedName("Name")
    public String Name;

    @SerializedName("ClientID")
    public int ClientID;

    @SerializedName("ID")
    public int ID;

    @SerializedName("Date")
    public String Date;

    @SerializedName("Photo")
    public String Photo;



    
}
