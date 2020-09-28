package com.example.attar.lacuisine.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.attar.lacuisine.Constants;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.adapters.RecipeListAdapter;
import com.example.attar.lacuisine.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EdamamSearchActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rvApiRecipesList)
    RecyclerView rvApiRecipesList;

    @BindView(R.id.SearchTxt)
    TextView mtxtseatch;

    @BindView(R.id.SearchButton)
    Button mButtonSearch;

    private RecipeListAdapter mAdapter;
    public List<Recipe> mrecipes = new ArrayList<>();
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edamam_search);
        ButterKnife.bind(this);


        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// you can type ...breakfast .. lunch ... dessert for an easier search
                search = mtxtseatch.getText().toString();
                if (search == ""){
                  Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.MustEnterRecipe),Toast.LENGTH_LONG).show();

                }
                else{
                String url = Constants.EDAMAM_BASE_URL + "q=" + search + "&app_id=" + Constants.EDAMAM_ID + "&app_key=" + Constants.EDAMAM_KEY + "&from=0&to=30";

                new DownloadRecipesTask().execute(url);
                }
            }
        });
    }

    private class DownloadRecipesTask extends AsyncTask<String, Void, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(String... urls) {

            OkHttpClient client = new OkHttpClient();
            Request request =
                    new Request.Builder()
                            .url(urls[0])
                            .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                try {


                    String jsonData = response.body().string();

                    JSONObject recipeCatalogJSON = new JSONObject(jsonData);
                    JSONArray recipesJSON = recipeCatalogJSON.getJSONArray(getApplicationContext().getResources().getString(R.string.hits));
                    for (int i = 0; i < recipesJSON.length(); i++) {
                        //gets specific hit
                        JSONObject recipeJSON = recipesJSON.getJSONObject(i);
                        //goes into the recipe portion of hit
                        JSONObject currentRecipe = recipeJSON.getJSONObject(getApplicationContext().getResources().getString(R.string.recipe));
                        String title = currentRecipe.getString(getApplicationContext().getResources().getString(R.string.label));
                        String image = currentRecipe.getString(getApplicationContext().getResources().getString(R.string.image));
                        String murl = currentRecipe.getString(getApplicationContext().getResources().getString(R.string.url));
                        String calories = Integer.toString(currentRecipe.getInt(getApplicationContext().getResources().getString(R.string.calories_json)));
                        String TotalTime = Integer.toString(currentRecipe.getInt(getApplicationContext().getResources().getString(R.string.totalTime)));
                        String servings = Integer.toString(currentRecipe.getInt(getApplicationContext().getResources().getString(R.string.yield)));

                        //looks into an array of ingredients and add them to the recipe
                        ArrayList<String> ingredients = new ArrayList<>();
                        JSONArray ingredientList = currentRecipe.getJSONArray(getApplicationContext().getResources().getString(R.string.ingredientLines));
                        for (int j = 0; j < ingredientList.length(); j++) {
                            ingredients.add(ingredientList.get(j).toString());
                        }
                        ArrayList<String> HealthLabels = new ArrayList<>();
                        JSONArray HealthLabelsList = currentRecipe.getJSONArray(getApplicationContext().getResources().getString(R.string.healthLabels));
                        for (int j = 0; j < HealthLabelsList.length(); j++) {
                            HealthLabels.add(HealthLabelsList.get(j).toString());
                        }

                        Recipe recipe = new Recipe(title, image, murl, ingredients, calories, servings, HealthLabels, TotalTime);
                        mrecipes.add(recipe);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return mrecipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {

            mAdapter = new RecipeListAdapter(getApplicationContext(), recipes);
            rvApiRecipesList.setAdapter(mAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EdamamSearchActivity.this);
            rvApiRecipesList.setLayoutManager(layoutManager);
            rvApiRecipesList.setHasFixedSize(true);
            mAdapter.notifyDataSetChanged();




            super.onPostExecute(recipes);
        }
    }
}




