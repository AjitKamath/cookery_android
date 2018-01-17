package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_LOGIN;
import static com.cookery.utils.Constants.FRAGMENT_REGISTER;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class LoginFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;


    //components
    @InjectView(R.id.common_fragment_header_rl)
    RelativeLayout fragment_my_recipe_header_rl;

    @InjectView(R.id.common_fragment_header_tv)
    TextView fragment_my_recipe_header_tv;

    @InjectView(R.id.et_email)
    EditText et_email;

    @InjectView(R.id.et_password)
    EditText et_password;

    @InjectView(R.id.btn_login)
    Button btn_login;

    @InjectView(R.id.tv_register)
    TextView tv_register;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.inject(this, view);

        fragment_my_recipe_header_tv.setText("LOGIN");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return view;
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
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
            d.setCanceledOnTouchOutside(false);
        }
    }

    private void login()
    {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        if(validateUserDetails(email, password))
        {
            UserMO obj = new UserMO();
            obj.setEMAIL(email);
            obj.setPASSWORD(password);
            new LoginFragment.AsyncTaskerLoginUser().execute(obj);
        }
    }


    private boolean validateUserDetails(String email, String password)
    {
        boolean flag = true;

        if(email.length() == 0 || email.trim().equalsIgnoreCase("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et_email.setError("Invalid email entered !!");
            flag = false;
        }

        if(password.length() == 0 || password.trim().equalsIgnoreCase(""))
        {
            et_password.setError("Please enter password !!");
            flag = false;
        }

        return flag;
    }

    private void register()
    {

        String fragmentNameStr = FRAGMENT_REGISTER;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null)
        {
            manager.beginTransaction().remove(frag).commit();
        }
        MyAccountFragment fragment = new MyAccountFragment();

        fragment.show(manager, fragmentNameStr);
    }


    class AsyncTaskerLoginUser extends AsyncTask<UserMO, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Logging in ..");
        }

        @Override
        protected Object doInBackground(UserMO... objects) {
            return InternetUtility.userLogin(objects[0].getEMAIL(), objects[0].getPASSWORD());
        }

        @Override
        protected void onPostExecute(Object object) {
            UserMO user = (UserMO) object;

            if(user != null && user.getUSER_ID() != 0)
            {
                String fragmentNameStr = FRAGMENT_LOGIN;

                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag(fragmentNameStr);

                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, user);
                ((HomeActivity)getActivity()).updateLoggedInUser();
                ((HomeActivity)getActivity()).loginSuccess();

            }
            else
            {
                et_email.setText("");
                et_password.setText("");

                Utility.showSnacks(fragment_my_recipe_header_rl, "Login failed !", OK, Snackbar.LENGTH_LONG);
            }

            Utility.closeWaitDialog(getFragmentManager(), fragment);

        }
    }


}
