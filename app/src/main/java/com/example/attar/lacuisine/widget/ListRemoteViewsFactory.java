package com.example.attar.lacuisine.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.attar.lacuisine.Prefs;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.model.Recipe;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Recipe recipe;

    public ListRemoteViewsFactory(Context context) {
        this.mContext = context;
    }
    @Override
    public void onCreate() {

    }
    @Override
    public void onDataSetChanged() { {
        recipe = Prefs.loadRecipe(mContext);
    }
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredientLines().size();
    }
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.lacuisine_widget_item);
        row.setTextViewText(R.id.ingredient_item_text, recipe.getIngredientLines().get(position).toString());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
