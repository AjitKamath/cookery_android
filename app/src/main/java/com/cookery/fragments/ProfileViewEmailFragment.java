package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.UserMO;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SCOPE_FOLLOWERS;
import static com.cookery.utils.Constants.SCOPE_PUBLIC;
import static com.cookery.utils.Constants.SCOPE_SELF;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewEmailFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_email_ll)
    LinearLayout profile_view_email_ll;

    @InjectView(R.id.profile_view_email_et)
    EditText profile_view_email_et;

    @InjectView(R.id.profile_view_scope_radio_buttons_rg)
    RadioGroup profile_view_scope_radio_buttons_rg;

    @InjectView(R.id.profile_view_email_ok_tv)
    TextView profile_view_email_ok_tv;
    /*components*/

    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_email, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_email_ll);

        return view;
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        profile_view_email_et.setText(loggedInUser.getEMAIL());

        profile_view_email_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newScopeId = Integer.parseInt(String.valueOf(profile_view_scope_radio_buttons_rg.findViewById(profile_view_scope_radio_buttons_rg.getCheckedRadioButtonId()).getTag()));

                if(String.valueOf(profile_view_email_et.getText()).trim().isEmpty()){
                    Utility.showSnacks(profile_view_email_ll, "Email cannot be empty !", OK, Snackbar.LENGTH_LONG);
                }
                else if(loggedInUser.getEMAIL().toUpperCase().trim().equals(String.valueOf(profile_view_email_et.getText()).toUpperCase().trim()) && newScopeId == loggedInUser.getEMAIL_SCOPE_ID()){
                    dismiss();
                }
                else{
                    loggedInUser.setEMAIL(String.valueOf(profile_view_email_et.getText()).trim());
                    loggedInUser.setEMAIL_SCOPE_ID(newScopeId);
                    updateEmail();
                }
            }
        });

        if(SCOPE_PUBLIC == loggedInUser.getEMAIL_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_public_rb)).setChecked(true);
        }
        else if(SCOPE_FOLLOWERS == loggedInUser.getEMAIL_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_followers_rb)).setChecked(true);
        }
        else if(SCOPE_SELF == loggedInUser.getEMAIL_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_myself_rb)).setChecked(true);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not identify the scope name : "+loggedInUser.getEmailScopeName());
        }
    }

    // Empty constructor required for DialogFragment
    public ProfileViewEmailFragment() {}

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

    private void updateEmail(){
        ((ProfileViewFragment)getTargetFragment()).updateEmail(loggedInUser);
        dismiss();
    }
}
