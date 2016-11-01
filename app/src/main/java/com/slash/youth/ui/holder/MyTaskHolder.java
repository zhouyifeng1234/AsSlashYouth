package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.viewmodel.ItemMyTaskModel;
import com.slash.youth.utils.CommonUtils;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskHolder extends BaseHolder<MyTaskBean> {

    private ItemMyTaskModel mItemMyTaskModel;

    @Override
    public View initView() {
        ItemMyTaskBinding itemMyTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_task, null, false);
        mItemMyTaskModel = new ItemMyTaskModel(itemMyTaskBinding);
        itemMyTaskBinding.setItemMyTaskModel(mItemMyTaskModel);
        return itemMyTaskBinding.getRoot();
    }

    @Override
    public void refreshView(MyTaskBean data) {
        //加载头像
        if (TextUtils.isEmpty(data.avatar)) {
            //加载默认头像

        } else {

        }

        mItemMyTaskModel.setTaskTitle(data.title);

        long startTimeMill = data.starttime;
        String startTimeStr = convertStartTimeFormat(startTimeMill);
        mItemMyTaskModel.setStartTime(startTimeStr);

        if (data.isauth == 1) {
            mItemMyTaskModel.setAddVvisibility(View.VISIBLE);
        } else {
            mItemMyTaskModel.setAddVvisibility(View.INVISIBLE);
        }

        mItemMyTaskModel.setUsername(data.name);

        if (data.instalment == 0) {
            mItemMyTaskModel.setInstalmentText("分期到账");
        } else {
            mItemMyTaskModel.setInstalmentText("分期");
        }

        //显示报价前应该先判断是否需要显示报价
        mItemMyTaskModel.setQuote(data.quote + "");

        //抢单数量，服务端返回的是所有的抢单数量，这里需要新增的抢单数量
        mItemMyTaskModel.setBidnum(data.bidnum + "");

        if (data.type == 1) {
            //type 1 表示需求
            if (data.roleid == 1) {
                //需求发布者
                mItemMyTaskModel.setPublishDemandStatusPointVisibility(View.VISIBLE);
                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.GONE);
            } else if (data.roleid == 2) {
                //抢需求
                mItemMyTaskModel.setPublishDemandStatusPointVisibility(View.GONE);
                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.VISIBLE);
            }

        } else if (data.type == 2) {
            //type 2 表示服务
            if (data.roleid == 1) {
                //服务发布者

            } else if (data.roleid == 2) {
                //抢服务

            }
        }

    }

    public String convertStartTimeFormat(long startTimeMill) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        return "开始时间:" + sdf.format(startTimeMill);
    }
}
