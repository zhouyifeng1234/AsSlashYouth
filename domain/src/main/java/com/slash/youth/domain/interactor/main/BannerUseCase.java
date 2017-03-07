package com.slash.youth.domain.interactor.main;


import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.LoginResult;
import com.slash.youth.domain.executor.PostExecutionThread;
import com.slash.youth.domain.executor.ThreadExecutor;
import com.slash.youth.domain.interactor.UseCase;
import com.slash.youth.domain.repository.LoginRepository;
import com.slash.youth.domain.repository.MainRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/11/14
 */
public class BannerUseCase extends UseCase<BannerConfigBean> {
    MainRepository repository;

    @Inject
    public BannerUseCase(MainRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable<BannerConfigBean> buildUseCaseObservable() {
        return repository.getBanners("{\"a\":\"a\"}");
    }
}
