package com.abilitymap;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.location.Address;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import java.text.SimpleDateFormat;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener, SetMarker {
    private GpsTracker gpsTracker;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;

    private Location mLastlocation = null;
    private double speed, calSpeed, getSpeed;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    List<LatLng> latLngList = new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState){ //화면 생성과 함께 현재 위치 받아옴.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        if(mapFragment ==null ){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);



    }





//
    @Override
    public void onRequestPermissionsResult ( int requestCode,
                                             @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE  && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

            if (grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }


    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if(overlay instanceof Marker){
            Toast.makeText(this.getApplicationContext(),"위험지역입니다",Toast.LENGTH_LONG).show();

            LocationDetailFragment infoFragment = new LocationDetailFragment();


            getSupportFragmentManager().beginTransaction().add(R.id.map, infoFragment).commit();
            return true;
        }
        return false;

    }



    void checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }



    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    8);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);

        return address.getAddressLine(0).toString()+"\n";

    }



    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);

                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, id);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void UpdateCircle(double x, double y){
        CircleOverlay circle = new CircleOverlay();
        circle.setCenter(new LatLng(x, y));
        circle.setRadius(30);
        circle.setColor(Color.parseColor("#30FF7B00"));
        circle.setOutlineColor(Color.parseColor("#30FF7B00"));
        circle.setMap(naverMap);
        circle.setMinZoom(15);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(x,y));
        marker.setIcon(OverlayImage.fromResource(R.drawable.invalid_name));
        marker.setMinZoom(8);
        marker.setMaxZoom(15);
        marker.setWidth(120);
        marker.setHeight(120);
        marker.setMap(naverMap);

        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng(x,y));
        marker2.setIcon(OverlayImage.fromResource(R.drawable.invalid_name));
        marker2.setMinZoom(17);
        marker.setMaxZoom(15);
        marker2.setWidth(120);
        marker2.setHeight(120);
        marker2.setMap(naverMap);


    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setMaxZoom(18.0);
        naverMap.setMinZoom(8.0);

        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);



        final TextView location_text = (TextView)findViewById(R.id.location_text);


        latLngList.add(new LatLng(37.5670135,126.9783740));
        latLngList.add(new LatLng(37.6670135,126.5783740));
        latLngList.add(new LatLng(37.4670135,126.3783740));
        latLngList.add(new LatLng(37.2670135,126.0783740));

        setMarker(0,latLngList,"danger",naverMap);
        setMarker(1,latLngList,"slope",naverMap);
        setMarker(2,latLngList,"charger",naverMap);
        setMarker(3,latLngList,"wheelchair",naverMap);


        UpdateCircle(37.300930274423386,126.84060399395506);
        UpdateCircle(37.301087156219666,126.84001151017893);


        /*

        Marker marker = new Marker();
        marker.setPosition(latLngList.get(0));
        marker.setMap(naverMap);

        marker.setOnClickListener(this);

*/

        /*
        final TextView textView_lat = findViewById(R.id.lat);
        final TextView textView_lon = findViewById(R.id.lon);
        */
//        final TextView tvGetSpeed = findViewById(R.id.tvGetspeed);
//        final TextView tvCalSpeed = findViewById(R.id.tvCalspeed);


        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {

                gpsTracker = new GpsTracker(MainActivity.this);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                double deltaTime = 0;

                // getSpeed() 함수를 이용하여 속도 계산(m/s -> km/h)
                getSpeed = Double.parseDouble(String.format("%.3f", location.getSpeed() * 3.6));

                // 위치 변경이 두번째로 변경된 경우 계산에 의해 속도 계산
                if(mLastlocation != null){
                    deltaTime = (location.getTime() - mLastlocation.getTime());
                    // 속도 계산(시간=ms, 거리=m -> km/h)
                    speed = (mLastlocation.distanceTo(location) / deltaTime) * 3600;
                    calSpeed = Double.parseDouble(String.format("%.3f", speed));
                }
                //현재위치를 지난 위치로 변경
                mLastlocation = location;

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);


                String addrCut[] = address.split(" ");
                location_text.setText(addrCut[1]+" "+addrCut[2]+" "+addrCut[3]);




                String lat_str = Double.toString(latitude);
                String lon_str = Double.toString(longitude);

                /*
                textView_lat.setText(lat_str);
                textView_lon.setText(lon_str);
                 */
                UiSettings uiSettings = naverMap.getUiSettings();
                uiSettings.setCompassEnabled(true);
                uiSettings.setScaleBarEnabled(true);
                uiSettings.setZoomControlEnabled(false); //줌인 줌아웃
                uiSettings.setLocationButtonEnabled(true);

                LocationButtonView locationButtonView = findViewById(R.id.navermap_location_button);
                locationButtonView.setMap(naverMap);

                String gs_str = Double.toString(getSpeed);
                String cs_str = Double.toString(calSpeed);



            }
        });

    }



}

