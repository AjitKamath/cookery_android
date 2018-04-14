package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.utils.AsyncTaskUtility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_NO_INTERNET;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class NoInternetFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.common_no_internet_iv)
    ImageView common_no_internet_iv;
    /*components*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_no_internet, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupPage();

        return view;
    }

    private void setupPage() {
        //animate the image
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(800);
        rotate.setRepeatCount(Animation.INFINITE);
        common_no_internet_iv.startAnimation(rotate);

        checkInternet();
    }

    public void checkInternet() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_NO_INTERNET, AsyncTaskUtility.Purpose.CHECK_INTERNET, null, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 5000);
    }

    // Empty constructor required for DialogFragment
    public NoInternetFragment() {
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
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
            d.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
            }
        };
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
