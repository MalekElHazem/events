package com.example.android_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;  // Changed from Button
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder> {
    private List<Guest> guestList;
    private OnGuestClickListener onGuestClickListener;

    public interface OnGuestClickListener {
        void onEditClick(String guestId);
        void onDeleteClick(String guestId);
    }

    public GuestAdapter(List<Guest> guestList, OnGuestClickListener onGuestClickListener) {
        this.guestList = guestList;
        this.onGuestClickListener = onGuestClickListener;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_card, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        Guest guest = guestList.get(position);
        holder.guestNameText.setText(guest.getName());
        holder.contactView.setText(guest.getContact());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditGuestActivity.class);
            intent.putExtra("guestId", guest.getGuestId());
            intent.putExtra("name", guest.getName());
            intent.putExtra("contact", guest.getContact());
            v.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> 
            onGuestClickListener.onDeleteClick(guest.getGuestId()));
    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    public static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView guestNameText;
        TextView contactView;
        ImageButton editButton;    // Changed to ImageButton
        ImageButton deleteButton;  // Changed to ImageButton

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            guestNameText = itemView.findViewById(R.id.guestName);
            contactView = itemView.findViewById(R.id.guestContact);
            editButton = itemView.findViewById(R.id.editButton);    // Make sure IDs match in layout
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}