package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.fragments.AddRecipeFragment;
import com.cookery.fragments.CommonImagePickerFragment;
import com.cookery.fragments.MessageFragment;
import com.cookery.fragments.MyTimelinesFragment;
import com.cookery.fragments.RecipeCommentsFragment;
import com.cookery.fragments.RecipeFragment;
import com.cookery.fragments.RecipeImagesFragment;
import com.cookery.fragments.RecipeReviewFragment;
import com.cookery.fragments.RecipeViewImagesFragment;
import com.cookery.fragments.RecipeViewStepsFragment;
import com.cookery.fragments.WaitFragment;
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
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.FRAGMENT_COMMON_MESSAGE;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_WAIT;
import static com.cookery.utils.Constants.FRAGMENT_MY_TIMELINES;
import static com.cookery.utils.Constants.FRAGMENT_PICK_IMAGE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_IMAGES;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_REVIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.MY_TIMELINES;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

public class Utility extends Activity {
    private static final String CLASS_NAME = Utility.class.getName();

    public static UserMO getUserFromUserSecurity(Context context){
        UserSecurity userSecurity = new UserSecurity(context);
        return (UserMO) userSecurity.read(LOGGED_IN_USER);
    }

    public static void writeIntoUserSecurity(Context context, String key, Object value){
        UserSecurity userSecurity = new UserSecurity(context);userSecurity.write(key, value);
    }

    public static String getSmartNumber(int number){
        //TODO: here goes logic to convert number into smart number
        return String .valueOf(number);
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

    public static void showUnimplemetedActionSnacks(ViewGroup viewGroup){
        showSnacks(viewGroup, UN_IDENTIFIED_OBJECT_TYPE, OK, Snackbar.LENGTH_INDEFINITE);
    }

    public static String obectToJson(Object object){
        Gson gson = new Gson();

        try {
            if (object instanceof UserMO) {
                return gson.toJson(object, new TypeToken<UserMO>(){}.getType());
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+object.getClass().getName());
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Error in Object o JSON conversion");
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
                return gson.fromJson(jsonStr, new TypeToken<List<FoodTypeMO>>(){}.getType());
            }
            else if(mappingClass.equals(CuisineMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<CuisineMO>>(){}.getType());
            }
            else if(mappingClass.equals(IngredientMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<IngredientMO>>(){}.getType());
            }
            else if(mappingClass.equals(QuantityMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<QuantityMO>>(){}.getType());
            }
            else if(mappingClass.equals(TasteMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<TasteMO>>(){}.getType());
            }
            else if(mappingClass.equals(RecipeMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<RecipeMO>>(){}.getType());
            }
            else if(mappingClass.equals(MessageMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<MessageMO>(){}.getType());
            }
            else if(mappingClass.equals(UserMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<UserMO>(){}.getType());
            }
            else if(mappingClass.equals(ReviewMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<ReviewMO>>(){}.getType());
            }
            else if(mappingClass.equals(LikesMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<LikesMO>(){}.getType());
            }
            else if(mappingClass.equals(TimelineMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<TimelineMO>>(){}.getType());
            }
            else if(mappingClass.equals(CommentMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<CommentMO>>(){}.getType());
            }
            else{
                Log.e(CLASS_NAME, mappingClass+" is not identified for parsing JSON");
                throw new Exception();
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Error in parsing the json("+jsonStr+") into the mapping class("+mappingClass+")");
            Log.e(CLASS_NAME, e.getMessage());
        }

        return null;
    }

    public static void loadImageFromURL(Context context, String imageAddress, ImageView imageView){
        Picasso.with(context).load(SERVER_ADDRESS+imageAddress).placeholder(R.drawable.placeholder).into(imageView);
    }

    public static void loadImageFromPath(Context context, String imageAddress, ImageView imageView){
        Picasso.with(context).load(new File(imageAddress)).into(imageView);
    }

    public static void showRecipeFragment(FragmentManager fragmentManager, RecipeMO recipe){
        if(recipe == null){
            Log.e(CLASS_NAME, "Recipe is null");
            return;
        }

        String fragmentNameStr = FRAGMENT_RECIPE;
        String parentFragmentNameStr = null;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_ITEM, recipe);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(fragmentManager, fragmentNameStr);
    }

