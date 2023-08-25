package com.phonesecretcode.mobilelocator.coder.adapter;

import static com.phonesecretcode.mobilelocator.coder.singletonClass.MyApplication.getCodesDao;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.phonesecretcode.mobilelocator.coder.R;

import com.phonesecretcode.mobilelocator.coder.databinding.ViewCodesListBinding;
import com.phonesecretcode.mobilelocator.coder.models.CodesModel;
import com.phonesecretcode.mobilelocator.coder.utils.Utility;

import java.util.Objects;

public class CodesListAdapter extends ListAdapter<CodesModel, CodesListAdapter.ContentViewHolder> {

    static DiffUtil.ItemCallback<CodesModel> diffCallback = new DiffUtil.ItemCallback<CodesModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CodesModel oldItem, @NonNull CodesModel newItem) {
            return Objects.equals(oldItem.getCode(), newItem.getCode()) || Objects.equals(oldItem.isFavourite(), newItem.isFavourite());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CodesModel oldItem, @NonNull CodesModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
    Context context;

    public CodesListAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public CodesListAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewCodesListBinding binding = ViewCodesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CodesListAdapter.ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CodesListAdapter.ContentViewHolder holder, int position) {
        CodesModel model = getItem(position);

        holder.binding.tvCode.setText(model.getCode());
        holder.binding.tvDesc.setText(model.getCodeDesc());

        if (model.isFavourite()) holder.binding.btnLike.setImageResource(R.drawable.ic_liked);
        else holder.binding.btnLike.setImageResource(R.drawable.ic_not_liked);

        holder.binding.btnLike.setOnClickListener(v -> {
            model.setFavourite(!model.isFavourite());
            AsyncTask.execute(() -> getCodesDao().updateData(model));
        });
        holder.binding.btnShare.setOnClickListener(v -> {
            Utility.shareMessage(model);
        });
        holder.binding.btnCopy.setOnClickListener(v -> {
            Utility.copy(context, model.getCode());
        });

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ViewCodesListBinding binding;

        public ContentViewHolder(@NonNull ViewCodesListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
