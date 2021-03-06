package com.cookery.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.RecipeMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_TIMELINE_DELETE;
import static com.cookery.utils.Constants.FRAGMENT_TIMELINE_HIDE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOUVRE_REQUEST_CODE;
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

        if(initialLoggedInUserCheck()){
            setupPage();

            setupSearch();
            setupFab();
            setupRateUs();
            setupNavigator();
        }
    }

    private void loadHomeContent() {
        new AsyncTaskUtility(getFragmentManager(), this,
                AsyncTaskUtility.Purpose.FETCH_STORIES_SELF, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new AsyncTaskUtility(getFragmentManager(), this,
                AsyncTaskUtility.Purpose.FETCH_TRENDS_SELF, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new AsyncTaskUtility(getFragmentManager(), this,
                AsyncTaskUtility.Purpose.FETCH_TIMELINE_SELF, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateTimelines(int index, List<TimelineMO> timelines){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter())
                .updateTimelines(index, timelines);
    }

    public void updateStories(int index, List<TimelineMO> stories){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter())
                .updateStories(index, stories);
    }

    public void updateTrends(List<TrendMO> trends){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter())
                .updateTrends(trends);
    }

    private void setupPage(){
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.home_stories);
        viewPagerTabsList.add(R.layout.home_trends);
        viewPagerTabsList.add(R.layout.home_timelines);

        for(Integer iter : viewPagerTabsList){
            content_home_timelines_trends_tl.addTab(content_home_timelines_trends_tl.newTab());
        }

        final Activity activity = this;
        content_home_timelines_trends_vp.setAdapter(new HomeTimelinesTrendsViewPagerAdapter(mContext, viewPagerTabsList, loggedInUser, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() != null) {
                    if (view.getId() == R.id.home_timeline_user_follow_unfollow_item_photo_iv) {
                        new AsyncTaskUtility(getFragmentManager(), activity, AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Integer) view.getTag());
                    } else if (view.getTag() instanceof RecipeMO) {
                        new AsyncTaskUtility(getFragmentManager(), activity, AsyncTaskUtility.Purpose.FETCH_RECIPE, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (RecipeMO) view.getTag());
                    } else if (view.getTag() instanceof UserMO) {
                        new AsyncTaskUtility(getFragmentManager(), activity, AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((UserMO) view.getTag()).getUSER_ID());
                    } else {
                        Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + view.getTag());
                    }
                } else {
                    Log.e(CLASS_NAME, "View Tag is null !");
                }
            }
        }, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch(content_home_timelines_trends_vp.getCurrentItem()){
                    case 0 : new AsyncTaskUtility(getFragmentManager(), activity,
                                AsyncTaskUtility.Purpose.FETCH_STORIES_SELF, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); break;

                    case 2 : new AsyncTaskUtility(getFragmentManager(), activity,
                                AsyncTaskUtility.Purpose.FETCH_TIMELINE_SELF, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); break;

                    default: Log.e(CLASS_NAME, "Error ! onRefresh is not yet handled in this tab. New kind of tab?");
                }
            }
        }, new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                int itemsCount = 0;

                switch(content_home_timelines_trends_vp.getCurrentItem()){
                    case 0 :
                            itemsCount = ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).getHome_stories_rv().getAdapter().getItemCount();

                            new AsyncTaskUtility(getFragmentManager(), activity,
                                AsyncTaskUtility.Purpose.FETCH_STORIES_SELF, loggedInUser, itemsCount)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); break;

                    case 2 :
                            itemsCount = ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).getHome_timelines_rv().getAdapter().getItemCount();

                            new AsyncTaskUtility(getFragmentManager(), activity,
                                AsyncTaskUtility.Purpose.FETCH_TIMELINE_SELF, loggedInUser, itemsCount)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); break;

                    default: Log.e(CLASS_NAME, "Error ! OnBottomReached is not yet handled in this tab. New kind of tab?");
                }
            }
        }, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.timeline_options_hide == item.getItemId()) {
                    if (item.getActionView() == null) {
                        Log.e(CLASS_NAME, "Error ! View associated with menu item is null.");
                        return false;
                    } else if (item.getActionView().getTag() == null) {
                        Log.e(CLASS_NAME, "Error ! Object in view tag is null.");
                        return false;
                    }

                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put(GENERIC_OBJECT, item.getActionView().getTag());

                    Utility.showFragment(getFragmentManager(), null, FRAGMENT_TIMELINE_HIDE, new TimelineHideFragment(), paramsMap);
                } else if (R.id.timeline_options_delete == item.getItemId()) {
                    if (item.getActionView() == null) {
                        Log.e(CLASS_NAME, "Error ! View associated with menu item is null.");
                        return false;
                    } else if (item.getActionView().getTag() == null) {
                        Log.e(CLASS_NAME, "Error ! Object in view tag is null.");
                        return false;
                    }

                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put(GENERIC_OBJECT, item.getActionView().getTag());

                    Utility.showFragment(getFragmentManager(), null, FRAGMENT_TIMELINE_DELETE, new TimelineDeleteFragment(), paramsMap);
                } else {
                    Log.e(CLASS_NAME, "Error ! Unimplemented menu item !");
                    return true;
                }

                return false;
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

        loadHomeContent();
    }

    public void updateTimelinePrivacy(TimelineMO timeline){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).updateTimelinePrivacy(timeline);
    }

    public void deleteTimeline(TimelineMO timeline){
        ((HomeTimelinesTrendsViewPagerAdapter)content_home_timelines_trends_vp.getAdapter()).deleteTimeline(timeline);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == LOUVRE_REQUEST_CODE){
            Fragment recipeAddFragment = getFragmentManager().findFragmentByTag(FRAGMENT_ADD_RECIPE);
            recipeAddFragment.onActivityResult(requestCode, resultCode, data);
        }
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
}
