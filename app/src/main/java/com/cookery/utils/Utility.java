package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.FRAGMENT_COMMON_WAIT;
import static com.cookery.utils.Constants.FRAGMENT_PICK_IMAGE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SERVER_TIMEOUT;

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
                    iteration.setImage(InternetUtility.getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(CuisineMO.class)){
                List<CuisineMO> list = gson.fromJson(jsonStr, new TypeToken<List<CuisineMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(CuisineMO iteration : list){
                    iteration.setImage(InternetUtility.getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(IngredientMO.class)){
                List<IngredientMO> list = gson.fromJson(jsonStr, new TypeToken<List<IngredientMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(IngredientMO iteration : list){
                    iteration.setImage(InternetUtility.getImageFromUrl(iteration.getIMG()));
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
                    iteration.setImage(InternetUtility.getImageFromUrl(iteration.getIMG()));
                }

                return list;
            }
            else if(mappingClass.equals(RecipeMO.class)){
                List<RecipeMO> list = gson.fromJson(jsonStr, new TypeToken<List<RecipeMO>>(){}.getType());

                //TODO:this must be replaced when GSON supports calling getters/setters
                for(RecipeMO iteration : list){
                    List<Bitmap> imagesList = new ArrayList<>();

                    for(String iter : iteration.getRCP_IMGS()){
                        imagesList.add(InternetUtility.getImageFromUrl(iter));
                    }

                    iteration.setImagesList(imagesList);
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
}
