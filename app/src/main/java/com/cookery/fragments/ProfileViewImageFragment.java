package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.LikesMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_COMMENTS;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewImageFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_image_fullscreen_iv)
    ImageView profile_view_image_fullscreen_iv;

    @InjectView(R.id.common_like_view_ll)
    LinearLayout common_like_view_ll;

    @InjectView(R.id.profile_view_image_fullscreen_comments_ll)
    LinearLayout profile_view_image_fullscreen_comments_ll;

    @InjectView(R.id.profile_view_image_fullscreen_comments_tv)
    TextView profile_view_image_fullscreen_comments_tv;
    /*components*/

    private Object object;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_image_fullscreen, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        object = getArguments().get(GENERIC_OBJECT);
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        UserMO user = (UserMO)object;

        Utility.loadImageFromURL(mContext, user.getIMG(), profile_view_image_fullscreen_iv);

        setupLikeView(user);
    }

    private void setupLikeView(UserMO user) {
        Utility.setupLikeView(common_like_view_ll, user.isUserLiked(), user.getLikesCount());

        common_like_view_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMO user = (UserMO) v.getTag();

                Utility.addRemoveLike(v);

                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("USER");
                like.setTYPE_ID(user.getUSER_ID());

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                        AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
            }
        });

        common_like_view_ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UserMO user = (UserMO) v.getTag();

                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("USER");
                like.setTYPE_ID(user.getUSER_ID());

                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                        AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "LIKE", like , user);

                return false;
            }
        });

        common_like_view_ll.setTag(user);
    }

    // Empty constructor required for DialogFragment
    public ProfileViewImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d != null) {
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

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
