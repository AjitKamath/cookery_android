package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
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
import com.cookery.adapters.RecipeViewImagesFullscreenViewPagerAdapter;
import com.cookery.utils.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GALLERY_DIR;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewImagesFragment extends DialogFragment  {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.recipe_view_images_fullscreen_vp)
    ViewPager recipe_view_images_fullscreen_vp;

    @InjectView(R.id.recipe_view_images_fullscreen_count_tv)
    TextView recipe_view_images_fullscreen_count_tv;
    //end of components

    @InjectView(R.id.recipe_view_images_download)
    ImageView recipe_view_images_download;

    private Object object;
    private int imageposition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view_images_fullscreen, container);
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
    }

    private void setupPage() {
        Object array[] = (Object[])object;

        int imageIndex = (Integer)array[0];
        final List<String> images = (List<String>) array[1];

        recipe_view_images_fullscreen_vp.setAdapter(new RecipeViewImagesFullscreenViewPagerAdapter(mContext, images, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
        recipe_view_images_fullscreen_vp.setCurrentItem(imageIndex);

        recipe_view_images_fullscreen_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                popupMenu.getMenuInflater().inflate(R.menu.recipe_images_download_options_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.recipe_images_download_current:
                                Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_LONG).show();
                                new AsyncTaskerDownloadRecipeImages().execute(images.get(imageposition));
                                return true;

                            case R.id.recipe_images_download_all:
                                Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_LONG).show();
                                new AsyncTaskerDownloadRecipeImages().execute(images.get(imageposition));
                                return true;

                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    class AsyncTaskerDownloadRecipeImages extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "Downloading Images..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            getImage((String) objects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            Bitmap imageToSave = (Bitmap) object;
            if(imageToSave != null){
                createDirectoryAndSaveFile(imageToSave);
            Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }

    public void getImage(String image)
    {
        try{
            Picasso.with(mContext)
                    .load(SERVER_ADDRESS + image)
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  try {
                                      String root = Environment.getExternalStorageDirectory().toString();
                                      File myDir = new File(root + "/yourDirectory");

                                      if (!myDir.exists()) {
                                          myDir.mkdirs();
                                      }

                                      String name = new Date().toString() + ".jpg";
                                      myDir = new File(myDir, name);
                                      FileOutputStream out = new FileOutputStream(myDir);
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                      out.flush();
                                      out.close();
                                  } catch(Exception e){
                                      // some action
                                  }
                              }

                              @Override
                              public void onBitmapFailed(Drawable errorDrawable) {
                              }

                              @Override
                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                              }
                          }
                    );
        }

       /* try {
            Picasso.with(mContext).load(SERVER_ADDRESS + image).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try{
                        object = bitmap;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }*/
        catch (Exception e)
        {
            e.getMessage();
        }
       // return object;
    }




    private void createDirectoryAndSaveFile(Bitmap image) {

        try {
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + GALLERY_DIR);
        if (!direct.exists()) {
            direct.mkdirs();
//            File wallpaperDirectory = new File("/sdcard/DirName/");
  //          wallpaperDirectory.mkdirs();
        }

        File file = new File(direct, "Test_image");
        if (file.exists())
            file.delete();

            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateImageCounter(int index, int maxCount){
        recipe_view_images_fullscreen_count_tv.setText(index+"/"+maxCount);
    }

    // Empty constructor required for DialogFragment
    public RecipeViewImagesFragment() {
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
