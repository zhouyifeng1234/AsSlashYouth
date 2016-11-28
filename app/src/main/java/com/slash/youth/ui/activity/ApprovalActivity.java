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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        careertype = intent.getIntExtra("careertype", -1);
        activityApprovalBinding = DataBindingUtil.setContentView(this, R.layout.activity_approval);
        approvalModel = new ApprovalModel(activityApprovalBinding,2, this);
        activityApprovalBinding.setApprovalModel(approvalModel);
        listener();
    }


    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("认证");
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setOnClickListener(this);
        save.setText("提交");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                Intent intentExamineActivity = new Intent(CommonUtils.getContext(), ExamineActivity.class);
                intentExamineActivity.putExtra("bitmap", bitmap);
                intentExamineActivity.putExtra("careertype", careertype);
                intentExamineActivity.putExtra("cardType", approvalModel.cardType);
                intentExamineActivity.putExtra("photoUri", photoUri);
                startActivity(intentExamineActivity);
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
                    Bitmap photo = BitmapKit.zoomBitmap(bitmap, 250, 200);

                    ApprovalCertificatesBinding approvalCertificatesBinding = approvalModel.approvalCertificatesBinding;
                    approvalCertificatesBinding.flCertificates.removeAllViews();

                    //选择类型
                    switch (cardType) {
                        case Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD:
                           // setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_BUSINESS_CARD");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_IDCARD:

                            ImageView imageView = new ImageView(CommonUtils.getContext());
                            imageView.setImageBitmap(photo);
                            approvalCertificatesBinding.flCertificates.addView(imageView);
                            approvalModel.viewpageAdapter.notifyDataSetChanged();//后台


                          //  setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_IDCARD");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION:


                            ImageView imageView1 = new ImageView(CommonUtils.getContext());
                            imageView1.setImageBitmap(photo);
                            approvalCertificatesBinding.flCertificates.addView(imageView1);
                            approvalModel.viewpageAdapter.notifyDataSetChanged();



                            // setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_MAIL:
                           // setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_MAIL");
                            break;
                        case Cardtype.USER_REAL_AUTH_CARD_SIN:
                          //  setAndSavePhoto(photo,"USER_REAL_AUTH_CARD_SIN");
                            break;
                        default:
                           // activityApprovalBinding.ivCertificates.setImageBitmap(photo);
                           // photoUri = BitmapKit.saveBitmap(photo, "photo");
                            break;
                    }
                }
                break;
            case Constants.USERINFO_PHOTOALBUM://7
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    photoUri = uri.toString();
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        Bitmap photo = BitmapKit.zoomBitmap(bitmap, 250, 200);

                        switch (cardType) {
                            case Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD:
                            case Cardtype.USER_REAL_AUTH_CARD_IDCARD:
                            case Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION:
                            case Cardtype.USER_REAL_AUTH_CARD_MAIL:
                            case Cardtype.USER_REAL_AUTH_CARD_SIN:
                            ApprovalCertificatesBinding approvalCertificatesBinding = approvalModel.approvalCertificatesBinding;
                            approvalCertificatesBinding.ivCertificates.setImageBitmap(photo);
                                break;
                            default:
                                activityApprovalBinding.ivCertificates.setImageBitmap(photo);
                                break;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAndSavePhoto(Bitmap photo,String photoName) {
      /*  ApprovalCertificatesBinding approvalCertificatesBinding = approvalModel.approvalCertificatesBinding;
        approvalCertificatesBinding.ivCertificates.setImageBitmap(photo);*/
        photoUri = BitmapKit.saveBitmap(photo,photoName);
    }

    private void setPhoto(Intent data) {
        Bundle bundle = data.getExtras();
        bitmap = (Bitmap) bundle.get("data");
        Bitmap photo = BitmapKit.zoomBitmap(bitmap, 250, 200);
        activityApprovalBinding.ivCertificates.setImageBitmap(photo);
        photoUri = BitmapKit.saveBitmap(photo, "photo");
    }

}
