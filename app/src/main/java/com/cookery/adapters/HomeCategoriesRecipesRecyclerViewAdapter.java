package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;

import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.TOP_RECIPES_CHEF;
import static com.cookery.utils.Constants.TOP_RECIPES_MONTH;
import static com.cookery.utils.Constants.TRENDING_RECIPES;

public class HomeCategoriesRecipesRecyclerViewAdapter extends RecyclerView.Adapter<HomeCategoriesRecipesRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = HomeCategoriesRecipesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private Map<String, List<RecipeMO>> categoryRecipes;

    public HomeCategoriesRecipesRecyclerViewAdapter(Context mContext, Map<String, List<RecipeMO>> categoryRecipes) {
        this.mContext = mContext;
        this.categoryRecipes = categoryRecipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_categorized_recipes, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String choice = "";

        switch (position){
            case 0: choice = TRENDING_RECIPES; break;
            case 1: choice = TOP_RECIPES_MONTH; break;
            case 2: choice = TOP_RECIPES_CHEF; break;
            default: Log.e(CLASS_NAME, "There are more categories of recipes than they are implemented");
        }

        List<RecipeMO> recipes = categoryRecipes.get(choice);

        holder.home_categorized_recipes_tv.setText(choice.toUpperCase());

        HomeCateoryRecipesViewPagerAdapter adapter = new HomeCateoryRecipesViewPagerAdapter(mContext, recipes, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.home_categorized_recipes_vp.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return categoryRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView home_categorized_recipes_tv;
        public ViewPager home_categorized_recipes_vp;

        public ViewHolder(View view) {
            super(view);
            home_categorized_recipes_tv = view.findViewById(R.id.home_categorized_recipes_tv);
            home_categorized_recipes_vp = view.findViewById(R.id.home_categorized_recipes_vp);
        }
    }
}