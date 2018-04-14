package com.cookery.activities;

import android.app.Fragment;
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
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.HomeTimelinesTrendsViewPagerAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.fragments.TimelineDeleteFragment;
import com.cookery.fragments.TimelineHideFragment;
import com.cookery.fragments.UserViewFragment;
import com.cookery.models.RecipeMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_TIMELINE_DELETE;
import static com.cookery.utils.Constants.FRAGMENT_TIMELINE_HIDE;
import static com.cookery.utils.Constants.FRAGMENT_USER_VIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

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

    private void prepareTabs(Object array[]) {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.home_stories);
        viewPagerTabsList.add(R.layout.home_trends);
        viewPagerTabsList.add(R.layout.home_timelines);

        for(Integer iter : viewPagerTabsList){
            content_home_timelines_trends_tl.addTab(content_home_timelines_trends_tl.newTab());
        }

        HomeTimelinesTrendsViewPagerAdapter adapter = new HomeTimelinesTrendsViewPagerAdapter(mContext, viewPagerTabsList, loggedInUser, array, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag() != null){
                    if(view.getId() == R.id.home_timeline_user_follow_unfollow_item_photo_iv) {
                        new AsyncFetchUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Integer) view.getTag());
                    }
                    else if (view.getTag() instanceof RecipeMO) {
                        new AsyncTaskerFetchRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (RecipeMO) view.getTag());
                    }
                    else if (view.getTag() instanceof UserMO) {
                        new AsyncFetchUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((UserMO)view.getTag()).getUSER_ID());
                    }
                    else{
                        Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+view.getTag());
                    }
                }
                else{
                    Log.e(CLASS_NAME, "View Tag is null !");
                }
            }
        }, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineContent();
            }
        }, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(R.id.timeline_options_hide == item.getItemId()){
                    if(item.getActionView() == null){
                        Log.e(CLASS_NAME, "Error ! View associated with menu item is null.");
                        return false;
                    }
                    else if(item.getActionView().getTag() == null ){
                        Log.e(CLASS_NAME, "Error ! Object in view tag is null.");
                        return false;
                    }

                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put(GENERIC_OBJECT, item.getActionView().getTag());

                    Utility.showFragment(getFragmentManager(), null, FRAGMENT_TIMELINE_HIDE, new TimelineHideFragment(), paramsMap);
                }
                else if(R.id.timeline_options_delete == item.getItemId()){
                    if(item.getActionView() == null){
                        Log.e(CLASS_NAME, "Error ! View associated with menu item is null.");
                        return false;
                    }
                    else if(item.getActionView().getTag() == null ){
                        Log.e(CLASS_NAME, "Error ! Object in view tag is null.");
                        return false;
                    }

                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put(GENERIC_OBJECT, item.getActionView().getTag());

                    Utility.showFragment(getFragmentManager(), null, FRAGMENT_TIMELINE_DELETE, new TimelineDeleteFragment(), paramsMap);
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Unimplemented menu item !");
                    return true;
                }

                return false;
            }
        });

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        content_home_timelines_trends_vp.setAdapter(adapter);
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

    public void updateTimelinePrivacy(TimelineMO timeline){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).updateTimelinePrivacy(timeline);
    }

    public void deleteTimeline(TimelineMO timeline){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).deleteTimeline(timeline);
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
        this.prepareTabs(array);
    }


    class AsyncFetchUser extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching user details ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchUsersPublicDetails(Integer.parseInt(String.valueOf(objects[0])), loggedInUser.getUSER_ID());
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if (object == null) {
                return;
            }

            List<UserMO> users = (List<UserMO>) object;

            if (users != null && !users.isEmpty()) {
                Map<String, Object> bundleMap = new HashMap<String, Object>();
                bundleMap.put(GENERIC_OBJECT, users.get(0));

                Utility.showFragment(getFragmentManager(), null, FRAGMENT_USER_VIEW, new UserViewFragment(), bundleMap);

            }
            else{
                Log.e(CLASS_NAME, "Failed to fetch users details");
            }
        }
    }
}
