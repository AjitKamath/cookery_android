package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.filters.AutocompleteFilter;
import com.cookery.models.IngredientMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 27/8/17.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private static final String CLASS_NAME = AutoCompleteAdapter.class.getName();

    public List<Object> filteredIngredients = new ArrayList<>();
    private String type;
    private int layout;
    private String query;

    public AutoCompleteAdapter(Context context, int layout, String type) {
        super(context, layout);
        this.mContext = context;
        this.type = type;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        if(filteredIngredients == null){
            return 0;
        }

        return filteredIngredients.size();
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
        return new AutocompleteFilter(this, type);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(layout, parent, false);

        TextView autocomplete_tv = new TextView(mContext);
        ImageView autocomplete_iv;

        if(filteredIngredients.get(position) instanceof IngredientMO){
            // Get the data item from filtered list.
            IngredientMO ingredient = (IngredientMO) filteredIngredients.get(position);

            LinearLayout autocomplete_ingredient_ll = convertView.findViewById(R.id.autocomplete_ingredient_ll);

            autocomplete_tv = convertView.findViewById(R.id.autocomplete_ingredient_tv);
            autocomplete_iv = convertView.findViewById(R.id.autocomplete_ingredient_iv);

            autocomplete_tv.setText(ingredient.getING_NAME());
            autocomplete_iv.setImageResource(R.drawable.food);

            convertView.setTag(ingredient);

            setFont(autocomplete_ingredient_ll);
        }
        else if(filteredIngredients.get(position) instanceof RecipeMO){
            // Get the data item from filtered list.
            RecipeMO recipe = (RecipeMO) filteredIngredients.get(position);

            RelativeLayout home_master_search_recipe_item_image_rl = convertView.findViewById(R.id.home_master_search_recipe_item_image_rl);

            autocomplete_tv = convertView.findViewById(R.id.home_master_search_recipe_item_recipe_name_tv);
            autocomplete_iv = convertView.findViewById(R.id.home_master_search_recipe_item_image_iv);
            TextView home_master_search_recipe_item_user_name_tv = convertView.findViewById(R.id.home_master_search_recipe_item_user_name_tv);

            autocomplete_tv.setText(recipe.getRCP_NAME().toUpperCase());

            if(recipe.getRCP_IMGS() != null && recipe.getRCP_IMGS().get(0) != null){
                Utility.loadImageFromURL(mContext, recipe.getRCP_IMGS().get(0), autocomplete_iv);
            }

            home_master_search_recipe_item_user_name_tv.setText(recipe.getNAME());

            convertView.setTag(recipe);

            setFont(home_master_search_recipe_item_image_rl);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+filteredIngredients.get(position));
        }

        query = String.valueOf(autocomplete_tv.getText());

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
