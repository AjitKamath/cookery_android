package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andremion.louvre.home.GalleryActivity;
import com.cookery.R;
import com.cookery.adapters.RecipeAddImagesViewPagerAdapter;
import com.cookery.adapters.RecipeAddViewPagerAdapter;
import com.cookery.animators.HeightWidthAnimator;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.app.Activity.RESULT_OK;
import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_PICK_IMAGE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOUVRE_REQUEST_CODE;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 25/8/17.
 */

public class RecipeAddFragment extends DialogFragment {

    private Context mContext;
    private static final String CLASS_NAME = RecipeAddFragment.class.getName();

    private UserMO loggerInUser;

    /*components*/
    @InjectView(R.id.recipe_add_rl)
    RelativeLayout recipe_add_rl;

    @InjectView(R.id.recipe_add_close_iv)
    ImageView recipe_add_close_iv;

    @InjectView(R.id.recipe_add_images_iv)
    ImageView recipe_add_images_iv;

    @InjectView(R.id.recipe_add_images_vp)
    ViewPager recipe_add_images_vp;

    @InjectView(R.id.recipe_add_images_count_tv)
    TextView recipe_add_images_count_tv;

    @InjectView(R.id.recipe_add_images_tv)
    TextView recipe_add_images_tv;

    @InjectView(R.id.recipe_add_header_ll)
    LinearLayout recipe_add_header_ll;

    @InjectView(R.id.recipe_add_collapse_expand_iv)
    ImageView recipe_add_collapse_expand_iv;

    @InjectView(R.id.recipe_add_tl)
    TabLayout recipe_add_tl;

    @InjectView(R.id.recipe_add_tabs_vp)
    ViewPager recipe_add_tabs_vp;

    @InjectView(R.id.recipe_add_submit_fab)
    FloatingActionButton recipe_add_submit_fab;
    /*components*/

    private RecipeMO recipe;
    private MasterDataMO masterData;
    private static final int ANIMATION_SPEED = 250;

    public List<Uri> selectedImages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_add, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        getLoggedInUser();

        setupPage();

        setFont(recipe_add_rl);

