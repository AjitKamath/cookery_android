package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.RecipeMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

public class ReviewsMiniRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsMiniRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = ReviewsMiniRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<RecipeMO> reviews;
    private View.OnClickListener listener;

    public ReviewsMiniRecyclerViewAdapter(Context mContext, List<RecipeMO> reviews, View.OnClickListener listener) {
        this.mContext = mContext;
        this.reviews = reviews;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reviews_review_mini_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecipeMO recipe = reviews.get(position);

        if(recipe.getImages() != null && !recipe.getImages().isEmpty()){
            Utility.loadImageFromURL(mContext, recipe.getImages().get(0), holder.fragment_reviews_review_mini_item_iv);
        }

        /*stars*/
        List<ImageView> starsList = new ArrayList<>();
        starsList.add(holder.fragment_reviews_review_mini_item_star_1_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_2_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_3_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_4_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_5_iv);
        /*stars*/

        holder.fragment_reviews_review_mini_item_recipe_name_tv.setText(recipe.getRCP_NAME().toUpperCase());
        holder.fragment_reviews_review_mini_item_food_type_tv.setText(recipe.getFoodTypeName().toUpperCase());
        holder.fragment_reviews_review_mini_item_cuisine_tv.setText(recipe.getFoodCuisineName().toUpperCase());
        holder.fragment_reviews_review_mini_item_likes_count_tv.setText(String.valueOf(recipe.getLikedUsers() == null ? 0 : recipe.getLikedUsers().size()));

        if(recipe.getReviews() != null || !recipe.getReviews().isEmpty()){
            holder.fragment_reviews_review_mini_item_review_tv.setText(recipe.getReviews().get(0).getREVIEW());
            setStars(starsList, recipe.getReviews().get(0).getRATING());
        }

        if(recipe.getCREATE_DTM() != null && !recipe.getCREATE_DTM().trim().isEmpty()){
            Date date = DateTimeUtility.convertStringToDateTime(recipe.getCREATE_DTM(), DB_DATE_TIME);
            holder.view_pager_recipes_recipe_mini_date_time_tv.setText(DateTimeUtility.getSmartDateTime(date));
        }
        else if(recipe.getCREATE_DTM() != null && !recipe.getCREATE_DTM().trim().isEmpty()){
            Date date = DateTimeUtility.convertStringToDateTime(recipe.getCREATE_DTM(), DB_DATE_TIME);
            holder.view_pager_recipes_recipe_mini_date_time_tv.setText(DateTimeUtility.getSmartDateTime(date));
        }

        holder.fragment_reviews_review_mini_item_username_tv.setText(recipe.getUserName());

        holder.fragment_reviews_review_mini_item_rl.setTag(recipe);
        holder.fragment_reviews_review_mini_item_rl.setOnClickListener(listener);
        holder.fragment_reviews_review_mini_item_options_iv.setOnClickListener(listener);

        setFont(holder.fragment_reviews_review_mini_item_rl);
    }

    private void setStars(List<ImageView> starsList, int count){
        if(starsList != null && !starsList.isEmpty()){
            for (int i=0; i<starsList.size(); i++){
                if(i <= count-1){
                    starsList.get(i).setImageResource(R.drawable.star);
                }
                else{
                    starsList.get(i).setImageResource(R.drawable.star_unselected);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout fragment_reviews_review_mini_item_rl;
        public ImageView fragment_reviews_review_mini_item_iv;
        public TextView fragment_reviews_review_mini_item_recipe_name_tv;
        public TextView fragment_reviews_review_mini_item_food_type_tv;
        public TextView fragment_reviews_review_mini_item_cuisine_tv;
        public TextView fragment_reviews_review_mini_item_username_tv;
        public TextView fragment_reviews_review_mini_item_review_tv;
        public TextView fragment_reviews_review_mini_item_likes_count_tv;
        public TextView view_pager_recipes_recipe_mini_date_time_tv;
        public ImageView fragment_reviews_review_mini_item_options_iv;
        public ImageView fragment_reviews_review_mini_item_star_1_iv;
        public ImageView fragment_reviews_review_mini_item_star_2_iv;
        public ImageView fragment_reviews_review_mini_item_star_3_iv;
        public ImageView fragment_reviews_review_mini_item_star_4_iv;
        public ImageView fragment_reviews_review_mini_item_star_5_iv;

        public ViewHolder(View view) {
            super(view);
            fragment_reviews_review_mini_item_rl = view.findViewById(R.id.fragment_reviews_review_mini_item_rl);
            fragment_reviews_review_mini_item_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_iv);
            fragment_reviews_review_mini_item_recipe_name_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_recipe_name_tv);
            fragment_reviews_review_mini_item_food_type_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_food_type_tv);
            fragment_reviews_review_mini_item_cuisine_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_cuisine_tv);
            fragment_reviews_review_mini_item_username_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_username_tv);
            fragment_reviews_review_mini_item_review_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_review_tv);
            fragment_reviews_review_mini_item_likes_count_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_likes_count_tv);
            view_pager_recipes_recipe_mini_date_time_tv = view.findViewById(R.id.view_pager_recipes_recipe_mini_date_time_tv);
            fragment_reviews_review_mini_item_options_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_options_iv);
            fragment_reviews_review_mini_item_star_1_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_star_1_iv);
            fragment_reviews_review_mini_item_star_2_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_star_2_iv);
            fragment_reviews_review_mini_item_star_3_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_star_3_iv);
            fragment_reviews_review_mini_item_star_4_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_star_4_iv);
            fragment_reviews_review_mini_item_star_5_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_star_5_iv);
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