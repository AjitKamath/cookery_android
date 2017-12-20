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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAddIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeAddIngredientsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeAddIngredientsRecyclerViewAdapter.class.getName();
    private Context mContext;

    public List<IngredientMO> ingredients;
    private View.OnClickListener listener;

    public RecipeAddIngredientsRecyclerViewAdapter(Context mContext, List<IngredientMO> ingredients, View.OnClickListener listener) {
        this.mContext = mContext;
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_add_ingredients_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IngredientMO ingredient = ingredients.get(position);

        if(ingredient.getIMG() != null){
            Utility.loadImageFromURL(mContext, ingredient.getIMG(), holder.recipe_add_ingredients_item_iv);
        }

        holder.recipe_add_ingredients_item_tv.setText(ingredient.getING_NAME().toUpperCase());
        holder.recipe_add_ingredients_item_qty_tv.setText(String.valueOf(ingredient.getQTY()));
        holder.recipe_add_ingredients_item_qty_type_tv.setText(ingredient.getQuantity().getQTY_NAME().toUpperCase());

        holder.recipe_add_ingredients_item_edit_iv.setTag(ingredient);
        holder.recipe_add_ingredients_item_delete_iv.setTag(ingredient);

        holder.recipe_add_ingredients_item_edit_iv.setOnClickListener(listener);
        holder.recipe_add_ingredients_item_delete_iv.setOnClickListener(listener);

        /*divider*/
        if(position == ingredients.size()-1){
            holder.recipe_add_ingredients_item_divider_view.setVisibility(View.GONE);
        }
        else{
            holder.recipe_add_ingredients_item_divider_view.setVisibility(View.VISIBLE);
        }
        /*divider*/
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void addData(IngredientMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        if(!ingredients.isEmpty()){
            //check if the ingredien is already added, if yes, bring it on top
            for(IngredientMO thisIngredient: ingredients){
                if(thisIngredient.getING_NAME().equalsIgnoreCase(ingredient.getING_NAME())){
                    ingredients.remove(thisIngredient);
                    break;
                }
            }
        }

        ingredients.add(0, ingredient);
        notifyDataSetChanged();
    }

    public void removeData(IngredientMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        ingredients.remove(ingredient);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView recipe_add_ingredients_item_iv;
        public TextView recipe_add_ingredients_item_tv;
        public TextView recipe_add_ingredients_item_qty_tv;
        public TextView recipe_add_ingredients_item_qty_type_tv;
        public ImageView recipe_add_ingredients_item_edit_iv;
        public ImageView recipe_add_ingredients_item_delete_iv;
        public View recipe_add_ingredients_item_divider_view;

        public ViewHolder(View view) {
            super(view);
            recipe_add_ingredients_item_iv = view.findViewById(R.id.recipe_add_ingredients_item_iv);
            recipe_add_ingredients_item_tv = view.findViewById(R.id.recipe_add_ingredients_item_tv);
            recipe_add_ingredients_item_qty_tv = view.findViewById(R.id.recipe_add_ingredients_item_qty_tv);
            recipe_add_ingredients_item_qty_type_tv = view.findViewById(R.id.recipe_add_ingredients_item_qty_type_tv);
            recipe_add_ingredients_item_edit_iv = view.findViewById(R.id.recipe_add_ingredients_item_edit_iv);
            recipe_add_ingredients_item_delete_iv = view.findViewById(R.id.recipe_add_ingredients_item_delete_iv);
            recipe_add_ingredients_item_divider_view = view.findViewById(R.id.recipe_add_ingredients_item_divider_view);
        }
    }
}