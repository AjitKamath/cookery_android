package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.MessageMO;
import com.cookery.models.ReviewMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE_REVIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeReviewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_fragment_recipe_review_cv)
    CardView common_fragment_recipe_review_cv;

    @InjectView(R.id.common_fragment_recipe_rating_star_1_iv)
    ImageView common_fragment_recipe_rating_star_1_iv;

    @InjectView(R.id.common_fragment_recipe_rating_star_2_iv)
    ImageView common_fragment_recipe_rating_star_2_iv;

    @InjectView(R.id.common_fragment_recipe_rating_star_3_iv)
    ImageView common_fragment_recipe_rating_star_3_iv;

    @InjectView(R.id.common_fragment_recipe_rating_star_4_iv)
    ImageView common_fragment_recipe_rating_star_4_iv;

    @InjectView(R.id.common_fragment_recipe_rating_star_5_iv)
    ImageView common_fragment_recipe_rating_star_5_iv;

    @InjectView(R.id.common_fragment_recipe_rating_review_tv)
    EditText common_fragment_recipe_rating_review_tv;

    @InjectView(R.id.common_fragment_recipe_rating_submit_ll)
    LinearLayout common_fragment_recipe_rating_submit_ll;
    //end of components

    private ReviewMO review;
    private List<ImageView> stars = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_recipe_review, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        review = (ReviewMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        stars.add(common_fragment_recipe_rating_star_1_iv);
        stars.add(common_fragment_recipe_rating_star_2_iv);
        stars.add(common_fragment_recipe_rating_star_3_iv);
        stars.add(common_fragment_recipe_rating_star_4_iv);
        stars.add(common_fragment_recipe_rating_star_5_iv);

        if(review == null){
            review = new ReviewMO();
        }

        if(review.getRATING() != 0){
            selectStars(review.getRATING());
            common_fragment_recipe_rating_review_tv.setText(review.getREVIEW());
        }
        else{
            selectStars(0);
        }

        common_fragment_recipe_rating_submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(review.getRATING() == 0){
                    Utility.showSnacks(common_fragment_recipe_review_cv, "Rate before you submit", OK, Snackbar.LENGTH_LONG);
                    return;
                }
                review.setREVIEW(String.valueOf(common_fragment_recipe_rating_review_tv.getText()));

                MessageMO message = InternetUtility.submitRecipeReview(review);

                message.setPurpose("ADD_RECIPE_REVIEW");
                Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_REVIEW);
                Utility.showMessageDialog(getFragmentManager(), currentFrag, message);

                if(!message.isError()){
                    dismiss();
                }
            }
        });
    }

    private void selectStars(int count) {
        review.setRATING(count);

        for(int i=0; i<stars.size(); i++){
            if(i < count){
                stars.get(i).setImageResource(R.drawable.star);
            }
            else{
                stars.get(i).setImageResource(R.drawable.star_unselected);
            }
        }
    }

    // Empty constructor required for DialogFragment
    public RecipeReviewFragment() {
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
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
