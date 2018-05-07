package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.IngredientViewImagesGridViewAdapter;
import com.cookery.adapters.IngredientViewNutritionCategoriesRecyclerViewAdapter;
import com.cookery.models.IngredientMO;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class IngredientViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.ingredient_view_ll)
    LinearLayout ingredient_view_ll;

    @InjectView(R.id.ingredient_view_primary_image_iv)
    CircleImageView ingredient_view_primary_image_iv;

    @InjectView(R.id.ingredient_view_ingredient_name_tv)
    TextView ingredient_view_ingredient_name_tv;

    @InjectView(R.id.ingredient_view_ingredient_category_tv)
    TextView ingredient_view_ingredient_category_tv;

    @InjectView(R.id.ingredient_view_ingredient_images_gv)
    GridView ingredient_view_ingredient_images_gv;

    @InjectView(R.id.ingredient_view_ingredient_no_nutrition_tv)
    TextView ingredient_view_ingredient_no_nutrition_tv;

    @InjectView(R.id.ingredient_view_ingredient_nutrition_category_rv)
    RecyclerView ingredient_view_ingredient_nutrition_category_rv;
    /*components*/


    private IngredientMO ingredient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        ingredient = (IngredientMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage(){
        if(ingredient == null){
            Log.e(CLASS_NAME, "Error ! Ingredient is null");
            return;
        }

        if(ingredient.getIngredientPrimaryImage() != null && !ingredient.getIngredientPrimaryImage().isEmpty()){
            Utility.loadImageFromURL(mContext, ingredient.getIngredientPrimaryImage().get(0).getING_IMG(),  ingredient_view_primary_image_iv);
        }

        ingredient_view_ingredient_name_tv.setText(ingredient.getIngredientAkaName().toUpperCase());
        ingredient_view_ingredient_category_tv.setText(ingredient.getIngredientCategoryName().toUpperCase());

        setupImages();
        setupNutritions();
    }

    private void setupNutritions() {
        if(ingredient.getIngredientNutritionCategories() == null || ingredient.getIngredientNutritionCategories().isEmpty()){
            ingredient_view_ingredient_no_nutrition_tv.setVisibility(View.VISIBLE);
            return;
        }
        else{
            ingredient_view_ingredient_no_nutrition_tv.setVisibility(View.GONE);
        }

        ingredient_view_ingredient_nutrition_category_rv.setAdapter(
                new IngredientViewNutritionCategoriesRecyclerViewAdapter(mContext, ingredient.getIngredientNutritionCategories(),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ingredient_view_ingredient_nutrition_category_rv.setLayoutManager(mLayoutManager);
        ingredient_view_ingredient_nutrition_category_rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupImages() {
        ingredient_view_ingredient_images_gv.setAdapter(new IngredientViewImagesGridViewAdapter(mContext, ingredient.getImages(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }

    // Empty constructor required for DialogFragment
    public IngredientViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
