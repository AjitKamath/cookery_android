package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.fragments.AddRecipeIngredientQuantityFragment;
import com.cookery.fragments.SelectionFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;
import com.cookery.utils.TestData;
import com.cookery.utils.Utility;

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

public class AddRecipeViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = AddRecipeViewPagerAdapter.class.getName();

    private FragmentManager fragmentManager;
    private List<Integer> layoutsList;

    private LinearLayout view_pager_add_recipe_2_food_type_ll;
    private LinearLayout view_pager_add_recipe_3_cuisine_ll;
    private AutoCompleteTextView view_pager_add_recipe_ingredients_av;
    private ImageView view_pager_add_recipe_user_ingredients_iv;
    private ImageView view_pager_add_recipe_8_primary_photo_iv;
    private TextView view_pager_add_recipe_8_no_photos_tv;
    private LinearLayout view_pager_add_recipe_8_photos_ll;
    private GridView view_pager_add_recipe_8_photos_gv;

    public EditText view_pager_add_recipe_1_recipe_name_et;
    public GridView view_pager_add_recipe_4_ingedients_gv;
    public EditText view_pager_add_recipe_5_recipe_et;

    private View.OnClickListener clickListener;
    private MasterDataMO masterData;
    public RecipeMO recipe = new RecipeMO();

    public AddRecipeViewPagerAdapter(Context context, FragmentManager fragmentManager, List<Integer> layoutsList, MasterDataMO masterData, View.OnClickListener clickListener) {
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        this.layoutsList = layoutsList;
        this.clickListener = clickListener;
        this.masterData = masterData;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layoutsList.get(position), collection, false);

        switch(position){
            case 0: setupAddRecipe1(layout); break;
            case 1: setupAddRecipe2(layout); break;
            case 2: setupAddRecipe3(layout); break;
            case 3: setupAddRecipe4(layout); break;
            case 4: setupAddRecipe5(layout); break;
            case 6: setupAddRecipe7(layout); break;
            case 7: setupAddRecipe8(layout); break;
        }

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupAddRecipe5(ViewGroup layout) {
        view_pager_add_recipe_5_recipe_et = layout.findViewById(R.id.view_pager_add_recipe_5_recipe_et);
    }

    private void setupAddRecipe1(ViewGroup layout) {
        view_pager_add_recipe_1_recipe_name_et = layout.findViewById(R.id.view_pager_add_recipe_1_recipe_name_et);
    }

    private void setupAddRecipe7(ViewGroup layout) {
        /*spice ratings*/
        final List<ImageView> spicyIVList = new ArrayList<>();
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_1_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_2_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_3_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_4_iv));
        spicyIVList.add((ImageView) layout.findViewById(R.id.view_pager_ad_recipe_7_spice_5_iv));

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

    private void setupAddRecipe3(ViewGroup layout) {
        final List<CuisineMO> cuisines = masterData.getCuisines();

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
        view_pager_add_recipe_ingredients_av = layout.findViewById(R.id.view_pager_add_recipe_ingredients_av);
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(mContext, R.layout.ingredient_autocomplete_item, "INGREDIENTS");
        view_pager_add_recipe_ingredients_av.setAdapter(adapter);

        view_pager_add_recipe_4_ingedients_gv = layout.findViewById(R.id.view_pager_add_recipe_4_ingedients_gv);
        final IngredientsGridViewAdapter ingAdapter = new IngredientsGridViewAdapter(mContext, new ArrayList<IngredientMO>());
        view_pager_add_recipe_4_ingedients_gv.setAdapter(ingAdapter);

        view_pager_add_recipe_user_ingredients_iv = layout.findViewById(R.id.view_pager_add_recipe_user_ingredients_iv);
        view_pager_add_recipe_user_ingredients_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view_pager_add_recipe_ingredients_av.getText().length() == 0){
                    return;
                }

                IngredientMO ingredient = new IngredientMO();
                ingredient.setING_NAME(String.valueOf(view_pager_add_recipe_ingredients_av.getText()));

                showQuantityFragment(ingredient);
            }
        });

        view_pager_add_recipe_ingredients_av.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQuantityFragment((IngredientMO)view.getTag());
            }
        });
    }

    public void addIngredient(IngredientMO ingredient){
        ((IngredientsGridViewAdapter)view_pager_add_recipe_4_ingedients_gv.getAdapter()).updateDatalist(ingredient);
        view_pager_add_recipe_ingredients_av.setText("");
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


    private void setupAddRecipe8(ViewGroup layout) {
        view_pager_add_recipe_8_no_photos_tv = layout.findViewById(R.id.view_pager_add_recipe_8_no_photos_tv);
        view_pager_add_recipe_8_photos_ll = layout.findViewById(R.id.view_pager_add_recipe_8_photos_ll);
        view_pager_add_recipe_8_primary_photo_iv = layout.findViewById(R.id.view_pager_add_recipe_8_primary_photo_iv);
        view_pager_add_recipe_8_photos_gv = layout.findViewById(R.id.view_pager_add_recipe_8_photos_gv);
        TextView view_pager_add_recipe_8_add_photos_tv = layout.findViewById(R.id.view_pager_add_recipe_8_add_photos_tv);
        LinearLayout view_pager_add_recipe_8_finish_ll = layout.findViewById(R.id.view_pager_add_recipe_8_finish_ll);

        view_pager_add_recipe_8_no_photos_tv.setVisibility(View.VISIBLE);
        view_pager_add_recipe_8_photos_ll.setVisibility(View.GONE);

        AddRecipePhotosGridViewAdapter adapter = new AddRecipePhotosGridViewAdapter(mContext, new ArrayList<String>(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(CLASS_NAME, "");
            }
        });
        view_pager_add_recipe_8_photos_gv.setAdapter(adapter);

        view_pager_add_recipe_8_add_photos_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.pickPhotos(fragmentManager, FRAGMENT_ADD_RECIPE);
            }
        });

        view_pager_add_recipe_8_finish_ll.setOnClickListener(clickListener);
    }

    private void setupAddRecipe2(ViewGroup layout) {
        final List<FoodTypeMO> foodTypes = masterData.getFoodTypes();

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

    public  void setCuisine(final CuisineMO cuisine) {
        final ImageView image = view_pager_add_recipe_3_cuisine_ll.findViewById(R.id.view_pager_add_recipe_3_cuisine_iv);
        TextView text = view_pager_add_recipe_3_cuisine_ll.findViewById(R.id.view_pager_add_recipe_3_cuisine_tv);

        text.setText(cuisine.getFOOD_CSN_NAME());

        if(cuisine.getImage() != null){
            image.setImageBitmap(cuisine.getImage());
        }

        view_pager_add_recipe_3_cuisine_ll.setTag(cuisine);

        recipe.setFOOD_CSN_ID(cuisine.getFOOD_CSN_ID());
    }

    public void setFoodType(FoodTypeMO foodType) {
        ImageView image = view_pager_add_recipe_2_food_type_ll.findViewById(R.id.view_pager_add_recipe_2_food_type_iv);
        TextView text = view_pager_add_recipe_2_food_type_ll.findViewById(R.id.view_pager_add_recipe_2_food_type_tv);

        text.setText(foodType.getFOOD_TYP_NAME());

        if(foodType.getImage() != null){
            image.setImageBitmap(foodType.getImage());
        }

        view_pager_add_recipe_2_food_type_ll.setTag(foodType);

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
        recipe.setRCP_NAME(String.valueOf(view_pager_add_recipe_1_recipe_name_et.getText()));
    }

    public void setRecipe() {
        recipe.setRCP_PROC(String.valueOf(view_pager_add_recipe_5_recipe_et.getText()));
    }

    public void setPhotos(String photoPath) {
        AddRecipePhotosGridViewAdapter adapter = (AddRecipePhotosGridViewAdapter) view_pager_add_recipe_8_photos_gv.getAdapter();

        if(adapter.getCount() == 0 && view_pager_add_recipe_8_photos_ll.getVisibility() == View.GONE){
            view_pager_add_recipe_8_primary_photo_iv.setImageBitmap(BitmapFactory.decodeFile(photoPath));
            view_pager_add_recipe_8_primary_photo_iv.setTag(photoPath);
        }
        else{
            adapter.updateDatalist(photoPath);
        }

        view_pager_add_recipe_8_no_photos_tv.setVisibility(View.GONE);
        view_pager_add_recipe_8_photos_ll.setVisibility(View.VISIBLE);

        List<String> images = new ArrayList<>();
        images.add(String.valueOf(view_pager_add_recipe_8_primary_photo_iv.getTag()));
        images.addAll(adapter.dataList);

        recipe.setImages(images);
    }

    public void setIngredients() {
        recipe.setIngredients(((IngredientsGridViewAdapter)view_pager_add_recipe_4_ingedients_gv.getAdapter()).dataList);
    }

    public FoodTypeMO getSelectedFoodType(){
        return (FoodTypeMO) view_pager_add_recipe_2_food_type_ll.getTag();
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