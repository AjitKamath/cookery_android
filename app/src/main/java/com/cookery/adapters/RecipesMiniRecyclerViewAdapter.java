package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.RecipeMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipesMiniRecyclerViewAdapter extends RecyclerView.Adapter<RecipesMiniRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipesMiniRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<RecipeMO> recipes;
    private View.OnClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;
    private String purpose;

    public RecipesMiniRecyclerViewAdapter(Context mContext, List<RecipeMO> recipes, String purpose, View.OnClickListener listener) {
        this.mContext = mContext;
        this.recipes = recipes;
        this.purpose = purpose;
        this.listener = listener;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_recipes_recipe_mini, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position ==  recipes.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        final RecipeMO recipe = recipes.get(position);

        if(recipe.getImages() != null && !recipe.getImages().isEmpty()){
            Utility.loadImageFromURL(mContext, recipe.getImages().get(0).getRCP_IMG(), holder.view_pager_recipes_recipe_mini_iv);
        }

        holder.view_pager_recipes_recipe_mini_tv.setText(recipe.getRCP_NAME().toUpperCase());
        holder.view_pager_recipes_recipe_mini_food_type_tv.setText(recipe.getFoodTypeName().toUpperCase());
        holder.view_pager_recipes_recipe_mini_cuisine_tv.setText(recipe.getFoodCuisineName().toUpperCase());
        holder.view_pager_recipes_recipe_mini_rating_tv.setText(recipe.getAvgRating());
        holder.view_pager_recipes_recipe_mini_views_tv.setText(String.valueOf(recipe.getViewsCount()));
        holder.view_pager_recipes_recipe_mini_likes_tv.setText(String.valueOf(recipe.getLikesCount()));

        holder.view_pager_recipes_recipe_mini_date_time_tv.setText(DateTimeUtility.getCreateOrModifiedTime(recipe.getCREATE_DTM(), recipe.getMOD_DTM()));

        if("MY_RECIPES".equalsIgnoreCase(purpose)){
            holder.view_pager_recipes_recipe_mini_username_tv.setVisibility(View.GONE);
        }
        else{
            holder.view_pager_recipes_recipe_mini_username_tv.setVisibility(View.VISIBLE);
            holder.view_pager_recipes_recipe_mini_username_tv.setText(recipe.getUserName());
        }

        holder.view_pager_recipes_recipe_mini_rl.setTag(recipe);
        holder.view_pager_recipes_recipe_mini_rl.setOnClickListener(listener);
        holder.view_pager_recipes_recipe_options_iv.setOnClickListener(listener);

        setFont(holder.view_pager_recipes_recipe_mini_rl);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(List<RecipeMO> myRecipes, int index) {
        if(recipes == null){
            recipes = new ArrayList<>();
        }

        if(index == 0){
            recipes.clear();
        }

        recipes.addAll(myRecipes);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout view_pager_recipes_recipe_mini_rl;
        public ImageView view_pager_recipes_recipe_mini_iv;
        public TextView view_pager_recipes_recipe_mini_tv;
        public TextView view_pager_recipes_recipe_mini_food_type_tv;
        public TextView view_pager_recipes_recipe_mini_cuisine_tv;
        public TextView view_pager_recipes_recipe_mini_username_tv;
        public TextView view_pager_recipes_recipe_mini_rating_tv;
        public TextView view_pager_recipes_recipe_mini_views_tv;
        public TextView view_pager_recipes_recipe_mini_likes_tv;
        public TextView view_pager_recipes_recipe_mini_date_time_tv;
        public ImageView view_pager_recipes_recipe_options_iv;

        public ViewHolder(View view) {
            super(view);
            view_pager_recipes_recipe_mini_rl = view.findViewById(R.id.view_pager_recipes_recipe_mini_rl);
            view_pager_recipes_recipe_mini_iv = view.findViewById(R.id.view_pager_recipes_recipe_mini_iv);
            view_pager_recipes_recipe_mini_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_tv);
            view_pager_recipes_recipe_mini_food_type_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_food_type_tv);
            view_pager_recipes_recipe_mini_cuisine_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_cuisine_tv);
            view_pager_recipes_recipe_mini_username_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_username_tv);
            view_pager_recipes_recipe_mini_rating_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_rating_tv);
            view_pager_recipes_recipe_mini_views_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_views_tv);
            view_pager_recipes_recipe_mini_likes_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_likes_tv);
            view_pager_recipes_recipe_mini_date_time_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_date_time_tv);
            view_pager_recipes_recipe_options_iv = view.findViewById(R.id.view_pager_recipes_recipe_options_iv);
        }
    }

    private void setFont(ViewGroup group) {
        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}