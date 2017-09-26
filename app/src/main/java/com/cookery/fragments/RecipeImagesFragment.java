package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeImagesViewPagerAdapter;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeImagesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_fragment_recipe_images_vp)
    ViewPager common_fragment_recipe_images_vp;
    //end of components

    private List<String> imagesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_recipe_images, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        imagesList = ((RecipeMO)getArguments().get(GENERIC_OBJECT)).getRCP_IMGS();
    }

    private void setupPage() {
        if(imagesList == null || imagesList.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Object is null");
            return;
        }

        common_fragment_recipe_images_vp.setAdapter(new RecipeImagesViewPagerAdapter(mContext, imagesList, false, new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RecipeMO temp = new RecipeMO();
                temp.setRCP_IMGS(imagesList);

                Utility.showRecipeImagesFragment(getFragmentManager(), temp);
            }
        }));
    }

    // Empty constructor required for DialogFragment
    public RecipeImagesFragment() {}

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
            int height = ViewGroup.LayoutParams.MATCH_PARENT;;
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
