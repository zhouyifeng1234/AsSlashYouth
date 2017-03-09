package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ExamineActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

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
        FunctionConfig functionConfig = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setEnableCamera(true).build();
        GalleryFinal.openCamera(21, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                Bitmap bitmap = null;
                try {
                    PhotoInfo photoInfo = resultList.get(0);
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(photoInfo.getPhotoPath(), bitmapOptions);
                    int outWidth = bitmapOptions.outWidth;
                    int outHeight = bitmapOptions.outHeight;
                    if (outWidth <= 0 || outHeight <= 0) {
                        ToastUtils.shortToast("请选择图片文件");
                        return;
                    }
                    int scale = 1;
                    int widthScale = (int) (outWidth / compressPicMaxWidth + 0.5f);
                    int heightScale = (int) (outHeight / compressPicMaxHeight + 0.5f);
                    if (widthScale > heightScale) {
                        scale = widthScale;
                    } else {
                        scale = heightScale;
                    }
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmapOptions.inSampleSize = scale;
                    bitmap = BitmapFactory.decodeFile(photoInfo.getPhotoPath(), bitmapOptions);

                    String picCachePath = approvalActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                    File cacheDir = new File(picCachePath);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdir();
                    }
                    final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));

                    jumpAlbumActivity(ApprovalModel.careertype,ApprovalModel.cardType,tempFile.getAbsolutePath());

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    private static final float compressPicMaxWidth = CommonUtils.dip2px(100);
    private static final float compressPicMaxHeight = CommonUtils.dip2px(100);
    public void albunPhoto(View view){
        FunctionConfig functionConfig = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setEnableCamera(true).build();
        GalleryFinal.openGallerySingle(20, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                Bitmap bitmap = null;
                try {
                    PhotoInfo photoInfo = resultList.get(0);
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(photoInfo.getPhotoPath(), bitmapOptions);
                    int outWidth = bitmapOptions.outWidth;
                    int outHeight = bitmapOptions.outHeight;
                    if (outWidth <= 0 || outHeight <= 0) {
                        ToastUtils.shortToast("请选择图片文件");
                        return;
                    }
                    int scale = 1;
                    int widthScale = (int) (outWidth / compressPicMaxWidth + 0.5f);
                    int heightScale = (int) (outHeight / compressPicMaxHeight + 0.5f);
                    if (widthScale > heightScale) {
                        scale = widthScale;
                    } else {
                        scale = heightScale;
                    }
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmapOptions.inSampleSize = scale;
                    bitmap = BitmapFactory.decodeFile(photoInfo.getPhotoPath(), bitmapOptions);

                    String picCachePath = approvalActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                    File cacheDir = new File(picCachePath);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdir();
                    }
                    final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));

                    //认证页面
                   jumpAlbumActivity(ApprovalModel.careertype,ApprovalModel.cardType,tempFile.getAbsolutePath());

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    private void jumpAlbumActivity(int careertype,int cardType,String url) {
        Intent intentExamineActivity = new Intent(CommonUtils.getContext(), ExamineActivity.class);
        intentExamineActivity.putExtra("careertype", careertype);
        intentExamineActivity.putExtra("cardType", cardType);
        intentExamineActivity.putExtra("photoUri",url);
        approvalActivity.startActivity(intentExamineActivity);
    }
}
