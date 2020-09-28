package com.example.attar.lacuisine.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.attar.lacuisine.Constants;
import com.example.attar.lacuisine.ItemTouchHelperAdapter;
import com.example.attar.lacuisine.OnStartDragListener;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.SimpleItemTouchHelperCallback;
import com.example.attar.lacuisine.adapters.FirebaseRecipeListAdapter;
import com.example.attar.lacuisine.model.Recipe;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRecipeListFragment extends Fragment implements OnStartDragListener {
    private DatabaseReference mRecipeReference;
    private FirebaseRecipeListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Query recipeQuery;
    @BindView(R.id.rvRecipesList)
    RecyclerView rvRecipesListView;


    public SavedRecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_recipe_list, container, false);
        ButterKnife.bind(this, view);
        Toast.makeText(getContext(),getContext().getResources().getString(R.string.swipetodelete),Toast.LENGTH_LONG).show();
        setUpFirebaseAdapter();
        return view;
    }

    private void setUpFirebaseAdapter() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRecipeReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES)
                .child(uid);
        recipeQuery = mRecipeReference.getRef();

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>().setQuery(recipeQuery, Recipe.class).build();

        mFirebaseAdapter = new FirebaseRecipeListAdapter(options, recipeQuery, this, getActivity());

        rvRecipesListView.setHasFixedSize(true);
        rvRecipesListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecipesListView.setAdapter(mFirebaseAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mFirebaseAdapter.onItemDismiss(viewHolder.getAdapterPosition());
                Toast.makeText(getContext(), Resources.getSystem().getString(R.string.deletedrecipe), Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(rvRecipesListView);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mFirebaseAdapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvRecipesListView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
