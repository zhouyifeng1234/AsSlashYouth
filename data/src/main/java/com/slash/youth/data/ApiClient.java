package com.slash.youth.data;

import com.slash.youth.data.api.BaseResponse;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.CustomerService;
import com.slash.youth.domain.bean.FindDemand;
import com.slash.youth.domain.bean.FindServices;
import com.slash.youth.domain.bean.HomeTagInfoBean;
import com.slash.youth.domain.bean.LoginResult;
import com.slash.youth.domain.bean.TaskList;

import okhttp3.RequestBody;
import retrofit2.http.Body;
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

    @POST(UriMethod.GET_CUSTOMER_SERVICE)
    Observable<BaseResponse<CustomerService>> getCustomService(@Body RequestBody requestBody);


    @POST(UriMethod.TOKEN_LOGIN)
    Observable<BaseResponse<LoginResult>> login(@Body RequestBody requestBody);

    @POST(UriMethod.GET_BANNER_CONFIG)
    Observable<BannerConfigBean> getBanners(@Body RequestBody requestBody);

    @POST(UriMethod.HOME_TAG_CONFIG)
    Observable<HomeTagInfoBean> getTags(@Body RequestBody requestBody);

    @POST(UriMethod.GET_RECOMMEND_SERVICE2)
    Observable<BaseResponse<FindServices>> getFindServices(@Body RequestBody requestBody);

    @POST(UriMethod.GET_RECOMMEND_DEMAND2)
    Observable<BaseResponse<FindDemand>> getFindDemand(@Body RequestBody requestBody);

    @POST(UriMethod.GET_MY_TASK_LIST)
    Observable<BaseResponse<TaskList>> getTaskList(@Body RequestBody requestBody);


}

