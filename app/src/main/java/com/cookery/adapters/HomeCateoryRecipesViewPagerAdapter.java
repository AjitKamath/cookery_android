package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class HomeCateoryRecipesViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = HomeCateoryRecipesViewPagerAdapter.class.getName();

    private View.OnClickListener clickListener;
    public List<RecipeMO> recipes = new ArrayList<>();

    public HomeCateoryRecipesViewPagerAdapter(Context context, List<RecipeMO> recipes, View.OnClickListener clickListener) {
        this.mContext = context;
        this.recipes = recipes;
        this.clickListener = clickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.home_categorized_recipes_recipe_item, collection, false);

        setupPage(layout, position);

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupPage(ViewGroup layout, int position) {
        TextView home_categorized_recipes_recipe_item_recipe_name_tv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_recipe_name_tv);
        TextView home_categorized_recipes_recipe_item_food_type_tv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_food_type_tv);
        TextView home_categorized_recipes_recipe_item_food_cuisine_tv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_food_cuisine_tv);
        TextView home_categorized_recipes_recipe_item_user_name_tv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_user_name_tv);
        ImageView home_categorized_recipes_recipe_item_image_iv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_image_iv);
        TextView home_categorized_recipes_recipe_item_views_tv = layout.findViewById(R.id.home_categorized_recipes_recipe_item_views_tv);

        RecipeMO recipe = recipes.get(position);

        if(recipe.getRCP_IMGS() != null && !recipe.getRCP_IMGS().isEmpty()){
            Utility.loadImageFromURL(mContext, recipe.getRCP_IMGS().get(0), home_categorized_recipes_recipe_item_image_iv);
        }

        home_categorized_recipes_recipe_item_recipe_name_tv.setText(recipe.getRCP_NAME().toUpperCase());
        home_categorized_recipes_recipe_item_food_type_tv.setText(recipe.getFOOD_TYP_NAME().toUpperCase());
        home_categorized_recipes_recipe_item_food_cuisine_tv.setText(recipe.getFOOD_CSN_NAME());
        home_categorized_recipes_recipe_item_user_name_tv.setText(recipe.getNAME());
        home_categorized_recipes_recipe_item_views_tv.setText(Utility.getSmartNumber(recipe.getViews()));

        layout.setOnClickListener(clickListener);

        layout.setTag(recipe);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return recipes.get(position).getRCP_NAME();
    }


    @Override
    public int getCount() {
        if(recipes == null){
            return 0;
        }

        return recipes.size();
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