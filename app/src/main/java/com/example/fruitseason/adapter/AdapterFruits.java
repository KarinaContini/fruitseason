package com.example.fruitseason.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitseason.R;
import com.example.fruitseason.model.Fruit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFruits extends RecyclerView.Adapter<AdapterFruits.MyViewHolder> {

    private List<Fruit> fruits;
    private Context context;

    public AdapterFruits(List<Fruit> fruits, Context context) {
        this.fruits = fruits;
        this.context = context;
    }
    public void setFilteredList(List<Fruit> filteredList){
        this.fruits = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fruits, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Fruit fruit = fruits.get(position);
        holder.name.setText(fruit.getName());

        Picasso.get().load(fruit.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameFruitsList);
            image = itemView.findViewById(R.id.imageFruitsList);
        }
    }

}
