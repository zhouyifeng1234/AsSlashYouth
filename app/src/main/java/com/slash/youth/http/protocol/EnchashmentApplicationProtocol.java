package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.MD5Utils;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/14.
 */
public class EnchashmentApplicationProtocol extends BaseProtocol<SetBean> {
    private double amount;
    private String address;
    private int type;
    private String password;

    public EnchashmentApplicationProtocol(double amount, String address, int type, String password) {
        this.amount = amount;
        this.address = address;
        this.type = type;
        this.password = password;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.ENCHASHMENT_APPlICATION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if(amount > 0){
            params.addBodyParameter("amount", String.valueOf(amount));
        }
        params.addBodyParameter("address", address);
        params.addBodyParameter("type", String.valueOf(type));

        String md5PassWord = MD5Utils.md5(password);
        params.addBodyParameter("password",md5PassWord);
    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean= gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
