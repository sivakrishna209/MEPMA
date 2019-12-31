package com.mepma.smid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mepma.smid.Fragment.LoginFragment;
import com.mepma.smid.Fragment.SignupFragment;
import com.mepma.smid.Util.DataHolder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener {


    public  static final int requestLocationPermissionCode  = 1 ;
    private boolean gpsStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addLoginFragment();

        checkGpsStatus();
        enableRuntimePermission();


        //initializeLocationManager(getApplicationContext());
    }


    public void addLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        loadFragment(fragment);

    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return  false;

    }

    public void addFragment(Fragment fragment) {
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onLoginSuccessfully() {

        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }




    public void enableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {
            DataHolder.getInstance().initializeLocationManager(getApplicationContext());
            Toast.makeText(MainActivity.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION }, requestLocationPermissionCode);

        }
    }


    public void checkGpsStatus(){

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        Log.d("Location","onRequestPermissionsResult PResult: "+ Arrays.toString(PResult).toString() + " permission : " +Arrays.toString(per));

        switch (RC) {

            case requestLocationPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    DataHolder.getInstance().initializeLocationManager(getApplicationContext());
//                    Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();
                    finishAndRemoveTask();
                }
                break;
        }
    }


    @Override
    public void onFragmentInteraction(LoginFragment fragment) {
        SignupFragment signupFragment = new SignupFragment();
        addFragment(signupFragment);
    }
}
