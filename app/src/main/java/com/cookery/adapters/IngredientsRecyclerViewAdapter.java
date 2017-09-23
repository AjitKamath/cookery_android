package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientMO;
import com.cookery.models.RecipeMO;

import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.TOP_RECIPES_CHEF;
import static com.cookery.utils.Constants.TOP_RECIPES_MONTH;
import static com.cookery.utils.Constants.TRENDING_RECIPES;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = IngredientsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<IngredientMO> ingredients;

    public IngredientsRecyclerViewAdapter(Context mContext, List<IngredientMO> ingredients) {
        this.mContext = mContext;
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_recipe_ingredients_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IngredientMO ingredient = ingredients.get(position);

        if(ingredient.getImage() != null){
            holder.view_pager_recipe_ingredients_item_iv.setImageBitmap(ingredient.getImage());
        }

        holder.view_pager_recipe_ingredients_item_tv.setText(ingredient.getING_NAME().toUpperCase());
        holder.view_pager_recipe_ingredients_item_qty_tv.setText(String.valueOf(ingredient.getING_QTY()));
        holder.view_pager_recipe_ingredients_item_qty_type_tv.setText(ingredient.getQTY_NAME().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView view_pager_recipe_ingredients_item_iv;
        public TextView view_pager_recipe_ingredients_item_tv;
        public TextView view_pager_recipe_ingredients_item_qty_tv;
        public TextView view_pager_recipe_ingredients_item_qty_type_tv;

        public ViewHolder(View view) {
            super(view);
            view_pager_recipe_ingredients_item_iv = view.findViewById(R.id.view_pager_recipe_ingredients_item_iv);
            view_pager_recipe_ingredients_item_tv = view.findViewById(R.id.view_pager_recipe_ingredients_item_tv);
            view_pager_recipe_ingredients_item_qty_tv = view.findViewById(R.id.view_pager_recipe_ingredients_item_qty_tv);
            view_pager_recipe_ingredients_item_qty_type_tv = view.findViewById(R.id.view_pager_recipe_ingredients_item_qty_type_tv);
        }
    }
}