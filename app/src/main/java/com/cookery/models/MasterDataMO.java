package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 4/9/17.
 */

@Getter
@Setter
public class MasterDataMO implements Serializable {
    private List<CuisineMO> foodCuisines;
    private List<FoodTypeMO> foodTypes;
    private List<IngredientUOMMO> ingredientUOMs;
    private List<TasteMO> tastes;
}
