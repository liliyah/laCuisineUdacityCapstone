package com.example.attar.lacuisine;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.attar.lacuisine.model.Recipe;
import com.google.gson.Gson;

public class Prefs {
    public static final String PREFS_NAME = "prefs";


    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        prefs.putString("MyObject", json);
        prefs.apply();

    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("MyObject", "");
        Recipe  recipe = gson.fromJson(json, Recipe.class);
        return recipe;
    }


}

