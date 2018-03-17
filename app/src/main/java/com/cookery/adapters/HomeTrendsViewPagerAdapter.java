package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;
import com.cookery.models.UserMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

public class HomeTrendsViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = HomeTrendsViewPagerAdapter.class.getName();

    private View.OnClickListener clickListener;
    public List<? extends Object> items = new ArrayList<>();

    public HomeTrendsViewPagerAdapter(Context context, List<? extends Object> items, View.OnClickListener clickListener) {
        this.mContext = context;
        this.items = items;
        this.clickListener = clickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = null;

        if(items.get(position) instanceof RecipeMO){
            layout = (ViewGroup) inflater.inflate(R.layout.home_trends_item_recipe_item, collection, false);
        }
        else if(items.get(position) instanceof UserMO){
            layout = (ViewGroup) inflater.inflate(R.layout.home_trends_item_user_item, collection, false);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+items.get(position));
        }

        if(layout != null){
            setupPage(layout, position);
            setFont(layout);
            collection.addView(layout);
        }

        return layout;
    }

    private void setupPage(ViewGroup layout, int position) {
        if(items.get(position) instanceof RecipeMO){
            TextView home_trends_item_recipe_item_recipe_name_tv = layout.findViewById(R.id.home_trends_item_recipe_item_recipe_name_tv);
            TextView home_trends_item_recipe_item_name_tv = layout.findViewById(R.id.home_trends_item_recipe_item_name_tv);
            ImageView home_trends_item_recipe_item_iv = layout.findViewById(R.id.home_trends_item_recipe_item_iv);
            ImageView home_trends_item_recipe_item_avg_rating_iv = layout.findViewById(R.id.home_trends_item_recipe_item_avg_rating_iv);
            TextView home_trends_item_recipe_item_avg_rating_tv = layout.findViewById(R.id.home_trends_item_recipe_item_avg_rating_tv);
            ImageView home_trends_item_recipe_item_likes_iv = layout.findViewById(R.id.home_trends_item_recipe_item_likes_iv);
            TextView home_trends_item_recipe_item_likes_tv = layout.findViewById(R.id.home_trends_item_recipe_item_likes_tv);
            ImageView home_trends_item_recipe_item_views_iv = layout.findViewById(R.id.home_trends_item_recipe_item_views_iv);
            TextView home_trends_item_recipe_item_views_tv = layout.findViewById(R.id.home_trends_item_recipe_item_views_tv);
            ImageView home_trends_item_recipe_item_comments_iv = layout.findViewById(R.id.home_trends_item_recipe_item_comments_iv);
            TextView home_trends_item_recipe_item_comments_tv = layout.findViewById(R.id.home_trends_item_recipe_item_comments_tv);

            RecipeMO recipe = (RecipeMO) items.get(position);
            if(recipe == null){
                Log.e(CLASS_NAME, "Error ! Recipe object is null");
                return;
            }

            if(recipe.getImages() != null && !recipe.getImages().isEmpty()){
                Utility.loadImageFromURL(mContext, recipe.getImages().get(0).getRCP_IMG(), home_trends_item_recipe_item_iv);
            }
            home_trends_item_recipe_item_recipe_name_tv.setText(recipe.getRCP_NAME().toUpperCase());
            home_trends_item_recipe_item_name_tv.setText(recipe.getUserName());

            home_trends_item_recipe_item_avg_rating_tv.setText(recipe.getAvgRating());
            if(recipe.isUserReviewed()){
                home_trends_item_recipe_item_avg_rating_iv.setBackgroundResource(R.drawable.star);
            }
            else{
                home_trends_item_recipe_item_avg_rating_iv.setBackgroundResource(R.drawable.star_unselected);
            }

            home_trends_item_recipe_item_views_tv.setText(Utility.getSmartNumber(recipe.getViewsCount()));

            home_trends_item_recipe_item_likes_tv.setText(Utility.getSmartNumber(recipe.getLikesCount()));
            if(recipe.isUserLiked()){
                home_trends_item_recipe_item_likes_iv.setBackgroundResource(R.drawable.heart);
            }
            else{
                home_trends_item_recipe_item_likes_iv.setBackgroundResource(R.drawable.heart_unselected);
            }

            home_trends_item_recipe_item_comments_tv.setText(Utility.getSmartNumber(recipe.getCommentsCount()));
        }
        else if(items.get(position) instanceof UserMO){
            CircleImageView home_trends_item_user_item_iv = layout.findViewById(R.id.home_trends_item_user_item_iv);
            ImageView home_trends_item_user_item_likes_iv = layout.findViewById(R.id.home_trends_item_user_item_likes_iv);
            TextView home_trends_item_user_item_likes_tv = layout.findViewById(R.id.home_trends_item_user_item_likes_tv);
            ImageView home_trends_item_user_item_comments_iv = layout.findViewById(R.id.home_trends_item_user_item_comments_iv);
            TextView home_trends_item_user_item_comments_tv = layout.findViewById(R.id.home_trends_item_user_item_comments_tv);
            TextView home_trends_item_user_item_user_name_tv = layout.findViewById(R.id.home_trends_item_user_item_user_name_tv);
            TextView home_trends_item_user_item_recipes_count_tv = layout.findViewById(R.id.home_trends_item_user_item_recipes_count_tv);
            TextView home_trends_item_user_item_followers_tv = layout.findViewById(R.id.home_trends_item_user_item_followers_tv);
            TextView home_trends_item_user_item_following_tv = layout.findViewById(R.id.home_trends_item_user_item_following_tv);

            UserMO user = (UserMO) items.get(position);
            if(user == null){
                Log.e(CLASS_NAME, "Error ! User object is null");
                return;
            }

            Utility.loadImageFromURL(mContext, user.getIMG(), home_trends_item_user_item_iv);

            home_trends_item_user_item_likes_iv.setBackgroundResource(Utility.getLikeImageId(user.isUserLiked()));
            home_trends_item_user_item_likes_tv.setText(Utility.getSmartNumber(user.getLikesCount()));
            home_trends_item_user_item_comments_tv.setText(Utility.getSmartNumber(user.getCommentsCount()));
            home_trends_item_user_item_user_name_tv.setText(user.getNAME());
            home_trends_item_user_item_recipes_count_tv.setText(Utility.getSmartNumber(user.getRecipesCount()));
            home_trends_item_user_item_followers_tv.setText(Utility.getSmartNumber(user.getFollowersCount()));
            home_trends_item_user_item_following_tv.setText(Utility.getSmartNumber(user.getFollowingCount()));
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+items.get(position));
        }

        layout.setTag(items.get(position));
        layout.setOnClickListener(clickListener);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }


    @Override
    public int getCount() {
        if(items == null){
            return 0;
        }

        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}