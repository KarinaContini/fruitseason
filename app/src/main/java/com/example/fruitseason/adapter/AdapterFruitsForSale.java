package com.example.fruitseason.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitseason.R;
import com.example.fruitseason.model.Fruit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFruitsForSale extends RecyclerView.Adapter<AdapterFruitsForSale.MyViewHolder> {

    private List<Fruit> fruits;
    private Context context;

    public AdapterFruitsForSale(List<Fruit> fruits, Context context) {
        this.fruits = fruits;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruits_list_sale, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Fruit fruit = fruits.get(position);
        holder.name.setText(fruit.getName());

    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtFruits);

        }
    }

}
