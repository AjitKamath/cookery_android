package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.activities.HomeActivity;
import com.cookery.models.MessageMO;
import com.cookery.models.Milestone;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.cookery.utils.Constants.CAMERA_CHOICE;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_EMAIL;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_GENDER;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_NAME;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_PASSWORD;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW_PHONE;
import static com.cookery.utils.Constants.GALLERY_CHOICE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.REQUEST_GALLERY_PHOTO;
import static com.cookery.utils.Constants.REQUEST_TAKE_PHOTO;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ProfileViewFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    /*components*/
    @InjectView(R.id.profile_view_rl)
    RelativeLayout profile_view_rl;

    @InjectView(R.id.profile_view_profile_image_iv)
    CircleImageView profile_view_profile_image_iv;

    @InjectView(R.id.profile_view_profile_image_change_tv)
    TextView profile_view_profile_image_change_tv;

    @InjectView(R.id.profile_view_profile_name_tv)
    TextView profile_view_profile_name_tv;

    @InjectView(R.id.profile_view_profile_name_change_iv)
    ImageView profile_view_profile_name_change_iv;

    @InjectView(R.id.profile_view_profile_email_tv)
    TextView profile_view_profile_email_tv;

    @InjectView(R.id.profile_view_profile_email_change_iv)
    ImageView profile_view_profile_email_change_iv;

    @InjectView(R.id.profile_view_profile_email_verify_tv)
    TextView profile_view_profile_email_verify_tv;

    @InjectView(R.id.profile_view_profile_password_change_iv)
    ImageView profile_view_profile_password_change_iv;

    @InjectView(R.id.profile_view_profile_phone_tv)
    TextView profile_view_profile_phone_tv;

    @InjectView(R.id.profile_view_profile_phone_change_iv)
    ImageView profile_view_profile_phone_change_iv;

    @InjectView(R.id.profile_view_profile_phone_verify_tv)
    TextView profile_view_profile_phone_verify_tv;

    @InjectView(R.id.profile_view_profile_gender_tv)
    TextView profile_view_profile_gender_tv;

    @InjectView(R.id.profile_view_followers_tv)
    TextView profile_view_followers_tv;

    @InjectView(R.id.profile_view_following_tv)
    TextView profile_view_following_tv;

    @InjectView(R.id.profile_view_current_rank_tv)
    TextView profile_view_current_rank_tv;

    @InjectView(R.id.profile_view_current_recipes_tv)
    TextView profile_view_current_recipes_tv;

    @InjectView(R.id.profile_view_current_likes_tv)
    TextView profile_view_current_likes_tv;

    @InjectView(R.id.profile_view_current_reviews_tv)
    TextView profile_view_current_reviews_tv;

    @InjectView(R.id.profile_view_next_rank_tv)
    TextView profile_view_next_rank_tv;

    @InjectView(R.id.profile_view_next_recipes_tv)
    TextView profile_view_next_recipes_tv;

    @InjectView(R.id.profile_view_next_likes_tv)
    TextView profile_view_next_likes_tv;

    @InjectView(R.id.profile_view_next_reviews_tv)
    TextView profile_view_next_reviews_tv;
    /*components*/

    private UserMO loggedInUser;
    private boolean doUpdateLoggedInUser = false;
    private String imagePathStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_view, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(profile_view_rl);

        return view;
    }

    private void getDataFromBundle() {
        loggedInUser = (UserMO) getArguments().get(GENERIC_OBJECT);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        if(doUpdateLoggedInUser){
            ((HomeActivity)getActivity()).updateLoggedInUser();
        }
    }

    private void setupPage() {
        if(loggedInUser.getIMG() != null && !loggedInUser.getIMG().trim().isEmpty()){
            Utility.loadImageFromURL(mContext, loggedInUser.getIMG(), profile_view_profile_image_iv);
        }

        profile_view_profile_name_tv.setText(loggedInUser.getNAME());
        profile_view_profile_email_tv.setText(loggedInUser.getEMAIL());

        if(loggedInUser.getMOBILE() != null && !loggedInUser.getMOBILE().trim().isEmpty()){
            profile_view_profile_phone_tv.setText(loggedInUser.getMOBILE());
        }

        if(loggedInUser.getGENDER() != null && !loggedInUser.getGENDER().trim().isEmpty()){
            profile_view_profile_gender_tv.setText(Utility.getGender(loggedInUser.getGENDER()));
        }

        profile_view_followers_tv.setText(loggedInUser.getFollowersCount()+" FOLLOWERS");
        profile_view_following_tv.setText(loggedInUser.getFollowingCount()+" FOLLOWING");

        profile_view_profile_image_change_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.pickPhotos(getFragmentManager(), FRAGMENT_PROFILE_VIEW);
            }
        });

        profile_view_profile_name_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_NAME, new ProfileViewNameFragment(), paramsMap);
            }
        });

        profile_view_profile_email_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_EMAIL, new ProfileViewEmailFragment(), paramsMap);
            }
        });

        profile_view_profile_password_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_PASSWORD, new ProfileViewPasswordFragment(), paramsMap);
            }
        });

        profile_view_profile_phone_change_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_PHONE, new ProfileViewPhoneFragment(), paramsMap);
            }
        });

        profile_view_profile_gender_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), FRAGMENT_PROFILE_VIEW, FRAGMENT_PROFILE_VIEW_GENDER, new ProfileViewGenderFragment(), paramsMap);
            }
        });

        setupRanksAndMilestones();
    }

    private void setupRanksAndMilestones(){
        //current milestone
        if(loggedInUser.getCurrentRankAndMilestone() != null && !loggedInUser.getCurrentRankAndMilestone().isEmpty()){
            profile_view_current_rank_tv.setText(loggedInUser.getCurrentRank().toUpperCase());

            for(Milestone milestone : loggedInUser.getCurrentRankAndMilestone()) {
                if ("RECIPE".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_recipes_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_recipes_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                } else if ("LIKE".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_likes_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_likes_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                }
                else if ("REVIEW".equalsIgnoreCase(milestone.getTYPE())) {
                    if (milestone.getNUMBER() == 0) {
                        profile_view_current_reviews_tv.setText(String.valueOf(milestone.getCurrentNumber()));
                    } else {
                        profile_view_current_reviews_tv.setText(milestone.getCurrentNumber() + "/" + milestone.getNUMBER());
                    }
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the milestone : "+milestone.getTYPE());
                }
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Current rank & milestones are null");
        }
        //current milestone

        //next milestone
        if(loggedInUser.getNextRankAndMilestone() != null && !loggedInUser.getNextRankAndMilestone().isEmpty()){
            profile_view_next_rank_tv.setText(loggedInUser.getNextRankAndMilestone().get(0).getRANK_NAME().toUpperCase());

            for(Milestone milestone : loggedInUser.getNextRankAndMilestone()) {
                if ("RECIPE".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_recipes_tv.setText(String.valueOf(milestone.getNUMBER()));
                } else if ("LIKE".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_likes_tv.setText(String.valueOf(milestone.getNUMBER()));
                }
                else if ("REVIEW".equalsIgnoreCase(milestone.getTYPE())) {
                    profile_view_next_reviews_tv.setText(String.valueOf(milestone.getNUMBER()));
                }
                else{
                    Log.e(CLASS_NAME, "Error ! Could not identify the milestone : "+milestone.getTYPE());
                }
            }
        }
        else{
            Log.e(CLASS_NAME, "Error ! Next rank & milestones are null");
        }
        //Next milestone
    }

    public void updateName(String name){
        profile_view_profile_name_tv.setText(name);

        doUpdateLoggedInUser = true;
    }

    public void updateEmail(String email){
        profile_view_profile_email_tv.setText(email);
        profile_view_profile_email_verify_tv.setText("NOT VERIFIED");
        profile_view_profile_email_verify_tv.setTextColor(ContextCompat.getColor(mContext, R.color.red));

        doUpdateLoggedInUser = true;
    }

    public void updatePhone(String phone){
        profile_view_profile_phone_tv.setText(phone);
        profile_view_profile_phone_verify_tv.setText("NOT VERIFIED");
        profile_view_profile_phone_verify_tv.setTextColor(ContextCompat.getColor(mContext, R.color.red));

        doUpdateLoggedInUser = true;
    }

    public void updateGender(String gender){
        profile_view_profile_gender_tv.setText(Utility.getGender(gender));

        doUpdateLoggedInUser = true;
    }

    private void updatePhoto(String photoPath){
        loggedInUser.setIMG(photoPath);
        new AsyncTaskerUpdateUserImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onFinishDialog(Integer choice) {
        if (choice == null) {
            Log.e(CLASS_NAME, "Could not identify the choice : "+choice);
            return;
        }
        else if(GALLERY_CHOICE == choice){
            showPickImageFromGallery();
        }
        else if(CAMERA_CHOICE == choice){
            showPickImageFromCamera();
        }
    }

    private void showPickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
    }

    private void showPickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
            catch (IOException ex) {
                Log.e(CLASS_NAME, "An error occurred when getting image file from camera: ");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePathStr);
            updatePhoto(imagePathStr);
        }

        else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                File photoFile = createImageFile();

                OutputStream outputStream =  new FileOutputStream(photoFile);

                int read = 0;
                byte[] bytes = new byte[1024];

                InputStream is = mContext.getContentResolver().openInputStream(data.getData());

                while ((read = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                imagePathStr = photoFile.getAbsolutePath();

                updatePhoto(imagePathStr);
            }
            catch (Exception e){
                Log.e(CLASS_NAME, "Error while getting the image from the user choice");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        imagePathStr = image.getAbsolutePath();
        return image;
    }

    // Empty constructor required for DialogFragment
    public ProfileViewFragment() {}

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

    class AsyncTaskerUpdateUserImage extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.updateUserImage(loggedInUser);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "updating your photo..");
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO message = (MessageMO) object;

            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(message != null && !message.isError()){
                Utility.loadImageFromPath(mContext, loggedInUser.getIMG(), profile_view_profile_image_iv);
                doUpdateLoggedInUser = true;
            }
            else{
                if(message == null){
                    message = new MessageMO();
                    message.setError(true);
                    message.setErr_message("Something went wrong !");
                }

                message.setPurpose("USER_UPDATE_IMAGE_FAILED");
                Utility.showMessageDialog(getFragmentManager(), null, message);
            }
        }
    }
}
