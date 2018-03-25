package com.cookery.adapters;

/**
 * Created by vishal on 24/03/18.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;

public class OnBoardingSplashViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private static final String CLASS_NAME = OnBoardingSplashViewPagerAdapter.class.getName();

    public OnBoardingSplashViewPagerAdapter(Context context) {
        this.mContext = context;
    }


    public int[] images = {

            R.drawable.magicdish,
            R.drawable.addorshare,
            R.drawable.masterchef,
            R.drawable.instalist,
            R.drawable.follower,
            R.drawable.socialbeing
    };


    public String[] headings = {

            "Magic Dish",
            "Add or Share recipe",
            "Be a masterchef",
            "Insta List",
            "Follow your Fav",
            "Being Social"
    };

    public String[] descriptions = {

            "Put together all ingredients what you have in the search and get a best dish all around the world which you can cook out of it.",
            "Find and share great dishes, put your delicious recipe in the plate and show your cooking talent to the world.",
            "compete with your friends and book the crown of masterchef for you.",
            "Create your ingredient list instantly directly from your favorite recipe.So you never miss any ingredient.",
            "follow your favourite chef or get followed.",
            "Invite and Connect with your friends and checkout what next they are going to prepare."
    };


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.fragment_onboarding_splash_slider, collection, false);

        ImageView image = (ImageView) view.findViewById(R.id.main_image_iv);
        TextView heading = (TextView) view.findViewById(R.id.heading_tv);
        TextView description = (TextView) view.findViewById(R.id.description_tv);

        image.setImageResource(images[position]);
        heading.setText(headings[position]);
        description.setText(descriptions[position]);

        collection.addView(view);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}