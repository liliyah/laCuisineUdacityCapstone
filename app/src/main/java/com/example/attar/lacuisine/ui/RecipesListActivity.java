package com.example.attar.lacuisine.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.attar.lacuisine.Constants;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.adapters.RecipeListAdapter;
import com.example.attar.lacuisine.model.Recipe;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipesListActivity extends AppCompatActivity {
    @BindView(R.id.rvRecipesList)
    RecyclerView rvApiRecipesList;
    private RecipeListAdapter mAdapter;
    //public List<Recipe> recipes = new ArrayList<>();
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String cuisinetype = intent.getStringExtra((this.getString(R.string.cuisine)));

        if (cuisinetype == null ){

            getRecipes((this.getString(R.string.breakfast)));
        }

       else if (cuisinetype.equals((this.getString(R.string.breakfast)))) {

            getRecipes((this.getString(R.string.breakfast)));


        } else if (cuisinetype.equals((this.getString(R.string.lunch)))) {

            getRecipes((this.getString(R.string.lunch)));

        } else if (cuisinetype.equals((this.getString(R.string.dinner)))) {

            getRecipes(((this.getString(R.string.dinner))));

        } else if (cuisinetype.equals((this.getString(R.string.Dessert)))) {

            getRecipes(((this.getString(R.string.Dessert))));

        } else if (cuisinetype.equals((this.getString(R.string.Drinks)))) {

            getRecipes(((this.getString(R.string.Drinks))));

        }



    }

    private void getRecipes(String searchQuery) {
        String url = Constants.EDAMAM_BASE_URL + "q=" + searchQuery + "&app_id=" + Constants.EDAMAM_ID + "&app_key=" + Constants.EDAMAM_KEY + "&from=0&to=30";
        final AlertDialog.Builder LicenceDialog = new AlertDialog.Builder(RecipesListActivity.this);

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ArrayList<Recipe> allRecipes = new ArrayList<>();
                        try {
                            String jsonData = response.toString();

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
                                //get the healthlabel array
                                //System.out.println(ingredients + "ingredients");
                                Recipe recipe = new Recipe(title, image, murl, ingredients, calories, servings, HealthLabels, TotalTime);
                                allRecipes.add(recipe);

                                mAdapter = new RecipeListAdapter(RecipesListActivity.this, allRecipes);
                                rvApiRecipesList.setAdapter(mAdapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RecipesListActivity.this);

                                rvApiRecipesList.setLayoutManager(layoutManager);
                                rvApiRecipesList.setHasFixedSize(true);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.Volleyerror), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    LicenceDialog.setTitle(getApplicationContext().getResources().getString(R.string.error_msg));
                    LicenceDialog.setMessage(getApplicationContext().getResources().getString(R.string.CheckInternet));
                    LicenceDialog.setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    final Dialog loding = LicenceDialog.create();
                    loding.show();
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.AuthFailure_Error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.NetworkError), Toast.LENGTH_LONG).show();
                    ;
                } else if (error instanceof ParseError) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.parseError), Toast.LENGTH_LONG).show();
                }
            }
        });

        requestQueue.add(request);
    }


}
