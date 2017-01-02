package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommentBinding;
import com.slash.youth.domain.CommentResultBean;
import com.slash.youth.domain.CommentStatusBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 当服务方顺利完成需求方的任务之后，需求方对服务方评价F
 * Created by zhouyifeng on 2016/11/11.
 */
public class CommentModel extends BaseObservable {

    ActivityCommentBinding mActivityCommentBinding;
    Activity mActivity;

    int serviceQualityMarks = 0;
    int completeSpeedMarks = 0;
    int serviceAttitudeMarks = 0;

    long tid;//任务ID（需求or服务ID）
    int type;//需求服务类型 1需求 2服务
    long suid;//服务者UID

    boolean isCompleteComment = false;


    public CommentModel(ActivityCommentBinding activityCommentBinding, Activity activity) {
        this.mActivityCommentBinding = activityCommentBinding;
        this.mActivity = activity;
        displayLoadLayer();
        initData();
        initView();
    }


    private void initData() {
        Bundle commentInfo = mActivity.getIntent().getExtras();
        tid = commentInfo.getLong("tid");
        type = commentInfo.getInt("type");
        suid = commentInfo.getLong("suid");

        MyTaskEngine.getCommentStatus(new BaseProtocol.IResultExecutor<CommentStatusBean>() {
            @Override
            public void execute(CommentStatusBean dataBean) {
                CommentStatusBean.Evaluation evaluation = dataBean.data.evaluation;
                if (evaluation != null && evaluation.cts != 0) {
                    LogKit.v("已评价");
                    isCompleteComment = true;
                    setGoBackIconVisibility(View.GONE);
                    setCompleteCommentBtnVisibility(View.GONE);
                    setCloseCommentBtnVisibility(View.VISIBLE);
                    setBottomShareBtnVisibility(View.VISIBLE);
                    setCompleteCommentIconVisibility(View.VISIBLE);
                    mActivityCommentBinding.etCommentContent.setEnabled(false);
                    mActivityCommentBinding.etCommentContent.setText(evaluation.remark);
                    serviceQualityMarks = evaluation.quality;
                    completeSpeedMarks = evaluation.speed;
                    serviceAttitudeMarks = evaluation.attitude;
                    displayStars(mActivityCommentBinding.llServiceQualityStars, serviceQualityMarks - 1);
                    displayStars(mActivityCommentBinding.llCompleteSpeedStars, completeSpeedMarks - 1);
                    displayStars(mActivityCommentBinding.llServiceAttitudeStars, serviceAttitudeMarks - 1);

                } else {
                    LogKit.v("未评价");
                    isCompleteComment = false;
                    setGoBackIconVisibility(View.VISIBLE);
                    setCompleteCommentBtnVisibility(View.VISIBLE);
                    setCloseCommentBtnVisibility(View.GONE);
                    setBottomShareBtnVisibility(View.INVISIBLE);
                    setCompleteCommentIconVisibility(View.GONE);
                    mActivityCommentBinding.etCommentContent.setEnabled(true);
                }
                hideLoadLayer();
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取任务评价状态失败");
            }
        }, tid + "", type + "");
    }

    private void initView() {

    }

    /**
     * 刚进入页面时，显示加载层
     */
    private void displayLoadLayer() {
        setLoadLayerVisibility(View.VISIBLE);
    }

    /**
     * 数据加载完毕后,隐藏加载层
     */
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    public void giveMarks(View v) {
        if (!isCompleteComment) {
            ViewGroup viewGroup = (ViewGroup) v.getParent();
            int currentCheckedIndex = viewGroup.indexOfChild(v);
            displayStars(viewGroup, currentCheckedIndex);
            switch (viewGroup.getId()) {
                case R.id.ll_service_quality_stars:
                    serviceQualityMarks = currentCheckedIndex + 1;
                    break;
                case R.id.ll_complete_speed_stars:
                    completeSpeedMarks = currentCheckedIndex + 1;
                    break;
                case R.id.ll_service_attitude_stars:
                    serviceAttitudeMarks = currentCheckedIndex + 1;
                    break;
            }
        }
    }

