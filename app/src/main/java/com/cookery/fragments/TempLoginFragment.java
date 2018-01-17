package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class TempLoginFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;


    //components
    @InjectView(R.id.login_fragment)
    LinearLayout login_fragment;

    @InjectView(R.id.email)
    EditText email;

    @InjectView(R.id.password)
    EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temp_login, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @OnClick(R.id.login)
    public void login(View view){
        new AsyncTaskerLogin().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            int height = 500;
            d.getWindow().setLayout(width, height);
        }
    }

    class AsyncTaskerLogin extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.userLogin(email.getText().toString(), password.getText().toString());
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "logging in ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> user = (List<UserMO>) object;

            if(user != null && !user.isEmpty()){
                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, user.get(0));

                if(getActivity() instanceof HomeActivity){
                    //((HomeActivity) getActivity()).verifyLoggedInUser();
                    dismiss();
                }


            }
            else{
                Utility.showSnacks(login_fragment, "Login failed !", OK, Snackbar.LENGTH_LONG);
            }
        }
    }
}
