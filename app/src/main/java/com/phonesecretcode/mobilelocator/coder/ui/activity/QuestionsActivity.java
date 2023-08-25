package com.phonesecretcode.mobilelocator.coder.ui.activity;

import static com.phonesecretcode.mobilelocator.coder.adapter.DeviceListAdapter.selectedID;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.getDevices;
import static com.phonesecretcode.mobilelocator.coder.utils.Utility.nextFinishActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.adapter.DeviceListAdapter;
import com.phonesecretcode.mobilelocator.coder.databinding.ActivityQuestionsBinding;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";
    ActivityQuestionsBinding binding;
    int position = 0, ID = -1;
    DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions);

        setOnBoardingIndicator();
        setCurrentOnBoardingIndicators(position);

        setAdapter(position);

    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.btnNext.setOnClickListener(v -> {
            if (position == 0) {
                position++;
                setCurrentOnBoardingIndicators(position);
                binding.tvQuestion.setText(getString(R.string.ques2));
//                binding.searchCard.setVisibility(View.VISIBLE);
                setAdapter(selectedID);
            } else {
                nextFinishActivity(this, HomeActivity.class);
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ID == -1) ID = selectedID;
                if (s.length() != 0) {
                    searchValue(s.toString());
                }
                else setAdapter(ID);
            }
        });

    }

    private void searchValue(String text) {
        List<DevicesModel> list = new ArrayList<>();
        for (DevicesModel model : adapter.getCurrentList()) {
            if (selectedID == model.getID() || model.getName().toLowerCase().contains(text.toLowerCase())){
                list.add(model);
            }
        }
        adapter = new DeviceListAdapter();
        adapter.submitList(list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setAdapter(int position) {
        selectedID = -1;
        List<DevicesModel> list = new ArrayList<>();
        for (DevicesModel data : getDevices()) {
            if (data.getParentID() == position) {
                list.add(data);
            }
        }
        list.sort(Comparator.comparing(DevicesModel::getName));
        if (list.size() > 0) {
            adapter = new DeviceListAdapter();
            adapter.submitList(list);
            binding.recyclerView.setAdapter(adapter);
        }
    }

    private void setOnBoardingIndicator() {
        ImageView[] indicators = new ImageView[2];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            indicators[i].setLayoutParams(layoutParams);
            binding.dotsLayout.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnBoardingIndicators(int index) {
        int childCount = binding.dotsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.dotsLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            }
            binding.tvTitle.setText((index + 1) + ". Question");
        }
    }

}