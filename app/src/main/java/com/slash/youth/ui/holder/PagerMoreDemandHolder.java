package com.slash.youth.ui.holder;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.databinding.ItemListviewHomeDemandBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMoreDemandHolder extends BaseHolder<FreeTimeMoreDemandBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private FirstPagerMoreActivity firstPagerMoreActivity;

    public PagerMoreDemandHolder(FirstPagerMoreActivity firstPagerMoreActivity) {
        this.firstPagerMoreActivity = firstPagerMoreActivity;
    }

    @Override
    public View initView() {
        itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(FreeTimeMoreDemandBean.DataBean.ListBean data) {
        int anonymity = data.getAnonymity();
        String name = data.getName();
        String avatar = data.getAvatar();
        //匿名，实名
        switch (anonymity){
            case 1://实名
                if(avatar!=null&&avatar.equals("")){
                    BitmapKit.bindImage(itemHomeDemandServiceBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                }
                itemHomeDemandServiceBinding.tvName.setText(name);
                break;
            case 0://匿名
                itemHomeDemandServiceBinding.ivAvater.setImageResource(R.mipmap.default_avatar_1);
                String firstName = name.substring(0, 1);
                String anonymityName = firstName + "xx";
                itemHomeDemandServiceBinding.tvName.setText(anonymityName);
                break;
        }

        long starttime = data.getStarttime();
        String startData = TimeUtils.getData(starttime);
        mItemHomeDemandServiceModel.setDemandOrServiceTime(startData);
        mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);

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

        String city = data.getCity();
        String location = data.getLocation();
        itemHomeDemandServiceBinding.tvLocation.setText(city);

        double lat = data.getLat();
        double lng = data.getLng();
        double currentLatitude = SlashApplication.getCurrentLatitude();
        double currentLongitude = SlashApplication.getCurrentLongitude();
        double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
        itemHomeDemandServiceBinding.tvDistance.setText("<"+distance+"KM");
    }
}
