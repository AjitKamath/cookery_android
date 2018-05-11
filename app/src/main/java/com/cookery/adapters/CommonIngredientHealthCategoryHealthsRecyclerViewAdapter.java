package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.HealthMO;

import java.util.List;

public class CommonIngredientHealthCategoryHealthsRecyclerViewAdapter extends RecyclerView.Adapter<CommonIngredientHealthCategoryHealthsRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = CommonIngredientHealthCategoryHealthsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private View.OnClickListener onClickListener;
    private List<HealthMO> ingredientHealths;

    public CommonIngredientHealthCategoryHealthsRecyclerViewAdapter(Context mContext, List<HealthMO> ingredientHealths, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.ingredientHealths = ingredientHealths;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_view_health_category_health_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HealthMO health = ingredientHealths.get(position);

        holder.ingredient_view_health_category_health_item_health_number_tv.setText(position+1+".");
        holder.ingredient_view_health_category_health_item_health_desc_tv.setText(health.getING_HLTH_DESC());
    }

    @Override
    public int getItemCount() {
        return ingredientHealths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ingredient_view_health_category_health_item_health_number_tv;
        private TextView ingredient_view_health_category_health_item_health_desc_tv;

        public ViewHolder(View view) {
            super(view);
            ingredient_view_health_category_health_item_health_number_tv = view.findViewById(R.id.ingredient_view_health_category_health_item_health_number_tv);
            ingredient_view_health_category_health_item_health_desc_tv = view.findViewById(R.id.ingredient_view_health_category_health_item_health_desc_tv);
        }
    }
}