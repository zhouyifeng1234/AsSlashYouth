package com.slash.youth.data.repository;


import com.slash.youth.data.ApiClient;
import com.slash.youth.data.api.transformer.ErrorTransformer;
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

}
