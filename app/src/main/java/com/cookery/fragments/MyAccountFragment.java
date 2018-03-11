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
import android.widget.ToggleButton;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.MessageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_LOGIN;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class MyAccountFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_fragment_header_rl)
    RelativeLayout common_fragment_header_rl;

    @InjectView(R.id.common_fragment_header_tv)
    TextView fragment_my_recipe_header_tv;

    @InjectView(R.id.et_name)
    EditText et_name;

    @InjectView(R.id.et_email)
    EditText et_email;

    @InjectView(R.id.et_password)
    EditText et_password;

    @InjectView(R.id.btn_register)
    Button btn_register;

    @InjectView(R.id.tv_login)
    TextView tv_login;

    @InjectView(R.id.et_mobile)
    EditText et_mobile;

    @InjectView(R.id.gender_toggle)
    ToggleButton gender_toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container);
        ButterKnife.inject(this, view);

        fragment_my_recipe_header_tv.setText("My Account");

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
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
        String fragmentNameStr = FRAGMENT_LOGIN;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null)
        {
            manager.beginTransaction().remove(frag).commit();
        }
        LoginFragment fragment = new LoginFragment();

        fragment.show(manager, fragmentNameStr);

    }

    private void registerUser()
    {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String mobile = et_mobile.getText().toString();
        String password = et_password.getText().toString();
        String gender = gender_toggle.isChecked()?"F":"M";
        if(validateUserDetails(name, email, mobile, password, gender))
        {
            UserMO obj = new UserMO();
            obj.setEMAIL(email);
            obj.setGENDER(gender);
            obj.setMOBILE(mobile);
            obj.setNAME(name);
            obj.setPASSWORD(password);

            new AsyncTaskerRegisterUser().execute(obj);

        }
    }


    private boolean validateUserDetails(String name,String email, String mobile, String password, String gender)
    {
        boolean flag = true;

        if(name.length() == 0 || name.trim().equalsIgnoreCase(""))
        {
            et_name.setError("Please enter name !!");
            flag = false;
        }

        if(email.length() == 0 || email.trim().equalsIgnoreCase("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et_email.setError("Invalid email entered !!");
            flag = false;
        }

        if(mobile.length() <= 9 || mobile.trim().equalsIgnoreCase(""))
        {
            et_mobile.setError("Invalid Mobile entered !!");
            flag = false;
        }

        if(password.length() == 0 || password.trim().equalsIgnoreCase(""))
        {
            et_password.setError("Please enter password !!");
            flag = false;
        }

        return flag;
    }

    class AsyncTaskerRegisterUser extends AsyncTask<UserMO, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            //Null check is for registration via social login
            if(null != getFragmentManager()) {
                fragment = Utility.showWaitDialog(getFragmentManager(), "Registering User ..");
            }
        }

        @Override
        protected Object doInBackground(UserMO... objects) {
            //return InternetUtility.userRegistration(objects[0].getNAME(),objects[0].getEMAIL(),objects[0].getMOBILE(),objects[0].getPASSWORD(),objects[0].getGENDER());
            return InternetUtility.userRegistration(objects[0].getNAME(),objects[0].getEMAIL(),objects[0].getPASSWORD());
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO msg = (MessageMO)object;
            String status = msg.getErr_message();
            UserMO userobj = new UserMO();
            userobj.setUSER_ID(msg.getUser_id());
            if(status.equalsIgnoreCase("User Registered Successfully"))
            {
                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, userobj);
                dismiss();
                ((HomeActivity) getActivity()).updateLoggedInUser();

               // login();
                Utility.closeWaitDialog(getFragmentManager(), fragment);

            }
            else
            {
                Utility.showSnacks(common_fragment_header_rl, status, OK, Snackbar.LENGTH_LONG);
            }

             Utility.closeWaitDialog(getFragmentManager(), fragment);

        }
    }
}
