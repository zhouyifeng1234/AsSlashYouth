package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityApprovalBinding;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.adapter.ViewpageAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/5.
 */
public class ApprovalModel extends BaseObservable {
    private ActivityApprovalBinding activityApprovalBinding;
    private int[] images ={R.mipmap.workcard_image,R.mipmap.onjob_proof_image,R.mipmap.mailbox_image,
            R.mipmap.business_card,R.mipmap.id_image};
    private String[] names ={"工牌","在职证明等其他证件","邮箱后台截图","名片","身份证"};
    private ViewpageAdapter viewpageAdapter;

    private String[] titles ={"请上传身份证正面","工牌、名片、邮箱后台截图、其他在职资料任选其一"};

    public ApprovalModel(ActivityApprovalBinding activityApprovalBinding) {
        this.activityApprovalBinding = activityApprovalBinding;
        initData();
        initView();
    }

    //点击修改
    public void modify(View view){
        LogKit.d("修改");

    }

    private void initData() {

    }

    private void initView() {


    viewpageAdapter = new ViewpageAdapter(images,names);
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });
        //两边出现边界
        activityApprovalBinding.vpApprovalContainer.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position < -1) { // [-Infinity,-1)
                   // page.setScaleX(2);
                   // page.setScaleY(2);
                   // page.setTranslationX(2);
                } else if (position <= 0) { // [-1,0]
                   // page.setScaleX((float) 1 + position / (float) 9);
                  //  page.setScaleY((float) 1 + position / (float) 9);
                 //   page.setTranslationX((0 - position) * translationX);
                } else if (position <= 1) { // (0,1]
                   // page.setScaleX((float) 1 - position / (float) 9);
                  //  page.setScaleY((float) 1 - position / (float) 9);
                  //  page.setTranslationX((0 - position) * translationX);
                } else { // (1,+Infinity]
                    //page.setScaleX(2);
                   //page.setScaleY(2);
                   //page.setTranslationX(-2);
                }
            }

        });

    }


}
