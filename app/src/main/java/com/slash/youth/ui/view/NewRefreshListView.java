package com.slash.youth.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.icu.util.Measure;
import android.support.v4.view.MotionEventCompat;
import android.test.TouchUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;

public class NewRefreshListView extends ListView implements OnScrollListener {

    public static final int STATE_PULL_REFRESH = 1001;//下拉
    public static final int STATE_RELEASE_REFRESH = 1002;//刷新恢复中
    public static final int STATE_REFRESHING = 1003;//刷新中


    private Context context;

    public NewRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        this.context = context;
        initRefreshHeader();
    }

    public NewRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        initRefreshHeader();
    }

    public NewRefreshListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        initRefreshHeader();
        //初始化为尾布局
    }



    OnItemClickListener mOnItemClickListener;
    private int headerHeight;
    private View refreshHeader;
    private ImageView ivRefreshPic;
    private TextView tvRefreshState;
    private TextView tvRefreshTime;

    private void initRefreshHeader() {
        refreshHeader = View.inflate(context, R.layout.header_listview_refresh,
                null);//头布局
        addMoreFooter =  View.inflate(getContext(), R.layout.footer_listview_addmore, null);
        this.addHeaderView(refreshHeader);
       // this.addFooterView(addMoreFooter);//添加布局到底部
        ivRefreshPic = (ImageView) refreshHeader
                .findViewById(R.id.iv_header_listview_refresh_pic);
        tvRefreshState = (TextView) refreshHeader
                .findViewById(R.id.tv_header_listview_refresh_state);
        tvRefreshTime = (TextView) refreshHeader
                .findViewById(R.id.tv_header_listview_refresh_last_refreshtime);
        pbRefreshProgress = (ProgressBar) refreshHeader
                .findViewById(R.id.pb_header_listview_refresh_progress);
        refreshHeader.measure(0, 0);
        headerHeight = refreshHeader.getMeasuredHeight();
        // System.out.println(measuredHeight);
       // int measuredWidth = refreshHeader.getMeasuredWidth();
        // System.out.println(measuredWidth);
         refreshHeader.setPadding(0, -headerHeight, 0, 0);

       /* addMoreFooter.measure(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);*/

       /* addMoreFooter.measure(0,0);
        footerHeight = addMoreFooter.getMeasuredHeight();
        addMoreFooter.setPadding(0, -footerHeight, 0, 0);*/

        initData();
        initAnimation();
        initListener();
    }


    private void initListener() {
        // TODO Auto-generated method stub
        this.setOnScrollListener(this);
    }

    private void initAnimation() {
        raPull2Release = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        raPull2Release.setDuration(500);
        raPull2Release.setFillAfter(true);

        raRelease2Pull = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        raRelease2Pull.setDuration(500);
        raRelease2Pull.setFillAfter(true);
    }

    private void initData() {
        currentRefreshState = STATE_PULL_REFRESH;
        // String lastRefreshTime = getCurrentTime();
        // tvRefreshTime.setText("最后刷新时间:" + lastRefreshTime);
    }

    private String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        String lastRefreshTime = simpleDateFormat.format(currentTimeMillis);
        return lastRefreshTime;
    }


    int startY = -1;
    private int currentRefreshState;
    private int currentPullUpRefreshState;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        // System.out.println("onTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getFirstVisiblePosition() == 0) {//当前显示是第一条
                    if (currentRefreshState == STATE_REFRESHING) {
                        break;
                    }
                    if (startY == -1) {
                        startY = (int) ev.getRawY();
                    }
                    int endY = (int) ev.getRawY();
                    int dy = endY - startY;
                    if (dy > 0) {
                        int headerPadding = dy - headerHeight;
                        if (headerPadding > 0) {
                            headerPadding = 0;
                        }
                        refreshHeader.setPadding(0, headerPadding, 0, 0);
                        if (headerPadding >= 0) {
                            if (currentRefreshState != STATE_RELEASE_REFRESH) {
                                currentRefreshState = STATE_RELEASE_REFRESH;
                                setRefreshState(currentRefreshState);
                            }
                        } else {
                            if (currentRefreshState != STATE_PULL_REFRESH) {
                                currentRefreshState = STATE_PULL_REFRESH;
                                setRefreshState(currentRefreshState);
                            }
                        }
                        super.onTouchEvent(ev);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentRefreshState == STATE_PULL_REFRESH) {
                    refreshHeader.setPadding(0, -headerHeight, 0, 0);
                } else if (currentRefreshState == STATE_RELEASE_REFRESH) {

                    if (refreshDataTask != null) {
                        currentRefreshState = STATE_REFRESHING;
                        setRefreshState(currentRefreshState);
                        refreshDataTask.refresh();
                    } else {
                        currentRefreshState = STATE_PULL_REFRESH;
                        setRefreshState(currentRefreshState);
                        refreshHeader.setPadding(0, -headerHeight, 0, 0);
                    }
                }
                startY = -1;
                break;
        }
        return super.onTouchEvent(ev);
    }

    //设置下拉的状态
    private void setRefreshState(int refreshState) {
        String lastRefreshTime = getCurrentTime();
        switch (refreshState) {
            case STATE_PULL_REFRESH:
                // System.out.println("下拉刷新");
                ivRefreshPic.setVisibility(View.VISIBLE);
                pbRefreshProgress.setVisibility(View.INVISIBLE);
                tvRefreshState.setText("下拉刷新");
                ivRefreshPic.startAnimation(raRelease2Pull);
                break;
            case STATE_RELEASE_REFRESH:
                ivRefreshPic.setVisibility(View.VISIBLE);
                pbRefreshProgress.setVisibility(View.INVISIBLE);
                tvRefreshState.setText("松开刷新");
                ivRefreshPic.startAnimation(raPull2Release);
                break;
            case STATE_REFRESHING:
                ivRefreshPic.clearAnimation();
                ivRefreshPic.setVisibility(View.INVISIBLE);
                pbRefreshProgress.setVisibility(View.VISIBLE);
                tvRefreshState.setText("正在刷新。。。");
                break;
        }
    }


    @Override
    public void setOnItemClickListener(
            OnItemClickListener listener) {
        // TODO Auto-generated method stub
        this.mOnItemClickListener = listener;
        super.setOnItemClickListener(new RefreshListViewOnItemClickListener());
    }

    public class RefreshListViewOnItemClickListener implements
            OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            if (position - getHeaderViewsCount() >= 0) {
                mOnItemClickListener.onItemClick(parent, view, position
                        - getHeaderViewsCount(), id);
            }
        }
    }

    private NewIRefreshDataTask refreshDataTask;
    private RotateAnimation raPull2Release;
    private RotateAnimation raRelease2Pull;
    private ProgressBar pbRefreshProgress;
    private View addMoreFooter;
    private int footerHeight;


    /**
     * 下拉刷新执行的回调，执行结束后需要调用refreshDataFinish()方法，用来更新状态
     */
    public interface NewIRefreshDataTask {
        public void refresh();
    }

    public void setNewRefreshDataTask(NewIRefreshDataTask refreshDataTask) {
        this.refreshDataTask = refreshDataTask;
    }

    public void refreshDataFinish() {
        if (currentRefreshState != STATE_PULL_REFRESH) {
            currentRefreshState = STATE_PULL_REFRESH;
            setRefreshState(currentRefreshState);
            refreshHeader.setPadding(0, -headerHeight, 0, 0);
        }
        String lastRefreshTime = getCurrentTime();
        tvRefreshTime.setText("最后刷新时间:" + lastRefreshTime);
    }

    /**
     * SCROLL_STATE_IDLE:闲置状态，就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
     * SCROLL_STATE_FLING：快速滑动后松开
     */

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState==OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition()==(getCount()-1) &&!isLoadingMore){
            isLoadingMore = true;
            addMoreFooter.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来，在页面完全显示出底布局

            if (loadMoreNewsTask != null&&isLoadToLast == false) {
                loadMoreNewsTask.loadMore();
            } else {
               // ToastUtils.shortToast("加载更多失败~~~~(>_<);~~~~");
                //addMoreFooter.setPadding(0, -footerHeight, 0, 0);
                isLoadingMore = false;
            }
        }
    }

    private boolean isLoadingMore = false;
    private NewILoadMoreNewsTask loadMoreNewsTask = null;
    private boolean isLoadToLast = false;

    /**
     * 上拉加载更多执行的回调，执行完毕后需要调用loadMoreNewsFinished()方法，用来更新状态,如果加载到最后一页，则需要调用setLoadToLast()方法
     */
    public interface NewILoadMoreNewsTask {
        public void loadMore();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        // TODO Auto-generated method stub
        // System.out.println("totalItemCount:" + totalItemCount
        // + "  getLastVisiblePosition:" + getLastVisiblePosition());
      /*  if (isLoadToLast == false && isLoadingMore == false
                && getLastVisiblePosition() == totalItemCount - 1
                && getLastVisiblePosition() >= 0&&getFirstVisiblePosition()!=0) {
            // ToastUtils.shortToast(context, "the last");
            System.out.println("load more");
            isLoadingMore = true;
            addMoreFooter.setPadding(0, 0, 0, 0);
            if (loadMoreNewsTask != null) {
                loadMoreNewsTask.loadMore();
            } else {
                ToastUtils.shortToast("加载更多失败~~~~(>_<);~~~~");
                addMoreFooter.setPadding(0, -footerHeight, 0, 0);
                isLoadingMore = false;
            }
        }*/
    }

    public void setNewLoadMoreNewsTast(NewILoadMoreNewsTask loadMoreNewsTask) {
        this.loadMoreNewsTask = loadMoreNewsTask;
    }

    public void loadMoreNewsFinished() {
        addMoreFooter.setPadding(0, -footerHeight, 0, 0);
        isLoadingMore = false;
    }

    public void setLoadToLast() {
        isLoadToLast = true;
    }

}
