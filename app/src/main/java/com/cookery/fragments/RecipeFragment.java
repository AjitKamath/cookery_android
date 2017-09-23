package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeImagesViewPagerAdapter;
import com.cookery.adapters.RecipeViewPagerAdapter;
import com.cookery.models.MasterDataMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.TestData;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_IMAGES;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.common_fragment_recipe_rl)
    RelativeLayout common_fragment_recipe_rl;

    @InjectView(R.id.common_fragment_recipe_vp)
    ViewPager common_fragment_recipe_vp;

    @InjectView(R.id.common_fragment_recipe_tab_vp)
    ViewPager common_fragment_recipe_tab_vp;

    @InjectView(R.id.common_fragment_recipe_tl)
    TabLayout common_fragment_recipe_tl;

    @InjectView(R.id.common_fragment_recipe_recipe_name_tv)
    TextView common_fragment_recipe_recipe_name_tv;

    @InjectView(R.id.common_fragment_recipe_food_type_tv)
    TextView common_fragment_recipe_food_type_tv;

    @InjectView(R.id.common_fragment_recipe_cuisine_name_tv)
    TextView common_fragment_recipe_cuisine_name_tv;

    @InjectView(R.id.common_fragment_recipe_username_tv)
    TextView common_fragment_recipe_username_tv;

    @InjectView(R.id.common_fragment_recipe_rating_tv)
    TextView common_fragment_recipe_rating_tv;
    //end of components

    private RecipeMO recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_recipe, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(common_fragment_recipe_rl);

        return view;
    }

    private void getDataFromBundle() {
        recipe = (RecipeMO) getArguments().get(SELECTED_ITEM);
    }

    private void setupPage() {
        setupImages();

        common_fragment_recipe_recipe_name_tv.setText(recipe.getRCP_NAME().toUpperCase());
        common_fragment_recipe_food_type_tv.setText(recipe.getFOOD_TYP_NAME().toUpperCase());
        common_fragment_recipe_cuisine_name_tv.setText(recipe.getFOOD_CSN_NAME());
        common_fragment_recipe_username_tv.setText(recipe.getNAME());
        common_fragment_recipe_rating_tv.setText(recipe.getRATING());

        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.view_pager_recipe_recipe);
        viewPagerTabsList.add(R.layout.view_pager_recipe_ingredients);
        viewPagerTabsList.add(R.layout.view_pager_recipe_reviews);

        for(Integer iter : viewPagerTabsList){
            common_fragment_recipe_tl.addTab(common_fragment_recipe_tl.newTab());
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        common_fragment_recipe_tab_vp.setAdapter(new RecipeViewPagerAdapter(mContext, viewPagerTabsList, recipe));
        common_fragment_recipe_tab_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(common_fragment_recipe_tl));

        common_fragment_recipe_tl.setupWithViewPager(common_fragment_recipe_tab_vp);
        common_fragment_recipe_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                common_fragment_recipe_tab_vp.setCurrentItem(tab.getPosition());
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
        if(recipe.getImagesList() == null){
            recipe.setImagesList(Utility.getTestImages(mContext));
        }

        common_fragment_recipe_vp.setAdapter(new RecipeImagesViewPagerAdapter(mContext, recipe.getImagesList(), new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Utility.showRecipeImagesFragment(getFragmentManager(), recipe);
            }
        }));
    }

    // Empty constructor required for DialogFragment
    public RecipeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
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
