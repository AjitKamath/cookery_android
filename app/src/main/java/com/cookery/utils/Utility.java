package com.cookery.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.cookery.fragments.WaitFragment;
import com.cookery.models.FoodTypeMO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.cookery.utils.Constants.FRAGMENT_COMMON_WAIT;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.PHP_FETCH_ALL_FOOD_TYPES;
import static com.cookery.utils.Constants.SERVER_ADDRESS;
import static com.cookery.utils.Constants.SERVER_TIMEOUT;
import static com.cookery.utils.Constants.SLASH;

public class Utility extends Activity {

    private static final String CLASS_NAME = Utility.class.getName();

	public static void showSnacks(ViewGroup viewGroup, String messageStr, final String doWhatStr, int duration){
        Snackbar snackbar = Snackbar.make(viewGroup, messageStr, duration).setAction(doWhatStr, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OK
                if(OK.equalsIgnoreCase(doWhatStr)){

                }
                else{
                    Log.e(CLASS_NAME, "Could not identify the action of the snacks");
                }
            }
        });

        snackbar.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!hasInternet){
            Log.i(CLASS_NAME, "Internet connection is available");
        }
        else{
            Log.i(CLASS_NAME, "No Internet connection available");
        }

        return hasInternet;
    }

    public static HttpURLConnection getHttpConnection(URL url, String method) throws Exception {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-length", "0");
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(SERVER_TIMEOUT);
        urlConnection.setAllowUserInteraction(false);
        urlConnection.connect();
        return urlConnection;
    }

    public static String getResponseFromBMB(HttpURLConnection connection) throws Exception {
        int status = connection.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return String.valueOf(sb);
        }

        return null;
    }

    public static Object jsonToObject(String jsonStr, Class mappingClass){
        if(jsonStr == null || jsonStr.isEmpty()){
            Log.e(CLASS_NAME, "JSON is null");
            return null;
        }
        else if (mappingClass == null){
            Log.e(CLASS_NAME, "No mapping class has been passed to map json into object");
            return null;
        }

        try{
            if(mappingClass.equals(FoodTypeMO.class)){
                Gson gson = new Gson();
                List<FoodTypeMO> list = gson.fromJson(jsonStr, new TypeToken<List<FoodTypeMO>>(){}.getType());
                return list;
            }
            else{
                Log.e(CLASS_NAME, mappingClass+" is not identified for parsing JSON");
            }
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Error in parsing the json("+jsonStr+") into the mapping class("+mappingClass+")");
        }

        return null;
    }

    public static Object fetchAllFoodTypes() {
        try {
            URL url = new URL(SERVER_ADDRESS+SLASH+PHP_FETCH_ALL_FOOD_TYPES);
            HttpURLConnection connection = Utility.getHttpConnection(url, "GET");
            String jsonStr = Utility.getResponseFromBMB(connection);
            return Utility.jsonToObject(jsonStr, FoodTypeMO.class);
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "Could not fetch Categories from the server : "+e);
        }

        return null;
    }


    public static Fragment showWaitDialog(FragmentManager fragManager, String message) {
        String fragmentNameStr = FRAGMENT_COMMON_WAIT;

        Fragment frag = fragManager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            fragManager.beginTransaction().remove(frag).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, message);

        WaitFragment fragment = new WaitFragment();
        fragment.setArguments(bundle);

        fragment.show(fragManager, fragmentNameStr);

        return fragment;
    }
}
