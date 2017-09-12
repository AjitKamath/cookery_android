package com.cookery.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ajit on 27/8/17.
 */

public class IngredientMO implements Serializable {
    private int ING_ID;
    private String ING_NAME;
    private int QTY;
    private String IMG;

    private QuantityMO quantity;

    public QuantityMO getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityMO quantity) {
        this.quantity = quantity;
    }

    transient private Bitmap image;

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

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

    public int getQTY() {
        return QTY;
    }

    public void setQTY(int QTY) {
        this.QTY = QTY;
    }
}
