package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.databinding.DialogHintBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SetBaseProtocol;
import com.slash.youth.http.protocol.SetSaveUserInfoProtocol;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.ui.activity.ReplacePhoneActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.ui.view.WarpLinearLayout;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by zss on 2016/10/31.
 */
public class ActivityUserInfoEditorModel extends BaseObservable {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private String name;
    private String skilldescrib;
    private boolean checked = true;
    private String setLocation;
    private HashMap<String, String> paramsMap = new HashMap<>();
    private String[] slashIdentitys;
    private String company;
    private String position;
    public String direction;
    private long myUid;//我自己的id
    private UserinfoEditorActivity userinfoEditorActivity;
    private StringBuffer sb = new StringBuffer();
    public String city;
    public String province;
    private String phone;
    private String[] skillLabels;
    public ArrayList<String> skillLabelList = new ArrayList<>();
    public String avatar;
    public String industry;
    private String identity;
    public static int careertype;
    private String tag;
    private String slashIdentity;
    private int isauth;
    private WarpLinearLayout llSkilllabelContainer;
    private boolean isChange = false;
    private MyFirstPageBean.DataBean.MyinfoBean myinfo;
    private static final float compressPicMaxWidth = CommonUtils.dip2px(100);
    private static final float compressPicMaxHeight = CommonUtils.dip2px(100);

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding, long myUid, UserinfoEditorActivity userinfoEditorActivity) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
        this.myUid = myUid;
        this.userinfoEditorActivity = userinfoEditorActivity;
        initData();
        initView();
        listener();
    }

    private void initData() {
        llSkilllabelContainer = activityUserinfoEditorBinding.llSkilllabelContainer;
        MyManager.getMyUserinfo(new OnGetMyUserinfo());
    }

    private void initView() {
    }

    private void setUserView() {

        if (!TextUtils.isEmpty(skilldescrib)) {
            activityUserinfoEditorBinding.etSkilldescribe.setText(skilldescrib);
        }
        //头像的路径
        if (!TextUtils.isEmpty(avatar)) {
            BitmapKit.bindImage(activityUserinfoEditorBinding.ibClickAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        //姓名
        if (!TextUtils.isEmpty(name)) {
            activityUserinfoEditorBinding.etUsername.setText(name);
        }
        //手机号码
        if (!TextUtils.isEmpty(phone)) {
            activityUserinfoEditorBinding.tvUserphone.setText(phone);
        }
        //所在地
        if (province.equals(city) && !TextUtils.isEmpty(city)) {
            activityUserinfoEditorBinding.tvLocation.setText(city);
        } else if (!province.equals(city)) {
            activityUserinfoEditorBinding.tvLocation.setText(province + "" + city);
        }
        //斜杠身份
        String replace = identity.replace(",", "/");
        slashIdentity = replace;
        activityUserinfoEditorBinding.tvIdentity.setText(replace);
        //职业类型
        switch (careertype) {
            case 1://固定职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(true);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(false);
                setCompanyAndPosition(false);
                break;
            case 2://自由职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(false);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(true);
                setCompanyAndPosition(true);
                break;
        }
        //公司
        activityUserinfoEditorBinding.tvCompany.setText(company);
        //职位
        activityUserinfoEditorBinding.tvProfession.setText(position);

        //行业方向
        if (!TextUtils.isEmpty(industry) && !TextUtils.isEmpty(direction)) {
            activityUserinfoEditorBinding.tvDirection.setText(direction);
        }

        //技能标签
        skillLabels = tag.split(",");
        List<String> lists = Arrays.asList(skillLabels);
        skillLabelList.clear();
        skillLabelList.addAll(lists);

        if (!TextUtils.isEmpty(tag)) {
            activityUserinfoEditorBinding.llSkilllabelContainer.removeAllViews();
            String[] split = tag.split(",");
            for (String textTag : split) {
                TextView skillTag = createSkillTag(textTag);
                activityUserinfoEditorBinding.llSkilllabelContainer.addView(skillTag);
            }
        }
    }

    //生成标签
    public TextView createSkillTag(String textTag) {
        TextView textViewTag = new TextView(CommonUtils.getContext());
        textViewTag.setText(textTag);
        textViewTag.setTextColor(Color.parseColor("#31C5E4"));
        textViewTag.setTextSize(CommonUtils.dip2px(4));
        // textViewTag.setTextSize(11);
        textViewTag.setPadding(CommonUtils.dip2px(8), CommonUtils.dip2px(6), CommonUtils.dip2px(8), CommonUtils.dip2px(6));
        textViewTag.setBackgroundColor(Color.parseColor("#d6f3fa"));
        return textViewTag;
    }

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

    //点击头像
    public void clickAvatar(View view) {
        setUploadPicLayerVisibility(View.VISIBLE);
        //编辑头像的埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_AVATAR);
    }

    //照相
    public void photoGraph(View view) {
        FunctionConfig functionConfig = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setEnableCamera(true).build();
        GalleryFinal.openCamera(21, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                PhotoInfo photoInfo = resultList.get(0);
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

                            String picCachePath = userinfoEditorActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                            File cacheDir = new File(picCachePath);
                            if (!cacheDir.exists()) {
                                cacheDir.mkdir();
                            }
                            final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));

                            DemandEngine.uploadFile(new onUploadFile(), tempFile.getAbsolutePath());

                            ImageOptions.Builder builder = new ImageOptions.Builder();
                            ImageOptions imageOptions = builder.build();
                            avatar = tempFile.toURI().toString();
                            x.image().bind(activityUserinfoEditorBinding.ibClickAvatar, tempFile.toURI().toString(), imageOptions);
                            setUploadPicLayerVisibility(View.GONE);

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

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    public void getAlbumPic(View view) {
        //第三方
        FunctionConfig functionConfig = new FunctionConfig.Builder().setMutiSelectMaxSize(1).setEnableCamera(true).build();
        GalleryFinal.openGallerySingle(20, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                PhotoInfo photoInfo = resultList.get(0);
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

                            String picCachePath = userinfoEditorActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                            File cacheDir = new File(picCachePath);
                            if (!cacheDir.exists()) {
                                cacheDir.mkdir();
                            }
                            final File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));

                            DemandEngine.uploadFile(new onUploadFile(), tempFile.getAbsolutePath());

                            ImageOptions.Builder builder = new ImageOptions.Builder();
                            ImageOptions imageOptions = builder.build();
                            avatar = tempFile.toURI().toString();
                            x.image().bind(activityUserinfoEditorBinding.ibClickAvatar, tempFile.toURI().toString(), imageOptions);

                            setUploadPicLayerVisibility(View.GONE);

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

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    //设置斜杠身份
    public void editorIdentity(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_SLASH_IDENTITY);

        String slashIdentity = activityUserinfoEditorBinding.tvIdentity.getText().toString();
        Intent editorIdentityActivity = new Intent(CommonUtils.getContext(), EditorIdentityActivity.class);
        editorIdentityActivity.putExtra("editorIdentity", slashIdentity);
        userinfoEditorActivity.startActivityForResult(editorIdentityActivity, Constants.USERINFO_IDENTITY);
    }

    //设置手机号码
    public void setPhone(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_TELEPHONE_NUMBER);

        Intent replacePhoneActivity = new Intent(CommonUtils.getContext(), ReplacePhoneActivity.class);
        userinfoEditorActivity.startActivityForResult(replacePhoneActivity, Constants.USERINFO_PHONE);
    }

    //点击所在地
    public void setLocation(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_LOCATION);

        Intent intentCityLocationActivity = new Intent(CommonUtils.getContext(), CityLocationActivity.class);
        userinfoEditorActivity.startActivityForResult(intentCityLocationActivity, Constants.USERINFO_LOCATION);
    }

    //技能标签
    public void skillLabel(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_INDUSTRY_DIRECTION_SKILL_TAG);

        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        intentSubscribeActivity.putExtra("isEditor", true);
        intentSubscribeActivity.putExtra("industry", industry);
        intentSubscribeActivity.putExtra("direction", direction);
        intentSubscribeActivity.putStringArrayListExtra("addedTagsName", skillLabelList);
        intentSubscribeActivity.putStringArrayListExtra("addedTags", skillLabelList);
        userinfoEditorActivity.startActivityForResult(intentSubscribeActivity, Constants.USERINFO_SKILLLABEL);
    }

    //上传图片信息
    public void uploadPhoto(String fileId) {
        UserInfoEngine.setAvater(new onSetAvater(), fileId);
    }

    private String uploadFail = "上传照片失败";

    String avatarFileId;

    //上传图片
    public class onUploadFile implements BaseProtocol.IResultExecutor<UploadFileResultBean> {
        @Override
        public void execute(UploadFileResultBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                UploadFileResultBean.Data data = dataBean.data;
                avatarFileId = data.fileId;
//                if (!TextUtils.isEmpty(fileId)) {
//                    uploadPhoto(fileId);
//                }
            } else {
                LogKit.d(uploadFail);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.v("上传头像失败：" + result);
            ToastUtils.shortToast("上传头像失败：" + result);
        }
    }

    public class onSetAvater implements BaseProtocol.IResultExecutor<CommonResultBean> {
        @Override
        public void execute(CommonResultBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                CommonResultBean.Data data = dataBean.data;
                int status = data.status;
                switch (status) {
                    case 1:
                        LogKit.d("设置成功");
                        break;
                    case 0:
                        LogKit.d("设置失败");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //点击保存
    public void save(View view) {
        skilldescrib = activityUserinfoEditorBinding.etSkilldescribe.getText().toString();
        company = activityUserinfoEditorBinding.tvCompany.getText().toString();
        position = activityUserinfoEditorBinding.tvProfession.getText().toString();
        setLocation = activityUserinfoEditorBinding.tvLocation.getText().toString();
        direction = activityUserinfoEditorBinding.tvDirection.getText().toString();
        //监听斜杠身份
        slashIdentity = activityUserinfoEditorBinding.tvIdentity.getText().toString();
        name = activityUserinfoEditorBinding.etUsername.getText().toString();

        if (name.equals(myinfo.getName()) && avatar.equals(myinfo.getAvatar()) && phone.equals(myinfo.getPhone()) &&
                setLocation.equals(myinfo.getCity()) && slashIdentity.equals(myinfo.getIdentity()) && position.equals(myinfo.getPosition()) &&
                skilldescrib.equals(myinfo.getDesc())) {
            isChange = false;
        } else {
            isChange = true;
        }

        //三级标签
        ArrayList<String> listCheckedLabelName = SubscribeActivity.saveListCheckedLabelName;
        if (listCheckedLabelName.size() != 0) {//==0
            if (skillLabelList.size() == listCheckedLabelName.size()) {//三级标签
                Collections.sort(skillLabelList);
                Collections.sort(listCheckedLabelName);
                if (!listCheckedLabelName.contains(skillLabelList)) {
                    isChange = true;
                }
            } else {
                isChange = true;
            }
        }

        //一级标签
      /*  String tvDirection = activityUserinfoEditorBinding.tvDirection.getText().toString();
        String direction = myinfo.getIndustry() + "|" + myinfo.getDirection();
        if(direction.equals(tvDirection) ){
            isChange = false;
        }else {
            isChange = true;
        }*/

        if (TextUtils.isEmpty(name)) {
            ToastUtils.shortCenterToast("请填写姓名");
        } else if (name.length() >= 6) {
            ToastUtils.shortCenterToast("姓名最多5个字");
        } else {
            if ((company.length() <= 1 || company.length() >= 16) && checked) {
                ToastUtils.shortCenterToast("公司名字数2~15");
            } else {
                if ((position.length() < 2 || position.length() > 10) && checked) {
                    ToastUtils.shortCenterToast("公司职位名字数2~10");
                } else {
                    if (skilldescrib.length() > 50) {
                        ToastUtils.shortCenterToast("个人简介字数不超过50");
                    } else {
                        int childCount = llSkilllabelContainer.getChildCount();
                        if (childCount == 0) {
                            ToastUtils.shortCenterToast("请填写技能标签");
                        } else {
                            //认证过的
                            if (isauth == 1) {
                                if (isChange) {//有改动并且认证过，就显示弹框
                                    showDialog();
                                } else {//没有改动直接保存
                                    savePersonInfo();
                                    userinfoEditorActivity.finish();
                                }
                            } else {
                                //没有认证过
                                savePersonInfo();
                                userinfoEditorActivity.finish();
                            }
                        }
                    }
                }
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(userinfoEditorActivity);
        DialogHintBinding dialogHintBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_hint, null, false);
        DialogHintModel dialogHintModel = new DialogHintModel(dialogHintBinding);
        dialogHintBinding.setDialogHintModel(dialogHintModel);
        dialogBuilder.setView(dialogHintBinding.getRoot());
        AlertDialog dialogSearch = dialogBuilder.create();
        dialogSearch.show();
        dialogHintModel.currentDialog = dialogSearch;
        dialogSearch.setCanceledOnTouchOutside(false);
        Window dialogSubscribeWindow = dialogSearch.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(300);//宽度
        dialogParams.height = CommonUtils.dip2px(185);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
        dialogHintModel.setOnHintCklickListener(new DialogHintModel.OnHintClickListener() {
            @Override
            public void OnHintClick() {
                savePersonInfo();
                userinfoEditorActivity.finish();
            }
        });
    }

    private void savePersonInfo() {
        //保存头像
        if (!TextUtils.isEmpty(avatarFileId)) {
            uploadPhoto(avatarFileId);
        }
        //保存姓名，技能描述，职业类型
        name = activityUserinfoEditorBinding.etUsername.getText().toString();
        skilldescrib = activityUserinfoEditorBinding.etSkilldescribe.getText().toString();
        checked = activityUserinfoEditorBinding.rbOfficeWorker.isChecked();
        saveUserInfo(name, checked ? 1 : 2, skilldescrib);

        //保存公司和职位
        if (checked && company != null && position != null) {
            paramsMap.put("company", company);
            paramsMap.put("position", position);
            SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_COMPANY_AND_POSITION, paramsMap);
        }

        //保存所在地
        if (city != null && province != null) {
            paramsMap.put("province", province);
            paramsMap.put("city", city);
            SetProtocol(GlobalConstants.HttpUrl.SET_LOCATION, paramsMap);
        } else if (TextUtils.isEmpty(province) && city != null) {
            paramsMap.put("province", city);
            paramsMap.put("city", city);
            SetProtocol(GlobalConstants.HttpUrl.SET_LOCATION, paramsMap);
        }

        //保存行业方向
        paramsMap.put("industry", industry);
        paramsMap.put("direction", direction);
        SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_INDUSTRY, paramsMap);

        //斜杠身份只能包含中文\英文\数字
        ArrayList<String> newSkillLabelList = EditorIdentityModel.newSkillLabelList;
        UserInfoEngine.onSaveSlathYouth(new onSaveSlathYouth(), newSkillLabelList);


        //技能标签
        ArrayList<String> listCheckedLabelName = SubscribeActivity.saveListCheckedLabelName;
        if (!listCheckedLabelName.isEmpty()) {
            UserInfoEngine.onSaveListTag(new onSaveSlathYouth(), listCheckedLabelName);
        }
    }

    private void saveUserInfo(String name, int careertype, String desc) {
        if (name.length() < 256 && desc.length() < 4096) {
            SetSaveUserInfoProtocol setSaveUserInfoProtocol = new SetSaveUserInfoProtocol(name, careertype, desc);
            setSaveUserInfoProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
                @Override
                public void execute(SetBean dataBean) {
                    int rescode = dataBean.rescode;
                    if (rescode != 0) {
                        LogKit.d("保存用户信息错误");
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.d("result:" + result);
                }
            });
        }

        if (name.length() > 256) {
            ToastUtils.shortToast("姓名字数过多");
        }

        if (desc.length() > 4096) {
            ToastUtils.shortToast("技能描述字数过多");
        }
    }

    private void listener() {
        activityUserinfoEditorBinding.rbOfficeWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.rb_office_worker) {
                    //埋点
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_CHOOSE_OCCUPATION_TYPE);
                    checked = isChecked;
                    setCompanyAndPosition(!checked);
                }
            }
        });

        //技能描述
        activityUserinfoEditorBinding.etSkilldescribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_SKILL_DESCRIBE);
                }
                return false;
            }
        });

        //编辑姓名
        activityUserinfoEditorBinding.etUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_NAME);
                }
                return false;
            }
        });
        //编辑公司
        activityUserinfoEditorBinding.tvCompany.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_RECENT_COMPANY);
                }
                return false;
            }
        });
        //编辑职位
        activityUserinfoEditorBinding.tvProfession.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_EDIT_COMPANY_POSITION);
                }
                return false;
            }
        });
    }

    private void setCompanyAndPosition(boolean isChecked) {
        if (isChecked) {
            activityUserinfoEditorBinding.rlCompany.setVisibility(View.GONE);
            activityUserinfoEditorBinding.viewPosition.setVisibility(View.GONE);
            activityUserinfoEditorBinding.rlPosition.setVisibility(View.GONE);
            activityUserinfoEditorBinding.viewCompany.setVisibility(View.GONE);
            activityUserinfoEditorBinding.tvCompany.setText("");
            activityUserinfoEditorBinding.tvProfession.setText("");
        } else {
            activityUserinfoEditorBinding.rlCompany.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.viewPosition.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.rlPosition.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.viewCompany.setVisibility(View.VISIBLE);
        }
    }

    //设置协议 参数String
    private void SetProtocol(String url, Map<String, String> paramsMap) {
        SetBaseProtocol setBaseProtocol = new SetBaseProtocol(url, paramsMap);
        setBaseProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if (rescode == 0) {
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    LogKit.d("status:" + status);
                } else {
                    LogKit.d("SetBaseProtocol: 有错误");
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
        paramsMap.clear();
    }

    //保存斜杠身份
    public class onSaveSlathYouth implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status) {
                    case 0:
                        LogKit.d("保存失败");
                        break;
                    case 1:
                        LogKit.d("保存成功");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //获取个人信息的接口
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<MyFirstPageBean> {
        @Override
        public void execute(MyFirstPageBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                MyFirstPageBean.DataBean data = dataBean.getData();
                myinfo = data.getMyinfo();
                setUserInfoEditor(myinfo);
                setUserView();
            } else {
                LogKit.d("rescode=" + rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //获取数据
    private void setUserInfoEditor(MyFirstPageBean.DataBean.MyinfoBean myinfo) {
        avatar = myinfo.getAvatar();
        name = myinfo.getName();
        phone = myinfo.getPhone();
        province = myinfo.getProvince();
        city = myinfo.getCity();
        identity = myinfo.getIdentity();
        careertype = myinfo.getCareertype();
        company = myinfo.getCompany();
        position = myinfo.getPosition();
        industry = myinfo.getIndustry();
        direction = myinfo.getDirection();
        tag = myinfo.getTag();
        isauth = myinfo.getIsauth();
        skilldescrib = myinfo.getDesc();
    }
}