package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.IngredientViewImagesGridViewAdapter;
import com.cookery.adapters.IngredientViewViewPagerAdapter;
import com.cookery.models.IngredientMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class IngredientViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.ingredient_view_ll)
    LinearLayout ingredient_view_ll;

    @InjectView(R.id.ingredient_view_primary_image_iv)
    CircleImageView ingredient_view_primary_image_iv;

    @InjectView(R.id.ingredient_view_ingredient_name_tv)
    TextView ingredient_view_ingredient_name_tv;

    @InjectView(R.id.ingredient_view_ingredient_category_tv)
    TextView ingredient_view_ingredient_category_tv;

    @InjectView(R.id.ingredient_view_ingredient_images_gv)
    GridView ingredient_view_ingredient_images_gv;

    @InjectView(R.id.ingredient_view_tl)
    TabLayout ingredient_view_tl;

    @InjectView(R.id.ingredient_view_vp)
    ViewPager ingredient_view_vp;
    /*components*/


    private IngredientMO ingredient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredient_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        ingredient = (IngredientMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage(){
        if(ingredient == null){
            Log.e(CLASS_NAME, "Error ! Ingredient is null");
            return;
        }

        if(ingredient.getIngredientPrimaryImage() != null && !ingredient.getIngredientPrimaryImage().isEmpty()){
            Utility.loadImageFromURL(mContext, ingredient.getIngredientPrimaryImage().get(0).getING_IMG(),  ingredient_view_primary_image_iv);
        }

        ingredient_view_ingredient_name_tv.setText(ingredient.getIngredientAkaName().toUpperCase());
        ingredient_view_ingredient_category_tv.setText(ingredient.getIngredientCategoryName().toUpperCase());

        setupImages();
        setupNutritionsAndHealth();
    }

    private void setupNutritionsAndHealth() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.ingredient_view_nutritions);
        viewPagerTabsList.add(R.layout.ingredient_view_healths);

        for (Integer iter : viewPagerTabsList) {
            ingredient_view_tl.addTab(ingredient_view_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ingredient_view_vp.setAdapter(new IngredientViewViewPagerAdapter(mContext, viewPagerTabsList, ingredient));
        ingredient_view_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(ingredient_view_tl));

        ingredient_view_tl.setupWithViewPager(ingredient_view_vp);
        ingredient_view_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ingredient_view_vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupImages() {
        ingredient_view_ingredient_images_gv.setAdapter(new IngredientViewImagesGridViewAdapter(mContext, ingredient.getImages(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }

    // Empty constructor required for DialogFragment
    public IngredientViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
