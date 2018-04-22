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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.ReviewMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;

public class RecipeViewReviewsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewReviewsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeViewReviewsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<ReviewMO> reviews;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private OnBottomReachedListener onBottomReachedListener;

    public RecipeViewReviewsRecyclerViewAdapter(Context mContext, List<ReviewMO> reviews, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        this.mContext = mContext;
        this.reviews = reviews;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_reviews_item, parent, false);

        return new ViewHolder(itemView);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == reviews.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        final ReviewMO review = reviews.get(position);

        Utility.loadImageFromURL(mContext, review.getUserImage(), holder.recipe_reviews_item_iv);

        holder.recipe_reviews_item_user_name_tv.setText(review.getUserName());
        holder.recipe_reviews_item_tv.setText(review.getREVIEW());

        Utility.setupLikeView(holder.common_like_view, review.isUserLiked(), review.getLikesCount());

        setupStars(holder, review);

        if(review.getMOD_DTM() != null && !review.getMOD_DTM().trim().isEmpty()){
            holder.recipe_reviews_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(review.getMOD_DTM(), DB_DATE_TIME)));
        }
        else{
            holder.recipe_reviews_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(review.getCREATE_DTM(), DB_DATE_TIME)));
        }

        holder.common_like_view.setOnClickListener(listener);
        holder.common_like_view.setOnLongClickListener(longClickListener);

        holder.common_like_view.setTag(review);
    }

    private void setupStars(ViewHolder holder, final ReviewMO review) {
        List<ImageView> stars = new ArrayList<>();
        stars.add(holder.recipe_reviews_item_star_1_iv);
        stars.add(holder.recipe_reviews_item_star_2_iv);
        stars.add(holder.recipe_reviews_item_star_3_iv);
        stars.add(holder.recipe_reviews_item_star_4_iv);
        stars.add(holder.recipe_reviews_item_star_5_iv);

        for(int i=0; i<stars.size(); i++){
            if(i < review.getRATING()){
                stars.get(i).setImageResource(R.drawable.star);
            }
            else{
                stars.get(i).setImageResource(R.drawable.star_unselected);
            }
        }
    }


    @Override
    public int getItemCount() {

        if(reviews == null){
            return 0;
        }
        return reviews.size();
    }

    public void updateReviews(List<ReviewMO> newReviews, int index) {
        if(reviews == null){
            reviews = new ArrayList<>();
        }

        if(index == 0){
            reviews.clear();
        }

        reviews.addAll(newReviews);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView recipe_reviews_item_cv;
        public ImageView recipe_reviews_item_iv;
        public TextView recipe_reviews_item_user_name_tv;
        public TextView recipe_reviews_item_tv;
        public LinearLayout common_like_view;
        public ImageView recipe_reviews_item_star_1_iv;
        public ImageView recipe_reviews_item_star_2_iv;
        public ImageView recipe_reviews_item_star_3_iv;
        public ImageView recipe_reviews_item_star_4_iv;
        public ImageView recipe_reviews_item_star_5_iv;
        public TextView recipe_reviews_item_date_time_tv;

        public ViewHolder(View view) {
            super(view);
            recipe_reviews_item_cv = view.findViewById(R.id.recipe_reviews_item_cv);
            recipe_reviews_item_iv = view.findViewById(R.id.recipe_reviews_item_iv);
            recipe_reviews_item_user_name_tv = view.findViewById(R.id.recipe_reviews_item_user_name_tv);
            recipe_reviews_item_tv = view.findViewById(R.id.recipe_reviews_item_tv);
            common_like_view = view.findViewById(R.id.common_like_view);
            recipe_reviews_item_star_1_iv = view.findViewById(R.id.recipe_reviews_item_star_1_iv);
            recipe_reviews_item_star_2_iv = view.findViewById(R.id.recipe_reviews_item_star_2_iv);
            recipe_reviews_item_star_3_iv = view.findViewById(R.id.recipe_reviews_item_star_3_iv);
            recipe_reviews_item_star_4_iv = view.findViewById(R.id.recipe_reviews_item_star_4_iv);
            recipe_reviews_item_star_5_iv = view.findViewById(R.id.recipe_reviews_item_star_5_iv);
            recipe_reviews_item_date_time_tv = view.findViewById(R.id.recipe_reviews_item_date_time_tv);
        }
    }
}