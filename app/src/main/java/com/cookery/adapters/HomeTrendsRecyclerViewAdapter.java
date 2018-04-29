package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.TrendMO;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.TRENDS_RECIPES_OF_THE_MONTH;
import static com.cookery.utils.Constants.TRENDS_RECIPE_OF_THE_DAY;
import static com.cookery.utils.Constants.TRENDS_USER_OF_THE_WEEK;

public class HomeTrendsRecyclerViewAdapter extends RecyclerView.Adapter<HomeTrendsRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = HomeTrendsRecyclerViewAdapter.class.getName();
    private Context mContext;


    private List<TrendMO> trends;
    private View.OnClickListener clickListener;

    public HomeTrendsRecyclerViewAdapter(Context mContext, List<TrendMO> trends, View.OnClickListener clickListener) {
        this.mContext = mContext;
        this.trends = trends;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_trends_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrendMO trend = trends.get(position);

        holder.home_trends_item_title_tv.setText(trend.getTRND_MSG());

        List<? extends Object> items = null;
        if(TRENDS_RECIPE_OF_THE_DAY.equalsIgnoreCase(trend.getTRND_KEY()) || TRENDS_RECIPES_OF_THE_MONTH.equalsIgnoreCase(trend.getTRND_KEY())){
            items = trend.getRecipes();
        }
        else if(TRENDS_USER_OF_THE_WEEK.equalsIgnoreCase(trend.getTRND_KEY())){
            items = trend.getUsers();
        }
        else{
            Log.e(CLASS_NAME, "Unimplmented Trend Type : "+trend.getTRND_KEY());
            return;
        }

        holder.home_trends_item_vp.setAdapter(new HomeTrendsViewPagerAdapter(mContext, items, clickListener));
    }

    public void updateTrends(List<TrendMO> trends){
        if(this.trends == null){
            this.trends = new ArrayList<>();
        }

        this.trends.clear();
        this.trends.addAll(trends);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView home_trends_item_title_tv;
        public ViewPager home_trends_item_vp;

        public ViewHolder(View view) {
            super(view);
            home_trends_item_title_tv = view.findViewById(R.id.home_trends_item_title_tv);
            home_trends_item_vp = view.findViewById(R.id.home_trends_item_vp);
        }
    }
}