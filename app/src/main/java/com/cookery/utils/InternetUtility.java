package com.cookery.utils;

import android.util.Log;

import com.cookery.models.CommentMO;
import com.cookery.models.CuisineMO;
import com.cookery.models.FavouritesMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TasteMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.API_KEY_ANDROID;
import static com.cookery.utils.Constants.API_KEY_IDENTIFIER;
import static com.cookery.utils.Constants.PHP_CONTROLLER;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_DELETE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FAV_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FOOD_CUISINE_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FOOD_TYPE_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_INGREDIENT_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_LIKE_FETCH_USERS;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_LIKE_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_SUBMIT_FROM_RECIPE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_UPDATE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_VIEW;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_QUANTITY_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_FAVORITE_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_REVIEW_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_SEARCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_USER_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_RECIPE_USER_VIEWED_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_REVIEW_DELETE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_REVIEW_RECIPE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_REVIEW_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_REVIEW_USER_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_REVIEW_USER_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_STORY_USER_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_TASTE_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_TIMELINE_DELETE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_TIMELINE_SCOPE_MODIFY;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_TIMELINE_USER_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_TREND_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_FETCH_PUBLIC;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_FETCH_SELF;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_FOLLOWERS_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_FOLLOWINGS_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_FOLLOW_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_LOGIN;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_REGISTER;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_REGISTER_FIRST_CHECK;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_SEARCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_EMAIL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_GENDER;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_IMAGE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_NAME;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_PASSWORD;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_USER_UPDATE_PHONE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_VIEW_FETCH_USERS;
import static com.cookery.utils.Constants.SERVER_ADDRESS_PUBLIC;
import static com.cookery.utils.Constants.SERVER_CHARSET;
import static com.cookery.utils.Constants.SUCCESS;
import static com.cookery.utils.Constants.USE_TEST_DATA;

/**
 * Created by ajit on 13/9/17.
 */

public class InternetUtility {
    private static final String CLASS_NAME = InternetUtility.class.getName();

