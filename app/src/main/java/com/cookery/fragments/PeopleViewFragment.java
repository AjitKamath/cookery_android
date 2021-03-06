package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.PeopleViewSearchAutoCompleteAdapter;
import com.cookery.adapters.PeopleViewViewPagerAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.interfaces.ItemClickListener;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

import static com.cookery.utils.Constants.FRAGMENT_PEOPLE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

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
    
    @InjectView(R.id.people_view_search_act)
    DelayAutoCompleteTextView people_view_search_act;
    
    @InjectView(R.id.people_view_search_iv)
    ImageView people_view_search_iv;

    @InjectView(R.id.people_view_tl)
    TabLayout people_view_tl;

    @Getter
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

    private void setupPage() {
        setupSearch();

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
                if (item == null) {
                    Log.e(CLASS_NAME, "Error ! User object is null");
                    return;
                }

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PEOPLE_VIEW,
                        AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((UserMO) item).getUSER_ID());
            }
        }, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PEOPLE_VIEW,
                        ((PeopleViewViewPagerAdapter)people_view_tab_vp.getAdapter()).getViewPurposeMap().get(people_view_tab_vp.getCurrentItem()),
                        loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                int count = ((PeopleViewViewPagerAdapter)people_view_tab_vp.getAdapter()).getViewObjectList()
                        .get(people_view_tab_vp.getCurrentItem()).getPeople_view_followers_following_rv()
                        .getAdapter().getItemCount();


                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PEOPLE_VIEW,
                        ((PeopleViewViewPagerAdapter)people_view_tab_vp.getAdapter()).getViewPurposeMap().get(people_view_tab_vp.getCurrentItem()),
                        loggedInUser, count)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public void updateUsers(String who, List<UserMO> users, int index){
        ((PeopleViewViewPagerAdapter)people_view_tab_vp.getAdapter()).updateData(who, users, index);
    }

    private void setupSearch() {
        PeopleViewSearchAutoCompleteAdapter adapter = new PeopleViewSearchAutoCompleteAdapter(mContext, loggedInUser);
        people_view_search_act.setThreshold(2);
        people_view_search_act.setAutoCompleteDelay(1000);
        people_view_search_act.setAdapter(adapter);

        people_view_search_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(view.getTag() != null){
                    if(view.getTag() instanceof UserMO){
                        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                                AsyncTaskUtility.Purpose.FETCH_USER_PUBLIC_DETAILS, loggedInUser, 0)
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ((UserMO)view.getTag()).getUSER_ID());
                    }
                    else{
                        Log.e(CLASS_NAME, "Error ! "+UN_IDENTIFIED_OBJECT_TYPE+" : "+view.getTag());
                    }
                }
                else{
                    Log.e(CLASS_NAME, "Error ! View Tag is null/empty");
                }
            }
        });

        people_view_search_act.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(people_view_search_act.getText());

                if (text.trim().isEmpty()) {
                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setInterpolator(new LinearInterpolator());
                    people_view_search_iv.startAnimation(rotate);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(people_view_search_act.getText());

                if (text.trim().isEmpty()) {
                    RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setInterpolator(new LinearInterpolator());
                    people_view_search_iv.startAnimation(rotate);

                    people_view_search_iv.setVisibility(View.GONE);
                } else {
                    people_view_search_iv.setVisibility(View.VISIBLE);
                }
            }
        });

        people_view_search_iv.setVisibility(View.GONE);
        people_view_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                people_view_search_act.setText("");

                RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100);
                rotate.setInterpolator(new LinearInterpolator());
                people_view_search_iv.startAnimation(rotate);
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
}
