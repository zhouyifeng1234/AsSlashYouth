package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceModeBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PublishServiceModeModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/9/20.
 */
public class PublishServiceModeActivity extends BaseActivity {

    MapView mMapView;
    public AMap aMap;
    private MarkerOptions mMapCenterMarkerOptions;
    private Marker mMarker;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishServiceModeBinding activityPublishServiceModeBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_service_mode);
        PublishServiceModeModel publishServiceModeModel = new PublishServiceModeModel(activityPublishServiceModeBinding, this);
        activityPublishServiceModeBinding.setPublishServiceModeModel(publishServiceModeModel);

        mMapView = activityPublishServiceModeBinding.mapviewActivityPublishServiceMode;
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        UiSettings mapUiSettings = aMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(false);
        mapUiSettings.setZoomGesturesEnabled(false);
        mMapCenterMarkerOptions = new MarkerOptions();
        BitmapDescriptor centerBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.dingwei_icon);
        mMapCenterMarkerOptions.icon(centerBitmapDescriptor);
        mMarker = aMap.addMarker(mMapCenterMarkerOptions);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                int mapviewMeasuredWidth = mMapView.getMeasuredWidth();
                int mapviewMeasuredHeight = mMapView.getMeasuredHeight();
                mMarker.setPositionByPixels(mapviewMeasuredWidth / 2, mapviewMeasuredHeight / 2);
            }
        });
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
                    double currentLatitude = aMapLocation.getLatitude();//获取纬度
                    double currentLongitude = aMapLocation.getLongitude();//获取经度
                    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//                    String currentAddress = aMapLocation.getAddress();
                    String currentAoiName = aMapLocation.getAoiName();
//                    String currentCityCode = aMapLocation.getCityCode();
                    mMarker.setTitle("");
                    aMap.setInfoWindowAdapter(new SlashMapInfoWindowAdapter(currentAoiName));
                    mMarker.showInfoWindow();

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    ToastUtils.shortToast("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.stopLocation();
        }
    }

    public class SlashMapInfoWindowAdapter implements AMap.InfoWindowAdapter {
        private String mInfo;

        public SlashMapInfoWindowAdapter(String info) {
            this.mInfo = info;
        }

        //这两个回调方法，首先检测getInfoWindow是否返回null，如果非null，则不检测getInfoContents，否则检测getInfoContents的返回值
        @Override
        public View getInfoWindow(Marker marker) {
            View vInfoWindow = View.inflate(CommonUtils.getContext(), R.layout.infowindow_map_marker, null);
            TextView mTvMarkerTitle = (TextView) vInfoWindow.findViewById(R.id.tv_infowindow_map_marker_title);
            mTvMarkerTitle.setText(mInfo);
            return vInfoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
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
