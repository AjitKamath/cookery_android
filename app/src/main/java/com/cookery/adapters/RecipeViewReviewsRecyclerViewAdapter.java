package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.LikesMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;

public class RecipeViewReviewsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewReviewsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeViewReviewsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<ReviewMO> reviews;
    private View.OnLongClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;
    private UserMO loggedInUser;

    public RecipeViewReviewsRecyclerViewAdapter(Context mContext, UserMO loggedInUser, List<ReviewMO> reviews, View.OnLongClickListener listener) {
        this.mContext = mContext;
        this.loggedInUser = loggedInUser;
        this.reviews = reviews;
        this.listener = listener;
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
        holder.recipe_reviews_item_likes_count_tv.setText(String.valueOf(review.getLikesCount()));

        if(review.isUserLiked()){
            holder.recipe_reviews_likes_iv.setImageResource(R.drawable.heart);
        }
        else{
            holder.recipe_reviews_likes_iv.setImageResource(R.drawable.heart_unselected);
        }

        setupStars(holder, review);

        if(review.getMOD_DTM() != null && !review.getMOD_DTM().trim().isEmpty()){
            holder.recipe_reviews_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(review.getMOD_DTM(), DB_DATE_TIME)));
        }
        else{
            holder.recipe_reviews_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(review.getCREATE_DTM(), DB_DATE_TIME)));
        }

        holder.recipe_reviews_likes_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("REVIEW");
                like.setTYPE_ID(review.getREV_ID());

                new AsyncSubmitLike().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like, holder);
            }
        });

        holder.recipe_reviews_likes_iv.setTag(review);
        holder.recipe_reviews_likes_iv.setOnLongClickListener(listener);
    }

    private void setupLike(ViewHolder holder, LikesMO like){
        if(like.isUserLiked()){
            holder.recipe_reviews_likes_iv.setImageResource(R.drawable.heart);
        }
        else{
            holder.recipe_reviews_likes_iv.setImageResource(R.drawable.heart_unselected);
        }

        holder.recipe_reviews_item_likes_count_tv.setText(String.valueOf(like.getLikesCount()));
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
        public ImageView recipe_reviews_likes_iv;
        public TextView recipe_reviews_item_likes_count_tv;
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
            recipe_reviews_likes_iv = view.findViewById(R.id.recipe_reviews_likes_iv);
            recipe_reviews_item_likes_count_tv = view.findViewById(R.id.recipe_reviews_item_likes_count_tv);
            recipe_reviews_item_star_1_iv = view.findViewById(R.id.recipe_reviews_item_star_1_iv);
            recipe_reviews_item_star_2_iv = view.findViewById(R.id.recipe_reviews_item_star_2_iv);
            recipe_reviews_item_star_3_iv = view.findViewById(R.id.recipe_reviews_item_star_3_iv);
            recipe_reviews_item_star_4_iv = view.findViewById(R.id.recipe_reviews_item_star_4_iv);
            recipe_reviews_item_star_5_iv = view.findViewById(R.id.recipe_reviews_item_star_5_iv);
            recipe_reviews_item_date_time_tv = view.findViewById(R.id.recipe_reviews_item_date_time_tv);
        }
    }

    public class AsyncSubmitLike extends AsyncTask<Object, Void, Object> {
        private ViewHolder holder;

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Object doInBackground(Object... objects) {
            LikesMO like = (LikesMO) objects[0];
            holder = (ViewHolder) objects[1];


            return InternetUtility.submitLike(like);
        }

        @Override
        protected void onPostExecute(Object object) {
            LikesMO like = (LikesMO) object;

            if(like != null){
                setupLike(holder, like);
            }
        }
    }
}