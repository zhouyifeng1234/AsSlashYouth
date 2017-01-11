package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.PagerHomeInfoBinding;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.MyTaskActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.HomeInfoListAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

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
        initListener();
    }

    ArrayList<ConversationListBean.ConversationInfo> listConversation = new ArrayList<ConversationListBean.ConversationInfo>();

    private void initView() {
        getDataFromServer();
    }

    private void initListener() {
        mPagerHomeInfoBinding.lvPagerHomeInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversationListBean.ConversationInfo conversationInfo = listConversation.get(position);
                long uid = conversationInfo.uid;
                Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
                intentChatActivity.putExtra("targetId", uid + "");
                mActivity.startActivity(intentChatActivity);
            }
        });
    }

    public void getDataFromServer() {
        MsgManager.getConversationList(new BaseProtocol.IResultExecutor<ConversationListBean>() {
            @Override
            public void execute(ConversationListBean dataBean) {
                listConversation.clear();
                listConversation = dataBean.data.list;
                homeInfoListAdapter = null;//因为这里listConversation的引用变了，如果仍然调用 homeInfoListAdapter.notifyDataSetChanged()，刷新的是原来的引用，里面的数据都被clear了
                if (listConversation == null) {
                    listConversation = new ArrayList<ConversationListBean.ConversationInfo>();
                }
                MsgManager.conversationUidList.clear();
                for (int i = 0; i < listConversation.size(); i++) {
                    ConversationListBean.ConversationInfo conversationInfo = listConversation.get(i);
                    MsgManager.conversationUidList.add(conversationInfo.uid + "");
                }
                setConversationList();
            }

            @Override
            public void executeResultError(String result) {

            }
        }, conversationListOffset + "", conversationListLimit + "");
    }

    HomeInfoListAdapter homeInfoListAdapter;

    public void setConversationList() {
        //这里当做listConversation中的数据是全部的会话列表数据，MsgManager.conversationUidList中就是全部会话的uid，并且是按时间顺序排列的
        if (!MsgManager.conversationUidList.contains("1000")) {
            RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, "1000", 1, new RongIMClient.ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
                    ConversationListBean conversationListBean = new ConversationListBean();
                    ConversationListBean.ConversationInfo conversationInfo = conversationListBean.new ConversationInfo();
                    conversationInfo.uid = 1000;
                    if (messages != null && messages.size() > 0) {
                        //本地有消息
                        Message message = messages.get(0);
                        long msgtime = 0;
                        Message.MessageDirection messageDirection = message.getMessageDirection();
                        if (messageDirection == Message.MessageDirection.SEND) {
                            msgtime = message.getSentTime();
                        } else if (messageDirection == Message.MessageDirection.RECEIVE) {
                            msgtime = message.getReceivedTime();
                        }
                        conversationInfo.uts = msgtime;
                        //这里认为listConversation中的数据都是按照时间顺序排列的
                        int index = listConversation.size() - 1;
                        for (int i = 0; i < listConversation.size(); i++) {
                            ConversationListBean.ConversationInfo conversation = listConversation.get(i);
                            if (conversation.uts < conversationInfo.uts) {
                                index = i;
                                break;
                            }
                        }
                        listConversation.add(index, conversationInfo);
                        MsgManager.conversationUidList.add(index, "1000");
                    } else {
                        //本地没有消息
                        conversationInfo.uts = System.currentTimeMillis();
                        listConversation.add(conversationInfo);
                        MsgManager.conversationUidList.add("1000");
                    }
                    if (homeInfoListAdapter == null) {
                        homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                        mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(homeInfoListAdapter);
                    } else {
                        homeInfoListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

        } else {
            if (homeInfoListAdapter == null) {
                homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(homeInfoListAdapter);
            } else {
                homeInfoListAdapter.notifyDataSetChanged();
            }
        }
    }


    //跳转到我的任务界面
    public void gotoMyTask(View v) {
        Intent intentMyTaskActivity = new Intent(CommonUtils.getContext(), MyTaskActivity.class);
        mActivity.startActivity(intentMyTaskActivity);
    }

    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        mActivity.startActivity(intentSearchActivity);
    }
}
