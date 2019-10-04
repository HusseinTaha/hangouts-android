package com.comred.JsonDATA;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HTaha on 1/10/14.
 */
public class JsonContact {



    public static List<JsonContact> data=new ArrayList<JsonContact>();

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("Country")
    public int Country;

    @SerializedName("ContactName")
    public String ContactName;

    @SerializedName("ID")
    public int ID;



}
