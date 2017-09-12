package com.cookery.filters;

import android.util.Log;
import android.widget.Filter;

import com.cookery.adapters.IngredientAutoCompleteAdapter;
import com.cookery.models.IngredientMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class AutocompleteFilter extends Filter {

    private IngredientAutoCompleteAdapter adapter;
    private List<Object> filteredList;
    private String type;

    public AutocompleteFilter(IngredientAutoCompleteAdapter adapter, String type) {
        super();
        this.adapter = adapter;
        this.filteredList = new ArrayList<>();
        this.type = type;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        final String filterPattern = String.valueOf(constraint).trim();

        if(filterPattern.isEmpty() || filterPattern.equalsIgnoreCase("null")){
            return results;
        }

        //fetch from database
        if("INGREDIENTS".equalsIgnoreCase(type)){
           filteredList = (List<Object>)Utility.fetchIngredients(filterPattern);
        }
        else{
            Log.e(AutocompleteFilter.class.getName(), "Error ! Could not identify auto complete type : "+type);
        }


        if(filteredList != null){
            results.values = filteredList;
            results.count = filteredList.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredIngredients.clear();

        if(results.values != null && !((List<Object>)results.values).isEmpty()){
            adapter.filteredIngredients.addAll((List<Object>) results.values);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter.notifyDataSetInvalidated();
        }
    }
}
