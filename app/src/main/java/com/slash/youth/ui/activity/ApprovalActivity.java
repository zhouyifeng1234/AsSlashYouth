package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.viewmodel.ApprovalModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import java.io.FileNotFoundException;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private TextView save;
    private ActivityApprovalBinding activityApprovalBinding;
    private Bitmap bitmap;
    private int careertype;
    private ApprovalModel approvalModel;
    private String photoUri;
    private String titleText = "认证";
   // private String submint = "提交";
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        careertype = intent.getIntExtra("careertype", -1);
        long uid = intent.getLongExtra("Uid", -1);
        activityApprovalBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval);
        approvalModel = new ApprovalModel(activityApprovalBinding,2, this,uid);
        activityApprovalBinding.setApprovalModel(approvalModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleText);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setVisibility(View.GONE);
       // save.setOnClickListener(this);
      //  save.setText(submint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int cardType = approvalModel.cardType;
        switch (requestCode) {
            case Constants.USERINFO_TAKEPHOTO://6
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    photo =  BitmapKit.zoomBitmap(bitmap, 250, 200);
                  //  listener.OnBackClick(photo);
                    //选择类型
                    switch (cardType) {
                        case Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD:
                            LogKit.d("名片");
                            setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_BUSINESS_CARD");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_IDCARD:
                            LogKit.d("身份证");
                            setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_IDCARD");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION:
                            LogKit.d("在职证明");
                             setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_MAIL:
                            LogKit.d("邮箱后台");
                            setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_MAIL");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_SIN:
                            LogKit.d("工牌");
                            setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_SIN");
                            break;
                        default:
                            photoUri = BitmapKit.saveBitmap(photo, "takePhoto");
                            break;
                    }
                    //跳转界面
                    jumpActivity();
                }
                break;
            case Constants.USERINFO_PHOTOALBUM://7
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        photo = BitmapKit.zoomBitmap(bitmap, 250, 200);

                        switch (cardType) {
                            case Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD:
                                setAndSavePhoto(photo,"business_card");
                                break;
                            case Cardtype.USER_REAL_AUTH_CARD_IDCARD:
                                setAndSavePhoto(photo,"id_card");
                                break;
                            case Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION:
                                setAndSavePhoto(photo,"incumbeny_certification");
                                break;
                            case Cardtype.USER_REAL_AUTH_CARD_MAIL:
                                setAndSavePhoto(photo,"mail");
                                break;
                            case Cardtype.USER_REAL_AUTH_CARD_SIN:
                                setAndSavePhoto(photo,"sin");
                                break;
                            default:
                                photoUri = BitmapKit.saveBitmap(photo, "photoAlbum");
                                break;
                        }
                        jumpAlbumActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAndSavePhoto(Bitmap photo,String photoName) {
        photoUri = BitmapKit.saveBitmap(photo,photoName);
    }

    private void jumpActivity() {
        Intent intentExamineActivity = new Intent(CommonUtils.getContext(), ExamineActivity.class);
        intentExamineActivity.putExtra("bitmap", photo);
        intentExamineActivity.putExtra("careertype", careertype);
        intentExamineActivity.putExtra("cardType", approvalModel.cardType);
        intentExamineActivity.putExtra("photoUri", photoUri);
        startActivity(intentExamineActivity);
    }

    private void jumpAlbumActivity() {
        Intent intentExamineActivity = new Intent(CommonUtils.getContext(), ExamineActivity.class);
        intentExamineActivity.putExtra("careertype", careertype);
        intentExamineActivity.putExtra("cardType", approvalModel.cardType);
        intentExamineActivity.putExtra("photoUri", photoUri);
        startActivity(intentExamineActivity);
    }

    //监听回调返回键
    public interface OnBackPhotoCListener{
        void OnBackClick(Bitmap photo);
    }
    private OnBackPhotoCListener listener;
    public void setOnBackPhotoListener(OnBackPhotoCListener listener) {
        this.listener = listener;
    }
}
