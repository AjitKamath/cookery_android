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

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SCOPE_FOLLOWERS;
import static com.cookery.utils.Constants.SCOPE_PUBLIC;
import static com.cookery.utils.Constants.SCOPE_SELF;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewPhoneFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_phone_ll)
    LinearLayout profile_view_phone_ll;

    @InjectView(R.id.profile_view_phone_et)
    EditText profile_view_phone_et;

    @InjectView(R.id.profile_view_scope_radio_buttons_rg)
    RadioGroup profile_view_scope_radio_buttons_rg;

    @InjectView(R.id.profile_view_phone_ok_tv)
    TextView profile_view_phone_ok_tv;
    /*components*/

    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_phone, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_phone_ll);

        return view;
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        profile_view_phone_et.setText(loggedInUser.getMOBILE());

        profile_view_phone_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newScopeId = Integer.parseInt(String.valueOf(profile_view_scope_radio_buttons_rg.findViewById(profile_view_scope_radio_buttons_rg.getCheckedRadioButtonId()).getTag()));

                if(String.valueOf(profile_view_phone_et.getText()).trim().isEmpty()){
                    Utility.showSnacks(profile_view_phone_ll, "Phone number cannot be empty !", OK, Snackbar.LENGTH_LONG);
                }
                else if(loggedInUser.getMOBILE().trim().equals(String.valueOf(profile_view_phone_et.getText()).trim()) && newScopeId == loggedInUser.getMOBILE_SCOPE_ID()){
                    dismiss();
                }
                else{
                    loggedInUser.setMOBILE(String.valueOf(profile_view_phone_et.getText()).trim());
                    loggedInUser.setMOBILE_SCOPE_ID(newScopeId);
                    updatePhone();
                }
            }
        });

        if(SCOPE_PUBLIC == loggedInUser.getMOBILE_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_public_rb)).setChecked(true);
        }
        else if(SCOPE_FOLLOWERS == loggedInUser.getMOBILE_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_followers_rb)).setChecked(true);
        }
        else if(SCOPE_SELF == loggedInUser.getMOBILE_SCOPE_ID()){
            ((RadioButton)profile_view_scope_radio_buttons_rg.findViewById(R.id.profile_view_scope_radio_buttons_myself_rb)).setChecked(true);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Could not identify the scope name : "+loggedInUser.getMobileScopeName());
        }

    }

    // Empty constructor required for DialogFragment
    public ProfileViewPhoneFragment() {}

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

    private void updatePhone(){
        ((ProfileViewFragment)getTargetFragment()).updatePhone(loggedInUser);
        dismiss();
    }
}
