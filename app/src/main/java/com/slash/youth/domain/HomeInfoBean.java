package com.slash.youth.domain;

/**
 * 首页消息 消息列表实体类
 * Created by zhouyifeng on 2016/10/11.
 */
public class HomeInfoBean {

    public boolean isSlashLittleHelper;
    public boolean hasRelatedTasks;
    public String username;
    public boolean isAddV;

    public HomeInfoBean(boolean isSlashLittleHelper, boolean hasRelatedTasks, String username, boolean isAddV) {
        this.isSlashLittleHelper = isSlashLittleHelper;
        this.hasRelatedTasks = hasRelatedTasks;
        this.username = username;
        this.isAddV = isAddV;
    }
}
