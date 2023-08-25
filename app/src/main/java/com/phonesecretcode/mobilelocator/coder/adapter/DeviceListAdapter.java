package com.phonesecretcode.mobilelocator.coder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.phonesecretcode.mobilelocator.coder.databinding.ViewDeviceListBinding;
import com.phonesecretcode.mobilelocator.coder.models.DevicesModel;

import java.util.Objects;

public class DeviceListAdapter extends ListAdapter<DevicesModel, DeviceListAdapter.ContentViewHolder> {

    Context context;
    public static int selectedPosition = 0;
    public static int selectedID = -1;

    public DeviceListAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public DeviceListAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewDeviceListBinding binding = ViewDeviceListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.ContentViewHolder holder, int position) {
        DevicesModel model = getItem(position);

        if (selectedID == -1){
            selectedID = model.getID();
        }

        holder.binding.tvName.setText(model.getName());

        holder.itemView.post(()->holder.binding.tvName.setChecked(selectedID != -1 && selectedID == model.getID()));

        holder.binding.tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                selectedID = model.getID();
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
            }
        });

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ViewDeviceListBinding binding;
        public ContentViewHolder(@NonNull ViewDeviceListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

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

}
