package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 23/9/17.
 */

@Getter
@Setter
public class MessageMO implements Serializable {
    private int err_code;
    private String err_message;
    private boolean isError;

    private String purpose;
    private transient Object object;
    private int user_id;
}
