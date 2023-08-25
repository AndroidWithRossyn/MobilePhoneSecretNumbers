package com.phonesecretcode.mobilelocator.coder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.databinding.ViewDeviceCardListBinding;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;
import com.phonesecretcode.mobilelocator.coder.ui.activity.ModelsActivity;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MobileListAdapter extends ListAdapter<DevicesModel, MobileListAdapter.ContentViewHolder> {

    static DiffUtil.ItemCallback<DevicesModel> diffCallback = new DiffUtil.ItemCallback<DevicesModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DevicesModel oldItem, @NonNull DevicesModel newItem) {
            return Objects.equals(oldItem.getName(), newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DevicesModel oldItem, @NonNull DevicesModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
    Context context;

    public MobileListAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MobileListAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewDeviceCardListBinding binding = ViewDeviceCardListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MobileListAdapter.ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileListAdapter.ContentViewHolder holder, int position) {
        DevicesModel model = getItem(position);

        holder.binding.tvName.setText(model.getName());
        int imageResId = getImageList().get(holder.getAdapterPosition());
        holder.binding.image.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> {
            Utility.SELECTED_MODEL = model;
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), (Activity) context, isLoaded -> {
                context.startActivity(new Intent(context, ModelsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("model", model));
            });
        });

    }

    public List<Integer> getImageList() {
        List<Integer> list = new ArrayList<>();

        list.add(R.drawable.ic_phone);
        list.add(R.drawable.ic_phone_green);
        list.add(R.drawable.ic_phone_blue);
        list.add(R.drawable.ic_phone_yellow);
        list.add(R.drawable.ic_phone_purple);
        list.add(R.drawable.ic_phone_red);
        list.add(R.drawable.ic_phone);
        list.add(R.drawable.ic_phone_green);
        list.add(R.drawable.ic_phone_blue);
        list.add(R.drawable.ic_phone_yellow);
        list.add(R.drawable.ic_phone_purple);
        list.add(R.drawable.ic_phone_red);
        list.add(R.drawable.ic_phone);
        list.add(R.drawable.ic_phone_green);
        list.add(R.drawable.ic_phone_blue);

    /*    int randomIndex;
        if (adapterPosition >= list.size())
            randomIndex = adapterPosition - list.size();
        else
            randomIndex = adapterPosition;*/

        return list;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ViewDeviceCardListBinding binding;

        public ContentViewHolder(@NonNull ViewDeviceCardListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
