package com.cookery.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SERVER_TIMEOUT;

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


}
