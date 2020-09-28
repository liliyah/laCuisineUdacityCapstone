package com.example.attar.lacuisine.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.model.Cuisine;
import com.example.attar.lacuisine.ui.RecipesListActivity;

import java.util.ArrayList;
import java.util.List;


public class CuisineAdapter  extends RecyclerView.Adapter<CuisineAdapter.MyViewHolder> {
    List<Cuisine> data ;
    Context context;

    public CuisineAdapter(Context context, ArrayList<Cuisine> data) {
        this.context = context;
        this.data = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        public TextView mTextView;
        public ImageView imageView;

        public MyViewHolder(View v){
            super(v);


            mTextView = (TextView) v.findViewById(R.id.tv_title_name);
            imageView = (ImageView) v.findViewById(R.id.image_id);

        }
    }

    @Override
    public CuisineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuisineitem, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){

        Cuisine all = data.get(position);
        holder.mTextView.setText(all.txt);
        holder.imageView.setImageResource(all.imageid);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent =  new Intent(context, RecipesListActivity.class);
                int position = holder.getAdapterPosition();
                if (position == 0){
                    intent.putExtra("cuisine","breakfast");
                    context.startActivity(intent);
                } else if (position == 1){
                    intent.putExtra("cuisine","lunch");
                    context.startActivity(intent);
                } else if (position == 2){
                    intent.putExtra("cuisine","dinner");
                    context.startActivity(intent);

                }else if (position == 3) {
                    intent.putExtra("cuisine", "Dessert");
                    context.startActivity(intent);
                }else if (position == 4) {
                    intent.putExtra("cuisine", "Drinks");
                    context.startActivity(intent);

                }
                //Toast.makeText(context,String.valueOf(position), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}