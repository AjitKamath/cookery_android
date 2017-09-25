package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;

import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.UI_FONT;

public class FavRecipesViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = FavRecipesViewPagerAdapter.class.getName();

    private Map<String, List<RecipeMO>> favRecipes;
    private View.OnClickListener listener;

    public FavRecipesViewPagerAdapter(Context context, Map<String, List<RecipeMO>> favRecipes, View.OnClickListener listener) {
        this.mContext = context;
        this.favRecipes = favRecipes;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_pager_fav_recipes_recipes, collection, false);

        String choice = "";
        switch(position){
            case 0: choice = "FAVORITES"; break;
            case 1: choice = "VIEWED"; break;
            case 2: choice = "REVIEWED"; break;
            default: choice = "UNIMPL";
        }

        setupPage(layout, choice);

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupPage(ViewGroup layout, String choice) {
        List<RecipeMO> recipes = favRecipes.get(choice);

        TextView view_pager_fav_recipes_tv = layout.findViewById(R.id.view_pager_fav_recipes_tv);

        if(recipes == null || recipes.isEmpty()){
            String message = "";
            switch(choice){
                case "FAVORITES": message = "No Favorites Recipes"; break;
                case "VIEWED": message = "No Viewed Recipes"; break;
                case "REVIEWED": message = "No Reviewed Recipes"; break;
                default: message = "UNIMPL";
            }

            view_pager_fav_recipes_tv.setText(message);
            view_pager_fav_recipes_tv.setVisibility(View.VISIBLE);
            return;
        }
        else{
            layout.findViewById(R.id.view_pager_fav_recipes_tv).setVisibility(View.GONE);
        }

        FavRecipesRecyclerViewAdapter adapter = new FavRecipesRecyclerViewAdapter(mContext, favRecipes.get(choice), listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        RecyclerView view_pager_fav_recipes_rv = layout.findViewById(R.id.view_pager_fav_recipes_rv);
        view_pager_fav_recipes_rv.setLayoutManager(mLayoutManager);
        view_pager_fav_recipes_rv.setItemAnimator(new DefaultItemAnimator());
        view_pager_fav_recipes_rv.setAdapter(adapter);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "FAVORITES";
            case 1: return "VIEWED";
            case 2: return "REVIEWED";
            default: return "UNIMPL";
        }
    }

    @Override
    public int getCount() {
        return favRecipes.size();
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