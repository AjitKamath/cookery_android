package com.cookery.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.HomeCategoriesRecipesRecyclerViewAdapter;
import com.cookery.models.RecipeMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.ASYNC_TASK_GET_ALL_CATEGORY_RECIPES;
import static com.cookery.utils.Constants.TOP_RECIPES_CHEF;
import static com.cookery.utils.Constants.TOP_RECIPES_MONTH;
import static com.cookery.utils.Constants.TRENDING_RECIPES;

public class HomeActivity extends CommonActivity{

    private static final String CLASS_NAME = HomeActivity.class.getName();
    private Context mContext = this;

    /*components*/
    @InjectView(R.id.wrapper_home_cl)
    CoordinatorLayout wrapper_home_cl;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @InjectView(R.id.common_header_search_av)
    AutoCompleteTextView common_header_search_av;

    @InjectView(R.id.common_header_navigation_drawer_iv)
    ImageView common_header_navigation_drawer_iv;

    @InjectView(R.id.nav_view)
    NavigationView nav_view;

    @InjectView(R.id.fab)
    FloatingActionButton fab;

    @InjectView(R.id.content_home_rv)
    RecyclerView content_home_rv;
    /*components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setupScreen();
    }

    private void setupScreen() {
        fetchAllCategoriesRecipes();
    }

    private void fetchAllCategoriesRecipes() {
        new AsyncTasker().execute();
    }

    private void setupAllCategoriesRecipes(Map<String, List<RecipeMO>> categoriesRecipes){
        HomeCategoriesRecipesRecyclerViewAdapter adapter = new HomeCategoriesRecipesRecyclerViewAdapter(mContext, categoriesRecipes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        content_home_rv.setLayoutManager(mLayoutManager);
        content_home_rv.setItemAnimator(new DefaultItemAnimator());
        content_home_rv.setAdapter(adapter);
    }

    @Override
    protected DrawerLayout getDrawer_layout() {
        return drawer_layout;
    }

    @Override
    protected ImageView getCommon_header_navigation_drawer_iv(){
        return common_header_navigation_drawer_iv;
    }

    @Override
    protected NavigationView getNav_view() {
        return nav_view;
    }

    @Override
    protected CoordinatorLayout getWrapper_home_cl() {
        return wrapper_home_cl;
    }

    @Override
    protected FloatingActionButton getFab() {
        return fab;
    }

    @Override
    protected AutoCompleteTextView getCommon_header_search_av(){
        return common_header_search_av;
    }

    class AsyncTasker extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            Map<String, List<RecipeMO>> allCategoriesRecipes = new HashMap<>();

            allCategoriesRecipes.put(TRENDING_RECIPES, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_MONTH, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_CHEF, InternetUtility.fetchTrendingRecipes());

            return allCategoriesRecipes;
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "loading recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            setupAllCategoriesRecipes((Map<String, List<RecipeMO>>) object);

            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
