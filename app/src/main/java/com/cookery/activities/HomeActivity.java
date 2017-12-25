package com.cookery.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.HomeTimelinesTrendsViewPagerAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.models.RecipeMO;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @InjectView(R.id.content_home_timelines_trends_tl)
    TabLayout content_home_timelines_trends_tl;

    @InjectView(R.id.content_home_timelines_trends_vp)
    ViewPager content_home_timelines_trends_vp;
    /*components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
    }

    private void setupTabs(Object array[]) {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.home_timelines);
        viewPagerTabsList.add(R.layout.home_trends);

        for(Integer iter : viewPagerTabsList){
            content_home_timelines_trends_tl.addTab(content_home_timelines_trends_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        content_home_timelines_trends_vp.setAdapter(new HomeTimelinesTrendsViewPagerAdapter(mContext, viewPagerTabsList, loggedInUser, array, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() != null && view.getTag() instanceof RecipeMO) {
                    new AsyncTaskerFetchRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (RecipeMO) view.getTag());
                }
            }
        }, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchHomeContent();
            }
        }));
        content_home_timelines_trends_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(content_home_timelines_trends_tl));

        content_home_timelines_trends_tl.setupWithViewPager(content_home_timelines_trends_vp);
        content_home_timelines_trends_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                content_home_timelines_trends_vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    @Override
    protected void setUpTabs(Object array[]) {
        this.setupTabs(array);
    }
}
