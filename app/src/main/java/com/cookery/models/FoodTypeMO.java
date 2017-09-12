package com.cookery.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ajit on 26/8/17.
 */

public class FoodTypeMO implements Serializable{
    private int FOOD_TYP_ID;
    private String FOOD_TYP_NAME;
    private String IS_DEF;
    private String IMG;

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

    public String getIS_DEF() {
        return IS_DEF;
    }

    public void setIS_DEF(String IS_DEF) {
        this.IS_DEF = IS_DEF;
    }

    public int getFOOD_TYP_ID() {

        return FOOD_TYP_ID;
    }

    public void setFOOD_TYP_ID(int FOOD_TYP_ID) {
        this.FOOD_TYP_ID = FOOD_TYP_ID;
    }

    public String getFOOD_TYP_NAME() {
        return FOOD_TYP_NAME;
    }

    public void setFOOD_TYP_NAME(String FOOD_TYP_NAME) {
        this.FOOD_TYP_NAME = FOOD_TYP_NAME;
    }
}
