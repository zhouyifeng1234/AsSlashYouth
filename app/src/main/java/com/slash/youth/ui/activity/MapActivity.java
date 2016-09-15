package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMapBinding;
import com.slash.youth.domain.NearLocationBean;
import com.slash.youth.ui.viewmodel.ActivityMapModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
    float mapZoom = 14;
    PoiSearch poiSearch;
    PoiSearch.Query query;
    private String mCurrentAddress;
    private String mCurrentAoiName;
    private ArrayList<NearLocationBean> mListNearLocation;
    private ActivityMapModel mActivityMapModel;
    private ActivityMapBinding mActivityMapBinding;
    private String mCurrentCityCode;
    private LatLng mCurrentLatlng;
    private MarkerOptions mMapCenterMarkerOptions;
    private Marker mMarker;
    private boolean isMoveByGestures = false;
    GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CommonUtils.setCurrentActivity(this);
        mActivityMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        mActivityMapModel = new ActivityMapModel(mActivityMapBinding);
        mActivityMapBinding.setActivityMapModel(mActivityMapModel);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapview_activity_map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        //初始化地图变量
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
//        UiSettings uiSettings = mMap.getUiSettings();
//        uiSettings.setMyLocationButtonEnabled(true);

//        float zoom = mMap.getCameraPosition().zoom;
//        ToastUtils.shortToast(zoom+"");
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.30400177, 120.64404488), 10));
//        AMapOptions aMapOptions=new AMapOptions();
//        aMapOptions.camera(CameraPosition.fromLatLngZoom(new LatLng(100,50),10));

        mMapCenterMarkerOptions = new MarkerOptions();
        BitmapDescriptor centerBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.dingwei_icon);
        mMapCenterMarkerOptions.icon(centerBitmapDescriptor);
//      mMapCenterMarkerOptions.position(mMap.getCameraPosition().target);
        mMarker = mMap.addMarker(mMapCenterMarkerOptions);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                int mapviewMeasuredWidth = mMapView.getMeasuredWidth();
                int mapviewMeasuredHeight = mMapView.getMeasuredHeight();
//                LogKit.v("mapviewMeasuredWidth:" + mapviewMeasuredWidth);
//                LogKit.v("mapviewMeasuredHeight:" + mapviewMeasuredHeight);
                mMarker.setPositionByPixels(mapviewMeasuredWidth / 2, mapviewMeasuredHeight / 2);
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new SlashOnGeocodeSearchListener());
        initAMapLocation();
        initListener();
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
                        mCurrentLatlng = latLng;
