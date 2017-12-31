package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vishal on 24/12/17.
 */

public class MyListMO implements Serializable {

    private int LIST_ID;
    private String LIST_NAME;
    private int ING_ID;
    private String IS_CHECKED;
    private String ING_NAME;
    private int ITEM_COUNT;
    private List<IngredientMO> listofingredients;
    private int USER_ID;



    public int getLIST_ID() {
        return LIST_ID;
    }

    public void setLIST_ID(int LIST_ID) {
        this.LIST_ID = LIST_ID;
    }

    public String getLIST_NAME() {
        return LIST_NAME;
    }

    public void setLIST_NAME(String LIST_NAME) {
        this.LIST_NAME = LIST_NAME;
    }

    public int getING_ID() {
        return ING_ID;
    }

    public void setING_ID(int ING_ID) {
        this.ING_ID = ING_ID;
    }

    public String getIS_CHECKED() {
        return IS_CHECKED;
    }

    public void setIS_CHECKED(String IS_CHECKED) {
        this.IS_CHECKED = IS_CHECKED;
    }

    public String getING_NAME() {
        return ING_NAME;
    }

    public void setING_NAME(String ING_NAME) {
        this.ING_NAME = ING_NAME;
    }


    public int getITEM_COUNT() {
        return ITEM_COUNT;
    }

    public void setITEM_COUNT(int ITEM_COUNT) {
        this.ITEM_COUNT = ITEM_COUNT;
    }

    public List<IngredientMO> getListofingredients() {
        return listofingredients;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setListofingredients(List<IngredientMO> listofingredients) {
        this.listofingredients = listofingredients;
    }





}
