package com.cookery.utils;

import android.util.Log;

import com.cookery.exceptions.CookeryException;
import com.cookery.models.CommentMO;
import com.cookery.models.FavouritesMO;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.API_KEY_ANDROID;
import static com.cookery.utils.Constants.API_KEY_IDENTIFIER;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_DELETE;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_FETCH_ALL;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_COMMENT_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FAV_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FETCH_MASTER_DATA;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_FETCH_RECIPE_IMAGES;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_INGREDIENT_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_LIKE_FETCH_USERS;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_LIKE_SUBMIT;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_FETCH;
import static com.cookery.utils.Constants.PHP_FUNCTION_KEY_MYLIST_VIEW;
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
import static com.cookery.utils.Constants.PHP_SERVICE_URL;
import static com.cookery.utils.Constants.SERVER_CHARSET;

/**
 * Created by ajit on 13/9/17.
 */

public class InternetUtility {
    private static final String CLASS_NAME = InternetUtility.class.getName();

    public static boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            Log.e(CLASS_NAME, "No internet connection");
        }
        return false;
    }

    public static Object fetchRecipe(RecipeMO recipe, int userId) throws CookeryException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_FETCH);
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
        paramMap.put("user_id", String.valueOf(userId));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeMO.class);
    }

    public static Object fetchFavRecipes(int user_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_FAVORITE_FETCH);
        paramMap.put("user_id", String.valueOf(user_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeMO.class);
    }

    public static Object fetchViewedRecipes(int user_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_USER_VIEWED_FETCH);
        paramMap.put("user_id", String.valueOf(user_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeMO.class);
    }

    public static Object fetchReviewedRecipes(int user_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_REVIEW_FETCH);
        paramMap.put("user_id", String.valueOf(user_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeMO.class);
    }

    public static Object fetchIngredients(String query) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_INGREDIENT_FETCH);
        paramMap.put("search_query", query);

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, IngredientAkaMO.class);
    }

    public static Object searchRecipes(int user_id, String query) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_SEARCH);
        paramMap.put("search_query", query);
        paramMap.put("user_id", String.valueOf(user_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeMO.class);
    }

    public static Object fetchUsersRecipeReview(UserMO loggedInUser, RecipeMO recipe) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_USER_FETCH);
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, ReviewMO.class);
    }

    public static Object submitRecipe(RecipeMO recipe) {
        MultipartUtility multipart = getMultipartUtility();

        //images
        //Note: image upload doesnt work if you do not add form field to multipart.
        //form field should be added to multipart only after file part
        for (int i = 0; i < recipe.getImages().size(); i++) {
            multipart.addFilePart("images[" + i + "]", new File(recipe.getImages().get(i).getRCP_IMG()));
        }

        multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_SUBMIT);

        multipart.addFormField("rcp_nm", recipe.getRCP_NAME());
        multipart.addFormField("user_id", String.valueOf(recipe.getUSER_ID()));
        multipart.addFormField("food_typ_id", String.valueOf(recipe.getFOOD_TYP_ID()));
        multipart.addFormField("food_csn_id", String.valueOf(recipe.getFOOD_CSN_ID()));

        //ingredients
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            multipart.addFormField("ing_aka_id[" + i + "]", String.valueOf(recipe.getIngredients().get(i).getING_AKA_ID()));
            multipart.addFormField("ing_aka_name[" + i + "]", String.valueOf(recipe.getIngredients().get(i).getING_AKA_NAME()));
            multipart.addFormField("ing_qty[" + i + "]", String.valueOf(recipe.getIngredients().get(i).getQTY()));
            multipart.addFormField("qty_id[" + i + "]", String.valueOf(recipe.getIngredients().get(i).getQuantity().getQTY_ID()));
        }

        /*steps*/
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            multipart.addFormField("rcp_steps[" + i + "]", recipe.getSteps().get(i));
        }
        /*steps*/

        //tastes
        for (int i = 0; i < recipe.getTastes().size(); i++) {
            multipart.addFormField("tst_id[" + i + "]", String.valueOf(recipe.getTastes().get(i).getTST_ID()));
            multipart.addFormField("tst_qty[" + i + "]", String.valueOf(recipe.getTastes().get(i).getQuantity()));
        }

        return Utility.jsonToObject(multipart.finish(), MessageMO.class);
    }

    public static Object viewMyList(int id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_VIEW);
        paramMap.put("list_id", Integer.toString(id));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MyListMO.class);
    }

    public static MessageMO saveList(MyListMO mylistObj) {
        MultipartUtility multipart = getMultipartUtility();

        multipart.addFormField("list_name", String.valueOf(mylistObj.getLIST_NAME()));
        multipart.addFormField("user_id", String.valueOf(mylistObj.getUSER_ID()));

        //ingredients
        for (int i = 0; i < mylistObj.getListofingredients().size(); i++) {
            multipart.addFormField("ing_aka_id[" + i + "]", String.valueOf(mylistObj.getListofingredients().get(i).getING_AKA_ID()));
            multipart.addFormField("ing_aka_name[" + i + "]", String.valueOf(mylistObj.getListofingredients().get(i).getING_AKA_NAME()));
        }

        return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);

    }

    public static MessageMO saveListFromRecipe(MyListMO mylistObj) {
        MultipartUtility multipart = getMultipartUtility();

        multipart.addFormField("list_id", String.valueOf(mylistObj.getLIST_ID()));
        multipart.addFormField("ing_aka_id", String.valueOf(mylistObj.getING_AKA_ID()));

        return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
    }


    public static MessageMO updateList(MyListMO mylistObj) {
        MultipartUtility multipart = getMultipartUtility();

        multipart.addFormField("list_id", String.valueOf(mylistObj.getLIST_ID()));
        multipart.addFormField("list_name", String.valueOf(mylistObj.getLIST_NAME()));
        multipart.addFormField("user_id", String.valueOf(mylistObj.getUSER_ID()));

        //ingredients
        for (int i = 0; i < mylistObj.getListofingredients().size(); i++) {
            multipart.addFormField("ing_aka_id[" + i + "]", String.valueOf(mylistObj.getListofingredients().get(i).getING_AKA_ID()));
            multipart.addFormField("ing_aka_name[" + i + "]", String.valueOf(mylistObj.getListofingredients().get(i).getING_AKA_NAME()));
        }

        return (MessageMO) Utility.jsonToObject(multipart.finish(), MessageMO.class);
    }


    public static Object submitComment(CommentMO comment) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_SUBMIT);
        paramMap.put("type", comment.getTYPE());
        paramMap.put("type_id", String.valueOf(comment.getTYPE_ID()));
        paramMap.put("user_id", String.valueOf(comment.getUSER_ID()));
        paramMap.put("comment", comment.getCOMMENT());

        return Utility.jsonToObject(getResponseFromCookery(paramMap), CommentMO.class);
    }

    public static Object submitRecipeReview(UserMO loggedInUser, RecipeMO recipe) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_SUBMIT);
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("review", recipe.getUserReview().getREVIEW());
        paramMap.put("rating", String.valueOf(recipe.getUserReview().getRATING()));

        return Utility.jsonToObject(getResponseFromCookery(paramMap), ReviewMO.class);
    }

    public static Object deleteRecipeReview(UserMO loggedInUser, ReviewMO review) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_DELETE);
        paramMap.put("rev_id", String.valueOf(review.getREV_ID()));
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));

        return Utility.jsonToObject(getResponseFromCookery(paramMap), MessageMO.class);
    }

    public static Object deleteComment(UserMO loggedInUser, CommentMO comment) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_DELETE);
        paramMap.put("com_id", String.valueOf(comment.getCOM_ID()));
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));

        return Utility.jsonToObject(getResponseFromCookery(paramMap), MessageMO.class);
    }

    public static LikesMO submitLike(LikesMO like) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_LIKE_SUBMIT);
        paramMap.put("type_id", String.valueOf(like.getTYPE_ID()));
        paramMap.put("user_id", String.valueOf(like.getUSER_ID()));
        paramMap.put("type", like.getTYPE());

        return (LikesMO) Utility.jsonToObject(getResponseFromCookery(paramMap), LikesMO.class);
    }

    public static ArrayList<FavouritesMO> submitFavourite(FavouritesMO fav) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FAV_SUBMIT);
        paramMap.put("user_id", String.valueOf(fav.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(fav.getRCP_ID()));

        return (ArrayList<FavouritesMO>) Utility.jsonToObject(getResponseFromCookery(paramMap), FavouritesMO.class);
    }

    public static List<CommentMO> fetchComments(UserMO loggedInUser, CommentMO comment, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_COMMENT_FETCH_ALL);
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("type", comment.getTYPE());
        paramMap.put("type_id", String.valueOf(comment.getTYPE_ID()));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<CommentMO>) Utility.jsonToObject(jsonStr, CommentMO.class);
    }

    public static List<UserMO> fetchLikedUsers(int loggedInUserId, String type, int type_id, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_LIKE_FETCH_USERS);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("index", String.valueOf(index));
        paramMap.put("type", type);
        paramMap.put("type_id", String.valueOf(type_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static List<UserMO> fetchViewedUsers(int loggedInUserId, RecipeMO recipe, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_VIEW_FETCH_USERS);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static List<ReviewMO> fetchRecipeReviews(UserMO loggedInUser, RecipeMO recipe, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_RECIPE);
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<ReviewMO>) Utility.jsonToObject(jsonStr, ReviewMO.class);
    }

    public static String getResponseFromCookery(Map<String, String> paramMap) throws CookeryException {
        MultipartUtility multipart = getMultipartUtility();

        if (paramMap != null && !paramMap.isEmpty()) {
            for (Map.Entry<String, String> iter : paramMap.entrySet()) {
                multipart.addFormField(iter.getKey(), iter.getValue());
            }
        }

        Date start = new Date();
        String response = multipart.finish();
        Date end = new Date();

        Log.i(CLASS_NAME, "*");
        Log.i(CLASS_NAME, "*** POST (" + (double) (end.getTime() - start.getTime()) / 1000 + " seconds)***");
        Log.i(CLASS_NAME, "URL : " + PHP_SERVICE_URL);
        Log.i(CLASS_NAME, "PARAMS : " + paramMap);
        Log.i(CLASS_NAME, "RESPONSE : " + response);
        Log.i(CLASS_NAME, "*** POST ***");
        Log.i(CLASS_NAME, "*");

        return response;
    }

    private static MultipartUtility getMultipartUtility(){
        MultipartUtility multipart = new MultipartUtility(PHP_SERVICE_URL, SERVER_CHARSET);

        //keep security related stuff in header only
        multipart.addHeaderField(API_KEY_IDENTIFIER, API_KEY_ANDROID + 1);

        return multipart;
    }

    public static List<RecipeMO> fetchMyRecipes(int user_id, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_RECIPE_USER_FETCH);
        paramMap.put("user_id", String.valueOf(user_id));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<RecipeMO>) Utility.jsonToObject(jsonStr, RecipeMO.class);

    }

    public static Object fetchMyReviews(int user_id, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_REVIEW_USER_FETCH_ALL);
        paramMap.put("user_id", String.valueOf(user_id));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, ReviewMO.class);

    }


    //public static List<MyListMO> fetchUserList(int userId, int index) {
    public static List<MyListMO> fetchUserList(int userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_MYLIST_FETCH);
        paramMap.put("user_id", String.valueOf(userId));
        //paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<MyListMO>) Utility.jsonToObject(jsonStr, MyListMO.class);

    }

    public static List<TimelineMO> getFetchUserTimeline(int userId, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_USER_FETCH);
        paramMap.put("user_id", String.valueOf(userId));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<TimelineMO>) Utility.jsonToObject(jsonStr, TimelineMO.class);

    }

    public static List<TimelineMO> getFetchUserStories(int userId, int index) throws CookeryException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_STORY_USER_FETCH);
        paramMap.put("user_id", String.valueOf(userId));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<TimelineMO>) Utility.jsonToObject(jsonStr, TimelineMO.class);
    }

    public static List<TrendMO> getFetchTrends(int user_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TREND_FETCH);
        paramMap.put("user_id", String.valueOf(user_id));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<TrendMO>) Utility.jsonToObject(jsonStr, TrendMO.class);

    }

    public static Object userCheckFirstTimeSocialLogin(String email, String name, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_REGISTER_FIRST_CHECK);
        paramMap.put("email", email);

        String jsonStr = getResponseFromCookery(paramMap);
        Object obj = Utility.jsonToObject(jsonStr, UserMO.class);
        if (null != obj) {
            ArrayList<UserMO> userobj = (ArrayList<UserMO>) obj;
            userobj.get(0).setEMAIL(email);
            userobj.get(0).setNAME(name);
            userobj.get(0).setPASSWORD(password);
            return userobj.get(0);
        }

        return null;
    }

    public static Object userRegistration(String name, String email, String password) {
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

    public static Object userLogin(String email, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_LOGIN);
        paramMap.put("email", email);
        paramMap.put("password", password);

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static Object fetchUser(int userId) throws CookeryException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FETCH_SELF);
        paramMap.put("user_id", String.valueOf(userId));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static Object updateUserName(UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_NAME);
        paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
        paramMap.put("name", String.valueOf(user.getNAME()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object updateUserEmail(UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_EMAIL);
        paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
        paramMap.put("email", String.valueOf(user.getEMAIL()));
        paramMap.put("scope_id", String.valueOf(user.getEMAIL_SCOPE_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object updateUserPassword(UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_PASSWORD);
        paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
        paramMap.put("password", String.valueOf(user.getPASSWORD()));
        paramMap.put("new_password", String.valueOf(user.getNewPassword()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object updateUserPhone(UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_PHONE);
        paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
        paramMap.put("mobile", String.valueOf(user.getMOBILE()));
        paramMap.put("scope_id", String.valueOf(user.getMOBILE_SCOPE_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object updateUserGender(UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_GENDER);
        paramMap.put("user_id", String.valueOf(user.getUSER_ID()));
        paramMap.put("gender", user.getGENDER());
        paramMap.put("scope_id", String.valueOf(user.getGENDER_SCOPE_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object updateUserImage(UserMO user) {
        MultipartUtility multipart = getMultipartUtility();

        //images
        //Note: image upload doesnt work if you do not add form field to multipart.
        //form field should be added to multipart only after file part
        multipart.addFilePart("image[0]", new File(user.getIMG()));
        multipart.addFormField(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_UPDATE_IMAGE);
        multipart.addFormField("user_id", String.valueOf(user.getUSER_ID()));

        String jsonStr = multipart.finish();
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static List<UserMO> fetchUsersPublicDetails(int userId, int loggedInUserId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FETCH_PUBLIC);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("user_id", String.valueOf(userId));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static Object submitUserFollow(UserMO loggedInUser, UserMO user) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOW_SUBMIT);
        paramMap.put("flwr_user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("flws_user_id", String.valueOf(user.getUSER_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static List<UserMO> fetchUserFollowers(int userId, int loggedInUserId, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOWERS_FETCH);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("user_id", String.valueOf(userId));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static List<UserMO> fetchUserFollowings(int userId, int loggedInUserId, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_FOLLOWINGS_FETCH);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("user_id", String.valueOf(userId));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return (List<UserMO>) Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static Object modifyTimelineScope(TimelineMO timeline) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_SCOPE_MODIFY);
        paramMap.put("tmln_id", String.valueOf(timeline.getTMLN_ID()));
        paramMap.put("scope_id", String.valueOf(timeline.getScopeId()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, TimelineMO.class);
    }

    public static Object deleteTimeline(TimelineMO timeline) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_TIMELINE_DELETE);
        paramMap.put("tmln_id", String.valueOf(timeline.getTMLN_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MessageMO.class);
    }

    public static Object searchUsers(String searchQuery, int loggedInUserId, int index) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_USER_SEARCH);
        paramMap.put("search_query", searchQuery);
        paramMap.put("logged_in_user_id", String.valueOf(loggedInUserId));
        paramMap.put("index", String.valueOf(index));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, UserMO.class);
    }

    public static Object fetchRecipeImages(UserMO loggedInUser, RecipeMO recipe) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FETCH_RECIPE_IMAGES);
        paramMap.put("user_id", String.valueOf(loggedInUser.getUSER_ID()));
        paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, RecipeImageMO.class);
    }

    public static Object fetchMasterData() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(PHP_FUNCTION_KEY, PHP_FUNCTION_KEY_FETCH_MASTER_DATA);

        String jsonStr = getResponseFromCookery(paramMap);
        return Utility.jsonToObject(jsonStr, MasterDataMO.class);
    }
}