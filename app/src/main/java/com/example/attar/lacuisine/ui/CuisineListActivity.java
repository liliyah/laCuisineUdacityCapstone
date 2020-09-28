package com.example.attar.lacuisine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.adapters.CuisineAdapter;
import com.example.attar.lacuisine.model.Cuisine;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class CuisineListActivity extends AppCompatActivity {
    private TextView mCuisineHeadingTextView;
    CuisineAdapter cuisinea_dapter;
    @BindView(R.id.recycler_cuisines)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_share)
    FloatingActionButton mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_list);
        ButterKnife.bind(this);
        mCuisineHeadingTextView = (TextView) findViewById(R.id.cuisineHeadingTextView);
        Typeface headingFont = Typeface.createFromAsset(getAssets(), "fonts/Windsong.ttf");
        mCuisineHeadingTextView.setTypeface(headingFont);

        ArrayList<Cuisine> data = new ArrayList<>();

        data.add(new Cuisine(R.drawable.breakfast, this.getString(R.string.Breakfastrecipes)));
        data.add(new Cuisine(R.drawable.lunch, this.getString(R.string.lunchrecipes)));
        data.add(new Cuisine(R.drawable.dinner, this.getString(R.string.dinnerrecipes)));
        data.add(new Cuisine(R.drawable.dessert, this.getString(R.string.dessertrecipes)));
        data.add(new Cuisine(R.drawable.drinks, this.getString(R.string.drinks_recipes)));

        cuisinea_dapter = new CuisineAdapter(this, data);


        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        mRecyclerView.setLayoutManager(new GridLayoutManager(CuisineListActivity.this, 2));
        mRecyclerView.setAdapter(cuisinea_dapter);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType(getApplicationContext().getResources().getString(R.string.type));
                String shareBody = getApplicationContext().getResources().getString(R.string.ShareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getApplicationContext().getResources().getString(R.string.Intentextra));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getApplicationContext().getResources().getString(R.string.Shared)));

            }
        });

    }


}
