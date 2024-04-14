package com.example.rizkimotor.features.transactions.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.BankAccountModel;

import java.util.List;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ViewHolder> {
    private Context context;
    private List<BankAccountModel> bankAccountModels;

    public BankAccountAdapter(Context context, List<BankAccountModel> bankAccountModels) {
        this.context = context;
        this.bankAccountModels = bankAccountModels;
    }

    @NonNull
    @Override
    public BankAccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_bank_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAccountAdapter.ViewHolder holder, int position) {
        holder.tvOwner.setText(bankAccountModels.get(holder.getBindingAdapterPosition()).getNama());
        holder.tvBankName.setText(bankAccountModels.get(holder.getBindingAdapterPosition()).getBank_name());
        holder.tvNumberAcc.setText(bankAccountModels.get(holder.getBindingAdapterPosition()).getNo_rekening());

    }

    @Override
    public int getItemCount() {
        return bankAccountModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBankName, tvNumberAcc, tvOwner;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvNumberAcc = itemView.findViewById(R.id.tvNumberAccount);
            tvOwner = itemView.findViewById(R.id.tvOwnerName);
        }
    }
}
