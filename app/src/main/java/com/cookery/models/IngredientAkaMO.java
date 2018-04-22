package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 27/8/17.
 */

@Getter
@Setter
public class IngredientAkaMO implements Serializable {
    private int ING_AKA_ID;
    private int ING_ID;
    private String ING_AKA_NAME;
    private int QTY;

    private int ING_QTY;
    private String QTY_NAME;
    private QuantityMO quantity;
    private String ingredientCategoryName;
    private List<IngredientImageMO> images;
}
