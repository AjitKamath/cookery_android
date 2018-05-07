package com.cookery.exceptions;

import android.app.FragmentManager;
import android.util.Log;

import com.cookery.fragments.NoInternetFragment;
import com.cookery.fragments.SomethingWrongFragment;
import com.cookery.utils.Utility;

import static com.cookery.utils.Constants.FRAGMENT_NO_INTERNET;
import static com.cookery.utils.Constants.FRAGMENT_SOMETHING_WRONG;

public class CookeryException extends RuntimeException {
    private static final long serialVersionUID = 7718828512143293558L;
    private static final String CLASS_NAME = CookeryException.class.getName();

    private final ErrorCode code;
    private FragmentManager fragmentManager;

    public CookeryException(ErrorCode code, Exception e) {
        super(e);
        this.code = code;
    }

    public enum ErrorCode{
        SOMETHING_WRONG, NO_INTERNET, ACCESS_DENIED, GATEWAY_TIMEOUT, JSON_TO_OBJECT_MAPPING_ERROR, NO_JSON,
        NO_JSON_MAPPING_CLASS, BAD_JSON;
    }

    public CookeryException(ErrorCode code) {
        super();
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
    }

    public CookeryException(ErrorCode code, FragmentManager fragmentManager, String message, Throwable cause) {
        super();
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
        Log.e(CLASS_NAME, message);
        Log.e(CLASS_NAME, cause.toString());

        if(code == ErrorCode.SOMETHING_WRONG){
            Utility.showFragment(fragmentManager, null, FRAGMENT_SOMETHING_WRONG, new SomethingWrongFragment(), null);
        }
        else if(code == ErrorCode.NO_INTERNET){
            Utility.showFragment(fragmentManager, null, FRAGMENT_NO_INTERNET, new NoInternetFragment(), null);
        }
    }

    public CookeryException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
        Log.e(CLASS_NAME, message);
        Log.e(CLASS_NAME, cause.toString());
    }
    public CookeryException(String message, ErrorCode code) {
        super(message);
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
        Log.e(CLASS_NAME, message);
    }
    public CookeryException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
        Log.e(CLASS_NAME, cause.toString());
    }
    public ErrorCode getCode() {
        return this.code;
    }
}
