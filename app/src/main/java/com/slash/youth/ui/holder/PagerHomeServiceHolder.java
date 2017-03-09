package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandLayoutBinding;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.viewmodel.ItemDemandModel;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeServiceHolder extends BaseHolder<SearchServiceItemBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;
    private ItemHomeDemandServiceBinding itemHomeDemandServiceBinding;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ItemDemandLayoutBinding itemDemandLayoutBinding;
    private ItemDemandModel itemDemandModel;

    @Override
    public View initView() {
        itemDemandLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_demand_layout, null, false);
        itemDemandModel = new ItemDemandModel(itemDemandLayoutBinding);
        itemDemandLayoutBinding.setItemDemandModel(itemDemandModel);
        return itemDemandLayoutBinding.getRoot();
    }

    @Override
    public void refreshView(SearchServiceItemBean.DataBean.ListBean data) {
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
        String quoteString = FirstPagerManager.QUOTE + quote + "元";
        itemDemandModel.setQuote(quoteString);

        int pattern = data.getPattern();
        String place = data.getPlace();
        double lat = data.getLat();
        double lng = data.getLng();
        switch (pattern){
            case 0:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_UP);
                itemDemandModel.setPlace("不限城市");
                itemDemandModel.setDistance("");
                break;
            case 1:
                itemDemandModel.setPattern(FirstPagerManager.PATTERN_DOWN);

                if(!TextUtils.isEmpty(place)){
                    itemDemandModel.setPlace(place);
                }
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                itemDemandModel.setDistance("距您"+distance+"KM");
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

        int timetype = data.getTimetype();
        itemDemandModel.setTimeVisibility(View.VISIBLE);
        if(timetype!=0){
            itemDemandModel.setFreeTime(FirstPagerManager.TIMETYPES[timetype-1]);
        }
    }
}
