package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeWorkbenchBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskList;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandChooseServiceActivity;
import com.slash.youth.ui.activity.HomeActivity2;
import com.slash.youth.ui.activity.MyBidDemandActivity;
import com.slash.youth.ui.activity.MyBidServiceActivity;
import com.slash.youth.ui.activity.MyPublishDemandActivity;
import com.slash.youth.ui.activity.MyPublishServiceActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.adapter.MyTaskAdapter;
import com.slash.youth.ui.view.RefreshListView;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class PagerHomeWorkbenchModel extends BaseObservable {

    private static final int LOAD_DATA_TYPE_LOAD = 0;//首次加载数据
    private static final int LOAD_DATA_TYPE_REFRESH = 1;//刷新数据
    private static final int LOAD_DATA_TYPE_MORE = 2;//加载更多数据

    PagerHomeWorkbenchBinding mPagerHomeWorkbenchBinding;
    Activity mActivity;

    private int offset = 0;
    private int pageSize = 20;


    private int currentFilterTaskType = MyTaskEngine.USER_TASK_ALL_TYPE;//当前过滤展示的任务类型，默认为全部，type=0
    private int currentLoadDataType = LOAD_DATA_TYPE_LOAD;

    public PagerHomeWorkbenchModel(PagerHomeWorkbenchBinding pagerHomeWorkbenchBinding, Activity activity) {
        this.mPagerHomeWorkbenchBinding = pagerHomeWorkbenchBinding;
        this.mActivity = activity;
        initData();
        initListener();
        initView();
    }

    ArrayList<MyTaskBean> listMyTask = null;

    private void initData() {
        //去掉100号的我未读消息数量
        //这里目前不能使用这个方法，因为是用叠加的方式计数的
//        clearOtherMessagesUnreadCount();
        MsgManager.taskMessageCount = 0;
        SpUtils.setInt(GlobalConstants.SpConfigKey.TASK_MESSAGE_COUNT, MsgManager.taskMessageCount);
        //初始化每一个任务对应的消息数量的HashMap
        if (MsgManager.everyTaskMessageCount == null) {
            MsgManager.everyTaskMessageCount = MsgManager.deserializeEveryTaskMessageCount();
            if (MsgManager.everyTaskMessageCount == null) {
                MsgManager.everyTaskMessageCount = new HashMap<Long, Integer>();
            }
        }

        //首次进入页面，加载我的全部任务（进行中任务，发的和抢的，不包括任务）
        currentFilterTaskType = MyTaskEngine.USER_TASK_ALL_TYPE;
        currentLoadDataType = LOAD_DATA_TYPE_LOAD;
        offset = 0;
        getMyTotalTaskList(MyTaskEngine.USER_TASK_ALL_TYPE, offset, pageSize);
    }

    MyTaskAdapter myTaskAdapter;

    private void setTotalTaskData() {
        if (listMyTask != null && listMyTask.size() > 0) {
            setMyTaskTypeText("进行中任务");
            setMyTaskListVisibility(View.VISIBLE);
            setNoTaskVisibility(View.GONE);
            myTaskAdapter = new MyTaskAdapter(listMyTask);
            mPagerHomeWorkbenchBinding.lvMyTaskList.setAdapter(myTaskAdapter);
        } else {
            setMyTaskListVisibility(View.GONE);
            setNoTaskVisibility(View.VISIBLE);
        }
    }

    private void setMyPublishTaskData() {
        setMyTaskTypeText("发布的任务");
        if (listMyTask != null && listMyTask.size() > 0) {
            myTaskAdapter = new MyTaskAdapter(listMyTask);
            mPagerHomeWorkbenchBinding.lvMyTaskList.setAdapter(myTaskAdapter);
            setMyTaskListVisibility(View.VISIBLE);
            setNoTaskVisibility(View.GONE);
        }
    }

    private void setMyBidTaskData() {
        setMyTaskTypeText("抢到的任务");
        if (listMyTask != null && listMyTask.size() > 0) {
            myTaskAdapter = new MyTaskAdapter(listMyTask);
            mPagerHomeWorkbenchBinding.lvMyTaskList.setAdapter(myTaskAdapter);
            setMyTaskListVisibility(View.VISIBLE);
            setNoTaskVisibility(View.GONE);
        }
    }

    private void setMyHistoryTaskData() {
        setMyTaskTypeText("历史任务");
        if (listMyTask != null && listMyTask.size() > 0) {
            myTaskAdapter = new MyTaskAdapter(listMyTask);
            mPagerHomeWorkbenchBinding.lvMyTaskList.setAdapter(myTaskAdapter);
            setMyTaskListVisibility(View.VISIBLE);
            setNoTaskVisibility(View.GONE);
        }
    }

    private boolean isMoveListView = false;

    private void initListener() {

        mPagerHomeWorkbenchBinding.lvMyTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMoveListView) {
                    isMoveListView = false;
                    return;
                }
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MESSAGE_MY_MISSON_CLICK_MISSON);

                MyTaskBean myTaskBean = listMyTask.get(position);

                Bundle taskInfo = new Bundle();
                taskInfo.putLong("tid", myTaskBean.tid);
                taskInfo.putInt("type", myTaskBean.type);
                taskInfo.putInt("roleid", myTaskBean.roleid);

