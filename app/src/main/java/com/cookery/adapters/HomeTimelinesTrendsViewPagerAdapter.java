package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class HomeTimelinesTrendsViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = HomeTimelinesTrendsViewPagerAdapter.class.getName();

    private List<Integer> layouts;
    private UserMO loggedInUser;
    private Object array[];
    private View.OnClickListener listener;
    private android.support.v7.widget.PopupMenu.OnMenuItemClickListener menuItemListener;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView home_timelines_rv;
    private RecyclerView home_stories_rv;

    public HomeTimelinesTrendsViewPagerAdapter(Context context, List<Integer> layouts, UserMO loggedInUser, Object array[], View.OnClickListener listener, SwipeRefreshLayout.OnRefreshListener refreshListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener menuItemListener) {
        this.mContext = context;
        this.layouts = layouts;
        this.loggedInUser = loggedInUser;
        this.array = array;
        this.listener = listener;
        this.refreshListener = refreshListener;
        this.menuItemListener = menuItemListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupStories(layout); break;
            case 1: setupTrend(layout); break;
            case 2: setupTimeline(layout); break;
            default: break;
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupStories(ViewGroup layout) {
        List<TimelineMO> stories = (List<TimelineMO>) array[0];

        if(stories == null || stories.isEmpty()){
            return;
        }

        SwipeRefreshLayout home_stories_srl = layout.findViewById(R.id.home_stories_srl);
        home_stories_rv = layout.findViewById(R.id.home_stories_rv);

        final HomeStoriesRecyclerViewAdapter adapter = new HomeStoriesRecyclerViewAdapter(mContext, stories, listener, menuItemListener);
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                new AsyncTaskerStories(adapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        home_stories_rv.setLayoutManager(mLayoutManager);
        home_stories_rv.setItemAnimator(new DefaultItemAnimator());
        home_stories_rv.setAdapter(adapter);

        home_stories_srl.setOnRefreshListener(refreshListener);
    }

    private void setupTimeline(ViewGroup layout) {
        List<TimelineMO> timelines = (List<TimelineMO>) array[2];

        if(timelines == null || timelines.isEmpty()){
            return;
        }

        SwipeRefreshLayout home_timelines_srl = layout.findViewById(R.id.home_timelines_srl);
        home_timelines_rv = layout.findViewById(R.id.home_timelines_rv);

        final HomeTimelinesRecyclerViewAdapter adapter = new HomeTimelinesRecyclerViewAdapter(mContext, timelines, listener, menuItemListener);
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                new AsyncTaskerTimelines(adapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        home_timelines_rv.setLayoutManager(mLayoutManager);
        home_timelines_rv.setItemAnimator(new DefaultItemAnimator());
        home_timelines_rv.setAdapter(adapter);

        home_timelines_srl.setOnRefreshListener(refreshListener);
    }

    private void setupTrend(ViewGroup layout) {
        List<? extends Object> trends = (List<? extends Object>)array[1];

        if(trends == null || trends.isEmpty()){
            Log.e(CLASS_NAME, "Error ! No Trends found !");
            return;
        }

        RecyclerView home_trends_rv = layout.findViewById(R.id.home_trends_rv);

        HomeTrendsRecyclerViewAdapter adapter = new HomeTrendsRecyclerViewAdapter(mContext, trends, listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        home_trends_rv.setLayoutManager(mLayoutManager);
        home_trends_rv.setItemAnimator(new DefaultItemAnimator());
        home_trends_rv.setAdapter(adapter);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "STORIES";
            case 1: return "TREND";
            case 2: return "TIMELINE";
            default: return "UNIMPL";
        }
    }


    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }

    public void updateTimelinePrivacy(TimelineMO timeline) {
        ((HomeTimelinesRecyclerViewAdapter)home_timelines_rv.getAdapter()).updateTimelinePrivacy(timeline);
    }

    public void deleteTimeline(TimelineMO timeline) {
        ((HomeTimelinesRecyclerViewAdapter)home_timelines_rv.getAdapter()).deleteTimeline(timeline);
    }

    class AsyncTaskerTimelines extends AsyncTask<Object, Void, Object> {
        private HomeTimelinesRecyclerViewAdapter adapter;

        public AsyncTaskerTimelines(HomeTimelinesRecyclerViewAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.getFetchUserTimeline(loggedInUser.getUSER_ID(), adapter.getItemCount());
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object object) {
            List<TimelineMO> timelines = (List<TimelineMO>) object;

            if(timelines != null && !timelines.isEmpty()){
                adapter.updateBottomTimelines(timelines);
            }
            else{
                adapter.setOnBottomReachedListener(null);
            }
        }
    }

    class AsyncTaskerStories extends AsyncTask<Object, Void, Object> {
        private HomeStoriesRecyclerViewAdapter adapter;

        public AsyncTaskerStories(HomeStoriesRecyclerViewAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.getFetchUserStories(loggedInUser.getUSER_ID(), adapter.getItemCount());
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object object) {
            List<TimelineMO> stories = (List<TimelineMO>) object;

            if(stories != null && !stories.isEmpty()){
                adapter.updateBottomStories(stories);
            }
            else{
                adapter.setOnBottomReachedListener(null);
            }
        }
    }
}