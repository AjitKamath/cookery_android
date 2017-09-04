package com.cookery.models;

/**
 * Created by ajit on 27/8/17.
 */

public class IngredientMO extends QuantityMO {
    private int ING_ID;
    private String ING_NAME;
    private int QUANTITY;

    public int getING_ID() {
        return ING_ID;
    }

    public void setING_ID(int ING_ID) {
        this.ING_ID = ING_ID;
    }

    public String getING_NAME() {
        return ING_NAME;
    }

    public void setING_NAME(String ING_NAME) {
        this.ING_NAME = ING_NAME;
    }

    public int getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(int QUANTITY) {
        this.QUANTITY = QUANTITY;
    }
}
