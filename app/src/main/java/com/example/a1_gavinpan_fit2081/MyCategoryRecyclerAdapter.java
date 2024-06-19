package com.example.a1_gavinpan_fit2081;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1_gavinpan_fit2081.provider.Category;

import java.util.ArrayList;

public class MyCategoryRecyclerAdapter extends RecyclerView.Adapter<MyCategoryRecyclerAdapter.CustomCategoryViewHolder> {

    ArrayList<Category> data = new ArrayList<Category>();

    @NonNull
    @Override
    public CustomCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CustomCategoryViewHolder viewHolder = new CustomCategoryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCategoryViewHolder holder, int position) {
        holder.tvCategoryName.setText(data.get(position).getCategoryName());
        holder.tvEventCount.setText(String.valueOf(data.get(position).getEventCount()));

        holder.tvCategoryId.setText(data.get(position).getCategoryId());
        if(data.get(position).isCategoryActive()){
            holder.tvCategoryActive.setText("Active");
        } else{
            holder.tvCategoryActive.setText("Inactive");
        }
        holder.cardView.setOnClickListener(v -> {
            String selectedLocation = data.get(position).getEventLocation();

            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, GoogleMapActivity.class);
            intent.putExtra("location", selectedLocation);
            context.startActivity(intent);
        });
    }

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;
    }

    public class CustomCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategoryId;
        public TextView tvCategoryActive;
        public TextView tvCategoryName;
        public TextView tvEventCount;

        public View cardView;

        public CustomCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            tvCategoryId = itemView.findViewById(R.id.tvCategoryId);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvEventCount = itemView.findViewById(R.id.tvEventCount);
            tvCategoryActive = itemView.findViewById(R.id.tvCategoryActive);
        }
    }
}