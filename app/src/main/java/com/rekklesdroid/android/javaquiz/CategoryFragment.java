package com.rekklesdroid.android.javaquiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rekklesdroid.android.javaquiz.ViewHolder.CategoryViewHolder;
import com.rekklesdroid.android.javaquiz.interfaces.RecyclerViewClickListener;
import com.rekklesdroid.android.javaquiz.model.Category;
import com.rekklesdroid.android.javaquiz.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryFragment extends Fragment {

    View myFragment;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseRecyclerOptions<Category> options;

    public static CategoryFragment newInstance() {

        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        categories = database.getReference().child("Category");
        options = new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(categories, new SnapshotParser<Category>() {
                            @NonNull
                            @Override
                            public Category parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Category(
                                        snapshot.child("Title").getValue().toString(),
                                        snapshot.child("Image").getValue().toString());
                            }
                        })
                        .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = myFragment.findViewById(R.id.recview_category_list);
        
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        
        loadCategories();

        return myFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }



    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_layout, parent, false);

                return new CategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {
                holder.categoryTitle.setText(model.getTitle());
                Picasso.with(getActivity())
                        .load(model.getImage())
                        .into(holder.categoryImage);

                holder.setItemClickListener(new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),model.getTitle()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
