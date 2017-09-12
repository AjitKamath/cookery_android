package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.cookery.R;
import com.cookery.fragments.CommonImagePickerFragment;
import com.cookery.fragments.WaitFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.FRAGMENT_COMMON_WAIT;
import static com.cookery.utils.Constants.FRAGMENT_PICK_IMAGE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.HEADER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_CUISINES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_FOOD_TYPES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_QUANTITIES;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_TASTES;
import static com.cookery.utils.Constants.PHP_FETCH_INGREDIENTS;
import static com.cookery.utils.Constants.PHP_FETCH_SUBMIT_RECIPE;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.SERVER_CHARSET;
import static com.cookery.utils.Constants.SERVER_TIMEOUT;
import static com.cookery.utils.Constants.SLASH;
import static com.cookery.utils.Constants.USE_TEST_DATA;

public class Utility extends Activity {

    private static final String CLASS_NAME = Utility.class.getName();

    public static Bitmap drawableToBitmap(Drawable drawable){
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        return bitmapDrawable .getBitmap();
    }

    public static void pickPhotos(FragmentManager fragmentManager, String parentFragmentStr){
        String fragmentNameStr = FRAGMENT_PICK_IMAGE;
        String parentFragmentNameStr = parentFragmentStr;
        CommonImagePickerFragment fragment = new CommonImagePickerFragment();

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }

        fragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.fragment_theme);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(fragmentManager, fragmentNameStr);
    }

	public static void showSnacks(ViewGroup viewGroup, String messageStr, final String doWhatStr, int duration){
        Snackbar snackbar = Snackbar.make(viewGroup, messageStr, duration).setAction(doWhatStr, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OK
                if(OK.equalsIgnoreCase(doWhatStr)){

                }
                else{
                    Log.e(CLASS_NAME, "Could not identify the action of the snacks");
                }
            }
        });

        snackbar.show();
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

    public static HttpURLConnection getHttpConnection(URL url, String method) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-length", "0");
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(SERVER_TIMEOUT);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.connect();
        return urlConnection;
    }

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

    public static Object jsonToObject(String jsonStr, Class mappingClass){
        if(jsonStr == null || jsonStr.isEmpty()){
            Log.e(CLASS_NAME, "JSON is null");
            return null;
        }
        else if (mappingClass == null){
            Log.e(CLASS_NAME, "No mapping class has been passed to map json into object");
            return null;
        }

        try{
            Gson gson = new Gson();
            if(mappingClass.equals(FoodTypeMO.class)){
                List<FoodTypeMO> list = gson.fromJson(jsonStr, new TypeToken<List<FoodTypeMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(FoodTypeMO iteration : list){
                    iteration.setImage(getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(CuisineMO.class)){
                List<CuisineMO> list = gson.fromJson(jsonStr, new TypeToken<List<CuisineMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(CuisineMO iteration : list){
                    iteration.setImage(getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(IngredientMO.class)){
                List<IngredientMO> list = gson.fromJson(jsonStr, new TypeToken<List<IngredientMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(IngredientMO iteration : list){
                    iteration.setImage(getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(QuantityMO.class)){
                List<QuantityMO> list = gson.fromJson(jsonStr, new TypeToken<List<QuantityMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                /*for(QuantityMO iteration : list){
                    iteration.setImage(getImageFromUrl(iteration.getIMG()));
                }*/

                return list;
            }
            else if(mappingClass.equals(TasteMO.class)){
                List<TasteMO> list = gson.fromJson(jsonStr, new TypeToken<List<TasteMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(TasteMO iteration : list){
                    iteration.setImage(getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else{
                Log.e(CLASS_NAME, mappingClass+" is not identified for parsing JSON");
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Error in parsing the json("+jsonStr+") into the mapping class("+mappingClass+")");
        }

        return null;
    }

    public static Object fetchAllFoodTypes() {
        if(USE_TEST_DATA){
            return TestData.foodTypes;
        }

        try {
            String jsonStr = Utility.getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_ALL_FOOD_TYPES, null);
            return Utility.jsonToObject(jsonStr, FoodTypeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Food Types from the server : "+e);
        }

        return null;
    }

    public static Object fetchIngredients(String query) {
        if(USE_TEST_DATA){
            return TestData.ingredients;
        }

        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("text", query);

            String jsonStr = Utility.getResponseFromCookery(SERVER_ADDRESS+PHP_FETCH_INGREDIENTS, paramMap);
            return Utility.jsonToObject(jsonStr, IngredientMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Ingredients from the server : "+e);
        }

        return null;
    }

    public static Object fetchAllTastes() {
        if(USE_TEST_DATA){
            return TestData.tastes;
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

    public static String submitRecipe(RecipeMO recipe) {
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

            return multipart.finish(); // response from server.
        }
        catch(EOFException e){
            //shareBook(book);
        }
        catch(Exception e){
            Log.e(CLASS_NAME, e.getMessage());
        }

        return "";
    }

    public static Object fetchAllQuantities() {
        if(USE_TEST_DATA){
            return TestData.quantities;
        }

        try {
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+SLASH+PHP_FETCH_ALL_QUANTITIES, null);
            return Utility.jsonToObject(jsonStr, QuantityMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Quantities from the server : "+e);
        }

        return null;
    }
    
    public static Object fetchAllCuisines() {
        if(USE_TEST_DATA){
            return TestData.cuisines;
        }

        try {
            String jsonStr = getResponseFromCookery(SERVER_ADDRESS+SLASH+PHP_FETCH_ALL_CUISINES, null);
            return Utility.jsonToObject(jsonStr, CuisineMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Cuisines from the server : "+e);
        }

        return null;
    }

    public static Fragment showWaitDialog(FragmentManager fragManager, String message) {
        String fragmentNameStr = FRAGMENT_COMMON_WAIT;

        Fragment frag = fragManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragManager.beginTransaction().remove(frag).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, message);

        WaitFragment fragment = new WaitFragment();
        fragment.setArguments(bundle);

        fragment.show(fragManager, fragmentNameStr);

        return fragment;
    }

    public static String getResponseFromCookery(String url, Map<String, String> paramMap){
        try {
            MultipartUtility multipart = new MultipartUtility(url, SERVER_CHARSET);

            if(paramMap != null && !paramMap.isEmpty()){
                for(Map.Entry<String, String> iter : paramMap.entrySet()){
                    multipart.addFormField(iter.getKey(), iter.getValue());
                }
            }

            String response = multipart.finish();
            return  response;
        }
        catch(Exception e) {
            Log.e(CLASS_NAME, e.getMessage());
        }
        return null;
    }

    public static Bitmap getImageFromUrl(String url){
        if(url != null && !url.trim().isEmpty()){
            try {
                URL urlConnection = new URL(SERVER_ADDRESS+SLASH+url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }
            catch (Exception e){
                Log.e(CuisineMO.class.getName(), "Error !! "+e);
            }
        }

        return null;
    }
}
