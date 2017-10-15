package com.cookery.async;

import android.os.AsyncTask;

import com.cookery.fragments.RecipeFragment;
import com.cookery.models.LikesMO;
import com.cookery.utils.InternetUtility;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;

/**
 * Created by ajit on 10/10/17.
 */

public class AsyncUtility extends AsyncTask<Object, Void, Object> {
    Object activityOrFragment;
    String purpose;

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected Object doInBackground(Object... objects) {
        activityOrFragment = objects[0];
        purpose = String.valueOf(objects[1]);

        if(FRAGMENT_RECIPE.equalsIgnoreCase(purpose)){
            LikesMO like = (LikesMO) objects[2];
            return InternetUtility.submitLike(like);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        if(object == null){
            return;
        }

        if(FRAGMENT_RECIPE.equalsIgnoreCase(purpose)){
            LikesMO like = (LikesMO) object;

            ((RecipeFragment) activityOrFragment).setLikeView(like.isLiked(), like.getLikes());
        }

    }
}
