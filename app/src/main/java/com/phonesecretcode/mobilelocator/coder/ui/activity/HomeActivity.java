package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.getTipsTricksData;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextTTActivity;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityHomeBinding;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), binding.large, 1, null);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.bringToFront();

    }

    @Override
    protected void onResume() {
        super.onResume();

        closeDrawer();
        binding.btnMenu.setOnClickListener(v -> drawerOpen());
        binding.btnMobileCodes.setOnClickListener(v -> nextActivity(this, MobilesActivity.class));
        binding.btnFavourites.setOnClickListener(v -> nextActivity(this, FavouritesActivity.class));
        binding.callerId.setOnClickListener(v -> nextActivity(this, MobileSecretCodeActivity.class));
        binding.btnMobileTips.setOnClickListener(v -> nextTTActivity(this, getTipsTricksData().get(0)));
        binding.btnMobileTricks.setOnClickListener(v -> nextTTActivity(this, getTipsTricksData().get(1)));

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