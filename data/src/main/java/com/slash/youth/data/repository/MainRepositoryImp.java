package com.slash.youth.data.repository;


import com.slash.youth.data.ApiClient;
import com.slash.youth.data.api.transformer.ErrorTransformer;
import com.slash.youth.data.util.RetrofitUtil;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.FindDemand;
import com.slash.youth.domain.bean.FindServices;
import com.slash.youth.domain.bean.HomeTagInfoBean;
import com.slash.youth.domain.bean.TaskList;
import com.slash.youth.domain.repository.MainRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/10/11
 */
@Singleton
public class MainRepositoryImp implements MainRepository {

    ApiClient apiClient;


    @Inject
    public MainRepositoryImp(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Observable<BannerConfigBean> getBanners(String def) {
        return apiClient.getBanners(RetrofitUtil.toRequestBody(def));
    }

    @Override
    public Observable<HomeTagInfoBean> getTags(String def) {
        return apiClient.getTags(RetrofitUtil.toRequestBody(def));
    }

    @Override
    public Observable<FindServices> getFindServices(String limit) {
        return apiClient.getFindServices(RetrofitUtil.toRequestBody(limit)).compose(new ErrorTransformer());
    }

    @Override
    public Observable<FindDemand> getFindDemand(String limit) {
        return apiClient.getFindDemand(RetrofitUtil.toRequestBody(limit)).compose(new ErrorTransformer());
    }

    @Override
    public Observable<TaskList> getTaskList(String def) {
        return apiClient.getTaskList(RetrofitUtil.toRequestBody(def)).compose(new ErrorTransformer());
    }
}
