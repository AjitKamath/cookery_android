package com.cookery.filters;

import android.widget.Filter;

import com.cookery.adapters.IngredientAutoCompleteAdapter;
import com.cookery.models.IngredientMO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class IngredientFilter extends Filter {

    IngredientAutoCompleteAdapter adapter;
    List<IngredientMO> originalList;
    List<IngredientMO> filteredList;

    public IngredientFilter(IngredientAutoCompleteAdapter adapter, List<IngredientMO> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final IngredientMO ingredient : originalList) {
                if (ingredient.getING_NAME().toLowerCase().contains(filterPattern)) {
                    filteredList.add(ingredient);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredIngredients.clear();
        adapter.filteredIngredients.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
