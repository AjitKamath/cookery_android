package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 26/8/17.
 */

@Data
public class FoodTypeMO implements Serializable{
    private int FOOD_TYP_ID;
    private String FOOD_TYP_NAME;
    private String IS_DEF;
    private String IMG;
}
