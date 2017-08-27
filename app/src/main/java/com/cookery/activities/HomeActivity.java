package com.cookery.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cookery.R;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends CommonActivity{

    /*components*/
    @InjectView(R.id.wrapper_home_cl)
    CoordinatorLayout wrapper_home_cl;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @InjectView(R.id.common_header_navigation_drawer_iv)
    ImageView common_header_navigation_drawer_iv;

    @InjectView(R.id.nav_view)
    NavigationView nav_view;

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    /*components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setupScreen();
    }

    private void setupScreen() {
        setupTrends();
    }

    private void setupTrends() {

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
}
