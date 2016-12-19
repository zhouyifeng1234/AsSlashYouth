package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.view.View;

import com.slash.youth.databinding.ActivityExamineCertificatesBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ExamineActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class ExamineCertificatesModel extends BaseObservable {
    private ActivityExamineCertificatesBinding activityExamineCertificatesBinding;
    private ExamineActivity examineActivity;
    private Bitmap bitmap;
    private int type;
    private int cardType;
    private String photoUri;

    public ExamineCertificatesModel(ActivityExamineCertificatesBinding activityExamineCertificatesBinding,
                                    ExamineActivity examineActivity, Bitmap bitmap,int type,int cardType,String photoUri) {
        this.activityExamineCertificatesBinding = activityExamineCertificatesBinding;
        this.examineActivity = examineActivity;
        this.cardType = cardType;
        this.bitmap = bitmap;
        this.type = type;
        this.photoUri = photoUri;
        initView();

    }

    private void initView() {
        if(bitmap!=null){
            Bitmap photo = BitmapKit.zoomBitmap(bitmap, 220, 200);
            activityExamineCertificatesBinding.ivCertificates.setImageBitmap(photo);
        }

    }

    //提交审核
    public void examine(View view){
        MyManager.checkoutAuth(new onCheckoutAuth(),type, cardType,photoUri);
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
                LogKit.d("status:"+status);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
