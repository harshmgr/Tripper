package com.example.tripper.HelperClasses.HomeAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tripper.Common.ConnectionAddress;
import com.example.tripper.R;
import com.example.tripper.User.Destination;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {
    List<FeaturedHelperClass> featuredLocations;

    public FeaturedAdapter(List<FeaturedHelperClass> featuredLocations) {
        this.featuredLocations = featuredLocations;

    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_card_design,parent,false);
        FeaturedViewHolder featuredViewHolder=new FeaturedViewHolder(view);
        return featuredViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FeaturedHelperClass featuredHelperClass=featuredLocations.get(position);
        Glide.with(holder.title.getContext()).load(ConnectionAddress.ipaddress+"/images/"
                + featuredHelperClass.getPlaceImage()).into(holder.imageView);
        holder.title.setText(featuredHelperClass.getTitle());
        holder.description.setText(featuredHelperClass.getDescription());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Destination.class);
                intent.putExtra("placeId", featuredLocations.get(position).getPlaceId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return featuredLocations.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,description;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.featured_image);
            title = itemView.findViewById(R.id.featured_title);
            description = itemView.findViewById(R.id.featured_description);

        }
    }
}
