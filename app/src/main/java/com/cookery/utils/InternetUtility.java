package com.cookery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.MessageMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
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
import static com.cookery.utils.Constants.PHP_FETCH_RECIPE;
import static com.cookery.utils.Constants.PHP_FETCH_SUBMIT_RECIPE;
import static com.cookery.utils.Constants.PHP_FETCH_TRENDING_RECIPES;
import static com.cookery.utils.Constants.PHP_USER_REGISTRATION;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.SERVER_CHARSET;
import static com.cookery.utils.Constants.USE_TEST_DATA;

/**
 * Created by ajit on 13/9/17.
 */

public class InternetUtility {
    private static final String CLASS_NAME = InternetUtility.class.getName();

    public static String getResponseFromCookery(HttpURLConnection connection) throws Exception {
        int status = connection.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return String.valueOf(sb);
        }

        return null;
    }

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

    public static Object fetchRecipe(RecipeMO recipe) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData().get(0);
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("rcp_id", String.valueOf(recipe.getRCP_ID()));

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_RECIPE, paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Recipe for rcp_id("+recipe.getRCP_ID()+") from the server : "+e);
        }

        return null;
    }

    public static Object fetchFavRecipes(String favRecipeType) {
        if(USE_TEST_DATA){
            return TestData.getRecipesTestData();
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("text", favRecipeType);

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_FAV_RECIPES, paramMap);
            return Utility.jsonToObject(jsonStr, RecipeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch fav recipes("+favRecipeType+") from the server : "+e);
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

    public static Object submitRecipe(RecipeMO recipe) {
        MessageMO message = new MessageMO();
        try {
            MultipartUtility multipart = new MultipartUtility(SERVER_ADDRESS+PHP_FETCH_SUBMIT_RECIPE, SERVER_CHARSET);

            //images
            //Note: image upload doesnt work if you do not add form field to multipart.
            //form field should be added to multipart only after file part
            for(int i=0; i<recipe.getImages().size(); i++){
                multipart.addFilePart("images["+i+"]", new File(recipe.getImages().get(i)));
            }

            multipart.addFormField("rcp_nm", recipe.getRCP_NAME());
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

    public static Bitmap getImageFromUrl(String url){
        if(url != null && !url.trim().isEmpty()){
            try {
                URL urlConnection = new URL(SERVER_ADDRESS+url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }
            catch (Exception e){
                Log.e(CLASS_NAME, "Error !!  "+e);
            }
        }

        return null;
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
            Log.e(CLASS_NAME, "Could not fetch Cuisines from the server : "+e);
        }

        return null;
    }


    public static Object userRegistraion(String name,String email, String mobile, String password, String gender)
    {
        String flag="";

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("code", "1");
            paramMap.put("name", name);
            paramMap.put("email", email);
            paramMap.put("mobile", mobile);
            paramMap.put("password", password);
            paramMap.put("gender", gender);

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_USER_REGISTRATION, paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not register the user : "+e);
        }
        return null;
    }

    public static Object userLogin(String email, String password)
    {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("code", "4");
            paramMap.put("email", email);
            paramMap.put("password", password);

            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+PHP_USER_REGISTRATION, paramMap);
            return Utility.jsonToObject(jsonStr, MessageMO.class);

        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not register the user : "+e);
        }

        return null;
    }

}
