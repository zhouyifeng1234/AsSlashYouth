package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.PublishServiceProtocol;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/1.
 * 服务相关操作
 */
public class ServiceEngine {


    /**
     * 一、[服务]-发布服务
     *
     * @param onPublishServiceFinished
     * @param title
     * @param listTag
     * @param startime
     * @param endtime
     * @param anonymity
     * @param desc
     * @param timetype
     * @param listPic
     * @param instalment
     * @param bp
     * @param pattern
     * @param place
     * @param lng
     * @param lat
     * @param quote
     * @param quoteunit
     */
    public static void publishService(BaseProtocol.IResultExecutor onPublishServiceFinished, String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, int timetype, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        PublishServiceProtocol publishServiceProtocol = new PublishServiceProtocol(title, listTag, startime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        publishServiceProtocol.getDataFromServer(onPublishServiceFinished);
    }


}