    public static void showMyTimelinesFragment(FragmentManager fragmentManager, List<TimelineMO> timelines){
        if(timelines == null || timelines.isEmpty()){
            Log.e(CLASS_NAME, "Timelines are null");
            return;
        }

        String fragmentNameStr = FRAGMENT_MY_TIMELINES;
        String parentFragmentNameStr = null;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable(MY_TIMELINES, (Serializable) timelines);

        MyTimelinesFragment fragment = new MyTimelinesFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(fragmentManager, fragmentNameStr);
    }

    public static void showRecipeReviewFragment(FragmentManager fragmentManager, String parentFragmentNameStr, RecipeMO recipe){
        if(recipe == null){
            Log.e(CLASS_NAME, "Recipe is null");
            return;
        }

        String fragmentNameStr = FRAGMENT_RECIPE_REVIEW;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, recipe);

        RecipeReviewFragment fragment = new RecipeReviewFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(fragmentManager, fragmentNameStr);
    }

    public static void showRecipeCommentsFragment(FragmentManager fragmentManager, String parentFragmentNameStr, RecipeMO recipe){
        if(recipe == null){
            Log.e(CLASS_NAME, "Recipe is null");
            return;
        }

        String fragmentNameStr = FRAGMENT_RECIPE_COMMENTS;

        Fragment frag = fragmentManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragmentNameStr);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_ITEM, recipe);

        RecipeCommentsFragment fragment = new RecipeCommentsFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(fragmentManager, fragmentNameStr);
    }

    public static void closeWaitDialog(FragmentManager fragManager, Fragment fragment){
        fragManager.beginTransaction().remove(fragment).commit();
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

    public static Fragment showMessageDialog(FragmentManager fragManager, Fragment parentFrag,  MessageMO message) {
        String fragmentNameStr = FRAGMENT_COMMON_MESSAGE;

        Fragment frag = fragManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragManager.beginTransaction().remove(frag).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, message);

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(parentFrag, 0);

        fragment.show(fragManager, fragmentNameStr);

        return fragment;
    }

    public static Fragment showRecipeImagesFragment(FragmentManager fragManager, RecipeMO recipe) {
        String fragmentNameStr = FRAGMENT_RECIPE_IMAGES;

        Fragment frag = fragManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragManager.beginTransaction().remove(frag).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, recipe);

        RecipeImagesFragment fragment = new RecipeImagesFragment();
        fragment.setArguments(bundle);

        fragment.show(fragManager, fragmentNameStr);

        return fragment;
    }

    /*public static List<Bitmap> getTestImages(Context context){
        List<Bitmap> images = new ArrayList<>();
        Bitmap bitmap = null;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_sample);
        images.add(bitmap);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_sample);
        images.add(bitmap);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_sample);
        images.add(bitmap);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_sample);
        images.add(bitmap);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_sample);
        images.add(bitmap);

        return images;
    }*/

    public static void showFragment(FragmentManager fragmentManager, String parentFragKey, String fragKey, Object fragment, Map<String, Object> params){
        Fragment frag = fragmentManager.findFragmentByTag(fragKey);

        if (frag != null) {
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragKey != null && !parentFragKey.trim().isEmpty()){
            parentFragment = fragmentManager.findFragmentByTag(parentFragKey);
        }


        Bundle bundle = new Bundle();

        for(Map.Entry<String, Object> iterMap : params.entrySet()){
            bundle.putSerializable(iterMap.getKey(), (Serializable) iterMap.getValue());
        }

        if(fragment instanceof AddRecipeFragment){
            AddRecipeFragment currentFrag = (AddRecipeFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof RecipeViewImagesFragment){
            RecipeViewImagesFragment currentFrag = (RecipeViewImagesFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof RecipeViewStepsFragment){
            RecipeViewStepsFragment currentFrag = (RecipeViewStepsFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else{
            Log.e(CLASS_NAME, "Error ! "+fragment+" fragment hasn't been configured in "+CLASS_NAME+" showFragment method yet.");
        }
    }

    public static int getPlaceHolder(){
        return R.drawable.placeholder;
    }
}
