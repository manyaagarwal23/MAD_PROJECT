package com.example.unimingle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class
Event_Adapter extends RecyclerView.Adapter<Event_Adapter.EventViewHolder> {

    Context context;
    List<Event_Model> eventList;

    public Event_Adapter(Context context, List<Event_Model> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event_Model event = eventList.get(position);
        holder.imgEvent.setImageResource(event.imageResId);
        holder.txtTitle.setText(event.title);
        holder.txtLocation.setText(event.location);
        holder.txtDate.setText(event.date);
        holder.txtDescription.setText(event.description);
        holder.txtPeople.setText(event.currentPeople + "/   " + event.totalPeople);

        // Optional: Set click listeners for join, skip, share
        holder.btnJoin.setOnClickListener(v -> {
            // Join button logic
        });

        holder.btnSkip.setOnClickListener(v -> {
            // Skip button logic
        });

        holder.btnShare.setOnClickListener(v -> {
            // Share logic (e.g. shareIntent)
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEvent;
        TextView txtTitle, txtLocation, txtDate, txtDescription, txtPeople;
        ImageButton btnJoin, btnSkip, btnShare;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEvent = itemView.findViewById(R.id.eventImage);
            txtTitle = itemView.findViewById(R.id.eventName);
            txtLocation = itemView.findViewById(R.id.eventLocation);
            txtDate = itemView.findViewById(R.id.eventDate);
            txtDescription = itemView.findViewById(R.id.eventDesc);
            btnJoin = itemView.findViewById(R.id.btn_events);
            btnSkip = itemView.findViewById(R.id.btn_chat);
        }
    }
}
