package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.PrivacyLink;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.shareApp;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.showRateApp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityGetStartedBinding;
import com.phonesecretcode.mobilelocator.coder.databinding.DialogExitBinding;
import com.phonesecretcode.mobilelocator.coder.db.DBWorker;
import com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication;

public class GetStartedActivity extends AppCompatActivity {
    ActivityGetStartedBinding binding;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_started);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large, 1, null);

        if (MyApplication.getPreferences().isFirstRun()) {
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DBWorker.class).build();
            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            MyApplication.getPreferences().setFirstRun(false);
        }

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.bringToFront();

    }

    @Override
    protected void onResume() {
        super.onResume();

        closeDrawer();

        binding.btnBack.setOnClickListener(v -> GetStartedActivity.this.closeDrawer());
        binding.btnMobileCodes.setOnClickListener(v -> nextActivity(GetStartedActivity.this, MobilesActivity.class));
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(GetStartedActivity.this, TMCActivity.class);
        });
        binding.btnMenu.setOnClickListener(v -> drawerOpen());
        binding.getStarted.setOnClickListener(v -> nextActivity(this, HomeActivity.class));
        binding.btnBack.setOnClickListener(v -> GetStartedActivity.this.closeDrawer());
        binding.btnMobileCodes.setOnClickListener(v -> nextActivity(GetStartedActivity.this, MobilesActivity.class));
        binding.btnCurrentAddress.setOnClickListener(v -> nextActivity(GetStartedActivity.this, CurrentAddressActivity.class));
        binding.btnCompass.setOnClickListener(v -> nextActivity(GetStartedActivity.this, CompassActivity.class));
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
               /* final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            }
        });
        binding.btnPrivacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PrivacyLink));
            startActivity(intent);
        });
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(GetStartedActivity.this, TMCActivity.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(GetStartedActivity.this);
            DialogExitBinding bind = DialogExitBinding.inflate(LayoutInflater.from(GetStartedActivity.this));
            builder.setView(bind.getRoot());
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), bind.large, 1, null);
            bind.btnExit.setOnClickListener(v -> {
                dialog.dismiss();
                finishAffinity();
                System.exit(0);
            });
            bind.btnClose.setOnClickListener(v -> dialog.dismiss());
            bind.btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRateApp();
                }
            });

        }

    }

}
