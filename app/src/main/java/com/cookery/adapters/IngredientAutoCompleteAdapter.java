package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.filters.AutocompleteFilter;
import com.cookery.models.IngredientMO;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 27/8/17.
 */

public class IngredientAutoCompleteAdapter extends ArrayAdapter<String> {
    private Context mContext;

    public List<Object> filteredIngredients = new ArrayList<>();
    private static final String TYPE = "INGREDIENTS";

    public IngredientAutoCompleteAdapter(Context context) {
        super(context, R.layout.autocomplete_ingredient);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return filteredIngredients.size();
    }

    @Override
    public String getItem(int position) {
        return ((IngredientMO) filteredIngredients.get(position)).getING_NAME();
    }

    @Override
    public Filter getFilter() {
        return new AutocompleteFilter(this, TYPE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        IngredientMO ingredient = (IngredientMO) filteredIngredients.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.autocomplete_ingredient, parent, false);

        LinearLayout autocomplete_ingredient_ll = convertView.findViewById(R.id.autocomplete_ingredient_ll);

        TextView autocomplete_ingredient_tv = convertView.findViewById(R.id.autocomplete_ingredient_tv);
        ImageView autocomplete_ingredient_iv = convertView.findViewById(R.id.autocomplete_ingredient_iv);

        autocomplete_ingredient_tv.setText(ingredient.getING_NAME());
        autocomplete_ingredient_iv.setImageResource(R.drawable.food);

        convertView.setTag(ingredient);

        setFont(autocomplete_ingredient_ll);

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
