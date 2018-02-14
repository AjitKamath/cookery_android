package com.cookery.filters;

import android.widget.Filter;

import com.cookery.adapters.PeopleViewSearchAutoCompleteAdapter;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class PeopleViewSearchAutoCompleteFilter extends Filter {

    private PeopleViewSearchAutoCompleteAdapter adapter;
    private List<Object> filteredList;
    private UserMO loggedInUser;

    public PeopleViewSearchAutoCompleteFilter(PeopleViewSearchAutoCompleteAdapter adapter, UserMO loggedInUser) {
        super();
        this.adapter = adapter;
        this.filteredList = new ArrayList<>();
        this.loggedInUser = loggedInUser;
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
        filteredList = (List<Object>) InternetUtility.searchUsers(filterPattern, loggedInUser.getUSER_ID(), 0);

        if(filteredList != null){
            results.values = filteredList;
            results.count = filteredList.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if(adapter.users != null){
            adapter.users.clear();
        }

        if(results.values != null && !((List<Object>)results.values).isEmpty()){
            adapter.users.addAll((List<Object>) results.values);
            adapter.query = String.valueOf(constraint);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter.notifyDataSetInvalidated();
        }
    }
}
