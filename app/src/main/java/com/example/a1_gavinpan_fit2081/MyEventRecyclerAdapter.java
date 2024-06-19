package com.example.a1_gavinpan_fit2081;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1_gavinpan_fit2081.provider.Event;

import java.util.ArrayList;

public class MyEventRecyclerAdapter extends RecyclerView.Adapter<MyEventRecyclerAdapter.CustomEventViewHolder> {
    ArrayList<Event> data = new ArrayList<>();

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public CustomEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_layout, parent, false);
        MyEventRecyclerAdapter.CustomEventViewHolder viewHolder = new MyEventRecyclerAdapter.CustomEventViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomEventViewHolder holder, int position) {
        holder.tvEventId.setText("Id: " + data.get(position).getEventId());
        holder.tvCategoryId.setText("Category Id: " + data.get(position).getCategoryId());
        holder.tvEventName.setText("Name: " + data.get(position).getEventName());
        holder.tvTickets.setText(("Tickets: " + String.valueOf(data.get(position).getTicketsAvaliable())));

        if(data.get(position).isEventActive()){
            holder.tvEventActive.setText("Active");
        } else{
            holder.tvEventActive.setText("Inactive");
        }
        holder.cardView.setOnClickListener(v -> {
            String inputEventName = data.get(position).getEventName();

            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
            intent.putExtra("eventName", inputEventName);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;
    }

    public class CustomEventViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventId;
        public TextView tvCategoryId;
        public TextView tvEventName;
        public TextView tvTickets;
        public TextView tvEventActive;
        public View cardView;

        public CustomEventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            tvEventId = itemView.findViewById(R.id.itemEventId);
            tvCategoryId = itemView.findViewById(R.id.itemCategoryId);
            tvEventName = itemView.findViewById(R.id.itemEventName);
            tvTickets = itemView.findViewById(R.id.itemTickets);
            tvEventActive = itemView.findViewById(R.id.itemActive);
        }
    }
}
