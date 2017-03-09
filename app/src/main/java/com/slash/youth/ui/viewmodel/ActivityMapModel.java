package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.adapter.MapNearLocationAdapter;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class ActivityMapModel extends BaseObservable {
    ActivityMapBinding mActivityMapBinding;
    MapActivity mActivity;
    private int llMapInfoVisible = View.VISIBLE;
    private int svSearchListVisible = View.INVISIBLE;

    SQLiteDatabase mapReadableDB;
    SQLiteDatabase mapWritableDB;

    public ActivityMapModel(ActivityMapBinding activityMapBinding, MapActivity activity, SQLiteDatabase mapReadableDB, SQLiteDatabase mapWritableDB) {
        this.mActivityMapBinding = activityMapBinding;
        this.mActivity = activity;
        this.mapReadableDB = mapReadableDB;
        this.mapWritableDB = mapWritableDB;
        initView();
    }

    private void initView() {
//        initNearLocationData();
    }

    public void initNearLocationData(final ArrayList<NearLocationBean> listNearLocation) {
        //TODO 附近地点数据可由地图API获取，第一个为我的当前位置
//        ArrayList<NearLocationBean> listNearLocation = new ArrayList<NearLocationBean>();
//        listNearLocation.add(new NearLocationBean("我的位置", "苏州工业园区", "0.50KM"));//第一个为我的当前位置
//        listNearLocation.add(new NearLocationBean("银行", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("酒店", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("KTV", "苏州工业园区", "0.50KM"));
//        listNearLocation.add(new NearLocationBean("饭店", "苏州工业园区", "0.50KM"));
        mActivityMapBinding.lvActivityMapNearLocation.setAdapter(new MapNearLocationAdapter(listNearLocation));
    }

    @Bindable
    public int getSvSearchListVisible() {
        return svSearchListVisible;
    }

    public void setSvSearchListVisible(int svSearchListVisible) {
        this.svSearchListVisible = svSearchListVisible;
        notifyPropertyChanged(BR.svSearchListVisible);
    }

    @Bindable
    public int getLlMapInfoVisible() {
        return llMapInfoVisible;
    }

    public void setLlMapInfoVisible(int llMapInfoVisible) {
        this.llMapInfoVisible = llMapInfoVisible;
        notifyPropertyChanged(BR.llMapInfoVisible);
    }

    public void okChooseLocation(View v) {
        //异常闪退调试
//        int a = 5 / 0;
//        LogKit.v(a + "");

        if (mActivity.mCurrentAddress != null && mActivity.mCurrentAoiName != null && mActivity.mCurrentLatlng != null) {
            if (TextUtils.isEmpty(mActivity.mCurrentAddress) && TextUtils.isEmpty(mActivity.mCurrentAoiName)) {
                ToastUtils.shortToast("没有获取到当前位置的地理信息");
            } else {
                Intent intentResult = new Intent();
                intentResult.putExtra("mCurrentAddress", mActivity.mCurrentAddress);
                intentResult.putExtra("mCurrentAoiName", mActivity.mCurrentAoiName);
                intentResult.putExtra("lng", mActivity.mCurrentLatlng.longitude);//线下服务地点地图经度
                intentResult.putExtra("lat", mActivity.mCurrentLatlng.latitude);//线下服务地点地图纬度
                mActivity.setResult(20, intentResult);
                mActivity.finish();
            }
        } else {
            ToastUtils.shortToast("未获取到定位信息，可能没有开启定位权限，请到手机设置中心开启");
        }
    }

    public void goBack(View v) {
        if (svSearchListVisible == View.VISIBLE) {
            setLlMapInfoVisible(View.VISIBLE);
            setSvSearchListVisible(View.INVISIBLE);
        } else {
            mActivity.finish();
        }
    }

    /**
     * 取消清除历史搜索记录
     *
     * @param v
     */
    public void cancelDelAll(View v) {
        setDelSearchHisLayerVisibility(View.GONE);
    }

    /**
     * 确定清除历史搜索记录
     *
     * @param v
     */
    public void okDelAll(View v) {
        mapWritableDB.execSQL("delete from map_search_his");
        setLlMapInfoVisible(View.VISIBLE);
        setSvSearchListVisible(View.INVISIBLE);
        setDelSearchHisLayerVisibility(View.GONE);
    }

    public int delSearchHisLayerVisibility = View.GONE;

    @Bindable
    public int getDelSearchHisLayerVisibility() {
        return delSearchHisLayerVisibility;
    }

    public void setDelSearchHisLayerVisibility(int delSearchHisLayerVisibility) {
        this.delSearchHisLayerVisibility = delSearchHisLayerVisibility;
        notifyPropertyChanged(BR.delSearchHisLayerVisibility);
    }
}
