package com.example.attar.lacuisine.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.attar.lacuisine.Prefs;
import com.example.attar.lacuisine.R;
import com.example.attar.lacuisine.model.Recipe;

public class AppWidgetService  extends RemoteViewsService {
    public static void updateWidget(Context context, Recipe recipe) {
        Prefs.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, laCuisineAppWidget.class));
        laCuisineAppWidget.updateAppWidget(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        return new ListRemoteViewsFactory(getApplicationContext());
    }
}
