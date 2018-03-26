package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.cookery.activities.HomeActivity;
import com.cookery.fragments.MyRecipesFragment;
import com.cookery.fragments.MyReviewsFragment;
import com.cookery.fragments.ProfileViewEmailFragment;
import com.cookery.fragments.ProfileViewFragment;
import com.cookery.fragments.ProfileViewGenderFragment;
import com.cookery.fragments.ProfileViewImageFragment;
import com.cookery.fragments.RecipeViewCommentsFragment;
import com.cookery.fragments.RecipeViewFragment;
import com.cookery.fragments.RecipeViewReviewsFragment;
import com.cookery.fragments.UserViewFragment;
import com.cookery.fragments.UsersFragment;
import com.cookery.models.CommentMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_COMMENTS;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_MY_RECIPES;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_MY_REVIEWS;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_RECIPE;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_REVIEWS;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_USERS;
import static com.cookery.utils.AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS;
import static com.cookery.utils.AsyncTaskUtility.Purpose.SUBMIT_LIKE;
import static com.cookery.utils.AsyncTaskUtility.Purpose.UPDATE_USER;
import static com.cookery.utils.Constants.FRAGMENT_MY_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_IMAGE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_LIKED_USERS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_REVIEWS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_VIEWED_USERS;
import static com.cookery.utils.Constants.FRAGMENT_USERS;
import static com.cookery.utils.Constants.FRAGMENT_USER_VIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.GENERIC_OBJECT2;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 19/3/18.
 */

public class AsyncTaskUtility extends AsyncTask {
    private final String CLASS_NAME = this.getClass().getName();

    private Activity activity;
    private String fragmentKey;
    private Fragment waitFragment;
    private FragmentManager fragmentManager;
    private UserMO loggedInUser;
    private Integer index;

    public enum Purpose {
        FETCH_USER_PUBLIC_DETAILS, FETCH_USERS, FETCH_RECIPE, FETCH_COMMENTS, FETCH_REVIEWS,
        SUBMIT_LIKE, FETCH_MY_RECIPES, FETCH_MY_REVIEWS, UPDATE_USER
    }

    private Purpose purpose;

    @Override
    protected void onPreExecute() {}

