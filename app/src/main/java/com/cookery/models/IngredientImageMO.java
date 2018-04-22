package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 17/3/18.
 */

@Getter
@Setter
public class IngredientImageMO implements Serializable{
    private int ING_IMG_ID;
    private String ING_IMG;
    private int ING_ID;
    private String IS_DEF;
}
