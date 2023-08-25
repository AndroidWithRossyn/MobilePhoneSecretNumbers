package com.phonesecretcode.mobilelocator.coder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.phonesecretcode.mobilelocator.coder.databinding.ViewModelsListBinding;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;
import com.phonesecretcode.mobilelocator.coder.ui.activity.ModelsActivity;

import java.util.Objects;

public class ModelsListAdapter extends ListAdapter<DevicesModel, ModelsListAdapter.ContentViewHolder> {

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

    public ModelsListAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ModelsListAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewModelsListBinding binding = ViewModelsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ModelsListAdapter.ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelsListAdapter.ContentViewHolder holder, int position) {

        DevicesModel model = getItem(position);
        Log.d("ModelsListAdapter", "Model name: " + model.getName());
        holder.binding.tvName.setText(model.getName());

        int index = holder.getAdapterPosition()+1;

        if (index%4 == 0){
            AdUtils.showNativeAd((Activity) context, Constants.adsResponseModel.getNative_ads().getAdx(), holder.binding.adsView0, 2, null);
            holder.binding.adsView0.setVisibility(View.VISIBLE);
        } else holder.binding.adsView0.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), (Activity) context, isLoaded -> {
                context.startActivity(new Intent(context, ModelsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("model", model));
            });
        });

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ViewModelsListBinding binding;

        public ContentViewHolder(@NonNull ViewModelsListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
