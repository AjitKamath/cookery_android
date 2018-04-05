package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = RecipeViewViewPagerAdapter.class.getName();

    private List<Integer> layouts;
    public RecipeMO recipe;
    private View.OnClickListener listener;
    private FragmentManager manager;

    public RecipeViewViewPagerAdapter(Context context,FragmentManager manager, List<Integer> layouts, RecipeMO recipe, View.OnClickListener listener) {
        this.mContext = context;
        this.layouts = layouts;
        this.recipe = recipe;
        this.manager = manager;
        this.listener = listener;

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupRecipeSteps(layout); break;
            case 1: setupRecipeIngredients(layout); break;
            default: break;
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupRecipeIngredients(ViewGroup layout) {
        final RecipeViewIngredientsRecyclerViewAdapter adapter = new RecipeViewIngredientsRecyclerViewAdapter(mContext,manager,recipe.getIngredients(), recipe.getMylists() ,new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        RecyclerView view_pager_recipe_ingredients_rv = layout.findViewById(R.id.recipe_view_ingredients_rv);
        view_pager_recipe_ingredients_rv.setLayoutManager(mLayoutManager);
        view_pager_recipe_ingredients_rv.setItemAnimator(new DefaultItemAnimator());
        view_pager_recipe_ingredients_rv.setAdapter(adapter);
    }


    private void setupRecipeSteps(ViewGroup layout) {
        ImageView recipe_view_recipe_steps_fullscreen_iv = layout.findViewById(R.id.recipe_view_recipe_steps_fullscreen_iv);
        RecyclerView recipe_view_recipe_steps_rv = layout.findViewById(R.id.recipe_view_recipe_steps_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_view_recipe_steps_rv.setLayoutManager(mLayoutManager);
        recipe_view_recipe_steps_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_view_recipe_steps_rv.setAdapter(new RecipeViewStepsRecyclerViewAdapter(mContext, recipe.getSteps(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));

        recipe_view_recipe_steps_fullscreen_iv.setOnClickListener(listener);
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