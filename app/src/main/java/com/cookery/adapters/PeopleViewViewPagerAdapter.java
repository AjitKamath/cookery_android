package com.cookery.adapters;

/**
 * Created by ajit on 25/8/17.
 */

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
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
import com.cookery.interfaces.ItemClickListener;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cookery.utils.Constants.SIMPLE_KEY_FOLLOWERS;
import static com.cookery.utils.Constants.SIMPLE_KEY_FOLLOWING;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_VIEW;

public class PeopleViewViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static final String CLASS_NAME = PeopleViewViewPagerAdapter.class.getName();

    private Object people;
    private List<Integer> layouts;
    public UserMO loggedInUser;
    private FragmentManager fragManager;

    private ItemClickListener itemClickListener;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private OnBottomReachedListener onBottomReachedListener;

    private RecyclerView people_view_followers_following_rv;

    @Getter
    private Map<Integer, AsyncTaskUtility.Purpose> viewPurposeMap = new HashMap<>();

    @Getter
    private List<ViewObject> viewObjectList = new ArrayList<>();

    public PeopleViewViewPagerAdapter(Context context, FragmentManager fragManager, List<Integer> layouts, UserMO loggedInUser, Object people, ItemClickListener itemClickListener,
                                      SwipeRefreshLayout.OnRefreshListener onRefreshListener, OnBottomReachedListener onBottomReachedListener) {
        this.mContext = context;
        this.layouts = layouts;
        this.loggedInUser = loggedInUser;
        this.people = people;
        this.fragManager = fragManager;
        this.itemClickListener = itemClickListener;
        this.onRefreshListener = onRefreshListener;
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(layouts.get(position), collection, false);

        switch(position){
            case 0: setupFollowing(layout);
                viewPurposeMap.put(position, AsyncTaskUtility.Purpose.FETCH_FOLLOWING);
                break;
            case 1: setupFollowers(layout);
                viewPurposeMap.put(position, AsyncTaskUtility.Purpose.FETCH_FOLLOWERS);
                break;

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

                setupUsers(layout, followers, SIMPLE_KEY_FOLLOWERS);
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

                setupUsers(layout, following, SIMPLE_KEY_FOLLOWING);
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Followers/Following array cannot be empty/null");
        }
    }

    private void setupUsers(final ViewGroup layout, List<UserMO> users, final String purpose){
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mContext, users, purpose, itemClickListener);
        adapter.setOnBottomReachedListener(onBottomReachedListener);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        people_view_followers_following_rv = layout.findViewById(R.id.people_view_followers_following_rv);
        people_view_followers_following_rv.setLayoutManager(mLayoutManager);
        people_view_followers_following_rv.setItemAnimator(new DefaultItemAnimator());
        people_view_followers_following_rv.setAdapter(adapter);

        final SwipeRefreshLayout people_view_followers_following_srl = layout.findViewById(R.id.people_view_followers_following_srl);
        people_view_followers_following_srl.setTag(purpose);
        people_view_followers_following_srl.setOnRefreshListener(onRefreshListener);

        viewObjectList.add(new ViewObject(people_view_followers_following_rv, people_view_followers_following_srl));
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 : return SIMPLE_KEY_FOLLOWING;
            case 1: return SIMPLE_KEY_FOLLOWERS;
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

    public void updateData(String who, List<UserMO> users, int index) {
        if("FOLLOWINGS".equalsIgnoreCase(who)){
            ((UsersRecyclerViewAdapter)viewObjectList.get(0).people_view_followers_following_rv.getAdapter()).updateUsers(index, users);
            viewObjectList.get(0).people_view_followers_following_srl.setRefreshing(false);

            notifyDataSetChanged();
        }
        else if("FOLLOWERS".equalsIgnoreCase(who)){
            ((UsersRecyclerViewAdapter)viewObjectList.get(1).people_view_followers_following_rv.getAdapter()).updateUsers(index, users);
            viewObjectList.get(1).people_view_followers_following_srl.setRefreshing(false);

            notifyDataSetChanged();
        }
    }

    @AllArgsConstructor
    public class ViewObject{
        @Getter
        private RecyclerView people_view_followers_following_rv;

        @Getter
        private SwipeRefreshLayout people_view_followers_following_srl;
    }
}

