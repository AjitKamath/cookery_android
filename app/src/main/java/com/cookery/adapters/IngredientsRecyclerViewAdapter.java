package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientMO;
import com.cookery.utils.Utility;

import java.util.List;

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

        if(ingredient.getIMG() != null){
            Utility.loadImageFromURL(mContext, ingredient.getIMG(), holder.view_pager_recipe_ingredients_item_iv);
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