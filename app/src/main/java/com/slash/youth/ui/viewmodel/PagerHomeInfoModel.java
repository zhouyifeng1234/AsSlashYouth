package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.PagerHomeInfoBinding;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyTaskActivity;
import com.slash.youth.ui.adapter.HomeInfoListAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeInfoModel extends BaseObservable {
    PagerHomeInfoBinding mPagerHomeInfoBinding;
    Activity mActivity;

    int conversationListOffset = 0;
    int conversationListLimit = 20;

    public PagerHomeInfoModel(PagerHomeInfoBinding pagerHomeInfoBinding, Activity activity) {
        this.mPagerHomeInfoBinding = pagerHomeInfoBinding;
        this.mActivity = activity;
        initView();
    }

    ArrayList<ConversationListBean.ConversationInfo> listConversation = new ArrayList<ConversationListBean.ConversationInfo>();

    private void initView() {
        getDataFromServer();
    }

    public void getDataFromServer() {
        MsgManager.getConversationList(new BaseProtocol.IResultExecutor<ConversationListBean>() {
            @Override
            public void execute(ConversationListBean dataBean) {
                listConversation = dataBean.data.list;
                setConversationList();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, conversationListOffset + "", conversationListLimit + "");
    }

    private void setConversationList() {
        mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(new HomeInfoListAdapter(listConversation));
    }

    //跳转到我的任务界面
    public void gotoMyTask(View v) {
        Intent intentMyTaskActivity = new Intent(CommonUtils.getContext(), MyTaskActivity.class);
        mActivity.startActivity(intentMyTaskActivity);
    }
}
