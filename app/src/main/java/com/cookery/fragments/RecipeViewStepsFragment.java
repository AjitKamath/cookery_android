package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeViewStepsFullscreenRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewStepsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.recipe_view_recipe_steps_fullscreen_steps_rv)
    RecyclerView recipe_view_recipe_steps_fullscreen_steps_rv;
    //end of components

    private Object object;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view_recipe_steps_fullscreen, container);
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
        List<String> steps = (List<String>)object;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_view_recipe_steps_fullscreen_steps_rv.setLayoutManager(mLayoutManager);
        recipe_view_recipe_steps_fullscreen_steps_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_view_recipe_steps_fullscreen_steps_rv.setAdapter(new RecipeViewStepsFullscreenRecyclerViewAdapter(mContext, steps, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
    }

    // Empty constructor required for DialogFragment
    public RecipeViewStepsFragment() {
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
