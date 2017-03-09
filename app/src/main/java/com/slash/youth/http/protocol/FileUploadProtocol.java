package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/23.
 */
public class FileUploadProtocol extends BaseProtocol<UploadFileResultBean> {

    String mFilePath;

    public FileUploadProtocol(String filePath) {
        mFilePath = filePath;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.IMG_UPLOAD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
//        params.setAsJsonContent(false);
//        params.setMultipart(true);
//        params.addBodyParameter("filename", new File("/storage/emulated/0/360WiFi/file/4.jpg"));
        List<KeyValue> multipartParams = new ArrayList<KeyValue>();
        multipartParams.add(new KeyValue("filename", new File(mFilePath)));
        MultipartBody mb = new MultipartBody(multipartParams, null);
        params.setRequestBody(mb);
    }

    @Override
    public UploadFileResultBean parseData(String result) {
        return mUploadFileResultBean;
    }

    UploadFileResultBean mUploadFileResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        mUploadFileResultBean = gson.fromJson(result, UploadFileResultBean.class);
        if (mUploadFileResultBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
