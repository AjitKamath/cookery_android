package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ajit on 4/9/17.
 */

public class MasterDataMO implements Serializable {
    private List<CuisineMO> cuisines;
    private List<FoodTypeMO> foodTypes;
    private List<QuantityMO> quantities;

    public List<CuisineMO> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<CuisineMO> cuisines) {
        this.cuisines = cuisines;
    }

    public List<FoodTypeMO> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(List<FoodTypeMO> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public List<QuantityMO> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<QuantityMO> quantities) {
        this.quantities = quantities;
    }
}
