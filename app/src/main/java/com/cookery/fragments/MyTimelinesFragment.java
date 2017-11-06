package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.TimelinesRecyclerViewAdapter;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.TimelineMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.MY_TIMELINES;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by vishal on 27/9/17.
 */
public class MyTimelinesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.fragment_my_timeline_ll)
    LinearLayout fragment_my_timeline_ll;

    @InjectView(R.id.fragment_my_timeline_srl)
    SwipeRefreshLayout fragment_my_timeline_srl;

    @InjectView(R.id.fragment_my_timeline_rv)
    RecyclerView fragment_my_timeline_rv;
    //components

    private List<TimelineMO> myTimelines;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_timeline, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupMyRecipesFragment();

        setFont(fragment_my_timeline_ll);

        return view;
    }

    private void getDataFromBundle() {
        myTimelines = (ArrayList<TimelineMO>) getArguments().get(MY_TIMELINES);

        if(myTimelines == null){
            myTimelines = new ArrayList<>();
        }
    }


    private void setupMyRecipesFragment() {
        if(myTimelines == null || myTimelines.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Timelines are empty !");
            return;
        }

        final TimelinesRecyclerViewAdapter adapter = new TimelinesRecyclerViewAdapter(mContext, myTimelines, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.view_pager_recipes_recipe_options_iv == view.getId()){
                    final PopupMenu menu = new   PopupMenu(getActivity(), view);
                    menu.getMenuInflater().inflate(R.menu.recipe_mini_options_menu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //TODO: NOT IMPLEMENED YET

                            switch (item.getItemId()) {
                                case R.id.activity_home_drawer_my_favorites:
                                    // do something
                                    break;

                            }

                            menu.dismiss();

                            Utility.showSnacks(fragment_my_timeline_ll, "Not Implemented Yet !", OK, Snackbar.LENGTH_LONG);

                            return false;
                        }
                    });
                    menu.show();
                }
                else{
                    Utility.showSnacks(fragment_my_timeline_ll, "Not Implemented Yet !", OK, Snackbar.LENGTH_LONG);
                }
            }
        });
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                new AsyncTaskerFetchMyTimelines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTimelines.size());
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.scrollToPosition(myTimelines.size()-1);

        fragment_my_timeline_rv.setLayoutManager(mLayoutManager);
        fragment_my_timeline_rv.setItemAnimator(new DefaultItemAnimator());
        fragment_my_timeline_rv.setAdapter(adapter);

        fragment_my_timeline_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskerFetchMyTimelines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTimelines.size());
            }
        });

        /*fragment_my_timeline_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    new AsyncTaskerFetchMyTimelines().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTimelines.size());
                }
            }
        });*/
    }

    private void updateTopTimeline(List<TimelineMO> timelines){
        TimelinesRecyclerViewAdapter adapter = (TimelinesRecyclerViewAdapter) fragment_my_timeline_rv.getAdapter();
        adapter.updateTopTimelines(timelines);
    }

    private void updateBottomTimeline(List<TimelineMO> timelines){
        TimelinesRecyclerViewAdapter adapter = (TimelinesRecyclerViewAdapter) fragment_my_timeline_rv.getAdapter();
        adapter.updateBottomTimelines(timelines);
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

    class AsyncTaskerFetchMyTimelines extends AsyncTask<Integer, Void, List<TimelineMO>> {
        @Override
        protected void onPreExecute(){}

        @Override
        protected List<TimelineMO> doInBackground(Integer... objects) {
            return InternetUtility.getFetchUserTimeline(Utility.getUserFromUserSecurity(mContext).getUser_id(), myTimelines.size());
        }

        @Override
        protected void onPostExecute(List<TimelineMO> objectList) {
            List<TimelineMO> timelines = objectList;

            if(timelines != null && !timelines.isEmpty()){
                if(timelines.isEmpty()){
                    updateTopTimeline(timelines);
                }
                else{
                    updateBottomTimeline(timelines);
                }
            }

            fragment_my_timeline_srl.setRefreshing(false);
        }
    }
}
