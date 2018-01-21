package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.TimelineMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SCOPE_FOLLOWERS;
import static com.cookery.utils.Constants.SCOPE_PUBLIC;
import static com.cookery.utils.Constants.SCOPE_SELF;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class TimelineHideFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.home_timeline_hide_ll)
    LinearLayout home_timeline_hide_ll;

    @InjectView(R.id.home_timeline_hide_public_select_iv)
    ImageView home_timeline_hide_public_select_iv;

    @InjectView(R.id.home_timeline_hide_followers_select_iv)
    ImageView home_timeline_hide_followers_select_iv;

    @InjectView(R.id.home_timeline_hide_myself_select_iv)
    ImageView home_timeline_hide_myself_select_iv;

    @InjectView(R.id.home_timeline_hide_public_ll)
    LinearLayout home_timeline_hide_public_ll;

    @InjectView(R.id.home_timeline_hide_followers_ll)
    LinearLayout home_timeline_hide_followers_ll;

    @InjectView(R.id.home_timeline_hide_myself_ll)
    LinearLayout home_timeline_hide_myself_ll;
    /*components*/

    private Object object;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_timeline_hide, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        object = getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        final TimelineMO timeline = (TimelineMO) object;

        if(timeline == null){
            Log.e(CLASS_NAME, "Error ! Timeline is null !");
            return;
        }

        if(SCOPE_PUBLIC == timeline.getScopeId()){
            home_timeline_hide_public_select_iv.setVisibility(View.VISIBLE);
        }
        else if(SCOPE_FOLLOWERS == timeline.getScopeId()){
            home_timeline_hide_followers_select_iv.setVisibility(View.VISIBLE);
        }
        else if(SCOPE_SELF == timeline.getScopeId()){
            home_timeline_hide_myself_select_iv.setVisibility(View.VISIBLE);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Unidentified timeline scope : "+timeline.getScopeName());
        }

        home_timeline_hide_public_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null){
                    Log.e(CLASS_NAME, "Error ! Expecting tag to have scope id but found null.");
                    return;
                }

                timeline.setScopeId(Integer.parseInt(String.valueOf(v.getTag())));
                new AsyncTaskerModifyTimelineScope().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeline);
            }
        });

        home_timeline_hide_followers_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null){
                    Log.e(CLASS_NAME, "Error ! Expecting tag to have scope id but found null.");
                    return;
                }

                timeline.setScopeId(Integer.parseInt(String.valueOf(v.getTag())));
                new AsyncTaskerModifyTimelineScope().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeline);
            }
        });

        home_timeline_hide_myself_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() == null){
                    Log.e(CLASS_NAME, "Error ! Expecting tag to have scope id but found null.");
                    return;
                }

                timeline.setScopeId(Integer.parseInt(String.valueOf(v.getTag())));
                new AsyncTaskerModifyTimelineScope().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeline);
            }
        });

    }

    // Empty constructor required for DialogFragment
    public TimelineHideFragment() {
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

    class AsyncTaskerModifyTimelineScope extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;

        @Override
        protected void onPreExecute(){
            frag = Utility.showWaitDialog(getFragmentManager(), "Changing timeline privacy ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.modifyTimelineScope((TimelineMO) objects[0]);
        }

        @Override
        protected void onPostExecute(Object object) {
            List<TimelineMO> timeline = (List<TimelineMO>) object;

            Utility.closeWaitDialog(getFragmentManager(), frag);

            if(timeline == null || timeline.isEmpty() || timeline.get(0) == null){
                Log.e(CLASS_NAME, "Error ! Timeline object null from server ");
                Utility.showSnacks(home_timeline_hide_ll, "Something went wrong !", OK, Snackbar.LENGTH_LONG);
            }
            else{
                dismiss();

                ((HomeActivity)getActivity()).updateTimelinePrivacy(timeline.get(0));

            }
        }
    }
}
