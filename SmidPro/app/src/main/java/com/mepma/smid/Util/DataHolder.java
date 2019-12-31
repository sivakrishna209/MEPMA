package com.mepma.smid.Util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DataHolder implements Serializable, LocationListener {

    private static volatile DataHolder sharedObject;


    public String token = null;
    public Location currentLocation = null;
    public boolean gpsStatus;
    public LocationManager mLocationManager;

    //private constructor.
    private DataHolder() {

        //Prevent form the reflection api.
        if (sharedObject != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }


    public static DataHolder getInstance() {
        //Double check locking pattern
        if (sharedObject == null) { //Check for the first time

            synchronized (DataHolder.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sharedObject == null) sharedObject = new DataHolder();
            }
        }

        return sharedObject;
    }


    //Make singleton from serialize and deserialize operation.
    protected DataHolder readResolve() {
        return getInstance();
    }


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public void initialize() {

        /*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();

        if (!imageLoader.isInited()) {
            imageLoader.init(config);
        } */

    }


    public void initializeLocationManager(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Log.d("locationManager"," Does not have location Permission");
            return;
        }
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        String holder = mLocationManager.getBestProvider(new Criteria(), false);
        Log.d("locationManger", holder + " " + LocationManager.GPS_PROVIDER);

//        if (holder != null) {
//            mLocationManager.requestLocationUpdates(holder, 0, 0, this);
//        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
//        }
    }




    public long parseDateStringToTimeInterval(String strDate) {

        try {
            Date date = simpleDateFormat.parse(strDate);
            return date.getTime();
        } catch (ParseException ex) {
            Log.d("Exception", ex.getLocalizedMessage());
        }
        return -1;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        gpsStatus = true;

        Log.d("locationManager"," location :"+location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("locationManager"," onStatusChanged location :"+provider + String.format(" status:%d",status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("locationManager"," onProviderEnabled :"+provider);
        gpsStatus = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("locationManager"," onProviderDisabled :"+provider);
        gpsStatus = false;
    }
}
