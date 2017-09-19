package com.cookery.filters;

import android.util.Log;
import android.widget.Filter;

import com.cookery.adapters.AutoCompleteAdapter;
import com.cookery.utils.InternetUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class AutocompleteFilter extends Filter {

    private AutoCompleteAdapter adapter;
    private List<Object> filteredList;
    private String type;

    public AutocompleteFilter(AutoCompleteAdapter adapter, String type) {
        super();
        this.adapter = adapter;
        this.filteredList = new ArrayList<>();
        this.type = type;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        final FilterResults results = new FilterResults();

        if(filteredList == null){
            filteredList = new ArrayList<>();
        }

        filteredList.clear();

        final String filterPattern = String.valueOf(constraint).trim();

        if(filterPattern.isEmpty() || filterPattern.equalsIgnoreCase("null")){
            return results;
        }

        //fetch from database
        if("INGREDIENTS".equalsIgnoreCase(type)){
           filteredList = (List<Object>) InternetUtility.fetchIngredients(filterPattern);
        }
        else if("MASTER SEARCH".equalsIgnoreCase(type)){
            filteredList = (List<Object>) InternetUtility.fetchMasterSearch(filterPattern);
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
        if(adapter.filteredIngredients != null){
            adapter.filteredIngredients.clear();
        }

        if(results.values != null && !((List<Object>)results.values).isEmpty()){
            adapter.filteredIngredients.addAll((List<Object>) results.values);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter.notifyDataSetInvalidated();
        }
    }
}
