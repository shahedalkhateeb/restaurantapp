package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    //  تعريف interface مرة واحدة فقط
    public interface OnFilterAppliedListener {
        void onFilterApplied(String category, double maxPrice, String sortOrder);
    }

    private Spinner categorySpinner;
    private SeekBar priceSeekBar;
    private TextView priceLabel;
    private Button applyButton;
    private RadioGroup sortGroup;

    private String selectedCategory = "";
    private double maxPrice = 100;
    private String sortOrder = "none";

    private OnFilterAppliedListener listener;

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        priceSeekBar = view.findViewById(R.id.priceSeekBar);
        priceLabel = view.findViewById(R.id.priceLabel);
        applyButton = view.findViewById(R.id.applyButton);
        sortGroup = view.findViewById(R.id.sortGroup);

        // التصنيفات
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList("All", "Starters", "Main Course", "Desserts", "Drinks"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        //   السعر
        priceSeekBar.setMax(100);
        priceSeekBar.setProgress(100);
        priceLabel.setText("Max Price: ₪100");

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPrice = progress;
                priceLabel.setText("Max Price: ₪" + progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //  الترتيب حسب السعر
        sortGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sortLowToHigh) {
                sortOrder = "asc";
            } else if (checkedId == R.id.sortHighToLow) {
                sortOrder = "desc";
            } else {
                sortOrder = "none";
            }
        });

        //  زر  الفلاتر
        applyButton.setOnClickListener(v -> {
            selectedCategory = categorySpinner.getSelectedItem().toString();
            if (listener != null) {
                listener.onFilterApplied(selectedCategory, maxPrice, sortOrder);
            }
            dismiss();
        });

        return view;
    }
}


