package com.cookery.filters;

import android.widget.Filter;

import com.cookery.adapters.HomeSearchAutoCompleteAdapter;
import com.cookery.utils.InternetUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class HomeSearchAutoCompleteFilter extends Filter {

    private HomeSearchAutoCompleteAdapter adapter;
    private List<Object> filteredList;

    public HomeSearchAutoCompleteFilter(HomeSearchAutoCompleteAdapter adapter) {
        super();
        this.adapter = adapter;
        this.filteredList = new ArrayList<>();
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
        filteredList = (List<Object>) InternetUtility.searchRecipes(filterPattern);

        if(filteredList != null){
            results.values = filteredList;
            results.count = filteredList.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if(adapter.filteredRecipes != null){
            adapter.filteredRecipes.clear();
        }

        if(results.values != null && !((List<Object>)results.values).isEmpty()){
            adapter.filteredRecipes.addAll((List<Object>) results.values);
            adapter.query = String.valueOf(constraint);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter.notifyDataSetInvalidated();
        }
    }
}
