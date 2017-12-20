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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.fragments.AddRecipeIngredientQuantityFragment;
import com.cookery.fragments.SelectionFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.AFFIRMATIVE;
import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_SELECTION;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LIST_DATA;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_VIEW;

public class RecipeAddViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = RecipeAddViewPagerAdapter.class.getName();

    private FragmentManager fragmentManager;
    private List<Integer> layoutsList;

    /*components*/
    /*common*/
    private TextView common_component_add_recipe_heading_tv;
    /*common*/

    /*recipe main*/
    private LinearLayout recipe_add_recipe_name_ll;
    private TextView recipe_add_recipe_main_recipe_name_tv;
    private EditText recipe_add_recipe_main_recipe_name_et;
    private LinearLayout recipe_add_recipe_main_recipe_type_ll;
    private TextView recipe_add_recipe_main_recipe_type_tv;
    private LinearLayout recipe_add_recipe_main_recipe_cuisine_ll;
    private TextView recipe_add_recipe_main_recipe_cuisine_tv;
    /*recipe main*/

    /*ingredients*/
    private AutoCompleteTextView recipe_add_ingredients_act;
    private ImageView recipe_add_ingredients_iv;
    private TextView recipe_add_ingredients_no_ingredients_tv;
    private TextView recipe_add_ingredients_count_tv;
    private RecyclerView recipe_add_ingredients_ingredients_rv;
    /*ingredients*/

    /*steps*/
    private TextView recipe_add_recipe_step_tv;
    private ImageView recipe_add_recipe_step_clear_iv;
    private ImageView recipe_add_recipe_step_add_iv;
    private EditText recipe_add_recipe_step_et;
    private TextView recipe_add_recipe_step_text_count_tv;
    private TextView recipe_add_recipe_steps_no_step_tv;
    private TextView recipe_add_recipe_steps_count_tv;
    private RecyclerView recipe_add_recipe_steps_rv;
    /*steps*/

    /*components*/

    private MasterDataMO masterData;
    public RecipeMO recipe;

    public RecipeAddViewPagerAdapter(Context context, FragmentManager fragmentManager, List<Integer> layoutsList, RecipeMO recipe, MasterDataMO masterData) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        this.layoutsList = layoutsList;
        this.recipe = recipe;
        this.masterData = masterData;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layoutsList.get(position), collection, false);

        setupCommon(layout);

        switch(position){
            case 0: setupAddRecipeMain(layout); break;
            case 1: setupAddRecipeIngredients(layout); break;
            case 2: setupAddRecipeSteps(layout); break;
            case 3: setupAddRecipeTastes(layout); break;
        }

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupCommon(ViewGroup layout) {
        common_component_add_recipe_heading_tv = layout.findViewById(R.id.common_component_add_recipe_heading_tv);
    }

    private void setupAddRecipeSteps(ViewGroup layout) {
        recipe_add_recipe_step_tv = layout.findViewById(R.id.recipe_add_recipe_step_tv);
        recipe_add_recipe_step_add_iv = layout.findViewById(R.id.recipe_add_recipe_step_add_iv);
        recipe_add_recipe_step_clear_iv = layout.findViewById(R.id.recipe_add_recipe_step_clear_iv);
        recipe_add_recipe_step_et = layout.findViewById(R.id.recipe_add_recipe_step_et);
        recipe_add_recipe_step_text_count_tv = layout.findViewById(R.id.recipe_add_recipe_step_text_count_tv);
        recipe_add_recipe_steps_no_step_tv = layout.findViewById(R.id.recipe_add_recipe_steps_no_step_tv);
        recipe_add_recipe_steps_count_tv = layout.findViewById(R.id.recipe_add_recipe_steps_count_tv);
        recipe_add_recipe_steps_rv = layout.findViewById(R.id.recipe_add_recipe_steps_rv);

        common_component_add_recipe_heading_tv.setText("WRITE UP YOUR RECIPE");

        recipe_add_recipe_step_clear_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStep();
            }
        });

        recipe_add_recipe_step_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user is trying to update a old step
                if(view.getTag(R.id.recipe_add_recipe_step_add_iv) != null){
                    String step[] = (String[])view.getTag(R.id.recipe_add_recipe_step_add_iv);
                    step[1] = String.valueOf(recipe_add_recipe_step_et.getText()).trim();

                    updateStep(step);
                    resetStep();

                    view.setTag(null);
                }
                //user is trying to add a new step
                else{
                    if(!String.valueOf(recipe_add_recipe_step_et.getText()).trim().isEmpty()){
                        if(recipe.getSteps() == null){
                            recipe.setSteps(new ArrayList<String>());
                        }

                        addStep(String.valueOf(recipe_add_recipe_step_et.getText()).trim());
                        resetStep();
                    }
                }
            }
        });

        recipe_add_recipe_step_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateStepCount();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_add_recipe_steps_rv.setLayoutManager(mLayoutManager);
        recipe_add_recipe_steps_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_add_recipe_steps_rv.setAdapter(new RecipeAddStepsRecyclerViewAdapter(mContext, new ArrayList<String>(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.recipe_add_recipe_steps_item_delete_iv == view.getId()){
                    String step = String.valueOf(view.getTag());
                    removeStep(step);
                }
                else if(R.id.recipe_add_recipe_steps_item_edit_iv == view.getId()){
                    String step[] = (String[])view.getTag();
                    recipe_add_recipe_step_add_iv.setTag(R.id.recipe_add_recipe_step_add_iv, step);
                    recipe_add_recipe_step_et.setText(step[1]);
                    recipe_add_recipe_step_tv.setText("STEP "+(Integer.parseInt(step[0])+1));

                    updateStepCount();
                }
            }
        }));

        updateSteps();
        updateStepCount();
    }

    private void updateSteps(){
        if(recipe.getSteps() == null || recipe.getSteps().isEmpty()){
            recipe_add_recipe_step_tv.setText("STEP 1");
            recipe_add_recipe_steps_no_step_tv.setVisibility(View.VISIBLE);
            recipe_add_recipe_steps_count_tv.setVisibility(View.GONE);
            recipe_add_recipe_steps_rv.setVisibility(View.GONE);
        }
        else{
            recipe_add_recipe_step_tv.setText("STEP "+(recipe.getSteps().size()+1));

            if(recipe.getSteps().size() == 1){
                recipe_add_recipe_steps_count_tv.setText(recipe.getSteps().size()+" STEP");
            }
            else{
                recipe_add_recipe_steps_count_tv.setText(recipe.getSteps().size()+" STEPS");
            }

            recipe_add_recipe_steps_no_step_tv.setVisibility(View.GONE);
            recipe_add_recipe_steps_count_tv.setVisibility(View.VISIBLE);
            recipe_add_recipe_steps_rv.setVisibility(View.VISIBLE);
        }
    }

    private void resetStep(){
        recipe_add_recipe_step_et.setText("");
        updateStepCount();
    }

    private void updateStepCount(){
        recipe_add_recipe_step_text_count_tv.setText(recipe_add_recipe_step_et.getText().length()+"/"+mContext.getResources().getInteger(R.integer.recipe_step_text_limit));
    }

    private void addStep(String step){
        RecipeAddStepsRecyclerViewAdapter adapter = (RecipeAddStepsRecyclerViewAdapter)recipe_add_recipe_steps_rv.getAdapter();
        adapter.addData(step);

        recipe.setSteps(adapter.steps);
        updateSteps();
    }

    private void removeStep(String step){
        RecipeAddStepsRecyclerViewAdapter adapter = (RecipeAddStepsRecyclerViewAdapter)recipe_add_recipe_steps_rv.getAdapter();
        adapter.removeData(step);

        recipe.setSteps(adapter.steps);
        updateSteps();
    }

    private void updateStep(String[] step){
        RecipeAddStepsRecyclerViewAdapter adapter = (RecipeAddStepsRecyclerViewAdapter)recipe_add_recipe_steps_rv.getAdapter();
        adapter.updateData(step);

        recipe.setSteps(adapter.steps);
        updateSteps();
    }

    private void setupAddRecipeMain(ViewGroup layout) {
        recipe_add_recipe_name_ll = layout.findViewById(R.id.recipe_add_recipe_name_ll);
        recipe_add_recipe_main_recipe_name_tv = layout.findViewById(R.id.recipe_add_recipe_main_recipe_name_tv);
        recipe_add_recipe_main_recipe_name_et = layout.findViewById(R.id.recipe_add_recipe_main_recipe_name_et);
        recipe_add_recipe_main_recipe_type_ll = layout.findViewById(R.id.recipe_add_recipe_main_recipe_type_ll);
        recipe_add_recipe_main_recipe_type_tv = layout.findViewById(R.id.recipe_add_recipe_main_recipe_type_tv);
        recipe_add_recipe_main_recipe_cuisine_ll = layout.findViewById(R.id.recipe_add_recipe_main_recipe_cuisine_ll);
        recipe_add_recipe_main_recipe_cuisine_tv = layout.findViewById(R.id.recipe_add_recipe_main_recipe_cuisine_tv);

        common_component_add_recipe_heading_tv.setText("TELL US ABOUT YOUR RECIPE");

        recipe_add_recipe_main_recipe_name_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        /*setup food type*/
        final List<FoodTypeMO> foodTypes = masterData.getFoodTypes();
        setFoodType(getDefaultFoodType(foodTypes));

        recipe_add_recipe_main_recipe_type_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFoodTypeSelectionFragment(foodTypes);
            }
        });
        /*setup food type*/

        /*setup cuisine*/
        final List<CuisineMO> cuisines = masterData.getCuisines();
        setCuisine(getDefaultCuisine(cuisines));

        recipe_add_recipe_main_recipe_cuisine_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCuisinesSelectionFragment(cuisines);
            }
        });
        /*setup cuisine*/
    }

    private void setupAddRecipeIngredients(ViewGroup layout) {
        recipe_add_ingredients_act = layout.findViewById(R.id.recipe_add_ingredients_act);
        recipe_add_ingredients_no_ingredients_tv = layout.findViewById(R.id.recipe_add_ingredients_no_ingredients_tv);
        recipe_add_ingredients_iv = layout.findViewById(R.id.recipe_add_ingredients_iv);
        recipe_add_ingredients_count_tv = layout.findViewById(R.id.recipe_add_ingredients_count_tv);
        recipe_add_ingredients_ingredients_rv = layout.findViewById(R.id.recipe_add_ingredients_ingredients_rv);

        common_component_add_recipe_heading_tv.setText("WHAT GOES INTO YOUR RECIPE");

        /*auto complete*/
        recipe_add_ingredients_act.setAdapter(new AutoCompleteAdapter(mContext, R.layout.ingredient_autocomplete_item, "INGREDIENTS"));
        recipe_add_ingredients_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recipe_add_ingredients_act.getText().length() == 0){
                    return;
                }

                IngredientMO ingredient = new IngredientMO();
                ingredient.setING_NAME(String.valueOf(recipe_add_ingredients_act.getText()));

                showQuantityFragment(ingredient);
            }
        });
        recipe_add_ingredients_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQuantityFragment((IngredientMO)view.getTag());
            }
        });
        /*auto complete*/

        /*ingredients list*/
        RecipeAddIngredientsRecyclerViewAdapter adapter = new RecipeAddIngredientsRecyclerViewAdapter(mContext, new ArrayList<IngredientMO>(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.recipe_add_ingredients_item_delete_iv == view.getId()){
                    removeIngredient((IngredientMO)view.getTag());
                }
                else if(R.id.recipe_add_ingredients_item_edit_iv == view.getId()){
                    showQuantityFragment((IngredientMO)view.getTag());
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_VIEW+view);
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(adapter.getItemCount()-1);
        recipe_add_ingredients_ingredients_rv.setLayoutManager(mLayoutManager);
        recipe_add_ingredients_ingredients_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_add_ingredients_ingredients_rv.setAdapter(adapter);
        /*ingredients list*/

        updateIngredients();
    }

    private void updateIngredients(){
        if(recipe.getIngredients() == null || recipe.getIngredients().isEmpty()){
            recipe_add_ingredients_no_ingredients_tv.setVisibility(View.VISIBLE);
            recipe_add_ingredients_ingredients_rv.setVisibility(View.GONE);
            recipe_add_ingredients_count_tv.setVisibility(View.GONE);
        }
        else{
            if(recipe.getIngredients() .size() == 1){
                recipe_add_ingredients_count_tv.setText(recipe.getIngredients().size()+" INGREDIENT");
            }
            else{
                recipe_add_ingredients_count_tv.setText(recipe.getIngredients().size()+" INGREDIENTS");
            }

            recipe_add_ingredients_no_ingredients_tv.setVisibility(View.GONE);
            recipe_add_ingredients_ingredients_rv.setVisibility(View.VISIBLE);
            recipe_add_ingredients_count_tv.setVisibility(View.VISIBLE);
        }
    }

    private void setupAddRecipeTastes(ViewGroup layout) {
        /*spice ratings*/
        final List<ImageView> spicyIVList = new ArrayList<>();
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_1_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_2_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_3_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_4_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_5_iv));

        common_component_add_recipe_heading_tv.setText("DEFINE HOW YOUR RECIPE TASTES LIKE");

        //by default set spice rating as 3
        setSpiceRating(3, spicyIVList);

        for(ImageView iter : spicyIVList){
            iter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSpiceRating(Integer.parseInt(String.valueOf(view.getTag())), spicyIVList);
                }
            });
        }
        /*spice ratings*/

        /*sweet ratings*/
        final List<ImageView> sweetIVList = new ArrayList<>();
        sweetIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_sweet_1_iv));
        sweetIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_sweet_2_iv));
        sweetIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_sweet_3_iv));
        sweetIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_sweet_4_iv));
        sweetIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_sweet_5_iv));

        //by default set sweet rating as 2
        setSweetRating(2, sweetIVList);

        for(ImageView iter : sweetIVList){
            iter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSweetRating(Integer.parseInt(String.valueOf(view.getTag())), sweetIVList);
                }
            });
        }
        /*sweet ratings*/
    }

    public void addIngredient(IngredientMO ingredient){
        RecipeAddIngredientsRecyclerViewAdapter adapter = (RecipeAddIngredientsRecyclerViewAdapter)recipe_add_ingredients_ingredients_rv.getAdapter();
        adapter.addData(ingredient);
        recipe.setIngredients(adapter.ingredients);
        recipe_add_ingredients_act.setText("");
        updateIngredients();
    }

    public void removeIngredient(IngredientMO ingredient){
        RecipeAddIngredientsRecyclerViewAdapter adapter = (RecipeAddIngredientsRecyclerViewAdapter)recipe_add_ingredients_ingredients_rv.getAdapter();
        adapter.removeData(ingredient);
        recipe.setIngredients(adapter.ingredients);
        updateIngredients();
    }

    private void showQuantityFragment(IngredientMO ingredient) {
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

        AddRecipeIngredientQuantityFragment fragment = new AddRecipeIngredientQuantityFragment();

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_ITEM, ingredient);
        bundle.putSerializable(LIST_DATA, (Serializable) masterData.getQuantities());
        fragment.setArguments(bundle);

        fragment.show(fragmentManager, fragmentNameStr);
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

    public  void setCuisine(final CuisineMO cuisine) {
        TextView text = recipe_add_recipe_main_recipe_cuisine_ll.findViewById(R.id.recipe_add_recipe_main_recipe_cuisine_tv);
        text.setText(cuisine.getFOOD_CSN_NAME());
        recipe_add_recipe_main_recipe_cuisine_ll.setTag(cuisine);

        recipe.setFOOD_CSN_ID(cuisine.getFOOD_CSN_ID());
    }

    public void setFoodType(FoodTypeMO foodType) {
        TextView text = recipe_add_recipe_main_recipe_type_ll.findViewById(R.id.recipe_add_recipe_main_recipe_type_tv);
        text.setText(foodType.getFOOD_TYP_NAME());
        recipe_add_recipe_main_recipe_type_ll.setTag(foodType);

        recipe.setFOOD_TYP_ID(foodType.getFOOD_TYP_ID());
    }

    private void setSpiceRating(int rating, List<ImageView> spiceIVList){
        for(int i=0; i<spiceIVList.size(); i++){
            if(i+1 <= rating){
                spiceIVList.get(i).setImageResource(R.drawable.spice_enabled);
            }
            else{
                spiceIVList.get(i).setImageResource(R.drawable.spice_disabled);
            }
        }

        for(TasteMO iter : masterData.getTastes()){
            if("SPICY".equalsIgnoreCase(iter.getTST_NAME())){
                iter.setQuantity(rating);
                break;
            }
        }

        setTaste();
    }

    private void setSweetRating(int rating, List<ImageView> sweetIVList){
        for(int i=0; i<sweetIVList.size(); i++){
            if(i+1 <= rating){
                sweetIVList.get(i).setImageResource(R.drawable.sweet_enabled);
            }
            else{
                sweetIVList.get(i).setImageResource(R.drawable.sweet_disabled);
            }
        }

        for(TasteMO iter : masterData.getTastes()){
            if("SWEET".equalsIgnoreCase(iter.getTST_NAME())){
                iter.setQuantity(rating);
                break;
            }
        }

        setTaste();
    }

    private void setTaste(){
        recipe.setTastes(masterData.getTastes());
    }

    public void setRecipeName(){
        recipe.setRCP_NAME(String.valueOf(recipe_add_recipe_main_recipe_name_et.getText()).trim());
    }

    private CuisineMO getDefaultCuisine(List<CuisineMO> cuisines) {
        for(CuisineMO cuisine : cuisines){
            if(AFFIRMATIVE.equalsIgnoreCase(cuisine.getIS_DEF())){
                return cuisine;
            }
        }

        return null;
    }


    private FoodTypeMO getDefaultFoodType(List<FoodTypeMO> foodTypes){
        for(FoodTypeMO foodType : foodTypes){
            if(AFFIRMATIVE.equalsIgnoreCase(foodType.getIS_DEF())){
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
        switch(position){
            case 0: return "RECIPE";
            case 1: return "ING.";
            case 2: return "STEPS";
            case 3: return "TASTES";
            default: return "UNIMPL";
        }
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