    @Override
    protected Object doInBackground(Object[] objects) {
        if (purpose == null) {
            Log.e(CLASS_NAME, "Error ! Purpose is null ! ");
        } else if (purpose == FETCH_USER_PUBLIC_DETAILS) {
            return fetchUsersPublicDetails(objects);
        } else if (purpose == FETCH_USERS) {
            return fetchUsers(objects);
        } else if (purpose == FETCH_RECIPE) {
            return fetchRecipe(objects);
        } else if (purpose == FETCH_COMMENTS) {
            return fetchComments(objects);
        } else if (purpose == FETCH_REVIEWS) {
            return fetchReviews(objects);
        } else if (purpose == SUBMIT_LIKE) {
            return submitLike(objects);
        } else if (purpose == FETCH_MY_RECIPES) {
            return fetchMyRecipes();
        } else if (purpose == FETCH_MY_REVIEWS) {
            return fetchMyReviews();
        } else if (purpose == UPDATE_USER) {
            return updateUser(objects);
        } else {
            Log.e(CLASS_NAME, "Could not understand the purpose : " + purpose);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        if (waitFragment != null) {
            Utility.closeWaitDialog(fragmentManager, waitFragment);
        }

        if (object == null) {
            return;
        }

        if (purpose == FETCH_USER_PUBLIC_DETAILS) {
            postFetchUsersPublicDetails(object);
        } else if (purpose == FETCH_USERS) {
            postFetchUsers(object);
        } else if (purpose == FETCH_RECIPE) {
            postFetchRecipe(object);
        } else if (purpose == FETCH_COMMENTS) {
            postFetchComments(object);
        } else if (purpose == FETCH_REVIEWS) {
            postFetchReviews(object);
        } else if (purpose == SUBMIT_LIKE) {
            postSubmitLike(object);
        } else if (purpose == FETCH_MY_RECIPES) {
            postFetchMyRecipes(object);
        } else if (purpose == FETCH_MY_REVIEWS) {
            postFetchMyReviews(object);
        } else if (purpose == UPDATE_USER) {
            postUpdateUser(object);
        } else {
            Log.e(CLASS_NAME, "Could not understand the purpose : " + purpose);
        }
    }

    private Object updateUser(Object[] objects) {
        if(objects != null && objects.length > 0){
            if("EMAIL".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserEmail(loggedInUser)};
            }
            else if("IMAGE".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserImage(loggedInUser)};
            }
            else if("GENDER".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserGender(loggedInUser)};
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+objects);
            }
        }

        return null;
    }

    private void postUpdateUser(Object object) {
        Object[] objs = (Object[]) object;

        String what = String.valueOf(objs[0]);
        MessageMO message = (MessageMO) objs[1];

        if(message != null && !message.isError()){
            Fragment fragment = getFragment(fragmentKey);

            if(fragment instanceof ProfileViewEmailFragment){
                if("EMAIL".equalsIgnoreCase(what)){
                    ((ProfileViewEmailFragment)fragment).updateEmail();
                    message.setPurpose("USER_UPDATE_EMAIL_SUCCESS");
                }
            }
            else if(fragment instanceof ProfileViewGenderFragment){
                if("GENDER".equalsIgnoreCase(what)){
                    ((ProfileViewGenderFragment)fragment).updateGender();
                    message.setPurpose("USER_UPDATE_GENDER_SUCCESS");
                }
            }
            else if(fragment instanceof ProfileViewFragment){
                if("IMAGE".equalsIgnoreCase(what)){
                    ((ProfileViewFragment)fragment).updateUserImage();
                    message.setPurpose("USER_UPDATE_IMAGE_SUCCESS");
                }
            }
            else{
                Log.e(CLASS_NAME, "Fragment("+fragment+") could not be understood");
                return;
            }
        }
        else{
            if(message == null){
                message = new MessageMO();
                message.setError(true);
                message.setErr_message("Something went wrong !");
            }
        }

        Utility.showMessageDialog(fragmentManager, null, message);
    }

    private Object fetchMyReviews() {
        return InternetUtility.fetchMyReviews(loggedInUser.getUSER_ID(), index);
    }

    private void postFetchMyReviews(Object object) {
        if (object == null) {
            return;
        }

        List<ReviewMO> myReviews = (List<ReviewMO>) object;

        if (fragmentManager != null && fragmentKey != null && !fragmentKey.trim().isEmpty()) {
            Fragment fragment = getFragment(fragmentKey);
            if (fragment instanceof MyReviewsFragment) {
                MyReviewsFragment myReviewsFragment = (MyReviewsFragment) fragment;

                if (myReviews != null || !myReviews.isEmpty()) {
                    myReviewsFragment.updateReviews(myReviews, index);
                } else {
                    myReviewsFragment.removeOnBottomReachedListener();
                }
            }
        }
    }

    private Object fetchMyRecipes() {
        return InternetUtility.fetchMyRecipes(loggedInUser.getUSER_ID(), index);
    }

    private void postFetchMyRecipes(Object object) {
        if (object == null) {
            return;
        }

        List<RecipeMO> myRecipes = (List<RecipeMO>) object;

        if (fragmentManager != null && fragmentKey != null && !fragmentKey.trim().isEmpty()) {
            Fragment fragment = getFragment(fragmentKey);
            if (fragment instanceof MyRecipesFragment) {
                MyRecipesFragment myRecipesFragment = (MyRecipesFragment) fragment;

                if (myRecipes != null || !myRecipes.isEmpty()) {
                    myRecipesFragment.updateRecipes(myRecipes, index);
                } else {
                    myRecipesFragment.removeOnBottomReachedListener();
                }
            }
        } else if (activity != null) {
            if (activity instanceof HomeActivity) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, myRecipes);
                paramsMap.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(activity.getFragmentManager(), null, FRAGMENT_MY_RECIPE, new MyRecipesFragment(), paramsMap);
            }

        }
    }

    private Object submitLike(Object[] objects) {
        LikesMO like = (LikesMO) objects[0];

        if ("RECIPE_IMG".equalsIgnoreCase(like.getTYPE())) {
            Object array[] = new Object[]{InternetUtility.submitLike((LikesMO) objects[0]), objects[1]};
            return array;
        } else if ("RECIPE".equalsIgnoreCase(like.getTYPE())) {
            Object array[] = new Object[]{InternetUtility.submitLike((LikesMO) objects[0])};
            return array;
        }
        else if ("USER".equalsIgnoreCase(like.getTYPE())) {
            Object array[] = new Object[]{InternetUtility.submitLike((LikesMO) objects[0])};
            return array;
        }

        return null;
    }

    private void postSubmitLike(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        LikesMO like = (LikesMO) array[0];
        if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            RecipeViewFragment fragment = (RecipeViewFragment) getFragment(fragmentKey);

            if ("RECIPE".equalsIgnoreCase(like.getTYPE())) {
                fragment.updateRecipeLikeView(like);
            }
            else if ("RECIPE_IMG".equalsIgnoreCase(like.getTYPE())) {
                fragment.updateRecipeImageLikeView(like, (View) array[1]);
            } else {
                Log.e(CLASS_NAME, "Could not understand");
            }
        }
        else if(FRAGMENT_PROFILE_VIEW_IMAGE.equalsIgnoreCase(fragmentKey)){
            ProfileViewImageFragment fragment = (ProfileViewImageFragment) getFragment(fragmentKey);

            if ("USER".equalsIgnoreCase(like.getTYPE())) {
                loggedInUser.setUserLiked(like.isUserLiked());
                loggedInUser.setLikesCount(like.getLikesCount());
                fragment.setupLikeView(loggedInUser);
            }
        }
        else {
            Log.e(CLASS_NAME, "Missing required objects");
        }
    }

    private Object fetchReviews(Object[] objects) {
        if (objects == null || objects.length < 1) {
            Log.e(CLASS_NAME, "Error ! Required objects are missing");
        } else if (!(objects[0] instanceof RecipeMO)) {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objects[0]);
        } else if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            waitFragment = Utility.showWaitDialog(fragmentManager, "fetching reviews ..");

            ReviewMO userReview = InternetUtility.fetchUsersRecipeReview(loggedInUser, (RecipeMO) objects[0]);
            List<ReviewMO> reviews = InternetUtility.fetchRecipeReviews(loggedInUser, (RecipeMO) objects[0], index);
            Object array[] = new Object[]{objects[0], userReview, reviews};

            return array;
        } else {
            Log.e(CLASS_NAME, "Something wrong !");
        }

        return null;
    }

    private void postFetchReviews(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        if (array.length == 3) {
            if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
                Map<String, Object> params = new HashMap<>();
                params.put(SELECTED_ITEM, array[0]);
                params.put(GENERIC_OBJECT, array[1]);
                params.put(GENERIC_OBJECT2, array[2]);
                params.put(LOGGED_IN_USER, loggedInUser);
                Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_RECIPE_REVIEWS, new RecipeViewReviewsFragment(), params);
            } else {
                Log.e(CLASS_NAME, "Something went wrong !");
            }
        } else {
            Log.e(CLASS_NAME, "Missing required objects");
        }
    }

    private Object fetchComments(Object[] objects) {
        if (objects == null || objects.length < 1) {
            Log.e(CLASS_NAME, "Error ! Required objects are missing");
        } else if (!(objects[0] instanceof CommentMO)) {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objects[0]);
        } else if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            waitFragment = Utility.showWaitDialog(fragmentManager, "fetching comments ..");

            List<CommentMO> comments = InternetUtility.fetchComments(loggedInUser, (CommentMO) objects[0], index);
            Object array[] = new Object[]{objects[0], comments};

            return array;
        } else {
            Log.e(CLASS_NAME, "Something wrong !");
        }

        return null;
    }

    private void postFetchComments(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        if (array.length == 2) {
            if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, array[1]);
                params.put(SELECTED_ITEM, array[0]);
                params.put(LOGGED_IN_USER, loggedInUser);
                Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_RECIPE_COMMENTS, new RecipeViewCommentsFragment(), params);
            } else {
                Log.e(CLASS_NAME, "Something went wrong !");
            }
        } else {
            Log.e(CLASS_NAME, "Missing required objects");
        }
    }

    private Object fetchRecipe(Object[] objects) {
        if (objects == null || objects.length < 1) {
            Log.e(CLASS_NAME, "Error ! Required objects are missing");
        } else if (!(objects[0] instanceof RecipeMO)) {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objects[0]);
        } else if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            waitFragment = Utility.showWaitDialog(fragmentManager, "updating the Recipe ..");

            List<RecipeMO> recipes = (List<RecipeMO>) InternetUtility.fetchRecipe((RecipeMO) objects[0], loggedInUser.getUSER_ID());
            Object array[] = new Object[]{recipes == null || recipes.isEmpty() ? null : recipes.get(0)};

            return array;
        } else {
            Log.e(CLASS_NAME, "Could not understand : " + objects[1]);
        }

        return null;
    }

    private void postFetchRecipe(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        if (array.length == 1) {
            if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
                RecipeViewFragment fragment = (RecipeViewFragment) getFragment(fragmentKey);
                fragment.setRecipe((RecipeMO) array[0]);
                fragment.setupPage();
            } else {
                Log.e(CLASS_NAME, "Something wrong !");
            }
        } else {
            Log.e(CLASS_NAME, "Missing required objects");
        }
    }


    private void postFetchUsers(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        if ("LIKE".equalsIgnoreCase(String.valueOf(array[0]))) {
            Map<String, Object> params = new HashMap<>();
            params.put(GENERIC_OBJECT, array);
            params.put(SELECTED_ITEM, array[2]);
            params.put(LOGGED_IN_USER, loggedInUser);

            Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_RECIPE_LIKED_USERS, new UsersFragment(), params);
        } else if ("VIEW".equalsIgnoreCase(String.valueOf(array[0]))) {
            Map<String, Object> params = new HashMap<>();
            params.put(GENERIC_OBJECT, array);
            params.put(SELECTED_ITEM, array[2]);
            params.put(LOGGED_IN_USER, loggedInUser);

            Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_RECIPE_VIEWED_USERS, new UsersFragment(), params);
        }
        else if ("FOLLOWERS".equalsIgnoreCase(String.valueOf(array[0])) || "FOLLOWINGS".equalsIgnoreCase(String.valueOf(array[0]))) {
            Object temp[] = new Object[]{String.valueOf(array[0]), array[1]};
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(GENERIC_OBJECT, temp);
            params.put(SELECTED_ITEM, loggedInUser);
            params.put(LOGGED_IN_USER, loggedInUser);

            Utility.showFragment(fragmentManager, FRAGMENT_PROFILE_VIEW, FRAGMENT_USERS, new UsersFragment(), params);
        }
    }

    private Fragment getFragment(String fragmentKey) {
        if (fragmentManager != null) {
            return fragmentManager.findFragmentByTag(fragmentKey);
        }

        return null;
    }

    private void postFetchUsersPublicDetails(Object object) {
        List<UserMO> users = (List<UserMO>) object;

        if (users != null && !users.isEmpty()) {
            Map<String, Object> bundleMap = new HashMap<String, Object>();
            bundleMap.put(GENERIC_OBJECT, users.get(0));
            Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_USER_VIEW, new UserViewFragment(), bundleMap);
        } else {
            Log.e(CLASS_NAME, "Failed to fetch users details");
        }
    }

    private Object fetchUsers(Object[] objects) {
        if(objects != null && objects.length > 0){
            if ("FOLLOWERS".equalsIgnoreCase(String.valueOf(objects[0])) || "FOLLOWINGS".equalsIgnoreCase(String.valueOf(objects[0]))) {
                List<UserMO> users = null;

                if("FOLLOWERS".equalsIgnoreCase(String.valueOf(objects[0]))){
                    waitFragment = Utility.showWaitDialog(fragmentManager, "fetching followers ..");
                    users = InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
                }
                else if("FOLLOWINGS".equalsIgnoreCase(String.valueOf(objects[0]))){
                    waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users ..");
                    users = InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
                }

                return new Object[]{String.valueOf(objects[0]), users};
            }
            else if ("LIKE".equalsIgnoreCase(String.valueOf(objects[0]))) {
                waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users who liked the Recipe ..");

                List<UserMO> users = InternetUtility.fetchLikedUsers(loggedInUser.getUSER_ID(), "RECIPE", ((RecipeMO) objects[1]).getRCP_ID(), index);
                Object array[] = new Object[]{String.valueOf(objects[0]), users, objects[1]};

                return array;
            } else if ("VIEW".equalsIgnoreCase(String.valueOf(objects[0]))) {
                waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users who viewed the Recipe ..");

                List<UserMO> users = InternetUtility.fetchViewedUsers(loggedInUser.getUSER_ID(), (RecipeMO) objects[1], index);
                Object array[] = new Object[]{String.valueOf(objects[0]), users, objects[1]};

                return array;
            }
            else {
                Log.e(CLASS_NAME, "Could not understand : " + objects[1]);
            }
        }

        return null;
    }

    private Object fetchUsersPublicDetails(Object[] objects) {
        if (objects == null || objects.length < 1) {
            Log.e(CLASS_NAME, "Error ! Required objects are missing");
        } else if (!(objects[0] instanceof Integer)) {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objects[0]);
        } else {
            waitFragment = Utility.showWaitDialog(fragmentManager, "fetching user details ..");
            return InternetUtility.fetchUsersPublicDetails(Integer.parseInt(String.valueOf(objects[0])), loggedInUser.getUSER_ID());
        }

        return null;
    }

    //for n/w operations from a fragment
    public AsyncTaskUtility(FragmentManager fragmentManager, String fragmentKey, Purpose purpose, UserMO loggedInUser, Integer index) {
        this.fragmentManager = fragmentManager;
        this.fragmentKey = fragmentKey;
        this.purpose = purpose;
        this.loggedInUser = loggedInUser;

        if (index == null) {
            this.index = 0;
        } else {
            this.index = index;
        }
    }

    //for n/w operations from an activity
    public AsyncTaskUtility(Activity activity, Purpose purpose, UserMO loggedInUser, Integer index) {
        this.activity = activity;
        this.purpose = purpose;
        this.loggedInUser = loggedInUser;

        if (index == null) {
            this.index = 0;
        } else {
            this.index = index;
        }
    }
}
