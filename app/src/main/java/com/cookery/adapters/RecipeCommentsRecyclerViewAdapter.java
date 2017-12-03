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
import com.cookery.models.CommentMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;

public class RecipeCommentsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeCommentsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeCommentsRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<CommentMO> comments;
    private View.OnClickListener listener;

    public RecipeCommentsRecyclerViewAdapter(Context mContext, List<CommentMO> comments, View.OnClickListener listener) {
        this.mContext = mContext;
        this.comments = comments;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_fragment_recipe_comments_comment_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CommentMO comment = comments.get(position);

        if(comment.getUserImage() != null && !comment.getUserImage().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, comment.getUserImage(), holder.common_fragment_recipe_comments_comment_item_iv);
        }

        //TODO: yet to implement ability for user to like/unlike comment on tapping on the comment heart

        holder.common_fragment_recipe_comments_comment_item_tv.setText(comment.getCOMMENT());
        holder.common_fragment_recipe_comments_comment_item_likes_count_tv.setText(Utility.getSmartNumber(comment.getLikeCount()));
        holder.common_fragment_recipe_comments_comment_item_date_time_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(comment.getCREATE_DTM(), DB_DATE_TIME)));
    }

    @Override
    public int getItemCount() {

        if(comments == null){
            return 0;
        }
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView common_fragment_recipe_comments_comment_item_cv;
        public ImageView common_fragment_recipe_comments_comment_item_iv;
        public TextView common_fragment_recipe_comments_comment_item_tv;
        public TextView common_fragment_recipe_comments_comment_item_likes_count_tv;
        public TextView common_fragment_recipe_comments_comment_item_date_time_tv;

        public ViewHolder(View view) {
            super(view);
            common_fragment_recipe_comments_comment_item_cv = view.findViewById(R.id.common_fragment_recipe_comments_comment_item_cv);
            common_fragment_recipe_comments_comment_item_iv = view.findViewById(R.id.common_fragment_recipe_comments_comment_item_iv);
            common_fragment_recipe_comments_comment_item_tv = view.findViewById(R.id.common_fragment_recipe_comments_comment_item_tv);
            common_fragment_recipe_comments_comment_item_likes_count_tv = view.findViewById(R.id.common_fragment_recipe_comments_comment_item_likes_count_tv);
            common_fragment_recipe_comments_comment_item_date_time_tv = view.findViewById(R.id.common_fragment_recipe_comments_comment_item_date_time_tv);
        }
    }
}