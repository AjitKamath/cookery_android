package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 23/9/17.
 */

public class MessageMO implements Serializable {
    private int err_code;
    private String err_message;
    private boolean isError;

    private String purpose;
    private Object object;
    private int user_id;

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
