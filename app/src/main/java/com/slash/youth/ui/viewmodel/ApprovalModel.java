package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.ui.adapter.ViewpageAdapter;
import com.slash.youth.utils.Cardtype;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalModel extends BaseObservable {
    private ActivityApprovalBinding activityApprovalBinding;
    private int careertype;
    private int[] images ={R.mipmap.workcard_image,R.mipmap.onjob_proof_image,R.mipmap.mailbox_image,
            R.mipmap.business_card,R.mipmap.id_image};
    public String[] names ={"工牌","在职证明等其他证件","邮箱后台截图","名片","身份证"};
    public ViewpageAdapter viewpageAdapter;
    private String[] titles ={"请上传身份证正面","工牌、名片、邮箱后台截图、其他在职资料任选其一"};
    private ApprovalActivity approvalActivity;
    public int cardType;
    public ApprovalCertificatesBinding approvalCertificatesBinding;


    public ApprovalModel(ActivityApprovalBinding activityApprovalBinding, int careertype , ApprovalActivity approvalActivity) {
        this.activityApprovalBinding = activityApprovalBinding;
        this.careertype = careertype;
        this.approvalActivity = approvalActivity;
        initData();
        initView();
    }

    //点击修改
    public void modify(View view){
        Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
       // intentUserinfoEditorActivity.putExtra("phone",phone);
        //intentUserinfoEditorActivity.putExtra("myId",myId);
        //intentUserinfoEditorActivity.putExtra("uifo",uinfo);
       approvalActivity.startActivity(intentUserinfoEditorActivity);
    }

    private void initData() {

    }

    private void initView() {
        switch (1){
            case 0:
                LogKit.d("没有完善职业类型");
                break;
            case 1://上班的
                activityApprovalBinding.vpApprovalContainer.setVisibility(View.VISIBLE);
                activityApprovalBinding.cdIdImag.setVisibility(View.GONE);
                viewpageAdapter = new ViewpageAdapter(images,names,approvalActivity);
                activityApprovalBinding.vpApprovalContainer.setAdapter(viewpageAdapter);
                //预加载3个页面
                activityApprovalBinding.vpApprovalContainer.setOffscreenPageLimit(3);
                //两个页面的间距
                activityApprovalBinding.vpApprovalContainer.setPageMargin(CommonUtils.dip2px(10));

                activityApprovalBinding.vpApprovalContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        if (position == images.length && positionOffset > 0.99) {
                            //在position5左滑且左滑positionOffset百分比接近1时，偷偷替换为position1（原本会滑到position6）
                            activityApprovalBinding.vpApprovalContainer.setCurrentItem(1, false);
                        } else if (position == 0 && positionOffset < 0.01) {
                            //在position1右滑且右滑百分比接近0时，偷偷替换为position5（原本会滑到position0）
                            activityApprovalBinding.vpApprovalContainer.setCurrentItem(5, false);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position==5){
                            activityApprovalBinding.tvApprovalTitle.setText(titles[0]);
                        }else {
                            activityApprovalBinding.tvApprovalTitle.setText(titles[1]);
                        }

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
                            case 5:
                            cardType = Cardtype.USER_REAL_AUTH_CARD_IDCARD;
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                break;
            case 2://自顾者,只要认证身份证
                activityApprovalBinding.vpApprovalContainer.setVisibility(View.GONE);
                activityApprovalBinding.cdIdImag.setVisibility(View.VISIBLE);
                break;
            case -1:
                LogKit.d("没有获取数据");
                break;
        }

    }

    //拍照
    public void takePhoto(View view){
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        approvalActivity.startActivityForResult(intent, Constants.USERINFO_TAKEPHOTO);
    }

    //相册
    public void  photoAlbum(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");//相片类型
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 100);
        approvalActivity.startActivityForResult(intent, Constants.USERINFO_PHOTOALBUM);
    }



}
