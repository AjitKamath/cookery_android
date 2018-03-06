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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.RecipeMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;

import java.util.List;
import java.util.Map;

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
            case 0: setupTimeline(layout); break;
            case 1: setupTrend(layout); break;
        }

        setFont(layout);
        collection.addView(layout);
        return layout;
    }

    private void setupTimeline(ViewGroup layout) {
        List<TimelineMO> timelines = (List<TimelineMO>) array[0];

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
        Map<String, List<RecipeMO>> categoriesRecipes = (Map<String, List<RecipeMO>>)array[1];

        if(categoriesRecipes == null || categoriesRecipes.isEmpty()){
            return;
        }

        RecyclerView home_trends_rv = layout.findViewById(R.id.home_trends_rv);

        HomeCategoriesRecipesRecyclerViewAdapter adapter = new HomeCategoriesRecipesRecyclerViewAdapter(mContext, categoriesRecipes, listener);
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
            case 0: return "TIMELINE";
            case 1: return "TREND";
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
            //fetch timelines
            int index = adapter.getItemCount();
            List<TimelineMO> timelines = InternetUtility.getFetchUserTimeline(loggedInUser.getUSER_ID(), index);

            /*if(timelines != null){
                if(array[0] == null){
                    array[0] = new ArrayList<>();
                }

                array[0] = ((List<TimelineMO>) array[0]).addAll(timelines);
            }*/

            return timelines;
        }

        @Override
        protected void onPreExecute() {
            //fragment = Utility.showWaitDialog(getFragmentManager(), "loading recipes ..");
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
}