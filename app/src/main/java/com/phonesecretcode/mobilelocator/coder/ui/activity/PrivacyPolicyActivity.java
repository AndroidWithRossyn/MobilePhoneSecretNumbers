package com.phonesecretcode.mobilelocator.coder.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityPrivacyPolicyBinding;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class PrivacyPolicyActivity extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    PrivacyPolicyActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        activity = this;

        AdUtils.showNativeAd(activity, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView0, 2, null);

        binding.btnAgree.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(activity);
    }

}