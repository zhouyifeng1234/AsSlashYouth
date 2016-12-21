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
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

import java.io.File;

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
    private String fileName;

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
        }else {
            switch (cardType){
                case 1://工牌
                    fileName = "sin";
                    break;
                case 2://在职证明
                    fileName = "incumbeny_certification";
                    break;
                case 3://邮箱后台
                    fileName = "mail";
                    break;
                case 4://名片
                    fileName = "business_card";
                    break;
           }
        x.image().bind(activityExamineCertificatesBinding.ivCertificates,new File(CommonUtils.getApplication().getCacheDir(), fileName).toURI().toString());
        }

    }

    //提交审核
    public void examine(View view){
     if(bitmap!=null){
         MyManager.checkoutAuth(new onCheckoutAuth(),type, cardType,photoUri);
     }else {
         String photoUri = new File(CommonUtils.getApplication().getCacheDir(), fileName).toURI().toString();
         MyManager.checkoutAuth(new onCheckoutAuth(),type, cardType,photoUri);
     }
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
