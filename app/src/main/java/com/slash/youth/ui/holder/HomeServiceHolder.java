package com.slash.youth.ui.holder;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;

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
       /* long starttime = data.getStarttime();
        long endtime = data.getEndtime();
        String startData = TimeUtils.getData(starttime);
        String endData = TimeUtils.getData(endtime);*/
        // mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.START_TIME + "" + startData);

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

        int timetype = data.getTimetype();
        itemHomeDemandServiceBinding.ivTime.setVisibility(View.VISIBLE);
        mItemHomeDemandServiceModel.setDemandOrServiceTime(FirstPagerManager.TIMETYPES[timetype]);
        mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);

        int isauth = data.getIsauth();
        switch (isauth) {
            case 0:
                itemHomeDemandServiceBinding.ivIsAuth.setVisibility(View.GONE);
                break;
            case 1:
                itemHomeDemandServiceBinding.ivIsAuth.setVisibility(View.VISIBLE);
                break;
        }

        String title = data.getTitle();
        if(!TextUtils.isEmpty(title)){
            itemHomeDemandServiceBinding.tvDemandServiceTitle.setText(title);
        }

        long quote = data.getQuote();
        int quoteunit = data.getQuoteunit();
        if (quoteunit >= 1 && quoteunit <= 9) {
            if(quote == 0){
                itemHomeDemandServiceBinding.tvQuote.setText("服务方报价");
            }else {
                String quoteString = FirstPagerManager.QUOTE + quote + "元" + "/" + FirstPagerManager.QUOTEUNITS[quoteunit - 1];
                itemHomeDemandServiceBinding.tvQuote.setText(quoteString);
            }
        }

        int pattern = data.getPattern();
        switch (pattern) {
            case 0:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_UP);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvPattern.setText(FirstPagerManager.PATTERN_DOWN);
                break;
        }

        int instalment = data.getInstalment();
        itemHomeDemandServiceBinding.tvInstalment.setText("分期支付");
        switch (instalment) {
            case 0:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.GONE);
                break;
            case 1:
                itemHomeDemandServiceBinding.tvInstalment.setVisibility(View.VISIBLE);
                break;
        }

        String place = data.getPlace();
        if(!TextUtils.isEmpty(place)){
            itemHomeDemandServiceBinding.tvLocation.setText(place);
        }

        //目标经纬度
        double lat = data.getLat();
        double lng = data.getLng();
        //用户的经纬度
        double currentLatitude = SlashApplication.getCurrentLatitude();
        double currentLongitude = SlashApplication.getCurrentLongitude();
        double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
        itemHomeDemandServiceBinding.tvDistance.setText("距离" + distance + "KM");
    }
}
