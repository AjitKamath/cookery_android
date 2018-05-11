package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.IngredientNutritionMO;
import com.cookery.models.NutritionCategoryMO;
import com.cookery.models.RecipeMO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = RecipeViewViewPagerAdapter.class.getName();

    private List<Integer> layouts;
    public RecipeMO recipe;
    private View.OnClickListener onClickListener;
    private FragmentManager manager;

    public RecipeViewViewPagerAdapter(Context context,FragmentManager manager, List<Integer> layouts, RecipeMO recipe, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.layouts = layouts;
        this.recipe = recipe;
        this.manager = manager;
        this.onClickListener = onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupRecipeSteps(layout); break;
            case 1: setupRecipeIngredients(layout); break;
            case 2: setupRecipeNutritions(layout); break;
            default: break;
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupRecipeNutritions(ViewGroup layout) {
        List<NutritionCategoryMO> ingredientNutritionCategories = buildList();

        RecyclerView recipe_view_nutrition_rv = layout.findViewById(R.id.recipe_view_nutrition_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_view_nutrition_rv.setLayoutManager(mLayoutManager);
        recipe_view_nutrition_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_view_nutrition_rv.setAdapter(new IngredientViewNutritionCategoriesRecyclerViewAdapter(mContext, ingredientNutritionCategories, onClickListener));
    }

    private List<NutritionCategoryMO> buildList(){
        Map<String, Map<String, IngredientNutritionMO>> nutritionMap = new HashMap<>();

        for(IngredientAkaMO ingredient : recipe.getIngredients()){
            List<NutritionCategoryMO> ingredientNutritionCategories = ingredient.getIngredientNutritionCategories();

            for(NutritionCategoryMO ingredientNutritionCategory : ingredientNutritionCategories){
                Map<String, IngredientNutritionMO> tempMap = null;
                if(nutritionMap.containsKey(ingredientNutritionCategory.getNUT_CAT_NAME())){
                    tempMap = nutritionMap.get(ingredientNutritionCategory.getNUT_CAT_NAME());
                }
                else{
                    tempMap = new HashMap<>();
                }

                for(IngredientNutritionMO ingredientNutrition : ingredientNutritionCategory.getIngredientNutritions()){
                    IngredientNutritionMO tempNutrition = ingredientNutrition;

                    if(tempMap.containsKey(ingredientNutrition.getIngredientNutritionName())){
                        tempNutrition = tempMap.get(ingredientNutrition.getIngredientNutritionName());
                        Double tempVal = Double.parseDouble(tempNutrition.getING_NUT_VAL());
                        tempVal += Double.parseDouble(ingredientNutrition.getING_NUT_VAL());

                        tempNutrition.setING_NUT_VAL(String.format("%.2f", tempVal));
                    }

                    tempMap.put(ingredientNutrition.getIngredientNutritionName(), tempNutrition);
                }

                nutritionMap.put(ingredientNutritionCategory.getNUT_CAT_NAME(), tempMap);
            }
        }

        List<NutritionCategoryMO> ingredientNutritionCategories = new ArrayList<>();
        for(Map.Entry<String, Map<String, IngredientNutritionMO>> item : nutritionMap.entrySet()){
            List<IngredientNutritionMO> ingredientNutritions = new ArrayList<>();
            for(Map.Entry<String, IngredientNutritionMO> item1 : item.getValue().entrySet()){
                ingredientNutritions.add(item1.getValue());
            }

            NutritionCategoryMO ingCategory = new NutritionCategoryMO();
            ingCategory.setNUT_CAT_NAME(item.getKey());
            ingCategory.setIngredientNutritions(ingredientNutritions);

            ingredientNutritionCategories.add(ingCategory);
        }

        return ingredientNutritionCategories;
    }

    private void setupRecipeIngredients(ViewGroup layout) {
        RecyclerView view_pager_recipe_ingredients_rv = layout.findViewById(R.id.recipe_view_ingredients_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        view_pager_recipe_ingredients_rv.setLayoutManager(mLayoutManager);
        view_pager_recipe_ingredients_rv.setItemAnimator(new DefaultItemAnimator());
        view_pager_recipe_ingredients_rv.setAdapter(new RecipeViewIngredientsRecyclerViewAdapter(mContext,manager,recipe.getIngredients(), recipe.getMylists(), onClickListener));
    }

    private void setupRecipeSteps(ViewGroup layout) {
        ImageView recipe_view_recipe_steps_fullscreen_iv = layout.findViewById(R.id.recipe_view_recipe_steps_fullscreen_iv);
        RecyclerView recipe_view_recipe_steps_rv = layout.findViewById(R.id.recipe_view_recipe_steps_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_view_recipe_steps_rv.setLayoutManager(mLayoutManager);
        recipe_view_recipe_steps_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_view_recipe_steps_rv.setAdapter(new RecipeViewStepsRecyclerViewAdapter(mContext, recipe.getSteps(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));

        recipe_view_recipe_steps_fullscreen_iv.setOnClickListener(onClickListener);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "RECIPE";
            case 1: return "INGREDIENTS";
            case 2: return "NUTRITION";
            default: return "ERROR";
        }
    }


    @Override
    public int getCount() {
        return layouts.size();
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