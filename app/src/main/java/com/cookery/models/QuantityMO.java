package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 29/8/17.
 */

public class QuantityMO  implements Serializable {
    private int QTY_ID;
    private String QTY_NAME;
    private String IS_DEF;

    public String getIS_DEF() {
        return IS_DEF;
    }

    public void setIS_DEF(String IS_DEF) {
        this.IS_DEF = IS_DEF;
    }

    public int getQTY_ID() {
        return QTY_ID;
    }

    public void setQTY_ID(int QTY_ID) {
        this.QTY_ID = QTY_ID;
    }

    public String getQTY_NAME() {
        return QTY_NAME;
    }

    public void setQTY_NAME(String QTY_NAME) {
        this.QTY_NAME = QTY_NAME;
    }
}
