package com.slash.youth.ui.holder;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMorwServiceHolder extends BaseHolder<FreeTimeMoreServiceBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private Activity mActivity;

    public PagerMorwServiceHolder(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public View initView() {
        itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(FreeTimeMoreServiceBean.DataBean.ListBean data) {
        int anonymity = data.getAnonymity();
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

        int timetype = data.getTimetype();
        itemHomeDemandServiceBinding.ivTime.setVisibility(View.VISIBLE);
        if(timetype == 0){
            long starttime = data.getStarttime();
            long endtime = data.getEndtime();
            String startData = TimeUtils.getData(starttime);
            String endData = TimeUtils.getData(endtime);
            mItemHomeDemandServiceModel.setDemandOrServiceTime(startData+"-"+endData);
        }else {
            mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.TIMETYPES[timetype]);
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
        int quoteunit = data.getQuoteunit();
        if(quoteunit>=1&&quoteunit<=8){
            String quoteString = FirstPagerManager.QUOTE + quote +"元"+"/"+FirstPagerManager.QUOTEUNITS[quoteunit-1];
            itemHomeDemandServiceBinding.tvQuote.setText(quoteString);
        }else {
            String quoteString = FirstPagerManager.QUOTE + quote +"元";
            itemHomeDemandServiceBinding.tvQuote.setText(quoteString);
        }


        int pattern = data.getPattern();
        String city = data.getPlace();

        //目标经纬度
        double lat = data.getLat();
        double lng = data.getLng();
        switch (pattern){
            case 1:
                itemHomeDemandServiceBinding.tvLocation.setText(city);
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_DOWN);
                break;
            case 0:
                itemHomeDemandServiceBinding.tvLocation.setText("全国");
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_UP);
                break;
        }

        if(pattern == 1){
            //用户的经纬度
            double currentLatitude = SlashApplication.getCurrentLatitude();
            double currentLongitude = SlashApplication.getCurrentLongitude();
            double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
            itemHomeDemandServiceBinding.tvDistance.setText("距离"+distance+"KM");
        }

        int instalment = data.getInstalment();
        itemHomeDemandServiceBinding.tvInstalment.setText(FirstPagerManager.SERVICE_INSTALMENT);
        switch (instalment){
            case 0:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.GONE);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.VISIBLE);
                break;
        }
    }
}
