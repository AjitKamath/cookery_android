package com.cookery.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cookery.R;
import com.cookery.models.MessageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class MyAccountFragment extends AppCompatActivity {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_fragment_header_rl)
    RelativeLayout fragment_my_recipe_header_rl;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_account);
        ButterKnife.inject(this);


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

    }

    private void login()
    {
        Intent i = new Intent(MyAccountFragment.this, LoginFragment.class);
        //finish();  //Kill the activity from which you will go to next activity
        startActivity(i);

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
            fragment = Utility.showWaitDialog(getFragmentManager(), "Registering User ..");
        }

        @Override
        protected Object doInBackground(UserMO... objects) {
            return InternetUtility.userRegistraion(objects[0].getNAME(),objects[0].getEMAIL(),objects[0].getMOBILE(),objects[0].getPASSWORD(),objects[0].getGENDER());
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO msg = (MessageMO)object;
            String status = msg.getErr_message();
            if(status.equalsIgnoreCase("User Registered Successfully"))
            {
                //TODO: pass user ID
                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, "");

                login();
                Utility.closeWaitDialog(getFragmentManager(), fragment);

            }
            else
            {
                Utility.showSnacks(fragment_my_recipe_header_rl, status, OK, Snackbar.LENGTH_LONG);
            }

             Utility.closeWaitDialog(getFragmentManager(), fragment);

        }
    }



}
