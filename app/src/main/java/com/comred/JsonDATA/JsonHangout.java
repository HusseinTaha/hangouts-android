package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonHangout {



    public static JsonHangout data=new JsonHangout();


    @SerializedName("Date")
    public String Date;

    @SerializedName("Hangout")
    public String Hangout;

    @SerializedName("Token")
    public String Token;

}
