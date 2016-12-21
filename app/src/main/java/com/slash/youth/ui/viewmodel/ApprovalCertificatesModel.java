package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalCertificatesModel extends BaseObservable {
    private ApprovalCertificatesBinding approvalCertificatesBinding;
    private ApprovalActivity approvalActivity;
    private int realPosition;
    private String fileName;

    public ApprovalCertificatesModel(ApprovalCertificatesBinding approvalCertificatesBinding,ApprovalActivity approvalActivity,int position) {
        this.approvalCertificatesBinding = approvalCertificatesBinding;
        this.approvalActivity = approvalActivity;
        this.realPosition = position;
    }

    public void shotPhoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        approvalActivity.startActivityForResult(intent, Constants.USERINFO_TAKEPHOTO);

       /* approvalActivity.setOnBackPhotoListener(new ApprovalActivity.OnBackPhotoCListener() {
            @Override
            public void OnBackClick(Bitmap photo) {
                approvalCertificatesBinding.ivPhoto.setImageBitmap(photo);
            }
        });*/

      /*
        switch (realPosition){
            case 0://工牌
                fileName = "USER_REAL_AUTH_CARD_SIN";
                break;
            case 1://在职证明
                fileName = "USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION";
                break;
            case 2://邮箱后台
                fileName = "USER_REAL_AUTH_CARD_MAIL";
                break;
            case 3://名片
                fileName = "USER_REAL_AUTH_CARD_BUSINESS_CARD";
                break;*/

          /*  case 4://身份证
                fileName = "USER_REAL_AUTH_CARD_IDCARD";
                break;*/
      //  }
      //  x.image().bind( approvalCertificatesBinding.ivPhoto,new File(CommonUtils.getApplication().getCacheDir(), fileName).toURI().toString());
    }

    public void albunPhoto(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image*//*");//相片类型
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 100);
        approvalActivity.startActivityForResult(intent, Constants.USERINFO_PHOTOALBUM);
    }
}
