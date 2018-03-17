package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by ajit on 4/9/17.
 */

@Data
public class MasterDataMO implements Serializable {
    private List<CuisineMO> cuisines;
    private List<FoodTypeMO> foodTypes;
    private List<QuantityMO> quantities;
    private List<TasteMO> tastes;
}
