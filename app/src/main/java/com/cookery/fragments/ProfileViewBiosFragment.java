package com.cookery.fragments;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.UI_FONT;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cookery.R;
import com.cookery.adapters.ProfileViewBiosRecyclerViewAdapter;
import com.cookery.models.UserBio;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewBiosFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.profile_view_bios_ll)
    LinearLayout profile_view_bios_ll;

    @InjectView(R.id.profile_view_bios_bio_iv)
    ImageView profile_view_bios_bio_iv;

    @InjectView(R.id.profile_view_bios_bio_et)
    TextView profile_view_bios_bio_et;

    @InjectView(R.id.profile_view_bios_bio_timestamp_tv)
    TextView profile_view_bios_bio_timestamp_tv;

    @InjectView(R.id.profile_view_bios_rv)
    RecyclerView profile_view_bios_rv;
    /*components*/

    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view_bios, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_bios_ll);

        return view;
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        setupBio();
        setupBios();
    }

    private void setupBios() {
        if(loggedInUser.getBios() != null && !loggedInUser.getBios().isEmpty()){
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            profile_view_bios_rv.setLayoutManager(mLayoutManager);
            profile_view_bios_rv.setItemAnimator(new DefaultItemAnimator());
            profile_view_bios_rv.setAdapter(
                    new ProfileViewBiosRecyclerViewAdapter(mContext, getActivity(), loggedInUser.getBios(),
                            new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(final MenuItem item) {
                                    UserBio bio = (UserBio) item.getActionView().getTag();
                                    loggedInUser.setBio(bio);

                                    if (item.getItemId() == R.id.user_bio_options_menu_set_item) {
                                        updateBio(loggedInUser);
                                    }
                                    else if (item.getItemId() == R.id.user_bio_options_menu_delete_item) {
                                        deleteBio(loggedInUser);
                                    }
                                    else {
                                        Log.e(CLASS_NAME, "Error ! Could not identify the menu item. New menu item ?");
                                    }

                                    return false;
                                }
                            }));
        }
    }

    private void setupBio() {
        profile_view_bios_bio_iv.setVisibility(View.GONE);

        if(loggedInUser.getBio() != null){
            profile_view_bios_bio_et.setText(loggedInUser.getBio().getUSER_BIO());

            if(loggedInUser.getBio().getCREATE_DTM() != null && !loggedInUser.getBio().getCREATE_DTM().trim().isEmpty()) {
                profile_view_bios_bio_timestamp_tv.setText(DateTimeUtility.getSmartDateTime(DateTimeUtility
                        .convertStringToDateTime(loggedInUser.getBio().getCREATE_DTM(), DB_DATE_TIME)));
            }
        }
        else{
            profile_view_bios_bio_timestamp_tv.setVisibility(View.GONE);
        }

        profile_view_bios_bio_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                String bio = String.valueOf(profile_view_bios_bio_et.getText());

                if(bio == null || bio.trim().isEmpty()){
                    profile_view_bios_bio_iv.setVisibility(View.GONE);
                }
                else if(loggedInUser.getBio() != null && loggedInUser.getBio().getUSER_BIO().equals(bio)){
                    profile_view_bios_bio_iv.setVisibility(View.GONE);
                }
                else{
                    profile_view_bios_bio_iv.setVisibility(View.VISIBLE);
                }
            }
        });

        profile_view_bios_bio_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                UserBio bio = new UserBio();
                bio.setUSER_BIO(String.valueOf(profile_view_bios_bio_et.getText()));
                loggedInUser.setBio(bio);

                updateBio(loggedInUser);
            }
        });
    }

    private void updateBio(UserMO user){
        ((ProfileViewFragment)getTargetFragment()).updateBio(user);
        dismiss();
    }

    private void deleteBio(UserMO user){
        ((ProfileViewFragment)getTargetFragment()).deleteBio(user);
        dismiss();
    }

    // Empty constructor required for DialogFragment
    public ProfileViewBiosFragment() {}

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
            int height = LayoutParams.MATCH_PARENT;
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
