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
import com.example.fruitseason.model.Model;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSellers extends RecyclerView.Adapter<AdapterSellers.MyViewHolder> {

    private List<Model> sellers;
    private Context context;

    public AdapterSellers(List<Model> sellers, Context context) {
        this.sellers = sellers;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sellers, parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model seller = sellers.get(position);
        holder.name.setText(seller.getName());
        holder.address.setText(seller.getAddress());

        Picasso.get().load(seller.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return sellers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameSellersList);
            image = itemView.findViewById(R.id.imageSellersList);
            address = itemView.findViewById(R.id.addressSellersList);

        }
    }
}
