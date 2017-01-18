package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandLayoutBinding;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.viewmodel.ItemDemandModel;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMoreDemandHolder extends BaseHolder<FreeTimeMoreDemandBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private FirstPagerMoreActivity firstPagerMoreActivity;
    private ItemDemandLayoutBinding itemDemandLayoutBinding;
    private ItemDemandModel itemDemandModel;

    public PagerMoreDemandHolder(FirstPagerMoreActivity firstPagerMoreActivity) {
        this.firstPagerMoreActivity = firstPagerMoreActivity;
    }

    @Override
    public View initView() {

        itemDemandLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_layout, null, false);
        itemDemandModel = new ItemDemandModel(itemDemandLayoutBinding);
        itemDemandLayoutBinding.setItemDemandModel(itemDemandModel);
        return itemDemandLayoutBinding.getRoot();


       /* itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();*/
    }

    @Override
    public void refreshView(FreeTimeMoreDemandBean.DataBean.ListBean data) {
        int anonymity = data.getAnonymity();
        String name = data.getName();
        String avatar = data.getAvatar();
        //匿名，实名
        switch (anonymity){
            case 1://实名
                BitmapKit.bindImage(itemDemandLayoutBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                itemDemandModel.setName(name);
                break;
            case 0://匿名
                itemDemandLayoutBinding.ivAvater.setImageResource(R.mipmap.anonymity_avater);
                String firstName = name.substring(0, 1);
                String anonymityName = firstName + "xx";
                itemDemandModel.setName(anonymityName );
                break;
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 0:
                itemDemandModel.setIsAuthVisivity(View.GONE);
                break;
            case 1:
                itemDemandModel.setIsAuthVisivity(View.VISIBLE);
                break;
        }

        String title = data.getTitle();
        itemDemandModel.setTitle(title);

        long quote = data.getQuote();
        if(quote>0){
            String quoteString = FirstPagerManager.QUOTE + quote + "元";
            itemDemandModel.setQuote(quoteString);
        }else {
            itemDemandModel.setQuote(FirstPagerManager.DEMAND_QUOTE);
        }

        int pattern = data.getPattern();
        String place = data.getPlace();
        double lat = data.getLat();
        double lng = data.getLng();
        switch (pattern){
            case 0:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_UP);
                itemDemandModel.setPlace("全国");
                break;
            case 1:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_DOWN);

                if(!TextUtils.isEmpty(place)){
                    itemDemandModel.setPlace(place);
                }
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                itemDemandModel.setDistance("距离"+distance+"KM");
                break;
        }

        int instalment = data.getInstalment();
        itemDemandModel.setInstalment(FirstPagerManager.DEMAND_INSTALMENT);
        switch (instalment){
            case 0:
                itemDemandModel.setInstalmentVisibility(View.VISIBLE);
                break;
            case 1:
                itemDemandModel.setInstalmentVisibility(View.GONE);
                break;
        }








     /*   int anonymity = data.getAnonymity();
        String name = data.getName();
        String avatar = data.getAvatar();
        //匿名，实名
        switch (anonymity){
            case 1://实名
                if(!TextUtils.isEmpty(avatar)){
                    BitmapKit.bindImage(itemHomeDemandServiceBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                }
                itemHomeDemandServiceBinding.tvName.setText(name);
                break;
            case 0://匿名
                itemHomeDemandServiceBinding.ivAvater.setImageResource(R.mipmap.anonymity_avater);
                String firstName = name.substring(0, 1);
                String anonymityName = firstName + "xx";
                itemHomeDemandServiceBinding.tvName.setText(anonymityName);
                break;
        }

        long starttime = data.getStarttime();
        if(starttime == 0){
            mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.ANY_TIME);
        }else {
            String startData = TimeUtils.getData(starttime);
            mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.START_TIME+startData);
            mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);
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

        long quote = data.getQuote();
        if(quote == 0){
            itemHomeDemandServiceBinding.tvQuote.setText(FirstPagerManager.DEMAND_QUOTE);
        }else {
            String quoteString = FirstPagerManager.QUOTE + quote + "元";
            itemHomeDemandServiceBinding.tvQuote.setText(quoteString);
        }

        int pattern = data.getPattern();
        String city = data.getCity();
        double lat = data.getLat();
        double lng = data.getLng();
        switch (pattern){
            case 0:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_UP);
              if(!TextUtils.isEmpty(city)){
                  itemHomeDemandServiceBinding.tvLocation.setText(city);
              }
                break;
            case 1:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_DOWN);
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                itemHomeDemandServiceBinding.tvDistance.setText("距离"+distance+"KM");
                break;
        }

        int instalment = data.getInstalment();
        itemHomeDemandServiceBinding.tvInstalment.setText(FirstPagerManager.DEMAND_INSTALMENT);
        switch (instalment){
            case 0:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.VISIBLE);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.GONE);
                break;
        }*/
    }
}
