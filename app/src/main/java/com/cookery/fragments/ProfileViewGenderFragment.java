package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
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
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_GENDER;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.SCOPE_FOLLOWERS;
import static com.cookery.utils.Constants.SCOPE_PUBLIC;
import static com.cookery.utils.Constants.SCOPE_SELF;
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

    @InjectView(R.id.profile_view_scope_radio_buttons_rg)
    RadioGroup profile_view_scope_radio_buttons_rg;

    @InjectView(R.id.profile_view_gender_ok_tv)
    TextView profile_view_gender_ok_tv;
    /*components*/

    private UserMO loggedInUser;

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
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        if(loggedInUser.getGENDER() != null && !loggedInUser.getGENDER().trim().isEmpty()){
            profile_view_gender_he_rb.setChecked(false);
            profile_view_gender_she_rb.setChecked(false);
            profile_view_gender_other_rb.setChecked(false);

            if("M".equalsIgnoreCase(loggedInUser.getGENDER().trim())){
                profile_view_gender_he_rb.setChecked(true);
            }
            else if("F".equalsIgnoreCase(loggedInUser.getGENDER().trim())){
                profile_view_gender_she_rb.setChecked(true);
            }
            else if("O".equalsIgnoreCase(loggedInUser.getGENDER().trim())){
                profile_view_gender_other_rb.setChecked(true);
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not identify the gender - "+loggedInUser.getGENDER());
            }
        }

        profile_view_gender_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = "M";
                int newScopeId = Integer.parseInt(String.valueOf(profile_view_scope_radio_buttons_rg.findViewById(profile_view_scope_radio_buttons_rg.getCheckedRadioButtonId()).getTag()));

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


                if(gender.equalsIgnoreCase(loggedInUser.getGENDER().trim()) && newScopeId == loggedInUser.getGENDER_SCOPE_ID()){
                    dismiss();
                }
                else{
                    loggedInUser.setGENDER(gender);
                    loggedInUser.setGENDER_SCOPE_ID(newScopeId);
                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_PROFILE_VIEW_GENDER,
                            AsyncTaskUtility.Purpose.UPDATE_USER, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "GENDER");
                }
            }
        });

        if(SCOPE_PUBLIC == loggedInUser.getGENDER_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_public_rb)).setChecked(true);
        }
        else if(SCOPE_FOLLOWERS == loggedInUser.getGENDER_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_followers_rb)).setChecked(true);
        }
        else if(SCOPE_SELF == loggedInUser.getGENDER_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_myself_rb)).setChecked(true);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not identify the scope name : "+loggedInUser.getGenderScopeName());
        }
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

    public void updateGender(){
        ((ProfileViewFragment)getTargetFragment()).updateGender(loggedInUser);
        dismiss();
    }
}
