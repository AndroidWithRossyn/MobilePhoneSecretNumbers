package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.getDevices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.adapter.MobileListAdapter;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityMobilesBinding;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MobilesActivity extends AppCompatActivity {

    ActivityMobilesBinding binding;
    MobileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobiles);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView0, 2, null);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView, 1, null);

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new MobileListAdapter();
        binding.recyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);

        setAdapter();

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    searchValue(s.toString());
                }
                else setAdapter();
            }
        });


    }

    private void searchValue(String text) {
        List<DevicesModel> list = new ArrayList<>();
        for (DevicesModel model : adapter.getCurrentList()) {
            if (model.getName().toLowerCase().contains(text.toLowerCase()))
                list.add(model);
        }
        adapter.submitList(list);
    }


    private void setAdapter() {
        List<DevicesModel> list = new ArrayList<>();
        for (DevicesModel data : getDevices()) {
            if (data.getParentID() == 1 || data.getParentID() == 2) {
                list.add(data);
            }
        }
        list.sort(Comparator.comparing(DevicesModel::getName));
        if (list.size() > 0) {
            adapter.submitList(list);
            binding.recyclerView.setAdapter(adapter);
        }
    }

}