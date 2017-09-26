package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 27/8/17.
 */

public class CuisineMO implements Serializable {
    private int FOOD_CSN_ID;
    private String FOOD_CSN_NAME;
    private String IMG;
    private String IS_DEF;

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public int getFOOD_CSN_ID() {
        return FOOD_CSN_ID;
    }

    public void setFOOD_CSN_ID(int FOOD_CSN_ID) {
        this.FOOD_CSN_ID = FOOD_CSN_ID;
    }

    public String getFOOD_CSN_NAME() {
        return FOOD_CSN_NAME;
    }

    public void setFOOD_CSN_NAME(String FOOD_CSN_NAME) {
        this.FOOD_CSN_NAME = FOOD_CSN_NAME;
    }

    public String getIS_DEF() {
        return IS_DEF;
    }

    public void setIS_DEF(String IS_DEF) {
        this.IS_DEF = IS_DEF;
    }
}
