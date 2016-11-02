package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyTaskBinding;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class ItemMyTaskModel extends BaseObservable {
    ItemMyTaskBinding mItemMyTaskBinding;

    public ItemMyTaskModel(ItemMyTaskBinding itemMyTaskBinding) {
        this.mItemMyTaskBinding = itemMyTaskBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    /**
     * @param status 需求 or 服务状态
     * @param type   需求或者服务类型 1需求 2服务
     * @param roleid 表示是我抢的单子 还是 我发布的任务 1发布者 2抢单者 （这个字段比较重要，用于判断单子类型）
     */
    public void displayCurrentBigStatusAndButtons(int status, int type, int roleid) {
        if (status == 1) {
            setStatusText("预约中");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 2) {
            setStatusText("已过期");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 3) {
            setStatusText("已过期");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 4) {//目前服务端接口的任务状态列表中没有这个值
            setStatusText("预约中");//带选择
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

        } else if (status == 5) {
            setStatusText("预约中");//待确认
            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 6) {
            setStatusText("待支付");//待支付
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 7) {
            setStatusText("服务中");//进行中
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 8) {
            setStatusText("待评价");//已完成
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 9) {
            setStatusText("已过期");//已退款
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 10) {
            setStatusText("已过期");//已淘汰
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        }
    }

    private String taskTitle;//任务标题
    private String startTime;//任务开始时间
    private int addVvisibility;//是否显示加V认证
    private String username;//用户名
    private String instalmentText;//分期显示的文本
    private String quote;//报价
    private int publishDemandStatusPointVisibility;//我发的需求状态小圆点是否可见（里面有数字，新增的抢单者数量）
    private int bidDemandStatusPointVisibility;//我抢的需求状态小圆点是否可见（没有需求，有状态变化，只需要显示圆点）
    private String bidnum;//抢单数量，服务端返回的是所有的抢单数量，这里需要新增的抢单数量
    private String statusText;
    private String instalmentratioStr;//显示的分期支付比例


    @Bindable
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        notifyPropertyChanged(BR.taskTitle);
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public int getAddVvisibility() {
        return addVvisibility;
    }

    public void setAddVvisibility(int addVvisibility) {
        this.addVvisibility = addVvisibility;
        notifyPropertyChanged(BR.addVvisibility);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getInstalmentText() {
        return instalmentText;
    }

    public void setInstalmentText(String instalmentText) {
        this.instalmentText = instalmentText;
        notifyPropertyChanged(BR.instalmentText);
    }

    @Bindable
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
        notifyPropertyChanged(BR.quote);
    }

    @Bindable
    public int getPublishDemandStatusPointVisibility() {
        return publishDemandStatusPointVisibility;
    }

    public void setPublishDemandStatusPointVisibility(int publishDemandStatusPointVisibility) {
        this.publishDemandStatusPointVisibility = publishDemandStatusPointVisibility;
        notifyPropertyChanged(BR.publishDemandStatusPointVisibility);
    }

    @Bindable
    public int getBidDemandStatusPointVisibility() {
        return bidDemandStatusPointVisibility;
    }

    public void setBidDemandStatusPointVisibility(int bidDemandStatusPointVisibility) {
        this.bidDemandStatusPointVisibility = bidDemandStatusPointVisibility;
        notifyPropertyChanged(BR.bidDemandStatusPointVisibility);
    }

    @Bindable
    public String getBidnum() {
        return bidnum;
    }

    public void setBidnum(String bidnum) {
        this.bidnum = bidnum;
        notifyPropertyChanged(BR.bidnum);
    }

    @Bindable
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
        notifyPropertyChanged(BR.statusText);
    }

    @Bindable
    public String getInstalmentratioStr() {
        return instalmentratioStr;
    }

    public void setInstalmentratioStr(String instalmentratioStr) {
        this.instalmentratioStr = instalmentratioStr;
        notifyPropertyChanged(BR.instalmentratioStr);
    }
}
