package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.domain.SkillLabelAllBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/13.
 */
public class UploadPhotoProtocol extends BaseProtocol {
    private String filename;

    public UploadPhotoProtocol(String filename) {
        this.filename = filename;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.UPLOAD_PHOTO;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("filename", filename);
    }

    @Override
    public Object parseData(String result) {

        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
