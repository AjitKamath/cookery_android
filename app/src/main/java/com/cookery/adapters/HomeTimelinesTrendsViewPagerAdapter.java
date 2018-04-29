package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.TimelineMO;
import com.cookery.models.TrendMO;
import com.cookery.models.UserMO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import static com.cookery.utils.Constants.UI_FONT;

public class HomeTimelinesTrendsViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = HomeTimelinesTrendsViewPagerAdapter.class.getName();

    private List<Integer> layouts;
    private UserMO loggedInUser;

    private View.OnClickListener listener;
    private android.support.v7.widget.PopupMenu.OnMenuItemClickListener menuItemListener;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private OnBottomReachedListener bottomReachedListener;

    @Getter
    private RecyclerView home_timelines_rv;
    private SwipeRefreshLayout home_timelines_srl;

    @Getter
    private RecyclerView home_stories_rv;
    private SwipeRefreshLayout home_stories_srl;

    @Getter
    private RecyclerView home_trends_rv;

    public HomeTimelinesTrendsViewPagerAdapter(Context context, List<Integer> layouts, UserMO loggedInUser, View.OnClickListener listener, SwipeRefreshLayout.OnRefreshListener refreshListener, OnBottomReachedListener bottomReachedListener, android.support.v7.widget.PopupMenu.OnMenuItemClickListener menuItemListener) {
        this.mContext = context;
        this.layouts = layouts;
        this.loggedInUser = loggedInUser;

        this.listener = listener;
        this.refreshListener = refreshListener;
        this.bottomReachedListener = bottomReachedListener;
        this.menuItemListener = menuItemListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupStories(layout); break;
            case 1: setupTrends(layout); break;
            case 2: setupTimeline(layout); break;
            default: break;
        }

        setFont(layout);
        collection.addView(layout);

        return layout;
    }

    private void setupStories(ViewGroup layout) {
        home_stories_srl = layout.findViewById(R.id.home_stories_srl);
        home_stories_rv = layout.findViewById(R.id.home_stories_rv);

        HomeStoriesRecyclerViewAdapter adapter = new HomeStoriesRecyclerViewAdapter(mContext, loggedInUser, new ArrayList<TimelineMO>(), listener, menuItemListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        home_stories_rv.setLayoutManager(mLayoutManager);
        home_stories_rv.setItemAnimator(new DefaultItemAnimator());
        home_stories_rv.setAdapter(adapter);

        adapter.setOnBottomReachedListener(bottomReachedListener);
        home_stories_srl.setOnRefreshListener(refreshListener);
    }

    private void setupTimeline(ViewGroup layout) {
        home_timelines_srl = layout.findViewById(R.id.home_timelines_srl);
        home_timelines_rv = layout.findViewById(R.id.home_timelines_rv);

        HomeTimelinesRecyclerViewAdapter adapter = new HomeTimelinesRecyclerViewAdapter(mContext, loggedInUser, new ArrayList<TimelineMO>(), listener, menuItemListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        home_timelines_rv.setLayoutManager(mLayoutManager);
        home_timelines_rv.setItemAnimator(new DefaultItemAnimator());
        home_timelines_rv.setAdapter(adapter);

        adapter.setOnBottomReachedListener(bottomReachedListener);
        home_timelines_srl.setOnRefreshListener(refreshListener);
    }

    private void setupTrends(ViewGroup layout) {
        home_trends_rv = layout.findViewById(R.id.home_trends_rv);

        HomeTrendsRecyclerViewAdapter adapter = new HomeTrendsRecyclerViewAdapter(mContext, new ArrayList<TrendMO>(), listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        home_trends_rv.setLayoutManager(mLayoutManager);
        home_trends_rv.setItemAnimator(new DefaultItemAnimator());
        home_trends_rv.setAdapter(adapter);
    }

    public void updateTimelines(int index, List<TimelineMO> timelines){
        ((HomeTimelinesRecyclerViewAdapter)home_timelines_rv.getAdapter()).updateTimelines(index, timelines);
        home_timelines_srl.setRefreshing(false);
    }

    public void updateTrends(List<TrendMO> trends){
        ((HomeTrendsRecyclerViewAdapter)home_trends_rv.getAdapter()).updateTrends(trends);
    }

    public void updateStories(int index, List<TimelineMO> stories){
        ((HomeStoriesRecyclerViewAdapter)home_stories_rv.getAdapter()).updateStories(index, stories);
        home_stories_srl.setRefreshing(false);
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
}