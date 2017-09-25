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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientMO;
import com.cookery.models.RecipeMO;

import java.util.List;

public class FavRecipesRecyclerViewAdapter extends RecyclerView.Adapter<FavRecipesRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = FavRecipesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<RecipeMO> recipes;
    private View.OnClickListener listener;

    public FavRecipesRecyclerViewAdapter(Context mContext, List<RecipeMO> recipes, View.OnClickListener listener) {
        this.mContext = mContext;
        this.recipes = recipes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_fav_recipes_recipe, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecipeMO recipe = recipes.get(position);

        if(recipe.getImagesList() != null && !recipe.getImagesList().isEmpty()){
            holder.view_pager_fav_recipes_recipe_iv.setImageBitmap(recipe.getImagesList().get(0));
        }

        holder.view_pager_fav_recipes_recipe_tv.setText(recipe.getRCP_NAME().toUpperCase());
        holder.view_pager_fav_recipes_recipe_food_type_tv.setText(recipe.getFOOD_TYP_NAME().toUpperCase());
        holder.view_pager_fav_recipes_recipe_cuisine_tv.setText(recipe.getFOOD_CSN_NAME().toUpperCase());
        holder.view_pager_fav_recipes_recipe_username_tv.setText(recipe.getNAME());

        holder.view_pager_fav_recipes_recipe_rl.setTag(recipe);
        holder.view_pager_fav_recipes_recipe_rl.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout view_pager_fav_recipes_recipe_rl;
        public ImageView view_pager_fav_recipes_recipe_iv;
        public TextView view_pager_fav_recipes_recipe_tv;
        public TextView view_pager_fav_recipes_recipe_food_type_tv;
        public TextView view_pager_fav_recipes_recipe_cuisine_tv;
        public TextView view_pager_fav_recipes_recipe_username_tv;

        public ViewHolder(View view) {
            super(view);
            view_pager_fav_recipes_recipe_rl = view.findViewById(R.id.view_pager_fav_recipes_recipe_rl);
            view_pager_fav_recipes_recipe_iv = view.findViewById(R.id.view_pager_fav_recipes_recipe_iv);
            view_pager_fav_recipes_recipe_tv = view.findViewById(R.id.view_pager_fav_recipes_recipe_tv);
            view_pager_fav_recipes_recipe_food_type_tv = view.findViewById(R.id.view_pager_fav_recipes_recipe_food_type_tv);
            view_pager_fav_recipes_recipe_cuisine_tv = view.findViewById(R.id.view_pager_fav_recipes_recipe_cuisine_tv);
            view_pager_fav_recipes_recipe_username_tv = view.findViewById(R.id.view_pager_fav_recipes_recipe_username_tv);
        }
    }
}