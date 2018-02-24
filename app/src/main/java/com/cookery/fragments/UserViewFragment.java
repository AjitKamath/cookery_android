package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.MessageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_IMAGE;
import static com.cookery.utils.Constants.FRAGMENT_USERS;
import static com.cookery.utils.Constants.FRAGMENT_USER_VIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class UserViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    /*components*/
    @InjectView(R.id.profile_view_public_rl)
    RelativeLayout profile_view_public_rl;

    @InjectView(R.id.profile_view_public_profile_message_tv)
    TextView profile_view_public_profile_message_tv;

    @InjectView(R.id.profile_view_public_profile_image_iv)
    CircleImageView profile_view_public_profile_image_iv;

    @InjectView(R.id.profile_view_public_profile_name_tv)
    TextView profile_view_public_profile_name_tv;

    @InjectView(R.id.profile_view_public_profile_email_tv)
    TextView profile_view_public_profile_email_tv;

    @InjectView(R.id.profile_view_public_profile_join_tv)
    TextView profile_view_public_profile_join_tv;

    @InjectView(R.id.profile_view_public_profile_rank_tv)
    TextView profile_view_public_profile_rank_tv;

    @InjectView(R.id.profile_view_public_others_ll)
    LinearLayout profile_view_public_others_ll;

    @InjectView(R.id.profile_view_public_profile_phone_tv)
    TextView profile_view_public_profile_phone_tv;

    @InjectView(R.id.profile_view_public_profile_gender_tv)
    TextView profile_view_public_profile_gender_tv;

    @InjectView(R.id.profile_view_public_profile_recipes_count_tv)
    TextView profile_view_public_profile_recipes_count_tv;

    @InjectView(R.id.profile_view_public_profile_recipes_label_tv)
    TextView profile_view_public_profile_recipes_label_tv;

    @InjectView(R.id.profile_view_public_followers_ll)
    LinearLayout profile_view_public_followers_ll;

    @InjectView(R.id.profile_view_public_followers_tv)
    TextView profile_view_public_followers_tv;

    @InjectView(R.id.profile_view_public_following_ll)
    LinearLayout profile_view_public_following_ll;

    @InjectView(R.id.profile_view_public_following_tv)
    TextView profile_view_public_following_tv;
    /*components*/

    private UserMO user;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_public, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getLoggedInUser();

        getDataFromBundle();

        setupPage();

        setFont(profile_view_public_rl);

        return view;
    }

    private void getDataFromBundle() {
        user = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    private void getLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
    }

    private void setupPage() {
        if(user != null && loggedInUser != null){
            if(user.getUSER_ID() != 0 && loggedInUser.getUSER_ID() != 0 && user.getUSER_ID() == loggedInUser.getUSER_ID()){
                profile_view_public_profile_message_tv.setVisibility(View.VISIBLE);
            }
            else{
                profile_view_public_profile_message_tv.setVisibility(View.GONE);
            }

            if(user.getIMG() != null && !user.getIMG().trim().isEmpty()){
                Utility.loadImageFromURL(mContext, user.getIMG(), profile_view_public_profile_image_iv);

                profile_view_public_profile_image_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put(GENERIC_OBJECT, user.getIMG());
                        Utility.showFragment(getFragmentManager(), FRAGMENT_USER_VIEW, FRAGMENT_PROFILE_VIEW_IMAGE, new ProfileViewImageFragment(), paramsMap);
                    }
                });
            }

            profile_view_public_profile_name_tv.setText(user.getNAME());

            if(user.getEMAIL() != null && !user.getEMAIL().trim().isEmpty()){
                profile_view_public_profile_email_tv.setText(user.getEMAIL().trim());
            }
            else{
                profile_view_public_profile_email_tv.setVisibility(View.GONE);
            }

            profile_view_public_profile_join_tv.setText("JOINED ON "+ DateTimeUtility.getSmartDate(user.getCREATE_DTM()));
            profile_view_public_profile_rank_tv.setText(user.getCurrentRank());

            if(user.getMOBILE() == null && user.getGENDER() == null){
                profile_view_public_others_ll.setVisibility(View.GONE);
            }
            else{
                if(user.getMOBILE() != null && !user.getMOBILE().trim().isEmpty()){
                    profile_view_public_profile_phone_tv.setText(user.getMOBILE().trim());
                }

                if(user.getGENDER() != null && !user.getGENDER().trim().isEmpty()){
                    profile_view_public_profile_gender_tv.setText(Utility.getGender(user.getGENDER().trim()));
                }
            }

            updateRecipesCountView(user);

            profile_view_public_following_tv.setText(user.getFollowingCount()+" FOLLOWING");
            updateFollowersView(user);

            profile_view_public_followers_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(loggedInUser.getUSER_ID() == user.getUSER_ID()){
                        Utility.showSnacks(profile_view_public_rl, "You cannot follow yourself !", OK, Snackbar.LENGTH_LONG);
                    }
                    else{
                        new AsyncTaskerSubmitUserFollow().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            });

            profile_view_public_followers_ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AsyncTaskerFetchUserFollowers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return true;
                }
            });

            profile_view_public_following_ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AsyncTaskerFetchUserFollowing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return true;
                }
            });
        }
        else{
            Log.e(CLASS_NAME, "Error ! User/logged in user is null !");
        }

    }

    private void updateRecipesCountView(UserMO user) {
        profile_view_public_profile_recipes_count_tv.setText(Utility.getSmartNumber(user.getRecipesCount()));

        if(user.getRecipesCount() == 1){
            profile_view_public_profile_recipes_label_tv.setText("RECIPE");
        }
        else{
            profile_view_public_profile_recipes_label_tv.setText("RECIPES");
        }
    }

    private void updateFollowersView(UserMO user) {
        profile_view_public_followers_tv.setText(user.getFollowersCount()+" FOLLOWERS");

        if(user.isFollowing()){
            profile_view_public_followers_tv.setTextColor(ContextCompat.getColor(mContext, R.color.app_color));
        }
        else{
            profile_view_public_followers_tv.setTextColor(ContextCompat.getColor(mContext, R.color.secondaryDarkText));
        }
    }

    // Empty constructor required for DialogFragment
    public UserViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
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

    class AsyncTaskerSubmitUserFollow extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.submitUserFollow(loggedInUser, user);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "just a moment ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> user = (List<UserMO>) object;

            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(user != null && !user.isEmpty() && user.get(0) != null){
                updateFollowersView(user.get(0));
            }
            else{
                MessageMO message = new MessageMO();
                message.setError(true);
                message.setErr_message("Something went wrong !");
                message.setPurpose("USER_FOLLOW_SUBMIT_FAILED");

                Utility.showMessageDialog(getFragmentManager(), null, message);
            }
        }
    }

    class AsyncTaskerFetchUserFollowers extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchUserFollowers(user.getUSER_ID(), loggedInUser.getUSER_ID(), 0);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching followers ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> followers = (List<UserMO>) object;

            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(followers != null && !followers.isEmpty()){
                Object array[] = new Object[]{"FOLLOWERS", followers};
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(GENERIC_OBJECT, array);
                params.put(SELECTED_ITEM, user);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), FRAGMENT_USER_VIEW, FRAGMENT_USERS, new UsersFragment(), params);
            }
            else{
                Utility.showSnacks(profile_view_public_rl, "No Followers to show !", OK, Snackbar.LENGTH_LONG);
            }
        }
    }

    class AsyncTaskerFetchUserFollowing extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchUserFollowings(user.getUSER_ID(), loggedInUser.getUSER_ID(), 0);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching users ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> followings = (List<UserMO>) object;

            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(followings != null && !followings.isEmpty()){
                Object array[] = new Object[]{"FOLLOWINGS", followings};
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(GENERIC_OBJECT, array);
                params.put(SELECTED_ITEM, user);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), FRAGMENT_USER_VIEW, FRAGMENT_USERS, new UsersFragment(), params);
            }
            else{
                Utility.showSnacks(profile_view_public_rl, "No Followers to show !", OK, Snackbar.LENGTH_LONG);
            }
        }
    }
}
