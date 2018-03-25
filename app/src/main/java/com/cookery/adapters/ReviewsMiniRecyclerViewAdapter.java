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
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.ReviewMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.UI_FONT;

public class ReviewsMiniRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsMiniRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = ReviewsMiniRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<ReviewMO> reviews;
    private View.OnClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;

    public ReviewsMiniRecyclerViewAdapter(Context mContext, List<ReviewMO> reviews, View.OnClickListener listener) {
        this.mContext = mContext;
        this.reviews = reviews;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reviews_review_mini_item, parent, false);

        return new ViewHolder(itemView);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position ==  reviews.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        final ReviewMO review = reviews.get(position);

        if(review.getRecipeImages() != null && !review.getRecipeImages().isEmpty()){
            Utility.loadImageFromURL(mContext, review.getRecipeImages().get(0), holder.fragment_reviews_review_mini_item_iv);
        }

        /*stars*/
        List<ImageView> starsList = new ArrayList<>();
        starsList.add(holder.fragment_reviews_review_mini_item_star_1_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_2_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_3_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_4_iv);
        starsList.add(holder.fragment_reviews_review_mini_item_star_5_iv);
        /*stars*/

        holder.fragment_reviews_review_mini_item_recipe_name_tv.setText(review.getRecipeName().toUpperCase());
        holder.fragment_reviews_review_mini_item_food_type_tv.setText(review.getFoodTypeName().toUpperCase());
        holder.fragment_reviews_review_mini_item_cuisine_tv.setText(review.getFoodCuisineName().toUpperCase());
        holder.fragment_reviews_review_mini_item_likes_count_tv.setText(String.valueOf(review.getLikesCount()));

        holder.fragment_reviews_review_mini_item_review_tv.setText(review.getREVIEW());
        setStars(starsList, review.getRATING());

        holder.fragment_reviews_review_mini_item_date_time_tv.setText(DateTimeUtility.getCreateOrModifiedTime(review.getCREATE_DTM(), review.getMOD_DTM()));

        Utility.loadImageFromURL(mContext, review.getRecipeOwnerImage(), holder.fragment_reviews_review_mini_item_username_iv);
        holder.fragment_reviews_review_mini_item_username_tv.setText(review.getRecipeOwnerName());

        holder.fragment_reviews_review_mini_item_rl.setTag(review);
        holder.fragment_reviews_review_mini_item_rl.setOnClickListener(listener);
        holder.fragment_reviews_review_mini_item_options_iv.setOnClickListener(listener);

        setFont(holder.fragment_reviews_review_mini_item_rl);
    }

    public void updateReviews(List<ReviewMO> myReviews, int index) {
        if(reviews == null){
            reviews = new ArrayList<>();
        }

        if(index == 0){
            reviews.clear();
        }

        reviews.addAll(myReviews);
        notifyDataSetChanged();
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
        public CircleImageView fragment_reviews_review_mini_item_username_iv;
        public TextView fragment_reviews_review_mini_item_recipe_name_tv;
        public TextView fragment_reviews_review_mini_item_food_type_tv;
        public TextView fragment_reviews_review_mini_item_cuisine_tv;
        public TextView fragment_reviews_review_mini_item_username_tv;
        public TextView fragment_reviews_review_mini_item_review_tv;
        public TextView fragment_reviews_review_mini_item_likes_count_tv;
        public TextView fragment_reviews_review_mini_item_date_time_tv;
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
            fragment_reviews_review_mini_item_username_iv = view.findViewById(R.id.fragment_reviews_review_mini_item_username_iv);
            fragment_reviews_review_mini_item_recipe_name_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_recipe_name_tv);
            fragment_reviews_review_mini_item_food_type_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_food_type_tv);
            fragment_reviews_review_mini_item_cuisine_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_cuisine_tv);
            fragment_reviews_review_mini_item_username_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_username_tv);
            fragment_reviews_review_mini_item_review_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_review_tv);
            fragment_reviews_review_mini_item_likes_count_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_likes_count_tv);
            fragment_reviews_review_mini_item_date_time_tv = view.findViewById(R.id.fragment_reviews_review_mini_item_date_time_tv);
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