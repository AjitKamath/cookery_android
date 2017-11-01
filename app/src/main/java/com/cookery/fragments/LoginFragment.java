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

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.MessageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class LoginFragment extends AppCompatActivity {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        ButterKnife.inject(this);

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
        Intent i = new Intent(LoginFragment.this, MyAccountFragment.class);
        //finish();  //Kill the activity from which you will go to next activity
        startActivity(i);

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
            MessageMO msg = (MessageMO)object;
            String status = msg.getErr_message();

            if(status.equalsIgnoreCase("login success"))
            {
                Intent i = new Intent(LoginFragment.this, HomeActivity.class);
                //finish();  //Kill the activity from which you will go to next activity
                startActivity(i);
            }
            else
            {
                et_email.setText("");
                et_password.setText("");

                Utility.showSnacks(fragment_my_recipe_header_rl, status, OK, Snackbar.LENGTH_LONG);
            }

            Utility.closeWaitDialog(getFragmentManager(), fragment);

        }
    }


}
