package com.example.rizkimotor.features.transactions.user.adapters;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rizkimotor.R;
import com.example.rizkimotor.util.listener.ClickListener;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private Context context;
    private List<Pair<String, String>> statusList;
    private ItemClickListener itemClickListener;
    private int selectedPosition = 0;

    public StatusAdapter(Context context, List<Pair<String, String>> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder holder, int position) {
        Pair<String, String> status = statusList.get(position);
        holder.tvStatus.setText(status.first);

        // Atur warna item berdasarkan apakah item saat ini dipilih
        if (position == selectedPosition) {
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.bg_second));
            holder.tvStatus.setTextColor(context.getColor(R.color.primary));
        } else {
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.card2));
            holder.tvStatus.setTextColor(context.getColor(R.color.black));
        }

        if (itemClickListener != null) {
            holder.tvStatus.setOnClickListener(view -> {
                selectedPosition =  position;
                itemClickListener.itemClickListener("status", position, status);
                notifyDataSetChanged();
            });
        }



    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatusName);
            cardView = itemView.findViewById(R.id.cvStatus);


        }
    }
}
