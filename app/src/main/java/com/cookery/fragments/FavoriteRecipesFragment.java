package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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
import com.cookery.adapters.RecipeFavoritesViewPagerAdapter;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class FavoriteRecipesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.common_fragment_fav_recipes_rl)
    RelativeLayout common_fragment_fav_recipes_rl;

    @InjectView(R.id.common_fragment_fav_recipes_tl)
    TabLayout common_fragment_fav_recipes_tl;

    @InjectView(R.id.common_fragment_fav_recipes_tab_vp)
    ViewPager common_fragment_fav_recipes_tab_vp;
    //end of components

    private Map<String, List<RecipeMO>> favRecipesMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_fav_recipes, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(common_fragment_fav_recipes_rl);

        return view;
    }

    private void getDataFromBundle() {
        favRecipesMap = (Map<String, List<RecipeMO>>) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.view_pager_recipes_recipes_mini);
        viewPagerTabsList.add(R.layout.view_pager_recipes_recipes_mini);
        viewPagerTabsList.add(R.layout.view_pager_recipes_recipes_mini);

        for(Integer iter : viewPagerTabsList){
            common_fragment_fav_recipes_tl.addTab(common_fragment_fav_recipes_tl.newTab());
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        common_fragment_fav_recipes_tab_vp.setAdapter(new RecipeFavoritesViewPagerAdapter(mContext, favRecipesMap, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeMO recipe = (RecipeMO) view.getTag();

                if(recipe == null){
                    Log.e(CLASS_NAME, "Recipe object is null");
                    return;
                }

                Utility.showRecipeFragment(getFragmentManager(), recipe);
            }
        }));
        common_fragment_fav_recipes_tab_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(common_fragment_fav_recipes_tl));

        common_fragment_fav_recipes_tl.setupWithViewPager(common_fragment_fav_recipes_tab_vp);
        common_fragment_fav_recipes_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                common_fragment_fav_recipes_tab_vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Empty constructor required for DialogFragment
    public FavoriteRecipesFragment() {}

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
