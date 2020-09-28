package com.example.attar.lacuisine.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Parcel
public class Recipe {
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("url")
    private String url;

    @SerializedName("ingredientLines")
    private List<String> ingredientLines = new ArrayList<>();
    @SerializedName("calories")
    private String calories;
    @SerializedName("servings")
    private String servings;

    @SerializedName("healthLabels")
    private List<String> healthLabels = new ArrayList<>();
    @SerializedName("totaltime")
    private String totaltime;

    private String pushId;

    private String index;


    public Recipe(){}



    public Recipe(String title, String image, String url, ArrayList<String> ingredientLines, String calories, String servings, ArrayList<String> healthLabels, String totaltime) {
        this.title = title;
        this.image = image;
        this.url = url;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.servings = servings;
        this.healthLabels = healthLabels;
        this.totaltime = totaltime;
        this.index = "not_specified";


    }


    public String getTotalTime() {
        return totaltime;
    }

    public void setTotalTime(String totalTime) {
        this.totaltime= totaltime;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(ArrayList<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }
    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }


    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

}
