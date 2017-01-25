package com.slash.youth.ui.viewmodel;

import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CreatePassWordProtovol;
import com.slash.youth.http.protocol.SetPassWordProtocol;
import com.slash.youth.http.protocol.UploadPhotoProtocol;
import com.slash.youth.ui.activity.FindPassWordActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordModel extends BaseObservable {
    private ActivityFindPasswordBinding activityFindPasswordBinding;
    private FindPassWordActivity findPassWordActivity;
    public Map<String, String> createPassWordMap = new HashMap<>();
    public Map<String, String> surePassWordMap = new HashMap<>();
    private String toastText = "两次输入的密码不一致,请重新输入密码";
    private int type;
    private static final float compressPicMaxWidth = CommonUtils.dip2px(100);
    private static final float compressPicMaxHeight = CommonUtils.dip2px(100);
    public String fileId;

    public FindPassWordModel(ActivityFindPasswordBinding activityFindPasswordBinding, FindPassWordActivity findPassWordActivity,int type) {
        this.activityFindPasswordBinding = activityFindPasswordBinding;
        this.findPassWordActivity = findPassWordActivity;
        this.type = type;
        listener();
    }

    private void listener() {
        getCtretePassWord(activityFindPasswordBinding.etSetNewPassword);
        getSurePassWord(activityFindPasswordBinding.etSureNewPassword);
    }

    private void getCtretePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String setPassWord = s.toString();
                if (setPassWord.length() == 6) {
                    createPassWordMap.put("createPassWord", setPassWord);
                }
            }

        });
    }

    private void getSurePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String setPassWord = s.toString();
                if (setPassWord.length() == 6) {
                    surePassWordMap.put("surePassWord", setPassWord);
                    //判断2次密码是否一致
                    String createPassWord = createPassWordMap.get("createPassWord");
                    String surePassWord = surePassWordMap.get("surePassWord");
                    if (!createPassWord.equals(surePassWord)) {
                        ToastUtils.shortCenterToast(toastText);
                        createPassWordMap.clear();
                        surePassWordMap.clear();
                    }
                }
            }
        });
    }

    //点击拍照
    public void photo(View view) {
        switch (type){
            case 1:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SET_FIND_TRADE_PASSWORD_UPLOAD_PICTURE);
                break;
            case 2:
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SET_SET_TRADE_PASSWORD_UPLOAD_PICTURE);
                break;
        }

        setUploadPicLayerVisibility(View.VISIBLE);
    }

    //照相
    public void photoGraph(View view) {
        setUploadPicLayerVisibility(View.GONE);

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

                    String picCachePath = findPassWordActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                    File cacheDir = new File(picCachePath);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdir();
                    }
                    final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));

                    DemandEngine.uploadFile(new onUploadFile(),tempFile.getAbsolutePath());
                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    ImageOptions imageOptions = builder.build();
                    x.image().bind( activityFindPasswordBinding.ivPhoto, tempFile.toURI().toString(), imageOptions);

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

    //相册
    public void getAlbumPic(View view) {
        setUploadPicLayerVisibility(View.GONE);

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

                    String picCachePath = findPassWordActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                    File cacheDir = new File(picCachePath);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdir();
                    }
                    final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));
                    //展示照片
                    DemandEngine.uploadFile(new onUploadFile(),tempFile.getAbsolutePath());

                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    ImageOptions imageOptions = builder.build();
                    x.image().bind( activityFindPasswordBinding.ivPhoto, tempFile.toURI().toString(), imageOptions);

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

    //上传图片
    public class onUploadFile implements BaseProtocol.IResultExecutor<UploadFileResultBean> {
        @Override
        public void execute(UploadFileResultBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                UploadFileResultBean.Data data = dataBean.data;
                fileId = data.fileId;
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.v("上传头像失败：" + result);
            ToastUtils.shortToast("上传头像失败：" + result);
        }
    }

    //关闭弹窗
    public void closeUploadPic(View v) {
        setUploadPicLayerVisibility(View.GONE);
    }

    private int uploadPicLayerVisibility = View.GONE;

    @Bindable
    public int getUploadPicLayerVisibility() {
        return uploadPicLayerVisibility;
    }

    public void setUploadPicLayerVisibility(int uploadPicLayerVisibility) {
        this.uploadPicLayerVisibility = uploadPicLayerVisibility;
        notifyPropertyChanged(BR.uploadPicLayerVisibility);
    }

    //设置密码
    public void setPassWord(String oldpass, String newpass) {
        SetPassWordProtocol setPassWordProtocol = new SetPassWordProtocol(oldpass, newpass);
        setPassWordProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if (rescode == 0) {
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    switch (status) {
                        case 1:
                            LogKit.d("设置成功");
                            ToastUtils.shortCenterToast("密码设置成功");
                            findPassWordActivity.finish();
                            break;
                        case 2:
                            LogKit.d("设置失败");
                            ToastUtils.shortCenterToast("密码设置失败");
                            break;
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    //创建密码，需要后台审核
    public void createPassWord(String passWord, String url) {
        ContactsManager.onCreatePassWord(new onCreatePassWord(), passWord, url);
    }

    //找回交易密码
    public void findPassWord(String passWord, String url) {
        ContactsManager.onFindPassWord(new onFindPassWord(), passWord, url);
    }

    //创建交易密码
    public class onCreatePassWord implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status) {
                    case 1://1表示申请创建交易密码成功请等待审核
                        ToastUtils.shortCenterToast("提交成功,后台审核中");
                        findPassWordActivity.finish();
                        break;
                    case 2://2表示由于服务端错误导致申请创建交易密码失败请重新提交审核
                        LogKit.d("设由于服务端错误导致申请创建交易密码失败请重新提交审核");
                        ToastUtils.shortCenterToast("设置失败");
                        break;
                    case 3://3表示已经申请过创建密码的审核，无需重复申请请耐心等待
                        LogKit.d("已经申请过创建密码的审核，无需重复申请请耐心等待");
                    break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //找回交易密码
    public class onFindPassWord implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        ToastUtils.shortCenterToast("找回交易密码成功，后台审核中");
                        findPassWordActivity.finish();
                        break;
                    case 0:
                        ToastUtils.shortCenterToast("找回交易密码失败");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }
}
