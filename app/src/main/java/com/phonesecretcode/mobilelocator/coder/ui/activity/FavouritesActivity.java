package com.phonesecretcode.mobilelocator.coder.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.adapter.CodesListAdapter;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityFavouritesBinding;
import com.phonesecretcode.mobilelocator.coder.db.CodesViewModel;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class FavouritesActivity extends AppCompatActivity {

    ActivityFavouritesBinding binding;
    CodesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favourites);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.small, 2, null);
        viewModel = new ViewModelProvider(this).get(CodesViewModel.class);

        CodesListAdapter adapter = new CodesListAdapter();
        binding.recyclerView.setAdapter(adapter);

        viewModel.getFavouritesData().observe(this, data -> {
            if (data.size() > 0) {
                adapter.submitList(data);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.tvNot.setVisibility(View.GONE);
            } else {
                binding.tvNot.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }
}