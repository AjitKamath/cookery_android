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
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @InjectView(R.id.profile_view_image_fullscreen_likes_ll)
    LinearLayout profile_view_image_fullscreen_likes_ll;

    @InjectView(R.id.profile_view_image_fullscreen_likes_iv)
    ImageView profile_view_image_fullscreen_likes_iv;

    @InjectView(R.id.profile_view_image_fullscreen_likes_tv)
    TextView profile_view_image_fullscreen_likes_tv;

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

    private void setupLikeView(final UserMO user) {
        profile_view_image_fullscreen_likes_iv.setBackgroundResource(Utility.getLikeImageId(user.isUserLiked()));
        profile_view_image_fullscreen_likes_tv.setText(Utility.getSmartNumber(user.getLikesCount()));

        profile_view_image_fullscreen_likes_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesMO like = new LikesMO();
                like.setUSER_ID(loggedInUser.getUSER_ID());
                like.setTYPE("USER");
                like.setTYPE_ID(user.getUSER_ID());

                new AsyncSubmitUserImageLike().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
            }
        });

        if(getTargetFragment() instanceof UserViewFragment){
            ((UserViewFragment)getTargetFragment()).setUser(user);
            ((UserViewFragment)getTargetFragment()).setupLikeView(user);
        }
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

    public class AsyncSubmitUserImageLike extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object... objects) {
            LikesMO like = (LikesMO) objects[0];
            return InternetUtility.submitLike(like);
        }

        @Override
        protected void onPostExecute(Object result) {
            LikesMO like = (LikesMO) result;

            if (like != null) {
                UserMO user = (UserMO) object;

                user.setUserLiked(like.isUserLiked());
                user.setLikesCount(like.getLikesCount());
                setupLikeView(user);
            }
        }
    }
}
