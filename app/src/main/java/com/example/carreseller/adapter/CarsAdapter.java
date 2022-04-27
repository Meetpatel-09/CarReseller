package com.example.carreseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carreseller.CarInfoActivity;
import com.example.carreseller.R;
import com.example.carreseller.models.CarsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {

    private final Context context;
    private final List<CarsModel> list;

    public CarsAdapter(Context context, List<CarsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder holder, int position) {
        CarsModel model = list.get(position);

        String price = "₹ " + model.getPrice();

        holder.row_name.setText(model.getCarName());
        holder.row_company.setText(model.getCompany());
        holder.row_price.setText(price);

        try {
            if (model.getCarImage() != null)
                Picasso.get().load(model.getCarImage()).placeholder(R.drawable.loading_shape).into(holder.row_thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.container.setOnClickListener(view -> {
            Intent i = new Intent(context , CarInfoActivity.class);

            i.putExtra("car_id" ,model.getId());
            i.putExtra("company" ,model.getCompany());
            i.putExtra("name",model.getCarName());
            i.putExtra("image",model.getCarImage());
//                i.putExtra("book_desc",model.getDescription());
            i.putExtra("category",model.getCategory());

            i.putExtra("dealer_id", model.getDealerId());
            i.putExtra("car_price",model.getPrice());
            i.putExtra("engine",model.getEngine());
            i.putExtra("seating_capacity",model.getSeatingCapacity());
            i.putExtra("fuel_type",model.getFuelType());
            i.putExtra("year",model.getLaunchedYear());

            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CarsViewHolder extends RecyclerView.ViewHolder {

        public ImageView row_thumbnail;
        public LinearLayout container;
        public TextView row_name,row_company,row_price;

        public CarsViewHolder(@NonNull View itemView) {
            super(itemView);

            container= itemView.findViewById(R.id.container);
            row_thumbnail= itemView.findViewById(R.id.row_thumbnail);
            row_name= itemView.findViewById(R.id.row_name);
            row_company= itemView.findViewById(R.id.row_company);
            row_price= itemView.findViewById(R.id.row_price);
        }
    }
}
