package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.fragments.SelectionFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.utils.TestData;

import java.io.Serializable;
import java.util.List;

import static com.cookery.utils.Constants.AFFIRMATIVE;
import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_SELECTION;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.TestData.foodTypes;

public class AddRecipeViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private FragmentManager fragmentManager;
    private List<Integer> layoutsList;

    private LinearLayout view_pager_add_recipe_2_food_type_ll;
    private LinearLayout view_pager_add_recipe_3_cuisine_ll;

    private View.OnClickListener clickListener;

    public AddRecipeViewPagerAdapter(Context context, FragmentManager fragmentManager, List<Integer> layoutsList, View.OnClickListener clickListener) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        this.layoutsList = layoutsList;
        this.clickListener = clickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layoutsList.get(position), collection, false);

        switch(position){
            case 0: break;
            case 1: setupAddRecipe2(layout); break;
            case 2: setupAddRecipe3(layout); break;
            case 3: setupAddRecipe4(layout); break;
            case 8: setupAddRecipe7(layout); break;
        }

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupAddRecipe3(ViewGroup layout) {
        //TODO: get it from the db
        final List<CuisineMO> cuisines = TestData.cuisines;

        view_pager_add_recipe_3_cuisine_ll = layout.findViewById(R.id.view_pager_add_recipe_3_cuisine_ll);

        setCuisine(getDefaultCuisine(cuisines));

        view_pager_add_recipe_3_cuisine_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCuisinesSelectionFragment(cuisines);
            }
        });
    }

    private void setupAddRecipe4(ViewGroup layout) {
        //TODO: get this from db
        final List<IngredientMO> dogsList = TestData.ingredients;

        final   AutoCompleteTextView autoCompleteTV = (AutoCompleteTextView) layout.findViewById(R.id.view_pager_add_recipe_ingredients_av);
        IngredientAutoCompleteAdapter adapter = new IngredientAutoCompleteAdapter(mContext, dogsList);
        autoCompleteTV.setAdapter(adapter);

        autoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                autoCompleteTV.setText(((IngredientMO)view.getTag()).getBreed());
            }
        });
    }

    private void setupAddRecipe7(ViewGroup layout) {
        LinearLayout view_pager_add_recipe_7_finish_ll = layout.findViewById(R.id.view_pager_add_recipe_7_finish_ll);

        view_pager_add_recipe_7_finish_ll.setOnClickListener(clickListener);
    }

    private void setupAddRecipe2(ViewGroup layout) {
        //TODO: get it from the db
        final List<FoodTypeMO> foodTypes = TestData.foodTypes;

        view_pager_add_recipe_2_food_type_ll = layout.findViewById(R.id.view_pager_add_recipe_2_food_type_ll);

        setFoodType(getDefaultFoodType(foodTypes));

        view_pager_add_recipe_2_food_type_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFoodTypeSelectionFragment(foodTypes);
            }
        });
    }

    private void showCuisinesSelectionFragment(List<CuisineMO> cuisines) {
        String fragmentNameStr = FRAGMENT_COMMON_SELECTION;
        String parentFragmentNameStr = FRAGMENT_ADD_RECIPE;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }

        SelectionFragment fragment = new SelectionFragment();

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, (Serializable) cuisines);
        fragment.setArguments(bundle);

        fragment.show(fragmentManager, fragmentNameStr);
    }

    private void showFoodTypeSelectionFragment(List<FoodTypeMO> foodTypes) {
        String fragmentNameStr = FRAGMENT_COMMON_SELECTION;
        String parentFragmentNameStr = FRAGMENT_ADD_RECIPE;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }

        SelectionFragment fragment = new SelectionFragment();

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, (Serializable) foodTypes);
        fragment.setArguments(bundle);

        fragment.show(fragmentManager, fragmentNameStr);
    }

    public  void setCuisine(CuisineMO defaultCuisine) {
        ImageView image = view_pager_add_recipe_3_cuisine_ll.findViewById(R.id.view_pager_add_recipe_3_cuisine_iv);
        TextView text = view_pager_add_recipe_3_cuisine_ll.findViewById(R.id.view_pager_add_recipe_3_cuisine_tv);

        text.setText(defaultCuisine.getFood_csn_name());

        view_pager_add_recipe_3_cuisine_ll.setTag(defaultCuisine);
    }


    public void setFoodType(FoodTypeMO foodType) {
        ImageView image = view_pager_add_recipe_2_food_type_ll.findViewById(R.id.view_pager_add_recipe_2_food_type_iv);
        TextView text = view_pager_add_recipe_2_food_type_ll.findViewById(R.id.view_pager_add_recipe_2_food_type_tv);

        text.setText(foodType.getFood_typ_name());

        view_pager_add_recipe_2_food_type_ll.setTag(foodType);
    }

    public FoodTypeMO getSelectedFoodType(){
        return (FoodTypeMO) view_pager_add_recipe_2_food_type_ll.getTag();
    }

    private CuisineMO getDefaultCuisine(List<CuisineMO> cuisines) {
        for(CuisineMO cuisine : cuisines){
            if(AFFIRMATIVE.equalsIgnoreCase(cuisine.getIs_def())){
                return cuisine;
            }
        }

        return null;
    }


    private FoodTypeMO getDefaultFoodType(List<FoodTypeMO> foodTypes){
        for(FoodTypeMO foodType : foodTypes){
            if(AFFIRMATIVE.equalsIgnoreCase(foodType.getIs_def())){
                return foodType;
            }
        }
        
        return null;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(layoutsList.get(position));
    }


    @Override
    public int getCount() {
        return layoutsList.size();
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