package com.example.fruitseason.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitseason.R;
import com.example.fruitseason.model.SellerFruit;


import java.util.List;

public class AdapterMyFruits extends RecyclerView.Adapter<AdapterMyFruits.MyViewHolder> {

    private List<SellerFruit> fruits;
    private Context context;

    public AdapterMyFruits(List<SellerFruit> fruits, Context context) {
        this.fruits = fruits;
        this.context = context;
    }
    public void setFilteredList(List<SellerFruit> filteredList) {
        this.fruits = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_fruits, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SellerFruit fruit = fruits.get(position);

        holder.name.setText(fruit.getName());
        holder.price.setText(fruit.getPrice());

    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtMyFruits);
            price = itemView.findViewById(R.id.txtPriceMyFruits);

        }
    }
}
