package com.phonesecretcode.mobilelocator.coder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adsmodule.api.AdsModule.AdUtils;
import com.adsmodule.api.AdsModule.Utils.Constants;
import com.bumptech.glide.Glide;
import com.phonesecretcode.mobilelocator.coder.databinding.ViewTipsTricksListBinding;
import com.phonesecretcode.mobilelocator.coder.models.MobileModels;
import com.phonesecretcode.mobilelocator.coder.ui.activity.TipsTricksActivity;

import java.util.Objects;

public class TipsTrickListAdapter extends ListAdapter<MobileModels, TipsTrickListAdapter.ContentViewHolder> {

    static DiffUtil.ItemCallback<MobileModels> diffCallback = new DiffUtil.ItemCallback<MobileModels>() {
        @Override
        public boolean areItemsTheSame(@NonNull MobileModels oldItem, @NonNull MobileModels newItem) {
            return Objects.equals(oldItem.getTitle(), newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MobileModels oldItem, @NonNull MobileModels newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
    Context context;
    int viewType;

    public TipsTrickListAdapter(int type) {
        super(diffCallback);
        viewType = type;
    }

    @NonNull
    @Override
    public TipsTrickListAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewTipsTricksListBinding binding = ViewTipsTricksListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsTrickListAdapter.ContentViewHolder holder, int position) {
        MobileModels model = getItem(position);

        if (viewType == 1) {
            holder.binding.tvTitle.setText(model.getTitle());
            Glide.with(context).load(context.getDrawable(model.getExtra())).into(holder.binding.imageIcon1);
            /*holder.binding.imageIcon1.setText(model.getTitle());*/
//            holder.binding.tvDesc.setText(model.getDesc());
//            holder.binding.cardBg.setImageResource(model.getExtra());
//            holder.binding.mainCard.setStrokeColor(context.getColor(model.getExtra()));
//            holder.binding.sideColor.setImageTintList(ColorStateList.valueOf(context.getColor(model.getExtra())));
//            holder.binding.sideArrow.setImageTintList(ColorStateList.valueOf(context.getColor(model.getExtra())));
            holder.binding.mainCard.setVisibility(View.VISIBLE);
            int index = holder.getAdapterPosition()+1;
            if (index%4 == 0){
                AdUtils.showNativeAd((Activity) context, Constants.adsResponseModel.getNative_ads().getAdx(), holder.binding.adsView0, 1, null);
                holder.binding.adsView0.setVisibility(View.VISIBLE);
            } else holder.binding.adsView0.setVisibility(View.GONE);

        } else {
            holder.binding.tvViewTitle.setText(model.getTitle());
            Glide.with(context).load(context.getDrawable(model.getExtra())).into(holder.binding.imageIcon);
            holder.binding.viewCard.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            AdUtils.showInterstitialAd(Constants.adsResponseModel.getInterstitial_ads().getAdx(), (Activity) context, isLoaded -> {
                context.startActivity(new Intent(context, TipsTricksActivity.class)
                        .putExtra("model", model)
                        .putExtra("span", 2));
            });
        });

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ViewTipsTricksListBinding binding;

        public ContentViewHolder(@NonNull ViewTipsTricksListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
