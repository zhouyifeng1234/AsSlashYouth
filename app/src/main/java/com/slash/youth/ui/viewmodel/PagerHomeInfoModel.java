package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeInfoBinding;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.MyTaskActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.HomeInfoListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

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
        //通过融云SDK获取所有的100号的未读消息数量
        //目前不能使用这个方法，因为服务端使用CmdNTF格式推送的，这个类型是不计数的，所以没法使用这个方式获取未读消息数
//        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "100", new RongIMClient.ResultCallback<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                int unreadCount = integer;
//                if (unreadCount > 0) {
//                    setTaskMessageCountPointVisibility(View.VISIBLE);
//                    setTaskMessageCount(unreadCount + "");
//                } else {
//                    setTaskMessageCountPointVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

        getDataFromServer();
    }

    private void initListener() {
        mPagerHomeInfoBinding.lvPagerHomeInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        mPagerHomeInfoBinding.lvPagerHomeInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                return false;
            }
        });
    }

    public void getDataFromServer() {
        if (MsgManager.taskMessageCount == -1) {
            MsgManager.taskMessageCount = SpUtils.getInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, 0);
        }
        if (MsgManager.taskMessageCount > 0) {
            setTaskMessageCountPointVisibility(View.VISIBLE);
            setTaskMessageCount(MsgManager.taskMessageCount + "");
        } else {
            setTaskMessageCountPointVisibility(View.GONE);
        }

        mPagerHomeInfoBinding.lvPagerHomeInfo.setEnabled(false);
        //这里应该不可能为null
        if (listConversation == null) {
            listConversation = new ArrayList<ConversationListBean.ConversationInfo>();
        }
        listConversation.clear();
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
//                listConversation = dataBean.data.list;
                if (dataBean.data.list != null) {
                    listConversation.addAll(dataBean.data.list);
                    if (dataBean.data.list.size() < 20 || loadConversationListTimes >= 3) {//加载完毕
                        for (int i = 0; i < listConversation.size(); i++) {
                            ConversationListBean.ConversationInfo conversationInfo = listConversation.get(i);
                            MsgManager.conversationUidList.add(conversationInfo.uid + "");
                        }
                        setConversationList();
                    } else {//加载未完成，继续加载
                        getConversationList();
                    }
                } else {//加载完毕
                    for (int i = 0; i < listConversation.size(); i++) {
                        ConversationListBean.ConversationInfo conversationInfo = listConversation.get(i);
                        MsgManager.conversationUidList.add(conversationInfo.uid + "");
                    }
                    setConversationList();
                }

            }

            @Override
            public void executeResultError(String result) {
                mPagerHomeInfoBinding.lvPagerHomeInfo.setEnabled(true);
            }
        }, conversationListOffset + "", conversationListLimit + "");
    }

    HomeInfoListAdapter homeInfoListAdapter;

    //这两个参数用来避免for循环遍历的时候重复执行
    int totalCount;
    int currentCount;

    public void setConversationList() {
        //这里当做listConversation中的数据是全部的会话列表数据，MsgManager.conversationUidList中就是全部会话的uid，并且是按时间顺序排列的
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
                            //这里认为listConversation中的数据都是按照时间顺序排列的
                            int index = listConversation.size() - 1;
                            for (int i = 0; i < listConversation.size(); i++) {
                                ConversationListBean.ConversationInfo conversation = listConversation.get(i);
                                if (conversation.uts < conversationInfo.uts) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index < 0) {
                                index = 0;
                            }
                            listConversation.add(index, conversationInfo);
                            MsgManager.conversationUidList.add(index, targetId);
                        } else {
                            //本地没有消息
//                            conversationInfo.uts = System.currentTimeMillis();
                            conversationInfo.uts = 0;
                            listConversation.add(conversationInfo);
                            MsgManager.conversationUidList.add(targetId);
                        }
                        currentCount++;
                        if (currentCount >= totalCount) {//避免for循环遍历的时候重复执行
                            if (homeInfoListAdapter == null) {
                                homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                                mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(homeInfoListAdapter);
                                LogKit.v("----HomeInfoListAdapter new HomeInfoListAdapter");
                            } else {
                                homeInfoListAdapter.notifyDataSetChanged();
                                LogKit.v("----HomeInfoListAdapter notifyDataSetChanged 1111");
                            }
                            mPagerHomeInfoBinding.lvPagerHomeInfo.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        currentCount++;
                    }
                });
            }
        } else {
            if (homeInfoListAdapter == null) {
                homeInfoListAdapter = new HomeInfoListAdapter(listConversation);
                mPagerHomeInfoBinding.lvPagerHomeInfo.setAdapter(homeInfoListAdapter);
                LogKit.v("----HomeInfoListAdapter new HomeInfoListAdapter 2222");
            } else {
                homeInfoListAdapter.notifyDataSetChanged();
                LogKit.v("----HomeInfoListAdapter notifyDataSetChanged 2222");
            }
            mPagerHomeInfoBinding.lvPagerHomeInfo.setEnabled(true);
        }
    }


    //跳转到我的任务界面
    public void gotoMyTask(View v) {
        //隐藏任务消息数量的小圆点
        setTaskMessageCountPointVisibility(View.GONE);

        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_CLICK_MY_MISSON);

        Intent intentMyTaskActivity = new Intent(CommonUtils.getContext(), MyTaskActivity.class);
        mActivity.startActivity(intentMyTaskActivity);
    }

    public void gotoSearchActivity(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_CLICK_SEARCH);

        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        mActivity.startActivity(intentSearchActivity);
    }

    private int taskMessageCountPointVisibility = View.GONE;
    private String taskMessageCount;

    @Bindable
    public String getTaskMessageCount() {
        return taskMessageCount;
    }

    public void setTaskMessageCount(String taskMessageCount) {
        this.taskMessageCount = taskMessageCount;
        notifyPropertyChanged(BR.taskMessageCount);
    }

    @Bindable
    public int getTaskMessageCountPointVisibility() {
        return taskMessageCountPointVisibility;
    }

    public void setTaskMessageCountPointVisibility(int taskMessageCountPointVisibility) {
        this.taskMessageCountPointVisibility = taskMessageCountPointVisibility;
        notifyPropertyChanged(BR.taskMessageCountPointVisibility);
    }
}
