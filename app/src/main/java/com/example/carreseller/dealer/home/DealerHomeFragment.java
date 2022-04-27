package com.example.carreseller.dealer.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.carreseller.R;
import com.example.carreseller.adapter.CarsAdapter;
import com.example.carreseller.models.CarsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DealerHomeFragment extends Fragment {

    private CarsAdapter adapter;
    private ArrayList<CarsModel> list;

    RecyclerView recycler_home_page_cars;

    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dealer_home, container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("Cars");

        recycler_home_page_cars = view.findViewById(R.id.recycler_home_page_cars_d);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);

//        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
//        ArrayList<SlideModel> images = new ArrayList<>();
//        images.add(new SlideModel(R.drawable.car1, null));
//        images.add(new SlideModel(R.drawable.car2, null));

//        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);

        recycler_home_page_cars.setHasFixedSize(true);
        recycler_home_page_cars.setLayoutManager(new LinearLayoutManager(getActivity()));

        showNewAvailable();

        pullToRefresh.setOnRefreshListener(() -> {
            showNewAvailable();
            pullToRefresh.setRefreshing(false);
        });

        return view;
    }

    private void showNewAvailable() {

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CarsModel model = dataSnapshot.getValue(CarsModel.class);
                    list.add(0, model);
                }

                adapter = new CarsAdapter(getContext(), list);
                adapter.notifyDataSetChanged();
                recycler_home_page_cars.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}