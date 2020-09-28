package com.example.attar.lacuisine;


import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}