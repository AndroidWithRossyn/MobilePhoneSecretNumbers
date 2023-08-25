package com.phonesecretcode.mobilelocator.coder.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class TrafficActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    ProgressDialog delayer;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    RelativeLayout relTraficRules;
    LocationManager locationManager;
    CardView small;
    public boolean onMyLocationButtonClick() {
        return false;
    }

    public void onMyLocationClick(Location location) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        small=findViewById(R.id.small);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), small, 2, null);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!this.locationManager.isProviderEnabled("gps")) {
            showGPSDisabledAlertToUser();
        }
        else
        {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.delayer = progressDialog;
            progressDialog.setMessage(" Loading....");
            this.delayer.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    delayer.dismiss();
                }
            }, 2000);
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            this.mapFragment = supportMapFragment;
            supportMapFragment.getMapAsync(this);
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rel_traficsign);
            this.relTraficRules = relativeLayout;
            relativeLayout.setVisibility(View.VISIBLE);
        }

    }
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        String str = "android.permission.ACCESS_FINE_LOCATION";
        if (ContextCompat.checkSelfPermission(this, str) == 0) {
            this.mMap.setMyLocationEnabled(true);
            this.mMap.setOnMyLocationButtonClickListener(this);
            this.mMap.setOnMyLocationClickListener(this);
            zoom_camera();
            this.mMap.setTrafficEnabled(true);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{str}, 102);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 102 && iArr.length > 0 && iArr[0] == 0 && (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0)) {
            this.mMap.setMyLocationEnabled(true);
            this.mMap.setOnMyLocationButtonClickListener(this);
            this.mMap.setOnMyLocationClickListener(this);
            zoom_camera();
            this.mMap.setTrafficEnabled(true);
        }
    }

    public void zoom_camera() {
        if (getMyLocation() == null) {
            Toast.makeText(this, "Zoom Location Button", Toast.LENGTH_LONG).show();
            return;
        }
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude()), 13.0f));
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(getMyLocation().getLatitude(), getMyLocation().getLongitude())).zoom(17.0f).bearing(90.0f).tilt(40.0f).build()));
    }

    private Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return null;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation("gps");
        if (lastKnownLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        }
        return lastKnownLocation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "GPS is disabled. Would you like to enable it?").setCancelable(false).setPositiveButton((CharSequence) "Enable", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }
}