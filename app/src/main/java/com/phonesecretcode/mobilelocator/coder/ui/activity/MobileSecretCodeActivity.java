package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.PrivacyLink;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.shareApp;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.showRateApp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityMobileSecretCodeBinding;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class MobileSecretCodeActivity extends AppCompatActivity {
    ActivityMobileSecretCodeBinding binding;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile_secret_code);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large, 1, null);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.small, 2, null);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.bringToFront();

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.btnBack.setOnClickListener(v -> MobileSecretCodeActivity.this.closeDrawer());
        binding.btnMobileCodes.setOnClickListener(v -> nextActivity(MobileSecretCodeActivity.this, MobilesActivity.class));
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(MobileSecretCodeActivity.this, TMCActivity.class);
        });
        binding.btnMenu.setOnClickListener(v -> drawerOpen());
        closeDrawer();
        binding.mobileSecretCodeId.setOnClickListener(v -> nextActivity(this, HomeActivity.class));
        binding.btnCurrentAddress.setOnClickListener(v -> nextActivity(this, CurrentAddressActivity.class));
        binding.btnCompass.setOnClickListener(v -> nextActivity(this, CompassActivity.class));
        binding.btnTraffic.setOnClickListener(v -> nextActivity(this, TrafficActivity.class));

        binding.btnCurrentAddress1.setOnClickListener(v -> nextActivity(this, CurrentAddressActivity.class));
        binding.btnCompass1.setOnClickListener(v -> nextActivity(this, CompassActivity.class));
        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                shareApp();
            }
        });
        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateApp();
            }
        });
        binding.btnPrivacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PrivacyLink));
            startActivity(intent);
        });
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(this, TMCActivity.class);
        });
    }

    public void drawerOpen() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else binding.drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else {
            Utility.finishActivity(this);
        }
    }
}