package com.example.attar.lacuisine.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attar.lacuisine.Constants;
import com.example.attar.lacuisine.model.Recipe;
import com.example.attar.lacuisine.widget.AppWidgetService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.example.attar.lacuisine.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.recipeImageView)
    ImageView mRecipeImageViewLabel;

    @BindView(R.id.caloriesTextView)
    TextView mCaloriesTextViewLabel;

    @BindView(R.id.tvCautionsOutput)
    TextView mHealthCautionsTextView;

    @BindView(R.id.tvPreptimeOutput)
    TextView RecipePrepTxt;

    @BindView(R.id.tvServingsOutput)
    TextView mtvServingsOutputTxt;

    @BindView(R.id.sourceTextView)
    TextView mSourceTextViewLabel;

    @BindView(R.id.saveRecipeButton)
    TextView mSaveRecipeButtonLabel;

    @BindView(R.id.recipeNameTextView)
    TextView mRecipeNameTextView;

    @BindView(R.id.WidgetButton)
    Button mWidgetButton;

    private Recipe mRecipe;
    private ListView lvIngredients;
    private List<Recipe> mRecipes;
    private int mPosition;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(List<Recipe> recipes, Integer position) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(recipes));
        args.putInt(Constants.EXTRA_KEY_POSITION, position);

        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipes = Parcels.unwrap(getArguments().getParcelable(Constants.EXTRA_KEY_RECIPES));
        mPosition = getArguments().getInt(Constants.EXTRA_KEY_POSITION);
        mRecipe = mRecipes.get(mPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        String RecipeName = mRecipe.getTitle();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference restaurantRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES)
                .child(uid);
        restaurantRef.orderByChild(getContext().getResources().getString(R.string.title)).equalTo(RecipeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    mSaveRecipeButtonLabel.setEnabled(false);
                    mSaveRecipeButtonLabel.setText(getContext().getResources().getString(R.string.RecipeSaved));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSourceTextViewLabel.setOnClickListener(this);
        mSaveRecipeButtonLabel.setOnClickListener(this);


        mWidgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWidgetService.updateWidget(getContext(), mRecipe);
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.recipeaddedtowidget), Toast.LENGTH_LONG).show();

            }
        });

        Picasso.get()
                .load(mRecipe.getImage())
                .into(mRecipeImageViewLabel);
        mRecipeNameTextView.setText(mRecipe.getTitle());
        mCaloriesTextViewLabel.setText(mRecipe.getCalories());
        List<String> HealthLabels = new ArrayList<>();
        HealthLabels.addAll(mRecipe.getHealthLabels());
        StringBuilder stringBuilder = new StringBuilder();
        for (String Details : HealthLabels) {

            stringBuilder.append(Details + "\n");
        }
        mHealthCautionsTextView.setText(stringBuilder.toString());


        RecipePrepTxt.setText(mRecipe.getTotalTime());

        mtvServingsOutputTxt.setText(mRecipe.getServings());

        lvIngredients = view.findViewById(R.id.lvIngredients);
        List<String> ingredients = new ArrayList<>();
        ingredients.addAll(mRecipe.getIngredientLines());

        lvIngredients.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, ingredients));

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mSourceTextViewLabel) {
            Intent directionsIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mRecipe.getUrl()));
            startActivity(directionsIntent);
        }
        if (v == mSaveRecipeButtonLabel) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference restaurantRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_RECIPES)
                    .child(uid);

            DatabaseReference pushRef = restaurantRef.push();
            String pushId = pushRef.getKey();
            mRecipe.setPushId(pushId);
            pushRef.setValue(mRecipe);
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            mSaveRecipeButtonLabel.setEnabled(false);
            mSaveRecipeButtonLabel.setText(getContext().getResources().getString(R.string.RecipeSaved));
        }

    }

}
