package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
                                new AsyncTaskerDownloadRecipeImages().execute(images.get(imageposition));
                                return true;

                            case R.id.recipe_images_download_all:
                                new AsyncTaskerDownloadAllRecipeImages().execute(images);
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

    class AsyncTaskerDownloadRecipeImages extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(mContext, "Downloading Recipe Image", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... objects) {
            URL url;
            String storeDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + GALLERY_DIR;
            try {
                String image = SERVER_ADDRESS+objects[0].toString();
                url = new URL(image);
                String pathl="";
                int count;
                try {
                    File f=new File(storeDir);
                    if(!f.exists()) {
                        f.mkdirs();
                    }
                        HttpURLConnection con=(HttpURLConnection)url.openConnection();
                        InputStream is=con.getInputStream();
                        String pathr=url.getPath();
                        String filename=pathr.substring(pathr.lastIndexOf('/')+1);
                        pathl=storeDir+"/"+filename;
                        FileOutputStream fos=new FileOutputStream(pathl);

                        byte data[] = new byte[1024];
                        long total = 0;
                        while ((count = is.read(data)) != -1) {
                            total += count;
                            // writing data to output file
                            fos.write(data, 0, count);

                        }
                        is.close();
                        fos.flush();
                        fos.close();

                } catch (Exception e) {
                    Toast.makeText(mContext, "Download failed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            } catch (MalformedURLException e) {
                Toast.makeText(mContext, "Download failed", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(mContext, "Download Completed", Toast.LENGTH_LONG).show();
        }
    }

    class AsyncTaskerDownloadAllRecipeImages extends AsyncTask<List<String>, String, Void> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(mContext, "Downloading Recipe Images", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(List<String>... objects) {
            URL url;
            String storeDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + GALLERY_DIR;

            for(int i=0; i<objects[0].size(); i++) {
                    try {
                        String image = SERVER_ADDRESS + objects[0].get(i).toString();
                        url = new URL(image);
                        String pathl = "";
                        int count;

                        File f = new File(storeDir);
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        InputStream is = con.getInputStream();
                        String pathr = url.getPath();
                        String filename = pathr.substring(pathr.lastIndexOf('/') + 1);
                        pathl = storeDir + "/" + filename;
                        FileOutputStream fos = new FileOutputStream(pathl);

                        byte data[] = new byte[1024];
                        long total = 0;
                        publishProgress(objects[0].get(i));
                        while ((count = is.read(data)) != -1) {
                            total += count;
                            // writing data to output file
                            fos.write(data, 0, count);
                        }

                        is.close();
                        fos.flush();
                        fos.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(mContext, "Download Completed", Toast.LENGTH_LONG).show();
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
