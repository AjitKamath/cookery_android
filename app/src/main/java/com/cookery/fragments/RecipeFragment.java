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
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
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

    @InjectView(R.id.common_fragment_recipe_comments_tv)
    TextView common_fragment_recipe_comments_tv;

    @InjectView(R.id.common_fragment_recipe_rating_iv)
    ImageView common_fragment_recipe_rating_iv;

    //end of components

    private RecipeMO recipe;
    private UserMO loggerInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_recipe, container);
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
        loggerInUser = (UserMO) Utility.readFromUserSecurity(mContext, LOGGED_IN_USER);
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

        if(recipe.getRating() != null && !recipe.getRating().trim().isEmpty()){
            common_fragment_recipe_rating_tv.setText(recipe.getRating());
        }

        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.view_pager_recipe_recipe);
        viewPagerTabsList.add(R.layout.view_pager_recipe_ingredients);
        viewPagerTabsList.add(R.layout.view_pager_recipe_reviews);

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

        if(recipe.getComments() == null || recipe.getComments().isEmpty()){
            common_fragment_recipe_comments_tv.setText("NO COMMENTS");
        }
        else{
            common_fragment_recipe_comments_tv.setText(recipe.getComments().size()+" COMMENTS");

            common_fragment_recipe_comments_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.showRecipeCommentsFragment(getFragmentManager(), recipe);
                }
            });
        }

        common_fragment_recipe_view_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO: show people who have viewed this recipe

                return false;
            }
        });

        common_fragment_recipe_rating_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncFetchReview().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

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

        setLikeView(recipe.isLiked(), recipe.getLikes());
        setRatingView(recipe.isReviewed(), recipe.getRating());
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

    public void setLikeView(boolean isLiked, int likes){
        if(likes != -1){
            common_fragment_recipe_like_tv.setText(Utility.getSmartNumber(likes));
        }

        if(isLiked){
            common_fragment_recipe_like_iv.setImageResource(R.drawable.heart);
        }
        else{
            common_fragment_recipe_like_iv.setImageResource(R.drawable.heart_unselected);
        }
    }

    public void setRatingView(boolean isReviewed, String rating){
        if(rating != null && !rating.trim().isEmpty()){
            common_fragment_recipe_rating_tv.setText(rating);
        }

        if(isReviewed){
            common_fragment_recipe_rating_iv.setImageResource(R.drawable.star);
        }
        else{
            common_fragment_recipe_rating_iv.setImageResource(R.drawable.star_unselected);
        }
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

    class AsyncFetchReview extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;

        @Override
        protected void onPreExecute(){
            frag = Utility.showWaitDialog(getFragmentManager(), "Getting your review ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            ReviewMO review = new ReviewMO();
            review.setRCP_ID(recipe.getRCP_ID());
            review.setUSER_ID(loggerInUser.getUser_id());

            return InternetUtility.fetchUsersRecipeReview(review);
        }

        @Override
        protected void onPostExecute(Object object) {
            ReviewMO review = (ReviewMO) object;
            Utility.closeWaitDialog(getFragmentManager(), frag);

            if(review == null){
                review = new ReviewMO();
            }

            review.setRCP_ID(recipe.getRCP_ID());
            review.setUSER_ID(loggerInUser.getUser_id());

            Utility.showRecipeReviewFragment(getFragmentManager(), FRAGMENT_RECIPE, review);
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
            setLikeView(like.isLiked(), like.getLikes());
        }
    }
}
