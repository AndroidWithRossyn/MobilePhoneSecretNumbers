package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phonesecretcode.mobilelocator.coder.R;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class CurrentAddressActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "iaming";
    TextView add_txt,current_add;
    List<Address> addresses;
    TextView city_txt;
    TextView country_txt;
    LatLng currentLocation;
    ProgressDialog delayer;
    Geocoder geocoder;
    int geocoderMaxResults = 1;
    TextView latitude_txt;
    LocationManager locationManager;
    TextView longitude_txt;
    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    TextView state_txt;
    LocationListener listener;
    ImageView back_btn;
    CardView large,small;
    public void onConnectionSuspended(int i) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_address);
        listener=this;
        large=findViewById(R.id.large);
        small=findViewById(R.id.small);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), large, 1, null);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), small, 2, null);
        current_add=findViewById(R.id.current_add);
        back_btn=findViewById(R.id.btn_back);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.delayer = progressDialog;
            progressDialog.setMessage(" Loading....");
            this.delayer.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    delayer.dismiss();
                }
            }, 2000);
            getSupportActionBar().setTitle((CharSequence) "Location Details");
        } catch (Exception unused) {
            String str = "ActionBar not supported";
            Log.d(str, str);
        }
        isGooglePlayServicesAvailable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.mGoogleApiClient = new Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }
        new Criteria();
        createLocationRequest();
      /*  ((FloatingActionButton) findViewById(R.id.refresh)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onResume();
            }
        });*/
        callStoragePermistion();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 19 && iArr.length > 0 && iArr[0] == 0) {
            onResume();
        } else {
            Toast.makeText(this, "permission is needed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);

    }


    public boolean callStoragePermistion() {
        if (Build.VERSION.SDK_INT >= 23) {
            String str = "android.permission.ACCESS_COARSE_LOCATION";
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                requestPermissions(new String[]{str}, 19);
                return false;
            }
        }
        return true;
    }

    private boolean isGooglePlayServicesAvailable() {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.d(TAG, "onStart fired .google play .............");
        if (isGooglePlayServicesAvailable == 0) {
            return true;
        }
        GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 0).show();
        return false;
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

    private void updateUI() {
        String str = "Atiq";
        Log.e(str, "updateUI");
        Location location = this.mCurrentLocation;
        if (location != null) {
            String valueOf = String.valueOf(location.getLatitude());
            String valueOf2 = String.valueOf(this.mCurrentLocation.getLongitude());
            this.addresses = getGeocoderAddress(getApplicationContext());
            Log.e(str, "LOcation !=null");
            List<Address> list = this.addresses;
            if (list != null && list.size() > 0) {
                Log.e(str, "list != empty");
                Address address = (Address) this.addresses.get(0);
                String all_Address = address.getAddressLine(0);
                String locality = address.getLocality();
                String countryName = address.getCountryName();
                String adminArea = address.getAdminArea();
                String addressLine = getAddressLine(this);

                TextView textView = this.latitude_txt;
                if (!(textView == null || this.longitude_txt == null || this.city_txt == null || this.country_txt == null || this.state_txt == null || this.add_txt == null)) {
                    textView.setText(valueOf);
                    this.longitude_txt.setText(valueOf2);
                    this.country_txt.setText(countryName);
                    this.city_txt.setText(locality);
                    this.state_txt.setText(adminArea);
                    this.add_txt.setText(addressLine);
                    current_add.setText(all_Address);
                }
            }
            this.currentLocation = new LatLng(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude());
        }
    }

    private void createLocationRequest() {
        LocationRequest locationRequest =  LocationRequest.create();
        this.mLocationRequest = locationRequest;
        locationRequest.setInterval(60000);
        this.mLocationRequest.setFastestInterval(60000);
        this.mLocationRequest.setPriority(102);
    }

    public String getAddressLine(Context context) {
        List<Address> list = this.addresses;
        if (list == null || list.size() <= 0) {
            return null;
        }
        Address address = (Address) this.addresses.get(0);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("Address Line 0", address.getAddressLine(0));
        String str = "Address Line 1";
        linkedHashMap.put(str, address.getAddressLine(1));
        String str2 = "Address Line 2";
        linkedHashMap.put(str2, address.getAddressLine(2));
        String str3 = "Address Line 3";
        linkedHashMap.put(str3, address.getAddressLine(3));
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((String) linkedHashMap.get(str));
        sb.append("\n");
        sb.append((String) linkedHashMap.get(str2));
        sb.append(" [");
        sb.append((String) linkedHashMap.get(str3));
        sb.append("].");
        return sb.toString();
    }

    public List<Address> getGeocoderAddress(Context context) {
        String str = TAG;
        if (this.mCurrentLocation != null) {
            try {
                Geocoder geocoder2 = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                this.geocoder = geocoder2;
                this.addresses = geocoder2.getFromLocation(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude(), this.geocoderMaxResults);
                StringBuilder sb = new StringBuilder();
                sb.append("Address in Geocoder");
                sb.append(this.addresses);
                Log.d(str, sb.toString());
                return this.addresses;
            } catch (Throwable th) {
                Log.e(str, "Impossible to connect to Geocoder", th);
            }
        }
        return null;
    }
    public void onConnected(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        sb.append("onConnected - isConnected ...............: ");
        sb.append(this.mGoogleApiClient.isConnected());
        Log.d(TAG, sb.toString());
        startLocationUpdates();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("Connection failed: ");
        sb.append(connectionResult.toString());
        Log.d(TAG, sb.toString());
    }

    public void onDestroy() {
        super.onDestroy();

    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        this.mCurrentLocation = location;
        this.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    public void onPause() {
        super.onPause();
        if (this.mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
        if (this.locationManager.isProviderEnabled("gps")) {
            this.latitude_txt = (TextView) findViewById(R.id.latitude_txt);
            this.longitude_txt = (TextView) findViewById(R.id.longitude_txt);
            this.city_txt = (TextView) findViewById(R.id.city_txt);
            this.state_txt = (TextView) findViewById(R.id.state_txt);
            this.add_txt = (TextView) findViewById(R.id.add_txt);
            this.country_txt = (TextView) findViewById(R.id.country_txt);
            return;
        }
        showGPSDisabledAlertToUser();
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        this.mGoogleApiClient.connect();
    }
    public void onStop() {
        super.onStop();
        String str = TAG;
        Log.d(str, "onStop fired ..............");
        this.mGoogleApiClient.disconnect();
        StringBuilder sb = new StringBuilder();
        sb.append("isConnected ...............: ");
        sb.append(this.mGoogleApiClient.isConnected());
        Log.d(str, sb.toString());
    }
    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  listener);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        Log.d(TAG, "Location update stopped .......................");
    }



}
/*implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "iaming";
    TextView add_txt,current_add;
    List<Address> addresses;
    TextView city_txt;
    TextView country_txt;
    LatLng currentLocation;
    ProgressDialog delayer;
    Geocoder geocoder;
    int geocoderMaxResults = 1;
    TextView latitude_txt;
    LocationManager locationManager;
    TextView longitude_txt;
    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    String mLastUpdateTime;
    LocationRequest mLocationRequest;
    TextView state_txt;
    LocationListener listener;
    ImageView back_btn,refresh;
    CardView large,small;
    public void onConnectionSuspended(int i) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_address);

        current_add=findViewById(R.id.current_add);
        back_btn=findViewById(R.id.btn_back);
        refresh=findViewById(R.id.refresh);
        large=findViewById(R.id.large);
        small=findViewById(R.id.small);
        listener=this;



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.finishActivity(CurrentAddressActivity.this);

            }
        });
        try {
           *//* ProgressDialog progressDialog = new ProgressDialog(this);
            this.delayer = progressDialog;
            progressDialog.setMessage(" Loading....");
            this.delayer.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    delayer.dismiss();
                }
            }, 2000);*//*
            getSupportActionBar().setTitle((CharSequence) "Location Details");
        } catch (Exception unused) {
            String str = "ActionBar not supported";
            Log.d(str, str);
        }
        isGooglePlayServicesAvailable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            this.mGoogleApiClient = new Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }
        new Criteria();
        createLocationRequest();
        ((ImageView) findViewById(R.id.refresh)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), CurrentAddressActivity.this, isLoaded -> {
                    onResume();
//                });
            }

        });

        callStoragePermistion();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 19 && iArr.length > 0 && iArr[0] == 0) {
            onResume();
        } else {
            Toast.makeText(this, "permission is needed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public boolean callStoragePermistion() {
        if (Build.VERSION.SDK_INT >= 23) {
            String str = "android.permission.ACCESS_COARSE_LOCATION";
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                requestPermissions(new String[]{str}, 19);
                return false;
            }
        }
        return true;
    }

    private boolean isGooglePlayServicesAvailable() {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.d(TAG, "onStart fired .google play .............");
        if (isGooglePlayServicesAvailable == 0) {
            return true;
        }
        GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 0).show();
        return false;
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

    private void updateUI() {
        String str = "Atiq";
        Log.e(str, "updateUI");
        Location location = this.mCurrentLocation;
        if (location != null) {
            String valueOf = String.valueOf(location.getLatitude());
            String valueOf2 = String.valueOf(this.mCurrentLocation.getLongitude());
            this.addresses = getGeocoderAddress(getApplicationContext());
            Log.e(str, "LOcation !=null");
            List<Address> list = this.addresses;
            if (list != null && list.size() > 0) {
                Log.e(str, "list != empty");
                Address address = (Address) this.addresses.get(0);
                String all_Address = address.getAddressLine(0);
                String locality = address.getLocality();
                String countryName = address.getCountryName();
                String adminArea = address.getAdminArea();
                String addressLine = getAddressLine(this);

                TextView textView = this.latitude_txt;
                if (!(textView == null || this.longitude_txt == null || this.city_txt == null || this.country_txt == null || this.state_txt == null || this.add_txt == null)) {
                    textView.setText(valueOf);
                    this.longitude_txt.setText(valueOf2);
                    this.country_txt.setText(countryName);
                    this.city_txt.setText(locality);
                    this.state_txt.setText(adminArea);
                    this.add_txt.setText(addressLine);
                    current_add.setText(all_Address);
                }
            }
            this.currentLocation = new LatLng(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude());
        }
    }

    private void createLocationRequest() {
        LocationRequest locationRequest =  LocationRequest.create();
        this.mLocationRequest = locationRequest;
        locationRequest.setInterval(60000);
        this.mLocationRequest.setFastestInterval(60000);
        this.mLocationRequest.setPriority(102);
    }

    public String getAddressLine(Context context) {
        List<Address> list = this.addresses;
        if (list == null || list.size() <= 0) {
            return null;
        }
        Address address = (Address) this.addresses.get(0);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("Address Line 0", address.getAddressLine(0));
        String str = "Address Line 1";
        linkedHashMap.put(str, address.getAddressLine(1));
        String str2 = "Address Line 2";
        linkedHashMap.put(str2, address.getAddressLine(2));
        String str3 = "Address Line 3";
        linkedHashMap.put(str3, address.getAddressLine(3));
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((String) linkedHashMap.get(str));
        sb.append("\n");
        sb.append((String) linkedHashMap.get(str2));
        sb.append(" [");
        sb.append((String) linkedHashMap.get(str3));
        sb.append("].");
        return sb.toString();
    }

    public List<Address> getGeocoderAddress(Context context) {
        String str = TAG;
        if (this.mCurrentLocation != null) {
            try {
                Geocoder geocoder2 = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                this.geocoder = geocoder2;
                this.addresses = geocoder2.getFromLocation(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude(), this.geocoderMaxResults);
                StringBuilder sb = new StringBuilder();
                sb.append("Address in Geocoder");
                sb.append(this.addresses);
                Log.d(str, sb.toString());
                return this.addresses;
            } catch (Throwable th) {
                Log.e(str, "Impossible to connect to Geocoder", th);
            }
        }
        return null;
    }
    public void onConnected(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        sb.append("onConnected - isConnected ...............: ");
        sb.append(this.mGoogleApiClient.isConnected());
        Log.d(TAG, sb.toString());
        startLocationUpdates();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        StringBuilder sb = new StringBuilder();
        sb.append("Connection failed: ");
        sb.append(connectionResult.toString());
        Log.d(TAG, sb.toString());
    }

    public void onDestroy() {
        super.onDestroy();

    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        this.mCurrentLocation = location;
        this.mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    public void onPause() {
        super.onPause();
        if (this.mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    public void onResume() {
        super.onResume();
        if (this.mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
        if (this.locationManager.isProviderEnabled("gps")) {
            this.latitude_txt = (TextView) findViewById(R.id.latitude_txt);
            this.longitude_txt = (TextView) findViewById(R.id.longitude_txt);
            this.city_txt = (TextView) findViewById(R.id.city_txt);
            this.state_txt = (TextView) findViewById(R.id.state_txt);
            this.add_txt = (TextView) findViewById(R.id.add_txt);
            this.country_txt = (TextView) findViewById(R.id.country_txt);
            return;
        }
        showGPSDisabledAlertToUser();
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        this.mGoogleApiClient.connect();
    }
    public void onStop() {
        super.onStop();
        String str = TAG;
        Log.d(str, "onStop fired ..............");
        this.mGoogleApiClient.disconnect();
        StringBuilder sb = new StringBuilder();
        sb.append("isConnected ...............: ");
        sb.append(this.mGoogleApiClient.isConnected());
        Log.d(str, sb.toString());
    }
    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  listener);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        Log.d(TAG, "Location update stopped .......................");
    }



}*/