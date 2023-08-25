package com.phonesecretcode.mobilelocator.coder.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.List;
import java.util.Locale;

public class StandardModeActivity extends AppCompatActivity implements SensorEventListener, LocationListener, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "iaming";
    String addressd = "";
    float altitudes;
    private float currentDegree = 0.0f;
    ProgressDialog delayer;
    private ImageView image;
    float latitudes;
    LocationManager locationManager;
    float longitudes;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private SensorManager mSensorManager;
    public TextView mTxtAddress;
    public TextView mTxtAltitude;
    public TextView mTxtLatitude;
    public TextView mTxtLongitude;
    TextView tvHeading;
    CardView large;
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public class getData extends AsyncTask<Void, String, Void> {
        Location location;
        Context mContext;

        public getData(Context context, Location location2) {
            this.mContext = context;
            this.location = location2;
        }


        public Void doInBackground(Void... voidArr) {
            double longitude = this.location.getLongitude();
            double latitude = this.location.getLatitude();
            longitudes = (float) longitude;
            latitudes = (float) latitude;
            altitudes = (float) this.location.getAltitude();
            try {
                List fromLocation = new Geocoder(this.mContext, Locale.getDefault()).getFromLocation(this.location.getLatitude(), this.location.getLongitude(), 1);
                if (fromLocation.size() > 0) {
                    Address address = (Address) fromLocation.get(0);
                    addressd = address.getAddressLine(0);
                }
            } catch (Exception unused) {
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            mTxtAddress.setText(addressd);
            String str = " ";
            String str2 = formatDms(longitudes) + str + getDirectionText(longitudes);
            String str3 = formatDms(latitudes) + str + getDirectionText(latitudes);
            mTxtLongitude.setText(str2);
            mTxtLatitude.setText(str3);
            double altitude = this.location.getAltitude();
            mTxtAltitude.setText(String.format(Locale.US, "%d m", new Object[]{Long.valueOf((long) altitude)}));
        }
    }

    public static String getDirectionText(float f) {
        return ((f < 0.0f || f >= 22.5f) && f <= 337.5f) ? (f < 22.5f || f >= 67.5f) ? (f < 67.5f || f >= 112.5f) ? (f < 112.5f || f >= 157.5f) ? (f < 157.5f || f >= 202.5f) ? (f < 202.5f || f >= 247.5f) ? (f < 247.5f || f >= 292.5f) ? (f < 292.5f || f >= 337.5f) ? "" : "NW" : "W" : "SW" : "S" : "SE" : "E" : "NE" : "N";
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onConnectionSuspended(int i) {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_mode);

        this.image = (ImageView) findViewById(R.id.iv_compass_bg);
        this.tvHeading = (TextView) findViewById(R.id.tv_degree);
        this.large = (CardView) findViewById(R.id.large);

        this.mSensorManager = (SensorManager) getSystemService("sensor");
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StandardModeActivity.super.onBackPressed();

            }
        });
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), large, 1, null);

       /* ProgressDialog progressDialog = new ProgressDialog(this);
        this.delayer = progressDialog;
        progressDialog.setMessage(" Loading....");
        this.delayer.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                delayer.dismiss();
            }
        }, 2000);*/
        this.mTxtAltitude = (TextView) findViewById(R.id.tv_altitude);
        this.mTxtLongitude = (TextView) findViewById(R.id.tv_lang);
        this.mTxtLatitude = (TextView) findViewById(R.id.tv_lat);
        this.mTxtAddress = (TextView) findViewById(R.id.txt_address);
        this.mTxtAddress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scroll_anim));

        new Criteria();
        isGooglePlayServicesAvailable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= 9) {
            this.mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        }
        new Criteria();
        createLocationRequest();


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

    public void onResume() {
        super.onResume();

        SensorManager sensorManager = this.mSensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(3), 1);
        if (this.mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
        if (!this.locationManager.isProviderEnabled("gps")) {
            showGPSDisabledAlertToUser();
        }
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);

    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        this.mGoogleApiClient.connect();
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
            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (LocationListener) this);
        Log.d(TAG, "Location update stopped .......................");
    }

    public void createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        this.mLocationRequest = locationRequest;
        locationRequest.setInterval(60000);
        this.mLocationRequest.setFastestInterval(60000);
        this.mLocationRequest.setPriority(102);
    }

    public void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        float round = (float) Math.round(sensorEvent.values[0]);
        TextView textView = this.tvHeading;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(Float.toString(round));
        sb.append("° Nw");
        textView.setText(sb.toString());
        float f = -round;
        RotateAnimation rotateAnimation = new RotateAnimation(this.currentDegree, f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        this.image.startAnimation(rotateAnimation);
        this.currentDegree = f;
    }

    public void onLocationChanged(Location location) {
        Log.i("iamingf", " changed");
        new getData(this, location).execute(new Void[0]);
    }

    public static String formatDms(float f) {
        long j = (long) f;
        float f2 = f - ((float) j);
        long j2 = (long) (60.0f * f2);
        float f3 = (f2 - ((float) (j2 / 60))) * 3600.0f;
        return String.format(Locale.US, "%d°%d'\"", new Object[]{Long.valueOf(j), Long.valueOf(j2), Float.valueOf(f3)});
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 121);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr[0] != 0 || iArr[1] != 0) {
            Toast.makeText(this, "Permission Denide", Toast.LENGTH_SHORT).show();
        }
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

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}