package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMessageBinding;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.adapter.HomeInfoListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by zhouyifeng on 2017/2/28.
 */
public class MessageModel extends BaseObservable {

    ActivityMessageBinding mActivityMessageBinding;
    Activity mActivity;

    int conversationListOffset = 0;
    int conversationListLimit = 20;

    public MessageModel(ActivityMessageBinding activityMessageBinding, Activity activity) {
        this.mActivityMessageBinding = activityMessageBinding;
        this.mActivity = activity;

        initView();
        initListener();
    }

    public void close(View v) {
        mActivity.finish();
    }

    static ArrayList<ConversationListBean.ConversationInfo> listConversation = new ArrayList<ConversationListBean.ConversationInfo>();
    ArrayList<ConversationListBean.ConversationInfo> listNewConversation = new ArrayList<ConversationListBean.ConversationInfo>();


    private void initView() {
        getDataFromServer();
    }

    private void initListener() {
        mActivityMessageBinding.lvConversationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= listConversation.size() - 1) {
                    ConversationListBean.ConversationInfo conversationInfo = listConversation.get(position);
                    long uid = conversationInfo.uid;
                    if (uid == 1000) {
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_CLICK_SLASH_SIGNPICS);
                    } else {
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_CLICK_OTHER_CHAT);
                    }
                    Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
                    intentChatActivity.putExtra("targetId", uid + "");
                    mActivity.startActivity(intentChatActivity);

                    //清楚小圆点
                    View viewUnReadPoint = view.findViewById(R.id.tv_info_unread_msg_count);
                    viewUnReadPoint.setVisibility(View.INVISIBLE);
                }
            }
        });

        mActivityMessageBinding.lvConversationList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;
            }
        });
    }

    public void getDataFromServer() {
        //这里应该不可能为null
        if (listConversation == null) {
            listConversation = new ArrayList<ConversationListBean.ConversationInfo>();
        } else {
            if (listConversation.size() > 0) {
                if (homeInfoListAdapter == null) {
                    homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                    mActivityMessageBinding.lvConversationList.setAdapter(homeInfoListAdapter);
                } else {
                    homeInfoListAdapter.notifyDataSetChanged();
                }
            }
        }
        if (listNewConversation == null) {
            listNewConversation = new ArrayList<ConversationListBean.ConversationInfo>();
        }

        listNewConversation.clear();
//        homeInfoListAdapter = null;//因为这里listConversation的引用变了，如果仍然调用 homeInfoListAdapter.notifyDataSetChanged()，刷新的是原来的引用，里面的数据都被clear了
        MsgManager.conversationUidList.clear();
        getConversationList();
    }

    int loadConversationListTimes = 0;

    /**
     * 这个方法，只能在getDataFromServer中调用，或者就是自己内部回调，不能在其它地方调用，否则会出错
     */
    private void getConversationList() {
        MsgManager.getConversationList(new BaseProtocol.IResultExecutor<ConversationListBean>() {
            @Override
            public void execute(ConversationListBean dataBean) {
                loadConversationListTimes++;
                if (dataBean.data.list != null) {
                    listNewConversation.addAll(dataBean.data.list);
                    if (dataBean.data.list.size() < 20 || loadConversationListTimes >= 3) {//加载完毕
                        for (int i = 0; i < listNewConversation.size(); i++) {
                            ConversationListBean.ConversationInfo conversationInfo = listNewConversation.get(i);
                            MsgManager.conversationUidList.add(conversationInfo.uid + "");
                        }
                        setConversationList();
                    } else {//加载未完成，继续加载
                        conversationListOffset += conversationListLimit;
                        getConversationList();
                    }
                } else {//加载完毕
                    for (int i = 0; i < listNewConversation.size(); i++) {
                        ConversationListBean.ConversationInfo conversationInfo = listNewConversation.get(i);
                        MsgManager.conversationUidList.add(conversationInfo.uid + "");
                    }
                    setConversationList();
                }
            }

            @Override
            public void executeResultError(String result) {

            }
        }, conversationListOffset + "", conversationListLimit + "");
    }

    HomeInfoListAdapter homeInfoListAdapter;

    //这两个参数用来避免for循环遍历的时候重复执行
    int totalCount;
    int currentCount;

    public void setConversationList() {
        //这里当做listNewConversation中的数据是全部的会话列表数据，MsgManager.conversationUidList中就是全部会话的uid，并且是按时间顺序排列的
        if ((!MsgManager.conversationUidList.contains("1000")) || (!MsgManager.conversationUidList.contains(MsgManager.customerServiceUid))) {
            ArrayList<String> listAddConversationUid = new ArrayList<String>();
            if (!MsgManager.conversationUidList.contains("1000")) {
                listAddConversationUid.add("1000");
            }
            if (!MsgManager.conversationUidList.contains(MsgManager.customerServiceUid)) {
                listAddConversationUid.add(MsgManager.customerServiceUid);
            }
            totalCount = listAddConversationUid.size();
            currentCount = 0;
            for (final String targetId : listAddConversationUid) {
                RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, 1, new RongIMClient.ResultCallback<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> messages) {
                        //打印主线程id和当前线程id
                        LogKit.v("Main Thread id:" + CommonUtils.getMainThreadId());
                        LogKit.v("Current Thread id:" + android.os.Process.myTid());

                        ConversationListBean conversationListBean = new ConversationListBean();
                        ConversationListBean.ConversationInfo conversationInfo = conversationListBean.new ConversationInfo();
                        conversationInfo.uid = Long.parseLong(targetId);
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
                            //这里认为listNewConversation中的数据都是按照时间顺序排列的
                            int index = listNewConversation.size() - 1;
                            for (int i = 0; i < listNewConversation.size(); i++) {
                                ConversationListBean.ConversationInfo conversation = listNewConversation.get(i);
                                if (conversation.uts < conversationInfo.uts) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index < 0) {
                                index = 0;
                            }
                            listNewConversation.add(index, conversationInfo);
                            MsgManager.conversationUidList.add(index, targetId);
                        } else {
                            //本地没有消息
//                            conversationInfo.uts = System.currentTimeMillis();
                            conversationInfo.uts = 0;
                            listNewConversation.add(conversationInfo);
                            MsgManager.conversationUidList.add(targetId);
                        }
                        currentCount++;
                        if (currentCount >= totalCount) {//避免for循环遍历的时候重复执行
                            listConversation.clear();
                            listConversation.addAll(listNewConversation);
                            if (homeInfoListAdapter == null) {
                                homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                                mActivityMessageBinding.lvConversationList.setAdapter(homeInfoListAdapter);
                                LogKit.v("----HomeInfoListAdapter new HomeInfoListAdapter");
                            } else {
                                homeInfoListAdapter.notifyDataSetChanged();
                                LogKit.v("----HomeInfoListAdapter notifyDataSetChanged 1111");
                            }
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        currentCount++;
                    }
                });
            }
        } else {
            listConversation.clear();
            listConversation.addAll(listNewConversation);
            if (homeInfoListAdapter == null) {
                homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                mActivityMessageBinding.lvConversationList.setAdapter(homeInfoListAdapter);
                LogKit.v("----HomeInfoListAdapter new HomeInfoListAdapter 2222");
            } else {
                homeInfoListAdapter.notifyDataSetChanged();
                LogKit.v("----HomeInfoListAdapter notifyDataSetChanged 2222");
            }
        }
    }

}
