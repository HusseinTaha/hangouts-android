package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonLogin {



    public static JsonLogin data=new JsonLogin();

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("Country")
    public int Country;

    @SerializedName("Mac_Address")
    public String Mac_Address;

    @SerializedName("Token")
    public String Token;

    
}
