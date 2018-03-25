package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 27/8/17.
 */

@Data
public class CuisineMO implements Serializable {
    private int FOOD_CSN_ID;
    private String FOOD_CSN_NAME;
    private String IMG;
    private String IS_DEF;
}
