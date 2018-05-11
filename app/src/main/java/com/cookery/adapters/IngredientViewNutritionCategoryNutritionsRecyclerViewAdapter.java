package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientNutritionMO;

import java.util.List;

public class IngredientViewNutritionCategoryNutritionsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientViewNutritionCategoryNutritionsRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = IngredientViewNutritionCategoryNutritionsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private View.OnClickListener onClickListener;
    List<IngredientNutritionMO> ingredientNutritionCategoryNutritions;

    public IngredientViewNutritionCategoryNutritionsRecyclerViewAdapter(Context mContext, List<IngredientNutritionMO> ingredientNutritionCategoryNutritions, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.ingredientNutritionCategoryNutritions = ingredientNutritionCategoryNutritions;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_ingredient_nutrition_category_nutrition_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IngredientNutritionMO nutritionCategoryNutrition = ingredientNutritionCategoryNutritions.get(position);

        holder.ingredient_view_nutrition_category_nutrition_item_nutrition_name_tv.setText(nutritionCategoryNutrition.getIngredientNutritionName().toUpperCase());
        holder.ingredient_view_nutrition_category_nutrition_item_nutrition_value_tv.setText(nutritionCategoryNutrition.getING_NUT_VAL());
        holder.ingredient_view_nutrition_category_nutrition_item_nutrition_uom_tv.setText(nutritionCategoryNutrition.getNutritionUOMName());
    }

    @Override
    public int getItemCount() {
        return ingredientNutritionCategoryNutritions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient_view_nutrition_category_nutrition_item_nutrition_name_tv;
        private TextView ingredient_view_nutrition_category_nutrition_item_nutrition_value_tv;
        private TextView ingredient_view_nutrition_category_nutrition_item_nutrition_uom_tv;

        public ViewHolder(View view) {
            super(view);
            ingredient_view_nutrition_category_nutrition_item_nutrition_name_tv = view.findViewById(R.id.common_ingredient_nutrition_category_nutrition_item_nutrition_name_tv);
            ingredient_view_nutrition_category_nutrition_item_nutrition_value_tv = view.findViewById(R.id.common_ingredient_nutrition_category_nutrition_item_nutrition_value_tv);
            ingredient_view_nutrition_category_nutrition_item_nutrition_uom_tv = view.findViewById(R.id.common_ingredient_nutrition_category_nutrition_item_nutrition_uom_tv);
        }
    }
}