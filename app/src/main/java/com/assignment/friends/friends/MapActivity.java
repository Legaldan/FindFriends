package com.assignment.friends.friends;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;


public class MapActivity extends AppCompatActivity {

    MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        AMap aMap = mMapView.getMap();
        LatLng latLng = new LatLng(31.32,120.62);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);

        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.drawable.ic_location)));
        markerOption.title("Suzhou").snippet("You are here");
        aMap.addMarker(markerOption);

        LatLng latLng1 = new LatLng(31.254,120.499);
        aMap.addMarker(new MarkerOptions().position(latLng1).title("Wu Yingzhen").snippet(String.valueOf((int)AMapUtils.calculateLineDistance(latLng,latLng1))+" m"));

        LatLng latLng2 = new LatLng(31.264,120.798);
        aMap.addMarker(new MarkerOptions().position(latLng2).title("Ding Yalei").snippet(String.valueOf((int)AMapUtils.calculateLineDistance(latLng,latLng2))+" m"));

        LatLng latLng3 = new LatLng(31.296,120.726);
        aMap.addMarker(new MarkerOptions().position(latLng3).title("Bob Smith").snippet(String.valueOf((int)AMapUtils.calculateLineDistance(latLng,latLng3))+" m"));

        LatLng latLng4 = new LatLng(31.305,120.673);
        aMap.addMarker(new MarkerOptions().position(latLng4).title("Alice Green").snippet(String.valueOf((int)AMapUtils.calculateLineDistance(latLng,latLng4))+" m"));

        aMap.getMapScreenMarkers();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
