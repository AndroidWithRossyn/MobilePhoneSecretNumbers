package com.phonesecretcode.mobilelocator.coder.ui.fragments;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.PrivacyLink;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.shareApp;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.showRateApp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.FragmentNavBinding;
import com.phonesecretcode.mobilelocator.coder.ui.activity.CompassActivity;
import com.phonesecretcode.mobilelocator.coder.ui.activity.CurrentAddressActivity;
import com.phonesecretcode.mobilelocator.coder.ui.activity.HomeActivity;
import com.phonesecretcode.mobilelocator.coder.ui.activity.MobilesActivity;
import com.phonesecretcode.mobilelocator.coder.ui.activity.TMCActivity;

public class NavFragment extends Fragment {

    FragmentNavBinding binding;
    HomeActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nav, container, false);

        activity = (HomeActivity) requireActivity();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.btnBack.setOnClickListener(v -> activity.closeDrawer());
        binding.btnMobileCodes.setOnClickListener(v -> nextActivity(activity, MobilesActivity.class));
        binding.btnCurrentAddress.setOnClickListener(v -> nextActivity(activity, CurrentAddressActivity.class));
        binding.btnCompass.setOnClickListener(v -> nextActivity(activity, CompassActivity.class));

        binding.btnPrivacy.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(PrivacyLink));
            startActivity(intent);
        });
        binding.btnTmc.setOnClickListener(view -> {
            nextActivity(activity, TMCActivity.class);
        });
        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRateApp();
                /*final String appPackageName = activity.getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            }
        });

    }

}