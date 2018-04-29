package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.NutritionCategoryMO;

import java.util.List;

public class IngredientViewNutritionCategoriesRecyclerViewAdapter extends RecyclerView.Adapter<IngredientViewNutritionCategoriesRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = IngredientViewNutritionCategoriesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private View.OnClickListener onClickListener;
    List<NutritionCategoryMO> ingredientNutritionCategories;

    public IngredientViewNutritionCategoriesRecyclerViewAdapter(Context mContext, List<NutritionCategoryMO> ingredientNutritionCategories, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.ingredientNutritionCategories = ingredientNutritionCategories;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_view_nutrition_category_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NutritionCategoryMO nutritionCategory = ingredientNutritionCategories.get(position);

        holder.ingredient_view_nutrition_category_item_name_tv.setText(nutritionCategory.getNUT_CAT_NAME().toUpperCase());

        holder.ingredient_view_nutrition_category_item_nutritions_rv.setAdapter(
                new IngredientViewNutritionCategoryNutritionsRecyclerViewAdapter(mContext, nutritionCategory.getIngredientNutritions(),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.ingredient_view_nutrition_category_item_nutritions_rv.setLayoutManager(mLayoutManager);
        holder.ingredient_view_nutrition_category_item_nutritions_rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public int getItemCount() {
        return ingredientNutritionCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient_view_nutrition_category_item_name_tv;
        private RecyclerView ingredient_view_nutrition_category_item_nutritions_rv;

        public ViewHolder(View view) {
            super(view);
            ingredient_view_nutrition_category_item_name_tv = view.findViewById(R.id.ingredient_view_nutrition_category_item_name_tv);
            ingredient_view_nutrition_category_item_nutritions_rv = view.findViewById(R.id.ingredient_view_nutrition_category_item_nutritions_rv);
        }
    }
}