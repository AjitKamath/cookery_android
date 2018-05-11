package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.HealthMO;

import java.util.List;
import java.util.Map;

public class CommonIngredientHealthCategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CommonIngredientHealthCategoriesRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = CommonIngredientHealthCategoriesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private View.OnClickListener onClickListener;
    private Map<String, List<HealthMO>> healthMap;

    public CommonIngredientHealthCategoriesRecyclerViewAdapter(Context mContext, Map<String, List<HealthMO>> healthMap, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.healthMap = healthMap;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_view_health_category_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String category = "ERROR";
        List<HealthMO> healths = null;
        int color = 0;

        switch (position){
            case 0: category = "GOOD FOR";
                    healths = healthMap.get("G");
                    color = R.color.lightGreen;
                    break;

            case 1: category = "BAD FOR";
                    healths = healthMap.get("B");
                    color = R.color.googleRed;
                    break;

            case 2: category = "WARNINGS";
                    healths = healthMap.get("W");
                    color = R.color.darkOrange;
                    break;

            default:
                Log.e(CLASS_NAME, "Error ! Expecting only B, G & W in healthMap keys. Seems there are more keys. New health category ?");
                return;
        }

        holder.ingredient_view_health_category_item_name_tv.setText(category);
        holder.ingredient_view_health_category_item_name_tv.setTextColor(ContextCompat.getColor(mContext,color));

        holder.ingredient_view_health_category_item_healths_rv.setAdapter(
                new CommonIngredientHealthCategoryHealthsRecyclerViewAdapter(mContext, healths,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.ingredient_view_health_category_item_healths_rv.setLayoutManager(mLayoutManager);
        holder.ingredient_view_health_category_item_healths_rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public int getItemCount() {
        return healthMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient_view_health_category_item_name_tv;
        private RecyclerView ingredient_view_health_category_item_healths_rv;

        public ViewHolder(View view) {
            super(view);
            ingredient_view_health_category_item_name_tv = view.findViewById(R.id.ingredient_view_health_category_item_name_tv);
            ingredient_view_health_category_item_healths_rv = view.findViewById(R.id.ingredient_view_health_category_item_healths_rv);
        }
    }
}