//                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.30400177, 120.64404488), 10));
                        isMoveByGestures = false;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mapZoom));
                        mapZoom = mMap.getCameraPosition().zoom;
                        mCurrentAddress = aMapLocation.getAddress();
                        mCurrentAoiName = aMapLocation.getAoiName();
                        mCurrentCityCode = aMapLocation.getCityCode();
                        LogKit.v("getAddress:" + mCurrentAddress + "  getAoiName:" + mCurrentAoiName);
                        mListNearLocation = new ArrayList<NearLocationBean>();
                        //首先添加由定位或得的当前位置信息
                        mListNearLocation.add(new NearLocationBean("当前位置(" + mCurrentAoiName + ")", mCurrentAddress, "0.00KM"));

                        //相关周边POI搜索
                        getNearPoi(currentLatitude, currentLongitude);

                        mMap.setOnCameraChangeListener(new SlashOnCameraChangeListener());
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
            }
        });
        //启动定位
        mLocationClient.startLocation();
    }

    public void getNearPoi(double currentLatitude, double currentLongitude) {
        query = new PoiSearch.Query("", "");
        poiSearch = new PoiSearch(CommonUtils.getContext(), query);
        query.setPageSize(50);
        query.setPageNum(1);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(currentLatitude, currentLongitude), 1000));
        poiSearch.setOnPoiSearchListener(new SlashServicePOISearchListener());
        poiSearch.searchPOIAsyn();
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


    public class SlashServicePOISearchListener implements PoiSearch.OnPoiSearchListener {


        @Override
        public void onPoiSearched(PoiResult result, int rCode) {

            if (rCode == 1000) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
//                        poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        List<SuggestionCity> suggestionCities = result
                                .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                        if (poiItems != null && poiItems.size() > 0) {
                            for (int i = 0; i < poiItems.size(); i++) {
                                PoiItem poiItem = poiItems.get(i);
                                LogKit.v("getDistance:" + poiItem.getDistance() + "    getTitle:" + poiItem.getTitle() + "    getSnippet:" + poiItem.getSnippet());
                                mListNearLocation.add(new NearLocationBean(poiItem.getTitle(), poiItem.getSnippet(), convertMeterToKM(poiItem.getDistance())));
                            }
//                            aMap.clear();// 清理之前的图标
//                            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                            poiOverlay.removeFromMap();
//                            poiOverlay.addToMap();
//                            poiOverlay.zoomToSpan();
                        } else if (suggestionCities != null
                                && suggestionCities.size() > 0) {
//                            showSuggestCity(suggestionCities);
                        } else {
                            ToastUtils.shortToast("没有搜索到结果");
                        }
                    }
                } else {
                    ToastUtils.shortToast("没有搜索到结果");
                }
            } else {
                ToastUtils.shortToast(rCode + "");
            }
            mActivityMapModel.initNearLocationData(mListNearLocation);
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    }


    public String convertMeterToKM(int meter) {
        float meterF = meter;
        return meterF / 1000 + "KM";
    }

    private void initListener() {
        mActivityMapBinding.etActivityMapSearchKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mActivityMapModel.setLlMapInfoVisible(View.VISIBLE);
                    mActivityMapModel.setSvSearchListVisible(View.INVISIBLE);
                } else {
                    mActivityMapModel.setLlMapInfoVisible(View.INVISIBLE);
                    mActivityMapModel.setSvSearchListVisible(View.VISIBLE);
                    addToSearchList(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mActivityMapBinding.ivbtnActivityMapCurrentLocation.setOnClickListener(new CurrentLocationClickListener());
    }

    private void addToSearchList(String keyword) {
        mActivityMapBinding.llActivityMapSearchlist.removeAllViews();
        query = new PoiSearch.Query(keyword, "", mCurrentCityCode);
        poiSearch = new PoiSearch(CommonUtils.getContext(), query);
        query.setPageSize(50);
        query.setPageNum(1);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int rCode) {
                if (rCode == 1000) {
                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(query)) {// 是否是同一条
                            List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                            List<SuggestionCity> suggestionCities = result
                                    .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                            if (poiItems != null && poiItems.size() > 0) {
                                for (int i = 0; i < poiItems.size(); i++) {
                                    PoiItem poiItem = poiItems.get(i);
                                    LogKit.v("getDistance:" + poiItem.getDistance() + "    getTitle:" + poiItem.getTitle() + "    getSnippet:" + poiItem.getSnippet());

                                    TextView tvKeywordPoi = createKeyWordPoiTextView(poiItem.getTitle(), poiItem);
                                    View vDividerKeywordPoi = createDividerKeywordPoi();
                                    mActivityMapBinding.llActivityMapSearchlist.addView(tvKeywordPoi);
                                    mActivityMapBinding.llActivityMapSearchlist.addView(vDividerKeywordPoi);
                                }
                            } else if (suggestionCities != null
                                    && suggestionCities.size() > 0) {
//                            showSuggestCity(suggestionCities);
                            } else {
                                ToastUtils.shortToast("没有搜索到结果");
                            }
                        }
                    } else {
                        ToastUtils.shortToast("没有搜索到结果");
                    }
                } else {
                    ToastUtils.shortToast(rCode + "");
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    private TextView createKeyWordPoiTextView(String title, final PoiItem poiItem) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.setMargins(CommonUtils.dip2px(11), CommonUtils.dip2px(20), 0, CommonUtils.dip2px(20));
        TextView tvKeywordPoi = new TextView(CommonUtils.getContext());
        tvKeywordPoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLonPoint poiLatLonPoint = poiItem.getLatLonPoint();
                LatLng latlng = new LatLng(poiLatLonPoint.getLatitude(), poiLatLonPoint.getLongitude());
                isMoveByGestures = false;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, mapZoom));
                mActivityMapModel.setLlMapInfoVisible(View.VISIBLE);
                mActivityMapModel.setSvSearchListVisible(View.INVISIBLE);
                mActivityMapBinding.etActivityMapSearchKeyword.setText("");

                mListNearLocation = new ArrayList<NearLocationBean>();
                //首先添加由定位或得的当前位置信息
                mListNearLocation.add(new NearLocationBean(poiItem.getTitle(), poiItem.getSnippet(), "0.00KM"));
                getNearPoi(poiLatLonPoint.getLatitude(), poiLatLonPoint.getLongitude());
            }
        });
        tvKeywordPoi.setLayoutParams(params);
        tvKeywordPoi.setText(title);
        tvKeywordPoi.setTextColor(0xff333333);
        tvKeywordPoi.setTextSize(16.5f);
        return tvKeywordPoi;
    }

    private View createDividerKeywordPoi() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, CommonUtils.dip2px(0.5f));
        View vDividerKeywordPoi = new View(CommonUtils.getContext());
        vDividerKeywordPoi.setLayoutParams(params);
        vDividerKeywordPoi.setBackgroundColor(0xffe5e5e5);
        return vDividerKeywordPoi;
    }

    public class SlashOnCameraChangeListener implements AMap.OnCameraChangeListener {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
//            LatLng target = cameraPosition.target;
//            LogKit.v("cameraPosition latitude" + target.latitude);
//            LogKit.v("cameraPosition longitude" + target.longitude);
        }

        @Override
        public void onCameraChangeFinish(CameraPosition cameraPosition) {
            LatLng target = cameraPosition.target;
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            LatLonPoint latLonPoint = new LatLonPoint(target.latitude, target.longitude);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);

            if (isMoveByGestures) {
//                LogKit.v("cameraPosition latitude" + target.latitude);
//                LogKit.v("cameraPosition longitude" + target.longitude);
                mListNearLocation = new ArrayList<NearLocationBean>();
                getNearPoi(target.latitude, target.longitude);
            } else {
                isMoveByGestures = true;
            }
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

    public class CurrentLocationClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            isMoveByGestures = false;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatlng, mapZoom));
            mListNearLocation = new ArrayList<NearLocationBean>();
            mListNearLocation.add(new NearLocationBean("当前位置(" + mCurrentAoiName + ")", mCurrentAddress, "0.00KM"));
            getNearPoi(mCurrentLatlng.latitude, mCurrentLatlng.longitude);
        }
    }

    public class SlashOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == 1000) {
                String aoiName = "没有地点信息";
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    List<AoiItem> aois = result.getRegeocodeAddress().getAois();
                    if (aois != null && aois.size() > 0) {
                        aoiName = aois.get(0).getAoiName();
                    }
                }
                mMarker.setTitle("");
                mMap.setInfoWindowAdapter(new SlashMapInfoWindowAdapter(aoiName));
                mMarker.showInfoWindow();
            } else {
                ToastUtils.shortToast("" + rCode);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    }

}
