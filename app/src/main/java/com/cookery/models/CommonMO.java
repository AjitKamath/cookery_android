package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 22/10/17.
 */

@Data
public class CommonMO implements Serializable {
    private int USER_ID;
    private String CREATE_DTM;
    private String MOD_DTM;
    private String IS_DEL;
}