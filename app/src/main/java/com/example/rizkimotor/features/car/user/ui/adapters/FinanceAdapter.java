package com.example.rizkimotor.features.car.user.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.FinanceModel;
import com.example.rizkimotor.util.contstans.Constants;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.util.List;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ViewHolder> {
    private Context context;
    private List<FinanceModel> financeModel;
    private ItemClickListener itemClickListener;

    public FinanceAdapter(Context context, List<FinanceModel> financeModel) {
        this.context = context;
        this.financeModel = financeModel;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public FinanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_finance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceAdapter.ViewHolder holder, int position) {

        holder.tvFinanceName.setText(financeModel.get(holder.getAbsoluteAdapterPosition()).getNama_finance());
        holder.tvBunga.setText("Bunga " + String.valueOf(financeModel.get(holder.getBindingAdapterPosition()).getBunga()) + "/ bulan");

        Glide.with(context)
                .load(financeModel.get(holder.getBindingAdapterPosition()).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(200, 200)
                .into(holder.ivFinance);

    }

    @Override
    public int getItemCount() {
        return financeModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFinanceName, tvBunga;
        private ImageView ivFinance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFinanceName = itemView.findViewById(R.id.tvFinanceName);
            tvBunga = itemView.findViewById(R.id.tvBunga);
            ivFinance = itemView.findViewById(R.id.ivFinance);

            itemView.setOnClickListener(view -> {
                if (itemView != null) {
                    itemClickListener.itemClickListener(Constants.FINANCE, getBindingAdapterPosition(), financeModel.get(getBindingAdapterPosition()));
                }
            });

        }
    }
}
