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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.ReviewsMiniRecyclerViewAdapter;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_MY_REVIEWS;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by vishal on 27/9/17.
 */
public class MyReviewsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.fragment_my_reviews_ll)
    LinearLayout fragment_my_reviews_ll;

    @InjectView(R.id.fragment_my_reviews_tv)
    TextView fragment_my_reviews_tv;

    @InjectView(R.id.fragment_my_reviews_content_recipes_srl)
    SwipeRefreshLayout fragment_my_reviews_content_recipes_srl;

    @InjectView(R.id.fragment_my_reviews_content_recipes_rv)
    RecyclerView fragment_my_reviews_content_recipes_rv;
    //components

    private List<ReviewMO> myReviews;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reviews, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupMyRecipesFragment();

        setFont(fragment_my_reviews_ll);

        return view;
    }

    private void getDataFromBundle() {
        myReviews = (List<ReviewMO>) getArguments().get(GENERIC_OBJECT);
        loggedInUser =(UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupMyRecipesFragment() {
        if(myReviews != null && !myReviews.isEmpty()){
            fragment_my_reviews_tv.setVisibility(View.GONE);
        }
        else{
            fragment_my_reviews_tv.setVisibility(View.VISIBLE);
        }

        final ReviewsMiniRecyclerViewAdapter adapter = new ReviewsMiniRecyclerViewAdapter(mContext, myReviews, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(R.id.fragment_reviews_review_mini_item_options_iv == view.getId()){
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
                                    default:break;

                            }

                            menu.dismiss();

                            Utility.showSnacks(fragment_my_reviews_content_recipes_rv, "Not Implemented Yet !", OK, Snackbar.LENGTH_LONG);

                            return false;
                        }
                    });
                    menu.show();
                }
            }
        });

        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_MY_REVIEWS,
                        AsyncTaskUtility.Purpose.FETCH_MY_REVIEWS, loggedInUser, adapter.getItemCount())
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        fragment_my_reviews_content_recipes_rv.setLayoutManager(mLayoutManager);
        fragment_my_reviews_content_recipes_rv.setItemAnimator(new DefaultItemAnimator());
        fragment_my_reviews_content_recipes_rv.setAdapter(adapter);

        fragment_my_reviews_content_recipes_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_MY_REVIEWS,
                        AsyncTaskUtility.Purpose.FETCH_MY_REVIEWS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    public void updateReviews(List<ReviewMO> reviews, int index){
        ((ReviewsMiniRecyclerViewAdapter)fragment_my_reviews_content_recipes_rv.getAdapter()).updateReviews(reviews, index);
        fragment_my_reviews_content_recipes_srl.setRefreshing(false);
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

    public void removeOnBottomReachedListener() {
        ((ReviewsMiniRecyclerViewAdapter)fragment_my_reviews_content_recipes_rv.getAdapter()).setOnBottomReachedListener(null);
    }
}
