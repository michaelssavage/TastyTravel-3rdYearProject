package com.example.tastytravel.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastytravel.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private OnPlaceListener mOnPlaceListener;

    public RecyclerViewAdapter(List<ListItem> listItems, OnPlaceListener onPlaceListener) {
        this.listItems = listItems;
        this.mOnPlaceListener = onPlaceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_maps_results_layout, parent, false);
        return new ViewHolder(v, mOnPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewHead;
        OnPlaceListener onPlaceListener;

        public ViewHolder(@NonNull View itemView, OnPlaceListener onPlaceListener) {
            super(itemView);
            textViewHead = itemView.findViewById(R.id.textViewHead);
            this.onPlaceListener = onPlaceListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlaceListener.OnPlaceClick(getAdapterPosition());
        }
    }

    public interface OnPlaceListener {
        void OnPlaceClick(int position);
    }
}