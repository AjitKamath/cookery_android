package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.UserMO;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewPasswordFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_password_ll)
    LinearLayout profile_view_password_ll;

    @InjectView(R.id.profile_view_current_password_et)
    EditText profile_view_current_password_et;

    @InjectView(R.id.profile_view_new_password_et)
    EditText profile_view_new_password_et;

    @InjectView(R.id.profile_view_repeat_password_et)
    EditText profile_view_repeat_password_et;

    @InjectView(R.id.profile_view_password_ok_tv)
    TextView profile_view_password_ok_tv;
    /*components*/

    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_password, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_password_ll);

        return view;
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        profile_view_password_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = String.valueOf(profile_view_current_password_et.getText());
                String newPassword = String.valueOf(profile_view_new_password_et.getText());
                String repeatPassword = String.valueOf(profile_view_repeat_password_et.getText());

                if(currentPassword.trim().isEmpty()){
                    Utility.showSnacks(profile_view_password_ll, "Enter Current Password", OK, Snackbar.LENGTH_LONG);
                }
                else if(newPassword.isEmpty()){
                    Utility.showSnacks(profile_view_password_ll, "Enter New Password", OK, Snackbar.LENGTH_LONG);
                }
                else if(!newPassword.equals(repeatPassword)){
                    Utility.showSnacks(profile_view_password_ll, "New Passwords do not match", OK, Snackbar.LENGTH_LONG);
                }
                else if(newPassword.equals(repeatPassword)){
                    loggedInUser.setPASSWORD(currentPassword);
                    loggedInUser.setNewPassword(newPassword);
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword(){
        ((ProfileViewFragment)getTargetFragment()).updatePassword(loggedInUser);
        dismiss();
    }

    // Empty constructor required for DialogFragment
    public ProfileViewPasswordFragment() {}

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
}
