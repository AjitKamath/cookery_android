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
    private int ING_UOM_VALUE;

    private String ING_UOM_NAME;
    private IngredientUOMMO quantity;
    private String ingredientCategoryName;
    private List<IngredientImageMO> images;
}
