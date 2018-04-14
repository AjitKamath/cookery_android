package com.cookery.exceptions;

import android.util.Log;

public class CookeryException extends RuntimeException {
    private static final long serialVersionUID = 7718828512143293558L;
    private static final String CLASS_NAME = CookeryException.class.getName();

    private final ErrorCode code;

    public CookeryException(ErrorCode code, Exception e) {
        super(e);
        this.code = code;
    }

    public enum ErrorCode{
        SOMETHING_WRONG, NO_INTERNET, ACCESS_DENIED, JSON_TO_OBJECT_MAPPING_ERROR, NO_JSON,
        NO_JSON_MAPPING_CLASS, BAD_JSON;
    }

    public CookeryException(ErrorCode code) {
        super();
        this.code = code;

        Log.e(CLASS_NAME, "Cookery Exception : ");
        Log.e(CLASS_NAME, code.toString());
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
