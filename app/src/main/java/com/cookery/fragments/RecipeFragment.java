package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeImagesViewPagerAdapter;
import com.cookery.adapters.RecipeViewPagerAdapter;
import com.cookery.models.LikesMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
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

    @InjectView(R.id.common_fragment_recipe_like_iv)
    ImageView common_fragment_recipe_like_iv;

    @InjectView(R.id.common_fragment_recipe_like_ll)
    LinearLayout common_fragment_recipe_like_ll;

    @InjectView(R.id.common_fragment_recipe_views_tv)
    TextView common_fragment_recipe_views_tv;

    @InjectView(R.id.common_fragment_recipe_like_tv)
    TextView common_fragment_recipe_like_tv;

    @InjectView(R.id.common_fragment_recipe_rating_ll)
    LinearLayout common_fragment_recipe_rating_ll;

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

    @InjectView(R.id.common_fragment_recipe_view_ll)
    LinearLayout common_fragment_recipe_view_ll;

    @InjectView(R.id.common_fragment_recipe_rating_tv)
    TextView common_fragment_recipe_rating_tv;

    @InjectView(R.id.common_fragment_recipe_comment_ll)
    LinearLayout common_fragment_recipe_comment_ll;

    @InjectView(R.id.common_fragment_recipe_comment_iv)
    ImageView common_fragment_recipe_comment_iv;

    @InjectView(R.id.common_fragment_recipe_comment_tv)
    TextView common_fragment_recipe_comment_tv;

    @InjectView(R.id.common_fragment_recipe_rating_iv)
    ImageView common_fragment_recipe_rating_iv;

    //end of components

    private RecipeMO recipe;
    private UserMO loggerInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        getLoggedInUser();

        setupPage();

        setFont(common_fragment_recipe_rl);

        return view;
    }

    private void getLoggedInUser() {
        loggerInUser = Utility.getUserFromUserSecurity(mContext);
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
        common_fragment_recipe_views_tv.setText(Utility.getSmartNumber(recipe.getViews()));

        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.recipe_view_recipe);
        viewPagerTabsList.add(R.layout.view_pager_recipe_ingredients);

        for(Integer iter : viewPagerTabsList){
            common_fragment_recipe_tl.addTab(common_fragment_recipe_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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

        setRatingView();
        setLikeView();
        setViewView();
        setCommentView();
    }

    private void setViewView() {
        common_fragment_recipe_views_tv.setText(String.valueOf(recipe.getViews()));

        common_fragment_recipe_view_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO: show people who have viewed this recipe

                return false;
            }
        });
    }

    private void setCommentView() {
        if(recipe.getComments() == null || recipe.getComments().isEmpty()){
            common_fragment_recipe_comment_iv.setImageResource(R.drawable.comment_disabled);
            common_fragment_recipe_comment_tv.setText("0");
        }
        else{
            common_fragment_recipe_comment_iv.setImageResource(R.drawable.comment_enabled);
            common_fragment_recipe_comment_tv.setText(String.valueOf(recipe.getComments().size()));
        }

        common_fragment_recipe_comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncFetchComments().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private void setupImages() {
        common_fragment_recipe_vp.setAdapter(new RecipeImagesViewPagerAdapter(mContext, recipe.getRCP_IMGS(), true, new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO: do not re use showrecipefragment. implement another fragment
                //Utility.showRecipeImagesFragment(getFragmentManager(), recipe);
            }
        }));
    }

    public void setLikeView(){
        if(recipe.getLikes() != -1){
            common_fragment_recipe_like_tv.setText(Utility.getSmartNumber(recipe.getLikes()));
        }

        if(recipe.isLiked()){
            common_fragment_recipe_like_iv.setImageResource(R.drawable.heart);
        }
        else{
            common_fragment_recipe_like_iv.setImageResource(R.drawable.heart_unselected);
        }

        common_fragment_recipe_like_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recipe.getUSER_ID() == loggerInUser.getUser_id()){
                    return;
                }

                LikesMO like = new LikesMO();
                like.setUSER_ID(loggerInUser.getUser_id());
                like.setTYPE("RECIPE");
                like.setTYPE_ID(recipe.getRCP_ID());

                new AsyncSubmitLike().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
            }
        });
    }

    public void setRatingView(){
        if(recipe.getAvgRating() != null && !recipe.getAvgRating().trim().isEmpty()){
            common_fragment_recipe_rating_tv.setText(String.valueOf(recipe.getAvgRating()));
        }

        if(recipe.isReviewed()){
            common_fragment_recipe_rating_iv.setImageResource(R.drawable.star);
        }
        else{
            common_fragment_recipe_rating_iv.setImageResource(R.drawable.star_unselected);
        }

        common_fragment_recipe_rating_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncFetchReviews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    public void updateRecipeView(){
        new AsyncUpdateRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe);
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

    public class AsyncSubmitLike extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Object doInBackground(Object... objects) {
            LikesMO like = (LikesMO) objects[0];
            return InternetUtility.submitLike(like);
        }

        @Override
        protected void onPostExecute(Object object) {
            LikesMO like = (LikesMO) object;

            if(like != null){
                recipe.setLikes(like.getLikes());
                recipe.setLiked(like.isLiked());

                setLikeView();
            }
        }
    }

    public class AsyncFetchReviews extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching Reviews ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            recipe.setUserReview(InternetUtility.fetchUsersRecipeReview(loggerInUser, recipe));
            recipe.setReviews(InternetUtility.fetchRecipeReviews(loggerInUser, recipe, 0));

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            Utility.showRecipeReviewFragment(getFragmentManager(), FRAGMENT_RECIPE, recipe);
        }
    }

    public class AsyncFetchComments extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching Comments ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            recipe.setComments(InternetUtility.fetchRecipeComments(loggerInUser, recipe, 0));

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);
            Utility.showRecipeCommentsFragment(getFragmentManager(), FRAGMENT_RECIPE, recipe);
        }
    }

    class AsyncUpdateRecipe extends AsyncTask<RecipeMO, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Updating Recipe ..");
        }

        @Override
        protected Object doInBackground(RecipeMO... objects) {
            List<RecipeMO> recipes = (List<RecipeMO>) InternetUtility.fetchRecipe(recipe, loggerInUser.getUser_id());

            if(recipes != null && !recipes.isEmpty()){
                recipes.get(0).setComments(InternetUtility.fetchRecipeComments(loggerInUser, recipes.get(0), 0));
                recipes.get(0).setReviews(InternetUtility.fetchRecipeReviews(loggerInUser, recipes.get(0), 0));
                return recipes.get(0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            if(object == null){
                return;
            }

            RecipeMO updatedRecipe = (RecipeMO) object;

            if(recipe != null){
                recipe = updatedRecipe;

                setRatingView();
                setLikeView();
                setViewView();
                setCommentView();

                Utility.closeWaitDialog(getFragmentManager(), fragment);

                if(recipe.isReviewed()) {
                    MessageMO message = new MessageMO();
                    message.setPurpose("ADD_RECIPE_REVIEW");

                    message.setError(false);
                    message.setErr_message("Thank You for the review !");

                    Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE);
                    Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
                }
            }
        }
    }
}
