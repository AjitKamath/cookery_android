package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.MessageMO;
import com.cookery.models.TimelineMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class TimelineDeleteFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.home_timeline_delete_ll)
    LinearLayout home_timeline_delete_ll;

    @InjectView(R.id.home_timeline_delete_yes_tv)
    TextView home_timeline_delete_yes_tv;

    @InjectView(R.id.home_timeline_delete_no_tv)
    TextView home_timeline_delete_no_tv;
    /*components*/

    private TimelineMO timeline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_timeline_delete, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        timeline = (TimelineMO)getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        if(timeline == null){
            Log.e(CLASS_NAME, "Error ! Timeline is null !");
            return;
        }

        home_timeline_delete_yes_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskerDeleteTimeline().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        home_timeline_delete_no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    // Empty constructor required for DialogFragment
    public TimelineDeleteFragment() {
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

    class AsyncTaskerDeleteTimeline extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;

        @Override
        protected void onPreExecute(){
            frag = Utility.showWaitDialog(getFragmentManager(), "Deleting timeline ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.deleteTimeline(timeline);
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO message = (MessageMO) object;

            Utility.closeWaitDialog(getFragmentManager(), frag);

            dismiss();
            if(message != null && !message.isError()){
                ((HomeActivity)getActivity()).deleteTimeline(timeline);
            }
            else{
                Log.e(CLASS_NAME, "Error ! Something went wrong timeline was not deleted.");
            }
        }
    }
}
