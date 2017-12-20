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
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeViewImagesFullscreenViewPagerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewImagesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.recipe_view_images_fullscreen_vp)
    ViewPager recipe_view_images_fullscreen_vp;

    @InjectView(R.id.recipe_view_images_fullscreen_count_tv)
    TextView recipe_view_images_fullscreen_count_tv;
    //end of components

    private Object object;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view_images_fullscreen, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        object = getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        Object array[] = (Object[])object;

        int imageIndex = (Integer)array[0];
        final List<String> images = (List<String>) array[1];

        recipe_view_images_fullscreen_vp.setAdapter(new RecipeViewImagesFullscreenViewPagerAdapter(mContext, images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
        recipe_view_images_fullscreen_vp.setCurrentItem(imageIndex);

        recipe_view_images_fullscreen_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateImageCounter(++position, images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateImageCounter(++imageIndex, images.size());
    }

    private void updateImageCounter(int index, int maxCount){
        recipe_view_images_fullscreen_count_tv.setText(index+"/"+maxCount);
    }

    // Empty constructor required for DialogFragment
    public RecipeViewImagesFragment() {
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
