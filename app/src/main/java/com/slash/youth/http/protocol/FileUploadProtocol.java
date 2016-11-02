package com.slash.youth.http.protocol;

import com.slash.youth.http.protocol.BaseProtocol;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/23.
 */
public class FileUploadProtocol extends BaseProtocol<String> {
    @Override
    public String getUrlString() {
        return "http://121.42.145.178/file/v1/api/upload";
    }

    @Override
    public void addRequestParams(RequestParams params) {
//        params.setAsJsonContent(false);
//        params.setMultipart(true);
//        params.addBodyParameter("filename", new File("/storage/emulated/0/360WiFi/file/4.jpg"));
        List<KeyValue> multipartParams = new ArrayList<KeyValue>();
        multipartParams.add(new KeyValue("filename", new File("/storage/emulated/0/360WiFi/file/4.jpg")));
        MultipartBody mb = new MultipartBody(multipartParams, null);
        params.setRequestBody(mb);
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
