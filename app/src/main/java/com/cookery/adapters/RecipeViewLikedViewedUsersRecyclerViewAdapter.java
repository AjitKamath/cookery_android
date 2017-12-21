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
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

public class RecipeViewLikedViewedUsersRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewLikedViewedUsersRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeViewLikedViewedUsersRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<UserMO> usersList;
    private View.OnClickListener listener;

    public RecipeViewLikedViewedUsersRecyclerViewAdapter(Context mContext, List<UserMO> usersList) {
        this.mContext = mContext;
        this.usersList = usersList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_liked_viewed_users_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserMO user = usersList.get(position);

        if(user.getIMG() != null && !user.getIMG().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, user.getIMG(), holder.recipe_view_liked_viewed_users_item_iv);
        }

        holder.recipe_view_liked_viewed_users_item_username_tv.setText(user.getNAME().trim());

        if(user.getMOD_DTM() != null && !user.getMOD_DTM().trim().isEmpty()){
            holder.recipe_view_liked_viewed_users_item_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(user.getMOD_DTM(), DB_DATE_TIME)));
        }
        else{
            holder.recipe_view_liked_viewed_users_item_datetime_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(user.getCREATE_DTM(), DB_DATE_TIME)));
        }

        setFont(holder.recipe_view_liked_viewed_users_item_rl);
    }

    @Override
    public int getItemCount() {
        if(usersList == null){
            return 0;
        }
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout recipe_view_liked_viewed_users_item_rl;
        public ImageView recipe_view_liked_viewed_users_item_iv;
        public TextView recipe_view_liked_viewed_users_item_username_tv;
        public TextView recipe_view_liked_viewed_users_item_datetime_tv;

        public ViewHolder(View view) {
            super(view);
            recipe_view_liked_viewed_users_item_rl = view.findViewById(R.id.recipe_view_liked_viewed_users_item_rl);
            recipe_view_liked_viewed_users_item_iv = view.findViewById(R.id.recipe_view_liked_viewed_users_item_iv);
            recipe_view_liked_viewed_users_item_username_tv = view.findViewById(R.id.recipe_view_liked_viewed_users_item_username_tv);
            recipe_view_liked_viewed_users_item_datetime_tv = view.findViewById(R.id.recipe_view_liked_viewed_users_item_datetime_tv);
        }
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
}