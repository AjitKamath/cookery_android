package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.ReviewMO;
import com.cookery.utils.Utility;

import java.util.List;

public class RecipeReviewsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeReviewsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeReviewsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<ReviewMO> reviews;
    private View.OnClickListener listener;

    public RecipeReviewsRecyclerViewAdapter(Context mContext, List<ReviewMO> reviews) {
        this.mContext = mContext;
        this.reviews = reviews;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_recipe_reviews_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReviewMO review = reviews.get(position);

        if(review.getUserImage() != null && !review.getUserImage().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, review.getUserImage(), holder.view_pager_recipe_reviews_item_iv);
        }

        holder.view_pager_recipe_reviews_item_tv.setText(review.getREVIEW());
        holder.view_pager_recipe_reviews_item_likes_count_tv.setText(String.valueOf(review.getLikeCount()));
    }

    @Override
    public int getItemCount() {

        if(reviews == null){
            return 0;
        }
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView view_pager_recipe_reviews_item_cv;
        public ImageView view_pager_recipe_reviews_item_iv;
        public TextView view_pager_recipe_reviews_item_tv;
        public TextView view_pager_recipe_reviews_item_likes_count_tv;
        public TextView view_pager_recipe_reviews_item_date_time_tv;

        public ViewHolder(View view) {
            super(view);
            view_pager_recipe_reviews_item_cv = view.findViewById(R.id.view_pager_recipe_reviews_item_cv);
            view_pager_recipe_reviews_item_iv = view.findViewById(R.id.view_pager_recipe_reviews_item_iv);
            view_pager_recipe_reviews_item_tv = view.findViewById(R.id.view_pager_recipe_reviews_item_tv);
            view_pager_recipe_reviews_item_likes_count_tv = view.findViewById(R.id.view_pager_recipe_reviews_item_likes_count_tv);
            view_pager_recipe_reviews_item_date_time_tv = view.findViewById(R.id.view_pager_recipe_reviews_item_date_time_tv);
        }
    }
}