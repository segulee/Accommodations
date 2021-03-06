package com.youngje.tgwing.accommodations.Util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
public class LocationUtil implements LocationListener {

    public static Location curlocation = null;

    private double lat;
    private double lon;
    Context ctx;
    private String provider;

    LocationManager locationMgr;        // 위치 관리자

    private boolean isGpsEnabled; // gps 사용 여부
    private boolean isNetworkEnabled; // 네트워크 사용 여부

    //private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 8001;

    public Location getLocation() {

        try {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: 2016. 10. 6. error check
            }
            return curlocation != null ? curlocation : locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e) {}

        return curlocation;
    }

    public static void setLocation(Location curlocation) {
        LocationUtil.curlocation = curlocation;
    }

    public LocationUtil(Context appCtx) { // 생성자.
        ctx = appCtx;
        locationMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        try {

            isGpsEnabled = locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("체크퍼미션","되지않음");
                return ;
            }

            if(!isGpsEnabled && !isNetworkEnabled) {
                Toast.makeText(ctx,"GPS 수신 불가",Toast.LENGTH_LONG);
            }

            if(isGpsEnabled)
                locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5000, this);

            if(isNetworkEnabled)
                locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,5000, this);

            try {
                // 위치 관리자로부터 gps, 네트워크의 마지막으로 알려진 장소를 얻어 옮
                Location gps = locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location network = locationMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // 위치 값을 얻어오게 되면 현재 위치를 지정
                // 우선순위는 gps > 네트워크 > 기본값
                if(gps!=null) {
                    curlocation = gps;
                    Log.i("GPS: ", gps.toString());
                }
                else if (network!=null) {
                    curlocation = network;
                    Log.i("Network: ", network.toString());
                }


            } catch (Exception ex2) {	// 예외 발생 시
                ex2.printStackTrace();	// 메세지를 내보내고

            }


        } catch (Exception e) {

        }


    }

    public void getGPSInfo() {


    }

    @Override
    public void onLocationChanged(Location location) {
        curlocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