//                ToastUtils.shortToast(myTaskBean.status + "");

                if (myTaskBean.roleid == 1) {//发布者
                    if (myTaskBean.type == 1) {//我发的需求
                        switch (myTaskBean.status) {
                            case 1:
                            case 4:
                            case 5:
                                Intent intentDemandChooseServiceActivity = new Intent(CommonUtils.getContext(), DemandChooseServiceActivity.class);
                                intentDemandChooseServiceActivity.putExtras(taskInfo);
                                mActivity.startActivityForResult(intentDemandChooseServiceActivity, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                                break;
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                Intent intentMyPublishDemandActivity = new Intent(CommonUtils.getContext(), MyPublishDemandActivity.class);
                                intentMyPublishDemandActivity.putExtras(taskInfo);
                                mActivity.startActivityForResult(intentMyPublishDemandActivity, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                                break;
                            default:
                                //其它情况应该跳转到需求详情页
                                //这里有疑问，是跳到需求详情页还是四个圈的页面，暂时先写成四个圈的页面
                                Intent intentMyPublishDemandActivity2 = new Intent(CommonUtils.getContext(), MyPublishDemandActivity.class);
                                intentMyPublishDemandActivity2.putExtras(taskInfo);
                                mActivity.startActivityForResult(intentMyPublishDemandActivity2, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                                break;
                        }
                    } else if (myTaskBean.type == 2) {//我发的服务
                        Intent intentMyPublishServiceActivity = new Intent(CommonUtils.getContext(), MyPublishServiceActivity.class);
                        intentMyPublishServiceActivity.putExtra("myTaskBean", listMyTask.get(position));
                        mActivity.startActivityForResult(intentMyPublishServiceActivity, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                    }

                } else if (myTaskBean.roleid == 2) {//抢单者
                    if (myTaskBean.type == 1) {//我抢的需求
                        switch (myTaskBean.status) {
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                                Intent intentMyBidDemandActivity = new Intent(CommonUtils.getContext(), MyBidDemandActivity.class);
                                intentMyBidDemandActivity.putExtras(taskInfo);
                                mActivity.startActivityForResult(intentMyBidDemandActivity, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                                break;
                            default:
                                //其它情况应该跳转到需求详情页
                                //这里有疑问，是跳到需求详情页还是四个圈的页面，暂时先写成四个圈的页面
                                Intent intentMyBidDemandActivity2 = new Intent(CommonUtils.getContext(), MyBidDemandActivity.class);
                                intentMyBidDemandActivity2.putExtras(taskInfo);
                                mActivity.startActivityForResult(intentMyBidDemandActivity2, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                                break;
                        }
                    } else if (myTaskBean.type == 2) {//我抢的服务(我预约的服务)
                        Intent intentMyBidServiceActivity = new Intent(CommonUtils.getContext(), MyBidServiceActivity.class);
                        intentMyBidServiceActivity.putExtra("myTaskBean", listMyTask.get(position));
                        mActivity.startActivityForResult(intentMyBidServiceActivity, HomeActivity2.REQUEST_CODE_TO_TASK_DETAIL);
                    }
                }
                //清空任务item对应的消息数量
                if (MsgManager.everyTaskMessageCount != null) {//照理说在这里不可能为null
                    MsgManager.everyTaskMessageCount.put(myTaskBean.id, 0);
                    MsgManager.serializeEveryTaskMessageCount(MsgManager.everyTaskMessageCount);
                }
                //隐藏任务item上的小圆点
                View taskMessagePoint = view.findViewById(R.id.view_task_message_point);
                taskMessagePoint.setVisibility(View.GONE);
            }
        });

        mPagerHomeWorkbenchBinding.lvMyTaskList.setOnTouchListener(new View.OnTouchListener() {
            int startY = -1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        if (startY != -1) {
                            int endY = (int) event.getRawY();
                            if (Math.abs(endY - startY) > 10) {
                                isMoveListView = true;
                                CommonUtils.getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isMoveListView = false;
                                    }
                                }, 100);
                            }
                            startY = -1;
                        }
                        break;
                }
                return false;
            }
        });

        mPagerHomeWorkbenchBinding.lvMyTaskList.setRefreshDataTask(new RefreshDataTask());
        mPagerHomeWorkbenchBinding.lvMyTaskList.setLoadMoreNewsTast(new LoadMoreNewsTask());
    }

    private void initView() {

    }


    /**
     * 获取我全部任务（进行中任务，发的和抢的，不包括任务）
     */
    public void getMyTotalTaskList(int type, int offset, final int limit) {
        //模拟数据，实际由服务端 返回
//        listMyTask.clear();
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());

        mPagerHomeWorkbenchBinding.tvSelectedTaskText.setText("进行中");
        MyTaskEngine.getMyTaskList(new BaseProtocol.IResultExecutor<MyTaskList>() {
            @Override
            public void execute(MyTaskList dataBean) {
                LogKit.v("currentLoadDataType:" + currentLoadDataType);

                ArrayList<MyTaskBean> loadData = dataBean.data.list;
                if (currentLoadDataType == LOAD_DATA_TYPE_LOAD) {
                    listMyTask = loadData;
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    setTotalTaskData();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else if (currentLoadDataType == LOAD_DATA_TYPE_REFRESH) {
                    listMyTask.clear();
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setTotalTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.refreshDataFinish();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else {
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    LogKit.v("----------load more listMyTask.size():" + listMyTask.size());
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setTotalTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.loadMoreNewsFinished();
                    if (loadData.size() < limit) {
                        mPagerHomeWorkbenchBinding.lvMyTaskList.setLoadToLast();
                        ToastUtils.shortToast("已经是最后一条了");
                    }
                }

//                ToastUtils.shortToast(listMyTask.size() + "");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("get my total task error\r\n" + result);
            }
        }, type, offset, limit);
    }

    /**
     * 获取我发布的任务
     */
    public void getMyPublishTaskList(int type, int offset, final int limit) {
        //模拟数据，实际由服务端 返回
//        listMyTask.clear();
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());

        mPagerHomeWorkbenchBinding.tvSelectedTaskText.setText("我发的");
        MyTaskEngine.getMyTaskList(new BaseProtocol.IResultExecutor<MyTaskList>() {
            @Override
            public void execute(MyTaskList dataBean) {
                LogKit.v("currentLoadDataType:" + currentLoadDataType);

                ArrayList<MyTaskBean> loadData = dataBean.data.list;
                if (currentLoadDataType == LOAD_DATA_TYPE_LOAD) {
                    listMyTask = loadData;
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    setMyPublishTaskData();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else if (currentLoadDataType == LOAD_DATA_TYPE_REFRESH) {
                    listMyTask.clear();
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyPublishTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.refreshDataFinish();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else {
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    LogKit.v("----------load more listMyTask.size():" + listMyTask.size());
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyPublishTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.loadMoreNewsFinished();
                    if (loadData.size() < limit) {
                        mPagerHomeWorkbenchBinding.lvMyTaskList.setLoadToLast();
                    }
                }

//                ToastUtils.shortToast(listMyTask.size() + "");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("get my total task error\r\n" + result);
            }
        }, type, offset, limit);
    }

    /**
     * 获取我抢的任务
     */
    public void getMyBidTaskList(int type, final int offset, final int limit) {
        //模拟数据，实际由服务端 返回
//        listMyTask.clear();
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());

        mPagerHomeWorkbenchBinding.tvSelectedTaskText.setText("我抢的");
        MyTaskEngine.getMyTaskList(new BaseProtocol.IResultExecutor<MyTaskList>() {
            @Override
            public void execute(MyTaskList dataBean) {
                LogKit.v("currentLoadDataType:" + currentLoadDataType);

                ArrayList<MyTaskBean> loadData = dataBean.data.list;
                if (currentLoadDataType == LOAD_DATA_TYPE_LOAD) {
                    listMyTask = loadData;
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    setMyBidTaskData();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else if (currentLoadDataType == LOAD_DATA_TYPE_REFRESH) {
                    listMyTask.clear();
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyBidTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.refreshDataFinish();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else {
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    LogKit.v("----------load more listMyTask.size():" + listMyTask.size());
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyBidTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.loadMoreNewsFinished();
                    if (loadData.size() < limit) {
                        mPagerHomeWorkbenchBinding.lvMyTaskList.setLoadToLast();
                    }
                }

//                ToastUtils.shortToast(listMyTask.size() + "");

            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("get my total task error\r\n" + result);
            }
        }, type, offset, limit);
    }

    /**
     * 获取我的任务
     */
    public void getMyHistoryTaskList(int type, int offset, final int limit) {
        //模拟数据，实际由服务端 返回
//        listMyTask.clear();
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());
//        listMyTask.add(new MyTaskBean());

        mPagerHomeWorkbenchBinding.tvSelectedTaskText.setText("历史");
        MyTaskEngine.getMyTaskList(new BaseProtocol.IResultExecutor<MyTaskList>() {
            @Override
            public void execute(MyTaskList dataBean) {
                LogKit.v("currentLoadDataType:" + currentLoadDataType);

                ArrayList<MyTaskBean> loadData = dataBean.data.list;
                if (currentLoadDataType == LOAD_DATA_TYPE_LOAD) {
                    listMyTask = loadData;
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    setMyHistoryTaskData();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else if (currentLoadDataType == LOAD_DATA_TYPE_REFRESH) {
                    listMyTask.clear();
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyHistoryTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.refreshDataFinish();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                } else {
                    listMyTask.addAll(loadData);
                    PagerHomeWorkbenchModel.this.offset = listMyTask.size();
                    LogKit.v("----------load more listMyTask.size():" + listMyTask.size());
                    if (myTaskAdapter != null) {
                        myTaskAdapter.notifyDataSetChanged();
                    } else {
                        setMyHistoryTaskData();
                    }
                    mPagerHomeWorkbenchBinding.lvMyTaskList.loadMoreNewsFinished();
                    if (loadData.size() < limit) {
                        mPagerHomeWorkbenchBinding.lvMyTaskList.setLoadToLast();
                    }
                }

//                ToastUtils.shortToast(listMyTask.size() + "");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("get my total task error\r\n" + result);
            }
        }, type, offset, limit);
    }

    /**
     * 下拉刷新执行的回调，执行结束后需要调用refreshDataFinish()方法，用来更新状态
     */
    public class RefreshDataTask implements RefreshListView.IRefreshDataTask {

        @Override
        public void refresh() {
            offset = 0;
            currentLoadDataType = LOAD_DATA_TYPE_REFRESH;
            if (currentFilterTaskType == MyTaskEngine.USER_TASK_ALL_TYPE) {
                getMyTotalTaskList(MyTaskEngine.USER_TASK_ALL_TYPE, offset, pageSize);
            } else if (currentFilterTaskType == MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE) {
                getMyPublishTaskList(MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE, offset, pageSize);
            } else if (currentFilterTaskType == MyTaskEngine.USER_TASK_MY_BID_TYPE) {
                getMyBidTaskList(MyTaskEngine.USER_TASK_MY_BID_TYPE, offset, pageSize);
            } else {
                getMyHistoryTaskList(MyTaskEngine.USER_TASK_MY_HIS_TYPE, offset, pageSize);
            }
            //避免数据加载错误的情况
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPagerHomeWorkbenchBinding.lvMyTaskList.refreshDataFinish();
                    mPagerHomeWorkbenchBinding.lvMyTaskList.setNotLoadToLast();
                }
            }, 15000);
        }
    }

    /**
     * 上拉加载更多执行的回调，执行完毕后需要调用loadMoreNewsFinished()方法，用来更新状态,如果加载到最后一页，则需要调用setLoadToLast()方法
     */
    public class LoadMoreNewsTask implements RefreshListView.ILoadMoreNewsTask {

        @Override
        public void loadMore() {
            currentLoadDataType = LOAD_DATA_TYPE_MORE;
            if (currentFilterTaskType == MyTaskEngine.USER_TASK_ALL_TYPE) {
                getMyTotalTaskList(MyTaskEngine.USER_TASK_ALL_TYPE, offset, pageSize);
            } else if (currentFilterTaskType == MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE) {
                getMyPublishTaskList(MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE, offset, pageSize);
            } else if (currentFilterTaskType == MyTaskEngine.USER_TASK_MY_BID_TYPE) {
                getMyBidTaskList(MyTaskEngine.USER_TASK_MY_BID_TYPE, offset, pageSize);
            } else {
                getMyHistoryTaskList(MyTaskEngine.USER_TASK_MY_HIS_TYPE, offset, pageSize);
            }
            //避免数据加载错误的情况
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPagerHomeWorkbenchBinding.lvMyTaskList.loadMoreNewsFinished();
                }
            }, 15000);
        }
    }

    //打开或关闭筛选任务的下拉框
    public void openFilterTask(View v) {
        if (openTaskVisibility == View.GONE) {
            setOpenTaskVisibility(View.VISIBLE);
        } else {
            setOpenTaskVisibility(View.GONE);
        }
    }

    //去发布任务
    public void gotoPublishTask(View v) {
//        ToastUtils.shortToast("去发布任务");
        setPublishTaskDialogVisibility(View.VISIBLE);
    }

    //去浏览任务
    public void gotoBrowseTask(View v) {
        HomeActivity2 homeActivity2 = (HomeActivity2) mActivity;
        ViewPager innerViewPager = homeActivity2.getInnerViewPager();
        innerViewPager.setCurrentItem(0);
    }

    //关闭发布任务对话框
    public void closePublishTaskDialog(View v) {
        setPublishTaskDialogVisibility(View.GONE);
    }

    //发布需求
    public void publishDemand(View v) {
        Intent intentPublishDemandBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
        mActivity.startActivity(intentPublishDemandBaseInfoActivity);
    }

    //发布服务
    public void publishService(View v) {
        Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
        mActivity.startActivity(intentPublishServiceBaseInfoActivity);
    }

    //筛选全部任务（进行中任务，发的和抢的，不包括任务）
    public void filterMyTotalTask(View v) {
        offset = 0;
        currentFilterTaskType = MyTaskEngine.USER_TASK_ALL_TYPE;
        currentLoadDataType = LOAD_DATA_TYPE_LOAD;
        getMyTotalTaskList(MyTaskEngine.USER_TASK_ALL_TYPE, offset, pageSize);
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我发布的任务
    public void filterMyPublishTask(View v) {
        offset = 0;
        currentFilterTaskType = MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE;
        currentLoadDataType = LOAD_DATA_TYPE_LOAD;
        getMyPublishTaskList(MyTaskEngine.USER_TASK_MY_PUBLISH_TYPE, offset, pageSize);
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我抢的任务
    public void filterMyBidTask(View v) {
        offset = 0;
        currentFilterTaskType = MyTaskEngine.USER_TASK_MY_BID_TYPE;
        currentLoadDataType = LOAD_DATA_TYPE_LOAD;
        getMyBidTaskList(MyTaskEngine.USER_TASK_MY_BID_TYPE, offset, pageSize);
        setOpenTaskVisibility(View.GONE);
    }

    //筛选我的任务（下架的或者过期的）
    public void filterMyHistoryTask(View v) {
        offset = 0;
        currentFilterTaskType = MyTaskEngine.USER_TASK_MY_HIS_TYPE;
        currentLoadDataType = LOAD_DATA_TYPE_LOAD;
        getMyHistoryTaskList(MyTaskEngine.USER_TASK_MY_HIS_TYPE, offset, pageSize);
        setOpenTaskVisibility(View.GONE);
    }

    public void backRefreshTaskListStatus() {
        initData();
        initListener();
        initView();
    }

    /**
     * 隐藏筛选
     *
     * @param v
     */
    public void hideFilterTask(View v) {
        setOpenTaskVisibility(View.GONE);
    }

    private void clearOtherMessagesUnreadCount() {
        RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, "100", new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                LogKit.v("Clear result:" + aBoolean);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private int openTaskVisibility = View.GONE;
    private int myTaskListVisibility = View.GONE;
    private int noTaskVisibility = View.GONE;
    private int publishTaskDialogVisibility = View.GONE;
    private String myTaskTypeText = "进行中任务";

    @Bindable
    public int getOpenTaskVisibility() {
        return openTaskVisibility;
    }

    public void setOpenTaskVisibility(int openTaskVisibility) {
        this.openTaskVisibility = openTaskVisibility;
        notifyPropertyChanged(BR.openTaskVisibility);
    }

    @Bindable
    public int getNoTaskVisibility() {
        return noTaskVisibility;
    }

    public void setNoTaskVisibility(int noTaskVisibility) {
        this.noTaskVisibility = noTaskVisibility;
        notifyPropertyChanged(BR.noTaskVisibility);
    }

    @Bindable
    public int getMyTaskListVisibility() {
        return myTaskListVisibility;
    }

    public void setMyTaskListVisibility(int myTaskListVisibility) {
        this.myTaskListVisibility = myTaskListVisibility;
        notifyPropertyChanged(BR.myTaskListVisibility);
    }

    @Bindable
    public int getPublishTaskDialogVisibility() {
        return publishTaskDialogVisibility;
    }

    public void setPublishTaskDialogVisibility(int publishTaskDialogVisibility) {
        this.publishTaskDialogVisibility = publishTaskDialogVisibility;
        notifyPropertyChanged(BR.publishTaskDialogVisibility);
    }

    @Bindable
    public String getMyTaskTypeText() {
        return myTaskTypeText;
    }

    public void setMyTaskTypeText(String myTaskTypeText) {
        this.myTaskTypeText = myTaskTypeText;
        notifyPropertyChanged(BR.myTaskTypeText);
    }
}
