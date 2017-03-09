package com.slash.youth.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zss on 2017/2/6.
 */
@Entity
public class CityHistoryEntity {
    private String city;

    @Transient
    private int tempUsageCount; // not persisted

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Generated(hash = 1501431064)
    public CityHistoryEntity(String city) {
        this.city = city;
    }

    @Generated(hash = 587496221)
    public CityHistoryEntity() {
    }


}
