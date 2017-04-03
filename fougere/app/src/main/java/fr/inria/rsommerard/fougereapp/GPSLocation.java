package fr.inria.rsommerard.fougereapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by lakhdar on 12/8/16.
 */

public class GPSLocation {
    private GPSLocationListener gpsLocationListener;
    private AppCompatActivity activity;
    public GPSLocation(AppCompatActivity activity, GPSLocationListener gpsLocationListener){
        this.gpsLocationListener = gpsLocationListener;
        this.activity = activity;
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
    public boolean getGPSLocation() {
        LocationManager locationManager =
                (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!checkLocationPermission()) {
            Log.e(MainActivity.TAG, "Missing permissions");
            return false;
        }
        String provider = locationManager.getBestProvider(new Criteria(), true);
        locationManager.requestLocationUpdates(provider, 36000, 100, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v(MainActivity.TAG, "New GPS location: [lat: " + location.getLatitude() + ", lon: " + location.getLongitude() + "]");
                GPSLocation.this.gpsLocationListener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v(MainActivity.TAG, "New GPS location status changed");

            }

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        });
        return true;
    }
    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    interface GPSLocationListener{
        public void onLocationChanged(Location location);
    }
}
