package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.getTipsTricksData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.adapter.TipsTrickListAdapter;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityTipsTricksBinding;
import com.phonesecretcode.mobilelocator.coder.models.MobileModels;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class TipsTricksActivity extends AppCompatActivity {

    ActivityTipsTricksBinding binding;
    int span;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tips_tricks);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView0, 2, null);

        MobileModels model = (MobileModels) getIntent().getSerializableExtra("model");
        span = getIntent().getIntExtra("span", 1);

        binding.tvTitle.setText(model.getTitle().replace(":", ""));

        binding.recyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);

        setAdapter(model);

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    private void setAdapter(MobileModels models) {

//        if (span == 2){
            AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView, 1, null);
//        }

        List<MobileModels> list = new ArrayList<>();
        for (MobileModels data : getTipsTricksData()) {
            if (data.getParentID() == models.getID()) {
                list.add(data);
            }
        }
//        list.sort(Comparator.comparing(MobileModels::getTitle));
        if (list.size() > 0) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), span));
            TipsTrickListAdapter adapter = new TipsTrickListAdapter(span);
            adapter.submitList(list);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.tvDesc.setText(models.getDesc());
            binding.tvDesc.setVisibility(View.VISIBLE);
            AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView, 1, null);
        }
    }

}