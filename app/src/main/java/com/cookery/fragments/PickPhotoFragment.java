package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andremion.louvre.Louvre;
import com.cookery.R;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.LOUVRE_MAX_IMAGES;
import static com.cookery.utils.Constants.LOUVRE_REQUEST_CODE;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_PARENT_FRAGMENT;

/**
 * Created by ajit on 21/3/16.
 */
public class PickPhotoFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    @InjectView(R.id.common_share_social_media_ll)
    LinearLayout common_pick_image_camera_ll;

    @InjectView(R.id.common_pick_image_gallery_ll)
    LinearLayout common_pick_image_gallery_ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_pick_image, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupPage();

        return view;
    }

    private void setupPage() {
        common_pick_image_gallery_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getTargetFragment() instanceof RecipeAddFragment){
                    RecipeAddFragment recipeAddFragment = (RecipeAddFragment) getFragmentManager().findFragmentByTag(FRAGMENT_ADD_RECIPE);
                    Louvre.init(getActivity())
                            .setRequestCode(LOUVRE_REQUEST_CODE)
                            .setMaxSelection(LOUVRE_MAX_IMAGES)
                            .setSelection(recipeAddFragment.selectedImages)
                            .open();
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+getTargetFragment());
                }
            }
        });

        common_pick_image_camera_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getTargetFragment() instanceof RecipeAddFragment){
                    Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_ADD_RECIPE);
                    Utility.startCropImageActivity(mContext, fragment);
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+getTargetFragment());
                }
            }
        });
    }

    // Empty constructor required for DialogFragment
    public PickPhotoFragment() {
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
            int width = 800;
            int height = 400;
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
