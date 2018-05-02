package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cookery.R;
import com.cookery.adapters.ImagesFullscreenViewPagerAdapter;
import com.cookery.models.CommentMO;
import com.cookery.models.LikesMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_IMAGES;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class ImagesFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_images_fullscreen_vp)
    ViewPager common_images_fullscreen_vp;

    @InjectView(R.id.common_images_fullscreen_count_tv)
    TextView recipe_view_images_fullscreen_count_tv;
    //end of components

    @InjectView(R.id.common_images_download)
    ImageView recipe_view_images_download;

    private Object object;
    private int imageposition;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_images_fullscreen, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setHasOptionsMenu(true);

        return view;
    }

    private void getDataFromBundle() {
        object = getArguments().get(GENERIC_OBJECT);
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        Object array[] = (Object[]) object;

        int imageIndex = (Integer) array[0];
        final List<? extends Object> images = (List<? extends Object>)array[1];

        common_images_fullscreen_vp.setAdapter(new ImagesFullscreenViewPagerAdapter(mContext, images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (R.id.common_like_view_ll == view.getId()) {
                    Utility.addRemoveLike(view);

                    LikesMO like = new LikesMO();
                    if(view.getTag() instanceof RecipeImageMO){
                        RecipeImageMO image = (RecipeImageMO) view.getTag();

                        if (image == null) {
                            Log.e(CLASS_NAME, "Image is null/empty");
                            return;
                        }

                        like.setUSER_ID(loggedInUser.getUSER_ID());
                        like.setTYPE("RECIPE_IMG");
                        like.setTYPE_ID(image.getRCP_IMG_ID());
                    }
                    else if(view.getTag() instanceof UserMO){
                        UserMO image = (UserMO) view.getTag();

                        if (image == null) {
                            Log.e(CLASS_NAME, "Image is null/empty");
                            return;
                        }

                        like.setUSER_ID(loggedInUser.getUSER_ID());
                        like.setTYPE("USER");
                        like.setTYPE_ID(image.getUSER_ID());
                    }

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_IMAGES,
                            AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
                } else if (R.id.common_images_fullscreen_item_comments_ll == view.getId()) {
                    CommentMO comment = new CommentMO();
                    if(view.getTag() instanceof RecipeImageMO){
                        RecipeImageMO image = (RecipeImageMO) view.getTag();

                        if(image == null){
                            Log.e(CLASS_NAME, "Image is null/empty");
                            return;
                        }

                        comment.setTYPE_ID(image.getRCP_IMG_ID());
                        comment.setTYPE("RECIPE_IMG");
                        comment.setRecipeImage(image.getRCP_IMG());
                    }
                    else if(view.getTag() instanceof UserMO){
                        UserMO image = (UserMO) view.getTag();

                        if(image == null){
                            Log.e(CLASS_NAME, "Image is null/empty");
                            return;
                        }

                        comment.setTYPE_ID(image.getUSER_ID());
                        comment.setTYPE("USER");
                        comment.setRecipeImage(image.getIMG());
                    }

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_RECIPE,
                            AsyncTaskUtility.Purpose.FETCH_COMMENTS, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, comment, 0);
                } else {
                    Log.e(CLASS_NAME, "Could not identify the purpose of event on this view");
                }
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        }));
        common_images_fullscreen_vp.setCurrentItem(imageIndex);

        common_images_fullscreen_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateImageCounter(++position, images.size());
                imageposition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateImageCounter(++imageIndex, images.size());

        recipe_view_images_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.images_download_options_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(mContext, "Downloading ..", Toast.LENGTH_LONG).show();

                        switch (item.getItemId()) {
                            case R.id.images_download_current_menu_item:
                                List<Object> temp = new ArrayList<>();
                                temp.add(images.get(imageposition));
                                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_IMAGES, AsyncTaskUtility.Purpose.DOWNLOAD_IMAGES, loggedInUser, 0)
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, temp);
                                return true;

                            case R.id.images_download_all_menu_item:
                                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_IMAGES, AsyncTaskUtility.Purpose.DOWNLOAD_IMAGES, loggedInUser, 0)
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, images);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void showDownloadComplete(){
        Toast.makeText(mContext, "Download complete !", Toast.LENGTH_LONG).show();
    }

    private void updateImageCounter(int index, int maxCount) {
        recipe_view_images_fullscreen_count_tv.setText(index + "/" + maxCount);
    }

    // Empty constructor required for DialogFragment
    public ImagesFragment() {
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
        }
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
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
}
