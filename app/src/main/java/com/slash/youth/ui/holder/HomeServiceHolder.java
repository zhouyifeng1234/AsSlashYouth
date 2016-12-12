package com.slash.youth.ui.holder;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeServiceHolder extends BaseHolder<FreeTimeServiceBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private Activity mActivity;

    public HomeServiceHolder(Activity mActivity) {
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
    public void refreshView(FreeTimeServiceBean.DataBean.ListBean data) {
        long starttime = data.getStarttime();
        long endtime = data.getEndtime();
        String startData = TimeUtils.getData(starttime);
        String endData = TimeUtils.getData(endtime);
        mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.FREE_TIME+""+startData+"-"+endData);
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

    /*    long quote = data.getQuote();
        int quoteunit = data.getQuoteunit();
        String quoteString = FirstPagerManager.QUOTE + quote +"元"+"/"+FirstPagerManager.QUOTEUNITS[quoteunit + 1];
        itemHomeDemandServiceBinding.tvQuote.setText(quoteString);
*/
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

        String place = data.getPlace();
        itemHomeDemandServiceBinding.tvLocation.setText(place);

        //目标经纬度
        double lat = data.getLat();
        double lng = data.getLng();
        //用户的经纬度


       // new  DistanceUtils().getLatAndLng(CommonUtils.getApplication());

        // System.out.println("====第三方第三方===="+DistanceUtils.getDistance(22.75424,112.76535 , 23.014171, 113.10111));
        itemHomeDemandServiceBinding.tvDistance.setText("<4.2KM");
    }
}
