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
import android.widget.TextView;

import com.cookery.R;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewImagesFullscreenViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = RecipeViewImagesFullscreenViewPagerAdapter.class.getName();

    public List<String> images;
    private View.OnClickListener listener;

    public RecipeViewImagesFullscreenViewPagerAdapter(Context context, List<String> images, View.OnClickListener listener) {
        this.mContext = context;
        this.images = images;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recipe_view_images_fullscreen_item, collection, false);

        setupPage(layout, position);

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupPage(ViewGroup layout, int position) {
        if(images == null){
            return;
        }

        ImageView recipe_view_images_fullscreen_item_image_iv = layout.findViewById(R.id.recipe_view_images_fullscreen_item_image_iv);
        Utility.loadImageFromURL(mContext, images.get(position), recipe_view_images_fullscreen_item_image_iv);

        recipe_view_images_fullscreen_item_image_iv.setOnClickListener(listener);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return images.get(position);
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