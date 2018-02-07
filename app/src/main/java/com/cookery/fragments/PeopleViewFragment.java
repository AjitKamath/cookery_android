package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.PeopleViewViewPagerAdapter;
import com.cookery.interfaces.ItemClickListener;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_PEOPLE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_USER_VIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class PeopleViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    /*components*/
    @InjectView(R.id.people_view_rl)
    RelativeLayout people_view_rl;

    @InjectView(R.id.people_view_tl)
    TabLayout people_view_tl;

    @InjectView(R.id.people_view_tab_vp)
    ViewPager people_view_tab_vp;
    /*components*/

    private Object people;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getLoggedInUser();

        getDataFromBundle();

        setupPage();

        setFont(people_view_rl);

        return view;
    }

    private void getDataFromBundle() {
        people = getArguments().get(GENERIC_OBJECT);
    }

    private void getLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
    }

    private void setupPage() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.people_view_followers_following);
        viewPagerTabsList.add(R.layout.people_view_followers_following);

        for (Integer iter : viewPagerTabsList) {
            people_view_tl.addTab(people_view_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        people_view_tab_vp.setAdapter(new PeopleViewViewPagerAdapter(mContext, getFragmentManager(), viewPagerTabsList, loggedInUser, people, new ItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                if(item == null){
                    Log.e(CLASS_NAME, "Error ! User object is null");
                    return;
                }

                new AsyncFetchUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((UserMO)item).getUSER_ID());
            }
        }));
        people_view_tab_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(people_view_tl));

        people_view_tl.setupWithViewPager(people_view_tab_vp);
        people_view_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                people_view_tab_vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    // Empty constructor required for DialogFragment
    public PeopleViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
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

                Utility.showFragment(getFragmentManager(), FRAGMENT_PEOPLE_VIEW, FRAGMENT_USER_VIEW, new UserViewFragment(), bundleMap);

            }
            else{
                Log.e(CLASS_NAME, "Failed to fetch users details");
            }
        }
    }
}
