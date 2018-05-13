package com.cookery.fragments;

import static android.app.Activity.RESULT_OK;
import static com.cookery.utils.Constants.FRAGMENT_BIOS_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_IMAGES;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_EMAIL;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_GENDER;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_NAME;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_PASSWORD;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_PHONE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.CommentMO;
import com.cookery.models.LikesMO;
import com.cookery.models.Milestone;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_sv)
    ScrollView profile_view_sv;

    @InjectView(R.id.profile_view_profile_join_tv)
    TextView profile_view_profile_join_tv;

    @InjectView(R.id.profile_view_profile_image_iv)
    CircleImageView profile_view_profile_image_iv;

    @InjectView(R.id.profile_view_profile_image_change_tv)
    TextView profile_view_profile_image_change_tv;

    @InjectView(R.id.common_like_view_ll)
    LinearLayout common_like_view_ll;

    @InjectView(R.id.profile_view_comment_ll)
    LinearLayout profile_view_comment_ll;

    @InjectView(R.id.profile_view_comment_tv)
    TextView profile_view_comment_tv;

    @InjectView(R.id.profile_view_profile_name_tv)
    TextView profile_view_profile_name_tv;

    @InjectView(R.id.profile_view_profile_name_change_iv)
    ImageView profile_view_profile_name_change_iv;

    @InjectView(R.id.profile_view_profile_email_tv)
    TextView profile_view_profile_email_tv;

    @InjectView(R.id.profile_view_profile_email_scope_iv)
    ImageView profile_view_profile_email_scope_iv;

    @InjectView(R.id.profile_view_profile_email_change_iv)
    ImageView profile_view_profile_email_change_iv;

    @InjectView(R.id.profile_view_profile_email_verify_tv)
    TextView profile_view_profile_email_verify_tv;

    @InjectView(R.id.profile_view_profile_password_change_iv)
    ImageView profile_view_profile_password_change_iv;

    @InjectView(R.id.profile_view_profile_phone_tv)
    TextView profile_view_profile_phone_tv;

    @InjectView(R.id.profile_view_profile_phone_scope_iv)
    ImageView profile_view_profile_phone_scope_iv;

    @InjectView(R.id.profile_view_profile_phone_change_iv)
    ImageView profile_view_profile_phone_change_iv;

    @InjectView(R.id.profile_view_profile_phone_verify_tv)
    TextView profile_view_profile_phone_verify_tv;

    @InjectView(R.id.profile_view_profile_gender_tv)
    TextView profile_view_profile_gender_tv;

    @InjectView(R.id.profile_view_profile_gender_scope_iv)
    ImageView profile_view_profile_gender_scope_iv;

    @InjectView(R.id.profile_view_profile_gender_change_iv)
    ImageView profile_view_profile_gender_change_iv;

    @InjectView(R.id.profile_view_profile_bio_change_iv)
    ImageView profile_view_profile_bio_change_iv;

    @InjectView(R.id.profile_view_bio_tv)
    TextView profile_view_bio_tv;

    @InjectView(R.id.profile_view_follow_followers_ll)
    LinearLayout profile_view_follow_followers_ll;

    @InjectView(R.id.profile_view_followers_tv)
    TextView profile_view_followers_tv;

    @InjectView(R.id.profile_view_follow_following_ll)
    LinearLayout profile_view_follow_following_ll;

    @InjectView(R.id.profile_view_following_tv)
    TextView profile_view_following_tv;

    @InjectView(R.id.profile_view_current_rank_tv)
    TextView profile_view_current_rank_tv;

    @InjectView(R.id.profile_view_current_recipes_tv)
    TextView profile_view_current_recipes_tv;

    @InjectView(R.id.profile_view_current_likes_tv)
    TextView profile_view_current_likes_tv;

    @InjectView(R.id.profile_view_current_reviews_tv)
    TextView profile_view_current_reviews_tv;

    @InjectView(R.id.profile_view_next_rank_tv)
    TextView profile_view_next_rank_tv;

    @InjectView(R.id.profile_view_next_recipes_tv)
    TextView profile_view_next_recipes_tv;

    @InjectView(R.id.profile_view_next_likes_tv)
    TextView profile_view_next_likes_tv;

    @InjectView(R.id.profile_view_next_reviews_tv)
    TextView profile_view_next_reviews_tv;
    /*components*/

    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_sv);

        return view;
    }

    public void updateUser(final UserMO user) {
        loggedInUser = user;
        setupPage();
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);

        if(getActivity() != null){
            ((HomeActivity)getActivity()).setupNavigator();
        }
        else{
            Log.e(CLASS_NAME, "Error ! Activity is null");
        }
    }

    private void setupPage() {
        profile_view_profile_join_tv.setText("JOINED ON "+DateTimeUtility.getSmartDate(loggedInUser.getCREATE_DTM()));

        if(loggedInUser.getIMG() != null && !loggedInUser.getIMG().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, loggedInUser.getIMG(), profile_view_profile_image_iv);

            profile_view_profile_image_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<UserMO> images = new ArrayList<>();
                    images.add(loggedInUser);

                    Object[] objects = new Object[]{0, images};

                    Map<String, Object> params = new HashMap<>();
                    params.put(GENERIC_OBJECT, objects);
                    params.put(LOGGED_IN_USER, loggedInUser);

                    Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_IMAGES, new ImagesFragment(), params);
                }
            });
        }

        profile_view_profile_name_tv.setText(loggedInUser.getNAME());

        setupEmail(loggedInUser);
        setupPhone(loggedInUser);
        setupGender(loggedInUser);

        profile_view_followers_tv.setText(loggedInUser.getFollowersCount()+" FOLLOWERS");
        profile_view_following_tv.setText(loggedInUser.getFollowingCount()+" FOLLOWING");

        profile_view_profile_image_change_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_PROFILE_VIEW);
                Utility.startCropImageActivity(mContext, fragment);
            }
        });

        setupLikeView();
        setupCommenView();

        profile_view_profile_name_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_NAME, new ProfileViewNameFragment(), paramsMap);
            }
        });

        profile_view_profile_email_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(LOGGED_IN_USER, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_EMAIL, new ProfileViewEmailFragment(), paramsMap);
            }
        });

        profile_view_profile_password_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_PASSWORD, new ProfileViewPasswordFragment(), paramsMap);
            }
        });

        profile_view_profile_phone_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_PHONE, new ProfileViewPhoneFragment(), paramsMap);
            }
        });

        profile_view_profile_gender_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(LOGGED_IN_USER, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_GENDER, new ProfileViewGenderFragment(), paramsMap);
            }
        });

        profile_view_follow_followers_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "FOLLOWERS");
                return true;
            }
        });

        profile_view_follow_following_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "FOLLOWINGS");
                return true;
            }
        });

        setupBio(loggedInUser);
        setupRanksAndMilestones();
    }

    private void setupBio(UserMO user) {
        profile_view_profile_bio_change_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_BIOS_VIEW, new ProfileViewBiosFragment(), paramsMap);
            }
        });

        if(user.getBio() != null){
            profile_view_bio_tv.setText(user.getBio().getUSER_BIO());
        }
        else{
            profile_view_bio_tv.setText("No bio has been set");
        }
    }

    private void setupCommenView() {
        profile_view_comment_tv.setText(Utility.getSmartNumber(loggedInUser.getCommentsCount()));

        profile_view_comment_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentMO comment = new CommentMO();
                comment.setTYPE_ID(loggedInUser.getUSER_ID());
                comment.setTYPE("USER");

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                        AsyncTaskUtility.Purpose.FETCH_COMMENTS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, loggedInUser, comment);
            }
        });
    }

    private void setupLikeView() {
        Utility.setupLikeView(common_like_view_ll, loggedInUser.isUserLiked(), loggedInUser.getLikesCount());

        common_like_view_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.addRemoveLike(v);

                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("USER");
                like.setTYPE_ID(loggedInUser.getUSER_ID());

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                        AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
            }
        });

        common_like_view_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("USER");
                like.setTYPE_ID(loggedInUser.getUSER_ID());

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "LIKE", like , loggedInUser);

                return false;
            }
        });
    }

    private void setupRanksAndMilestones(){
        //current milestone
        if(loggedInUser.getCurrentRankAndMilestone() != null && !loggedInUser.getCurrentRankAndMilestone().isEmpty()){
            profile_view_current_rank_tv.setText(loggedInUser.getCurrentRank().toUpperCase());

            for(Milestone milestone : loggedInUser.getCurrentRankAndMilestone()) {
                if ("RECIPE".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_recipes_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_recipes_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                } else if ("LIKE".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_likes_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_likes_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                }
                else if ("REVIEW".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_reviews_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_reviews_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the milestone : "+milestone.getTYPE());
                }
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Current rank & milestones are null");
        }
        //current milestone

        //next milestone
        if(loggedInUser.getNextRankAndMilestone() != null && !loggedInUser.getNextRankAndMilestone().isEmpty()){
            profile_view_next_rank_tv.setText(loggedInUser.getNextRankAndMilestone().get(0).getRANK_NAME().toUpperCase());

            for(Milestone milestone : loggedInUser.getNextRankAndMilestone()) {
                if ("RECIPE".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_recipes_tv.setText(String.valueOf(milestone.getNUMBER()));
                } else if ("LIKE".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_likes_tv.setText(String.valueOf(milestone.getNUMBER()));
                }
                else if ("REVIEW".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_reviews_tv.setText(String.valueOf(milestone.getNUMBER()));
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the milestone : "+milestone.getTYPE());
                }
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Next rank & milestones are null");
        }
        //Next milestone
    }

    public void updateName(String userName){
        profile_view_profile_name_tv.setText(userName);

        Utility.showSnacks(profile_view_sv, "Your name has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW, AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "NAME");
    }

    private void setupEmail(UserMO user){
        profile_view_profile_email_tv.setText(user.getEMAIL());
        if(user.getVeri_code() == 0){
            profile_view_profile_email_verify_tv.setText("VERIFIED");
        }
        else{
            profile_view_profile_email_verify_tv.setText("NOT VERIFIED");
        }
        profile_view_profile_email_verify_tv.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        profile_view_profile_email_scope_iv.setImageResource(Utility.getScopeImageId(user.getEMAIL_SCOPE_ID()));
    }

    public void updateEmail(UserMO user){
        setupEmail(user);

        Utility.showSnacks(profile_view_sv, "Your email has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "EMAIL");
    }

    public void updateBio(final UserMO user) {
        setupBio(user);

        Utility.showSnacks(profile_view_sv, "Your bio has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "BIO");
    }

    public void deleteBio(final UserMO user) {
        Utility.showSnacks(profile_view_sv, "Your bio has been deleted !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.DELETE_USER_BIO, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user.getBio());
    }

    public void updatePassword(UserMO user){
        Utility.showSnacks(profile_view_sv, "Your password has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "PASSWORD");
    }

    private void setupPhone(UserMO user){
        if(user.getMOBILE() != null && !user.getMOBILE().trim().isEmpty()){
            profile_view_profile_phone_tv.setText(user.getMOBILE());
            profile_view_profile_phone_verify_tv.setText("NOT VERIFIED");
            profile_view_profile_phone_verify_tv.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            profile_view_profile_phone_scope_iv.setImageResource(Utility.getScopeImageId(user.getMOBILE_SCOPE_ID()));
        }
        else{
            profile_view_profile_phone_tv.setText("Not Set");
            profile_view_profile_phone_scope_iv.setVisibility(View.GONE);
        }
    }

    public void updatePhone(UserMO user){
        setupPhone(user);

        Utility.showSnacks(profile_view_sv, "Your phone has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "PHONE");
    }

    private void setupGender(UserMO user){
        if(user.getGENDER() != null && !user.getGENDER().trim().isEmpty()) {
            profile_view_profile_gender_tv.setText(Utility.getGender(user.getGENDER()));
            profile_view_profile_gender_scope_iv.setImageResource(Utility.getScopeImageId(user.getGENDER_SCOPE_ID()));
        }
        else{
            profile_view_profile_gender_tv.setText("Not Set");
            profile_view_profile_gender_scope_iv.setVisibility(View.GONE);
        }
    }

    public void updateGender(UserMO user){
        setupGender(user);

        Utility.showSnacks(profile_view_sv, "Your gender has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "GENDER");
    }

    private void updatePhoto(Uri photoPath){
        loggedInUser.setIMG(photoPath.getPath());
        Utility.loadImageFromPath(mContext, photoPath.getPath(), profile_view_profile_image_iv);

        Utility.showSnacks(profile_view_sv, "Your photo has been changed !", OK, Snackbar.LENGTH_LONG);

        new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW,
                AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "IMAGE");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                updatePhoto(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(CLASS_NAME, "Error ! Something went wrong ! : "+error.getMessage());
            }
        }
    }

    // Empty constructor required for DialogFragment
    public ProfileViewFragment() {}

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
}
