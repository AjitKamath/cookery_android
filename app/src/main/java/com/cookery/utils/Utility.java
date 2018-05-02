package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
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
import com.cookery.fragments.PickPhotoFragment;
import com.cookery.fragments.ProfileViewEmailFragment;
import com.cookery.fragments.ProfileViewFragment;
import com.cookery.fragments.ProfileViewGenderFragment;
import com.cookery.fragments.ProfileViewImageFragment;
import com.cookery.fragments.ProfileViewNameFragment;
import com.cookery.fragments.ProfileViewPasswordFragment;
import com.cookery.fragments.ProfileViewPhoneFragment;
import com.cookery.fragments.RecipeAddFragment;
import com.cookery.fragments.RecipeImagesFragment;
import com.cookery.fragments.RecipeViewFragment;
import com.cookery.fragments.RecipeViewReviewsFragment;
import com.cookery.fragments.RecipeViewStepsFragment;
import com.cookery.fragments.ShareSocialMediaFragment;
import com.cookery.fragments.SomethingWrongFragment;
import com.cookery.fragments.TimelineDeleteFragment;
import com.cookery.fragments.TimelineHideFragment;
import com.cookery.fragments.UserViewFragment;
import com.cookery.fragments.UsersFragment;
import com.cookery.fragments.WaitFragment;
import com.cookery.models.CommentMO;
import com.cookery.models.CuisineMO;
import com.cookery.models.FavouritesMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.IngredientUOMMO;
import com.cookery.models.LikesMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TasteMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.cookery.utils.Constants.DEFAULT_CROP_RATIO;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_MESSAGE;
import static com.cookery.utils.Constants.FRAGMENT_COMMON_WAIT;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SCOPE_FOLLOWERS;
import static com.cookery.utils.Constants.SCOPE_PUBLIC;
import static com.cookery.utils.Constants.SCOPE_SELF;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

public class Utility extends Activity {
    private static final String CLASS_NAME = Utility.class.getName();

    public static String getUserNameOrYou(String username, int userId, int loggedInUserId){
        if(userId != 0 && userId == loggedInUserId){
            return "YOU";
        }
        else{
            return username;
        }
    }

    public static String getUserNameOrYour(String username, int userId, int loggedInUserId){
        if(userId != 0 && userId == loggedInUserId){
            return "YOUR";
        }
        else{
            return username+"'s";
        }
    }

    public static int getScopeImageId(int scope){
        if(SCOPE_SELF == scope){
            return R.drawable.user;
        }
        else if(SCOPE_PUBLIC == scope){
            return R.drawable.globe;
        }
        else if(SCOPE_FOLLOWERS == scope){
            return R.drawable.users;
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not identify the scope id("+scope+")");
            return R.drawable.globe;
        }
    }

    //TODO: use setupLikeView istead of this method
    @Deprecated
    public static int getLikeImageId(boolean isLiked){
        if(isLiked){
            return getLikeImage();
        }
        else{
            return getUnLikeImage();
        }
    }

    public static void setupLikeView(ViewGroup view, boolean userLiked, int likesCount) {
        ImageView image = view.findViewById(R.id.common_like_view_iv);
        TextView text = view.findViewById(R.id.common_like_view_tv);

        image.setBackgroundResource(userLiked ? getLikeImage() : getUnLikeImage());
        text.setText(Utility.getSmartNumber(likesCount));

        image.setTag(R.id.common_like_view_like, userLiked ? "LIKED" : "");
        text.setTag(R.id.common_like_view_like_count, likesCount);
    }

    public static void addRemoveLike(View view){
        ImageView image = view.findViewById(R.id.common_like_view_iv);
        TextView text = view.findViewById(R.id.common_like_view_tv);

        int likes = Integer.parseInt(String.valueOf(text.getTag(R.id.common_like_view_like_count)));

        if("LIKED".equalsIgnoreCase(String.valueOf(image.getTag(R.id.common_like_view_like)))){
            likes = likes - 1;

            image.setBackgroundResource(getUnLikeImage());
            text.setText(Utility.getSmartNumber(likes));

            image.setTag(R.id.common_like_view_like, "");
        }
        else{
            likes = likes + 1;

            image.setBackgroundResource(getLikeImage());
            text.setText(Utility.getSmartNumber(likes));

            image.setTag(R.id.common_like_view_like, "LIKED");
        }

        text.setTag(R.id.common_like_view_like_count, likes);
    }

