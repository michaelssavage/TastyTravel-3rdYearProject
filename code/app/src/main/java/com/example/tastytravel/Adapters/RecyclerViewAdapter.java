package com.example.tastytravel.Adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastytravel.Models.ListItem;
import com.example.tastytravel.Activities.PlaceInformation;
import com.example.tastytravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private DatabaseReference mDatabase;
    private FirebaseUser currentFirebaseUser;
    private List<ListItem> listItems;
    private OnPlaceListener mOnPlaceListener;
    private SparseBooleanArray mStateButtons = new SparseBooleanArray();


    public RecyclerViewAdapter(List<ListItem> listItems, OnPlaceListener onPlaceListener) {
        this.listItems = listItems;
        this.mOnPlaceListener = onPlaceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        View v;

        if(currentFirebaseUser == null){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_maps_results_layout_not_logged_in, parent, false);
        }
        else{
            mDatabase = FirebaseDatabase.getInstance().getReference().child(currentFirebaseUser.getUid());

            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_maps_results_layout, parent, false);
        }
        return new ViewHolder(v, mOnPlaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int currentPosition = holder.getAdapterPosition();
        ToggleButton button = holder.toggleButton;

        if(mStateButtons.valueAt(currentPosition)) {
            button.setChecked(true);
        } else {
            button.setChecked(false);
        }

        final ListItem listItem = listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());

        if(currentFirebaseUser != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStateButtons.put(currentPosition, true);

                    PlaceInformation place = new PlaceInformation(listItem.getHead(), listItem.getCoordinates());
                    mDatabase.child(listItem.getHead()).setValue(place);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return listItems.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewHead;
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
    public interface OnPlaceListener { void OnPlaceClick(int position);}
}