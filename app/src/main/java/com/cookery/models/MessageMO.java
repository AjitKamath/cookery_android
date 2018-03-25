package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 23/9/17.
 */

@Data
public class MessageMO implements Serializable {
    private int err_code;
    private String err_message;
    private boolean isError;

    private String purpose;
    private Object object;
    private int user_id;
}
