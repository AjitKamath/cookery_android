package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.DEFAULT_SOCIAL_KEY;
import static com.cookery.utils.Constants.FRAGMENT_LOGIN;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.OK;

/**
 * Created by vishal on 08/10/17.
 */
public class LoginFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    CallbackManager callbackManager;

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

    @InjectView(R.id.fb_login_button)
    LoginButton fb_login_button;

    @InjectView(R.id.google_sign_in_button)
    SignInButton google_sign_in_button;

    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 2000;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Dialog dialog;

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

        fb_login_button.setFragment(this);
        FBLogin();

        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        return view;
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            UserMO userobj = new UserMO();
            userobj.setNAME(acct.getGivenName()+" "+acct.getFamilyName());
            userobj.setPASSWORD(DEFAULT_SOCIAL_KEY);
            userobj.setEMAIL(acct.getEmail());

            checkSocialRegistration(userobj);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            google_sign_in_button.setVisibility(View.GONE);
            //signOutButton.setVisibility(View.VISIBLE);
        } else {
            // mStatusTextView.setText(R.string.signed_out);
            // Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_defaolt);
            // imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            google_sign_in_button.setVisibility(View.VISIBLE);
            //signOutButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        fragmentManager = getFragmentManager();

        // Call back for Facebook login
        callbackManager = CallbackManager.Factory.create();

        // For Gmail Login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
              //  .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        dialog = getDialog();
        if (dialog!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.setCanceledOnTouchOutside(false);
        }

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    public void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
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

    private void FBLogin()
    {
        fb_login_button.setReadPermissions("email");
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//            Toast.makeText(mContext,"Status: "+loginResult.getAccessToken(),Toast.LENGTH_LONG).show();

              GraphRequest graphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    UserMO userobj = new UserMO();
                                    userobj.setNAME(object.getString("first_name") + " " + object.getString("last_name"));
                                    userobj.setPASSWORD(DEFAULT_SOCIAL_KEY);
                                    userobj.setEMAIL(object.getString("email"));
                                    checkSocialRegistration(userobj);
                                }
                                catch (JSONException e){
                                    if(e.getMessage().equalsIgnoreCase("No value for email")){
                                        //Utility.showSnacks(fragment_my_recipe_header_rl, "Login failed !", OK, Snackbar.LENGTH_LONG);
                                    }
                                    Log.e(CLASS_NAME, e.getMessage());
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext,"Status: Cancelled",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mContext,"Status: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public boolean checkSocialRegistration(UserMO object) {

        if (!validateUserDetails(object.getEMAIL(), object.getPASSWORD())) {
            Utility.showSnacks(fragment_my_recipe_header_rl, "Login failed !", OK, Snackbar.LENGTH_LONG);
            Log.e(CLASS_NAME, "Email not found");
            return false;
        } else {
            new AsyncTaskerCheckFirstTimeSocialLogin().execute(object);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // For Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


        // For Gmail
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
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

        String fragmentNameStr = FRAGMENT_LOGIN;

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
            List<UserMO> userList = (List<UserMO>)object;

            if(userList != null && !userList.isEmpty() && userList.get(0) != null){
                dismiss();

                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, userList.get(0));
                ((HomeActivity)getActivity()).loggedInUser = userList.get(0);
                ((HomeActivity)getActivity()).updateContent();
            }
            else{
                et_password.setText("");
                Utility.showSnacks(fragment_my_recipe_header_rl, "Login failed !", OK, Snackbar.LENGTH_LONG);
            }

            Utility.closeWaitDialog(getFragmentManager(), fragment);
        }
    }

    class AsyncTaskerCheckFirstTimeSocialLogin extends AsyncTask<UserMO, Void, Object> {
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected Object doInBackground(UserMO... objects) {
            return InternetUtility.userCheckFirstTimeSocialLogin(objects[0].getEMAIL(),objects[0].getNAME(),objects[0].getPASSWORD());
        }

        @Override
        protected void onPostExecute(Object object) {

            UserMO msg = (UserMO)object;
            int user_id = msg.getUSER_ID();
            if (user_id != 0) {
                Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, object);
                dismiss();
                //((HomeActivity) getActivity()).updateLoggedInUser();
            }
            else{
            MyAccountFragment myacctfrag = new MyAccountFragment();
            myacctfrag.new AsyncTaskerRegisterUser().execute(msg);
            }


            Utility.closeWaitDialog(getFragmentManager(), fragment);
        }
    }


}
