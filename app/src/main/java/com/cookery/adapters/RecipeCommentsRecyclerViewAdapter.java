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
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.CommentMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

public class RecipeCommentsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCommentsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeCommentsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<CommentMO> comments;
    private View.OnClickListener listener;
    private UserMO loggedInUser;

    public RecipeCommentsRecyclerViewAdapter(Context mContext, UserMO loggedInUser, List<CommentMO> comments, View.OnClickListener listener) {
        this.mContext = mContext;
        this.loggedInUser = loggedInUser;
        this.comments = comments;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_comments_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CommentMO comment = comments.get(position);

        if(comment.getUserImage() != null && !comment.getUserImage().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, comment.getUserImage(), holder.recipe_comments_item_iv);
        }

        holder.recipe_comments_item_username_tv.setText(comment.getName().trim());

        if(loggedInUser.getUser_id() == comment.getUSER_ID()){
            holder.recipe_comments_item_delete_iv.setVisibility(View.VISIBLE);
        }
        else{
            holder.recipe_comments_item_delete_iv.setVisibility(View.GONE);
        }

        if(comment.isUserLiked()){
            holder.recipe_comments_likes_iv.setImageResource(R.drawable.heart);
        }
        else{
            holder.recipe_comments_likes_iv.setImageResource(R.drawable.heart_unselected);
        }

        //TODO: yet to implement ability for user to like/unlike comment on tapping on the comment heart

        holder.recipe_comments_item_tv.setText(comment.getCOMMENT());
        holder.recipe_comments_item_likes_count_tv.setText(Utility.getSmartNumber(comment.getLikeCount()));

        if(comment.getMOD_DTM() != null && !comment.getMOD_DTM().trim().isEmpty()){
            holder.recipe_comments_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(comment.getMOD_DTM(), DB_DATE_TIME)));
        }
        else{
            holder.recipe_comments_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(comment.getCREATE_DTM(), DB_DATE_TIME)));
        }

        setFont(holder.recipe_comments_item_cv);
    }

    @Override
    public int getItemCount() {

        if(comments == null){
            return 0;
        }
        return comments.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView recipe_comments_item_cv;
        public ImageView recipe_comments_item_iv;
        public TextView recipe_comments_item_username_tv;
        public ImageView recipe_comments_item_delete_iv;
        public TextView recipe_comments_item_tv;
        public ImageView recipe_comments_likes_iv;
        public TextView recipe_comments_item_likes_count_tv;
        public TextView recipe_comments_item_date_time_tv;

        public ViewHolder(View view) {
            super(view);
            recipe_comments_item_cv = view.findViewById(R.id.recipe_comments_item_cv);
            recipe_comments_item_iv = view.findViewById(R.id.recipe_comments_item_iv);
            recipe_comments_item_username_tv = view.findViewById(R.id.recipe_comments_item_username_tv);
            recipe_comments_item_delete_iv = view.findViewById(R.id.recipe_comments_item_delete_iv);
            recipe_comments_item_tv = view.findViewById(R.id.recipe_comments_item_tv);
            recipe_comments_likes_iv = view.findViewById(R.id.recipe_comments_likes_iv);
            recipe_comments_item_likes_count_tv = view.findViewById(R.id.recipe_comments_item_likes_count_tv);
            recipe_comments_item_date_time_tv = view.findViewById(R.id.recipe_comments_item_date_time_tv);
        }
    }
}