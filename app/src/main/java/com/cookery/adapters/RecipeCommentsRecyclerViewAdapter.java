package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
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
import com.cookery.models.CommentMO;
import com.cookery.models.LikesMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

public class RecipeCommentsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCommentsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeCommentsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<CommentMO> comments;
    private View.OnClickListener listener;
    private View.OnLongClickListener longClickListener;
    private OnBottomReachedListener onBottomReachedListener;
    private UserMO loggedInUser;

    public RecipeCommentsRecyclerViewAdapter(Context mContext, UserMO loggedInUser, List<CommentMO> comments, View.OnClickListener listener, View.OnLongClickListener longClickListener) {
        this.mContext = mContext;
        this.loggedInUser = loggedInUser;
        this.comments = comments;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_comments_item, parent, false);

        return new ViewHolder(itemView);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position ==  comments.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        final CommentMO comment = comments.get(position);

        if(comment.getUserImage() != null && !comment.getUserImage().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, comment.getUserImage(), holder.recipe_comments_item_iv);
        }

        holder.recipe_comments_item_username_tv.setText(comment.getUserName().trim());

        if(loggedInUser.getUSER_ID() == comment.getUSER_ID()){
            holder.recipe_comments_item_delete_iv.setVisibility(View.VISIBLE);

            holder.recipe_comments_item_delete_iv.setTag(comment);
            holder.recipe_comments_item_delete_iv.setOnClickListener(listener);
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

        holder.recipe_comments_item_tv.setText(comment.getCOMMENT());
        holder.recipe_comments_item_likes_count_tv.setText(Utility.getSmartNumber(comment.getLikesCount()));

        if(comment.getMOD_DTM() != null && !comment.getMOD_DTM().trim().isEmpty()){
            holder.recipe_comments_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(comment.getMOD_DTM(), DB_DATE_TIME)));
        }
        else{
            holder.recipe_comments_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(comment.getCREATE_DTM(), DB_DATE_TIME)));
        }

        holder.recipe_comments_likes_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("COMMENT");
                like.setTYPE_ID(comment.getCOM_ID());

                new AsyncSubmitLike().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like, holder);
            }
        });

        holder.recipe_comments_likes_iv.setTag(comment);
        holder.recipe_comments_likes_iv.setOnLongClickListener(longClickListener);

        setFont(holder.recipe_comments_item_cv);
    }

    private void setupLike(ViewHolder holder, LikesMO like){
        if(like.isLiked()){
            holder.recipe_comments_likes_iv.setImageResource(R.drawable.heart);
        }
        else{
            holder.recipe_comments_likes_iv.setImageResource(R.drawable.heart_unselected);
        }

        holder.recipe_comments_item_likes_count_tv.setText(String.valueOf(like.getLikes()));
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

    public void updateComments(List<CommentMO> newComments, int index) {
        if(comments == null){
            comments = new ArrayList<>();
        }

        if(index == 0){
            comments.clear();
        }

        comments.addAll(newComments);
        notifyDataSetChanged();
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