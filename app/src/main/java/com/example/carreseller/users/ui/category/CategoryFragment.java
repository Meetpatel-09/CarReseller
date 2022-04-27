package com.example.carreseller.users.ui.category;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.carreseller.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        listView = view.findViewById(R.id.list_view);

        getCategories();

        return view;
    }

    private void getCategories() {

        final List<String> catItems = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    catItems.add(dataSnapshot.getKey());
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, catItems);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((adapterView, view1, position, l) -> {
                    // TODO Auto-generated method stub
                    String value = adapter.getItem(position);

                    requireContext().getSharedPreferences("CATEGORY", Context.MODE_PRIVATE).edit().putString("categoryName", value).apply();
                    ((FragmentActivity) requireContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryAllFragment()).commit();

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}