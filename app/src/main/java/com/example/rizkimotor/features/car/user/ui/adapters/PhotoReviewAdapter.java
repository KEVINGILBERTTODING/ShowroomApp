package com.example.rizkimotor.features.car.user.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rizkimotor.R;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.listener.ClickListener;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.util.List;

public class PhotoReviewAdapter extends RecyclerView.Adapter<PhotoReviewAdapter.ViewHolder> {
    private Context context;
    private List<String> photoList;
    private ItemClickListener itemClickListener;


    public PhotoReviewAdapter(Context context, List<String> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
     this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public PhotoReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_img_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoReviewAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(photoList.get(position))
                .into(holder.ivPhotoReview);

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
