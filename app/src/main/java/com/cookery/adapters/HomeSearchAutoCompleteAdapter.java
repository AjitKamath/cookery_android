package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.filters.HomeSearchAutoCompleteFilter;
import com.cookery.models.IngredientMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 27/8/17.
 */

public class HomeSearchAutoCompleteAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private static final String CLASS_NAME = HomeSearchAutoCompleteAdapter.class.getName();

    public List<Object> filteredRecipes = new ArrayList<>();
    private int layout;
    public String query;

    public HomeSearchAutoCompleteAdapter(Context context) {
        super(context, R.layout.home_search_item);
        this.layout = R.layout.home_search_item;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(filteredRecipes == null){
            return 0;
        }

        return filteredRecipes.size();
    }

    @Override
    public String getItem(int position) {
        /*if(filteredIngredients.get(position) instanceof IngredientMO){
            return String.valueOf(((IngredientMO) filteredIngredients.get(position)).getING_NAME());
        }
        else if(filteredIngredients.get(position) instanceof RecipeMO){
            return String.valueOf(((RecipeMO) filteredIngredients.get(position)).getRCP_NAME());
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+filteredIngredients.get(position));
        }

        return "ERROR";*/

        return "";
    }

    @Override
    public Filter getFilter() {
        return new HomeSearchAutoCompleteFilter(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(layout, parent, false);

        if(filteredRecipes.get(position) instanceof RecipeMO){
            // Get the data item from filtered list.
            RecipeMO recipe = (RecipeMO) filteredRecipes.get(position);

            String matchMessage = "UNKNOWN";
            String matchType = "UNKNOWN";

            if(recipe.getRCP_NAME().toUpperCase().contains(query.toUpperCase())){
                matchMessage = ".. recipe name as ";
                matchType = recipe.getRCP_NAME();
            }
            else if(recipe.getUserName().toUpperCase().contains(query.toUpperCase())){
                matchMessage = ".. recipe by ";
                matchType = recipe.getUserName();
            }
            else{
                for(IngredientMO ingredient : recipe.getIngredients()){
                    if(ingredient.getING_NAME().toUpperCase().contains(query.toUpperCase())){
                        matchMessage = ".. recipe made with ";
                        matchType = ingredient.getING_NAME();
                        break;
                    }
                }
            }

            LinearLayout home_search_item_ll = convertView.findViewById(R.id.home_search_item_ll);
            TextView home_search_item_match_tv = convertView.findViewById(R.id.home_search_item_match_tv);
            TextView home_search_item_match_type_tv = convertView.findViewById(R.id.home_search_item_match_type_tv);
            CircleImageView home_search_item_recipe_iv = convertView.findViewById(R.id.home_search_item_recipe_iv);
            TextView home_search_item_recipe_tv = convertView.findViewById(R.id.home_search_item_recipe_tv);
            CircleImageView home_search_item_user_iv = convertView.findViewById(R.id.home_search_item_user_iv);
            TextView home_search_item_user_tv = convertView.findViewById(R.id.home_search_item_user_tv);
            TextView home_search_item_rating_tv = convertView.findViewById(R.id.home_search_item_rating_tv);
            TextView home_search_item_likes_tv = convertView.findViewById(R.id.home_search_item_likes_tv);
            TextView home_search_item_views_tv = convertView.findViewById(R.id.home_search_item_views_tv);

            home_search_item_match_tv.setText(matchMessage);
            home_search_item_match_type_tv.setText(matchType);

            if(recipe.getUserImage() != null && !recipe.getUserImage().trim().isEmpty()){
                Utility.loadImageFromURL(mContext, recipe.getUserImage(), home_search_item_user_iv);
            }

            home_search_item_user_tv.setText(recipe.getUserName());
            home_search_item_rating_tv.setText(recipe.getAvgRating());
            home_search_item_likes_tv.setText(Utility.getSmartNumber(recipe.getLikesCount()));
            home_search_item_views_tv.setText(Utility.getSmartNumber(recipe.getViewsCount()));

            home_search_item_recipe_tv.setText(recipe.getRCP_NAME());

            if(recipe.getImages() != null && !recipe.getImages().isEmpty()){
                Utility.loadImageFromURL(mContext, recipe.getImages().get(0), home_search_item_recipe_iv);
            }

            convertView.setTag(recipe);

            setFont(home_search_item_ll);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+filteredRecipes.get(position));
        }

        return convertView;
    }

    private void setFont(ViewGroup group) {
        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
