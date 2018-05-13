package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_FONT;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cookery.R;
import com.cookery.models.UserBio;
import com.cookery.utils.DateTimeUtility;
import java.lang.reflect.Field;
import java.util.List;
import lombok.Getter;

public class ProfileViewBiosRecyclerViewAdapter extends RecyclerView.Adapter<ProfileViewBiosRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = ProfileViewBiosRecyclerViewAdapter.class.getName();
    private Context mContext;
    private Activity activity;

    @Getter
    private List<UserBio> bios;

    private OnMenuItemClickListener menuItemListener;

    public ProfileViewBiosRecyclerViewAdapter(Context mContext, Activity activity, List<UserBio> bios, OnMenuItemClickListener menuItemListener) {
        this.mContext = mContext;
        this.activity = activity;
        this.bios = bios;
        this.menuItemListener = menuItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_view_bios_bio_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserBio bio = bios.get(position);

        if(bio.getIS_ACTIVE().equalsIgnoreCase("Y")){
            holder.profile_view_bios_bio_item_selected_iv.setVisibility(View.VISIBLE);
        }
        else{
            holder.profile_view_bios_bio_item_selected_iv.setVisibility(View.INVISIBLE);
        }

        holder.profile_view_bios_bio_item_tv.setText(bio.getUSER_BIO());
        holder.profile_view_bios_bio_item_timestamp_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(bio.getCREATE_DTM(), DB_DATE_TIME)));

        holder.profile_view_bios_bio_item_iv.setTag(bio);
        holder.profile_view_bios_bio_item_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                showPopupMenu(v);
            }
        });

        setFont(holder.profile_view_bios_bio_item_rl);
    }

    @Override
    public int getItemCount() {
        if(bios == null){
            return 0;
        }
        return bios.size();
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popupMenu = new PopupMenu(activity, view);        //do not pass context as 1st param if this method
        //is called from a fragment
        popupMenu.inflate(R.menu.user_bio_options_menu);

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

        UserBio bio = (UserBio) view.getTag();
        if(bio.getIS_ACTIVE().equalsIgnoreCase("Y")){
            popupMenu.getMenu().removeItem(R.id.user_bio_options_menu_set_item);
        }

        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItemListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout profile_view_bios_bio_item_rl;
        public ImageView profile_view_bios_bio_item_selected_iv;
        public ImageView profile_view_bios_bio_item_iv;
        public TextView profile_view_bios_bio_item_tv;
        public TextView profile_view_bios_bio_item_timestamp_tv;

        public ViewHolder(View view) {
            super(view);
            profile_view_bios_bio_item_rl = view.findViewById(R.id.profile_view_bios_bio_item_rl);
            profile_view_bios_bio_item_selected_iv = view.findViewById(R.id.profile_view_bios_bio_item_selected_iv);
            profile_view_bios_bio_item_iv = view.findViewById(R.id.profile_view_bios_bio_item_iv);
            profile_view_bios_bio_item_tv = view.findViewById(R.id.profile_view_bios_bio_item_tv);
            profile_view_bios_bio_item_timestamp_tv = view.findViewById(R.id.profile_view_bios_bio_item_timestamp_tv);
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