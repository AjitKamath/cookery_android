package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.AddRecipeViewPagerAdapter;
import com.cookery.component.ViewPagerCustom;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.RecipeMO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 25/8/17.
 */

public class AddRecipeFragment extends DialogFragment {

    private Context mContext;

    @InjectView(R.id.add_recipe_vp)
    ViewPagerCustom add_recipe_vp;

    @InjectView(R.id.common_fragment_header_add_recipe_close_iv)
    ImageView common_fragment_header_add_recipe_close_iv;

    @InjectView(R.id.common_fragment_header_add_recipe_back_iv)
    ImageView common_fragment_header_add_recipe_back_iv;

    @InjectView(R.id.common_fragment_header_add_recipe_forward_iv)
    ImageView common_fragment_header_add_recipe_forward_iv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupPage();

        return view;
    }

    private void setupPage() {
        setupSliders();

        common_fragment_header_add_recipe_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        common_fragment_header_add_recipe_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_recipe_vp.setCurrentItem(add_recipe_vp.getCurrentItem() - 1);
            }
        });

        common_fragment_header_add_recipe_forward_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_recipe_vp.setCurrentItem(add_recipe_vp.getCurrentItem() + 1);
            }
        });

    }

    private void setupSliders() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_1);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_2);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_3);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_4);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_5);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_6);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_7);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_8);

        final AddRecipeViewPagerAdapter viewPagerAdapter = new AddRecipeViewPagerAdapter(mContext, getFragmentManager(), viewPagerTabsList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeMO recipe = getInputs();
            }
        });

        int activePageIndex = 0;
        if (add_recipe_vp != null && add_recipe_vp.getAdapter() != null) {
            activePageIndex = add_recipe_vp.getCurrentItem();
        }

        add_recipe_vp.setAdapter(viewPagerAdapter);
        add_recipe_vp.setCurrentItem(activePageIndex);
        add_recipe_vp.setPagingEnabled(true);

        //hide back button initially
        common_fragment_header_add_recipe_back_iv.setVisibility(View.GONE);

        add_recipe_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                common_fragment_header_add_recipe_close_iv.setVisibility(View.VISIBLE);
                common_fragment_header_add_recipe_back_iv.setVisibility(View.VISIBLE);
                common_fragment_header_add_recipe_forward_iv.setVisibility(View.VISIBLE);

                //hide close
                if (position == 0) {
                    common_fragment_header_add_recipe_back_iv.setVisibility(View.GONE);
                }
                //hide forward
                else if (position == viewPagerTabsList.size() - 1) {
                    common_fragment_header_add_recipe_forward_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private RecipeMO getInputs() {
        return null;
    }

    public void setFoodType(FoodTypeMO foodType) {
        ((AddRecipeViewPagerAdapter) add_recipe_vp.getAdapter()).setFoodType(foodType);
    }

    public void setCuisine(CuisineMO cuisine) {
        ((AddRecipeViewPagerAdapter) add_recipe_vp.getAdapter()).setCuisine(cuisine);
    }

    // Empty constructor required for DialogFragment
    public AddRecipeFragment() {
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
            d.setCanceledOnTouchOutside(false);
        }
    }

    public void onFinishDialog(Integer choice) {
        if (choice == null) {
            return;
        }
        /*else if(GALLERY_CHOICE == choice){
            showPickImageFromGallery();
        }
        else if(CAMERA_CHOICE == choice){
            showPickImageFromCamera();
        }*/
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    private void setFont(ViewGroup group) {
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