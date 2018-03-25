package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeViewImagesFullscreenViewPagerAdapter;
import com.cookery.adapters.RecipeViewViewPagerAdapter;
import com.cookery.models.CommentMO;
import com.cookery.models.FavouritesMO;
import com.cookery.models.ImageMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Setter;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_IMAGES;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_STEPS;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.common_fragment_recipe_rl)
    RelativeLayout common_fragment_recipe_rl;

    @InjectView(R.id.common_fragment_recipe_vp)
    ViewPager common_fragment_recipe_vp;

    @InjectView(R.id.recipe_view_images_count_tv)
    TextView recipe_view_images_count_tv;

    @InjectView(R.id.common_fragment_recipe_like_iv)
    ImageView common_fragment_recipe_like_iv;

    @InjectView(R.id.common_fragment_recipe_favourite_iv)
    ImageView common_fragment_recipe_favourite_iv;

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

    @InjectView(R.id.common_fragment_recipe_user_details_ll)
    LinearLayout common_fragment_recipe_user_details_ll;

    @InjectView(R.id.recipe_view_user_iv)
    ImageView recipe_view_user_iv;

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

    @InjectView(R.id.fb_share_button)
    ShareButton fbShareButton;

    //end of components

    @Setter
    private RecipeMO recipe;
    private UserMO loggedInUser;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

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
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    private void getDataFromBundle() {
        recipe = (RecipeMO) getArguments().get(SELECTED_ITEM);
    }

    public void setupPage() {
        setupImages();

        Utility.loadImageFromURL(mContext, recipe.getUserImage().trim(), recipe_view_user_iv);

        common_fragment_recipe_recipe_name_tv.setText(recipe.getRCP_NAME().toUpperCase());
        common_fragment_recipe_food_type_tv.setText(recipe.getFoodTypeName().toUpperCase());
        common_fragment_recipe_cuisine_name_tv.setText(recipe.getFoodCuisineName());
        common_fragment_recipe_username_tv.setText(recipe.getUserName());
        common_fragment_recipe_views_tv.setText(Utility.getSmartNumber(recipe.getViewedUsers() == null ? 0 : recipe.getViewedUsers().size()));

        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.recipe_view_recipe);
        viewPagerTabsList.add(R.layout.recipe_view_ingredients);

        for (Integer iter : viewPagerTabsList) {
            common_fragment_recipe_tl.addTab(common_fragment_recipe_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        common_fragment_recipe_tab_vp.setAdapter(new RecipeViewViewPagerAdapter(mContext, getFragmentManager(),viewPagerTabsList, recipe, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.recipe_view_recipe_steps_fullscreen_iv) {
                    Map<String, Object> bundleMap = new HashMap<String, Object>();
                    bundleMap.put(GENERIC_OBJECT, recipe.getSteps());

                    Utility.showFragment(getFragmentManager(), FRAGMENT_RECIPE, FRAGMENT_RECIPE_STEPS, new RecipeViewStepsFragment(), bundleMap);
                }
            }
        }));
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

        setReviewView();
        setLikeView();
        setViewView();
        setCommentView();
        setfavouriteView();

        common_fragment_recipe_user_details_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe.getUSER_ID());
            }
        });

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.cookery"))
                .setQuote(recipe.getRCP_NAME()) // Name of Dish
                .build();
        fbShareButton.setShareContent(linkContent);
    }

    private void setViewView() {
        common_fragment_recipe_views_tv.setText(String.valueOf(recipe.getViewsCount()));

        common_fragment_recipe_view_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "VIEW", recipe, 0);
                return true;
            }
        });
    }

    private void setCommentView() {
        if (recipe.getCommentsCount() == 0) {
            common_fragment_recipe_comment_iv.setImageResource(R.drawable.comment_disabled);
        } else {
            common_fragment_recipe_comment_iv.setImageResource(R.drawable.comment_enabled);
        }
        common_fragment_recipe_comment_tv.setText(String.valueOf(recipe.getCommentsCount()));

        common_fragment_recipe_comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentMO comment = new CommentMO();
                comment.setTYPE_ID(recipe.getRCP_ID());
                comment.setTYPE("RECIPE");

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.FETCH_COMMENTS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, comment, 0);
            }
        });
    }

    private void setupImages() {
        common_fragment_recipe_vp.setAdapter(new RecipeViewImagesFullscreenViewPagerAdapter(mContext, recipe.getImages(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.recipe_view_images_fullscreen_item_image_iv == view.getId()){
                    Object array[] = new Object[]{common_fragment_recipe_vp.getCurrentItem(), recipe.getImages()};

                    Map<String, Object> bundleMap = new HashMap<String, Object>();
                    bundleMap.put(GENERIC_OBJECT, array);
                    bundleMap.put(LOGGED_IN_USER, loggedInUser);

                    Utility.showFragment(getFragmentManager(), FRAGMENT_RECIPE, FRAGMENT_RECIPE_IMAGES, new RecipeViewImagesFragment(), bundleMap);
                }
                else if(R.id.recipe_view_images_fullscreen_item_likes_ll == view.getId()){
                    ImageMO image = (ImageMO) view.getTag();

                    if(image == null){
                        Log.e(CLASS_NAME, "Image is null/empty");
                        return;
                    }

                    LikesMO like = new LikesMO();
                    like.setUSER_ID(loggedInUser.getUSER_ID());
                    like.setTYPE("RECIPE_IMG");
                    like.setTYPE_ID(image.getRCP_IMG_ID());

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                            AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like, view);
                }
                else if(R.id.recipe_view_images_fullscreen_item_comments_ll == view.getId()){
                    //TODO: comment recipe image
                }
                else{
                    Log.e(CLASS_NAME, "Could not identify the purpose of event on this view");
                }
            }
        }));

        common_fragment_recipe_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateImageCounter(++position, recipe.getImages().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateImageCounter(1, recipe.getImages().size());
    }

    private void updateImageCounter(int index, int maxCount) {
        recipe_view_images_count_tv.setText(index + "/" + maxCount);
    }

    public void updateRecipeLikeView(LikesMO like) {
        common_fragment_recipe_like_tv.setText(Utility.getSmartNumber(like.getLikesCount()));
        common_fragment_recipe_like_iv.setImageResource(Utility.getLikeImageId(recipe.isUserLiked()));
    }

    public void updateReviewView(ReviewMO review) {
        common_fragment_recipe_rating_tv.setText(String.valueOf(review.getAvgRating()));
        common_fragment_recipe_rating_iv.setImageResource(Utility.getReviewImageId(review.isUserReviewed()));
    }

    public void updateRecipeImageLikeView(LikesMO like, View layout) {
        layout.findViewById(R.id.recipe_view_images_fullscreen_item_likes_iv).setBackgroundResource(Utility.getLikeImageId(like.isUserLiked()));
        ((TextView)layout.findViewById(R.id.recipe_view_images_fullscreen_item_likes_tv)).setText(Utility.getSmartNumber(like.getLikesCount()));

        for(ImageMO image : recipe.getImages()){
            if(image.getRCP_IMG_ID() == like.getTYPE_ID()){
                image.setLikesCount(like.getLikesCount());
                image.setUserLiked(like.isUserLiked());
                break;
            }
        }
    }

    public void updateFavoriteView(ArrayList<FavouritesMO> fav) {
        if (fav.get(0).isFabStatus()) {
            common_fragment_recipe_favourite_iv.setImageResource(R.drawable.favstar36);
            Utility.showSnacks(common_fragment_recipe_rl, "Added to Favourites", OK, Snackbar.LENGTH_LONG);
        } else {
            common_fragment_recipe_favourite_iv.setImageResource(R.drawable.favstarunselected);
            Utility.showSnacks(common_fragment_recipe_rl, "Removed from Favourites", OK, Snackbar.LENGTH_LONG);
        }
    }

    public void setLikeView() {
        common_fragment_recipe_like_tv.setText(Utility.getSmartNumber(recipe.getLikesCount()));

        common_fragment_recipe_like_iv.setImageResource(Utility.getLikeImageId(recipe.isUserLiked()));

        common_fragment_recipe_like_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("RECIPE");
                like.setTYPE_ID(recipe.getRCP_ID());

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
                //new AsyncSubmitRecipeLike().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
            }
        });

        common_fragment_recipe_like_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "LIKE", recipe , 0);
                return true;
            }
        });
    }

    public void setfavouriteView() {
        if (recipe.isUserFavorite()) {
            common_fragment_recipe_favourite_iv.setImageResource(R.drawable.favstar36);
        } else {
            common_fragment_recipe_favourite_iv.setImageResource(R.drawable.favstarunselected);
        }

        common_fragment_recipe_favourite_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouritesMO fav = new FavouritesMO();
                fav.setUSER_ID(loggedInUser.getUSER_ID());
                fav.setRCP_ID(recipe.getRCP_ID());

                new AsyncSubmitFavourite().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fav);
            }
        });
    }

    public void setReviewView() {
        if (recipe.getAvgRating() != null && !recipe.getAvgRating().trim().isEmpty()) {
            common_fragment_recipe_rating_tv.setText(String.valueOf(recipe.getAvgRating()));
        }

        common_fragment_recipe_rating_iv.setImageResource(Utility.getReviewImageId(recipe.isUserReviewed()));

        common_fragment_recipe_rating_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                        AsyncTaskUtility.Purpose.FETCH_REVIEWS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe);
            }
        });
    }

    public void showReviewDeleteMessage() {
        MessageMO message = new MessageMO();
        message.setError(false);
        message.setPurpose("RECIPE_VIEW_REVIEW_DELETED");
        message.setErr_message("Your review has been deleted !");

        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE);
        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
    }

    public void showReviewAddMessage() {
        MessageMO message = new MessageMO();
        message.setError(false);
        message.setPurpose("RECIPE_VIEW_REVIEW_ADD");
        message.setErr_message("Thank You for the review !");

        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE);
        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
    }

    public void showCommentDeletedMessage() {
        MessageMO message = new MessageMO();
        message.setError(false);

        message.setPurpose("RECIPE_VIEW_COMMENT_DELETED");
        message.setErr_message("Your comment has been deleted !");

        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE);
        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
    }

    // Empty constructor required for DialogFragment
    public RecipeViewFragment() {
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

    public class AsyncSubmitFavourite extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object... objects) {
            FavouritesMO fav = (FavouritesMO) objects[0];
            return InternetUtility.submitFavourite(fav);
        }

        @Override
        protected void onPostExecute(Object object) {
            ArrayList<FavouritesMO> fav = (ArrayList<FavouritesMO>) object;

            if (fav != null) {
                recipe.setUserFavorite(fav.get(0).isFabStatus());
                updateFavoriteView(fav);
            }
        }
    }
}
