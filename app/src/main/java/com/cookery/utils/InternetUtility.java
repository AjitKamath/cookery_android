package com.cookery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cookery.models.CommentMO;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MessageMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TasteMO;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.PHP_FETCH_ALL_CUISINES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_FOOD_TYPES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_QUANTITIES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_TASTES;
import static com.cookery.utils.Constants.PHP_FETCH_FAV_RECIPES;
import static com.cookery.utils.Constants.PHP_FETCH_INGREDIENTS;
import static com.cookery.utils.Constants.PHP_FETCH_MASTER_SEARCH;
import static com.cookery.utils.Constants.PHP_FETCH_MY_RECIPES;
import static com.cookery.utils.Constants.PHP_FETCH_MY_REVIEWS;
import static com.cookery.utils.Constants.PHP_FETCH_RECIPE;
import static com.cookery.utils.Constants.PHP_FETCH_RECIPE_REVIEW;
import static com.cookery.utils.Constants.PHP_FETCH_REVIEWED_RECIPES;
import static com.cookery.utils.Constants.PHP_FETCH_TRENDING_RECIPES;
import static com.cookery.utils.Constants.PHP_FETCH_VIEWED_RECIPES;
import static com.cookery.utils.Constants.PHP_SUBMIT_LIKE;
import static com.cookery.utils.Constants.PHP_SUBMIT_RECIPE;
import static com.cookery.utils.Constants.PHP_SUBMIT_RECIPE_COMMENT;
import static com.cookery.utils.Constants.PHP_SUBMIT_RECIPE_REVIEW;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.SERVER_CHARSET;
import static com.cookery.utils.Constants.USE_TEST_DATA;

/**
 * Created by ajit on 13/9/17.
 */

