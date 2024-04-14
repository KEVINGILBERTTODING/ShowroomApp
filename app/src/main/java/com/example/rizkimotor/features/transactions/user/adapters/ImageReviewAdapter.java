package com.example.rizkimotor.features.transactions.user.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rizkimotor.R;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.util.List;

public class ImageReviewAdapter extends RecyclerView.Adapter<ImageReviewAdapter.ViewHolder> {
    private Context context;
    private List<Uri> uriList;
    private ItemClickListener itemClickListener;

    public ImageReviewAdapter(Context context, List<Uri> uriList) {
        this.context = context;
        this.uriList = uriList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ImageReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_img_review_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageReviewAdapter.ViewHolder holder, int position) {
        holder.ivReview.setImageURI(uriList.get(holder.getBindingAdapterPosition()));




    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public void removeItem(int postion) {

        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion, getItemCount());
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvDelete;
        ImageView ivReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDelete = itemView.findViewById(R.id.cvDelete);
            ivReview = itemView.findViewById(R.id.ivReview);

            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.itemClickListener("delete_review", getBindingAdapterPosition(), null);
                }
            });

        }
    }
}
