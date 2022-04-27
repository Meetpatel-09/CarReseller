package com.example.carreseller.users.ui.category;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carreseller.R;
import com.example.carreseller.adapter.CarsAdapter;
import com.example.carreseller.models.CarsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryAllFragment extends Fragment {

    private RecyclerView recyclerViewCar;
    private CarsAdapter adapter;
    private List<CarsModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_all, container, false);

        String data = requireContext().getSharedPreferences("CATEGORY", Context.MODE_PRIVATE).getString("categoryName", null);

        TextView categoryName = view.findViewById(R.id.category_name);
        categoryName.setText(data);

        recyclerViewCar = view.findViewById(R.id.recycler_view_car);
        recyclerViewCar.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerViewCar.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new CarsAdapter(getContext(), list);
        recyclerViewCar.setAdapter(adapter);

        showNewAvailable();

        return view;
    }

    private void showNewAvailable() {

        FirebaseDatabase.getInstance().getReference().child("Cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CarsModel model = dataSnapshot.getValue(CarsModel.class);

                    String data = requireContext().getSharedPreferences("CATEGORY", Context.MODE_PRIVATE).getString("categoryName", null);
                    if (model.getCategory().equals(data))
                        list.add(model);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}