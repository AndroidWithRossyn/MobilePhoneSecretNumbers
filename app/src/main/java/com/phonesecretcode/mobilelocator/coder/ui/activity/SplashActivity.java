package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.adsmodule.api.AdsModule.Retrofit.APICallHandler.callAdsApi;
import static com.adsmodule.api.AdsModule.Utils.Global.checkAppVersion;
import static com.adsmodule.api.AdsModule.Utils.StringUtils.isNull;
import static com.phonesecretcode.mobilelocator.coder.singletonClass.AppOpenAds.currentActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Retrofit.AdsDataRequestModel;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.adsmodule.api.AdsModule.Utils.Global;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int visibilityFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(visibilityFlags);
        }
        window.setStatusBarColor(Color.TRANSPARENT);

        if (MyApplication.getConnection().isConnectingToInternet()) {
            callAdsApi(Constants.MAIN_BASE_URL, new AdsDataRequestModel(getPackageName(), ""), adsResponseModel -> {
                if (adsResponseModel != null) {
                    Constants.adsResponseModel = adsResponseModel;
                    if (!isNull(Constants.adsResponseModel.getMonetize_platform()))
                        Constants.platformList = Arrays.asList(Constants.adsResponseModel.getMonetize_platform().split(","));
                    if (checkAppVersion(adsResponseModel.getVersion_name(), currentActivity)) {
                        Global.showUpdateAppDialog(currentActivity);
                    } else {
                        AdUtils.buildAppOpenAdCache(currentActivity, Constants.adsResponseModel.getApp_open_ads().getAdx());
                        AdUtils.buildNativeCache(Constants.adsResponseModel.getNative_ads().getAdx(), currentActivity);
                        AdUtils.buildInterstitialAdCache(Constants.adsResponseModel.getInterstitial_ads().getAdx(), currentActivity);
                        AdUtils.buildRewardAdCache(Constants.adsResponseModel.getRewarded_ads().getAdx(), currentActivity);
                        AdUtils.showAppOpenAds(Constants.adsResponseModel.getApp_open_ads().getAdx(), currentActivity, (state_load) -> {
                            beginActivity();
                        });
                    }
                }
            });
        } else {
            new Handler().postDelayed(this::beginActivity, 1200);
        }
    }

    private void beginActivity() {
        startActivity(new Intent(currentActivity, MyApplication.getPreferences().isFirstRun() ? OnBoardingActivity.class : GetStartedActivity.class));
    }


}