package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonCountry  extends JsonObject{



    public static List<JsonCountry> data= new ArrayList<JsonCountry>();


    @SerializedName("ID")
    public int ID;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Phone_ext")
    public int Phone_ext;


    public static int getNumfromCountryCode(int cc){
        int i=0;
        for (JsonCountry js:data){
            if(js.Phone_ext==cc)
                return i;
            i++;
        }
        return 0;
    }

    public static int getIDfromCountryCode(int cc){
        for (JsonCountry js:data){
            if(js.Phone_ext==cc)
                return js.ID;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "("+Phone_ext+") " + Name;
    }

    @Override
    public Object JsonValueMember() {
        return this;
    }

    @Override
    public String JsonDisplayMember() {
        return ""+Phone_ext;
    }
}
