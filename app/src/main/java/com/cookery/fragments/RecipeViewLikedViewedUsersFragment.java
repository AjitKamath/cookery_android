package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeViewLikedViewedUsersRecyclerViewAdapter;
import com.cookery.models.UserMO;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by vishal on 27/9/17.
 */
public class RecipeViewLikedViewedUsersFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.recipe_view_liked_viewed_users_ll)
    LinearLayout recipe_view_liked_viewed_users_ll;

    @InjectView(R.id.recipe_view_liked_viewed_users_tv)
    TextView recipe_view_liked_viewed_users_tv;

    @InjectView(R.id.recipe_view_liked_viewed_users_rv)
    RecyclerView recipe_view_liked_viewed_users_rv;
    /*components*/

    private List<UserMO> usersList;
    private String purpose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view_liked_viewed_users, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupPage();

        setFont(recipe_view_liked_viewed_users_ll);

        return view;
    }

    private void getDataFromBundle() {
        Object array[] = (Object[])getArguments().get(GENERIC_OBJECT);
        purpose = String.valueOf(array[0]);
        usersList = (List<UserMO>) array[1];
    }

    private void setupPage() {
        if("LIKE".equalsIgnoreCase(purpose)){
            recipe_view_liked_viewed_users_tv.setText("PEOPLE WHO LIKED");
        }
        else if("VIEW".equalsIgnoreCase(purpose)){
            recipe_view_liked_viewed_users_tv.setText("PEOPLE WHO VIEWED");
        }
        else{
            recipe_view_liked_viewed_users_tv.setText("UNKNOWN");
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recipe_view_liked_viewed_users_rv.setLayoutManager(mLayoutManager);
        recipe_view_liked_viewed_users_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_view_liked_viewed_users_rv.setAdapter(new RecipeViewLikedViewedUsersRecyclerViewAdapter(mContext, usersList));
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
