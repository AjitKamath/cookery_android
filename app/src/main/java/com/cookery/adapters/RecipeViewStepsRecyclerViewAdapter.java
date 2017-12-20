package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewStepsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewStepsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeViewStepsRecyclerViewAdapter.class.getName();
    private Context mContext;

    public List<String> steps = new ArrayList<>();
    private View.OnClickListener listener;

    public RecipeViewStepsRecyclerViewAdapter(Context mContext, List<String> steps, View.OnClickListener listener) {
        this.mContext = mContext;
        this.steps = steps;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_recipe_steps_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String step = steps.get(position);

        holder.recipe_view_recipe_steps_item_step_count_tv.setText("STEP "+(++position));
        holder.recipe_view_recipe_steps_item_step_tv.setText(step);

        setFont(holder.recipe_view_recipe_steps_item_cv);
    }

    @Override
    public int getItemCount() {
        if(steps == null){
            return 0;
        }
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView recipe_view_recipe_steps_item_cv;
        public TextView recipe_view_recipe_steps_item_step_tv;
        public TextView recipe_view_recipe_steps_item_step_count_tv;

        public ViewHolder(View view) {
            super(view);
            recipe_view_recipe_steps_item_cv = view.findViewById(R.id.recipe_view_recipe_steps_item_cv);
            recipe_view_recipe_steps_item_step_tv = view.findViewById(R.id.recipe_view_recipe_steps_item_step_tv);
            recipe_view_recipe_steps_item_step_count_tv = view.findViewById(R.id.recipe_view_recipe_steps_item_step_count_tv);
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