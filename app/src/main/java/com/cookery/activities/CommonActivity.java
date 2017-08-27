package com.cookery.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.fragments.AddRecipeFragment;
import com.cookery.utils.Utility;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.OK;

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
        if(!Utility.isNetworkAvailable(mContext)){
            FragmentManager fragment = getFragmentManager();
            //Utility.showNoInternetFragment(fragment);
            return;
        }

        //setupToolbar();

        setupNavigator();

        setupFab();
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
                showAddRecipeFragment();
            }
        });
    }

    private void showAddRecipeFragment() {
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

        AddRecipeFragment fragment = new AddRecipeFragment();

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
}
