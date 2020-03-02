package com.example.tastytravel.Adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastytravel.Models.ListItem;
import com.example.tastytravel.Activities.PlaceInformation;
import com.example.tastytravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.toggleButton.setChecked(mStateButtons.get(position, false));

        final ListItem listItem = listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());

        if(currentFirebaseUser != null) {
            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PlaceInformation place = new PlaceInformation(listItem.getHead(), listItem.getCoordinates());

                    if(holder.toggleButton.isChecked()){
                        mStateButtons.put(position, true);
                        mDatabase.child(listItem.getHead()).setValue(place);
                    }
                    else   {
                        mStateButtons.put(position, false);
                        mDatabase.child(listItem.getHead()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mDatabase.child(listItem.getHead()).removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

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