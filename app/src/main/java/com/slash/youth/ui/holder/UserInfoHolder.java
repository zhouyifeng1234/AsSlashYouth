package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.tool.ext.ExtKt;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemUserinfoBinding;
import com.slash.youth.domain.NewDemandAandServiceBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ItemUserInfoModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zss on 2016/11/1.
 */
public class UserInfoHolder extends BaseHolder<NewDemandAandServiceBean.DataBean.ListBean> {
    private ItemUserinfoBinding itemUserinfoBinding;

    @Override
    public View initView() {
        itemUserinfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_userinfo, null, false);
        ItemUserInfoModel itemUserInfoModel = new ItemUserInfoModel(itemUserinfoBinding);
        itemUserinfoBinding.setItemUserInfoModel(itemUserInfoModel);
        return itemUserinfoBinding.getRoot();
    }

    @Override
    public void refreshView(NewDemandAandServiceBean.DataBean.ListBean data) {
        int anonymity = data.getAnonymity();
        long starttime =  data.getStarttime();
        String startData = TimeUtils.getData(starttime);
        long endtime = data.getEndtime();
        String endData = TimeUtils.getData(endtime);

        int instalment = data.getInstalment();

        long quote = data.getQuote();
        int quoteunit = data.getQuoteunit();

        int type = data.getType();
        switch (type){
            case 1://需求
             /*   if(starttime == 0&&endtime == 0){
                    itemUserinfoBinding.tvTime.setText(UserInfoEngine.ANY_TIME);
                }else {
                    itemUserinfoBinding.tvTime.setText(FirstPagerManager.START_TIME+startData);
                }*/

                if(quote>0){
                    String demandQuote = FirstPagerManager.QUOTE + quote +"元";
                    itemUserinfoBinding.tvQuote.setText(demandQuote);
                }else {
                    itemUserinfoBinding.tvQuote.setText(FirstPagerManager.DEMAND_QUOTE);
                }
                break;
            case 2://服务
                if(quoteunit>=1&&quoteunit<=8){
                    String quoteString = FirstPagerManager.QUOTE + quote + "元" + "/" + FirstPagerManager.QUOTEUNITS[quoteunit-1];
                    itemUserinfoBinding.tvQuote.setText(quoteString);

                }else {
                    String quoteString = FirstPagerManager.QUOTE + quote + "元";
                    itemUserinfoBinding.tvQuote.setText(quoteString);
                }

                int timetype = data.getTimetype();
                itemUserinfoBinding.ivTime.setVisibility(View.VISIBLE);
                if(timetype == 0){
                    //itemUserinfoBinding.tvTime.setText(startData+"-"+endData);
                }else {
                    itemUserinfoBinding.tvTime.setText(FirstPagerManager.TIMETYPES[timetype-1]);
                }
                break;
        }

        itemUserinfoBinding.tvInstalment.setVisibility(View.VISIBLE);
        switch (instalment){
            case 0:
                itemUserinfoBinding.tvInstalment.setText(FirstPagerManager.DEMAND_INSTALMENT);
                break;
            case 1:
                itemUserinfoBinding.tvInstalment.setText(FirstPagerManager.SERVICE_INSTALMENT);
                break;
        }

        // int anonymity = data.getAnonymity();
        String name = data.getName();
        String avatar = data.getAvatar();
        //匿名，实名
        switch (anonymity){
            case 1://实名
                BitmapKit.bindImage(itemUserinfoBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                itemUserinfoBinding.tvName.setText(name);
                break;
            case 0://匿名
                itemUserinfoBinding.ivAvater.setImageResource(R.mipmap.anonymity_avater);
                String firstName = name.substring(0, 1);
                String anonymityName = firstName + "xx";
                itemUserinfoBinding.tvName.setText(anonymityName);
                break;
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 0:
                itemUserinfoBinding.ivIsAuth.setVisibility(View.GONE);
                break;
            case 1:
                itemUserinfoBinding.ivIsAuth.setVisibility(View.VISIBLE);
                break;
        }

        String title = data.getTitle();
        itemUserinfoBinding.tvTitle.setText(title);

        int pattern = data.getPattern();
        String place = data.getPlace();
        //用户的经纬度
        double lat = data.getLat();
        double lng = data.getLng();
            switch (pattern){
            case 0:
                itemUserinfoBinding.tvPattern.setVisibility(View.VISIBLE);
                itemUserinfoBinding.tvLocation.setText("不限城市");
                itemUserinfoBinding.tvPattern.setText(FirstPagerManager.PATTERN_UP);
                itemUserinfoBinding.tvDistance.setText("");
                break;
            case 1:
                itemUserinfoBinding.tvPattern.setVisibility(View.VISIBLE);
                itemUserinfoBinding.tvLocation.setText(place);
                itemUserinfoBinding.tvPattern.setText(FirstPagerManager.PATTERN_DOWN);
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                itemUserinfoBinding.tvDistance.setText("距您"+distance+"KM");
                break;
        }
    }
}