        return view;
    }

    private void getLoggedInUser() {
        loggerInUser = Utility.getUserFromUserSecurity(mContext);
    }

    private void getDataFromBundle() {
        masterData = (MasterDataMO) getArguments().get(MASTER);
        if(masterData == null || masterData.getFoodCuisines() == null || masterData.getFoodCuisines().isEmpty()
                || masterData.getFoodTypes() == null || masterData.getFoodTypes().isEmpty()
                || masterData.getIngredientUOMs() == null || masterData.getIngredientUOMs().isEmpty()){

            Log.e(CLASS_NAME, "Error ! Master data is null or required data in master data is not found !");
            dismiss();
        }

        recipe = (RecipeMO) getArguments().get(GENERIC_OBJECT);
    }

    private boolean validate() {
        if(recipe == null){
            Log.e(CLASS_NAME, "Error ! Recipe object is null");
            return false;
        }

        if(recipe.getImages() == null || recipe.getImages().isEmpty()){
            Utility.showSnacks(recipe_add_rl, "Add atleast one recipe image !", OK, Snackbar.LENGTH_SHORT);
            return false;
        }

        if(recipe.getRCP_NAME() == null || recipe.getRCP_NAME().isEmpty()){
            recipe_add_tabs_vp.setCurrentItem(0);
            Utility.showSnacks(recipe_add_rl, "Recipe name is empty !", OK, Snackbar.LENGTH_SHORT);
            return false;
        }

        if(recipe.getIngredients() == null || recipe.getIngredients().isEmpty()){
            recipe_add_tabs_vp.setCurrentItem(1);
            Utility.showSnacks(recipe_add_rl, "Recipe ingredients are empty !", OK, Snackbar.LENGTH_SHORT);
            return false;
        }

        if(recipe.getSteps() == null || recipe.getSteps().isEmpty()){
            recipe_add_tabs_vp.setCurrentItem(2);
            Utility.showSnacks(recipe_add_rl, "Recipe steps are empty !", OK, Snackbar.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    private void setupPage() {
        setupImages(new ArrayList<Uri>());
        setupTabs();
        collapseContent();

        recipe_add_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageMO message = new MessageMO();
                message.setErr_message("Your Recipe will be lost");
                message.setPurpose("CLOSE_ADD_RECIPE");
                message.setError(false);

                Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_ADD_RECIPE);
                Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
            }
        });

        recipe_add_submit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeAddViewPagerAdapter adapter = (RecipeAddViewPagerAdapter)recipe_add_tabs_vp.getAdapter();
                adapter.setRecipeName();
                updateRecipeObject(adapter.recipe);

                if(validate()){
                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_ADD_RECIPE,
                            AsyncTaskUtility.Purpose.SUBMIT_RECIPE, loggerInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, recipe);

                    //new AsyncTasker().execute(recipe);
                }
            }
        });
    }

    private void updateRecipeObject(RecipeMO recipe) {
        this.recipe.setUSER_ID(Utility.getUserFromUserSecurity(mContext).getUSER_ID());
        this.recipe.setRCP_NAME(recipe.getRCP_NAME());
        this.recipe.setFOOD_TYP_ID(recipe.getFOOD_TYP_ID());
        this.recipe.setFOOD_CSN_ID(recipe.getFOOD_CSN_ID());
        this.recipe.setIngredients(recipe.getIngredients());
        this.recipe.setSteps(recipe.getSteps());
        //this.recipe.setTastes(recipe.getTastes());
    }

    private void setupTabs() {
        final List<Integer> viewPagerTabsList = new ArrayList<>();
        viewPagerTabsList.add(R.layout.recipe_add_recipe_main);
        viewPagerTabsList.add(R.layout.recipe_add_ingredients);
        viewPagerTabsList.add(R.layout.recipe_add_recipe_steps);

        for(Integer iter : viewPagerTabsList){
            recipe_add_tl.addTab(recipe_add_tl.newTab());
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recipe_add_tabs_vp.setAdapter(new RecipeAddViewPagerAdapter(mContext, getFragmentManager(), viewPagerTabsList, recipe, masterData, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);

                expandContent();

                return false;
            }
        }));
        recipe_add_tabs_vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(recipe_add_tl));
        recipe_add_tabs_vp.setOffscreenPageLimit(viewPagerTabsList.size());

        recipe_add_tl.setupWithViewPager(recipe_add_tabs_vp);
        recipe_add_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recipe_add_tabs_vp.setCurrentItem(tab.getPosition());

                String tabText = String.valueOf(tab.getText());
                if(tabText.equalsIgnoreCase("STEPS") || tabText.equalsIgnoreCase("ING.")){
                    expandContent();
                }
                else{
                    collapseContent();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void expandContent(){
        recipe_add_collapse_expand_iv.setVisibility(View.VISIBLE);
        recipe_add_collapse_expand_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseContent();
            }
        });

        recipe_add_header_ll.startAnimation(new HeightWidthAnimator(recipe_add_header_ll, 0, HeightWidthAnimator.Type.HEIGHT, ANIMATION_SPEED));
    }

    private void collapseContent(){
        recipe_add_collapse_expand_iv.setVisibility(View.GONE);
        recipe_add_header_ll.startAnimation(new HeightWidthAnimator(recipe_add_header_ll, ViewGroup.LayoutParams.WRAP_CONTENT, HeightWidthAnimator.Type.HEIGHT, ANIMATION_SPEED));
    }

    private void setupImages(List<Uri> images) {
        selectedImages.addAll(images);

        recipe_add_images_vp.setAdapter(new RecipeAddImagesViewPagerAdapter(mContext, selectedImages, false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.recipe_add_images_item_close_iv){
                    Uri recipeImage = (Uri) view.getTag();
                    for(Uri image : selectedImages){
                        if(image.getPath().equalsIgnoreCase(recipeImage.getPath())){
                            selectedImages.remove(image);
                            break;
                        }
                    }

                    setupImages(new ArrayList<Uri>());
                }
            }
        }));
        recipe_add_images_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateImagesCount();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        recipe_add_images_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showFragment(getFragmentManager(), FRAGMENT_ADD_RECIPE, FRAGMENT_PICK_IMAGE, new PickPhotoFragment(), null);
            }
        });

        if(recipe_add_images_vp.getAdapter().getCount() != 0){
            recipe_add_images_vp.setCurrentItem(recipe_add_images_vp.getAdapter().getCount()-1);
        }

        recipe.setImages(convertUriToRecipeImages());
        updateImagesCount();
    }

    public void setFoodType(FoodTypeMO foodType) {
        ((RecipeAddViewPagerAdapter) recipe_add_tabs_vp.getAdapter()).setFoodType(foodType);
    }

    public void setCuisine(CuisineMO cuisine) {
        ((RecipeAddViewPagerAdapter) recipe_add_tabs_vp.getAdapter()).setCuisine(cuisine);
    }

    private void setPhoto(Uri photoPath){
        List<Uri> image = new ArrayList<>();
        image.add(photoPath);

        setupImages(image);
    }

    public void addIngredient(IngredientAkaMO ingredient){
        ((RecipeAddViewPagerAdapter) recipe_add_tabs_vp.getAdapter()).addIngredient(ingredient);
    }

    private void updateImagesCount(){
        int imagesCount = 0;
        int currentImage = 0;

        if(recipe_add_images_vp.getAdapter() != null){
            imagesCount = recipe_add_images_vp.getAdapter().getCount();

            if(imagesCount != 0){
                currentImage = recipe_add_images_vp.getCurrentItem()+1;
            }
        }

        if(currentImage > imagesCount){
            currentImage = imagesCount;
            recipe_add_images_vp.setCurrentItem(currentImage-1);
        }

        recipe_add_images_count_tv.setText(currentImage+"/"+imagesCount);

        if(imagesCount > 0){
            recipe_add_images_iv.setVisibility(View.GONE);
            recipe_add_images_vp.setVisibility(View.VISIBLE);
        }
        else{
            recipe_add_images_iv.setVisibility(View.VISIBLE);
            recipe_add_images_vp.setVisibility(View.GONE);
        }
    }

    // Empty constructor required for DialogFragment
    public RecipeAddFragment() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PickPhotoFragment pickPhotoFragment = (PickPhotoFragment) getFragmentManager().findFragmentByTag(FRAGMENT_PICK_IMAGE);

        if (requestCode == LOUVRE_REQUEST_CODE && resultCode == GalleryActivity.RESULT_OK) {
            List<Uri> tempImages = GalleryActivity.getSelection(data);
            selectedImages.clear();
            setupImages(tempImages);

            pickPhotoFragment.dismiss();
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                setPhoto(result.getUri());
                pickPhotoFragment.dismiss();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(CLASS_NAME, "Error ! Something went wrong ! : "+error.getMessage());
            }
        }
    }

    private List<RecipeImageMO> convertUriToRecipeImages(){
        RecipeImageMO recipeImage = null;
        List<RecipeImageMO> recipeImages = new ArrayList<>();
        for(Uri image : selectedImages){
            recipeImage = new RecipeImageMO();
            recipeImage.setRCP_IMG(image.getPath());
            recipeImages.add(recipeImage);
        }

        return recipeImages;
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    private void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}