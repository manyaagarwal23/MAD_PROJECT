package com.example.unimingle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.EventViewHolder> {

    private final Context context;
    private final List<Event_Model> eventList;

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

        // Bind event data to views
        // Set image based on whether we have a URL or resource ID
        if (event.imageUrl != null && !event.imageUrl.isEmpty()) {
            // If we have a URL, we would use a library like Glide or Picasso to load it
            // For now, we'll use the default image resource
            holder.eventImage.setImageResource(event.imageResId);
            
            // Example of how to use Glide (you would need to add the dependency):
            // Glide.with(context).load(event.imageUrl).placeholder(event.imageResId).into(holder.eventImage);
        } else {
            // Use the resource ID directly
            holder.eventImage.setImageResource(event.imageResId);
        }
        
        holder.eventName.setText(event.title);
        holder.eventLocation.setText(event.location);
        holder.eventDate.setText(event.date);
        holder.eventDesc.setText(event.description);
        holder.eventPeople.setText(event.currentPeople + " / " + event.totalPeople);
        holder.userprofile.setImageResource(event.userprofileResId);

        // Button actions
        holder.btnJoin.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetailsActivity.class);
            intent.putExtra("hostUid", event.hostUid);
            intent.putExtra("hostName", event.hostName);
            intent.putExtra("eventId", event.id);
            intent.putExtra("eventTitle", event.title);
            intent.putExtra("eventLocation", event.location);
            intent.putExtra("eventDate", event.date);
            intent.putExtra("eventDescription", event.description);
            v.getContext().startActivity(intent);
        });
        
        holder.btnSkip.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Event_Model eventToRemove = eventList.get(pos);
                // Remove from Firebase if it has an ownerUid and id
                if (eventToRemove.hostUid != null && eventToRemove.id != null && !eventToRemove.id.isEmpty()) {
                    com.google.firebase.database.DatabaseReference plansRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("plans");
                    plansRef.child(eventToRemove.id).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Event removed from database", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to remove: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
                eventList.remove(pos);
                notifyItemRemoved(pos);
                Toast.makeText(context, "Event removed from view", Toast.LENGTH_SHORT).show();
            }
        });
        
        holder.userprofile.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProfileActivity.class);
            intent.putExtra("userId", event.hostUid);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventLocation, eventDate, eventDesc, eventPeople;
        ImageButton btnJoin, btnSkip, btnShare,userprofile ;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize all views
            eventImage = itemView.findViewById(R.id.eventImage);
            eventName = itemView.findViewById(R.id.eventName);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            eventPeople = itemView.findViewById(R.id.eventPeople);

            btnJoin = itemView.findViewById(R.id.btn_tick);
            btnSkip = itemView.findViewById(R.id.btn_cross);
            btnShare = itemView.findViewById(R.id.btn_chat);
            userprofile  = itemView.findViewById(R.id.userprofile);

        }
    }
}