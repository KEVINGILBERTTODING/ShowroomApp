package com.example.rizkimotor.features.home.user.ui.adapters.car;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

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
import com.example.rizkimotor.util.listener.ClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.zip.Inflater;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private Context context;
    private List<CarModel> carModels;
    private ClickListener clickListener;

    public CarAdapter(Context context, List<CarModel> carModels) {
        this.context = context;
        this.carModels = carModels;
    }

    public void onClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.ViewHolder holder, int position) {
        final int carPrice = carModels.get(holder.getBindingAdapterPosition()).getHarga_jual() - carModels.get(holder.getBindingAdapterPosition()).getDiskon();
        final int totalCredit = (int) carModels.get(holder.getBindingAdapterPosition()).getTotal_cicilan();


        holder.tvCarName.setText(carModels.get(holder.getBindingAdapterPosition()).getNama_model());

        holder.tvPrice.setText(formatRupiah(carPrice));





        // car booked
        if (carModels.get(holder.getBindingAdapterPosition()).getStatus_mobil() == 2) {
            holder.tvCarStatus.setVisibility(View.VISIBLE);
            holder.tvCarStatus.setText("Dipesan");

            Glide.with(context)
                    .load(carModels.get(holder.getBindingAdapterPosition()).getGambar1())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(bitmapTransform(new BlurTransformation(20)))
                    .into(holder.ivCar);

            // car sold out
        }else if (carModels.get(holder.getBindingAdapterPosition()).getStatus_mobil() == 0) {
            holder.tvCarStatus.setVisibility(View.VISIBLE);
            holder.tvCarStatus.setText("Terjual");

            Glide.with(context)
                    .load(carModels.get(holder.getBindingAdapterPosition()).getGambar1())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(bitmapTransform(new BlurTransformation(20)))
                    .into(holder.ivCar);

            // car available
        }else if (carModels.get(holder.getBindingAdapterPosition()).getStatus_mobil() == 1) {
            Glide.with(context)
                    .load(carModels.get(holder.getBindingAdapterPosition()).getGambar1())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivCar);

            holder.tvCarStatus.setVisibility(View.GONE);
            
        }else {
            Glide.with(context)
                    .load(carModels.get(holder.getBindingAdapterPosition()).getGambar1())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivCar);

            holder.tvCarStatus.setVisibility(View.GONE);
        }

        holder.tvCredit.setText(formatRupiah(totalCredit) + " / bulan");



    }

    public void destroyCar(int position) {
        carModels.remove(position);
        notifyDataSetChanged();
        notifyItemRangeRemoved(carModels.size(), getItemCount());
    }

    @Override
    public int getItemCount() {
        return carModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCar;
        private TextView tvPrice, tvCarName, tvCredit, tvCarStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCar = itemView.findViewById(R.id.ivCar);
            tvPrice = itemView.findViewById(R.id.tvCarPrice);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarStatus = itemView.findViewById(R.id.tvCarStatus);
            tvCredit = itemView.findViewById(R.id.tvCredit);


            itemView.setOnClickListener(view -> {
                if (clickListener != null) {
                    clickListener.onClickListener(getBindingAdapterPosition(), carModels.get(getBindingAdapterPosition()));
                }
            });


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
