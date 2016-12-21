package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.AppointmentServiceResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/20.
 */
public class AppointmentServiceProtocol extends BaseProtocol<AppointmentServiceResultBean> {
    private String id;

    public AppointmentServiceProtocol(String id) {
        this.id = id;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.APPOINTMENT_SERVICE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
    }

    @Override
    public AppointmentServiceResultBean parseData(String result) {
        return appointmentServiceResultBean;
    }

    AppointmentServiceResultBean appointmentServiceResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        appointmentServiceResultBean = gson.fromJson(result, AppointmentServiceResultBean.class);
        if (appointmentServiceResultBean.rescode == 0 && appointmentServiceResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
