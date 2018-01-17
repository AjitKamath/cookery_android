package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeViewReviewsRecyclerViewAdapter;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_LIKED_USERS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_REVIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewReviewsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.recipe_reviews_rl)
    RelativeLayout recipe_reviews_rl;

    @InjectView(R.id.recipe_reviews_cv)
    CardView recipe_reviews_cv;

    @InjectView(R.id.recipe_reviews_delete_iv)
    ImageView recipe_reviews_delete_iv;

    @InjectView(R.id.recipe_reviews_star_1_iv)
    ImageView recipe_reviews_star_1_iv;

    @InjectView(R.id.recipe_reviews_star_2_iv)
    ImageView recipe_reviews_star_2_iv;

    @InjectView(R.id.recipe_reviews_star_3_iv)
    ImageView recipe_reviews_star_3_iv;

    @InjectView(R.id.recipe_reviews_star_4_iv)
    ImageView recipe_reviews_star_4_iv;

    @InjectView(R.id.recipe_reviews_star_5_iv)
    ImageView recipe_reviews_star_5_iv;

    @InjectView(R.id.recipe_reviews_review_et)
    EditText recipe_reviews_review_et;

    @InjectView(R.id.recipe_reviews_review_rl)
    RelativeLayout recipe_reviews_review_rl;

    @InjectView(R.id.recipe_reviews_review_tv)
    TextView recipe_reviews_review_tv;

    @InjectView(R.id.recipe_reviews_review_datetime_tv)
    TextView recipe_reviews_review_datetime_tv;

    @InjectView(R.id.recipe_reviews_reviews_rv)
    RecyclerView recipe_reviews_reviews_rv;

    @InjectView(R.id.recipe_review_submit_fab)
    FloatingActionButton recipe_review_submit_fab;
    //end of components

    private RecipeMO recipe;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_reviews, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getLoggedInUser();
        getDataFromBundle();
        setupPage();
        setFont(recipe_reviews_rl);

        return view;
    }

    private void getLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    private void getDataFromBundle() {
        recipe = (RecipeMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        setupStars();
        setupReview();
        setupReviews();

        /*common_fragment_recipe_rating_submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(review.getRATING() == 0){
                    Utility.showSnacks(common_fragment_recipe_review_cv, "Rate before you submit", OK, Snackbar.LENGTH_LONG);
                    return;
                }
                review.setREVIEW(String.valueOf(common_fragment_recipe_rating_review_tv.getText()));

                new AsyncSubmitReview().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, review);
            }
        });*/
    }

    private void setupReview() {
        if(recipe.isUserReviewed()){
            recipe_reviews_delete_iv.setVisibility(View.VISIBLE);
            recipe_reviews_review_rl.setVisibility(View.VISIBLE);
            recipe_reviews_review_et.setVisibility(View.GONE);
            recipe_review_submit_fab.setVisibility(View.GONE);

            if(recipe.getUserReview() != null){
                recipe_reviews_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageMO message = new MessageMO();
                        message.setError(false);
                        message.setPurpose("RECIPE_VIEW_DELETE_REVIEW");
                        message.setErr_message("Do You Want To Delete Your Review ?");

                        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_REVIEW);
                        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
                    }
                });

                recipe_reviews_review_tv.setText(recipe.getUserReview().getREVIEW().trim());

                if(recipe.getUserReview().getMOD_DTM() != null && !recipe.getUserReview().getMOD_DTM().isEmpty()){
                    recipe_reviews_review_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(recipe.getUserReview().getMOD_DTM(), DB_DATE_TIME)));
                }
                else if(recipe.getUserReview().getCREATE_DTM() != null && !recipe.getUserReview().getCREATE_DTM().isEmpty()){
                    recipe_reviews_review_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(recipe.getUserReview().getCREATE_DTM(), DB_DATE_TIME)));
                }
            }
        }
        else{
            recipe_reviews_delete_iv.setVisibility(View.GONE);
            recipe_reviews_review_rl.setVisibility(View.GONE);
            recipe_reviews_review_et.setVisibility(View.VISIBLE);
            recipe_review_submit_fab.setVisibility(View.VISIBLE);

            recipe_review_submit_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recipe.getUserReview().getRATING() == 0){
                        Utility.showSnacks(recipe_reviews_review_rl, "You haven't rated the Recipe", OK, Snackbar.LENGTH_LONG);
                        return;
                    }

                    if(recipe_reviews_review_et.getText() == null || String.valueOf(recipe_reviews_review_et.getText()).trim().isEmpty()){
                        Utility.showSnacks(recipe_reviews_review_rl, "You haven't reviewed the Recipe", OK, Snackbar.LENGTH_LONG);
                        return;
                    }

                    recipe.getUserReview().setREVIEW(String.valueOf(recipe_reviews_review_et.getText()).trim());

                    new AsyncSubmitReview().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe);
                }
            });
        }
    }

    private void setupReviews() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_reviews_reviews_rv.setLayoutManager(mLayoutManager);
        recipe_reviews_reviews_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_reviews_reviews_rv.setAdapter(new RecipeViewReviewsRecyclerViewAdapter(mContext, loggedInUser, recipe.getReviews(), new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AsyncFetchLikedUsers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (ReviewMO) view.getTag());
                return true;
            }
        }));
    }

    private void setupStars() {
        if(recipe.getUserReview() != null && recipe.getUserReview().getRATING() != 0){
            selectStars(recipe.getUserReview().getRATING());
        }
        else{
            selectStars(0);
        }
    }

    public void deleteReview(){
        if(recipe.isUserReviewed()){
            new AsyncDeleteReview().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe.getUserReview());
        }
        else{
            Log.e(CLASS_NAME, "Error ! Recipe.isUserReviewed is expected to be true.");
        }
    }

    private void selectStars(int count) {
        List<ImageView> stars = new ArrayList<>();
        stars.add(recipe_reviews_star_1_iv);
        stars.add(recipe_reviews_star_2_iv);
        stars.add(recipe_reviews_star_3_iv);
        stars.add(recipe_reviews_star_4_iv);
        stars.add(recipe_reviews_star_5_iv);

        if(recipe.getUserReview() == null){
            recipe.setUserReview(new ReviewMO());
        }

        recipe.getUserReview().setRATING(count);

        for(int i=0; i<stars.size(); i++){
            if(i < count){
                stars.get(i).setImageResource(R.drawable.star);
            }
            else{
                stars.get(i).setImageResource(R.drawable.star_unselected);
            }
        }

        if(!recipe.isUserReviewed()) {
            for (ImageView iterStars : stars) {
                iterStars.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectStars(Integer.parseInt(String.valueOf(view.getTag())));
                    }
                });
            }
        }
    }

    // Empty constructor required for DialogFragment
    public RecipeViewReviewsFragment() {
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

    public class AsyncSubmitReview extends AsyncTask<Object, Void, Object> {
        Fragment frag;

        @Override
        protected void onPreExecute(){
            frag = Utility.showWaitDialog(getFragmentManager(), "Submitting your review ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            RecipeMO recipe = (RecipeMO) objects[0];
            return InternetUtility.submitRecipeReview(loggedInUser, recipe);
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), frag);

            List<ReviewMO> review = (List<ReviewMO>) object;

            if(review != null && !review.isEmpty() && review.get(0) != null && review.get(0).isUserReviewed()){
                dismiss();

                if(getTargetFragment() instanceof RecipeViewFragment){
                    ((RecipeViewFragment)getTargetFragment()).updateRecipeView();
                    ((RecipeViewFragment)getTargetFragment()).showReviewAddMessage();
                }
            }
        }
    }

    public class AsyncDeleteReview extends AsyncTask<Object, Void, Object> {
        Fragment frag;

        @Override
        protected void onPreExecute() {
            frag = Utility.showWaitDialog(getFragmentManager(), "Deleting your review ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            ReviewMO review = (ReviewMO) objects[0];
            return InternetUtility.deleteRecipeReview(loggedInUser, review);
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), frag);

            MessageMO message = (MessageMO) object;

            if (message != null && !message.isError()) {
                dismiss();

                if(getTargetFragment() instanceof RecipeViewFragment){
                    ((RecipeViewFragment)getTargetFragment()).updateRecipeView();
                    ((RecipeViewFragment)getTargetFragment()).showReviewDeleteMessage();
                }
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not delete Review");
            }
        }
    }

    class AsyncFetchLikedUsers extends AsyncTask<ReviewMO, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching users who liked the Review ..");
        }

        @Override
        protected Object doInBackground(ReviewMO... objects) {
            ReviewMO review = objects[0];
            return InternetUtility.fetchLikedUsers("REVIEW", review.getREV_ID());
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(object == null){
                return;
            }

            List<UserMO> users = (List<UserMO>) object;

            if(users != null && !users.isEmpty()){
                Object array[] = new Object[]{"LIKE", users};

                Map<String, Object> bundleMap = new HashMap<String, Object>();
                bundleMap.put(GENERIC_OBJECT, array);

                Utility.showFragment(getFragmentManager(), FRAGMENT_RECIPE_REVIEW, FRAGMENT_RECIPE_LIKED_USERS, new UsersFragment(), bundleMap);
            }
        }
    }
}