    public static Object fetchRecipe(RecipeMO recipe, int userId) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_FETCH);
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
            paramMap.put("user_id", String.valueOf(userId));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Recipe for rcp_id("+recipe.getRCP_ID()+") from the server : "+e);
        }

        return null;
    }

    public static Object fetchFavRecipes(int user_id) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_FAVORITE_FETCH);
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch fav recipes from the server : "+e);
        }

        return null;
    }

    public static Object fetchViewedRecipes(int user_id) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_USER_VIEWED_FETCH);
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch viewed recipes from the server : "+e);
        }

        return null;
    }

    public static Object fetchReviewedRecipes(int user_id) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_REVIEW_FETCH);
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch reviewed recipes from the server : "+e);
        }

        return null;
    }

    public static Object fetchAllFoodTypes() {
        if(USE_TEST_DATA){
            return TestData.getFoodTypesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FOOD_TYPE_FETCH_ALL);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, FoodTypeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Food Types from the server : "+e);
        }

        return null;
    }

    public static Object fetchIngredients(String query) {
        if(USE_TEST_DATA){
            return TestData.getIngredientsTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_INGREDIENT_FETCH);
            paramMap.put("search_query", query);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, IngredientMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Ingredients from the server : "+e);
        }

        return null;
    }

    public static Object searchRecipes(int user_id, String query) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_SEARCH);
            paramMap.put("search_query", query);
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Recipes from the server : "+e);
        }

        return null;
    }

    public static Object fetchAllTastes() {
        if(USE_TEST_DATA){
            return TestData.getTastesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TASTE_FETCH_ALL);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, TasteMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Tastes from the server : "+e);
        }

        return null;
    }

    public static ReviewMO fetchUsersRecipeReview(UserMO loggedInUser, RecipeMO recipe) {
        if(USE_TEST_DATA){
            return TestData.getReviewsTestData().get(0);
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_USER_FETCH);
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));

        try {
            String jsonStr = getResponseFromCookery(paramMap);
            List<ReviewMO> reviews = (List<ReviewMO>) Utility.jsonToObject(jsonStr, ReviewMO.class);
            if(reviews != null && !reviews.isEmpty()){
                return reviews.get(0);
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Users Recipe Review from the server : "+e);
        }

        return null;
    }

    public static Object submitRecipe(RecipeMO recipe) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER, SERVER_CHARSET);

            //images
            //Note: image upload doesnt work if you do not add form field to multipart.
            //form field should be added to multipart only after file part
            for(int i=0; i<recipe.getImages().size(); i++){
                multipart.addFilePart("images["+i+"]", new File(recipe.getImages().get(i).getRCP_IMG()));
            }

            multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_SUBMIT);

            multipart.addFormField("rcp_nm", recipe.getRCP_NAME());
            multipart.addFormField("user_id", String.valueOf(recipe.getUSER_ID()));
            multipart.addFormField("food_typ_id", String.valueOf(recipe.getFOOD_TYP_ID()));
            multipart.addFormField("food_csn_id", String.valueOf(recipe.getFOOD_CSN_ID()));

            //ingredients
            for(int i =0; i<recipe.getIngredients().size(); i++){
                multipart.addFormField("ing_id["+i+"]", String.valueOf(recipe.getIngredients().get(i).getING_ID()));
                multipart.addFormField("ing_nm["+i+"]", String.valueOf(recipe.getIngredients().get(i).getING_NAME()));
                multipart.addFormField("ing_qty["+i+"]", String.valueOf(recipe.getIngredients().get(i).getQTY()));
                multipart.addFormField("qty_id["+i+"]", String.valueOf(recipe.getIngredients().get(i).getQuantity().getQTY_ID()));
            }

            /*steps*/
            for(int i =0; i<recipe.getSteps().size(); i++){
                multipart.addFormField("rcp_steps["+i+"]", recipe.getSteps().get(i));
            }
            /*steps*/

            //tastes
            for(int i=0; i<recipe.getTastes().size(); i++){
                multipart.addFormField("tst_id["+i+"]", String.valueOf(recipe.getTastes().get(i).getTST_ID()));
                multipart.addFormField("tst_qty["+i+"]", String.valueOf(recipe.getTastes().get(i).getQuantity()));
            }

            return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }

    public static Object viewMyList(int id) {
        MessageMO message = new MessageMO();
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_VIEW);
            paramMap.put("list_id", Integer.toString(id));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MyListMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }

    public static MessageMO saveList(MyListMO mylistObj) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER, SERVER_CHARSET);
            multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_SUBMIT);

            multipart.addFormField("list_name", String.valueOf(mylistObj.getLIST_NAME()));
            multipart.addFormField("user_id", String.valueOf(mylistObj.getUSER_ID()));

            //ingredients
            for(int i =0; i<mylistObj.getListofingredients().size(); i++){
                multipart.addFormField("ing_id["+i+"]", String.valueOf(mylistObj.getListofingredients().get(i).getING_ID()));
                multipart.addFormField("ing_nm["+i+"]", String.valueOf(mylistObj.getListofingredients().get(i).getING_NAME()));
            }

            return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }

    public static MessageMO saveListFromRecipe(MyListMO mylistObj) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER, SERVER_CHARSET);
            multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_SUBMIT_FROM_RECIPE);

            multipart.addFormField("list_id", String.valueOf(mylistObj.getLIST_ID()));
            multipart.addFormField("ing_id", String.valueOf(mylistObj.getING_ID()));

            return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }


    public static MessageMO updateList(MyListMO mylistObj) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER, SERVER_CHARSET);
            multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_UPDATE);

            multipart.addFormField("list_id", String.valueOf(mylistObj.getLIST_ID()));
            multipart.addFormField("list_name", String.valueOf(mylistObj.getLIST_NAME()));
            multipart.addFormField("user_id", String.valueOf(mylistObj.getUSER_ID()));

            //ingredients
            for(int i =0; i<mylistObj.getListofingredients().size(); i++){
                multipart.addFormField("ing_id["+i+"]", String.valueOf(mylistObj.getListofingredients().get(i).getING_ID()));
                multipart.addFormField("ing_nm["+i+"]", String.valueOf(mylistObj.getListofingredients().get(i).getING_NAME()));
            }

            return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }


    public static MessageMO submitRecipeComment(CommentMO comment) {
        MessageMO message = new MessageMO();
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_SUBMIT);
            paramMap.put("type", comment.getTYPE());
            paramMap.put("type_id", String.valueOf(comment.getTYPE_ID()));
            paramMap.put("user_id", String.valueOf(comment.getUSER_ID()));
            paramMap.put("comment", comment.getCOMMENT());

            String response = getResponseFromCookery(paramMap);

            if(SUCCESS.equalsIgnoreCase(response)){
                message.setError(false);
            }
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Check your internet");
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());

            message.setError(true);
            message.setErr_message("Something went wrong");
        }

        return message;
    }

    public static Object submitRecipeReview(UserMO loggedInUser, RecipeMO recipe) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_SUBMIT);
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
            paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
            paramMap.put("review", recipe.getUserReview().getREVIEW());
            paramMap.put("rating", String.valueOf(recipe.getUserReview().getRATING()));

            return  Utility.jsonToObject(getResponseFromCookery(paramMap), ReviewMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static Object deleteRecipeReview(UserMO loggedInUser, ReviewMO review) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_DELETE);
            paramMap.put("rev_id", String.valueOf(review.getREV_ID()));
            paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));

            return  Utility.jsonToObject(getResponseFromCookery(paramMap), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static Object deleteRecipeComment(UserMO loggedInUser, CommentMO comment) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_DELETE);
            paramMap.put("com_id", String.valueOf(comment.getCOM_ID()));
            paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));

            return  Utility.jsonToObject(getResponseFromCookery(paramMap), MessageMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static LikesMO submitLike(LikesMO like) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_LIKE_SUBMIT);
            paramMap.put("type_id", String.valueOf(like.getTYPE_ID()));
            paramMap.put("user_id", String.valueOf(like.getUSER_ID()));
            paramMap.put("type", like.getTYPE());

            return (LikesMO) Utility.jsonToObject(getResponseFromCookery(paramMap), LikesMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static ArrayList<FavouritesMO> submitFavourite(FavouritesMO fav) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FAV_SUBMIT);
            paramMap.put("user_id", String.valueOf(fav.getUSER_ID()));
            paramMap.put("rcp_id", String.valueOf(fav.getRCP_ID()));

            return (ArrayList<FavouritesMO>) Utility.jsonToObject(getResponseFromCookery(paramMap), FavouritesMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static List<CommentMO> fetchComments(UserMO loggedInUser, CommentMO comment, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_FETCH_ALL);
            paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
            paramMap.put("type", comment.getTYPE());
            paramMap.put("type_id", String.valueOf(comment.getTYPE_ID()));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<CommentMO>) Utility.jsonToObject(jsonStr, CommentMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch recipe comments from the server : "+e);
        }

        return null;
    }

    public static List<UserMO> fetchLikedUsers(String type, int type_id, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_LIKE_FETCH_USERS);
            paramMap.put("index", String.valueOf(index));
            paramMap.put("type", type);
            paramMap.put("type_id", String.valueOf(type_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user who liked recipe from the server : "+e);
        }

        return null;
    }

    public static List<UserMO> fetchViewedUsers(RecipeMO recipe, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_VIEW_FETCH_USERS);
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user who viewed recipe from the server : "+e);
        }

        return null;
    }

    public static List<ReviewMO> fetchRecipeReviews(UserMO loggedInUser, RecipeMO recipe, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_RECIPE);
            paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<ReviewMO>) Utility.jsonToObject(jsonStr, ReviewMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch recipe comments from the server : "+e);
        }

        return null;
    }

    public static Object fetchAllQuantities() {
        if(USE_TEST_DATA){
            return TestData.getQuantitiesTestDate();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_QUANTITY_FETCH_ALL);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, QuantityMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Quantities from the server : "+e);
        }

        return null;
    }

    public static Object fetchAllCuisines() {
        if(USE_TEST_DATA){
            return TestData.getCuisinesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FOOD_CUISINE_FETCH_ALL);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, CuisineMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Cuisines from the server : "+e);
        }

        return null;
    }

    public static String getResponseFromCookery(Map<String, String> paramMap) throws Exception{
        try {
            final String url = SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER;

            MultipartUtility multipart = new MultipartUtility(url, SERVER_CHARSET);

            //keep security related stuff in header only
            multipart.addHeaderField(API_KEY_IDENTIFIER, API_KEY_ANDROID+1);

            if(paramMap != null && !paramMap.isEmpty()){
                for(Map.Entry<String, String> iter : paramMap.entrySet()){
                    multipart.addFormField(iter.getKey(), iter.getValue());
                }
            }

            Date start = new Date();
            String response = multipart.finish();
            Date end = new Date();

            Log.i(CLASS_NAME, "*");
            Log.i(CLASS_NAME, "*** POST ("+(double)(end.getTime() - start.getTime())/1000+" seconds)***");
            Log.i(CLASS_NAME, "URL : "+url);
            Log.i(CLASS_NAME, "PARAMS : "+paramMap);
            Log.i(CLASS_NAME, "RESPONSE : "+response);
            Log.i(CLASS_NAME, "*** POST ***");
            Log.i(CLASS_NAME, "*");

            return  response;
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());

            throw new IOException(e);
        }
        catch(Exception e) {
            Log.e(CLASS_NAME, e.getMessage());

            throw new Exception(e);
        }
    }

    public static List<RecipeMO> fetchMyRecipes(int user_id, int index) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_USER_FETCH);
            paramMap.put("user_id", String.valueOf(user_id));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<RecipeMO>) Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch my recipes from the server : "+e);
        }

        return null;
    }

    public static Object fetchMyReviews(int user_id, int index) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_USER_FETCH_ALL);
            paramMap.put("user_id", String.valueOf(user_id));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, ReviewMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch users reviews from the server : "+e);
        }

        return null;
    }


    //public static List<MyListMO> fetchUserList(int userId, int index) {
    public static List<MyListMO> fetchUserList (int userId) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_FETCH);
            paramMap.put("user_id", String.valueOf(userId));
            //paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<MyListMO>) Utility.jsonToObject(jsonStr, MyListMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user timeline from the server : "+e);
        }

        return null;
    }

    public static List<TimelineMO> getFetchUserTimeline(int userId, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_USER_FETCH);
            paramMap.put("user_id", String.valueOf(userId));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<TimelineMO>) Utility.jsonToObject(jsonStr, TimelineMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user timeline from the server : "+e);
        }

        return null;
    }

    public static List<TimelineMO> getFetchUserStories(int userId, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_STORY_USER_FETCH);
            paramMap.put("user_id", String.valueOf(userId));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<TimelineMO>) Utility.jsonToObject(jsonStr, TimelineMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user stories from the server : "+e);
        }

        return null;
    }

    public static List<TrendMO> getFetchTrends(int user_id) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TREND_FETCH);
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<TrendMO>) Utility.jsonToObject(jsonStr, TrendMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch trends from the server : "+e);
        }

        return null;
    }

    public static Object userCheckFirstTimeSocialLogin(String email,String name, String password){
        String flag="";

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_REGISTER_FIRST_CHECK);
            paramMap.put("email", email);

            String jsonStr = getResponseFromCookery(paramMap);
            Object obj = Utility.jsonToObject(jsonStr, UserMO.class);
            if(null != obj){
            ArrayList<UserMO> userobj = (ArrayList<UserMO>) obj;
            userobj.get(0).setEMAIL(email);
            userobj.get(0).setNAME(name);
            userobj.get(0).setPASSWORD(password);
            return userobj.get(0);
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not found the user : "+e);
        }
        return null;
    }

    public static Object userRegistration(String name,String email, String password)
    {
        String flag="";

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_REGISTER);
           // paramMap.put("code", "");
            paramMap.put("name", name);
            paramMap.put("email", email);
           // paramMap.put("mobile", mobile);
            paramMap.put("password", password);
            //paramMap.put("gender", gender);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not register the user : "+e);
        }
        return null;
    }

    public static Object userLogin(String email, String password){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_LOGIN);
            paramMap.put("email", email);
            paramMap.put("password", password);

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, UserMO.class);

        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not login the user : "+e);
        }

        return null;
    }

    public static Object fetchUser(int userId){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FETCH_SELF);
            paramMap.put("user_id", String.valueOf(userId));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch the user : "+e);
        }

        return null;
    }

    public static Object updateUserName(UserMO user){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_NAME);
            paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
            paramMap.put("name", String.valueOf(user.getNAME()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the name of the user : "+e);
        }

        return null;
    }

    public static Object updateUserEmail(UserMO user){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_EMAIL);
            paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
            paramMap.put("email", String.valueOf(user.getEMAIL()));
            paramMap.put("scope_id", String.valueOf(user.getEMAIL_SCOPE_ID()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the email of the user : "+e);
        }

        return null;
    }

    public static Object updateUserPassword(UserMO user){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_PASSWORD);
            paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
            paramMap.put("password", String.valueOf(user.getPASSWORD()));
            paramMap.put("new_password", String.valueOf(user.getNewPassword()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the email of the user : "+e);
        }

        return null;
    }

    public static Object updateUserPhone(UserMO user){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_PHONE);
            paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
            paramMap.put("mobile", String.valueOf(user.getMOBILE()));
            paramMap.put("scope_id", String.valueOf(user.getMOBILE_SCOPE_ID()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the phone number of the user : "+e);
        }

        return null;
    }

    public static Object updateUserGender(UserMO user){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_GENDER);
            paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
            paramMap.put("gender", user.getGENDER());
            paramMap.put("scope_id", String.valueOf(user.getGENDER_SCOPE_ID()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the gender of the user : "+e);
        }

        return null;
    }

    public static Object updateUserImage(UserMO user) {
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS_PUBLIC+PHP_CONTROLLER, SERVER_CHARSET);

            //images
            //Note: image upload doesnt work if you do not add form field to multipart.
            //form field should be added to multipart only after file part
            multipart.addFilePart("image[0]", new File(user.getIMG()));
            multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_IMAGE);
            multipart.addFormField("user_id", String.valueOf(user.getUSER_ID()));

            String jsonStr = multipart.finish();
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the image of the user : "+e);
        }

        return null;
    }

    public static List<UserMO> fetchUsersPublicDetails(int userId, int loggedInUserId) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FETCH_PUBLIC);
            paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
            paramMap.put("user_id", String.valueOf(userId));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user details from the server : "+e);
        }

        return null;
    }

    public static Object submitUserFollow(UserMO loggedInUser, UserMO user) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOW_SUBMIT);
            paramMap.put("flwr_user_id", String.valueOf(loggedInUser.getUSER_ID()));
            paramMap.put("flws_user_id", String.valueOf(user.getUSER_ID()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not update the image of the user : "+e);
        }

        return null;
    }

    public static List<UserMO> fetchUserFollowers(int userId, int loggedInUserId, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOWERS_FETCH);
            paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
            paramMap.put("user_id", String.valueOf(userId));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user followers from the server : "+e);
        }

        return null;
    }

    public static List<UserMO> fetchUserFollowings(int userId, int loggedInUserId, int index) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOWINGS_FETCH);
            paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
            paramMap.put("user_id", String.valueOf(userId));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch user followers from the server : "+e);
        }

        return null;
    }

    public static Object modifyTimelineScope(TimelineMO timeline) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_SCOPE_MODIFY);
            paramMap.put("tmln_id", String.valueOf(timeline.getTMLN_ID()));
            paramMap.put("scope_id", String.valueOf(timeline.getScopeId()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, TimelineMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not modify timeline scope : "+e);
        }

        return null;
    }

    public static Object deleteTimeline(TimelineMO timeline) {
        if(USE_TEST_DATA){
            return null;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_DELETE);
            paramMap.put("tmln_id", String.valueOf(timeline.getTMLN_ID()));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not delete the timeline : "+e);
        }

        return null;
    }

    public static Object searchUsers(String searchQuery, int loggedInUserId, int index){
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_SEARCH);
            paramMap.put("search_query", searchQuery);
            paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
            paramMap.put("index", String.valueOf(index));

            String jsonStr = getResponseFromCookery(paramMap);
            return Utility.jsonToObject(jsonStr, UserMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could search users : "+e);
        }

        return null;
    }
}
