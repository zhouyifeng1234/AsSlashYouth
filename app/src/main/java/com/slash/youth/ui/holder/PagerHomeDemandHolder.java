package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.databinding.ItemListviewHomeDemandBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeDemandHolder extends BaseHolder<SearchItemDemandBean.DataBean.ListBean> {

    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    @Override
    public View initView() {
        itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(SearchItemDemandBean.DataBean.ListBean data) {
        long starttime = data.getStarttime();
        String startData = TimeUtils.getData(starttime);
        mItemHomeDemandServiceModel.setDemandOrServiceTime(startData);
        mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);

        String avatar = data.getAvatar();
        if(avatar!=null&&avatar.equals("")){
            BitmapKit.bindImage(itemHomeDemandServiceBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 0:
                itemHomeDemandServiceBinding.ivIsAuth.setVisibility(View.GONE);
                break;
            case 1:
                itemHomeDemandServiceBinding.ivIsAuth.setVisibility(View.VISIBLE);
                break;
        }

        String title = data.getTitle();
        itemHomeDemandServiceBinding.tvDemandServiceTitle.setText(title);

        String name = data.getName();
        itemHomeDemandServiceBinding.tvName.setText(name);

        long quote = data.getQuote();
        String quoteString = FirstPagerManager.QUOTE + quote + "元";
        itemHomeDemandServiceBinding.tvQuote.setText(quoteString);

        int pattern = data.getPattern();
        switch (pattern){
            case 0:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_UP);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_DOWN);
                break;
        }

        int instalment = data.getInstalment();
        switch (instalment){
            case 0:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.GONE);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.VISIBLE);
                break;
        }

        String place = data.getCity();
        itemHomeDemandServiceBinding.tvLocation.setText(place);

        double lat = data.getLat();
        double lng = data.getLng();
        DistanceUtils distanceUtils = new DistanceUtils();
        distanceUtils.getLatAndLng(currentActivity);
        double currentLatitude = distanceUtils.currentLatitude;
        double currentLongitude = distanceUtils.currentLongitude;
        double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
        itemHomeDemandServiceBinding.tvDistance.setText("<"+distance+"KM");

    }

}
