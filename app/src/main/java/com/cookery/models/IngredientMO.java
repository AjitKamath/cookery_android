package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 27/8/17.
 */

@Data
public class IngredientMO implements Serializable {
    private int ING_ID;
    private String ING_NAME;
    private int QTY;
    private String IMG;

    private int ING_QTY;
    private String QTY_NAME;
    private QuantityMO quantity;
}
