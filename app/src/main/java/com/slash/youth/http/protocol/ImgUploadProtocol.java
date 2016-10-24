package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

import java.io.File;

/**
 * 一、[文件]-图片上传
 * Created by zhouyifeng on 2016/10/21.
 */
public class ImgUploadProtocol extends BaseProtocol<String> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.IMG_UPLOAD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
//        params.addBodyParameter("filename", "MyImage.jpg");
        params.addBodyParameter("filename", new File("/storage/emulated/0/DCIM/Screenshots/Screenshot_2016-03-07-19-58-44.png"), "application/octet-stream", "MyImage.jpg");
    }

    @Override
    public String parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
