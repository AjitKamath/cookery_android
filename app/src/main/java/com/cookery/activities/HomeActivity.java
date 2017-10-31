package com.cookery.activities;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.HomeCategoriesRecipesRecyclerViewAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.models.RecipeMO;
import com.cookery.utils.InternetUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    DelayAutoCompleteTextView common_header_search_av;

    @InjectView(R.id.common_header_search_iv)
    ImageView common_header_search_iv;

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
        new AsyncTaskerHomeRecipes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new AsyncTaskerFetchMasterData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "JUST_FETCH");
    }

    private void setupAllCategoriesRecipes(Map<String, List<RecipeMO>> categoriesRecipes){
        HomeCategoriesRecipesRecyclerViewAdapter adapter = new HomeCategoriesRecipesRecyclerViewAdapter(mContext, categoriesRecipes, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeMO recipe = (RecipeMO) view.getTag();

                if(recipe == null){
                    Log.e(CLASS_NAME, "Selected Recipe object is null");
                    return;
                }

                new AsyncTaskerFetchRecipe().execute(recipe);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.scrollToPosition(categoriesRecipes.size()-1);

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
    protected DelayAutoCompleteTextView getCommon_header_search_av(){
        return common_header_search_av;
    }

    @Override
    protected ImageView getCommon_header_search_iv(){
        return common_header_search_iv;
    }

    class AsyncTaskerHomeRecipes extends AsyncTask<Object, Void, Object> {
        //private Fragment fragment;

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
            //fragment = Utility.showWaitDialog(getFragmentManager(), "loading recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            Map<String, List<RecipeMO>> categoryRecipesMap = (Map<String, List<RecipeMO>>) object;

            if(categoryRecipesMap != null || !categoryRecipesMap.isEmpty()){
                setupAllCategoriesRecipes((Map<String, List<RecipeMO>>) object);
                //Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }
}
