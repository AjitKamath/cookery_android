package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.AddRecipeViewPagerAdapter;
import com.cookery.component.ViewPagerCustom;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.app.Activity.RESULT_OK;
import static com.cookery.utils.Constants.CAMERA_CHOICE;
import static com.cookery.utils.Constants.GALLERY_CHOICE;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.REQUEST_GALLERY_PHOTO;
import static com.cookery.utils.Constants.REQUEST_TAKE_PHOTO;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 25/8/17.
 */

public class AddRecipeFragment extends DialogFragment {

    private Context mContext;
    private static final String CLASS_NAME = AddRecipeFragment.class.getName();

    @InjectView(R.id.fragment_add_recipe_vp)
    ViewPagerCustom fragment_add_recipe_vp;

    @InjectView(R.id.common_fragment_header_close_iv)
    ImageView common_fragment_header_close_iv;

    @InjectView(R.id.common_fragment_header_back_iv)
    ImageView common_fragment_header_back_iv;

    @InjectView(R.id.common_fragment_header_forward_iv)
    ImageView common_fragment_header_forward_iv;

    private MasterDataMO masterData;

    private String imagePathStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        masterData = (MasterDataMO) getArguments().get(MASTER);
    }

    private void setupPage() {
        setupSliders();

        common_fragment_header_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        common_fragment_header_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePage(fragment_add_recipe_vp.getCurrentItem() - 1);
            }
        });

        common_fragment_header_forward_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePage(fragment_add_recipe_vp.getCurrentItem() + 1);
            }
        });

    }

    private void changePage(int page){
        if(page == 1){
            String recipeName = String.valueOf(((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).view_pager_add_recipe_1_recipe_name_et.getText());
            if(recipeName == null || recipeName.trim().isEmpty()){
                Utility.showSnacks(fragment_add_recipe_vp, "Enter Recipe Name", OK, Snackbar.LENGTH_LONG);
                return;
            }
            else{
                ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setRecipeName();
            }
        }

        if(page == 4){
            if(((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).view_pager_add_recipe_4_ingedients_gv.getAdapter().isEmpty()){
                Utility.showSnacks(fragment_add_recipe_vp, "Enter Ingredients", OK, Snackbar.LENGTH_LONG);
                return;
            }
            else{
                ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setIngredients();
            }
        }

        if(page == 5){
            String recipe = String.valueOf(((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).view_pager_add_recipe_5_recipe_et.getText());
            if(recipe == null || recipe.trim().isEmpty()){
                Utility.showSnacks(fragment_add_recipe_vp, "Enter Recipe", OK, Snackbar.LENGTH_LONG);
                return;
            }
            else{
                ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setRecipe();
            }
        }

        fragment_add_recipe_vp.setCurrentItem(page);
    }

    private void setupSliders() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_1);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_2);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_3);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_4);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_5);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_6);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_7);
        viewPagerTabsList.add(R.layout.view_pager_add_recipe_8);

        final AddRecipeViewPagerAdapter viewPagerAdapter = new AddRecipeViewPagerAdapter(mContext, getFragmentManager(), viewPagerTabsList, masterData, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeMO recipe = ((AddRecipeViewPagerAdapter)fragment_add_recipe_vp.getAdapter()).recipe;

                if(recipe.getImages() == null || recipe.getImages().isEmpty()){
                    Utility.showSnacks(fragment_add_recipe_vp, "Add atleast one photo", OK, Snackbar.LENGTH_LONG);
                    return;
                }

                new AsyncTasker().execute(recipe);
            }
        });

        int activePageIndex = 0;
        if (fragment_add_recipe_vp != null && fragment_add_recipe_vp.getAdapter() != null) {
            activePageIndex = fragment_add_recipe_vp.getCurrentItem();
        }

        fragment_add_recipe_vp.setAdapter(viewPagerAdapter);
        fragment_add_recipe_vp.setCurrentItem(activePageIndex);
        fragment_add_recipe_vp.setOffscreenPageLimit(viewPagerTabsList.size());
        fragment_add_recipe_vp.setPagingEnabled(true); //TODO: set false in prod

        //hide back button initially
        common_fragment_header_back_iv.setVisibility(View.GONE);

        fragment_add_recipe_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                common_fragment_header_close_iv.setVisibility(View.VISIBLE);
                common_fragment_header_back_iv.setVisibility(View.VISIBLE);
                common_fragment_header_forward_iv.setVisibility(View.VISIBLE);

                //hide close
                if (position == 0) {
                    common_fragment_header_back_iv.setVisibility(View.GONE);
                }
                //hide forward
                else if (position == viewPagerTabsList.size() - 1) {
                    common_fragment_header_forward_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setFoodType(FoodTypeMO foodType) {
        ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setFoodType(foodType);
    }

    public void setCuisine(CuisineMO cuisine) {
        ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setCuisine(cuisine);
    }

    private void setPhoto(String photoPath){
        ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).setPhotos(photoPath);
    }

    public void addIngredient(IngredientMO ingredient){
        ((AddRecipeViewPagerAdapter) fragment_add_recipe_vp.getAdapter()).addIngredient(ingredient);
    }

    // Empty constructor required for DialogFragment
    public AddRecipeFragment() {
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
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.setCanceledOnTouchOutside(false);
        }
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
            setPhoto(imagePathStr);
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

                setPhoto(imagePathStr);
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

    //method iterates over each component in the activity and when it finds a text view..sets its font
    private void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }

    class AsyncTasker extends AsyncTask<Object, Void, Object> {
        android.app.Fragment frag = null;

        @Override
        protected void onPreExecute(){
            frag = Utility.showWaitDialog(getFragmentManager(), "Submitting your Recipe ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.submitRecipe((RecipeMO) objects[0]);
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO message = (MessageMO) object;

            if(message.isError()){
                Log.e(CLASS_NAME, "Error : "+message.getErr_message());
            }
            else{
                Log.i(CLASS_NAME, message.getErr_message());
                dismiss();
            }

            Utility.closeWaitDialog(getFragmentManager(), frag);
            Utility.showMessageDialog(getFragmentManager(), message);
        }
    }
}