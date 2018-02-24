package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 22/1/18.
 */

public class Milestone implements Serializable{
    private int MLT_ID;
    private int RANK_ID;
    private String TYPE;
    private int NUMBER;

    private String RANK_NAME;
    private int currentNumber;

    public int getMLT_ID() {
        return MLT_ID;
    }

    public void setMLT_ID(int MLT_ID) {
        this.MLT_ID = MLT_ID;
    }

    public int getRANK_ID() {
        return RANK_ID;
    }

    public void setRANK_ID(int RANK_ID) {
        this.RANK_ID = RANK_ID;
    }

    public String getRANK_NAME() {
        return RANK_NAME;
    }

    public void setRANK_NAME(String RANK_NAME) {
        this.RANK_NAME = RANK_NAME;
    }

    public int getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(int NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }
}
