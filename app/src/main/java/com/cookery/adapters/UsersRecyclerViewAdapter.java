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
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = UsersRecyclerViewAdapter.class.getName();
    private Context mContext;

    private List<UserMO> usersList;
    private View.OnClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;
    private String purpose;

    public UsersRecyclerViewAdapter(Context mContext, List<UserMO> usersList, String purpose) {
        this.mContext = mContext;
        this.usersList = usersList;
        this.listener = listener;
        this.purpose = purpose;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);

        return new ViewHolder(itemView);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == usersList.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }

        final UserMO user = usersList.get(position);

        if(user.getIMG() != null && !user.getIMG().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, user.getIMG(), holder.users_item_iv);
        }

        holder.users_item_username_tv.setText(user.getNAME().trim());

        if(user.isFollowing()){
            holder.users_item_follow_tv.setVisibility(View.VISIBLE);
        }
        else{
            holder.users_item_follow_tv.setVisibility(View.GONE);
        }

        holder.users_item_datetime_tv.setText(DateTimeUtility.getCreateOrModifiedTime(user.getCREATE_DTM(), user.getMOD_DTM()));

        setFont(holder.users_item_rl);
    }

    @Override
    public int getItemCount() {
        if(usersList == null){
            return 0;
        }
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout users_item_rl;
        public ImageView users_item_iv;
        public TextView users_item_username_tv;
        public TextView users_item_follow_tv;
        public TextView users_item_datetime_tv;

        public ViewHolder(View view) {
            super(view);
            users_item_rl = view.findViewById(R.id.users_item_rl);
            users_item_iv = view.findViewById(R.id.users_item_iv);
            users_item_username_tv = view.findViewById(R.id.users_item_username_tv);
            users_item_follow_tv = view.findViewById(R.id.users_item_follow_tv);
            users_item_datetime_tv = view.findViewById(R.id.users_item_datetime_tv);
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