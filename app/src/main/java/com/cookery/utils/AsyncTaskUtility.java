package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.cookery.activities.HomeActivity;
import com.cookery.exceptions.CookeryException;
import com.cookery.fragments.CommentsFragment;
import com.cookery.fragments.CookeryErrorFragment;
import com.cookery.fragments.DeleteCommentFragment;
import com.cookery.fragments.ImagesFragment;
import com.cookery.fragments.IngredientViewFragment;
import com.cookery.fragments.LoginFragment;
import com.cookery.fragments.MessageFragment;
import com.cookery.fragments.MyRecipesFragment;
import com.cookery.fragments.MyReviewsFragment;
import com.cookery.fragments.NoInternetFragment;
import com.cookery.fragments.PeopleViewFragment;
import com.cookery.fragments.ProfileViewFragment;
import com.cookery.fragments.ProfileViewImageFragment;
import com.cookery.fragments.RecipeAddFragment;
import com.cookery.fragments.RecipeViewFragment;
import com.cookery.fragments.RecipeViewReviewsFragment;
import com.cookery.fragments.SomethingWrongFragment;
import com.cookery.fragments.UserViewFragment;
import com.cookery.fragments.UsersFragment;
import com.cookery.models.CommentMO;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_MESSAGE;
import static com.cookery.utils.Constants.FRAGMENT_COOKERY_ERROR;
import static com.cookery.utils.Constants.FRAGMENT_IMAGES;
import static com.cookery.utils.Constants.FRAGMENT_INGREDIENT_NUTRIENTS;
import static com.cookery.utils.Constants.FRAGMENT_LOGIN;
import static com.cookery.utils.Constants.FRAGMENT_MY_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_NO_INTERNET;
import static com.cookery.utils.Constants.FRAGMENT_PEOPLE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_IMAGE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_REVIEWS;
import static com.cookery.utils.Constants.FRAGMENT_SOMETHING_WRONG;
import static com.cookery.utils.Constants.FRAGMENT_USERS;
import static com.cookery.utils.Constants.FRAGMENT_USER_VIEW;
import static com.cookery.utils.Constants.GALLERY_DIR;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.GENERIC_OBJECT2;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;
import static com.cookery.utils.Constants.UN_IDENTIFIED_PARENT_FRAGMENT;

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
        FETCH_USER_PUBLIC_DETAILS, FETCH_USER_SELF, FETCH_USERS, FETCH_RECIPE, FETCH_RECIPE_IMAGES,
        FETCH_COMMENTS, FETCH_REVIEWS, FETCH_MY_RECIPES, FETCH_MY_REVIEWS, FETCH_MASTER_DATA,
        FETCH_PEOPLE, FETCH_FOLLOWERS, FETCH_FOLLOWING, FETCH_INGREDIENT_NUTRIENTS,
        FETCH_TIMELINE_SELF, FETCH_STORIES_SELF, FETCH_TRENDS_SELF,
        SUBMIT_LIKE, SUBMIT_COMMENT, SUBMIT_RECIPE,
        DELETE_COMMENT,
        CHECK_INTERNET,
        UPDATE_USER,
        DOWNLOAD_IMAGES
    }

    private Purpose purpose;

    @Override
    protected void onPreExecute() {}

    @Override
    protected Object doInBackground(Object[] objects) {
        try{
            if (purpose == null) {
                Log.e(CLASS_NAME, "Error ! Purpose is null ! ");
                return null;
            }

            switch (purpose){
                case FETCH_USER_SELF            : return fetchUserSelf();
                case FETCH_USERS                : return fetchUsers(objects);
                case FETCH_USER_PUBLIC_DETAILS  : return fetchUsersPublicDetails(objects);
                case FETCH_RECIPE               : return fetchRecipe(objects);
                case FETCH_RECIPE_IMAGES        : return fetchRecipeImages(objects);
                case FETCH_COMMENTS             : return fetchComments(objects);
                case FETCH_REVIEWS              : return fetchReviews(objects);
                case FETCH_MY_RECIPES           : return fetchMyRecipes();
                case FETCH_MY_REVIEWS           : return fetchMyReviews();
                case FETCH_PEOPLE               : return fetchPeople();
                case FETCH_FOLLOWERS            : return fetchFollowers();
                case FETCH_FOLLOWING            : return fetchFollowings();
                case FETCH_MASTER_DATA          : return fetchMasterData();
                case FETCH_INGREDIENT_NUTRIENTS : return fetchIngredientNutrients(objects);
                case FETCH_TIMELINE_SELF        : return fetchTimelineSelf();
                case FETCH_STORIES_SELF         : return fetchStoriesSelf();
                case FETCH_TRENDS_SELF          : return fetchTrendsSelf();
                case SUBMIT_LIKE                : return submitLike(objects);
                case SUBMIT_COMMENT             : return submitComment(objects);
                case SUBMIT_RECIPE              : return submitRecipe(objects);
                case UPDATE_USER                : return updateUser(objects);
                case DELETE_COMMENT             : return deleteComment(objects);
                case CHECK_INTERNET             : return checkInternet();
                case DOWNLOAD_IMAGES            : return downloadImages(objects);

                default: Log.e(CLASS_NAME, "Error ! Purpose("+purpose+") is not handled in "+CLASS_NAME+".doInBackground() ! ");
                throw new CookeryException(CookeryException.ErrorCode.SOMETHING_WRONG);
            }
        }
        catch (CookeryException ce){
            closeWaitFragment();

            if(ce.getCode().equals(CookeryException.ErrorCode.NO_INTERNET)){
                Utility.showFragment(fragmentManager, null, FRAGMENT_NO_INTERNET, new NoInternetFragment(), null);
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.ACCESS_DENIED)){
                //TODO:You are not authorized. Please update Cookery to continue using.
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.BAD_JSON)){
                Utility.showFragment(fragmentManager, null, FRAGMENT_COOKERY_ERROR, new CookeryErrorFragment(), null);
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.JSON_TO_OBJECT_MAPPING_ERROR)){
                Utility.showFragment(fragmentManager, null, FRAGMENT_COOKERY_ERROR, new CookeryErrorFragment(), null);
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.NO_JSON_MAPPING_CLASS)){
                Utility.showFragment(fragmentManager, null, FRAGMENT_COOKERY_ERROR, new CookeryErrorFragment(), null);
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.GATEWAY_TIMEOUT)){
                //TODO:It seems that bee's are too busy today. Arranging more bees for you.
            }
            else if(ce.getCode().equals(CookeryException.ErrorCode.SOMETHING_WRONG)){
                Utility.showFragment(fragmentManager, null, FRAGMENT_SOMETHING_WRONG, new SomethingWrongFragment(), null);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        closeWaitFragment();

        if (object == null) {
            Log.e(CLASS_NAME, "Error ! PHP service returned null");
            return;
        }


        if (purpose == null) {
            Log.e(CLASS_NAME, "Error ! Purpose is null ! ");
        }

        switch (purpose){
            case FETCH_USER_SELF            : postFetchUserSelf(object); break;
            case FETCH_USERS                : postFetchUsers(object); break;
            case FETCH_USER_PUBLIC_DETAILS  : postFetchUsersPublicDetails(object); break;
            case FETCH_RECIPE               : postFetchRecipe(object); break;
            case FETCH_RECIPE_IMAGES        : postFetchRecipeImages(object); break;
            case FETCH_COMMENTS             : postFetchComments(object); break;
            case FETCH_REVIEWS              : postFetchReviews(object); break;
            case FETCH_MY_RECIPES           : postFetchMyRecipes(object); break;
            case FETCH_MY_REVIEWS           : postFetchMyReviews(object); break;
            case FETCH_PEOPLE               : postFetchPeople(object); break;
            case FETCH_FOLLOWERS            : postFetchFollowers(object); break;
            case FETCH_FOLLOWING            : postFetchFollowings(object); break;
            case FETCH_MASTER_DATA          : postFetchMasterData(object); break;
            case FETCH_TIMELINE_SELF        : postFetchTimelineSelf(object); break;
            case FETCH_STORIES_SELF         : postFetchStoriesSelf(object); break;
            case FETCH_TRENDS_SELF          : postFetchTrendsSelf(object); break;
            case FETCH_INGREDIENT_NUTRIENTS : postFetchIngredientNutrients(object); break;
            case SUBMIT_LIKE                : postSubmitLike(object); break;
            case SUBMIT_RECIPE              : postSubmitRecipe(object); break;
            case UPDATE_USER                : postUpdateUser(object); break;
            case DELETE_COMMENT             : postDeleteComment(object); break;
            case CHECK_INTERNET             : postCheckInternet(object); break;
            case DOWNLOAD_IMAGES            : postDownloadImages(object); break;

            default: Log.e(CLASS_NAME, "Error ! Purpose("+purpose+") is not handled in "+CLASS_NAME+".onPostExecute() ! ");
            break;
        }
    }

    private Object downloadImages(Object[] objects){
        List<? extends Object> images = (List<? extends Object>) objects[0];

        URL url;
        String storeDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + GALLERY_DIR;

        InputStream is = null;
        FileOutputStream fos=null;

        boolean failed = false;
        for (int i = 0; i < images.size(); i++) {
            try {
                String image = SERVER_ADDRESS;

                if(images.get(i) instanceof RecipeImageMO){
                    image += ((RecipeImageMO) images.get(i)).getRCP_IMG();
                }
                else if(images.get(i) instanceof UserMO){
                    image += ((UserMO) images.get(i)).getIMG();
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+images.get(0));
                }

                url = new URL(image);
                String pathl = "";
                int count;

                File f = new File(storeDir);
                if (!f.exists()) {
                    f.mkdirs();
                }
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                is = con.getInputStream();
                String pathr = url.getPath();
                String filename = pathr.substring(pathr.lastIndexOf('/') + 1);
                pathl = storeDir + "/" + filename;
                fos = new FileOutputStream(pathl);

                byte data[] = new byte[1024];

                if(objects[i] instanceof RecipeImageMO){
                    publishProgress(((RecipeImageMO) images.get(i)).getRCP_IMG());
                }
                else if(objects[i] instanceof UserMO){
                    publishProgress(((UserMO) images.get(i)).getIMG());
                }

                while ((count = is.read(data)) != -1) {
                    fos.write(data, 0, count);
                }

                fos.close();
                is.close();
                fos.flush();
            } catch (IOException e) {
                failed = true;
                throw new CookeryException(CookeryException.ErrorCode.NO_INTERNET, fragmentManager, "No Internet Connection", e);
            }
            finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos.flush();
                    }
                    if (is != null) {
                        is.close();
                    }
                }
                catch (Exception e){
                    failed = true;
                    throw new CookeryException(CookeryException.ErrorCode.SOMETHING_WRONG, fragmentManager, "Something is wrong", e);
                }
            }
        }

        return failed;
    }

    private void postDownloadImages(Object object){
        boolean failed = (boolean) object;

        if(!failed){
            if(getFragment(fragmentKey) instanceof ImagesFragment){
                ((ImagesFragment)getFragment(fragmentKey)).showDownloadComplete();
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+fragmentKey);
            }
        }
    }

    private void postFetchTrendsSelf(Object object) {
        List<TrendMO> trends = (List<TrendMO>) object;

        if(trends == null || trends.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Trends are null/empty");
            return;
        }

        if(activity != null){
            if(activity instanceof HomeActivity){
                ((HomeActivity) activity).updateTrends(trends);
            }
        }
    }

    private Object fetchTrendsSelf() {
        return InternetUtility.fetchTrends(loggedInUser.getUSER_ID());
    }

    private void postFetchStoriesSelf(Object object) {
        List<TimelineMO> stories = (List<TimelineMO>) object;

        if(stories == null || stories.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Stories are null/empty");
            return;
        }

        if(activity != null){
            if(activity instanceof HomeActivity){
                ((HomeActivity) activity).updateStories(index, stories);
            }
        }
    }

    private Object fetchStoriesSelf() {
        return InternetUtility.fetchUserStories(loggedInUser.getUSER_ID(), index);
    }

    private void postFetchTimelineSelf(Object object) {
        List<TimelineMO> timelines = (List<TimelineMO>) object;

        if(timelines == null || timelines.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Timelines are null/empty");
            return;
        }

        if(activity != null){
            if(activity instanceof HomeActivity){
                ((HomeActivity) activity).updateTimelines(index, timelines);
            }
        }
    }

    private Object fetchTimelineSelf() {
        return InternetUtility.fetchUserTimeline(loggedInUser.getUSER_ID(), index);
    }

    private Object fetchIngredientNutrients(Object[] objects){
        waitFragment = Utility.showWaitDialog(fragmentManager, "loading ..");
        return InternetUtility.fetchIngredientNutrients((IngredientAkaMO) objects[0]);
    }

    private void postFetchIngredientNutrients(Object object){
        List<IngredientMO> ingredients = (List<IngredientMO>) object;

        if(ingredients == null || ingredients.isEmpty()){
            Log.e(CLASS_NAME, "Error ! ingredients are null/empty");
            return;
        }

        IngredientMO ingredient = ingredients.get(0);

        if(ingredient == null){
            Log.e(CLASS_NAME, "Error ! ingredient is null/empty");
            return;
        }

        if(getFragment(fragmentKey) instanceof RecipeViewFragment){
            Map<String, Object> params = new HashMap<>();
            params.put(GENERIC_OBJECT, ingredient);
            Utility.showFragment(fragmentManager, fragmentKey, FRAGMENT_INGREDIENT_NUTRIENTS, new IngredientViewFragment(), params);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+getFragment(fragmentKey));
        }
    }

    private Object fetchFollowers(){
        return InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
    }

    private void postFetchFollowers(Object object) {
        List<UserMO> users = (List<UserMO>) object;

        if(getFragment(fragmentKey) instanceof PeopleViewFragment){
            PeopleViewFragment fragment = (PeopleViewFragment) getFragment(fragmentKey);

            if(users != null && !users.isEmpty()){
                fragment.updateUsers("FOLLOWERS", users, index);
            }
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+fragmentKey);
        }
    }

    private Object fetchFollowings(){
        return InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
    }

    private void postFetchFollowings(Object object) {
        List<UserMO> users = (List<UserMO>) object;

        if(getFragment(fragmentKey) instanceof PeopleViewFragment){
            PeopleViewFragment fragment = (PeopleViewFragment) getFragment(fragmentKey);

            if(users != null && !users.isEmpty()){
                fragment.updateUsers("FOLLOWINGS", users, index);
            }
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+fragmentKey);
        }
    }

    private Object fetchPeople(){
        waitFragment = Utility.showWaitDialog(fragmentManager, "fetching people who follow you &amp; whom you follow..");

        Object[] people = new Object[2];

        people[0] = InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
        people[1] = InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);

        return people;
    }

    private void postFetchPeople(Object object) {
        Object[] people = (Object[]) object;

        if(people != null && people.length != 0) {
            if(activity != null){
                if(activity instanceof HomeActivity){
                    Map<String, Object> params = new HashMap<>();
                    params.put(GENERIC_OBJECT, people);
                    Utility.showFragment(fragmentManager, null, FRAGMENT_PEOPLE_VIEW, new PeopleViewFragment(), params);
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+activity);
                }
            }
            else{
                Log.e(CLASS_NAME, "Error ! Activity is null");
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not fetch followers/following users");
        }
    }

    private Object fetchMasterData(){
        waitFragment = Utility.showWaitDialog(fragmentManager, "wait a moment ..");
        return InternetUtility.fetchMasterData();
    }

    private void postFetchMasterData(Object object) {
        MasterDataMO masterData = (MasterDataMO) object;

        if(masterData != null && masterData.getFoodCuisines() != null && !masterData.getFoodCuisines().isEmpty()
                && masterData.getFoodTypes() != null && !masterData.getFoodTypes().isEmpty()
                && masterData.getIngredientUOMs() != null && !masterData.getIngredientUOMs().isEmpty()){

            if(activity != null && activity instanceof HomeActivity){
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(MASTER, masterData);
                paramsMap.put(GENERIC_OBJECT, new RecipeMO());

                Utility.showFragment(fragmentManager, null, FRAGMENT_ADD_RECIPE, new RecipeAddFragment(), paramsMap);
            }
            else{
                Log.e(CLASS_NAME, "Error ! Un Expected Activity : "+activity);
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not fetch some master data");
        }
    }

    private Object submitRecipe(Object[] objects){
        waitFragment = Utility.showWaitDialog(fragmentManager, "submitting your recipe ..");
        return InternetUtility.submitRecipe((RecipeMO) objects[0]);
    }

    private void postSubmitRecipe(Object object){
        if(object == null){
            return;
        }


        MessageMO message = (MessageMO) object;
        message.setPurpose("ADD_RECIPE");

        Utility.showMessageDialog(fragmentManager, getFragment(FRAGMENT_ADD_RECIPE), message);

        if(message.isError()){
            Log.e(CLASS_NAME, "Error : "+message.getErr_message());
        }
        else{
            Log.i(CLASS_NAME, message.getErr_message());

            RecipeAddFragment recipeAddFragment = (RecipeAddFragment) getFragment(FRAGMENT_ADD_RECIPE);
            recipeAddFragment.dismiss();
        }
    }

    private Object deleteComment(Object[] objects){
        InternetUtility.deleteComment(loggedInUser, (CommentMO) objects[0]);
        return objects[0];
    }

    private void postDeleteComment(Object object){
        ((DeleteCommentFragment) getFragment(fragmentKey)).deleteComment((CommentMO) object);
    }

    private Object submitComment(Object[] objects) {
        return InternetUtility.submitComment((CommentMO)objects[0]);
    }

    private void postSubmitComment(Object object) {
        List<CommentMO> comments = (List<CommentMO>) object;

        if(getFragment(fragmentKey) instanceof CommentsFragment && comments != null && !comments.isEmpty()){
            CommentsFragment fragment = (CommentsFragment) getFragment(fragmentKey);
            fragment.addComment(comments.get(0));
        }
    }

    private Object fetchRecipeImages(Object object){
        waitFragment = Utility.showWaitDialog(fragmentManager, "loading ..");

        Object[] objects = (Object[]) object;

        return new Object[]{InternetUtility.fetchRecipeImages(loggedInUser, (RecipeMO)objects[0]), objects[1]};
    }

    private void postFetchRecipeImages(Object object){
        Object[] objects = (Object[]) object;

        if(getFragment(fragmentKey) instanceof RecipeViewFragment){
            Object array[] = new Object[]{objects[1], objects[0]};

            Map<String, Object> bundleMap = new HashMap<>();
            bundleMap.put(GENERIC_OBJECT, array);
            bundleMap.put(LOGGED_IN_USER, loggedInUser);

            Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_IMAGES, new ImagesFragment(), bundleMap);
        }
    }

    private Object checkInternet(){
        return InternetUtility.isInternetAvailable();
    }

    private void postCheckInternet(Object object){
        if((boolean)object){
            if(getFragment(fragmentKey) instanceof NoInternetFragment){
                ((NoInternetFragment) getFragment(fragmentKey)).dismiss();
            }
        }
        else{
            ((NoInternetFragment) getFragment(fragmentKey)).checkInternet();
        }
    }

    private void closeWaitFragment() {
        if (waitFragment != null) {
            Utility.closeWaitDialog(fragmentManager, waitFragment);
        }
    }

    private Object fetchUserSelf(){
        waitFragment = Utility.showWaitDialog(fragmentManager, "fetching your data ..");
        return InternetUtility.fetchUser(loggedInUser.getUSER_ID());
    }

    private void postFetchUserSelf(Object object){
        if(object == null){
            return;
        }

        List<UserMO> user = (List<UserMO>) object;

        if(activity != null){
            if(activity instanceof HomeActivity){
                HomeActivity homeActivity = (HomeActivity) activity;

                if(user != null && !user.isEmpty()) {
                    homeActivity.updateUserSecurity(user.get(0));

                    Map<String, Object> params = new HashMap<>();
                    params.put(GENERIC_OBJECT, user.get(0));
                    Utility.showFragment(fragmentManager, null, FRAGMENT_PROFILE_VIEW, new ProfileViewFragment(), params);
                }
                else{
                    Utility.showFragment(fragmentManager, null, FRAGMENT_LOGIN, new LoginFragment(), null);
                }
            }
        }
        else if(getFragment(fragmentKey) instanceof ProfileViewFragment){
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(GENERIC_OBJECT, user.get(0));
            paramsMap.put(LOGGED_IN_USER, user.get(0));
            Utility.showFragment(fragmentManager, FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_IMAGE, new ProfileViewImageFragment(), paramsMap);
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
            else if("NAME".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserName(loggedInUser)};
            }
            else if("PHONE".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserPhone(loggedInUser)};
            }
            else if("PASSWORD".equalsIgnoreCase(String.valueOf(objects[0]))){
                return new Object[]{objects[0], InternetUtility.updateUserPassword(loggedInUser)};
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+objects);
            }
        }

        return null;
    }

    private void postUpdateUser(Object object) {
        Object[] objects = (Object[]) object;

        if(objects == null || objects.length == 0){
            Log.e(CLASS_NAME, "Error ! Objecs are empty");
            return;
        }

        String what = String.valueOf(objects[0]);
        List<UserMO> users = (List<UserMO>)objects[1];

        if(users != null && !users.isEmpty() && users.get(0) != null && users.get(0).getUSER_ID() != 0){
            UserMO user = users.get(0);

            Fragment fragment = getFragment(fragmentKey);
            ((HomeActivity)fragment.getActivity()).updateUserSecurity(user);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not update user's : "+what);

            if(what.equalsIgnoreCase("PASSWORD")){
                MessageMO message = new MessageMO();
                message.setErr_code(0);
                message.setError(true);
                message.setPurpose("USER_UPDATE_PASSWORD_FAILED");
                message.setErr_message("Incorrect Password !");

                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, message);
                Utility.showFragment(fragmentManager, fragmentKey, FRAGMENT_COMMON_MESSAGE, new MessageFragment(), params);
            }
        }
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
        return InternetUtility.submitLike((LikesMO) objects[0]);
    }

    private void postSubmitLike(Object object) {
        LikesMO like = (LikesMO) object;

        if (like == null) {
            Log.e(CLASS_NAME, "Error ! Failed to like/unlike");
            return;
        }
    }

    private Object fetchReviews(Object[] objects) {
        if (objects == null || objects.length < 1) {
            Log.e(CLASS_NAME, "Error ! Required objects are missing");
        } else if (!(objects[0] instanceof RecipeMO)) {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objects[0]);
        } else if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            waitFragment = Utility.showWaitDialog(fragmentManager, "fetching reviews ..");

            List<ReviewMO> userReview = (List<ReviewMO>) InternetUtility.fetchUsersRecipeReview(loggedInUser, (RecipeMO) objects[0]);
            List<ReviewMO> reviews = InternetUtility.fetchRecipeReviews(loggedInUser, (RecipeMO) objects[0], index);
            Object array[] = new Object[]{objects[0], userReview == null || userReview.isEmpty() ? null : userReview.get(0), reviews};

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
        if(!(getFragment(fragmentKey) instanceof CommentsFragment)){
            waitFragment = Utility.showWaitDialog(fragmentManager, "fetching comments ..");
        }

        List<CommentMO> comments = InternetUtility.fetchComments(loggedInUser, (CommentMO) objects[1], index);
        return new Object[]{objects[0], comments, objects[1]};
    }

    private void postFetchComments(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;
        List<CommentMO> comments = (List<CommentMO>) array[1];

        if(getFragment(fragmentKey) instanceof CommentsFragment){
            CommentsFragment fragment = (CommentsFragment) getFragment(fragmentKey);

            if(comments != null && !comments.isEmpty()){
                fragment.updateComments(comments, index);
            }
            else{
                fragment.disableOnBottomListener();
            }
        }
        else if(getFragment(fragmentKey) instanceof RecipeViewFragment){
            Map<String, Object> params = new HashMap<>();
            params.put(SELECTED_ITEM, array[0]);
            params.put(GENERIC_OBJECT, comments);
            params.put(GENERIC_OBJECT2, array[2]);
            params.put(LOGGED_IN_USER, loggedInUser);
            Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_COMMENTS, new CommentsFragment(), params);
        }
        else if(getFragment(fragmentKey) instanceof UserViewFragment){
            Map<String, Object> params = new HashMap<>();
            params.put(SELECTED_ITEM, array[0]);
            params.put(GENERIC_OBJECT, comments);
            params.put(GENERIC_OBJECT2, array[2]);
            params.put(LOGGED_IN_USER, loggedInUser);
            Utility.showFragment(fragmentManager, FRAGMENT_USER_VIEW, FRAGMENT_COMMENTS, new CommentsFragment(), params);
        }
        else if(getFragment(fragmentKey) instanceof ProfileViewFragment){
            Map<String, Object> params = new HashMap<>();
            params.put(SELECTED_ITEM, array[0]);
            params.put(GENERIC_OBJECT, comments);
            params.put(GENERIC_OBJECT2, array[2]);
            params.put(LOGGED_IN_USER, loggedInUser);
            Utility.showFragment(fragmentManager, FRAGMENT_PROFILE_VIEW, FRAGMENT_COMMENTS, new CommentsFragment(), params);
        }
    }

    private Object fetchRecipe(Object[] objects) {
        waitFragment = Utility.showWaitDialog(fragmentManager, "fetching the Recipe ..");

        List<RecipeMO> recipes = (List<RecipeMO>) InternetUtility.fetchRecipe((RecipeMO) objects[0], loggedInUser.getUSER_ID());
        return recipes == null || recipes.isEmpty() ? null : recipes.get(0);
    }

    private void postFetchRecipe(Object object) {
        if (object == null) {
            return;
        }

        RecipeMO recipe = (RecipeMO) object;

        if(activity != null){
            if(activity instanceof HomeActivity){
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(SELECTED_ITEM, recipe);

                Utility.showFragment(fragmentManager, null, FRAGMENT_RECIPE, new RecipeViewFragment(), paramsMap);
            }
        }
        else if (FRAGMENT_RECIPE.equalsIgnoreCase(fragmentKey)) {
            RecipeViewFragment fragment = (RecipeViewFragment) getFragment(fragmentKey);
            fragment.setRecipe(recipe);
            fragment.setupPage();
        } else {
            Log.e(CLASS_NAME, "Something wrong !");
        }
    }

    private void postFetchUsers(Object object) {
        if (object == null) {
            return;
        }

        Object array[] = (Object[]) object;

        if(getFragment(fragmentKey) instanceof RecipeViewFragment){
            if ("LIKE".equalsIgnoreCase(String.valueOf(array[0]))) {
                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, array);
                params.put(SELECTED_ITEM, array[2]);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_USERS, new UsersFragment(), params);
            } else if ("VIEW".equalsIgnoreCase(String.valueOf(array[0]))) {
                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, array);
                params.put(SELECTED_ITEM, array[2]);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_USERS, new UsersFragment(), params);
            }
        }
        else if(getFragment(fragmentKey) instanceof ProfileViewFragment){
            if ("FOLLOWERS".equalsIgnoreCase(String.valueOf(array[0])) || "FOLLOWINGS".equalsIgnoreCase(String.valueOf(array[0]))) {
                Object temp[] = new Object[]{String.valueOf(array[0]), array[1]};
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(GENERIC_OBJECT, temp);
                params.put(SELECTED_ITEM, loggedInUser);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(fragmentManager, FRAGMENT_PROFILE_VIEW, FRAGMENT_USERS, new UsersFragment(), params);
            }
        }
        else if(getFragment(fragmentKey) instanceof UsersFragment){
            List<UserMO> users = (List<UserMO>) array[1];

            if(users != null && !users.isEmpty()){
                UsersFragment fragment = (UsersFragment) getFragment(fragmentKey);
                fragment.updateUsers((List<UserMO>) array[1], index);
            }
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_PARENT_FRAGMENT+fragmentKey);
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
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(GENERIC_OBJECT, users.get(0));
            paramsMap.put(LOGGED_IN_USER, loggedInUser);

            if(activity != null){
                if(activity instanceof HomeActivity){
                    Utility.showFragment(fragmentManager, null, FRAGMENT_USER_VIEW, new UserViewFragment(), paramsMap);
                }
            }
            else{
                if(getFragment(fragmentKey) instanceof UserViewFragment){
                    Utility.showFragment(fragmentManager, FRAGMENT_USER_VIEW, FRAGMENT_PROFILE_VIEW_IMAGE, new ProfileViewImageFragment(), paramsMap);
                }
                else if(getFragment(fragmentKey) instanceof RecipeViewFragment){
                    Utility.showFragment(fragmentManager, FRAGMENT_RECIPE, FRAGMENT_USER_VIEW, new UserViewFragment(), paramsMap);
                }
                else if(getFragment(fragmentKey) instanceof PeopleViewFragment){
                    Utility.showFragment(fragmentManager, FRAGMENT_PEOPLE_VIEW, FRAGMENT_USER_VIEW, new UserViewFragment(), paramsMap);
                }
                else if(getFragment(fragmentKey) instanceof UsersFragment){
                    Utility.showFragment(fragmentManager, FRAGMENT_USERS, FRAGMENT_USER_VIEW, new UserViewFragment(), paramsMap);
                }
            }


        } else {
            Log.e(CLASS_NAME, "Failed to fetch users details");
        }
    }

    private Object fetchUsers(Object[] objects) {
        if(objects != null && objects.length > 0){
            if ("FOLLOWERS".equalsIgnoreCase(String.valueOf(objects[0])) || "FOLLOWINGS".equalsIgnoreCase(String.valueOf(objects[0]))) {
                List<UserMO> users = null;

                if("FOLLOWERS".equalsIgnoreCase(String.valueOf(objects[0]))){
                    if(!(getFragment(fragmentKey) instanceof UsersFragment)){
                        waitFragment = Utility.showWaitDialog(fragmentManager, "fetching followers ..");
                    }

                    users = InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
                }
                else if("FOLLOWINGS".equalsIgnoreCase(String.valueOf(objects[0]))){
                    if(!(getFragment(fragmentKey) instanceof UsersFragment)){
                        waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users ..");
                    }

                    users = InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), index);
                }
                return new Object[]{String.valueOf(objects[0]), users};
            }
            else if ("LIKE".equalsIgnoreCase(String.valueOf(objects[0]))) {
                if(!(getFragment(fragmentKey) instanceof UsersFragment)){
                    waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users who liked the "+((LikesMO) objects[1]).getTYPE()+" ..");
                }

                List<UserMO> users = InternetUtility.fetchLikedUsers(loggedInUser.getUSER_ID(), ((LikesMO) objects[1]).getTYPE(), ((LikesMO) objects[1]).getTYPE_ID(), index);
                Object array[] = new Object[]{String.valueOf(objects[0]), users, objects[2]};

                return array;
            } else if ("VIEW".equalsIgnoreCase(String.valueOf(objects[0]))) {
                if(!(getFragment(fragmentKey) instanceof UsersFragment)){
                    waitFragment = Utility.showWaitDialog(fragmentManager, "fetching users who viewed the Recipe ..");
                }

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
    public AsyncTaskUtility(FragmentManager fragmentManager, Activity activity, Purpose purpose, UserMO loggedInUser, Integer index) {
        this.fragmentManager = fragmentManager;
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
