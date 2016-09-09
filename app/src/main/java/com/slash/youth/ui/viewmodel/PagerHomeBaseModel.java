package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogHomeSubscribeBinding;
import com.slash.youth.databinding.PagerHomeBaseBinding;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeBaseModel extends BaseObservable {

    private PagerHomeBaseBinding mPagerHomeBaseBinding;
    private Drawable mDrawableBottom;

    public PagerHomeBaseModel(PagerHomeBaseBinding pagerHomeBaseBinding) {
        this.mPagerHomeBaseBinding = pagerHomeBaseBinding;
        mDrawableBottom = CommonUtils.getContext().getResources().getDrawable(R.mipmap.tab_list_bg);
    }

    public void browseDemandClick(View v) {
        LogKit.v("browseDemandFocused");
//        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserDemand.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mDrawableBottom);
//        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserService.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        browseDemandFocused();

    }


    public void browseServiceClick(View v) {
        LogKit.v("browseServiceFocused");
//        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserDemand.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserService.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mDrawableBottom);
        browseServiceFocused();

    }

    public void browseDemandFocused() {
        mPagerHomeBaseBinding.ivPagerHomeBaseTabDemandBg.setVisibility(View.VISIBLE);
        mPagerHomeBaseBinding.ivPagerHomeBaseTabServiceBg.setVisibility(View.INVISIBLE);

        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserDemand.setTextColor(0xff31c5e4);
        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserService.setTextColor(0xff333333);
    }

    public void browseServiceFocused() {
        mPagerHomeBaseBinding.ivPagerHomeBaseTabDemandBg.setVisibility(View.INVISIBLE);
        mPagerHomeBaseBinding.ivPagerHomeBaseTabServiceBg.setVisibility(View.VISIBLE);

        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserDemand.setTextColor(0xff333333);
        mPagerHomeBaseBinding.tvPagerHomeBaseBrowserService.setTextColor(0xff31c5e4);
    }

    public void openCityLocation(View v) {
        LogKit.v("openCityLocation");
        Intent intentCityLocationActivity = new Intent(CommonUtils.getContext(), CityLocationActivity.class);
        intentCityLocationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentCityLocationActivity);
    }

    public void openSubscribe(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommonUtils.getCurrentActivity());
        DialogHomeSubscribeBinding dialogHomeSubscribeBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_home_subscribe, null, false);
        DialogHomeSubscribeModel dialogHomeSubscribeModel = new DialogHomeSubscribeModel(dialogHomeSubscribeBinding);
        dialogHomeSubscribeBinding.setDialogHomeSubscribeModel(dialogHomeSubscribeModel);
        dialogBuilder.setView(dialogHomeSubscribeBinding.getRoot());
        AlertDialog dialogSubscribe = dialogBuilder.create();
        dialogSubscribe.show();
        dialogHomeSubscribeModel.currentDialog = dialogSubscribe;
        dialogSubscribe.setCanceledOnTouchOutside(false);////设置点击Dialog外部任意区域关闭Dialog
        Window dialogSubscribeWindow = dialogSubscribe.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(300);
        dialogParams.height = CommonUtils.dip2px(300 + 70);
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);
//        dialogBuilder.show();


//        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
//        intentSubscribeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentSubscribeActivity);
    }


    public void openFilter(View v) {

    }


}
