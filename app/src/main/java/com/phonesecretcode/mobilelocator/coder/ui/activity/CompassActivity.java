package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

public class CompassActivity extends AppCompatActivity {
    TextView standar_mode,camera_mode,night_mode,telescope_mode;
    ImageView back_btn;
    CardView large;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        standar_mode=findViewById(R.id.standard_mode);
        camera_mode=findViewById(R.id.camera_mode);
        night_mode=findViewById(R.id.night_mode);
        telescope_mode=findViewById(R.id.telescope_mode);
        back_btn=findViewById(R.id.back_btn);
        large=findViewById(R.id.large);

        AdUtils.showNativeAd(this, Constants.adsResponseModel.getNative_ads().getAdx(), large, 1, null);
        standar_mode.setOnClickListener(v -> nextActivity(this, StandardModeActivity.class));
        camera_mode.setOnClickListener(v -> nextActivity(this, CameraModeActivity.class));
        night_mode.setOnClickListener(v -> nextActivity(this, NightModeActivity.class));
        telescope_mode.setOnClickListener(v -> nextActivity(this, TelescopeModeActivity.class));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.finishActivity(CompassActivity.this);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Utility.finishActivity(this);
    }
}