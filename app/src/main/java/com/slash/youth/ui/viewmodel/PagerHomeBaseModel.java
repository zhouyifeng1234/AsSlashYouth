package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.DialogHomeSubscribeBinding;
import com.slash.youth.databinding.PagerHomeBaseBinding;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeBaseModel extends BaseObservable {

    private PagerHomeBaseBinding mPagerHomeBaseBinding;
    private Drawable mDrawableBottom;
    boolean[] checkFilterFeatureArray = new boolean[4];
    private int filterViewVisible = View.INVISIBLE;
//    private int filterContentMarginTop;

    public PagerHomeBaseModel(PagerHomeBaseBinding pagerHomeBaseBinding) {
        this.mPagerHomeBaseBinding = pagerHomeBaseBinding;
        mDrawableBottom = CommonUtils.getContext().getResources().getDrawable(R.mipmap.tab_list_bg);
        checkFilterFeatureArray[1] = true;
    }

    public void initView() {
        initFilterSkilllabel();
        mPagerHomeBaseBinding.rlPagerHomeBaseAdheader.post(new Runnable() {
            @Override
            public void run() {
                int adHeaderHeight = mPagerHomeBaseBinding.rlPagerHomeBaseAdheader.getMeasuredHeight();
//                ToastUtils.shortToast(adHeaderHeight + "");
                mPagerHomeBaseBinding.llPagerHomeBaseHomecontent.setMoveUpDistance(adHeaderHeight);
//                int firstVisiblePosition = mPagerHomeBaseBinding.lvPagerHomeBaseContent.getFirstVisiblePosition();
//                ToastUtils.shortToast(firstVisiblePosition + "");
//                mPagerHomeBaseBinding.llPagerHomeBaseHomecontent.setInnerListView(mPagerHomeBaseBinding.lvPagerHomeBaseContent);
            }
        });
    }

//    @Bindable
//    public int getFilterContentMarginTop() {
//        return filterContentMarginTop;
//    }

//    public void setFilterContentMarginTop(int filterContentMarginTop) {
//        this.filterContentMarginTop = filterContentMarginTop;
//        notifyPropertyChanged(BR.filterContentMarginTop);
//    }

    @Bindable
    public int getFilterViewVisible() {
        return filterViewVisible;
    }

    public void setFilterViewVisible(int filterViewVisible) {
        this.filterViewVisible = filterViewVisible;
        notifyPropertyChanged(BR.filterViewVisible);
    }

    public void initFilterSkilllabel() {
        //首先需要从服务端获取过滤标签，暂时写死，以方便测试
        ArrayList<String> listLabelNames = new ArrayList<String>();
        listLabelNames.add("UI设计");
        listLabelNames.add("程序开发");
        listLabelNames.add("UI设计");
        listLabelNames.add("前端代码");
        listLabelNames.add("UI设计");
        listLabelNames.add("程序开发");
        listLabelNames.add("UI设计");
        listLabelNames.add("前端代码");
        listLabelNames.add("UI设计");
        listLabelNames.add("程序开发");
        listLabelNames.add("UI设计");
        listLabelNames.add("前端代码");

        TextView tvFilterSkilllabel = null;
        for (int i = 0; i < listLabelNames.size(); i++) {
            LogKit.v("filter_label " + i);
            String labelName = listLabelNames.get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            tvFilterSkilllabel = new TextView(CommonUtils.getContext());
            tvFilterSkilllabel.setText(labelName);
            tvFilterSkilllabel.setPadding(CommonUtils.dip2px(8), CommonUtils.dip2px(5), CommonUtils.dip2px(8), CommonUtils.dip2px(5));
            tvFilterSkilllabel.setBackgroundResource(R.drawable.selector_home_filter_skilllabell_bg);
            tvFilterSkilllabel.setTextColor(CommonUtils.getColorStateList(R.color.selector_home_filter_skilllabell_textcolor));
            tvFilterSkilllabel.setClickable(true);
            tvFilterSkilllabel.setEnabled(true);
            tvFilterSkilllabel.setFocusable(true);
            tvFilterSkilllabel.setFocusableInTouchMode(true);
            tvFilterSkilllabel.setTextSize(11.5f);
            if (i > 0) {
                params.leftMargin = CommonUtils.dip2px(32);
            }
            tvFilterSkilllabel.setLayoutParams(params);
            mPagerHomeBaseBinding.llPagerHomeBaseFilterSkilllabel.addView(tvFilterSkilllabel);
        }
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
        Intent intentCityLocationActivity = new Intent(CommonUtils.getContext(), CityLocationActivity.class);
        intentCityLocationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentCityLocationActivity);
    }

    public void openSubscribe(View v) {

        setFilterViewVisible(View.INVISIBLE);

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
        if (getFilterViewVisible() == View.VISIBLE) {
            return;
        }
        setFilterViewVisible(View.VISIBLE);
        int filterContentHeight = mPagerHomeBaseBinding.llPagerHomeBaseFilterContent.getMeasuredHeight();
//        setFilterContentMarginTop(-filterContentHeight);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPagerHomeBaseBinding.llPagerHomeBaseFilterContent.getLayoutParams();
//        params.topMargin = -filterContentHeight;
//        mPagerHomeBaseBinding.llPagerHomeBaseFilterContent.setLayoutParams(params);


        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -filterContentHeight, 0);
        translateAnimation.setInterpolator(new BounceInterpolator());
        translateAnimation.setDuration(1000);
        mPagerHomeBaseBinding.llPagerHomeBaseFilterContent.startAnimation(translateAnimation);
        mPagerHomeBaseBinding.ivbtnPagerHomeBaseOkFilter.startAnimation(translateAnimation);
    }

    public void okFilter(View v) {
        setFilterViewVisible(View.INVISIBLE);
    }

    public void nextFilterSkilllabel(View v) {
//        mPagerHomeBaseBinding.hsvPagerHomeBaseFilterSkilllabel.smoothScrollTo(100, 0);
        mPagerHomeBaseBinding.hsvPagerHomeBaseFilterSkilllabel.smoothScrollBy(CommonUtils.dip2px(80), 0);
    }


    public void checkFilterFeature(View v) {
//        ToastUtils.shortToast("Check Filter Feature");
        switch (v.getId()) {
            case R.id.iv_pager_home_base_check_orderbytime_desc:
                saveFilterFeatureState(0, (ImageView) v);
                break;
            case R.id.iv_pager_home_base_check_online:
                saveFilterFeatureState(1, (ImageView) v);
                break;
            case R.id.iv_pager_home_base_check_offline:
                saveFilterFeatureState(2, (ImageView) v);
                break;
            case R.id.iv_pager_home_base_check_only_authuser:
                saveFilterFeatureState(3, (ImageView) v);
                break;

        }
    }

    public void saveFilterFeatureState(int index, ImageView iv) {
        boolean state = checkFilterFeatureArray[index];
        if (state) {
            iv.setImageResource(R.mipmap.dianxuan_);
        } else {
            iv.setImageResource(R.mipmap.xuanzhong_icon);
        }
        checkFilterFeatureArray[index] = !state;
    }

    public void gotoSearchActivity(View v) {
        Intent intentSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        intentSearchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSearchActivity);
    }

}
