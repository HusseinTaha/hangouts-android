package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonSignUp {



    public static JsonSignUp data=new JsonSignUp();


    @SerializedName("Name")
    public String Name;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("Country")
    public int Country;

    @SerializedName("Mac_Address")
    public String Mac_Address;

    @SerializedName("Token")
    public String Token;

    
}
