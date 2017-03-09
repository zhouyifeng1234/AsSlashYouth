package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.PhoneLoginResultBean;
import com.slash.youth.domain.SendPinResultBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.test.RichTextTestActivity;
import com.slash.youth.ui.activity.test.RoundedImageTestActivity;
import com.slash.youth.ui.activity.test.ScaleViewPagerTestActivity;
import com.slash.youth.ui.activity.test.TestActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PhoneNumUtils;
import com.slash.youth.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoModel extends BaseObservable {

    ActivityPerfectInfoBinding mActivityPerfectInfoBinding;
    Activity mActivity;
    Bundle thirdPlatformBundle;
    private String _3ptoken;
    private String userInfo;
    boolean isThirdLogin;

    public PerfectInfoModel(ActivityPerfectInfoBinding activityPerfectInfoBinding, Activity activity) {
        this.mActivityPerfectInfoBinding = activityPerfectInfoBinding;
        this.mActivity = activity;
        permissions = new RxPermissions(activity);
        initData();
        initView();
    }

    private void initData() {
        //清空SharePreferences中的登录信息,解决token时未完成个人信息的注册账号直接进入首页的问题
        LoginManager.clearSpLoginInfo();

        thirdPlatformBundle = mActivity.getIntent().getExtras();
        if (thirdPlatformBundle == null) {
            //表示是由手机号登录进入的，所以不再需要输入手机号，隐藏手机号输入框
            LogKit.v("phonenum login 表示是由手机号登录进入的，所以不再需要输入手机号，隐藏手机号输入框");
            setPhonenumLoginInfoVisibility(View.GONE);
            isThirdLogin = false;
        } else {
            isThirdLogin = true;
            _3ptoken = thirdPlatformBundle.getString("_3ptoken");
            userInfo = thirdPlatformBundle.getString("userInfo");
            LogKit.v("third login");
        }
    }

    private void initView() {

    }

    boolean isSendPin = false;

    /**
     * 发送验证码
     *
     * @param v
     */
    public void sendPin(View v) {
        if (isSendPin) {
            return;
        }

        String phoenNum = mActivityPerfectInfoBinding.etActivityPerfectInfoPhonenum.getText().toString();
        if (TextUtils.isEmpty(phoenNum)) {
            ToastUtils.shortToast("请输入手机号");
            return;
        }
        boolean isCorrect = PhoneNumUtils.checkPhoneNum(phoenNum);
        if (!isCorrect) {
            ToastUtils.shortToast("请输入正确的手机号码");
            return;
        }
        LogKit.v(phoenNum);
        isSendPin = true;
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(new BaseProtocol.IResultExecutor<SendPinResultBean>() {
            @Override
            public void execute(SendPinResultBean dataBean) {
//                ToastUtils.shortToast("获取验证码成功");
                ToastUtils.shortToast("验证码已发送，请查收");
                isSendPin = true;
                pinSecondsCount = 61;
                CommonUtils.getHandler().post(new PinCountDown());
            }

            @Override
            public void executeResultError(String result) {
//                ToastUtils.shortToast("获取验证码失败");
                isSendPin = false;
                ToastUtils.shortToast("验证码发送失败");
            }
        }, phoenNum);
    }

    int pinSecondsCount;

    private class PinCountDown implements Runnable {

        @Override
        public void run() {
            pinSecondsCount--;
            if (pinSecondsCount < 0) {
                mActivityPerfectInfoBinding.btnSendpinText.setText("验证码");
                mActivityPerfectInfoBinding.btnSendpinText.setBackgroundResource(R.drawable.btn_send_pin_blue);
                isSendPin = false;
            } else {
                mActivityPerfectInfoBinding.btnSendpinText.setText(pinSecondsCount + "S");
                mActivityPerfectInfoBinding.btnSendpinText.setBackgroundResource(R.drawable.btn_send_pin_gray);
                CommonUtils.getHandler().postDelayed(this, 1000);
            }
        }
    }

    private static final float compressPicMaxWidth = CommonUtils.dip2px(100);
    private static final float compressPicMaxHeight = CommonUtils.dip2px(100);
    RxPermissions permissions;

    public void openCamera(View v) {
        permissions.request(Manifest.permission.CAMERA)
                .subscribe(agree -> {
                    if (agree) {
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_CLICK_PICTURE);

//        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mActivity.startActivityForResult(intentCamera, 0);

                        FunctionConfig functionConfig = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setEnableCamera(true).build();

//        GalleryFinal.openGalleryMuti(20, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
//            @Override
//            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//
//            }
//
//            @Override
//            public void onHanlderFailure(int requestCode, String errorMsg) {
//
//            }
//        });

                        GalleryFinal.openGallerySingle(20, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                PhotoInfo photoInfo = resultList.get(0);
                                //LogKit.v("photoInfo.getPhotoPath()" + photoInfo.getPhotoPath());
                                //LogKit.v("photoInfo.getWidth()" + photoInfo.getWidth());
                                // LogKit.v("photoInfo.getHeight()" + photoInfo.getHeight());
                                FunctionConfig functionConfigCrop = new FunctionConfig.Builder().setCropSquare(true).build();
                                GalleryFinal.openCrop(21, functionConfigCrop, photoInfo.getPhotoPath(), new GalleryFinal.OnHanlderResultCallback() {
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
                                            LogKit.v("outWidth:" + outWidth);
                                            LogKit.v("outHeight:" + outHeight);
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

                                            String picCachePath = mActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                                            File cacheDir = new File(picCachePath);
                                            if (!cacheDir.exists()) {
                                                cacheDir.mkdir();
                                            }
                                            final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));
                                            //调用上传图片接口获取fileId
                                            DemandEngine.uploadFile(new BaseProtocol.IResultExecutor<UploadFileResultBean>() {
                                                @Override
                                                public void execute(UploadFileResultBean dataBean) {
                                                    setCameraIconVisibility(View.GONE);//隐藏头像中间的摄像机icon
                                                    String fileId = dataBean.data.fileId;
                                                    LogKit.v(fileId);
                                                    setUploadAvatarFileId(fileId);
                                                    setIsUploadAvatar(true);
                                                    //页面上显示头像，直接加载本地缓存的图片
                                                    ImageOptions.Builder builder = new ImageOptions.Builder();
                                                    ImageOptions imageOptions = builder.build();
                                                    builder.setCircular(true);
                                                    x.image().bind(mActivityPerfectInfoBinding.ivUserAvatar, tempFile.toURI().toString(), imageOptions);
                                                }

                                                @Override
                                                public void executeResultError(String result) {
                                                    LogKit.v("上传头像失败：" + result);
                                                    ToastUtils.shortToast("上传头像失败：" + result);
                                                }
                                            }, tempFile.getAbsolutePath());
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
                                        LogKit.v("---------------crop failure-----------------");
                                        LogKit.v("--------" + errorMsg + "----------------");
                                    }
                                });


                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {

                            }
                        });
                    }
                });

    }

    String phonenum;
    String pin;
    String realname;

    public void okPerfectInfo(View v) {
        if (!isUploadAvatar) {
            ToastUtils.shortToast("必须先上传头像");
            return;
        } else {
            //必须在设置头像和真实姓名前调用手机号登录接口，这样才能获取用户token
            phonenum = mActivityPerfectInfoBinding.etActivityPerfectInfoPhonenum.getText().toString();
            pin = mActivityPerfectInfoBinding.etActivityPerfectInfoVerificationCode.getText().toString();
            realname = mActivityPerfectInfoBinding.etActivityPerfectInfoRealname.getText().toString();

            if (isThirdLogin) {//三方登录
                //再次调用手机号登录接口
                if (TextUtils.isEmpty(phonenum)) {
                    ToastUtils.shortToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pin)) {
                    ToastUtils.shortToast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(realname)) {
                    ToastUtils.shortToast("真实姓名不能为空");
                    return;
                }

                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.REGISTER_WRITE_NAME_CLICK_COMPLETE);

                LoginManager.phoneLogin(new BaseProtocol.IResultExecutor<PhoneLoginResultBean>() {
                    @Override
                    public void execute(PhoneLoginResultBean dataBean) {
                        //如果登录失败，dataBean.data可能是null  {  "rescode": 7  }
                        if (dataBean.data == null) {
                            ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                            return;
                        }
                        String rongToken = dataBean.data.rongToken;//融云token
                        String token = dataBean.data.token;
                        long uid = dataBean.data.uid;
                        if (dataBean.rescode == 11) {
                            MsgManager.connectRongCloud(rongToken);
                            savaLoginState(uid, token, rongToken);
                            setRealname();
                        } else if (dataBean.rescode == 0) {
                            ToastUtils.shortToast("该手机号已经被注册过");
                        } else if (dataBean.rescode == 7) {
                            ToastUtils.shortToast("验证码错误");
                        } else {
                            ToastUtils.shortToast("登录失败:" + dataBean.rescode);
                        }
                    }

                    @Override
                    public void executeResultError(String result) {
                        //这里不会执行
                    }
                }, phonenum, pin, _3ptoken, userInfo);

            } else {//手机号登录
                //设置真实姓名
                if (TextUtils.isEmpty(realname)) {
                    ToastUtils.shortToast("真实姓名不能为空");
                    return;
                }
                setRealname();
            }
        }
    }

    private void setRealname() {
        LogKit.v("LoginManager.currentLoginUserId:" + LoginManager.currentLoginUserId);

        LoginManager.loginSetRealname(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                setAvatar();
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("设置真实姓名失败:" + result);
                ToastUtils.shortToast("设置真实姓名失败:" + result);
            }
        }, realname);
    }

    private void setAvatar() {
        //调用设置头像接口
        LoginManager.loginSetAvatar(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                Intent intentChooseSkillActivity = new Intent(CommonUtils.getContext(), ChooseSkillActivity.class);
                mActivity.startActivity(intentChooseSkillActivity);
                mActivity.finish();
                mActivity = null;
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("设置头像失败：" + result);
                ToastUtils.shortToast("设置头像失败：" + result);
            }
        }, getUploadAvatarFileId());
    }

    /**
     * @param uid
     * @param token
     * @param rongToken
     */
    private void savaLoginState(long uid, String token, String rongToken) {
        LoginManager.currentLoginUserId = uid;
        LoginManager.token = token;
        LoginManager.rongToken = rongToken;

//        SpUtils.setLong("uid", uid);
//        SpUtils.setString("token", token);
//        SpUtils.setString("rongToken", rongToken);
    }


    public void openTestActivity(View v) {
        Intent intentTestActivity = new Intent(CommonUtils.getContext(), TestActivity.class);
        intentTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentTestActivity);
    }

    public void openScaleViewPagerTestActivity(View v) {
        Intent intentScaleViewPagerTestActivity = new Intent(CommonUtils.getContext(), ScaleViewPagerTestActivity.class);
        intentScaleViewPagerTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentScaleViewPagerTestActivity);
    }

    public void openRoundedImageTestActivity(View v) {
        Intent intentRoundedImageTestActivity = new Intent(CommonUtils.getContext(), RoundedImageTestActivity.class);
        intentRoundedImageTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentRoundedImageTestActivity);
    }

    public void openRichTextTestActivity(View v) {
        Intent intentRichTextTestActivity = new Intent(CommonUtils.getContext(), RichTextTestActivity.class);
        intentRichTextTestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentRichTextTestActivity);
    }

    private int cameraIconVisibility = View.VISIBLE;
    private int phonenumLoginInfoVisibility = View.VISIBLE;

    private boolean isUploadAvatar = false;
    private String uploadAvatarFileId;

    @Bindable
    public String getUploadAvatarFileId() {
        return uploadAvatarFileId;
    }

    public void setUploadAvatarFileId(String uploadAvatarFileId) {
        this.uploadAvatarFileId = uploadAvatarFileId;
        notifyPropertyChanged(BR.uploadAvatarFileId);
    }

    @Bindable
    public boolean getIsUploadAvatar() {
        return isUploadAvatar;
    }

    public void setIsUploadAvatar(boolean isUploadAvatar) {
        this.isUploadAvatar = isUploadAvatar;
        notifyPropertyChanged(BR.isUploadAvatar);
    }

    @Bindable
    public int getPhonenumLoginInfoVisibility() {
        return phonenumLoginInfoVisibility;
    }

    public void setPhonenumLoginInfoVisibility(int phonenumLoginInfoVisibility) {
        this.phonenumLoginInfoVisibility = phonenumLoginInfoVisibility;
        notifyPropertyChanged(BR.phonenumLoginInfoVisibility);
    }

    @Bindable
    public int getCameraIconVisibility() {
        return cameraIconVisibility;
    }

    public void setCameraIconVisibility(int cameraIconVisibility) {
        this.cameraIconVisibility = cameraIconVisibility;
        notifyPropertyChanged(BR.cameraIconVisibility);
    }
}
