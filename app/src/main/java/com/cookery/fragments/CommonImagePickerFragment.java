package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.cookery.utils.Constants.CAMERA_CHOICE;
import static com.cookery.utils.Constants.GALLERY_CHOICE;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_PARENT_FRAGMENT;

/**
 * Created by ajit on 21/3/16.
 */
public class CommonImagePickerFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.common_fragment_header_back_iv)
    ImageView common_fragment_header_back_iv;

    @InjectView(R.id.common_fragment_header_forward_iv)
    ImageView common_fragment_header_forward_iv;
    /*components*/

    private Integer choice = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_image_picker, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupPage();

        return view;
    }

    private void setupPage() {
        common_fragment_header_back_iv.setVisibility(View.INVISIBLE);
        common_fragment_header_forward_iv.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.common_image_picker_gallery_ll, R.id.common_image_picker_camera_ll})
    public void onImagePickerChoice(View view){
        if(R.id.common_image_picker_gallery_ll == view.getId()){
            choice = GALLERY_CHOICE;
        }
        else if(R.id.common_image_picker_camera_ll == view.getId()){
            choice = CAMERA_CHOICE;
        }
        dismiss();
    }

    // Empty constructor required for DialogFragment
    public CommonImagePickerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStop(){
        super.onStop();

        if(getTargetFragment() instanceof AddRecipeFragment){
            AddRecipeFragment fragment = (AddRecipeFragment) getTargetFragment();
            fragment.onFinishDialog(choice);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+":"+getParentFragment());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = 500;
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
