package com.slash.youth.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.global.SlashApplication;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/8.
 */
public class DistanceUtils {
    private static final double EARTH_RADIUS = 6378.137;//地球半径,单位千米
    public double currentLatitude;
    public double currentLongitude;
    public LatLng mCurrentLatlng;
    private AMap mMap;
    private boolean isMoveByGestures = false;
    private float mapZoom = 14;
    public String mCurrentAddress;
    public String mCurrentAoiName;
    private String mCurrentCityCode;
    private ArrayList<NearLocationBean> mListNearLocation;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    //GPS获取当前我自己的经纬度
    public void getLatAndLng(Context context) {

        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //传入true，启动定位，AmapLocationClient会驱动设备扫描周边wifi，获取最新的wifi列表（相比设备被动刷新会多消耗一些电量）
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setInterval(1000 * 60 * 10);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new SlashLocationListener());
        //启动定位
        mLocationClient.startLocation();
    }

    public class SlashLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
//                        mLocationOption.setInterval(60000);
//                        mLocationClient.setLocationOption(mLocationOption);
                    //获取纬度
                    currentLatitude = aMapLocation.getLatitude();
                    SlashApplication.setCurrentLatitude(currentLatitude);
                    //获取经度
                    currentLongitude = aMapLocation.getLongitude();
                    SlashApplication.setCurrentLongitude(currentLongitude);

                    mCurrentAddress = aMapLocation.getAddress();
                    mCurrentAoiName = aMapLocation.getAoiName();
                    mCurrentCityCode = aMapLocation.getCityCode();
                    String city = aMapLocation.getCity();
                    String province = aMapLocation.getProvince();
                    SpUtils.setString("currentyCity", city);
                    SpUtils.setString("currentyProvince",province);
                    // String country = aMapLocation.getCountry();
                    LogKit.v("currentLatitude:" + currentLatitude + " currentLongitude:" + currentLongitude + "city = " + city);
                   /* LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    mCurrentLatlng = latLng;
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.30400177, 120.64404488), 10));
                    isMoveByGestures = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoom));
                    mapZoom = mMap.getCameraPosition().zoom;
                    mCurrentAddress = aMapLocation.getAddress();
                    mCurrentAoiName = aMapLocation.getAoiName();
                    mCurrentCityCode = aMapLocation.getCityCode();
                    LogKit.v("getAddress:" + mCurrentAddress + "  getAoiName:" + mCurrentAoiName);
                    mListNearLocation = new ArrayList<NearLocationBean>();
                    //首先添加由定位或得的当前位置信息
                    mListNearLocation.add(new NearLocationBean("当前位置(" + mCurrentAoiName + ")", mCurrentAddress, "0.00KM"));*/
                    //相关周边POI搜索
                    //getNearPoi(currentLatitude, currentLongitude);

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                  /*  ToastUtils.shortToast("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());*/
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 12) {
//                        ToastUtils.shortToast("缺少定位权限，请到手机的设置中心开启定位权限");
                    }
                }
            }
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.stopLocation();
        }
    }
}
