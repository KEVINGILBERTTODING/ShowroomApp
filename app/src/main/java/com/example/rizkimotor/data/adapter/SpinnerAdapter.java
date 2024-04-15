package com.example.rizkimotor.data.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.rizkimotor.data.model.MerkModel;

import java.util.List;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {

    // Konstruktor SpinnerAdapter
    public SpinnerAdapter(@NonNull Context context, @NonNull List<T> itemList) {

        super(context, android.R.layout.simple_spinner_item, itemList);

        // Mengatur tata letak dropdown untuk adapter
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}