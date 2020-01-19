package com.example.filter_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;


public class FilterView<T> extends LinearLayout {

    private int itemTextColor;
    private int itemBackgroundColor;
    private int selectedItemTextColor;
    private int selectedItemBackgroundColor;
    private int selectedItemTextSize;
    private AppCompatSpinner spinner;
    private AppCompatTextView title;
    private int spinnerItemRes;
    private ArrayAdapter<T> spinnerAdapter;
    private int selectedPosition;
    private OnItemSelectedListener onItemSelectedListener;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);

        LayoutInflater.from(getContext()).inflate(R.layout.filter_view, this, true);

        title = findViewById(R.id.fv_title);
        spinner = findViewById(R.id.fv_spinner);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FilterView, defStyleAttr, 0);

        try {

            title.setTextColor(a.getColor(R.styleable.FilterView_title_color, Color.GRAY));
            title.setText(a.getText(R.styleable.FilterView_title));
            title.setGravity(Gravity.CENTER_VERTICAL);
            title.setTextSize(a.getResourceId(R.styleable.FilterView_title_text_size, 14));

            spinnerItemRes = a.getResourceId(R.styleable.FilterView_spinner_item, 0);
            itemTextColor = a.getColor(R.styleable.FilterView_item_text_color, Color.GRAY);
            itemBackgroundColor = a.getColor(R.styleable.FilterView_item_background_color, Color.WHITE);

            selectedItemTextColor = a.getColor(R.styleable.FilterView_selected_item_text_color, Color.GRAY);
            selectedItemBackgroundColor = a.getColor(R.styleable.FilterView_selected_item_background_color, Color.TRANSPARENT);
            selectedItemTextSize = a.getDimensionPixelSize(R.styleable.FilterView_selected_item_text_size, 13);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }

        setupAdapter(spinnerItemRes);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    private void setupAdapter(int spinnerItemRes) {
        spinnerAdapter = new SpinnerAdapter(getContext(), spinnerItemRes, itemTextColor);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(spinnerAdapter.getItem(position), position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSpinnerItem(@LayoutRes int itemRes) {
        this.spinnerItemRes = itemRes;
        setupAdapter(spinnerItemRes);
    }

    /*public void setSelectedItem(String item) {
        if (!TextUtils.isEmpty(item)) {
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (item.equalsIgnoreCase(spinnerAdapter.getItem(i)))
                    setSelection(i);
            }
        }
    }*/

    public void setTitle(@StringRes int titleRes) {
        setTitle(getContext().getString(titleRes));
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setAdapter(ArrayAdapter<T> adapter) {
        spinner.setAdapter(spinnerAdapter = adapter);
    }

    public void setItems(List<T> items) {
        spinnerAdapter.addAll(items);
        spinnerAdapter.notifyDataSetChanged();
    }

    public void setSelection(int pos) {

        if (pos >= 0 && pos < spinnerAdapter.getCount()) {
            selectedPosition = pos;
        }

        if (selectedPosition >= 0 && selectedPosition < spinnerAdapter.getCount()) {
            spinner.setSelection(selectedPosition);
        }

    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T item, int position);
    }

    private class SpinnerAdapter<T> extends ArrayAdapter<String> {
        int textColor;

        SpinnerAdapter(@NonNull Context context, int resource, int color) {
            super(context, resource);
            this.textColor = color;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);
            try {
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextColor(textColor);
            } catch (Exception e) {
            }
            return v;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            v.setBackgroundColor(selectedItemBackgroundColor);
            try {
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextColor(selectedItemTextColor);
            } catch (Exception e) {
            }
            return v;
        }
    }
}
