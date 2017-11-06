package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.TIMELINE_COMMENT_RECIPE_ADD;
import static com.cookery.utils.Constants.TIMELINE_COMMENT_RECIPE_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_LIKE_COMMENT_ADD;
import static com.cookery.utils.Constants.TIMELINE_LIKE_COMMENT_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_LIKE_RECIPE_ADD;
import static com.cookery.utils.Constants.TIMELINE_LIKE_RECIPE_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_LIKE_REVIEW_ADD;
import static com.cookery.utils.Constants.TIMELINE_LIKE_REVIEW_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_RECIPE_ADD;
import static com.cookery.utils.Constants.TIMELINE_RECIPE_MODIFY;
import static com.cookery.utils.Constants.TIMELINE_RECIPE_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_REVIEW_RECIPE_ADD;
import static com.cookery.utils.Constants.TIMELINE_REVIEW_RECIPE_REMOVE;
import static com.cookery.utils.Constants.TIMELINE_USER_ADD;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

public class TimelinesRecyclerViewAdapter extends RecyclerView.Adapter<TimelinesRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = TimelinesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<TimelineMO> timelines;
    private View.OnClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;
    private UserMO loggedInUser;

    public TimelinesRecyclerViewAdapter(Context mContext, List<TimelineMO> timelines, View.OnClickListener listener) {
        this.mContext = mContext;
        this.timelines = timelines;
        this.listener = listener;

        this.loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new ViewHolder(itemView, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        TimelineMO timeline = timelines.get(position);

        if(timeline == null || timeline.getTMLN_ID() == 0){
            Log.e(CLASS_NAME, "Empty/null timeline/tmln_id");
            return -1;
        }

        if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            return R.layout.fragment_timelines_timeline_recipe;
        }
        else if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE()) ||
                TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(timeline.getTYPE()) ||
                TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            return R.layout.fragment_timelines_timeline_like;
        }
        else if(TIMELINE_COMMENT_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_COMMENT_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            return R.layout.fragment_timelines_timeline_comment;
        }
        else if(TIMELINE_REVIEW_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_REVIEW_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            return R.layout.fragment_timelines_timeline_review;
        }
        else if(TIMELINE_USER_ADD.equalsIgnoreCase(timeline.getTYPE())){
            return R.layout.fragment_timelines_timeline_user;
        }
        else{
            Log.e(CLASS_NAME, "The timeline type("+timeline.getTYPE()+") could not be understood. New type of timeline ?");
            return -1;
        }
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public void updateTopTimelines(List<TimelineMO> timelines){
        List<TimelineMO> temp = new ArrayList<>(timelines);
        temp.addAll(this.timelines);
        this.timelines = temp;

        notifyDataSetChanged();
    }

    public void updateBottomTimelines(List<TimelineMO> timelines){
        if(this.timelines == null){
            this.timelines = new ArrayList<>();
        }

        this.timelines.addAll(timelines);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TimelineMO timeline = timelines.get(position);

        //commons
        if(timeline.getWhoUserImage() != null || !timeline.getWhoUserImage().trim().isEmpty()){
            Picasso.with(mContext).load(timeline.getWhoUserImage()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_round_image_mini_iv);
        }
        holder.common_component_text_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(timeline.getCREATE_DTM(), DB_DATE_TIME)));
        //commons

        if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            holder.fragment_timelines_timeline_recipe_recipe_name_tv.setText(timeline.getRecipeName().toUpperCase());
            holder.fragment_timelines_timeline_recipe_recipe_type_tv.setText(timeline.getRecipeTypeName().toUpperCase());
            holder.fragment_timelines_timeline_recipe_recipe_cuisine_tv.setText(timeline.getRecipeCuisineName().toUpperCase());

            if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText("You posted a Recipe !");
            }
            else if(TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText("You modified your Recipe !");
            }
            else if(TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText("You deleted your Recipe !");
            }

            /*recipe*/
            if(timeline.getRecipeImage() != null || !timeline.getRecipeImage().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeImage()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_card_timeline_recipe_recipe_iv);
            }


            holder.common_component_card_timeline_recipe_recipe_name_tv.setText(timeline.getRecipeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_type_tv.setText(timeline.getRecipeTypeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_cuisine_tv.setText(timeline.getRecipeCuisineName().toUpperCase());
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_recipe_rl);
        }
        else if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE()) ||
                TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(timeline.getTYPE()) ||
                TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(timeline.getTYPE())){

            if(timeline.getWhoUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_like_who_tv.setText(timeline.getWhoName());
            }

            if(timeline.getWhoseUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_like_whose_tv.setText(timeline.getWhoseName()+"'s");
            }

            /*like/unlike*/
            if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_like_what_tv.setText("liked");
            }
            else if(TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_like_what_tv.setText("unliked");
            }
            /*like/unlike*/

            if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Recipe");
                holder.fragment_timelines_timeline_like_review_tv.setVisibility(View.GONE);
            }
            else if(TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Comment");
                holder.fragment_timelines_timeline_like_review_tv.setText(timeline.getComment());
            }
            else if(TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Review");
                holder.fragment_timelines_timeline_like_review_tv.setText(timeline.getReview());
            }

            /*recipe*/
            if(timeline.getRecipeOwnerImg() != null || !timeline.getRecipeOwnerImg().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeOwnerImg()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_round_image_micro_iv);
            }

            if(timeline.getRecipeImage() != null || !timeline.getRecipeImage().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeImage()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_card_timeline_recipe_recipe_iv);
            }


            holder.common_component_card_timeline_recipe_recipe_name_tv.setText(timeline.getRecipeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_type_tv.setText(timeline.getRecipeTypeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_cuisine_tv.setText(timeline.getRecipeCuisineName().toUpperCase());
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_like_rl);
        }
        else if(TIMELINE_COMMENT_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_COMMENT_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            if(timeline.getWhoUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_like_who_tv.setText(timeline.getWhoName());
            }

            if(timeline.getWhoseUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_like_whose_tv.setText(timeline.getWhoseName()+"'s");
            }

            holder.fragment_timelines_timeline_comment_comment_tv.setText(timeline.getComment());

            /*recipe*/
            if(timeline.getRecipeOwnerImg() != null || !timeline.getRecipeOwnerImg().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeOwnerImg()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_round_image_micro_iv);
            }

            if(timeline.getRecipeImage() != null || !timeline.getRecipeImage().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeImage()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_card_timeline_recipe_recipe_iv);
            }


            holder.common_component_card_timeline_recipe_recipe_name_tv.setText(timeline.getRecipeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_type_tv.setText(timeline.getRecipeTypeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_cuisine_tv.setText(timeline.getRecipeCuisineName().toUpperCase());
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_comment_rl);
        }
        else if(TIMELINE_REVIEW_RECIPE_ADD.equalsIgnoreCase(timeline.getTYPE()) || TIMELINE_REVIEW_RECIPE_REMOVE.equalsIgnoreCase(timeline.getTYPE())){
            if(timeline.getWhoUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_like_who_tv.setText(timeline.getWhoName());
            }

            if(timeline.getWhoseUserId() == loggedInUser.getUser_id()){
                holder.fragment_timelines_timeline_like_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_like_whose_tv.setText(timeline.getWhoseName()+"'s");
            }

            /*review*/
            holder.fragment_timelines_timeline_review_review_tv.setText(timeline.getReview());

            List<ImageView> starsList = new ArrayList<>();
            starsList.add(holder.fragment_timelines_timeline_review_star1_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star2_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star3_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star4_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star5_iv);

            setStars(starsList, timeline.getRating());
            /*review*/

            /*recipe*/
            if(timeline.getRecipeOwnerImg() != null || !timeline.getRecipeOwnerImg().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeOwnerImg()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_round_image_micro_iv);
            }

            if(timeline.getRecipeImage() != null || !timeline.getRecipeImage().trim().isEmpty()){
                Picasso.with(mContext).load(timeline.getRecipeImage()).placeholder(Utility.getPlaceHolder()).into(holder.common_component_card_timeline_recipe_recipe_iv);
            }


            holder.common_component_card_timeline_recipe_recipe_name_tv.setText(timeline.getRecipeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_type_tv.setText(timeline.getRecipeTypeName().toUpperCase());
            holder.common_component_card_timeline_recipe_recipe_cuisine_tv.setText(timeline.getRecipeCuisineName().toUpperCase());
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_review_rl);
        }
        else if(TIMELINE_USER_ADD.equalsIgnoreCase(timeline.getTYPE())){
            setFont(holder.fragment_timelines_timeline_user_rl);
        }
        else{
            Log.e(CLASS_NAME, "The timeline type("+timeline.getTYPE()+") could not be understood. New type of timeline ?");
        }
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
        return timelines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /*common*/
        public CircleImageView common_component_round_image_mini_iv;
        public TextView common_component_text_datetime_tv;
        public ImageView common_component_image_options_mini_iv;
        public CircleImageView common_component_round_image_micro_iv;
        public TextView common_component_card_timeline_recipe_recipe_name_tv;
        public TextView common_component_card_timeline_recipe_recipe_type_tv;
        public TextView common_component_card_timeline_recipe_recipe_cuisine_tv;
        public ImageView common_component_card_timeline_recipe_recipe_iv;
        /*common*/

        /*fragment_timelines_timeline_comment.xml*/
        public RelativeLayout fragment_timelines_timeline_comment_rl;
        public TextView fragment_timelines_timeline_comment_who_tv;
        public TextView fragment_timelines_timeline_comment_what_tv;
        public TextView fragment_timelines_timeline_comment_whose_tv;
        public TextView fragment_timelines_timeline_comment_comment_tv;
        /*fragment_timelines_timeline_comment.xml*/

        /*fragment_timelines_timeline_like.xml*/
        public RelativeLayout fragment_timelines_timeline_like_rl;
        public TextView fragment_timelines_timeline_like_who_tv;
        public TextView fragment_timelines_timeline_like_what_tv;
        public TextView fragment_timelines_timeline_like_whose_tv;
        public TextView fragment_timelines_timeline_like_on_tv;
        public TextView fragment_timelines_timeline_like_review_tv;
        /*fragment_timelines_timeline_like.xml*/

        /*fragment_timelines_timeline_recipe.xml*/
        public RelativeLayout fragment_timelines_timeline_recipe_rl;
        public TextView fragment_timelines_timeline_recipe_msg_tv;
        public TextView fragment_timelines_timeline_recipe_recipe_name_tv;
        public TextView fragment_timelines_timeline_recipe_recipe_type_tv;
        public TextView fragment_timelines_timeline_recipe_recipe_cuisine_tv;
        public ImageView fragment_timelines_timeline_recipe_recipe_iv;
        /*fragment_timelines_timeline_recipe.xml*/

        /*fragment_timelines_timeline_review.xml*/
        public RelativeLayout fragment_timelines_timeline_review_rl;
        public TextView fragment_timelines_timeline_review_who_tv;
        public TextView fragment_timelines_timeline_review_what_tv;
        public TextView fragment_timelines_timeline_review_whose_tv;
        public TextView fragment_timelines_timeline_review_review_tv;
        public ImageView fragment_timelines_timeline_review_star1_iv;
        public ImageView fragment_timelines_timeline_review_star2_iv;
        public ImageView fragment_timelines_timeline_review_star3_iv;
        public ImageView fragment_timelines_timeline_review_star4_iv;
        public ImageView fragment_timelines_timeline_review_star5_iv;
        /*fragment_timelines_timeline_review.xml*/

        /*fragment_timelines_timeline_user.xml*/
        public RelativeLayout fragment_timelines_timeline_user_rl;
        public TextView fragment_timelines_timeline_user_msg_tv;
        /*fragment_timelines_timeline_user.xml*/

        public ViewHolder(View view, int layout) {
            super(view);

            /*commons*/
            common_component_round_image_mini_iv = view.findViewById(R.id.common_component_round_image_mini_iv);
            common_component_text_datetime_tv = view.findViewById(R.id.common_component_text_datetime_tv);
            common_component_image_options_mini_iv = view.findViewById(R.id.common_component_image_options_mini_iv);
            common_component_round_image_micro_iv = view.findViewById(R.id.common_component_round_image_micro_iv);
            common_component_card_timeline_recipe_recipe_name_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_name_tv);
            common_component_card_timeline_recipe_recipe_type_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_type_tv);
            common_component_card_timeline_recipe_recipe_cuisine_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_cuisine_tv);
            common_component_card_timeline_recipe_recipe_iv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_iv);
            /*commons*/

            if(R.layout.fragment_timelines_timeline_comment == layout){
                fragment_timelines_timeline_comment_rl = view.findViewById(R.id.fragment_timelines_timeline_comment_rl);
                fragment_timelines_timeline_comment_who_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_who_tv);
                fragment_timelines_timeline_comment_comment_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_comment_tv);
                fragment_timelines_timeline_comment_what_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_what_tv);
                fragment_timelines_timeline_comment_whose_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_whose_tv);
            }
            else if(R.layout.fragment_timelines_timeline_like == layout){
                fragment_timelines_timeline_like_rl = view.findViewById(R.id.fragment_timelines_timeline_like_rl);
                fragment_timelines_timeline_like_who_tv = view.findViewById(R.id.fragment_timelines_timeline_like_who_tv);
                fragment_timelines_timeline_like_what_tv = view.findViewById(R.id.fragment_timelines_timeline_like_what_tv);
                fragment_timelines_timeline_like_whose_tv = view.findViewById(R.id.fragment_timelines_timeline_like_whose_tv);
                fragment_timelines_timeline_like_on_tv = view.findViewById(R.id.fragment_timelines_timeline_like_on_tv);
                fragment_timelines_timeline_like_review_tv = view.findViewById(R.id.fragment_timelines_timeline_like_review_tv);
            }
            else if(R.layout.fragment_timelines_timeline_recipe == layout){
                fragment_timelines_timeline_recipe_rl = view.findViewById(R.id.fragment_timelines_timeline_recipe_rl);
                fragment_timelines_timeline_recipe_msg_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_msg_tv);
                fragment_timelines_timeline_recipe_recipe_name_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_name_tv);
                fragment_timelines_timeline_recipe_recipe_type_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_type_tv);
                fragment_timelines_timeline_recipe_recipe_cuisine_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_cuisine_tv);
                fragment_timelines_timeline_recipe_recipe_iv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_iv);
            }
            else if(R.layout.fragment_timelines_timeline_review == layout){
                fragment_timelines_timeline_review_rl = view.findViewById(R.id.fragment_timelines_timeline_review_rl);
                fragment_timelines_timeline_review_who_tv = view.findViewById(R.id.fragment_timelines_timeline_review_who_tv);
                fragment_timelines_timeline_review_what_tv = view.findViewById(R.id.fragment_timelines_timeline_review_what_tv);
                fragment_timelines_timeline_review_review_tv = view.findViewById(R.id.fragment_timelines_timeline_review_review_tv);
                fragment_timelines_timeline_review_whose_tv = view.findViewById(R.id.fragment_timelines_timeline_review_whose_tv);
                fragment_timelines_timeline_review_star1_iv = view.findViewById(R.id.fragment_timelines_timeline_review_star1_iv);
                fragment_timelines_timeline_review_star2_iv = view.findViewById(R.id.fragment_timelines_timeline_review_star2_iv);
                fragment_timelines_timeline_review_star3_iv = view.findViewById(R.id.fragment_timelines_timeline_review_star3_iv);
                fragment_timelines_timeline_review_star4_iv = view.findViewById(R.id.fragment_timelines_timeline_review_star4_iv);
                fragment_timelines_timeline_review_star5_iv = view.findViewById(R.id.fragment_timelines_timeline_review_star5_iv);
            }
            else if(R.layout.fragment_timelines_timeline_user == layout){
                fragment_timelines_timeline_user_rl = view.findViewById(R.id.fragment_timelines_timeline_user_rl);
                fragment_timelines_timeline_user_msg_tv = view.findViewById(R.id.fragment_timelines_timeline_user_msg_tv);
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE);
            }
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