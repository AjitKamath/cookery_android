package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = RecipeViewPagerAdapter.class.getName();

    private List<Integer> layouts;
    public RecipeMO recipe;

    public RecipeViewPagerAdapter(Context context, List<Integer> layouts, RecipeMO recipe) {
        this.mContext = context;
        this.layouts = layouts;
        this.recipe = recipe;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupRecipeProcedure(layout); break;
            case 1: setupRecipeIngredients(layout); break;
            case 2: setupRecipeReviews(layout); break;
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupRecipeReviews(ViewGroup layout) {
        RecipeReviewsRecyclerViewAdapter adapter = new RecipeReviewsRecyclerViewAdapter(mContext, recipe.getReviews());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        RecyclerView view_pager_recipe_reviews_rv = layout.findViewById(R.id.view_pager_recipe_reviews_rv);
        view_pager_recipe_reviews_rv.setLayoutManager(mLayoutManager);
        view_pager_recipe_reviews_rv.setItemAnimator(new DefaultItemAnimator());
        view_pager_recipe_reviews_rv.setAdapter(adapter);
    }

    private void setupRecipeIngredients(ViewGroup layout) {
        IngredientsRecyclerViewAdapter adapter = new IngredientsRecyclerViewAdapter(mContext, recipe.getIngredients());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        RecyclerView view_pager_recipe_ingredients_rv = layout.findViewById(R.id.view_pager_recipe_ingredients_rv);
        view_pager_recipe_ingredients_rv.setLayoutManager(mLayoutManager);
        view_pager_recipe_ingredients_rv.setItemAnimator(new DefaultItemAnimator());
        view_pager_recipe_ingredients_rv.setAdapter(adapter);
    }

    private void setupRecipeProcedure(ViewGroup layout) {
        TextView view_pager_recipe_recipe_proc_tv = layout.findViewById(R.id.view_pager_recipe_recipe_proc_tv);
        view_pager_recipe_recipe_proc_tv.setText(recipe.getRCP_PROC());
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "RECIPE";
            case 1: return "INGREDIENTS";
            case 2: return "REVIEWS";
            default: return "UNIMPL";
        }
    }


    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}