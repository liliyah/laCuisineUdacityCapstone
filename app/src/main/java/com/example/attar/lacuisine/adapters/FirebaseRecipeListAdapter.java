package com.example.attar.lacuisine.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.attar.lacuisine.Constants;
import com.example.attar.lacuisine.ItemTouchHelperAdapter;
import com.example.attar.lacuisine.OnStartDragListener;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.model.Recipe;
import com.example.attar.lacuisine.ui.RecipeDetailActivity;
import com.example.attar.lacuisine.ui.RecipeDetailFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseRecipeListAdapter extends FirebaseRecyclerAdapter<Recipe, FirebaseRecipeViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private OnStartDragListener mOnStartDragListener;
    private int mOrientation;

    public FirebaseRecipeListAdapter(FirebaseRecyclerOptions<Recipe> options, Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mRecipes.add(dataSnapshot.getValue(Recipe.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setIndexInFirebase() {
        for (Recipe recipe : mRecipes) {
            int index = mRecipes.indexOf(recipe);
            DatabaseReference ref = getRef(index);
            recipe.setIndex(Integer.toString(index));
            ref.setValue(recipe);
        }
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRecipes, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mRecipes.remove(position);
        //getSnapshots().getSnapshot(position).getRef().removeValue();
        getRef(position).removeValue();
    }


    @Override
    protected void onBindViewHolder(final FirebaseRecipeViewHolder holder, int position, Recipe model) {
        holder.bindRecipe(model);



        holder.mRecipeImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();

                    Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, holder.getAdapterPosition());
                    intent.putExtra(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
                    mContext.startActivity(intent);

            }
        });
    }

    @Override
    public FirebaseRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item_drag, parent, false);
        return new FirebaseRecipeViewHolder(view);
    }

}
