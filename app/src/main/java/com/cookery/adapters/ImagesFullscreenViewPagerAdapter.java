package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.Utility;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.cookery.utils.Constants.UI_FONT;

public class ImagesFullscreenViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = ImagesFullscreenViewPagerAdapter.class.getName();

    public List<? extends Object> images;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;

    public ImagesFullscreenViewPagerAdapter(Context context, List<? extends Object> images, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        this.mContext = context;
        this.images = images;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.common_images_fullscreen_item, collection, false);

        setupPage(layout, position);

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupPage(ViewGroup layout, int position) {
        if(images == null){
            return;
        }

        Object object = images.get(position);

        RelativeLayout recipe_view_images_fullscreen_item_image_rl = layout.findViewById(R.id.common_images_fullscreen_item_image_rl);
        ImageView recipe_view_images_fullscreen_item_image_iv = layout.findViewById(R.id.common_images_fullscreen_item_image_iv);
        LinearLayout common_like_view_ll = layout.findViewById(R.id.common_like_view_ll);
        LinearLayout recipe_view_images_fullscreen_item_comments_ll = layout.findViewById(R.id.common_images_fullscreen_item_comments_ll);
        TextView recipe_view_images_fullscreen_item_comments_tv = layout.findViewById(R.id.common_images_fullscreen_item_comments_tv);

        if(object instanceof RecipeImageMO){
            RecipeImageMO recipeImage = (RecipeImageMO) object;

            if(images != null && !images.isEmpty()){
                Utility.loadImageFromURL(mContext, recipeImage.getRCP_IMG(), recipe_view_images_fullscreen_item_image_iv);
            }

            Utility.setupLikeView(common_like_view_ll, recipeImage.isUserLiked(), recipeImage.getLikesCount());
            recipe_view_images_fullscreen_item_comments_tv.setText(Utility.getSmartNumber(recipeImage.getCommentsCount()));
        }
        else if(object instanceof UserMO){
            UserMO user = (UserMO) object;

            if(images != null && !images.isEmpty()){
                Utility.loadImageFromURL(mContext, user.getIMG(), recipe_view_images_fullscreen_item_image_iv);
            }

            Utility.setupLikeView(common_like_view_ll, user.isUserLiked(), user.getLikesCount());
            recipe_view_images_fullscreen_item_comments_tv.setText(Utility.getSmartNumber(user.getCommentsCount()));
        }

        recipe_view_images_fullscreen_item_image_rl.setOnClickListener(listener);
        common_like_view_ll.setOnClickListener(listener);
        common_like_view_ll.setOnLongClickListener(longClickListener);
        recipe_view_images_fullscreen_item_comments_ll.setOnClickListener(listener);

        common_like_view_ll.setTag(images.get(position));
        recipe_view_images_fullscreen_item_comments_ll.setTag(images.get(position));
        recipe_view_images_fullscreen_item_image_rl.setTag(images.get(position));

        PhotoViewAttacher pAttacher = new PhotoViewAttacher(recipe_view_images_fullscreen_item_image_iv);
        pAttacher.update();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return images.get(position).toString();
    }


    @Override
    public int getCount() {
        if(images == null){
            return 0;
        }

        return images.size();
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