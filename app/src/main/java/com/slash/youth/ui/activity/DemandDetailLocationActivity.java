package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityDemandDetailLocationBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.DemandDetailLocationModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.List;

/**
 * Created by zhouyifeng on 2016/10/25.
 */
public class DemandDetailLocationActivity extends BaseActivity {

    private LatLng mDemandLatLng;
    private MapView mMapView = null;
    private ActivityDemandDetailLocationBinding mActivityDemandDetailLocationBinding;
    private AMap mMap;
    private MarkerOptions mMapCenterMarkerOptions;
    private Marker mMarker;
    float mapZoom = 14;
    GeocodeSearch geocoderSearch;
    private DemandDetailLocationModel mDemandDetailLocationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mActivityDemandDetailLocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_demand_detail_location);
        mDemandDetailLocationModel = new DemandDetailLocationModel(mActivityDemandDetailLocationBinding, this);
        mActivityDemandDetailLocationBinding.setDemandDetailLocationModel(mDemandDetailLocationModel);

        initData();
        initListener();
        initView(savedInstanceState);
    }

    private void initData() {
        mDemandLatLng = getIntent().getParcelableExtra("demandLatLng");
    }

    private void initListener() {

    }

    private void initView(Bundle savedInstanceState) {
        mMapView = mActivityDemandDetailLocationBinding.mapviewDemandDetail;
        mMapView.onCreate(savedInstanceState);
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMapCenterMarkerOptions = new MarkerOptions();
        BitmapDescriptor centerBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.dingwei_icon);
        mMapCenterMarkerOptions.icon(centerBitmapDescriptor);
        mMarker = mMap.addMarker(mMapCenterMarkerOptions);
        mMapView.post(new Runnable() {
            @Override
            public void run() {
                int mapviewMeasuredWidth = mMapView.getMeasuredWidth();
                int mapviewMeasuredHeight = mMapView.getMeasuredHeight();
                mMarker.setPositionByPixels(mapviewMeasuredWidth / 2, mapviewMeasuredHeight / 2);
            }
        });

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new SlashOnGeocodeSearchListener());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDemandLatLng, mapZoom));

        LatLonPoint latLonPoint = new LatLonPoint(mDemandLatLng.latitude, mDemandLatLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    public class SlashOnGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == 1000) {
                String aoiName = "没有aoi信息";
                String formatAddress = "没有地址信息";
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    formatAddress = result.getRegeocodeAddress().getFormatAddress();

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

}
