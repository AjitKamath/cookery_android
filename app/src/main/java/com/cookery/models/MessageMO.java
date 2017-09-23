package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 23/9/17.
 */

public class MessageMO implements Serializable {
    private int err_code;
    private String err_message;
    private boolean isError;

    public String getErr_message() {
        return err_message;
    }

    public void setErr_message(String err_message) {
        this.err_message = err_message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public int getErr_code() {

        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }
}
