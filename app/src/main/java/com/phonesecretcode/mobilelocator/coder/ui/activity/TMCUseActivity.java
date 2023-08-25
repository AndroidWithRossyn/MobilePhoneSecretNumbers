package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.PrivacyLink;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextFinishActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityTmcuseBinding;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class TMCUseActivity extends AppCompatActivity {

    ActivityTmcuseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tmcuse);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView0, 2, null);

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnPrivacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PrivacyLink));
            startActivity(intent);
        });
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(this, TMCActivity.class);
        });

        binding.btnAgree.setOnClickListener(view -> {
            nextFinishActivity(this, GetStartedActivity.class);
        });

    }

}