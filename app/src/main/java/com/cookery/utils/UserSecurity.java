package com.cookery.utils;

/**
 * Created by ajit on 8/10/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.cookery.R;
import com.cookery.models.UserMO;

public class UserSecurity{
    private static final String CLASS_NAME = UserSecurity.class.getName();

    private Editor editor;
    private SharedPreferences pref;
    public static String DEFAULT_VALUE = "";

    public UserSecurity(Context context){
        pref = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void write(String key, Object object){
        String json = Utility.obectToJson(object);

        if(json == null || json.trim().isEmpty()){
            Log.e(CLASS_NAME, "Invalid JSON : "+json);
            return;
        }

        editor.putString(key, json);
        editor.commit();
    }

    public Object read(String key) {
        String json = pref.getString(key, DEFAULT_VALUE);

        if(json == null || json.trim().isEmpty()){
            Log.e(CLASS_NAME, "Invalid JSON : "+json);
            return null;
        }

        return Utility.jsonToObject(json, UserMO.class);
    }
}
