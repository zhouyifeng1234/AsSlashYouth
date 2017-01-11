package com.slash.youth.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zss on 2017/1/10.
 */

@Entity
public class SearchHistoryEntity {

    @Id
    private long id;
    private String searchHistory;

    @Transient
    private int tempUsageCount; // not persisted

    @Generated(hash = 170583686)
    public SearchHistoryEntity(long id, String searchHistory) {
        this.id = id;
        this.searchHistory = searchHistory;
    }

    @Generated(hash = 691068747)
    public SearchHistoryEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(String searchHistory) {
        this.searchHistory = searchHistory;
    }


}
