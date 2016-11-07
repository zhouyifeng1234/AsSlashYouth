package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/10/23.
 */
public class DownloadFileProtocol extends BaseProtocol<byte[]> {

    String fileId;

    public DownloadFileProtocol(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.IMG_DOWNLOAD;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.setAsJsonContent(false);
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");
        params.addBodyParameter("fileId", fileId);
    }

    @Override
    public byte[] parseData(String result) {
        return null;
    }

    public byte[] parseData(byte[] result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }


    public void getDataFromServer(final IResultExecutor resultExecutor) {
        x.http().post(getRequestParams(), new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {

                LogKit.v("onSuccess");
//                LogKit.v(result);
                boolean isResultSuccess = checkJsonResult("");
                if (isResultSuccess) {
                    dataBean = parseData(result);
                    resultExecutor.execute(dataBean);
                } else {
                    resultExecutor.executeResultError("");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogKit.v("onError");
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogKit.v("onCancelled");
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogKit.v("onFinished");
            }
        });
    }
}
