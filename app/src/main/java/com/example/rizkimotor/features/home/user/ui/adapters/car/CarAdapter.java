package com.example.rizkimotor.features.home.user.ui.adapters.car;

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
import com.example.rizkimotor.data.model.CarModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.zip.Inflater;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private Context context;
    private List<CarModel> carModels;

    public CarAdapter(Context context, List<CarModel> carModels) {
        this.context = context;
        this.carModels = carModels;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.ViewHolder holder, int position) {
        final int carPrice = carModels.get(holder.getAdapterPosition()).getHarga_jual() - carModels.get(holder.getAdapterPosition()).getDiskon();
        final int totalCredit = (int) carModels.get(holder.getAdapterPosition()).getTotal_cicilan();


        holder.tvCarName.setText(carModels.get(holder.getAdapterPosition()).getNama_model());

        holder.tvPrice.setText(formatRupiah(carPrice));

        Glide.with(context)
                .load(carModels.get(holder.getAdapterPosition()).getGambar1())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivCar);

        holder.tvCredit.setText(formatRupiah(totalCredit) + " / bulan");

    }

    @Override
    public int getItemCount() {
        return carModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCar;
        private TextView tvPrice, tvCarName, tvCredit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvPrice = itemView.findViewById(R.id.tvCarPrice);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCredit = itemView.findViewById(R.id.tvCredit);
        }
    }

    public static String formatRupiah(int nominal) {
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
