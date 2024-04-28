package com.example.rizkimotor.features.transactions.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rizkimotor.R;
import com.example.rizkimotor.data.model.TransactionModel;
import com.example.rizkimotor.util.listener.ItemClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminTransactionHistoryAdapter extends RecyclerView.Adapter<AdminTransactionHistoryAdapter.ViewHolder> {
    private Context context;
    private List<TransactionModel> transactionModels;
    private ItemClickListener itemClickListener;

    public AdminTransactionHistoryAdapter(Context context, List<TransactionModel> transactionModels) {
        this.context = context;
        this.transactionModels = transactionModels;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    @NonNull
    @Override
    public AdminTransactionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_history_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTransactionHistoryAdapter.ViewHolder holder, int position) {

       holder.tvCarName.setText(transactionModels.get(holder.getBindingAdapterPosition()).getNama_model());
       holder.tvMerk.setText(transactionModels.get(holder.getBindingAdapterPosition()).getMerk());
       holder.tvDateTrans.setText(convertDate(transactionModels.get(holder.getBindingAdapterPosition()).getCreated_at()));

       setCardStatus(holder.cvStatus, transactionModels.get(holder.getBindingAdapterPosition()).getStatus(), holder.tvStatus);

        Glide.with(context).load(transactionModels.get(holder.getBindingAdapterPosition()).getGambar1())
                .override(80, 80)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCar);

        holder.tvNominal.setText(formatRupiah(transactionModels.get(holder.getBindingAdapterPosition()).getTotal_pembayaran()));

        if (transactionModels.get(holder.getBindingAdapterPosition()).getPayment_method().equals("1")) {
            holder.tvPaymentMethod.setText("Tunai");
        } else if (transactionModels.get(holder.getBindingAdapterPosition()).getPayment_method().equals("2")) {
            holder.tvPaymentMethod.setText("Kredit");

        }else if (transactionModels.get(holder.getBindingAdapterPosition()).getPayment_method().equals("3")) {
            holder.tvPaymentMethod.setText("Transfer");

        }else {
            holder.tvPaymentMethod.setText("-");
        }


    }

    @Override
    public int getItemCount() {
        return transactionModels.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentMethod, tvDateTrans, tvStatus, tvCarName, tvMerk, tvNominal;
        ImageView ivCar;
        CardView cvStatus;
        Button btnReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvDateTrans = itemView.findViewById(R.id.tvDateTrans);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvMerk = itemView.findViewById(R.id.tvMerk);
            cvStatus = itemView.findViewById(R.id.cvStatus);
            tvNominal = itemView.findViewById(R.id.tvTotalTransaction);
            btnReview = itemView.findViewById(R.id.btnReview);



            itemView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.itemClickListener("history", getBindingAdapterPosition(), transactionModels.get(getBindingAdapterPosition()));
                }
            });

        }
    }

    public void deleteTransaction(int position) {
        transactionModels.remove(position);
        notifyDataSetChanged();
        notifyItemRangeRemoved(transactionModels.size(), getItemCount());
    }

    public static String convertDate(String isoDate) {
        // Format input: ISO 8601 (contoh: "2024-01-11T16:15:01.000000Z")
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        // Format output: Contoh "1 Maret 2024"
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

        try {
            // Parsing tanggal dari format input
            Date date = inputFormat.parse(isoDate);

            // Mengubah tanggal ke format output
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Tangani parsing error jika format input tidak sesuai
            e.printStackTrace();
            return null; // Kembalikan null jika ada error
        }
    }

    private void setCardStatus(CardView cardStatus, int status, TextView textView) {
        if (status == 1) {
            cardStatus.setCardBackgroundColor(context.getColor(R.color.soft_green));
            textView.setTextColor(context.getColor(R.color.green));
            textView.setText("Selesai");


        }else  if (status == 2) {
            cardStatus.setCardBackgroundColor(context.getColor(R.color.bg_second));
            textView.setTextColor(context.getColor(R.color.primary));
            textView.setText("Proses");

        }else  if (status == 3) {
            cardStatus.setCardBackgroundColor(context.getColor(R.color.soft_blue));
            textView.setTextColor(context.getColor(R.color.blue));
            textView.setText("Proses Finance");

        }else  if (status == 0) {
            cardStatus.setCardBackgroundColor(context.getColor(R.color.soft_red));
            textView.setTextColor(context.getColor(R.color.red));
            textView.setText("Tidak Valid");

        }
    }

    public void updateState(int position, int status) {
        transactionModels.get(position).setStatus(status);
        notifyDataSetChanged();
    }

    public static String formatRupiah(long nominal) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        // Mengatur jumlah digit desimal menjadi 0
        kursIndonesia.setMaximumFractionDigits(0);


        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nominal);
    }

}


