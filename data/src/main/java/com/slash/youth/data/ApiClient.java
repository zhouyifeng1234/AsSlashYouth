package com.slash.youth.data;

import com.slash.youth.data.api.BaseResponse;
import com.slash.youth.domain.bean.CustomerService;
import com.slash.youth.domain.bean.LoginResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/8/8
 */

public interface ApiClient {

    @FormUrlEncoded
    @POST(UriMethod.GET_CUSTOMER_SERVICE)
    Observable<BaseResponse<CustomerService>> getCustomService(@Field("a") String field);


    @FormUrlEncoded
    @POST(UriMethod.TOKEN_LOGIN)
    Observable<BaseResponse<LoginResult>> login(@Field("a") String field);
}