    private void displayStars(ViewGroup viewGroup, int currentCheckedIndex) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView ivStar = (ImageView) viewGroup.getChildAt(i);
            if (i <= currentCheckedIndex) {
                ivStar.setImageResource(R.mipmap.activation_star);
            } else {
                ivStar.setImageResource(R.mipmap.default_star);
            }
        }
    }

    //完成提交评论
    public void completeComment(View v) {
        if (serviceQualityMarks <= 0) {
            ToastUtils.shortToast("请给服务质量打分");
            return;
        }
        if (completeSpeedMarks <= 0) {
            ToastUtils.shortToast("请给完成速度打分");
            return;
        }
        if (serviceAttitudeMarks <= 0) {
            ToastUtils.shortToast("请给服务态度打分");
            return;
        }
        String remark = mActivityCommentBinding.etCommentContent.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            ToastUtils.shortToast("请填写评价描述信息");
            return;
        }
        //评价需求还是服务，由传入的type参数决定
        MyTaskEngine.comment(new BaseProtocol.IResultExecutor<CommentResultBean>() {
            @Override
            public void execute(CommentResultBean dataBean) {
                //评论成功
                //ToastUtils.shortToast("评论成功");
                setCommentSuccessDialogVisibility(View.VISIBLE);
                isCompleteComment = true;
                setGoBackIconVisibility(View.GONE);
                setCompleteCommentBtnVisibility(View.GONE);
                setCloseCommentBtnVisibility(View.VISIBLE);
                setCompleteCommentIconVisibility(View.VISIBLE);
                mActivityCommentBinding.etCommentContent.setEnabled(false);
                setBottomShareBtnVisibility(View.VISIBLE);
            }

            @Override
            public void executeResultError(String result) {
                //评论失败
                ToastUtils.shortToast("评论失败");
            }
        }, serviceQualityMarks + "", completeSpeedMarks + "", serviceAttitudeMarks + "", remark, type + "", tid + "", suid + "");
    }

    /**
     * 关闭评价成功弹框
     *
     * @param v
     */
    public void closeCommentSuccessDialog(View v) {
        setCommentSuccessDialogVisibility(View.GONE);
    }

    /**
     * 评价成功后弹框中的去分享按钮
     *
     * @param v
     */
    public void gotoShareTask(View v) {
        setCommentSuccessDialogVisibility(View.GONE);
        shareTask(null);
    }

    /**
     * 底部分享按钮
     *
     * @param v
     */
    public void shareTask(View v) {
//        ToastUtils.shortToast("分享任务");
        openShareLayer();
    }

    private void openShareLayer() {
        setShareLayerVisibility(View.VISIBLE);
    }

    /**
     * 取消分销按钮
     *
     * @param v
     */
    public void cancelShare(View v) {
        setShareLayerVisibility(View.GONE);
    }

    /**
     * 分享给微信好友
     *
     * @param v
     */
    public void shareToWeChat(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到微信朋友圈
     *
     * @param v
     */
    public void shareToWeChatCircle(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
        }
    }

    /**
     * 分享到QQ
     *
     * @param v
     */
    public void shareToQQ(View v) {
        UMShareAPI mShareAPI = UMShareAPI.get(mActivity);
        if (mShareAPI.isInstall(mActivity, SHARE_MEDIA.QQ)) {
            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
        } else {
            ToastUtils.shortToast("请先安装qq客户端");
        }
    }

    /**
     * 分享到QQ空间
     *
     * @param v
     */
    public void shareToQZone(View v) {
        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).withText("Good").withTargetUrl("https://www.baidu.com/").setCallback(umShareListener).share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogKit.v("platform" + platform);
            ToastUtils.shortToast(platform + " 分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.shortToast(platform + " 分享失败");
            if (t != null) {
                LogKit.v("throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.shortToast(platform + " 分享取消了");
        }
    };


    public void closeCommentActivity(View v) {
        mActivity.finish();
    }

    private int completeCommentIconVisibility = View.GONE;
    private int commentSuccessDialogVisibility = View.GONE;
    private int completeCommentBtnVisibility;
    private int closeCommentBtnVisibility;
    private int loadLayerVisibility;
    private int bottomShareBtnVisibility;
    private int goBackIconVisibility;

    private int shareLayerVisibility = View.GONE;

    @Bindable
    public int getShareLayerVisibility() {
        return shareLayerVisibility;
    }

    public void setShareLayerVisibility(int shareLayerVisibility) {
        this.shareLayerVisibility = shareLayerVisibility;
        notifyPropertyChanged(BR.shareLayerVisibility);
    }

    @Bindable
    public int getGoBackIconVisibility() {
        return goBackIconVisibility;
    }

    public void setGoBackIconVisibility(int goBackIconVisibility) {
        this.goBackIconVisibility = goBackIconVisibility;
        notifyPropertyChanged(BR.goBackIconVisibility);
    }

    @Bindable
    public int getBottomShareBtnVisibility() {
        return bottomShareBtnVisibility;
    }

    public void setBottomShareBtnVisibility(int bottomShareBtnVisibility) {
        this.bottomShareBtnVisibility = bottomShareBtnVisibility;
        notifyPropertyChanged(BR.bottomShareBtnVisibility);
    }

    @Bindable
    public int getLoadLayerVisibility() {
        return loadLayerVisibility;
    }

    public void setLoadLayerVisibility(int loadLayerVisibility) {
        this.loadLayerVisibility = loadLayerVisibility;
        notifyPropertyChanged(BR.loadLayerVisibility);
    }

    @Bindable
    public int getCompleteCommentBtnVisibility() {
        return completeCommentBtnVisibility;
    }

    public void setCompleteCommentBtnVisibility(int completeCommentBtnVisibility) {
        this.completeCommentBtnVisibility = completeCommentBtnVisibility;
        notifyPropertyChanged(BR.completeCommentBtnVisibility);
    }

    @Bindable
    public int getCloseCommentBtnVisibility() {
        return closeCommentBtnVisibility;
    }

    public void setCloseCommentBtnVisibility(int closeCommentBtnVisibility) {
        this.closeCommentBtnVisibility = closeCommentBtnVisibility;
        notifyPropertyChanged(BR.closeCommentBtnVisibility);
    }

    @Bindable
    public int getCommentSuccessDialogVisibility() {
        return commentSuccessDialogVisibility;
    }

    public void setCommentSuccessDialogVisibility(int commentSuccessDialogVisibility) {
        this.commentSuccessDialogVisibility = commentSuccessDialogVisibility;
        notifyPropertyChanged(BR.commentSuccessDialogVisibility);
    }

    @Bindable
    public int getCompleteCommentIconVisibility() {
        return completeCommentIconVisibility;
    }

    public void setCompleteCommentIconVisibility(int completeCommentIconVisibility) {
        this.completeCommentIconVisibility = completeCommentIconVisibility;
        notifyPropertyChanged(BR.completeCommentIconVisibility);
    }
}
