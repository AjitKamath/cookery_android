package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.HealthMO;
import com.cookery.models.IngredientMO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.SIMPLE_KEY_UNIMPLEMENTED;
import static com.cookery.utils.Constants.UI_FONT;

public class IngredientViewViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = IngredientViewViewPagerAdapter.class.getName();

    private List<Integer> viewPagerTabsList;
    private IngredientMO ingredient;
    private View.OnClickListener listener;

    public IngredientViewViewPagerAdapter(Context context, List<Integer> viewPagerTabsList, IngredientMO ingredient) {
        this.mContext = context;
        this.viewPagerTabsList = viewPagerTabsList;
        this.ingredient = ingredient;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(viewPagerTabsList.get(position), collection, false);

        switch(position){
            case 0: setupNutritions(layout); break;
            case 1: setupHealths(layout); break;
            default:
                Log.e(CLASS_NAME, "Error ! Unimplemented ViewPager Tab");
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupNutritions(ViewGroup layout) {
        TextView ingredient_view_nutritions_ingredient_no_nutrition_tv = layout.findViewById(R.id.ingredient_view_nutritions_ingredient_no_nutrition_tv);
        RecyclerView ingredient_view_nutritions_ingredient_nutrition_category_rv = layout.findViewById(R.id.ingredient_view_nutritions_ingredient_nutrition_category_rv);

        if(ingredient.getIngredientNutritionCategories() == null || ingredient.getIngredientNutritionCategories().isEmpty()){
            ingredient_view_nutritions_ingredient_no_nutrition_tv.setVisibility(View.VISIBLE);
            return;
        }
        else{
            ingredient_view_nutritions_ingredient_no_nutrition_tv.setVisibility(View.GONE);
        }

        ingredient_view_nutritions_ingredient_nutrition_category_rv.setAdapter(
                new IngredientViewNutritionCategoriesRecyclerViewAdapter(mContext, ingredient.getIngredientNutritionCategories(),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ingredient_view_nutritions_ingredient_nutrition_category_rv.setLayoutManager(mLayoutManager);
        ingredient_view_nutritions_ingredient_nutrition_category_rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupHealths(ViewGroup layout) {
        TextView ingredient_view_healths_no_tv = layout.findViewById(R.id.ingredient_view_healths_no_tv);
        RecyclerView ingredient_view_healths_rv = layout.findViewById(R.id.ingredient_view_healths_rv);

        if(ingredient.getIngredientHealths() == null || ingredient.getIngredientHealths().isEmpty()){
            ingredient_view_healths_no_tv.setVisibility(View.VISIBLE);
            return;
        }
        else{
            ingredient_view_healths_no_tv.setVisibility(View.GONE);
        }

        Map<String, List<HealthMO>> healthMap = new HashMap<>();
        for(HealthMO health : ingredient.getIngredientHealths()){
            List<HealthMO> tempList = null;

            if(healthMap.containsKey(health.getING_HLTH_IND())){
                tempList = healthMap.get(health.getING_HLTH_IND());
            }
            else{
                tempList = new ArrayList<>();
            }
            tempList.add(health);
            healthMap.put(health.getING_HLTH_IND(), tempList);
        }

        ingredient_view_healths_rv.setAdapter(
                new CommonIngredientHealthCategoriesRecyclerViewAdapter(mContext, healthMap,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        ingredient_view_healths_rv.setLayoutManager(mLayoutManager);
        ingredient_view_healths_rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "NUTRITIONS";
            case 1: return "HEALTH FACTS";
            default: return SIMPLE_KEY_UNIMPLEMENTED;
        }
    }

    @Override
    public int getCount() {
        return viewPagerTabsList.size();
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