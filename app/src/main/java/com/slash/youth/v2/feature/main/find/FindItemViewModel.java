package com.slash.youth.v2.feature.main.find;

import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;

import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.slash.youth.R;
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.domain.bean.FindDemand;
import com.slash.youth.domain.bean.FindServices;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DistanceUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import static android.R.attr.data;

/**
 * Created by acer on 2017/3/7.
 */

public class FindItemViewModel extends BViewModel {

    public FindDemand.ListBean demandBean;
    public FindServices.ListBean serviceBean;

    public String uri;

    public String name;

    public String title;

    public String quote;

    public final ObservableField<Integer> timeVisibility = new ObservableField<>(View.VISIBLE);
    public final ObservableField<Integer> instalmentVisibility = new ObservableField<>(View.VISIBLE);

    public String freeTime;
    public String pattern;
    public String instalment;
    public String place;
    public String distance;

    public final ReplyCommand click = new ReplyCommand(() -> {
        if (demandBean != null) {
            Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
            intentDemandDetailActivity.putExtra("demandId", demandBean.getId());
            activity.startActivity(intentDemandDetailActivity);
        } else {
            //服务
            Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
            intentServiceDetailActivity.putExtra("serviceId", serviceBean.getId());
            activity.startActivity(intentServiceDetailActivity);
        }
    });

    public
    @DrawableRes
    int defaulSrc;

    public FindItemViewModel(RxAppCompatActivity activity, FindDemand.ListBean listBean) {
        super(activity);
        this.demandBean = listBean;
        int anonymity = listBean.getAnonymity();
        String name = listBean.getName();
        String avatar = listBean.getAvatar();
        //匿名，实名
        switch (anonymity) {
            case 1://实名
                uri = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + listBean.getAvatar();
                this.name = name;
                break;
            case 0://匿名
                defaulSrc = R.mipmap.anonymity_avater;
                if (!TextUtils.isEmpty(name)) {
                    String firstName = name.substring(0, 1);
                    String anonymityName = firstName + "xx";
                    this.name = anonymityName;
                }
                break;
        }
        title = listBean.getTitle();
        double quote = listBean.getQuote();
        if (quote > 0) {
            this.quote = FirstPagerManager.QUOTE + quote + "元";
        } else {
            this.quote = FirstPagerManager.DEMAND_QUOTE;
        }

        int instalment = listBean.getInstalment();
        switch (instalment) {
            case 0:
                instalmentVisibility.set(View.VISIBLE);
                break;
            case 1:
                instalmentVisibility.set(View.GONE);
                break;
        }

        int timetype = listBean.getTimetype();
        if (timetype != 0) {
            freeTime = FirstPagerManager.TIMETYPES[timetype - 1];
        }

        int pattern = listBean.getPattern();
        this.place = listBean.getPlace();
        double lat = listBean.getLat();
        double lng = listBean.getLng();
        switch (pattern) {
            case 0:
                this.pattern = FirstPagerManager.PATTERN_UP;
                this.place = "不限城市";
                break;
            case 1:
                this.pattern = FirstPagerManager.PATTERN_DOWN;
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                this.distance = "距您" + distance + "km";
                break;
        }
    }

    public FindItemViewModel(RxAppCompatActivity activity, FindServices.ListBean listBean) {
        super(activity);
        this.serviceBean = listBean;

        int anonymity = listBean.getAnonymity();
        String name = listBean.getName();
        String avatar = listBean.getAvatar();
        //匿名，实名
        switch (anonymity) {
            case 1://实名
                uri = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + listBean.getAvatar();
                this.name = name;
                break;
            case 0://匿名
                defaulSrc = R.mipmap.anonymity_avater;
                if (!TextUtils.isEmpty(name)) {
                    String firstName = name.substring(0, 1);
                    String anonymityName = firstName + "xx";
                    this.name = anonymityName;
                }
                break;
        }
        title = listBean.getTitle();
        double quote = listBean.getQuote();
        if (quote > 0) {
            this.quote = FirstPagerManager.QUOTE + quote + "元";
        } else {
            this.quote = FirstPagerManager.DEMAND_QUOTE;
        }

        int instalment = listBean.getInstalment();
        switch (instalment) {
            case 0:
                instalmentVisibility.set(View.VISIBLE);
                break;
            case 1:
                instalmentVisibility.set(View.GONE);
                break;
        }

        int timetype = listBean.getTimetype();
        if (timetype != 0) {
            freeTime = FirstPagerManager.TIMETYPES[timetype - 1];
        }

        int pattern = listBean.getPattern();
        this.place = listBean.getPlace();
        double lat = listBean.getLat();
        double lng = listBean.getLng();
        switch (pattern) {
            case 0:
                this.pattern = FirstPagerManager.PATTERN_UP;
                this.place = "不限城市";
                break;
            case 1:
                this.pattern = FirstPagerManager.PATTERN_DOWN;
                double currentLatitude = SlashApplication.getCurrentLatitude();
                double currentLongitude = SlashApplication.getCurrentLongitude();
                double distance = DistanceUtils.getDistance(lat, lng, currentLatitude, currentLongitude);
                this.distance = "距您" + distance + "km";
                break;
        }
    }
}
