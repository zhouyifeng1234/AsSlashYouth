package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.ui.viewmodel.ActivityMapModel;
import com.slash.youth.utils.LogKit;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class MapActivity extends Activity {

    MapView mMapView = null;
    private AMap mMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    float mapZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CommonUtils.setCurrentActivity(this);
        ActivityMapBinding activityMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        ActivityMapModel activityMapModel = new ActivityMapModel(activityMapBinding);
        activityMapBinding.setActivityMapModel(activityMapModel);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapview_activity_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        //初始化地图变量
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
//        float zoom = mMap.getCameraPosition().zoom;
//        ToastUtils.shortToast(zoom+"");
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.30400177, 120.64404488), 10));
//        AMapOptions aMapOptions=new AMapOptions();
//        aMapOptions.camera(CameraPosition.fromLatLngZoom(new LatLng(100,50),10));
        initAMapLocation();

    }

    private void initAMapLocation() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //传入true，启动定位，AmapLocationClient会驱动设备扫描周边wifi，获取最新的wifi列表（相比设备被动刷新会多消耗一些电量）
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setInterval(120000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
//                        mLocationOption.setInterval(60000);
//                        mLocationClient.setLocationOption(mLocationOption);
                        double currentLatitude = aMapLocation.getLatitude();//获取纬度
                        double currentLongitude = aMapLocation.getLongitude();//获取经度
                        LogKit.v("currentLatitude:" + currentLatitude + "  currentLongitude:" + currentLongitude);
                        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                        mapZoom = mMap.getCameraPosition().zoom;
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
//                        markerOptions.title("苏州");
                        mMap.addMarker(markerOptions);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.30400177, 120.64404488), 10));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoom));
                        LogKit.v("getAddress:" + aMapLocation.getAddress() + "  getAoiName" + aMapLocation.getAoiName());
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }
}
