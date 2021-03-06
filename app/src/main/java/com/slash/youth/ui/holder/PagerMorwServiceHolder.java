package com.slash.youth.ui.holder;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandLayoutBinding;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ItemDemandModel;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CountUtils;
import com.slash.youth.utils.DistanceUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerMorwServiceHolder extends BaseHolder<FreeTimeMoreServiceBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private Activity mActivity;
    private ItemDemandLayoutBinding itemDemandLayoutBinding;
    private ItemDemandModel itemDemandModel;

    public PagerMorwServiceHolder(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public View initView() {
        itemDemandLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_layout, null, false);
        itemDemandModel = new ItemDemandModel(itemDemandLayoutBinding);
        itemDemandLayoutBinding.setItemDemandModel(itemDemandModel);
        return itemDemandLayoutBinding.getRoot();
    }

    @Override
    public void refreshView(FreeTimeMoreServiceBean.DataBean.ListBean data) {
        if (data.isInsertRadHint) {//显示
            itemDemandModel.setRadhintVisibility(View.VISIBLE);
        } else {//隐藏
            itemDemandModel.setRadhintVisibility(View.GONE);
        }

        int anonymity = data.getAnonymity();
        String name = data.getName();
        String avatar = data.getAvatar();
        //匿名，实名
        switch (anonymity) {
            case 1://实名
                BitmapKit.bindImage(itemDemandLayoutBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                itemDemandModel.setName(name);
                break;
            case 0://匿名
                itemDemandLayoutBinding.ivAvater.setImageResource(R.mipmap.anonymity_avater);
                if (!TextUtils.isEmpty(name)) {
                    String firstName = name.substring(0, 1);
                    String anonymityName = firstName + "xx";
                    itemDemandModel.setName(anonymityName);
                }
                break;
        }

        int isauth = data.getIsauth();
        switch (isauth) {
            case 0:
                itemDemandModel.setIsAuthVisivity(View.GONE);
                break;
            case 1:
                itemDemandModel.setIsAuthVisivity(View.VISIBLE);
                break;
        }

        String title = data.getTitle();
        itemDemandModel.setTitle(title);

        double quote = data.getQuote();
        if (quote > 0) {
            String quotes = CountUtils.DecimalFormat(quote);
            String quoteString = FirstPagerManager.QUOTE + quotes + "元";
            itemDemandModel.setQuote(quoteString);
        } else if (quote == 0) {
            itemDemandModel.setQuote(FirstPagerManager.QUOTE + "0" + "元");
        }


        int pattern = data.getPattern();
        String place = data.getPlace();
        double lat = data.getLat();
        double lng = data.getLng();
        switch (pattern) {
            case 0:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_UP);
                itemDemandModel.setPlace("不限城市");
                itemDemandModel.setDistance("");
                break;
            case 1:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_DOWN);
                if (!TextUtils.isEmpty(place)) {
                    itemDemandModel.setPlace(place);
                }
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                itemDemandModel.setDistance("距您" + distance + "km");
                break;
        }

        int instalment = data.getInstalment();
        itemDemandModel.setInstalment(FirstPagerManager.DEMAND_INSTALMENT);
        switch (instalment) {
            case 0:
                itemDemandModel.setInstalmentVisibility(View.VISIBLE);
                break;
            case 1:
                itemDemandModel.setInstalmentVisibility(View.GONE);
                break;
        }

        int timetype = data.getTimetype();
        itemDemandModel.setTimeVisibility(View.VISIBLE);
        if (timetype != 0) {
            itemDemandModel.setFreeTime(FirstPagerManager.TIMETYPES[timetype - 1]);
        }

    }
}
