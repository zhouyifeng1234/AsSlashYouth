package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ExamineActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.ui.adapter.ViewpageAdapter;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.StringUtils;
import com.slash.youth.utils.ToastUtils;
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
 * Created by zss on 2016/11/5.
 */
public class ApprovalModel extends BaseObservable {
    private ActivityApprovalBinding activityApprovalBinding;
    public static int careertype;
    /*private int[] images ={R.mipmap.workcard_image,R.mipmap.onjob_proof_image,R.mipmap.mailbox_image,
            R.mipmap.business_card,R.mipmap.id_image};*/
    private int[] images ={R.mipmap.workcard_image,R.mipmap.onjob_proof_image,R.mipmap.mailbox_image,
            R.mipmap.business_card};
    //public String[] names ={"工牌","在职证明等其他证件","邮箱后台截图","名片","身份证"};
    public String[] names ={"工牌","在职证明等其他证件","邮箱后台截图","名片"};
    public ViewpageAdapter viewpageAdapter;
    //private String[] titles ={"请上传身份证正面","工牌、名片、邮箱后台截图、其他在职资料任选其一"};
    private String[] titles ={"工牌、名片、邮箱后台截图、其他在职资料任选其一"};
    private ApprovalActivity approvalActivity;
    public static int cardType = 1;
    public ApprovalCertificatesBinding approvalCertificatesBinding;
    private long uid;
    private String avatar;

    private MyFirstPageBean.DataBean.MyinfoBean myinfo;
    private static final float compressPicMaxWidth = CommonUtils.dip2px(100);
    private static final float compressPicMaxHeight = CommonUtils.dip2px(100);

    public ApprovalModel(ActivityApprovalBinding activityApprovalBinding, int careertype , ApprovalActivity approvalActivity,long uid ) {
        this.activityApprovalBinding = activityApprovalBinding;
        this.careertype = careertype;
        this.approvalActivity = approvalActivity;
        this.uid = uid;
        initData();
    }

    //点击修改
    public void modify(View view){
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE_PERFECT_APPROVE);
        Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
        intentUserinfoEditorActivity.putExtra("myId",uid);
        approvalActivity.startActivityForResult(intentUserinfoEditorActivity, Constants.APPROVAL_TYPE);
    }

    public  void initData() {
        MyManager.checkoutAuthProcess(new checkoutAuthProcess());
        MyManager.getMyUserinfo(new OnGetMyUserinfo());
    }

    public class checkoutAuthProcess implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 0://用户不存在
                        LogKit.d("用户不存在");
                        break;
                    case 1://认证中
                        showDialog();
                        break;
                    case 2://认证成功
                        LogKit.d("认证成功");
                        break;
                    case 3://认证失败
                        LogKit.d("认证失败");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //获取我的用户信息数据
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<MyFirstPageBean> {
        @Override
        public void execute(MyFirstPageBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                MyFirstPageBean.DataBean data = dataBean.getData();
                myinfo = data.getMyinfo();
                 careertype = myinfo.getCareertype();
                initView();
            }else {
                LogKit.d("rescode : "+rescode);
            }
        }
        @Override
        public void executeResultError(String result) {
        }
    }

    private void initView() {
        switch (careertype){
            case 1://上班的
                setViewPage();
                break;
            case 2://自顾者,只要认证身份证
                activityApprovalBinding.vpApprovalContainer.setVisibility(View.GONE);
                activityApprovalBinding.cdIdImag.setVisibility(View.VISIBLE);
                break;
            default://默认，上班
                setViewPage();
                break;
        }
    }

    private void setViewPage() {
        activityApprovalBinding.vpApprovalContainer.setVisibility(View.VISIBLE);
        activityApprovalBinding.cdIdImag.setVisibility(View.GONE);
        viewpageAdapter = new ViewpageAdapter(images,names,approvalActivity);
        activityApprovalBinding.vpApprovalContainer.setAdapter(viewpageAdapter);
        //预加载3个页面
        activityApprovalBinding.vpApprovalContainer.setOffscreenPageLimit(3);
        //两个页面的间距
        activityApprovalBinding.vpApprovalContainer.setPageMargin(CommonUtils.dip2px(10));
        //页面切换监听
        activityApprovalBinding.vpApprovalContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == images.length && positionOffset > 0.99) {
                    //在position5左滑且左滑positionOffset百分比接近1时，偷偷替换为position1（原本会滑到position6）
                    activityApprovalBinding.vpApprovalContainer.setCurrentItem(1, false);
                } else if (position == 0 && positionOffset < 0.01) {
                    //在position1右滑且右滑百分比接近0时，偷偷替换为position5（原本会滑到position0）
                    activityApprovalBinding.vpApprovalContainer.setCurrentItem(4, false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                activityApprovalBinding.tvApprovalTitle.setText(titles[0]);
                approvalCertificatesBinding = viewpageAdapter.approvalCertificatesBinding;
                //选中哪一个证件
                switch (position){
                    case 1:
                    cardType = Cardtype.USER_REAL_AUTH_CARD_SIN;
                        break;
                    case 2:
                    cardType = Cardtype.USER_REAL_AUTH_CARD_INCUMBENCY_CERTIFICATION;
                        break;
                    case 3:
                    cardType = Cardtype.USER_REAL_AUTH_CARD_MAIL;
                        break;
                    case 4:
                    cardType = Cardtype.USER_REAL_AUTH_CARD_BUSINESS_CARD;
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void showDialog() {
        DialogUtils.showDialogFour(approvalActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {
                approvalActivity.finish();
            }
        });
    }

    //拍照
    public void takePhoto(View view){
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

                    jumpAlbumActivity(careertype,cardType,tempFile.getAbsolutePath());

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
    public void  photoAlbum(View view){
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

                    jumpAlbumActivity(careertype,cardType,tempFile.getAbsolutePath());

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
