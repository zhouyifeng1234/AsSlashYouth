package com.slash.youth.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/10/24.
 */
public class SkillLabelAllBean  implements Serializable {
    /**
     * f1 : 0
     * f2 : 0
     * id : 1
     * tag : 设计
     */

    private int f1;
    private int f2;
    private int id;
    private String tag;

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getF2() {return f2;}

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }




}
