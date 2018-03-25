package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
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
import com.cookery.models.RecipeMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.lang.reflect.Field;
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
import static com.cookery.utils.Constants.TIMELINE_USER_FOLLOW;
import static com.cookery.utils.Constants.TIMELINE_USER_PHOTO_MODIFY;
import static com.cookery.utils.Constants.TIMELINE_USER_UNFOLLOW;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

public class HomeStoriesRecyclerViewAdapter extends RecyclerView.Adapter<HomeStoriesRecyclerViewAdapter.ViewHolder> {
    private static final String CLASS_NAME = HomeStoriesRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<TimelineMO> timelines;
    private View.OnClickListener listener;
    private PopupMenu.OnMenuItemClickListener menuItemClickListener;
    private OnBottomReachedListener onBottomReachedListener;
    private UserMO loggedInUser;

    public HomeStoriesRecyclerViewAdapter(Context mContext, List<TimelineMO> timelines, View.OnClickListener listener, PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        this.mContext = mContext;
        this.timelines = timelines;
        this.listener = listener;
        this.menuItemClickListener = menuItemClickListener;

        this.loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new ViewHolder(itemView, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        TimelineMO story = timelines.get(position);

        if(story == null || story.getTMLN_ID() == 0){
            Log.e(CLASS_NAME, "Empty/null story/tmln_id");
            return -1;
        }

        if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(story.getTYPE()) || TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_recipe_item;
        }
        else if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE()) ||
                TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(story.getTYPE()) ||
                TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_like_item;
        }
        else if(TIMELINE_COMMENT_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_COMMENT_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_comment_item;
        }
        else if(TIMELINE_REVIEW_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_REVIEW_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_review_item;
        }
        else if(TIMELINE_USER_ADD.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_user_item;
        }
        else if(TIMELINE_USER_PHOTO_MODIFY.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_user_image_modify_item;
        }
        else if(TIMELINE_USER_FOLLOW.equalsIgnoreCase(story.getTYPE()) || TIMELINE_USER_UNFOLLOW.equalsIgnoreCase(story.getTYPE())){
            return R.layout.home_timeline_user_follow_unfollow_item;
        }
        else{
            Log.e(CLASS_NAME, "The story type("+story.getTYPE()+") could not be understood. New type of story ?");

            return R.layout.home_timeline_unknown_item;
        }
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public void updateTopStories(List<TimelineMO> timelines){
        List<TimelineMO> temp = new ArrayList<>(timelines);
        temp.addAll(this.timelines);
        this.timelines = temp;

        notifyDataSetChanged();
    }

    public void updateBottomStories(List<TimelineMO> timelines){
        if(this.timelines == null){
            this.timelines = new ArrayList<>();
        }

        this.timelines.addAll(timelines);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == timelines.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        setupLayout(holder, timelines.get(position));
    }

    private void setupLayout(ViewHolder holder, final TimelineMO story){
        if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(story.getTYPE()) || TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            setupRecipeOnClickListener(holder.fragment_timelines_timeline_recipe_recipe_cv, story.getRecipeId());

            if(story.getRecipeName() != null){
                holder.fragment_timelines_timeline_recipe_recipe_name_tv.setText(story.getRecipeName().toUpperCase());
            }
            else{
                Log.e(CLASS_NAME, "Error ! Possible data corruption due to non enforced foreign key violation ! tmln_id("+story.getTMLN_ID()+") type("+story.getTYPE()+") type_id("+story.getTYPE_ID()+")");
            }

            if(story.getRecipeTypeName() != null){
                holder.fragment_timelines_timeline_recipe_recipe_type_tv.setText(story.getRecipeTypeName().toUpperCase());
            }
            else{
                Log.e(CLASS_NAME, "Error ! Possible data corruption due to non enforced foreign key violation ! tmln_id("+story.getTMLN_ID()+") type("+story.getTYPE()+") type_id("+story.getTYPE_ID()+")");
            }

            if(story.getRecipeCuisineName() != null){
                holder.fragment_timelines_timeline_recipe_recipe_cuisine_tv.setText(story.getRecipeCuisineName().toUpperCase());
            }
            else{
                Log.e(CLASS_NAME, "Error ! Possible data corruption due to non enforced foreign key violation ! tmln_id("+story.getTMLN_ID()+") type("+story.getTYPE()+") type_id("+story.getTYPE_ID()+")");
            }

            if(TIMELINE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText(story.getWhoName()+" posted a Recipe !");
            }
            else if(TIMELINE_RECIPE_MODIFY.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText(story.getWhoName()+" modified the Recipe !");
            }
            else if(TIMELINE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_recipe_msg_tv.setText(story.getWhoName()+" deleted the Recipe !");
            }

            if(story.getRecipeImage() != null && !story.getRecipeImage().trim().isEmpty()){
                if(holder.fragment_timelines_timeline_recipe_recipe_iv == null){
                    Log.e(CLASS_NAME, "Error ! ImageView object is null !");
                }
                else{
                    Utility.loadImageFromURL(mContext, story.getRecipeImage(), holder.fragment_timelines_timeline_recipe_recipe_iv);
                }
            }

            setFont(holder.fragment_timelines_timeline_recipe_rl);
        }
        else if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE()) ||
                TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(story.getTYPE()) ||
                TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(story.getTYPE())){

            if(story.getWhoUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_like_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_like_who_tv.setText(story.getWhoName());
                setupUserOnClickListener(holder.fragment_timelines_timeline_like_who_tv, story.getWhoUserId());
            }

            if(story.getWhoseUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_like_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_like_whose_tv.setText(story.getWhoseName()+"'s");
                setupUserOnClickListener(holder.fragment_timelines_timeline_like_whose_tv, story.getWhoseUserId());
            }

            /*like/unlike*/
            if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_like_what_tv.setText("liked");
            }
            else if(TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_like_what_tv.setText("unliked");
            }
            /*like/unlike*/

            if(TIMELINE_LIKE_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Recipe");
                holder.fragment_timelines_timeline_like_review_tv.setVisibility(View.GONE);
            }
            else if(TIMELINE_LIKE_COMMENT_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_COMMENT_REMOVE.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Comment");
                holder.fragment_timelines_timeline_like_review_tv.setText(story.getComment());
            }
            else if(TIMELINE_LIKE_REVIEW_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_LIKE_REVIEW_REMOVE.equalsIgnoreCase(story.getTYPE())){
                holder.fragment_timelines_timeline_like_on_tv.setText("Review");
                holder.fragment_timelines_timeline_like_review_tv.setText(story.getReview());
            }

            /*recipe*/
            setupRecipeCommonView(holder, story);
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_like_rl);
        }
        else if(TIMELINE_COMMENT_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_COMMENT_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            if(story.getWhoUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_comment_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_comment_who_tv.setText(story.getWhoName());

                setupUserOnClickListener(holder.fragment_timelines_timeline_comment_who_tv, story.getWhoUserId());
            }

            if(story.getWhoseUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_comment_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_comment_whose_tv.setText(story.getWhoseName()+"'s");

                setupUserOnClickListener(holder.fragment_timelines_timeline_comment_whose_tv, story.getWhoseUserId());
            }

            holder.fragment_timelines_timeline_comment_comment_tv.setText(story.getComment());

            /*recipe*/
            setupRecipeCommonView(holder, story);
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_comment_rl);
        }
        else if(TIMELINE_REVIEW_RECIPE_ADD.equalsIgnoreCase(story.getTYPE()) || TIMELINE_REVIEW_RECIPE_REMOVE.equalsIgnoreCase(story.getTYPE())){
            if(story.getWhoUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_review_who_tv.setText("You");
            }
            else{
                holder.fragment_timelines_timeline_review_who_tv.setText(story.getWhoName());

                setupUserOnClickListener(holder.fragment_timelines_timeline_review_who_tv, story.getWhoUserId());
            }

            if(story.getWhoseUserId() == loggedInUser.getUSER_ID()){
                holder.fragment_timelines_timeline_review_whose_tv.setText("your");
            }
            else{
                holder.fragment_timelines_timeline_review_whose_tv.setText(story.getWhoseName()+"'s");

                setupUserOnClickListener(holder.fragment_timelines_timeline_review_whose_tv, story.getWhoseUserId());
            }

            /*review*/
            holder.fragment_timelines_timeline_review_review_tv.setText(story.getReview());

            List<ImageView> starsList = new ArrayList<>();
            starsList.add(holder.fragment_timelines_timeline_review_star1_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star2_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star3_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star4_iv);
            starsList.add(holder.fragment_timelines_timeline_review_star5_iv);

            setStars(starsList, story.getRating());
            /*review*/

            /*recipe*/
            setupRecipeCommonView(holder, story);
            /*recipe*/

            setFont(holder.fragment_timelines_timeline_review_rl);
        }
        else if(TIMELINE_USER_PHOTO_MODIFY.equalsIgnoreCase(story.getTYPE())){
            if(story.getWhoUserId() == loggedInUser.getUSER_ID()){
                holder.home_timeline_user_image_modify_item_who_tv.setText("You");
            }
            else{
                holder.home_timeline_user_image_modify_item_who_tv.setText(story.getWhoName().trim());

                setupUserOnClickListener(holder.home_timeline_user_image_modify_item_who_tv, story.getWhoUserId());
            }

            Utility.loadImageFromURL(mContext, story.getWhoUserImage(), holder.home_timeline_user_image_modify_item_photo_iv);

            setFont(holder.home_timeline_user_image_modify_item_rl);
        }
        else if(TIMELINE_USER_FOLLOW.equalsIgnoreCase(story.getTYPE()) || TIMELINE_USER_UNFOLLOW.equalsIgnoreCase(story.getTYPE())){
            setupUserOnClickListener(holder.home_timeline_user_follow_unfollow_item_who_tv, story.getWhoUserId());
            setupUserOnClickListener(holder.home_timeline_user_follow_unfollow_item_whose_tv, story.getWhoseUserId());
            Utility.loadImageFromURL(mContext, story.getWhoseUserImage(), holder.home_timeline_user_follow_unfollow_item_photo_iv);
            setupUserImageOnClickListener(holder.home_timeline_user_follow_unfollow_item_photo_iv, story.getWhoseUserId());

            if(story.getWhoUserId() == loggedInUser.getUSER_ID()){
                holder.home_timeline_user_follow_unfollow_item_who_tv.setText("You");

                if(TIMELINE_USER_FOLLOW.equalsIgnoreCase(story.getTYPE())){
                    holder.home_timeline_user_follow_unfollow_item_what_tv.setText(" followed");
                }
                else if(TIMELINE_USER_UNFOLLOW.equalsIgnoreCase(story.getTYPE())){
                    holder.home_timeline_user_follow_unfollow_item_what_tv.setText(" unfollowed");
                }
            }
            else{
                holder.home_timeline_user_follow_unfollow_item_who_tv.setText(story.getWhoName().trim());

                if(TIMELINE_USER_FOLLOW.equalsIgnoreCase(story.getTYPE())){
                    holder.home_timeline_user_follow_unfollow_item_what_tv.setText(" followed");
                }
                else if(TIMELINE_USER_UNFOLLOW.equalsIgnoreCase(story.getTYPE())){
                    holder.home_timeline_user_follow_unfollow_item_what_tv.setText(" unfollowed");
                }
            }

            if(story.getWhoseUserId() == loggedInUser.getUSER_ID()){
                holder.home_timeline_user_follow_unfollow_item_whose_tv.setText("You");
            }
            else{
                holder.home_timeline_user_follow_unfollow_item_whose_tv.setText(story.getWhoseName().trim());
            }

            setFont(holder.home_timeline_user_follow_unfollow_item_rl);
        }
        else{
            Log.e(CLASS_NAME, "The story type("+story.getTYPE()+") could not be understood. New type of story ?");
            holder.fragment_timelines_timeline_unknown_msg_tv.setText("Unknown Story - "+story.getTYPE());
            return;
        }

        //commons
        if(story.getWhoUserImage() != null && !story.getWhoUserImage().trim().isEmpty()){
            if(holder.common_component_round_image_mini_iv == null){
                Log.e(CLASS_NAME, "Error ! ImageView object is null !");
            }
            else{
                Utility.loadImageFromURL(mContext, story.getWhoUserImage(), holder.common_component_round_image_mini_iv);
                setupUserOnClickListener(holder.common_component_round_image_mini_iv, story.getWhoUserId());
            }
        }

        holder.home_timeline_options_scope_iv.setVisibility(View.GONE);
        holder.common_component_image_options_mini_iv.setTag(story);
        holder.common_component_image_options_mini_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, story);
            }
        });
        //commons

        if(story.getCREATE_DTM() != null && !story.getCREATE_DTM().trim().isEmpty()){
           holder.common_component_text_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(story.getCREATE_DTM(), DB_DATE_TIME)));
        }
        else{
            Log.e(CLASS_NAME, "Error ! CreateDtm is null !");
        }
        //commons
    }

    private void setupRecipeOnClickListener(ViewGroup component, int recipeId){
        RecipeMO recipe = new RecipeMO();
        recipe.setRCP_ID(recipeId);

        component.setTag(recipe);
        component.setOnClickListener(listener);
    }

    private void setupUserOnClickListener(View component, int userId){
        UserMO user = new UserMO();
        user.setUSER_ID(userId);

        component.setTag(user);
        component.setOnClickListener(listener);
    }

    private void setupUserImageOnClickListener(View component, int user_id){
        component.setTag(user_id);
        component.setOnClickListener(listener);
    }

    private void setupRecipeCommonView(ViewHolder holder, TimelineMO story){
        setupRecipeOnClickListener(holder.common_component_card_timeline_recipe_cv, story.getRecipeId());

        /*recipe*/
        if(story.getRecipeOwnerImg() != null && !story.getRecipeOwnerImg().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, story.getRecipeOwnerImg(), holder.common_component_round_image_micro_iv);
        }

        if(story.getRecipeImage() != null && !story.getRecipeImage().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, story.getRecipeImage(), holder.common_component_card_timeline_recipe_recipe_iv);
        }

        if(story.getRecipeName() != null && !story.getRecipeName().trim().isEmpty()){
            holder.common_component_card_timeline_recipe_recipe_name_tv.setText(story.getRecipeName().toUpperCase());
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not fetch recipe name for story.");
        }

        if(story.getRecipeTypeName() != null && !story.getRecipeTypeName().trim().isEmpty()){
            holder.common_component_card_timeline_recipe_recipe_type_tv.setText(story.getRecipeTypeName().toUpperCase());
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not fetch recipe food type name for story.");
        }

        if(story.getRecipeCuisineName() != null && !story.getRecipeCuisineName().trim().isEmpty()){
            holder.common_component_card_timeline_recipe_recipe_cuisine_tv.setText(story.getRecipeCuisineName().toUpperCase());
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not fetch recipe cuisine name for story.");
        }
            /*recipe*/
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, TimelineMO timeline) {
        // inflate menu
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.inflate(R.menu.story_options);

        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            // Possible exceptions are NoSuchMethodError and NoSuchFieldError
            //
            // In either case, an exception indicates something is wrong with the reflection code, or the
            // structure of the PopupMenu class or its dependencies has changed.
            //
            // These exceptions should never happen since we're shipping the AppCompat library in our own apk,
            // but in the case that they do, we simply can't force icons to display, so log the error and
            // show the menu normally.

            Log.w(CLASS_NAME, "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }

        for(int i=0; i<popupMenu.getMenu().size(); i++){
            popupMenu.getMenu().getItem(i).setActionView(view);
        }

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItemClickListener);
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
        public CircleImageView home_timeline_options_scope_iv;
        public ImageView common_component_image_options_mini_iv;
        public CircleImageView common_component_round_image_micro_iv;
        public CardView common_component_card_timeline_recipe_cv;
        public TextView common_component_card_timeline_recipe_recipe_name_tv;
        public TextView common_component_card_timeline_recipe_recipe_type_tv;
        public TextView common_component_card_timeline_recipe_recipe_cuisine_tv;
        public ImageView common_component_card_timeline_recipe_recipe_iv;
        /*common*/

        /*home_timeline_comment_item.xml*/
        public RelativeLayout fragment_timelines_timeline_comment_rl;
        public TextView fragment_timelines_timeline_comment_who_tv;
        public TextView fragment_timelines_timeline_comment_what_tv;
        public TextView fragment_timelines_timeline_comment_whose_tv;
        public TextView fragment_timelines_timeline_comment_comment_tv;
        /*home_timeline_comment_item.xml*/

        /*home_timeline_like_item.xml*/
        public RelativeLayout fragment_timelines_timeline_like_rl;
        public TextView fragment_timelines_timeline_like_who_tv;
        public TextView fragment_timelines_timeline_like_what_tv;
        public TextView fragment_timelines_timeline_like_whose_tv;
        public TextView fragment_timelines_timeline_like_on_tv;
        public TextView fragment_timelines_timeline_like_review_tv;
        /*home_timeline_like_item.xml*/

        /*home_timeline_recipe_item*/
        public RelativeLayout fragment_timelines_timeline_recipe_rl;
        public TextView fragment_timelines_timeline_recipe_msg_tv;
        public CardView fragment_timelines_timeline_recipe_recipe_cv;
        public TextView fragment_timelines_timeline_recipe_recipe_name_tv;
        public TextView fragment_timelines_timeline_recipe_recipe_type_tv;
        public TextView fragment_timelines_timeline_recipe_recipe_cuisine_tv;
        public ImageView fragment_timelines_timeline_recipe_recipe_iv;
        /*home_timeline_recipe_item*/

        /*home_timeline_review_item.xml*/
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
        /*home_timeline_review_item*/

        /*home_timeline_user_item.xml*/
        public RelativeLayout fragment_timelines_timeline_user_rl;
        public TextView fragment_timelines_timeline_user_msg_tv;
        /*home_timeline_user_item.xml*/

        /*home_timeline_user_image_modify_item.xml*/
        public RelativeLayout home_timeline_user_image_modify_item_rl;
        public TextView home_timeline_user_image_modify_item_who_tv;
        public CircleImageView home_timeline_user_image_modify_item_photo_iv;
        /*home_timeline_user_image_modify_item.xml*/

        /*home_timeline_user_follow_unfollow_item.xml*/
        public RelativeLayout home_timeline_user_follow_unfollow_item_rl;
        public TextView home_timeline_user_follow_unfollow_item_who_tv;
        public TextView home_timeline_user_follow_unfollow_item_what_tv;
        public CircleImageView home_timeline_user_follow_unfollow_item_photo_iv;
        public TextView home_timeline_user_follow_unfollow_item_whose_tv;
        /*home_timeline_user_follow_unfollow_item.xml*/

        /*home_timeline_unknown_item.xml*/
        public TextView fragment_timelines_timeline_unknown_msg_tv;
        /*home_timeline_unknown_item.xml*/

        public ViewHolder(View view, int layout) {
            super(view);

            /*commons*/
            common_component_round_image_mini_iv = view.findViewById(R.id.common_component_round_image_mini_layout);
            common_component_text_datetime_tv = view.findViewById(R.id.common_component_text_datetime_tv);
            home_timeline_options_scope_iv = view.findViewById(R.id.home_timeline_options_scope_iv);
            common_component_image_options_mini_iv = view.findViewById(R.id.home_timeline_options_iv);
            common_component_round_image_micro_iv = view.findViewById(R.id.common_component_round_image_micro_iv);
            common_component_card_timeline_recipe_cv = view.findViewById(R.id.common_component_card_timeline_recipe_cv);
            common_component_card_timeline_recipe_recipe_name_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_name_tv);
            common_component_card_timeline_recipe_recipe_type_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_type_tv);
            common_component_card_timeline_recipe_recipe_cuisine_tv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_cuisine_tv);
            common_component_card_timeline_recipe_recipe_iv = view.findViewById(R.id.common_component_card_timeline_recipe_recipe_iv);
            /*commons*/

            if (R.layout.home_timeline_comment_item == layout) {
                fragment_timelines_timeline_comment_rl = view.findViewById(R.id.fragment_timelines_timeline_comment_rl);
                fragment_timelines_timeline_comment_who_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_who_tv);
                fragment_timelines_timeline_comment_comment_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_comment_tv);
                fragment_timelines_timeline_comment_what_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_what_tv);
                fragment_timelines_timeline_comment_whose_tv = view.findViewById(R.id.fragment_timelines_timeline_comment_whose_tv);
            } else if (R.layout.home_timeline_like_item == layout) {
                fragment_timelines_timeline_like_rl = view.findViewById(R.id.fragment_timelines_timeline_like_rl);
                fragment_timelines_timeline_like_who_tv = view.findViewById(R.id.fragment_timelines_timeline_like_who_tv);
                fragment_timelines_timeline_like_what_tv = view.findViewById(R.id.fragment_timelines_timeline_like_what_tv);
                fragment_timelines_timeline_like_whose_tv = view.findViewById(R.id.fragment_timelines_timeline_like_whose_tv);
                fragment_timelines_timeline_like_on_tv = view.findViewById(R.id.fragment_timelines_timeline_like_on_tv);
                fragment_timelines_timeline_like_review_tv = view.findViewById(R.id.fragment_timelines_timeline_like_review_tv);
            } else if (R.layout.home_timeline_recipe_item == layout) {
                fragment_timelines_timeline_recipe_rl = view.findViewById(R.id.fragment_timelines_timeline_recipe_rl);
                fragment_timelines_timeline_recipe_msg_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_msg_tv);
                fragment_timelines_timeline_recipe_recipe_cv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_cv);
                fragment_timelines_timeline_recipe_recipe_name_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_name_tv);
                fragment_timelines_timeline_recipe_recipe_type_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_type_tv);
                fragment_timelines_timeline_recipe_recipe_cuisine_tv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_cuisine_tv);
                fragment_timelines_timeline_recipe_recipe_iv = view.findViewById(R.id.fragment_timelines_timeline_recipe_recipe_iv);
            } else if (R.layout.home_timeline_review_item == layout) {
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
            } else if (R.layout.home_timeline_user_item == layout) {
                fragment_timelines_timeline_user_rl = view.findViewById(R.id.fragment_timelines_timeline_user_rl);
                fragment_timelines_timeline_user_msg_tv = view.findViewById(R.id.fragment_timelines_timeline_user_msg_tv);
            } else if(R.layout.home_timeline_user_image_modify_item == layout){
                home_timeline_user_image_modify_item_rl = view.findViewById(R.id.home_timeline_user_image_modify_item_rl);
                home_timeline_user_image_modify_item_who_tv = view.findViewById(R.id.home_timeline_user_image_modify_item_who_tv);
                home_timeline_user_image_modify_item_photo_iv = view.findViewById(R.id.home_timeline_user_image_modify_item_photo_iv);
            } else if(R.layout.home_timeline_user_follow_unfollow_item == layout){
                home_timeline_user_follow_unfollow_item_rl = view.findViewById(R.id.home_timeline_user_follow_unfollow_item_rl);
                home_timeline_user_follow_unfollow_item_who_tv = view.findViewById(R.id.home_timeline_user_follow_unfollow_item_who_tv);
                home_timeline_user_follow_unfollow_item_what_tv = view.findViewById(R.id.home_timeline_user_follow_unfollow_item_what_tv);
                home_timeline_user_follow_unfollow_item_photo_iv = view.findViewById(R.id.home_timeline_user_follow_unfollow_item_photo_iv);
                home_timeline_user_follow_unfollow_item_whose_tv = view.findViewById(R.id.home_timeline_user_follow_unfollow_item_whose_tv);
            } else {
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE);
                fragment_timelines_timeline_unknown_msg_tv = view.findViewById(R.id.fragment_timelines_timeline_unknown_msg_tv);
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