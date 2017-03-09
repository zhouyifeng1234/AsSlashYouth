package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.databinding.ActivityExamineCertificatesBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ExamineActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.io.File;

/**
 * Created by zss on 2016/11/6.
 */
public class ExamineCertificatesModel extends BaseObservable {
    private ActivityExamineCertificatesBinding activityExamineCertificatesBinding;
    private ExamineActivity examineActivity;
    private int type;
    private int cardType;
    private String photoUri;
    private String fileId;

    public ExamineCertificatesModel(ActivityExamineCertificatesBinding activityExamineCertificatesBinding,
                                    ExamineActivity examineActivity,int type,int cardType,String photoUri) {
        this.activityExamineCertificatesBinding = activityExamineCertificatesBinding;
        this.examineActivity = examineActivity;
        this.cardType = cardType;
        this.type = type;
        this.photoUri = photoUri;
        initData();
    }

    private void initData() {
        switch (cardType){
            case Cardtype.USER_REAL_AUTH_CARD_IDCARD:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_IDENTITY_CARD_UPLOAD);
                break;
            case Cardtype.USER_REAL_AUTH_CARD_MAIL:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_MAILBOX_BACKGROUND_UPLOAD);
                break;
            case Cardtype.USER_REAL_AUTH_CARD_SIN:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_BADGE_UPLOAD);
                break;
            case Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_COMPANY_BUSINESS_CARD_UPLOAD);
                break;
            case Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_OTHER_EVIDENTIARY_MATERIAL_UPLOAD);
                break;
        }
       DemandEngine.uploadFile(new onUploadFile(),photoUri);
    }

    //上传图片
    public class onUploadFile implements BaseProtocol.IResultExecutor<UploadFileResultBean> {
        @Override
        public void execute(UploadFileResultBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                UploadFileResultBean.Data data = dataBean.data;
                fileId = data.fileId;
                if (!TextUtils.isEmpty(fileId)) {
                    initView(fileId);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
           // LogKit.v("上传头像失败：" + result);
           // ToastUtils.shortToast("上传头像失败：" + result);
        }
    }

    private void initView(String fileId) {
        BitmapKit.bindImage(activityExamineCertificatesBinding.ivCertificates, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + fileId);
    }

    //提交审核
    public void examine(View view){
        MyManager.checkoutAuth(new onCheckoutAuth(), type, cardType, fileId);
    }

    //重新上传
    public void upload(View view){
        examineActivity.finish();
    }

    //提交认证
    public class onCheckoutAuth implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 0:
                        ToastUtils.shortCenterToast("上传图片失败");
                        break;
                    case 1:
                        ToastUtils.shortCenterToast("上传图片成功");
                        examineActivity.finish();
                        break;
                    default:
                        LogKit.d("status:"+status);
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
