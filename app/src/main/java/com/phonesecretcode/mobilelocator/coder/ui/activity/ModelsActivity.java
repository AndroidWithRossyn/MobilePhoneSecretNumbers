package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.SELECTED_MODEL;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.getDevices;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.adapter.CodesListAdapter;
import com.phonesecretcode.mobilelocator.coder.adapter.ModelsListAdapter;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityModelsBinding;
import com.phonesecretcode.mobilelocator.coder.db.CodesViewModel;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModelsActivity extends AppCompatActivity {

    ActivityModelsBinding binding;
    CodesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_models);

        viewModel = new ViewModelProvider(this).get(CodesViewModel.class);
        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView0, 2, null);

        DevicesModel model = (DevicesModel) getIntent().getSerializableExtra("model");

        binding.tvTitle.setText(model.getName());

//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);

        setAdapter(model.getID());

        binding.btnBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }

    private void setAdapter(int parentID) {
        List<DevicesModel> list = new ArrayList<>();
        for (DevicesModel data : getDevices()) {
            if (data.getParentID() == parentID) {
                list.add(data);
            }
        }
        list.sort(Comparator.comparing(DevicesModel::getName));
        if (list.size() > 0) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            ModelsListAdapter adapter = new ModelsListAdapter();
            adapter.submitList(list);
            binding.recyclerView.setAdapter(adapter);
        } else {
            AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.adsView, 1, null);

            binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            CodesListAdapter adapter = new CodesListAdapter();
            binding.recyclerView.setAdapter(adapter);
            viewModel.getAllData(SELECTED_MODEL.getName()).observe(this, data -> {
                if (data.size() > 0) {
                    adapter.submitList(data);
                } else Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
            });
        }
    }

}