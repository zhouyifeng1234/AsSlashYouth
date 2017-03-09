package com.slash.youth.domain.repository;

import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.CustomerService;
import com.slash.youth.domain.bean.FindDemand;
import com.slash.youth.domain.bean.FindServices;
import com.slash.youth.domain.bean.HomeTagInfoBean;
import com.slash.youth.domain.bean.TaskList;

import rx.Observable;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/10/11
 */
public interface MainRepository {

    Observable<BannerConfigBean> getBanners(String def);

    Observable<HomeTagInfoBean> getTags(String def);

    Observable<FindServices> getFindServices(String def);

    Observable<FindDemand> getFindDemand(String def);

    Observable<TaskList> getTaskList(String def);
}
