package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.filters.PeopleViewSearchAutoCompleteFilter;
import com.cookery.models.UserMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 27/8/17.
 */

public class PeopleViewSearchAutoCompleteAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private static final String CLASS_NAME = PeopleViewSearchAutoCompleteAdapter.class.getName();

    public List<Object> users = new ArrayList<>();
    private int layout;
    private UserMO loggedInUser;
    public String query;

    public PeopleViewSearchAutoCompleteAdapter(Context context, UserMO loggedInUser) {
        super(context, R.layout.people_view_users_item);
        this.layout = R.layout.people_view_users_item;
        this.mContext = context;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public String getItem(int position) {
        return "";
    }

    @Override
    public Filter getFilter() {
        return new PeopleViewSearchAutoCompleteFilter(this, loggedInUser);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(layout, parent, false);

        if(users.get(position) instanceof UserMO){
            // Get the data item from filtered list.
            UserMO user = (UserMO) users.get(position);

            RelativeLayout people_view_users_item_rl = convertView.findViewById(R.id.people_view_users_item_rl);
            CircleImageView people_view_users_item_iv = convertView.findViewById(R.id.people_view_users_item_iv);
            TextView people_view_users_item_username_tv = convertView.findViewById(R.id.people_view_users_item_username_tv);
            TextView people_view_users_item_email_tv = convertView.findViewById(R.id.people_view_users_item_email_tv);
            TextView people_view_users_item_follow_tv = convertView.findViewById(R.id.people_view_users_item_follow_tv);

            Utility.loadImageFromURL(mContext, user.getIMG(), people_view_users_item_iv);
            people_view_users_item_username_tv.setText(user.getNAME());

            if(user.getEMAIL() != null && !user.getEMAIL().trim().isEmpty()){
                people_view_users_item_email_tv.setText(user.getEMAIL());
            }
            else{
                people_view_users_item_email_tv.setVisibility(View.GONE);
            }

            if(user.isFollowing()){
                people_view_users_item_follow_tv.setText(Utility.getFollowingText(user.getNAME()));
            }
            else{
                people_view_users_item_follow_tv.setVisibility(View.GONE);
            }

            convertView.setTag(user);

            setFont(people_view_users_item_rl);
        }
        else{
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+" : "+users.get(position));
        }

        return convertView;
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
