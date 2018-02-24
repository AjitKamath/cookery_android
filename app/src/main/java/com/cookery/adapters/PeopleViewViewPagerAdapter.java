package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
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
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_VIEW;

public class PeopleViewViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = PeopleViewViewPagerAdapter.class.getName();

    private Object people;
    private List<Integer> layouts;
    private View.OnClickListener listener;
    public UserMO loggedInUser;
    private FragmentManager fragManager;

    public PeopleViewViewPagerAdapter(Context context, FragmentManager fragManager, List<Integer> layouts, UserMO loggedInUser, Object people, View.OnClickListener listener) {
        this.mContext = context;
        this.layouts = layouts;
        this.loggedInUser = loggedInUser;
        this.people = people;
        this.fragManager = fragManager;
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupFollowing(layout); break;
            case 1: setupFollowers(layout); break;

            default:
                Log.e(CLASS_NAME, UN_IDENTIFIED_VIEW);
        }

        setFont(layout);

        collection.addView(layout);

        return layout;
    }

    private void setupFollowers(ViewGroup layout) {
        Object[] users = (Object[])people;

        if(users != null && users.length > 0){
            List<UserMO> followers = (List<UserMO>) users[0];

            TextView people_view_followers_following_message_tv = layout.findViewById(R.id.people_view_followers_following_message_tv);
            if(followers == null || followers.isEmpty()){
                people_view_followers_following_message_tv.setText("You don't have any followers");
            }
            else{
                people_view_followers_following_message_tv.setVisibility(View.GONE);

                setupUsers(layout, followers, "FOLLOWERS");
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Followers/Following array cannot be empty/null");
        }
    }

    private void setupFollowing(ViewGroup layout) {
        Object[] users = (Object[])people;

        if(users != null && users.length > 1){
            List<UserMO> following = (List<UserMO>) users[1];

            TextView people_view_followers_following_message_tv = layout.findViewById(R.id.people_view_followers_following_message_tv);
            if(following == null || following.isEmpty()){
                people_view_followers_following_message_tv.setText("You don't follow anyone");
            }
            else{
                people_view_followers_following_message_tv.setVisibility(View.GONE);

                setupUsers(layout, following, "FOLLOWING");
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Followers/Following array cannot be empty/null");
        }
    }

    private void setupUsers(final ViewGroup layout, List<UserMO> users, final String purpose){
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mContext, users, purpose);
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                new AsyncTaskerFetchOldPeople().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, adapter, purpose);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        RecyclerView people_view_followers_following_rv = layout.findViewById(R.id.people_view_followers_following_rv);
        people_view_followers_following_rv.setLayoutManager(mLayoutManager);
        people_view_followers_following_rv.setItemAnimator(new DefaultItemAnimator());
        people_view_followers_following_rv.setAdapter(adapter);

        final SwipeRefreshLayout people_view_followers_following_srl = layout.findViewById(R.id.people_view_followers_following_srl);
        people_view_followers_following_srl.setTag(purpose);
        people_view_followers_following_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskerFetchLatestPeople(purpose).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, layout, people_view_followers_following_srl);
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 : return "FOLLOWING";
            case 1: return "FOLLOWERS";
            default: return "UNIMPLEMENTED";
        }
    }


    @Override
    public int getCount() {
        return layouts == null ? 0 : layouts.size();
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

    class AsyncTaskerFetchLatestPeople extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;
        private ViewGroup layout;
        private String purpose;
        private SwipeRefreshLayout people_view_followers_following_srl;

        public AsyncTaskerFetchLatestPeople(String purpose){
            this.purpose = purpose;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            layout = (ViewGroup) objects[0];
            people_view_followers_following_srl = (SwipeRefreshLayout) objects[1];

            if("FOLLOWERS".equalsIgnoreCase(purpose)){
                return InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), 0);
            }
            else if("FOLLOWING".equalsIgnoreCase(purpose)){
                return InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), 0);
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not identify the purpose !");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            if("FOLLOWERS".equalsIgnoreCase(purpose)){
                fragment = Utility.showWaitDialog(fragManager, "fetching people who follow you..");
            }
            else if("FOLLOWING".equalsIgnoreCase(purpose)){
                fragment = Utility.showWaitDialog(fragManager, "fetching people whom you follow..");
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not identify the purpose !");
            }

        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> fetchedUsers = (List<UserMO>) object;

            if(fetchedUsers != null) {
                Utility.closeWaitDialog(fragManager, fragment);

                Object[] users = (Object[])people;
                if("FOLLOWERS".equalsIgnoreCase(purpose)){
                    users[0] = fetchedUsers;
                    setupFollowers(layout);
                    people_view_followers_following_srl.setRefreshing(false);
                }
                else if("FOLLOWING".equalsIgnoreCase(purpose)){
                    users[1] = fetchedUsers;
                    setupFollowing(layout);
                    people_view_followers_following_srl.setRefreshing(false);
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the purpose !");
                }
            }
        }
    }

    class AsyncTaskerFetchOldPeople extends AsyncTask<Object, Void, Object> {
        private UsersRecyclerViewAdapter adapter;
        private String purpose;

        @Override
        protected Object doInBackground(Object... objects) {
            adapter = (UsersRecyclerViewAdapter) objects[0];
            purpose = String.valueOf(objects[1]);

            if("FOLLOWERS".equalsIgnoreCase(purpose)){
                return InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), adapter.getItemCount());
            }
            else if("FOLLOWING".equalsIgnoreCase(purpose)){
                return InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), adapter.getItemCount());
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not identify the purpose !");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> fetchedUsers = (List<UserMO>) object;

            if(fetchedUsers != null) {
                Object[] users = (Object[])people;
                if("FOLLOWERS".equalsIgnoreCase(purpose)){
                    ((List<UserMO>)users[0]).addAll(fetchedUsers);
                    adapter.notifyDataSetChanged();
                }
                else if("FOLLOWING".equalsIgnoreCase(purpose)){
                    ((List<UserMO>)users[1]).addAll(fetchedUsers);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the purpose !");
                }
            }
        }
    }
}