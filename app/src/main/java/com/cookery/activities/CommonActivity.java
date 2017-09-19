package com.cookery.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.AutoCompleteAdapter;
import com.cookery.fragments.AddRecipeFragment;
import com.cookery.fragments.RecipeFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.io.Serializable;
import java.util.List;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SELECTED_ITEM;

public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String CLASS_NAME = CommonActivity.class.getName();
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //check if the user is connected to the internet
        if(!InternetUtility.isNetworkAvailable(mContext)){
            FragmentManager fragment = getFragmentManager();
            //Utility.showNoInternetFragment(fragment);
            return;
        }

        //setupToolbar();

        setupNavigator();

        setupSearch();

        setupFab();
    }

    private void setupSearch() {
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(mContext, R.layout.master_search_recipe_autocomplete_item, "MASTER SEARCH");
        getCommon_header_search_av().setAdapter(adapter);

        getCommon_header_search_av().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeMO recipe = (RecipeMO) view.getTag();

                if(recipe == null){
                    Log.e(CLASS_NAME, "Selected Recipe object is null");
                    return;
                }

                new AsyncTaskerFetchRecipe().execute(recipe);
            }
        });
    }

    private void showRecipeFragment(RecipeMO recipe){
        String fragmentNameStr = FRAGMENT_RECIPE;
        String parentFragmentNameStr = null;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = manager.findFragmentByTag(parentFragmentNameStr);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_ITEM, recipe);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(manager, fragmentNameStr);
    }

    private void setupToolbar() {
        //toolbar
        //getToolbar().setTitle(getResources().getString(R.string.app_name));
        //setSupportActionBar(getToolbar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupNavigator() {
        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getDrawer_layout().addDrawerListener(toggle);
        //toggle.syncState();


        getCommon_header_navigation_drawer_iv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDrawer_layout().openDrawer(GravityCompat.START);
            }
        });

        //navigation
        getNav_view().setItemIconTintList(null);
        getNav_view().getMenu().getItem(1).setActionView(R.layout.menu_dot);
        getNav_view().setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        /*if(R.id.fab_seek_ll == view.getId()){
        }
        else if(R.id.fab_share_ll == view.getId()){
            showFabToolbar(false);
            //showShareFragment(null);
        }
        else{
            Log.e(CLASS_NAME, "Could not identify the view");
            Utility.showSnacks(getWrapper_home_cl(), "Could not identify the view", OK, Snackbar.LENGTH_INDEFINITE);
        }*/
    }

    private void setupFab() {
        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragement = Utility.showWaitDialog(fragmentManager, "Loading data ..");

                new AsyncTaskerFetchMasterData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fragement);
            }
        });
    }

    private void showAddRecipeFragment(MasterDataMO masterData) {
        String fragmentNameStr = FRAGMENT_ADD_RECIPE;
        String parentFragmentNameStr = null;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = manager.findFragmentByTag(parentFragmentNameStr);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MASTER, masterData);

        AddRecipeFragment fragment = new AddRecipeFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(manager, fragmentNameStr);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(R.id.navigation_drawer_logout == item.getItemId()){
            //new SharedPrefUtility(mContext).clearSharedPreference();
            //clearMasterData();
            //setupAccountSummary();
        }
        else{
            Utility.showSnacks(getDrawer_layout(), "NOT IMPLEMENTED YET", OK, Snackbar.LENGTH_INDEFINITE);
            return true;
        }

        getDrawer_layout().closeDrawer(GravityCompat.START);
        onResume();
        return true;
    }

    protected abstract DrawerLayout getDrawer_layout();

    protected abstract FloatingActionButton getFab();

    protected abstract NavigationView getNav_view();

    protected abstract ImageView getCommon_header_navigation_drawer_iv();

    protected abstract CoordinatorLayout getWrapper_home_cl();

    protected abstract AutoCompleteTextView getCommon_header_search_av();

    class AsyncTaskerFetchMasterData extends AsyncTask<Fragment, Void, Object> {

        private Fragment fragment;

        @Override
        protected Object doInBackground(Fragment... objects) {
            this.fragment = objects[0];

            MasterDataMO masterData = new MasterDataMO();

            masterData.setFoodTypes((List<FoodTypeMO>) InternetUtility.fetchAllFoodTypes());
            masterData.setCuisines((List<CuisineMO>)InternetUtility.fetchAllCuisines());
            masterData.setQuantities((List<QuantityMO>)InternetUtility.fetchAllQuantities());
            masterData.setTastes((List<TasteMO>)InternetUtility.fetchAllTastes());

            return masterData;
        }

        @Override
        protected void onPostExecute(Object object) {
            MasterDataMO masterData = (MasterDataMO) object;

            if(masterData.getFoodTypes() != null && !masterData.getFoodTypes().isEmpty()) {
                if(masterData.getCuisines() != null && !masterData.getCuisines().isEmpty()){
                    if(masterData.getQuantities() != null && !masterData.getQuantities().isEmpty()){
                        if(masterData.getTastes() != null && !masterData.getTastes().isEmpty()){
                            getFragmentManager().beginTransaction().remove(fragment).commit();
                            showAddRecipeFragment(masterData);
                        }
                    }
                }
            }
        }
    }

    class AsyncTaskerFetchRecipe extends AsyncTask<RecipeMO, Void, Object> {
        @Override
        protected Object doInBackground(RecipeMO... objects) {
            return InternetUtility.fetchRecipe(objects[0]);
        }

        @Override
        protected void onPostExecute(Object object) {
            showRecipeFragment((RecipeMO) object);
        }
    }
}
