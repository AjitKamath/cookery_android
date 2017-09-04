package com.cookery.models;

/**
 * Created by ajit on 27/8/17.
 */

public class RecipeMO {
    private String RCP_NAME;
    private int FOOD_TYP_ID;
    private int FOOD_CSN_ID;
    private int TST_ID;
    private String RCP_PROC;
    private String RCP_PLATING;
    private String RCP_NOTE;

    public String getRCP_NAME() {
        return RCP_NAME;
    }

    public void setRCP_NAME(String RCP_NAME) {
        this.RCP_NAME = RCP_NAME;
    }

    public int getFOOD_TYP_ID() {
        return FOOD_TYP_ID;
    }

    public void setFOOD_TYP_ID(int FOOD_TYP_ID) {
        this.FOOD_TYP_ID = FOOD_TYP_ID;
    }

    public int getFOOD_CSN_ID() {
        return FOOD_CSN_ID;
    }

    public void setFOOD_CSN_ID(int FOOD_CSN_ID) {
        this.FOOD_CSN_ID = FOOD_CSN_ID;
    }

    public int getTST_ID() {
        return TST_ID;
    }

    public void setTST_ID(int TST_ID) {
        this.TST_ID = TST_ID;
    }

    public String getRCP_PROC() {
        return RCP_PROC;
    }

    public void setRCP_PROC(String RCP_PROC) {
        this.RCP_PROC = RCP_PROC;
    }

    public String getRCP_PLATING() {
        return RCP_PLATING;
    }

    public void setRCP_PLATING(String RCP_PLATING) {
        this.RCP_PLATING = RCP_PLATING;
    }

    public String getRCP_NOTE() {
        return RCP_NOTE;
    }

    public void setRCP_NOTE(String RCP_NOTE) {
        this.RCP_NOTE = RCP_NOTE;
    }
}
