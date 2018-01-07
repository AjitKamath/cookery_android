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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.MessageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewGenderFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_gender_ll)
    LinearLayout profile_view_gender_ll;

    @InjectView(R.id.profile_view_gender_rg)
    RadioGroup profile_view_gender_rg;

    @InjectView(R.id.profile_view_gender_he_rb)
    RadioButton profile_view_gender_he_rb;

    @InjectView(R.id.profile_view_gender_she_rb)
    RadioButton profile_view_gender_she_rb;

    @InjectView(R.id.profile_view_gender_other_rb)
    RadioButton profile_view_gender_other_rb;

    @InjectView(R.id.profile_view_gender_ok_tv)
    TextView profile_view_gender_ok_tv;
    /*components*/

    private UserMO user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_gender, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_gender_ll);

        return view;
    }

    private void getDataFromBundle() {
        user = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        if(user.getGENDER() != null && !user.getGENDER().trim().isEmpty()){
            profile_view_gender_he_rb.setChecked(false);
            profile_view_gender_she_rb.setChecked(false);
            profile_view_gender_other_rb.setChecked(false);

            if("M".equalsIgnoreCase(user.getGENDER().trim())){
                profile_view_gender_he_rb.setChecked(true);
            }
            else if("F".equalsIgnoreCase(user.getGENDER().trim())){
                profile_view_gender_she_rb.setChecked(true);
            }
            else if("O".equalsIgnoreCase(user.getGENDER().trim())){
                profile_view_gender_other_rb.setChecked(true);
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not identify the gender - "+user.getGENDER());
            }
        }

        profile_view_gender_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = "M";

                if(profile_view_gender_rg.getCheckedRadioButtonId() == profile_view_gender_he_rb.getId()){
                    gender = "M";
                }
                else if(profile_view_gender_rg.getCheckedRadioButtonId() == profile_view_gender_she_rb.getId()){
                    gender = "F";
                }
                else if(profile_view_gender_rg.getCheckedRadioButtonId() == profile_view_gender_other_rb.getId()){
                    gender = "O";
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the radio button");
                    return;
                }


                if(gender.equalsIgnoreCase(user.getGENDER().trim())){
                    dismiss();
                }
                else{
                    user.setGENDER(gender);
                    new AsyncTaskerUpdateUserGender().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }

    // Empty constructor required for DialogFragment
    public ProfileViewGenderFragment() {}

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

    class AsyncTaskerUpdateUserGender extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.updateUserGender(user);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "updating your gender..");
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO message = (MessageMO) object;

            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(message != null && !message.isError()){
                if(getTargetFragment() instanceof ProfileViewFragment){
                    ((ProfileViewFragment)getTargetFragment()).updateGender(user.getGENDER());
                    dismiss();
                }
                else{
                    Log.e(CLASS_NAME, "Fragment("+getTargetFragment()+") could not be understood");
                }
            }
            else{
                if(message == null){
                    message = new MessageMO();
                    message.setError(true);
                    message.setErr_message("Something went wrong !");
                }

                message.setPurpose("USER_UPDATE_GENDER_FAILED");
                Utility.showMessageDialog(getFragmentManager(), null, message);
            }
        }
    }
}
