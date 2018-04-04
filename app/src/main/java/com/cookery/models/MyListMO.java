package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by vishal on 24/12/17.
 */

@Data
public class MyListMO implements Serializable {
    private int LIST_ID;
    private String LIST_NAME;
    private int ING_AKA_ID;
    private String IS_CHECKED;
    private String ING_AKA_NAME;
    private int ITEM_COUNT;
    private List<IngredientAkaMO> listofingredients;
    private int USER_ID;
}
