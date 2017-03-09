package com.slash.youth.data.repository;


import com.slash.youth.data.ApiClient;
import com.slash.youth.data.api.subscriber.BaseSubscriber;
import com.slash.youth.data.api.transformer.ErrorTransformer;
import com.slash.youth.domain.bean.CustomerService;
import com.slash.youth.domain.bean.LoginResult;
import com.slash.youth.domain.repository.LoginRepository;
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
public class LoginRepositoryImp implements LoginRepository {

    ApiClient apiClient;


    @Inject
    public LoginRepositoryImp(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Observable<CustomerService> getCustomService(String def) {
        return apiClient.getCustomService(def).compose(new ErrorTransformer<>());
    }

    @Override
    public Observable<LoginResult> login(String def) {
        return apiClient.login(def).compose(new ErrorTransformer<>());
    }
}
