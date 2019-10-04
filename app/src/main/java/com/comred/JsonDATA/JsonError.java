package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonError {



    public static JsonError data=new JsonError();


    @SerializedName("Error")
    public int Error;

    @SerializedName("Details")
    public String Details;


    
}
