package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.UsersRecyclerViewAdapter;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.UserMO;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by vishal on 27/9/17.
 */
public class UsersFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.users_rl)
    RelativeLayout users_rl;

    @InjectView(R.id.users_tv)
    TextView users_tv;

    @InjectView(R.id.users_srl)
    SwipeRefreshLayout users_srl;

    @InjectView(R.id.users_rv)
    RecyclerView users_rv;
    /*components*/

    private List<UserMO> usersList;
    private String purpose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupPage();

        setFont(users_rl);

        return view;
    }

    private void getDataFromBundle() {
        Object array[] = (Object[])getArguments().get(GENERIC_OBJECT);
        purpose = String.valueOf(array[0]);
        usersList = (List<UserMO>) array[1];
    }

    private void setupPage() {
        if("LIKE".equalsIgnoreCase(purpose)){
            users_tv.setText("PEOPLE WHO LIKED");
        }
        else if("VIEW".equalsIgnoreCase(purpose)){
            users_tv.setText("PEOPLE WHO VIEWED");
        }
        else if("FOLLOWERS".equalsIgnoreCase(purpose)){
            users_tv.setText("PEOPLE WHO ARE FOLLOWING");
        }
        else if("FOLLOWINGS".equalsIgnoreCase(purpose)){
            users_tv.setText("PEOPLE WHO ARE FOLLOWED");
        }
        else{
            users_tv.setText("UNKNOWN");
        }

        if(usersList != null && !usersList.isEmpty()){
            final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mContext, usersList);
            adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {
                    //new HomeTimelinesTrendsViewPagerAdapter.AsyncTaskerTimelines(adapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            users_rv.setLayoutManager(mLayoutManager);
            users_rv.setItemAnimator(new DefaultItemAnimator());
            users_rv.setAdapter(adapter);

            users_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //new MyTimelinesFragment.AsyncTaskerFetchMyTimelines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTimelines.size());
                }
            });
        }
    }

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

    private void setFont(ViewGroup group) {
        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
