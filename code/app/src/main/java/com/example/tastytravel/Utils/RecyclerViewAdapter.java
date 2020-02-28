package com.example.tastytravel.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastytravel.MapsActivity;
import com.example.tastytravel.PlaceInformation;
import com.example.tastytravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private DatabaseReference mDatabase;

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

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(currentFirebaseUser.getUid());

        return new ViewHolder(v, mOnPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());

        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // IMPLEMENT LOGIC HERE TO SAVE PLACE
                PlaceInformation place = new PlaceInformation(listItem.getHead(), listItem.getCoordinates());
                mDatabase.child(listItem.getHead()).setValue(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewHead;
        OnPlaceListener onPlaceListener;
        ToggleButton toggleButton;

        public ViewHolder(@NonNull View itemView, OnPlaceListener onPlaceListener) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.button_favorite);
            textViewHead = itemView.findViewById(R.id.textViewHead);
            this.onPlaceListener = onPlaceListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPlaceListener.OnPlaceClick(getAdapterPosition());
        }
    }

    // Interface when a location result is clicked
    public interface OnPlaceListener {
        void OnPlaceClick(int position);
    }
}