    private static int getLikeImage(){
        return R.drawable.heart;
    }

    private static int getUnLikeImage(){
        return R.drawable.heart_unselected;
    }

    public static int getReviewImageId(boolean isReviewed){
        if(isReviewed){
            return R.drawable.star;
        }
        else{
            return R.drawable.star_unselected;
        }
    }

    public static UserMO getUserFromUserSecurity(Context context){
        UserSecurity userSecurity = new UserSecurity(context);
        List<UserMO> users = (List<UserMO>) userSecurity.read(LOGGED_IN_USER);

        if(users != null && !users.isEmpty()){
            return users.get(0);
        }

        return null;
    }

    public static String getGender(String gender){
        if(gender.equalsIgnoreCase("m")){
            return "HE";
        }
        else if(gender.equalsIgnoreCase("f")){
            return "SHE";
        }
        else{
            return "OTHER";
        }
    }

    public static void writeIntoUserSecurity(Context context, String key, Object value){
        UserSecurity userSecurity = new UserSecurity(context);
        userSecurity.write(key, value);
    }

    public static String getSmartNumber(int number){
        //TODO: here goes logic to convert number into smart number
        return String .valueOf(number);
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
                List<UserMO> users = new ArrayList<UserMO>();
                users.add((UserMO) object);

                return gson.toJson(users, new TypeToken<List<UserMO>>(){}.getType());
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

    public static Object jsonToObject(String jsonStr, Class mappingClass) throws CookeryException{
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
            else if(mappingClass.equals(IngredientAkaMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<IngredientAkaMO>>(){}.getType());
            }
            else if(mappingClass.equals(IngredientUOMMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<IngredientUOMMO>>(){}.getType());
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
                return gson.fromJson(jsonStr, new TypeToken<List<UserMO>>(){}.getType());
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
            else if(mappingClass.equals(MyListMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<MyListMO>>(){}.getType());
            }
            else if(mappingClass.equals(FavouritesMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<FavouritesMO>>(){}.getType());
            }
            else if(mappingClass.equals(TrendMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<TrendMO>>(){}.getType());
            }
            else if(mappingClass.equals(RecipeImageMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<RecipeImageMO>>(){}.getType());
            }
            else if(mappingClass.equals(MasterDataMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<MasterDataMO>(){}.getType());
            }
            else if(mappingClass.equals(IngredientMO.class)){
                return gson.fromJson(jsonStr, new TypeToken<List<IngredientMO>>(){}.getType());
            }
            else{
                Log.e(CLASS_NAME, mappingClass+" is not identified for parsing JSON");
                throw new CookeryException(CookeryException.ErrorCode.NO_JSON_MAPPING_CLASS);
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Error in parsing the json("+jsonStr+") into the mapping class("+mappingClass+")");
            Log.e(CLASS_NAME, e.getMessage());

            throw new CookeryException(CookeryException.ErrorCode.JSON_TO_OBJECT_MAPPING_ERROR, e);
        }
    }

    public static void loadImageFromURL(Context context, String imageAddress, ImageView imageView){
        if(imageView == null){
            Log.e(CLASS_NAME, "Image View is null !");
            return;
        }

        if(imageAddress == null || imageAddress.trim().isEmpty()){
            Log.e(CLASS_NAME, "Error ! Image url is null/empty !");
            imageView.setImageResource(R.drawable.placeholder);
            return;
        }

        Picasso.with(context).load(SERVER_ADDRESS+imageAddress).placeholder(R.drawable.placeholder).into(imageView);
    }

    public static void loadImageFromPath(Context context, String imageAddress, ImageView imageView){
        if(imageAddress == null || imageAddress.trim().isEmpty()){
            Log.e(CLASS_NAME, "Error ! Image url is null/empty !");
            return;
        }

        if(imageView == null){
            Log.e(CLASS_NAME, "Image View is null !");
            return;
        }

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

        RecipeViewFragment fragment = new RecipeViewFragment();
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

        if(params != null){
            for(Map.Entry<String, Object> iterMap : params.entrySet()){
                bundle.putSerializable(iterMap.getKey(), (Serializable) iterMap.getValue());
            }
        }

        if(fragment instanceof RecipeAddFragment){
            RecipeAddFragment currentFrag = (RecipeAddFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ShareSocialMediaFragment){
            ShareSocialMediaFragment currentFrag = (ShareSocialMediaFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof MessageFragment){
            MessageFragment currentFrag = (MessageFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ImagesFragment){
            ImagesFragment currentFrag = (ImagesFragment) fragment;
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
        else if(fragment instanceof UsersFragment){
            UsersFragment currentFrag = (UsersFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewFragment){
            ProfileViewFragment currentFrag = (ProfileViewFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewNameFragment){
            ProfileViewNameFragment currentFrag = (ProfileViewNameFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewEmailFragment){
            ProfileViewEmailFragment currentFrag = (ProfileViewEmailFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewPasswordFragment){
            ProfileViewPasswordFragment currentFrag = (ProfileViewPasswordFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewPhoneFragment){
            ProfileViewPhoneFragment currentFrag = (ProfileViewPhoneFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewGenderFragment){
            ProfileViewGenderFragment currentFrag = (ProfileViewGenderFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof UserViewFragment){
            UserViewFragment currentFrag = (UserViewFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof TimelineHideFragment){
            TimelineHideFragment currentFrag = (TimelineHideFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof TimelineDeleteFragment){
            TimelineDeleteFragment currentFrag = (TimelineDeleteFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof ProfileViewImageFragment){
            ProfileViewImageFragment currentFrag = (ProfileViewImageFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof PeopleViewFragment){
            PeopleViewFragment currentFrag = (PeopleViewFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof LoginFragment){
            LoginFragment currentFrag = (LoginFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof MyReviewsFragment){
            MyReviewsFragment currentFrag = (MyReviewsFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof MyRecipesFragment){
            MyRecipesFragment currentFrag = (MyRecipesFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof LoginFragment){
            LoginFragment currentFrag = (LoginFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof CommentsFragment){
            CommentsFragment currentFrag = (CommentsFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof RecipeViewReviewsFragment){
            RecipeViewReviewsFragment currentFrag = (RecipeViewReviewsFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof CookeryErrorFragment){
            CookeryErrorFragment currentFrag = (CookeryErrorFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof SomethingWrongFragment){
            SomethingWrongFragment currentFrag = (SomethingWrongFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof NoInternetFragment){
            NoInternetFragment currentFrag = (NoInternetFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof DeleteCommentFragment){
            DeleteCommentFragment currentFrag = (DeleteCommentFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof RecipeViewFragment){
            RecipeViewFragment currentFrag = (RecipeViewFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof PickPhotoFragment){
            PickPhotoFragment currentFrag = (PickPhotoFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof RecipeImagesFragment){
            RecipeImagesFragment currentFrag = (RecipeImagesFragment) fragment;
            currentFrag.setArguments(bundle);
            if (parentFragment != null) {
                currentFrag.setTargetFragment(parentFragment, 0);
            }
            currentFrag.show(fragmentManager, fragKey);
        }
        else if(fragment instanceof IngredientViewFragment){
            IngredientViewFragment currentFrag = (IngredientViewFragment) fragment;
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

    public static void startCropImageActivity(Context mContext, Object fragmentOrActivity){
        if(fragmentOrActivity instanceof Fragment){
            CropImage
                    .activity()
                    .setAllowRotation(true)
                    .setAutoZoomEnabled(true)
                    .setFixAspectRatio(true)
                    .setInitialCropWindowPaddingRatio(DEFAULT_CROP_RATIO)
                    .start(mContext, (Fragment) fragmentOrActivity);
        }
        else if(fragmentOrActivity instanceof Activity){
            CropImage
                    .activity()
                    .setAllowRotation(true)
                    .setAutoZoomEnabled(true)
                    .setFixAspectRatio(true)
                    .setInitialCropWindowPaddingRatio(DEFAULT_CROP_RATIO)
                    .start((Activity) fragmentOrActivity);
        }
        else{
            Log.e(CLASS_NAME, "Something went wrong");
        }
    }

    public static void hideSoftKeyboard (Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static String getUniquePhoneId() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.SUPPORTED_ABIS.length % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(SERVER_ADDRESS+src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.e(CLASS_NAME, "Error ! "+e);
            return null;
        }
    }
}