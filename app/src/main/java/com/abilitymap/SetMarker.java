package com.abilitymap;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.List;

public interface SetMarker extends Overlay.OnClickListener {
    public default void setMarker(int index,
                                  List<LatLng> list, String markerType, NaverMap naverMap){

        Marker marker = new Marker();
        marker.setPosition(list.get(index));
        marker.setWidth(100);
        marker.setHeight(100);
        switch(markerType){
            case "danger": marker.setIcon(OverlayImage.fromResource(R.drawable.danger)); break;
            case "slope": marker.setIcon(OverlayImage.fromResource(R.drawable.danger)); break;
            case "charger": marker.setIcon(OverlayImage.fromResource(R.drawable.charge_icon)); break;
            case "wheelchair": marker.setIcon(OverlayImage.fromResource(R.drawable.wheel_icon)); break;
        }
        marker.setMap(naverMap);

        marker.setOnClickListener(this);
    }

    @Override
    boolean onClick(@NonNull Overlay overlay);
}
