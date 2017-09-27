package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.FavRecipesRecyclerViewAdapter;
import com.cookery.models.RecipeMO;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.MY_RCPS;

/**
 * Created by vishal on 27/9/17.
 */
public class MyRecipesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;


    //components

    @InjectView(R.id.content_my_recipes_rv)
    RecyclerView content_my_recipes_rv;

    @InjectView(R.id.fragment_my_recipe_header_tv)
    TextView fragment_my_recipe_header_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container);
        ButterKnife.inject(this, view);

        setupMyRecipesFragment();

        return view;
    }


    private void setupMyRecipesFragment() {

        ArrayList<RecipeMO> myrcps = (ArrayList<RecipeMO>) getArguments().get(MY_RCPS);

        FavRecipesRecyclerViewAdapter adapter = new FavRecipesRecyclerViewAdapter(mContext, myrcps, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        content_my_recipes_rv.setLayoutManager(mLayoutManager);
        content_my_recipes_rv.setItemAnimator(new DefaultItemAnimator());
        content_my_recipes_rv.setAdapter(adapter);

        fragment_my_recipe_header_tv.setText("MY RECIPES");

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


}
