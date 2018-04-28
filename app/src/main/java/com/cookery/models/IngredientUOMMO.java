package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 29/8/17.
 */

@Getter
@Setter
public class IngredientUOMMO implements Serializable {
    private int ING_UOM_ID;
    private String ING_UOM_NAME;
    private String IS_DEF;
}
