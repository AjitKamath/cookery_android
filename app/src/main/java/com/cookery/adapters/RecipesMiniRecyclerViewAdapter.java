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
import com.cookery.models.RecipeMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.Date;
import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

public class RecipesMiniRecyclerViewAdapter extends RecyclerView.Adapter<RecipesMiniRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipesMiniRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<RecipeMO> recipes;
    private View.OnClickListener listener;
    private String purpose;

    public RecipesMiniRecyclerViewAdapter(Context mContext, List<RecipeMO> recipes, String purpose, View.OnClickListener listener) {
        this.mContext = mContext;
        this.recipes = recipes;
        this.purpose = purpose;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_recipes_recipe_mini, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecipeMO recipe = recipes.get(position);

        if(recipe.getRCP_IMGS() != null && !recipe.getRCP_IMGS().isEmpty()){
            Utility.loadImageFromURL(mContext, recipe.getRCP_IMGS().get(0), holder.view_pager_recipes_recipe_mini_iv);
        }

        holder.view_pager_recipes_recipe_mini_tv.setText(recipe.getRCP_NAME().toUpperCase());
        holder.view_pager_recipes_recipe_mini_food_type_tv.setText(recipe.getFOOD_TYP_NAME().toUpperCase());
        holder.view_pager_recipes_recipe_mini_cuisine_tv.setText(recipe.getFOOD_CSN_NAME().toUpperCase());
        holder.view_pager_recipes_recipe_mini_rating_tv.setText(recipe.getRating());
        holder.view_pager_recipes_recipe_mini_views_tv.setText(Utility.getSmartNumber(recipe.getViews()));
        holder.view_pager_recipes_recipe_mini_likes_tv.setText(Utility.getSmartNumber(recipe.getLikes()));

        if(recipe.getMOD_DTM() != null && !recipe.getMOD_DTM().trim().isEmpty()){
            Date date = DateTimeUtility.convertStringToDateTime(recipe.getMOD_DTM(), DB_DATE_TIME);
            holder.view_pager_recipes_recipe_mini_date_time_tv.setText(DateTimeUtility.getSmartDateTime(date));
        }
        else if(recipe.getCREATE_DTM() != null && !recipe.getCREATE_DTM().trim().isEmpty()){
            Date date = DateTimeUtility.convertStringToDateTime(recipe.getCREATE_DTM(), DB_DATE_TIME);
            holder.view_pager_recipes_recipe_mini_date_time_tv.setText(DateTimeUtility.getSmartDateTime(date));
        }

        if("MY_RECIPES".equalsIgnoreCase(purpose)){
            holder.view_pager_recipes_recipe_mini_username_tv.setVisibility(View.GONE);
        }
        else{
            holder.view_pager_recipes_recipe_mini_username_tv.setVisibility(View.VISIBLE);
            holder.view_pager_recipes_recipe_mini_username_tv.setText(recipe.getNAME());
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