public class InternetUtility {
    private static final String CLASS_NAME = InternetUtility.class.getName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!hasInternet){
            Log.e(CLASS_NAME, "No Internet connection available");
        }
        else{
            Log.i(CLASS_NAME, "Internet connection is available");
        }

        return hasInternet;
    }

    public static Object fetchRecipe(RecipeMO recipe, int userId) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
            paramMap.put("user_id", String.valueOf(userId));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_RECIPE, paramMap);
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
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_FAV_RECIPES, paramMap);
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
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_VIEWED_RECIPES, paramMap);
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
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_REVIEWED_RECIPES, paramMap);
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
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_ALL_FOOD_TYPES, null);
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
            paramMap.put("text", query);

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_INGREDIENTS, paramMap);
            return Utility.jsonToObject(jsonStr, IngredientMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Ingredients from the server : "+e);
        }

        return null;
    }

    public static Object fetchMasterSearch(String query) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("text", query);

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_MASTER_SEARCH, paramMap);
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
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_ALL_TASTES, null);
            return Utility.jsonToObject(jsonStr, TasteMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Tastes from the server : "+e);
        }

        return null;
    }

    public static Object fetchUsersRecipeReview(ReviewMO review) {
        if(USE_TEST_DATA){
            return TestData.getReviewsTestData().get(0);
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("user_id", String.valueOf(review.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(review.getRCP_ID()));

        try {
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_RECIPE_REVIEW, paramMap);
            return Utility.jsonToObject(jsonStr, ReviewMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Users Recipe Review from the server : "+e);
        }

        return null;
    }

    public static Object submitRecipe(RecipeMO recipe) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS+PHP_SUBMIT_RECIPE, SERVER_CHARSET);

            //images
            //Note: image upload doesnt work if you do not add form field to multipart.
            //form field should be added to multipart only after file part
            for(int i=0; i<recipe.getRCP_IMGS().size(); i++){
                multipart.addFilePart("images["+i+"]", new File(recipe.getRCP_IMGS().get(i)));
            }

            multipart.addFormField("rcp_nm", recipe.getRCP_NAME());
            multipart.addFormField("user_id", String.valueOf(recipe.getUSER_ID()));
            multipart.addFormField("food_typ_id", String.valueOf(recipe.getFOOD_TYP_ID()));
            multipart.addFormField("food_csn_nm", String.valueOf(recipe.getFOOD_CSN_ID()));

            //ingredients
            for(int i =0; i<recipe.getIngredients().size(); i++){
                multipart.addFormField("ing_id["+i+"]", String.valueOf(recipe.getIngredients().get(i).getING_ID()));
                multipart.addFormField("ing_nm["+i+"]", String.valueOf(recipe.getIngredients().get(i).getING_NAME()));
                multipart.addFormField("ing_qty["+i+"]", String.valueOf(recipe.getIngredients().get(i).getQTY()));
                multipart.addFormField("qty_id["+i+"]", String.valueOf(recipe.getIngredients().get(i).getQuantity().getQTY_ID()));
            }

            multipart.addFormField("rcp_proc", recipe.getRCP_PROC());
            multipart.addFormField("rcp_plating", recipe.getRCP_PLATING());
            multipart.addFormField("rcp_note", recipe.getRCP_NOTE());

            //tastes
            for(int i=0; i<recipe.getTastes().size(); i++){
                multipart.addFormField("tst_id["+i+"]", String.valueOf(recipe.getTastes().get(i).getTST_ID()));
                multipart.addFormField("tst_qty["+i+"]", String.valueOf(recipe.getTastes().get(i).getQuantity()));
            }

            message.setErr_message(multipart.finish());
            message.setError(false);
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
            paramMap.put("rcp_id", String.valueOf(comment.getRCP_ID()));
            paramMap.put("user_id", String.valueOf(comment.getUSER_ID()));
            paramMap.put("comment", comment.getCOMMENT());

            message.setErr_message(getResponseFromCookery(SERVER_ADDRESS+PHP_SUBMIT_RECIPE_COMMENT, paramMap));
            message.setError(false);
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

    public static ReviewMO submitRecipeReview(ReviewMO review) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("rcp_id", String.valueOf(review.getRCP_ID()));
            paramMap.put("user_id", String.valueOf(review.getUSER_ID()));
            paramMap.put("review", review.getREVIEW());
            paramMap.put("rating", String.valueOf(review.getRATING()));

            return  (ReviewMO) Utility.jsonToObject(getResponseFromCookery(SERVER_ADDRESS+PHP_SUBMIT_RECIPE_REVIEW, paramMap), ReviewMO.class);
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
            paramMap.put("type_id", String.valueOf(like.getTYPE_ID()));
            paramMap.put("user_id", String.valueOf(like.getUSER_ID()));
            paramMap.put("type", like.getTYPE());

            return (LikesMO) Utility.jsonToObject(getResponseFromCookery(SERVER_ADDRESS+PHP_SUBMIT_LIKE, paramMap), LikesMO.class);
        }
        catch(SocketException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static Object fetchAllQuantities() {
        if(USE_TEST_DATA){
            return TestData.getQuantitiesTestDate();
        }

        try {
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_ALL_QUANTITIES, null);
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
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_ALL_CUISINES, null);
            return Utility.jsonToObject(jsonStr, CuisineMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Cuisines from the server : "+e);
        }

        return null;
    }

    public static String getResponseFromCookery(String url, Map<String, String> paramMap) throws Exception{
        try {
            MultipartUtility multipart = new MultipartUtility(url, SERVER_CHARSET);

            if(paramMap != null && !paramMap.isEmpty()){
                for(Map.Entry<String, String> iter : paramMap.entrySet()){
                    multipart.addFormField(iter.getKey(), iter.getValue());
                }
            }

            String response = multipart.finish();

            Log.i(CLASS_NAME, "*");
            Log.i(CLASS_NAME, "*** GET ***");
            Log.i(CLASS_NAME, "URL : "+url);
            Log.i(CLASS_NAME, "PARAMS : "+paramMap);
            Log.i(CLASS_NAME, "RESPONSE : "+response);
            Log.i(CLASS_NAME, "*** GET ***");
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

    public static List<RecipeMO> fetchTrendingRecipes() {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_TRENDING_RECIPES, null);
            return (List<RecipeMO>) Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch trending recipes from the server : "+e);
        }

        return null;
    }

    public static List<RecipeMO> fetchMyRecipes(int user_id) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_MY_RECIPES, paramMap);
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

    public static List<RecipeMO> fetchMyReviews(int user_id) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", String.valueOf(user_id));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_MY_REVIEWS, paramMap);
            return (List<RecipeMO>) Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (IOException e){
            Log.e(CLASS_NAME, e.getMessage());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Cuisines from the server : "+e);
        }

        return null;
    }
}
