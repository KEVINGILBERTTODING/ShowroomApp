package com.example.rizkimotor.features.transactions.admin.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.remote.ApiService;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.util.List;

public class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewAdapter.ViewHolder> {
    private Context context;
    private List<Pair<String, String>> photoList;
    private ItemClickListener itemClickListener;


    public PhotoViewAdapter(Context context,List<Pair<String, String>> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
     this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public PhotoViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_img_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewAdapter.ViewHolder holder, int position) {

        if (photoList.get(holder.getBindingAdapterPosition()).second.equals("credit")) {
            Glide.with(context)
                    .load(ApiService.END_POINT + "data/credit/" + photoList.get(holder.getBindingAdapterPosition()).first)
                    .into(holder.ivPhotoReview);
        }else if (photoList.get(holder.getBindingAdapterPosition()).second.equals("evidence")) {
            Glide.with(context)
                    .load(ApiService.END_POINT + "data/evidence/" + photoList.get(holder.getBindingAdapterPosition()).first)
                    .into(holder.ivPhotoReview);
        }

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhotoReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhotoReview = itemView.findViewById(R.id.ivReview);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.itemClickListener(Constants.REVIEW_IMAGE, getBindingAdapterPosition(),
                            photoList.get(getBindingAdapterPosition()));
                }
            });
        }
    }
}
